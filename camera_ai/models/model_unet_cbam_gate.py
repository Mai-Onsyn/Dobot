# -*- coding: utf-8 -*-
"""
Attention-ResUNet Segmentation Model

Architecture:
  Encoder: Pretrained ResNet-18 -> 5-level features (64->64->128->256->512)
  Bridge:  Attention(512) -> SE / ECA / CBAM / Identity
  Decoder: 4-level upsample + skip connection + ResBlock + Dropout2d (MC Dropout)
  Output:  seg_logit [B, 1, H, W]
"""

import math
import torch
import torch.nn as nn
import torch.nn.functional as F
from torchvision import models

# ================= Domain-Specific Prior Module (Novelty) =================

class MethyleneBlueColorPrior(nn.Module):
    """
    Original Innovation: Methylene Blue Color Prior Module
    Given that Methylene Blue halos are specifically in the blue spectrum, 
    this module acts as a spatial soft-attention mechanism applied directly to the RGB input.
    It enhances the relative weight of the blue channel (index 2 in BGR or RGB depending on loader)
    to suppress yellow mud background noise before deep feature extraction.
    """
    def __init__(self, in_channels=3):
        super().__init__()
        # 1x1 conv to implicitly learn the color space transformations (e.g. RGB->HSV approximations)
        self.color_transform = nn.Sequential(
            nn.Conv2d(in_channels, in_channels, kernel_size=1, groups=in_channels, bias=False),
            nn.LeakyReLU(0.1, inplace=True),
            nn.Conv2d(in_channels, in_channels, kernel_size=1, bias=False),
            nn.Sigmoid()
        )
        
    def forward(self, x):
        # x is [B, 3, H, W]
        # Calculate a spectral attention mask
        color_attention = self.color_transform(x)
        # Apply the attention to boost Methylene Blue relevant pixels
        enhanced_x = x * color_attention + x
        return enhanced_x


# ================= Attention Modules =================

class ChannelAttention(nn.Module):
    """CBAM Channel Attention"""
    def __init__(self, in_planes, ratio=16, use_min_pool=False, residual_gate=False):
        super().__init__()
        self.avg_pool = nn.AdaptiveAvgPool2d(1)
        self.max_pool = nn.AdaptiveMaxPool2d(1)
        self.use_min_pool = use_min_pool
        self.residual_gate = residual_gate
        self.fc = nn.Sequential(
            nn.Conv2d(in_planes, in_planes // ratio, 1, bias=False),
            nn.ReLU(),
            nn.Conv2d(in_planes // ratio, in_planes, 1, bias=False)
        )
        self.sigmoid = nn.Sigmoid()

    def forward(self, x):
        avg_out = self.fc(self.avg_pool(x))
        if self.use_min_pool:
            second_out = self.fc(-self.max_pool(-x))
        else:
            second_out = self.fc(self.max_pool(x))
        ca = self.sigmoid(avg_out + second_out)
        return x * (1 + ca) if self.residual_gate else x * ca


class SpatialAttention(nn.Module):
    """CBAM Spatial Attention (Modified: 3x3 for titration granularity)"""
    def __init__(self, kernel_size=3):
        super().__init__()
        self.conv = nn.Conv2d(2, 1, kernel_size, padding=kernel_size // 2, bias=False)
        self.sigmoid = nn.Sigmoid()

    def forward(self, x):
        avg_out = torch.mean(x, dim=1, keepdim=True)
        max_out, _ = torch.max(x, dim=1, keepdim=True)
        x = torch.cat([avg_out, max_out], dim=1)
        return self.sigmoid(self.conv(x))


class CBAM(nn.Module):
    """CBAM: Channel + Spatial Attention (Woo et al., 2018)"""
    def __init__(self, in_planes, ratio=16, kernel_size=3, residual_gate=False, use_min_pool=False, learnable_alpha=False, channel_residual=False):
        super().__init__()
        self.ca = ChannelAttention(in_planes, ratio, use_min_pool=use_min_pool, residual_gate=channel_residual)
        self.sa = SpatialAttention(kernel_size)
        self.residual_gate = residual_gate
        self.learnable_alpha = learnable_alpha
        if learnable_alpha:
            self.alpha = nn.Parameter(torch.ones(1))

    def forward(self, x):
        x = self.ca(x)  # ChannelAttention now returns attended x directly
        if self.learnable_alpha:
            x = x * (self.alpha + self.sa(x))
        elif self.residual_gate:
            x = x * (1 + self.sa(x))
        else:
            x = x * self.sa(x)
        return x


class SEBlock(nn.Module):
    """Squeeze-and-Excitation: Channel-only Attention (Hu et al., 2017)"""
    def __init__(self, in_planes, ratio=16):
        super().__init__()
        self.avg_pool = nn.AdaptiveAvgPool2d(1)
        self.fc = nn.Sequential(
            nn.Linear(in_planes, in_planes // ratio, bias=False),
            nn.ReLU(inplace=True),
            nn.Linear(in_planes // ratio, in_planes, bias=False),
            nn.Sigmoid()
        )

    def forward(self, x):
        b, c, _, _ = x.size()
        y = self.avg_pool(x).view(b, c)
        y = self.fc(y).view(b, c, 1, 1)
        return x * y


class ECABlock(nn.Module):
    """Efficient Channel Attention (Wang et al., 2020)
    Uses adaptive 1D convolution instead of FC layers."""
    def __init__(self, in_planes, gamma=2, b=1):
        super().__init__()
        # Adaptive kernel size: k = |log2(C)/gamma + b/gamma| (odd)
        k_size = int(abs(math.log2(in_planes) / gamma + b / gamma))
        k_size = k_size if k_size % 2 else k_size + 1

        self.avg_pool = nn.AdaptiveAvgPool2d(1)
        self.conv = nn.Conv1d(1, 1, kernel_size=k_size, padding=k_size // 2, bias=False)
        self.sigmoid = nn.Sigmoid()

    def forward(self, x):
        b, c, _, _ = x.size()
        y = self.avg_pool(x).view(b, 1, c)       # [B, 1, C]
        y = self.conv(y)                           # [B, 1, C]
        y = self.sigmoid(y).view(b, c, 1, 1)      # [B, C, 1, 1]
        return x * y


class SpatialOnlyAttention(nn.Module):
    """Pure Spatial Attention (part of CBAM, used for ablation)"""
    def __init__(self, kernel_size=7):
        super().__init__()
        self.sa = SpatialAttention(kernel_size)

    def forward(self, x):
        return x * self.sa(x)


class ResidualSpatialBlock(nn.Module):
    """Residual Spatial Gating: x * (1 + sa(x))"""
    def __init__(self, kernel_size=3):
        super().__init__()
        self.sa = SpatialAttention(kernel_size)

    def forward(self, x):
        return x * (1 + self.sa(x))


class h_sigmoid(nn.Module):
    def __init__(self, inplace=True):
        super(h_sigmoid, self).__init__()
        self.relu = nn.ReLU6(inplace=inplace)
    def forward(self, x):
        return self.relu(x + 3) / 6


class h_swish(nn.Module):
    def __init__(self, inplace=True):
        super(h_swish, self).__init__()
        self.sigmoid = h_sigmoid(inplace=inplace)
    def forward(self, x):
        return x * self.sigmoid(x)


class CoordAtt(nn.Module):
    """Coordinate Attention (Hou et al., 2021)"""
    def __init__(self, inp, reduction=32, residual_gate=False):
        super(CoordAtt, self).__init__()
        self.pool_h = nn.AdaptiveAvgPool2d((None, 1))
        self.pool_w = nn.AdaptiveAvgPool2d((1, None))
        mip = max(8, inp // reduction)
        self.conv1 = nn.Conv2d(inp, mip, kernel_size=1, stride=1, padding=0)
        self.bn1 = nn.BatchNorm2d(mip)
        self.act = h_swish()
        self.conv_h = nn.Conv2d(mip, inp, kernel_size=1, stride=1, padding=0)
        self.conv_w = nn.Conv2d(mip, inp, kernel_size=1, stride=1, padding=0)
        self.residual_gate = residual_gate

    def forward(self, x):
        identity = x
        n, c, h, w = x.size()
        x_h = self.pool_h(x)
        x_w = self.pool_w(x).permute(0, 1, 3, 2)
        y = torch.cat([x_h, x_w], dim=2)
        y = self.conv1(y)
        y = self.bn1(y)
        y = self.act(y)
        x_h, x_w = torch.split(y, [h, w], dim=2)
        x_w = x_w.permute(0, 1, 3, 2)
        a_h = self.conv_h(x_h).sigmoid()
        a_w = self.conv_w(x_w).sigmoid()
        att = a_w * a_h
        out = identity * (1 + att) if self.residual_gate else identity * att
        return out


class BAM(nn.Module):
    """Bottleneck Attention Module (Park et al., 2018)
    Parallel Channel and Spatial Attention.
    """
    def __init__(self, gate_channel, reduction_ratio=16, dilation_rate=4):
        super(BAM, self).__init__()
        self.channel_att = nn.Sequential(
            nn.AdaptiveAvgPool2d(1),
            nn.Conv2d(gate_channel, gate_channel // reduction_ratio, kernel_size=1),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channel // reduction_ratio, gate_channel, kernel_size=1),
            nn.BatchNorm2d(gate_channel)
        )
        self.spatial_att = nn.Sequential(
            nn.Conv2d(gate_channel, gate_channel // reduction_ratio, kernel_size=1),
            nn.BatchNorm2d(gate_channel // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channel // reduction_ratio, gate_channel // reduction_ratio, kernel_size=3, padding=dilation_rate, dilation=dilation_rate),
            nn.BatchNorm2d(gate_channel // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channel // reduction_ratio, gate_channel // reduction_ratio, kernel_size=3, padding=dilation_rate, dilation=dilation_rate),
            nn.BatchNorm2d(gate_channel // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channel // reduction_ratio, 1, kernel_size=1)
        )

    def forward(self, x):
        channel_att = self.channel_att(x)
        spatial_att = self.spatial_att(x)
        att = torch.sigmoid(channel_att + spatial_att)
        return x * (1 + att)


class TripletAttention(nn.Module):
    """Triplet Attention (Li et al., 2021)
    Captures cross-dimension interaction via three-branch structure.
    Computes attention across (C, H), (C, W), and (H, W) dimensions.
    Reference: 'Rotate to Attend: Convolutional Triplet Attention Module', WACV 2021.
    """
    def __init__(self, kernel_size=7):
        super(TripletAttention, self).__init__()
        pad = kernel_size // 2
        # Branch 1: (C, H) interaction -> permute to (B, W, C, H)
        self.conv_ch = nn.Sequential(
            nn.Conv2d(2, 1, kernel_size, padding=pad, bias=False),
            nn.BatchNorm2d(1),
            nn.Sigmoid()
        )
        # Branch 2: (C, W) interaction -> permute to (B, H, C, W)
        self.conv_cw = nn.Sequential(
            nn.Conv2d(2, 1, kernel_size, padding=pad, bias=False),
            nn.BatchNorm2d(1),
            nn.Sigmoid()
        )
        # Branch 3: (H, W) spatial attention (standard)
        self.conv_hw = nn.Sequential(
            nn.Conv2d(2, 1, kernel_size, padding=pad, bias=False),
            nn.BatchNorm2d(1),
            nn.Sigmoid()
        )

    @staticmethod
    def _pool(x):
        """Channel-wise avg+max pool -> concat"""
        return torch.cat([x.mean(dim=1, keepdim=True), x.max(dim=1, keepdim=True)[0]], dim=1)

    def forward(self, x):
        # x: (B, C, H, W)
        b, c, h, w = x.size()

        # Branch 1: permute (B,C,H,W) -> (B,W,C,H), attend, permute back
        x1 = x.permute(0, 3, 1, 2)   # (B, W, C, H)
        x1_att = self.conv_ch(self._pool(x1))  # (B, 1, C, H)
        x1 = x1 * x1_att
        x1 = x1.permute(0, 2, 3, 1)  # back to (B, C, H, W)

        # Branch 2: permute (B,C,H,W) -> (B,H,C,W), attend, permute back
        x2 = x.permute(0, 2, 1, 3)   # (B, H, C, W)
        x2_att = self.conv_cw(self._pool(x2))  # (B, 1, C, W)
        x2 = x2 * x2_att
        x2 = x2.permute(0, 2, 1, 3)  # back to (B, C, H, W)

        # Branch 3: standard (H,W) spatial attention
        x3_att = self.conv_hw(self._pool(x))   # (B, 1, H, W)
        x3 = x * x3_att

        # Average the three branches
        return (x1 + x2 + x3) / 3.0


class SimAM(nn.Module):
    """SimAM: A Simple, Parameter-Free Attention Module (Yang et al., 2021)
    Estimates 3D attention weights without any learnable parameters.
    Based on neuroscience theory of neuronal energy.
    Reference: 'SimAM: A Simple, Parameter-Free Attention Module for CNNs', ICML 2021.
    """
    def __init__(self, e_lambda=1e-4):
        super(SimAM, self).__init__()
        self.e_lambda = e_lambda

    def forward(self, x):
        # x: (B, C, H, W)
        b, c, h, w = x.size()
        n = h * w - 1  # number of spatial neighbors

        # Mean of x across spatial dims for each channel
        x_mean = x.mean(dim=[2, 3], keepdim=True)  # (B, C, 1, 1)

        # Variance-like term
        d = (x - x_mean).pow(2)  # (B, C, H, W)
        v = d.sum(dim=[2, 3], keepdim=True) / n  # (B, C, 1, 1)

        # Energy-based attention
        e_inv = d / (4 * (v + self.e_lambda)) + 0.5

        # Sigmoid to get attention weights
        return x * torch.sigmoid(e_inv)


# ================= Fca-CBAM (Frequency + Spatial) =================

def get_dct_filter(tile_size, freq_h, freq_w, n_channels):
    """Generate 2D DCT filter weights"""
    import math
    dct_filter = torch.zeros(tile_size, tile_size)
    c_h = 1/math.sqrt(tile_size) if freq_h == 0 else math.sqrt(2/tile_size)
    c_w = 1/math.sqrt(tile_size) if freq_w == 0 else math.sqrt(2/tile_size)
    for i in range(tile_size):
        for j in range(tile_size):
            dct_filter[i, j] = c_h * c_w * math.cos(math.pi * freq_h * (i + 0.5) / tile_size) * \
                             math.cos(math.pi * freq_w * (j + 0.5) / tile_size)
    # Expand to (1, n_channels, tile_size, tile_size)
    return dct_filter.view(1, 1, tile_size, tile_size).repeat(1, n_channels, 1, 1)


class MultiSpectralAttentionLayer(nn.Module):
    """
    Physics-Driven Multi-Spectral Channel Attention.

    Core Innovation: Instead of FcaNet's approach (one DCT frequency per channel),
    this module computes a LEARNABLE WEIGHTED COMBINATION of three frequency tiers,
    each corresponding to a physical signal type in methylene blue halo detection:

      Tier 1 (Low freq):  DC component = Global Average Pooling
                          → Captures overall color tone (blue halo presence)
      Tier 2 (Mid freq):  (0,1) + (1,0) average
                          → Target Edge: Captures gradual color transitions (halo boundary)
      Tier 3 (High freq): (0,2) + (2,0) average
    """
    def __init__(self, channel, dct_h, dct_w, reduction=16, freq_mode='physical', freq_init='physics'):
        super(MultiSpectralAttentionLayer, self).__init__()
        self.dct_h = dct_h
        self.dct_w = dct_w
        self.freq_mode = freq_mode

        # Tier 1: DC component (0,0) — identical to Global Average Pooling
        self.register_buffer('filter_low', get_dct_filter(dct_h, 0, 0, channel))

        if freq_mode == 'fca_data':
            # Simulating FcaNet: Using standard high-energy DCT indices (e.g., 0, 1, 2) 
            self.register_buffer('filter_mid', get_dct_filter(dct_h, 0, 1, channel))
            self.register_buffer('filter_high', get_dct_filter(dct_h, 0, 2, channel))
        elif freq_mode == 'random_3':
            # Simulating a non-physical random selection of 3 frequency components
            self.register_buffer('filter_low',  get_dct_filter(dct_h, 3, 3, channel))
            self.register_buffer('filter_mid',  get_dct_filter(dct_h, 4, 4, channel))
            self.register_buffer('filter_high', get_dct_filter(dct_h, 5, 5, channel))
        else:
            # Tier 2: Low-mid frequencies — average of (0,1) and (1,0) bases
            f01 = get_dct_filter(dct_h, 0, 1, channel)
            f10 = get_dct_filter(dct_h, 1, 0, channel)
            self.register_buffer('filter_mid', (f01 + f10) / 2.0)

            # Tier 3: High frequencies — average of (0,2) and (2,0) bases
            f02 = get_dct_filter(dct_h, 0, 2, channel)
            f20 = get_dct_filter(dct_h, 2, 0, channel)
            self.register_buffer('filter_high', (f02 + f20) / 2.0)

        # Handle weight initialization
        if freq_init == 'physics':
            init_val = [2.0, 0.5, -0.5]
        elif freq_init == 'inverse':
            init_val = [-0.5, 0.5, 2.0]
        else: # uniform
            init_val = [0.0, 0.0, 0.0]
        
        self.freq_weights = nn.Parameter(torch.tensor(init_val))

        self.fc = nn.Sequential(
            nn.Linear(channel, channel // reduction, bias=False),
            nn.ReLU(inplace=True),
            nn.Linear(channel // reduction, channel, bias=False),
            nn.Sigmoid()
        )

    def forward(self, x):
        n, c, h, w = x.size()
        if h != self.dct_h or w != self.dct_w:
            x_pooled = F.adaptive_avg_pool2d(x, (self.dct_h, self.dct_w))
        else:
            x_pooled = x

        # Spectral pooling for each physical tier: (n, c)
        pool_low  = torch.sum(x_pooled * self.filter_low,  dim=(2, 3))
        pool_mid  = torch.sum(x_pooled * self.filter_mid,  dim=(2, 3))
        pool_high = torch.sum(x_pooled * self.filter_high, dim=(2, 3))

        # Handle Ablation Modes (masking out tiers)
        if self.freq_mode == 'low_only':
            pool_mid = pool_mid * 0.0
            pool_high = pool_high * 0.0
        elif self.freq_mode == 'mid_only':
            pool_low = pool_low * 0.0
            pool_high = pool_high * 0.0
        elif self.freq_mode == 'high_only':
            pool_low = pool_low * 0.0
            pool_mid = pool_mid * 0.0

        # Learnable weighted combination with softmax normalization
        w = torch.softmax(self.freq_weights, dim=0)
        spectral_pool = w[0] * pool_low + w[1] * pool_mid + w[2] * pool_high

        y = self.fc(spectral_pool).view(n, c, 1, 1)
        return x * y


class FcaCBAM(nn.Module):
    """
    PhyFca: Physics-guided Frequency Channel Attention + Spatial Attention.
    通道注意力：3档DCT物理加权，针对亚甲蓝任务低频色调主导设计。
    空间注意力：3×3 kernel（针对局部小目标），残差门控 x*(1+sa) 保留原始信号。
    """
    def __init__(self, gate_channels, reduction_ratio=16, pool_size=16, freq_mode='physical', freq_init='physics', use_spatial=True, residual_gate=True):
        super(FcaCBAM, self).__init__()
        self.ca = MultiSpectralAttentionLayer(gate_channels, pool_size, pool_size, reduction_ratio, freq_mode, freq_init)
        self.use_spatial = use_spatial
        self.residual_gate = residual_gate
        if use_spatial:
            self.sa = SpatialAttention(kernel_size=3)

    def forward(self, x):
        x = self.ca(x)
        if self.use_spatial:
            x = x * (1 + self.sa(x)) if self.residual_gate else x * self.sa(x)
        return x


class FcaBAM(nn.Module):
    def __init__(self, gate_channels, reduction_ratio=16, pool_size=16, dilation_rate=4, freq_mode='physical', freq_init='physics'):
        super(FcaBAM, self).__init__()
        self.spectral_ca = MultiSpectralAttentionLayer(
            gate_channels, pool_size, pool_size, reduction_ratio, freq_mode, freq_init
        )
        self.spatial_att = nn.Sequential(
            nn.Conv2d(gate_channels, gate_channels // reduction_ratio, kernel_size=1),
            nn.BatchNorm2d(gate_channels // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channels // reduction_ratio, gate_channels // reduction_ratio,
                      kernel_size=3, padding=dilation_rate, dilation=dilation_rate),
            nn.BatchNorm2d(gate_channels // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channels // reduction_ratio, gate_channels // reduction_ratio,
                      kernel_size=3, padding=dilation_rate, dilation=dilation_rate),
            nn.BatchNorm2d(gate_channels // reduction_ratio),
            nn.ReLU(inplace=True),
            nn.Conv2d(gate_channels // reduction_ratio, 1, kernel_size=1)
        )

    def forward(self, x):
        ca = self.spectral_ca(x) / (x + 1e-8)
        ca = ca.mean(dim=(2, 3), keepdim=True)
        sp = self.spatial_att(x)
        att = torch.sigmoid(ca + sp)
        return x * (1 + att)


# ================= Attention Gate (Oktay et al., 2018) =================

class AttentionGate(nn.Module):
    """Attention Gate for skip connections.
    Uses gating signal from the decoder to filter skip connection features,
    suppressing irrelevant regions (e.g., mud stains) before concatenation.
    Reference: Oktay et al., 'Attention U-Net', arXiv:1804.03999, 2018.
    """
    def __init__(self, F_g, F_l, F_int):
        """
        Args:
            F_g: channels of gating signal (from deeper decoder level)
            F_l: channels of skip connection feature (from encoder)
            F_int: intermediate channels for attention computation
        """
        super().__init__()
        self.W_g = nn.Sequential(
            nn.Conv2d(F_g, F_int, 1, bias=False),
            nn.BatchNorm2d(F_int)
        )
        self.W_x = nn.Sequential(
            nn.Conv2d(F_l, F_int, 1, bias=False),
            nn.BatchNorm2d(F_int)
        )
        self.psi = nn.Sequential(
            nn.Conv2d(F_int, 1, 1, bias=False),
            nn.BatchNorm2d(1),
            nn.Sigmoid()
        )
        self.relu = nn.ReLU(inplace=True)

    def forward(self, g, x):
        """g: gating signal (decoder), x: skip connection feature (encoder)"""
        g1 = self.W_g(g)
        x1 = self.W_x(x)
        if g1.shape[2:] != x1.shape[2:]:
            g1 = F.interpolate(g1, size=x1.shape[2:], mode='bilinear', align_corners=False)
        psi = self.relu(g1 + x1)
        psi = self.psi(psi)
        return x * psi


# ================= ResBlock =================

class ResBlock(nn.Module):
    def __init__(self, in_channels, out_channels):
        super().__init__()
        self.conv1 = nn.Conv2d(in_channels, out_channels, 3, padding=1, bias=False)
        self.bn1 = nn.BatchNorm2d(out_channels)
        self.relu = nn.ReLU(inplace=True)
        self.conv2 = nn.Conv2d(out_channels, out_channels, 3, padding=1, bias=False)
        self.bn2 = nn.BatchNorm2d(out_channels)

        self.shortcut = nn.Sequential()
        if in_channels != out_channels:
            self.shortcut = nn.Sequential(
                nn.Conv2d(in_channels, out_channels, 1, bias=False),
                nn.BatchNorm2d(out_channels)
            )

    def forward(self, x):
        out = self.relu(self.bn1(self.conv1(x)))
        out = self.bn2(self.conv2(out))
        out += self.shortcut(x)
        return self.relu(out)


# ================= Attention-ResUNet =================

class ResUNet_CBAM_Gated(nn.Module):
    def __init__(self, in_channels=3, out_channels=1, gate_type='none', gate_dropout=0.3,
                 use_color_prior=False, use_encoder_eca=False, use_attention_gate=False,
                 use_deep_supervision=False, attention_type='fcacbam', freq_mode='physical', freq_init='physics', use_spatial=True,
                 use_decoder_phyfca=False, residual_gate=True, use_min_pool=False, learnable_alpha=False, channel_residual=False, use_encoder_spatial=False):
        super(ResUNet_CBAM_Gated, self).__init__()

        self.gate_type = 'none'
        self._attention_maps = {}
        self.use_color_prior = use_color_prior
        self.use_deep_supervision = use_deep_supervision

        if use_color_prior:
            self.color_prior = MethyleneBlueColorPrior(in_channels=in_channels)
        else:
            self.color_prior = None

        self.base_model = models.resnet18(weights='IMAGENET1K_V1')

        self.encoder0 = nn.Sequential(
            self.base_model.conv1, self.base_model.bn1,
            self.base_model.relu, self.base_model.maxpool
        )
        self.encoder1 = self.base_model.layer1
        self.encoder2 = self.base_model.layer2
        self.encoder3 = self.base_model.layer3
        self.encoder4 = self.base_model.layer4

        if use_encoder_eca:
            self.enc_eca1 = ECABlock(64)
            self.enc_eca2 = ECABlock(128)
            self.enc_eca3 = ECABlock(256)
        elif use_encoder_spatial:
            self.enc_eca1 = ResidualSpatialBlock(kernel_size=3)
            self.enc_eca2 = ResidualSpatialBlock(kernel_size=3)
            self.enc_eca3 = ResidualSpatialBlock(kernel_size=3)
        else:
            self.enc_eca1 = nn.Identity()
            self.enc_eca2 = nn.Identity()
            self.enc_eca3 = nn.Identity()

        if attention_type == 'none':
            self.cbam = nn.Identity()
        elif attention_type == 'se':
            self.cbam = SEBlock(512)
        elif attention_type == 'eca':
            self.cbam = ECABlock(512)
        elif attention_type == 'bam':
            self.cbam = BAM(512)
        elif attention_type == 'coord':
            self.cbam = CoordAtt(512, residual_gate=residual_gate)
        elif attention_type == 'spatial':
            self.cbam = SpatialOnlyAttention()
        elif attention_type == 'triplet':
            self.cbam = TripletAttention()
        elif attention_type == 'simam':
            self.cbam = SimAM()
        elif attention_type.lower() == 'fcacbam':
            self.cbam = FcaCBAM(512, pool_size=16, freq_mode=freq_mode, freq_init=freq_init, use_spatial=use_spatial, residual_gate=residual_gate)
        elif attention_type.lower() == 'fcabam':
            self.cbam = FcaBAM(512, pool_size=16, freq_mode=freq_mode, freq_init=freq_init)
        else:
            self.cbam = CBAM(512, residual_gate=residual_gate, use_min_pool=use_min_pool, learnable_alpha=learnable_alpha, channel_residual=channel_residual)

        if use_attention_gate:
            self.ag4 = AttentionGate(F_g=256, F_l=256, F_int=128)
            self.ag3 = AttentionGate(F_g=128, F_l=128, F_int=64)
            self.ag2 = AttentionGate(F_g=64,  F_l=64,  F_int=32)
        else:
            self.ag4 = None
            self.ag3 = None
            self.ag2 = None
        
        self.up4 = nn.ConvTranspose2d(512, 256, kernel_size=2, stride=2)
        self.decoder4 = ResBlock(256 + 256, 256)
        self.drop4 = nn.Dropout2d(0.15)

        self.up3 = nn.ConvTranspose2d(256, 128, kernel_size=2, stride=2)
        self.decoder3 = ResBlock(128 + 128, 128)
        self.drop3 = nn.Dropout2d(0.15)

        self.up2 = nn.ConvTranspose2d(128, 64, kernel_size=2, stride=2)
        self.decoder2 = ResBlock(64 + 64, 64)
        self.drop2 = nn.Dropout2d(0.1)

        self.up1 = nn.ConvTranspose2d(64, 64, kernel_size=2, stride=2)
        self.decoder1 = ResBlock(64, 64)

        # 多尺度 Decoder PhyFca（可选）
        # d4(256ch): 低频主导，粗定位；d3(128ch): 中频边界；d2(64ch): 高频细节
        if use_decoder_phyfca:
            self.dec_phyfca4 = FcaCBAM(256, reduction_ratio=16, pool_size=16,
                                        freq_mode=freq_mode, freq_init=freq_init, use_spatial=True)
            self.dec_phyfca3 = FcaCBAM(128, reduction_ratio=8,  pool_size=16,
                                        freq_mode=freq_mode, freq_init=freq_init, use_spatial=True)
            self.dec_phyfca2 = FcaCBAM(64,  reduction_ratio=8,  pool_size=16,
                                        freq_mode=freq_mode, freq_init=freq_init, use_spatial=True)
        else:
            self.dec_phyfca4 = nn.Identity()
            self.dec_phyfca3 = nn.Identity()
            self.dec_phyfca2 = nn.Identity()

        self.final_upsample = nn.Sequential(
            nn.ConvTranspose2d(64, 32, kernel_size=2, stride=2),
            nn.ReLU(),
            nn.Dropout2d(0.15),
            nn.Conv2d(32, out_channels, kernel_size=1)
        )

        if use_deep_supervision:
            self.aux_head4 = nn.Conv2d(256, out_channels, 1)
            self.aux_head3 = nn.Conv2d(128, out_channels, 1)
            self.aux_head2 = nn.Conv2d(64, out_channels, 1)
        else:
            self.aux_head4 = None
            self.aux_head3 = None
            self.aux_head2 = None

    def forward(self, x, return_attention=False):
        if self.color_prior is not None:
            x = self.color_prior(x)

        x0 = self.encoder0(x)
        x1 = self.enc_eca1(self.encoder1(x0))
        x2 = self.enc_eca2(self.encoder2(x1))
        x3 = self.enc_eca3(self.encoder3(x2))
        x4 = self.encoder4(x3)

        if return_attention and isinstance(self.cbam, CBAM):
            b, attention_maps = self._forward_cbam_with_attention(x4)
        else:
            b = self.cbam(x4)
            attention_maps = None

        d4 = self.up4(b)
        if d4.size() != x3.size():
            d4 = F.interpolate(d4, size=x3.shape[2:], mode='bilinear', align_corners=False)
        x3_skip = self.ag4(g=d4, x=x3) if self.ag4 is not None else x3
        d4 = torch.cat([d4, x3_skip], dim=1)
        d4 = self.dec_phyfca4(self.drop4(self.decoder4(d4)))

        d3 = self.up3(d4)
        if d3.size() != x2.size():
            d3 = F.interpolate(d3, size=x2.shape[2:], mode='bilinear', align_corners=False)
        x2_skip = self.ag3(g=d3, x=x2) if self.ag3 is not None else x2
        d3 = torch.cat([d3, x2_skip], dim=1)
        d3 = self.dec_phyfca3(self.drop3(self.decoder3(d3)))

        d2 = self.up2(d3)
        if d2.size() != x1.size():
            d2 = F.interpolate(d2, size=x1.shape[2:], mode='bilinear', align_corners=False)
        x1_skip = self.ag2(g=d2, x=x1) if self.ag2 is not None else x1
        d2 = torch.cat([d2, x1_skip], dim=1)
        d2 = self.dec_phyfca2(self.drop2(self.decoder2(d2)))

        d1 = self.up1(d2)
        seg_logit = self.final_upsample(d1)

        if seg_logit.size()[2:] != x.size()[2:]:
            seg_logit = F.interpolate(seg_logit, size=x.shape[2:], mode='bilinear', align_corners=False)

        aux_outputs = None
        if self.training and self.use_deep_supervision and self.aux_head4 is not None:
            target_size = x.shape[2:]
            aux4 = F.interpolate(self.aux_head4(d4), size=target_size, mode='bilinear', align_corners=False)
            aux3 = F.interpolate(self.aux_head3(d3), size=target_size, mode='bilinear', align_corners=False)
            aux2 = F.interpolate(self.aux_head2(d2), size=target_size, mode='bilinear', align_corners=False)
            aux_outputs = [aux4, aux3, aux2]

        if return_attention:
            return seg_logit, aux_outputs, attention_maps
        return seg_logit, aux_outputs

    def _forward_cbam_with_attention(self, x):
        channel_att = self.cbam.ca(x)
        x_ca = x * channel_att
        spatial_att = self.cbam.sa(x_ca)
        result = x_ca * spatial_att

        attention_maps = {
            'channel_attention': channel_att.squeeze(-1).squeeze(-1),
            'spatial_attention': spatial_att.squeeze(1),
            'feature_before_cbam': x,
            'feature_after_cbam': result,
        }
        return result, attention_maps

    def predict(self, x):
        seg_logit, _ = self.forward(x)
        return torch.sigmoid(seg_logit)

    def predict_with_uncertainty(self, x, T=10, area_thresh=0.001, uncertain_thresh=0.005):
        was_training = self.training
        self.eval()

        for m in self.modules():
            if isinstance(m, (nn.Dropout, nn.Dropout2d)):
                m.train()

        seg_preds = []
        area_ratios = []
        total_pixels = x.shape[2] * x.shape[3]

        with torch.no_grad():
            for _ in range(T):
                seg_logit, _ = self.forward(x)
                seg_prob = torch.sigmoid(seg_logit)
                seg_preds.append(seg_prob)
                area = (seg_prob > 0.5).float().sum(dim=(1, 2, 3)) / total_pixels
                area_ratios.append(area)

        if was_training:
            self.train()

        seg_stack = torch.stack(seg_preds, dim=0)
        seg_mean = seg_stack.mean(dim=0)
        seg_uncertainty = seg_stack.var(dim=0)

        area_stack = torch.stack(area_ratios, dim=0)
        area_mean = area_stack.mean(dim=0)
        area_std = area_stack.std(dim=0)

        decisions = []
        for i in range(x.shape[0]):
            if area_std[i].item() > uncertain_thresh:
                decisions.append('uncertain')
            elif area_mean[i].item() >= area_thresh:
                decisions.append('positive')
            else:
                decisions.append('negative')

        return {
            'seg_prob': seg_mean,
            'seg_uncertainty': seg_uncertainty,
            'area_mean': area_mean,
            'area_std': area_std,
            'decision': decisions,
        }

    def predict_with_details(self, x):
        seg_logit, _, attention_maps = self.forward(x, return_attention=True)
        seg_prob = torch.sigmoid(seg_logit)
        return {'seg_prob': seg_prob, 'attention_maps': attention_maps}


# ================= Factory =================

def create_model(use_cbam=True, attention_type=None, use_color_prior=False,
                 use_encoder_eca=False, use_attention_gate=False,
                 use_deep_supervision=False, freq_mode='physical', freq_init='physics',
                 use_spatial=True, use_decoder_phyfca=False, residual_gate=True, use_min_pool=False, learnable_alpha=False, channel_residual=False, use_encoder_spatial=False):
    model = ResUNet_CBAM_Gated(
        in_channels=3, out_channels=1,
        use_color_prior=use_color_prior,
        use_encoder_eca=use_encoder_eca,
        use_attention_gate=use_attention_gate,
        use_deep_supervision=use_deep_supervision,
        attention_type=attention_type if attention_type else ('fcacbam' if use_cbam else 'none'),
        freq_mode=freq_mode,
        freq_init=freq_init,
        use_spatial=use_spatial,
        use_decoder_phyfca=use_decoder_phyfca,
        residual_gate=residual_gate,
        use_min_pool=use_min_pool,
        learnable_alpha=learnable_alpha,
        channel_residual=channel_residual,
        use_encoder_spatial=use_encoder_spatial,
    )
    return model