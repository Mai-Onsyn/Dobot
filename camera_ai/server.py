# -*- coding: utf-8 -*-
# MB 滴定终点检测 HTTP API 服务
# 运行方式: python server.py

import os
import sys
import io
import base64
import numpy as np
import torch
from PIL import Image
from scipy import ndimage
from flask import Flask, request, jsonify

# ================= 配置与初始化 =================

_HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, _HERE)

# 导入模型结构
try:
    from models.model_unet_cbam_gate import create_model
except ImportError:
    raise RuntimeError("无法导入模型，请确保 models/model_unet_cbam_gate.py 存在于同级目录")

IMG_SIZE = 512
MEAN = np.array([0.485, 0.456, 0.406], dtype=np.float32)
STD  = np.array([0.229, 0.224, 0.225], dtype=np.float32)
DEFAULT_WEIGHT = os.path.join(_HERE, "weights", "T2_VarI_Bottleneck_Enc_f0.pth")

app = Flask(__name__)

# 全局模型与设备变量
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = None

def load_global_model():
    global model
    if not os.path.exists(DEFAULT_WEIGHT):
        print(f"[警告] 找不到权重文件: {DEFAULT_WEIGHT}")
        return
    
    print(f"正在加载模型至 {device} ...")
    m = create_model(attention_type="cbam", use_color_prior=True,
                     residual_gate=True, use_encoder_spatial=True)
    m.load_state_dict(torch.load(DEFAULT_WEIGHT, map_location=device))
    model = m.to(device).eval()
    print("模型加载完成，服务准备就绪。")

# ================= 核心推理逻辑 =================

def run_predict(pil_image, prob_thresh=0.5, area_thresh=0.001):
    """纯张量推理逻辑，不涉及任何外部显示或 cv2"""
    # 预处理
    img = pil_image.convert("RGB").resize((IMG_SIZE, IMG_SIZE), Image.BILINEAR)
    arr = (np.array(img, dtype=np.float32) / 255.0 - MEAN) / STD
    t = torch.from_numpy(arr.transpose(2, 0, 1)).float().unsqueeze(0).to(device)
    
    # 推理
    with torch.no_grad():
        logit, _ = model(t)
        prob = torch.sigmoid(logit).squeeze().cpu().numpy()

    # 后处理与连通域分析 (替代 cv2 的寻边逻辑)
    binary = (prob > prob_thresh).astype(np.uint8)
    labeled, n = ndimage.label(binary)
    
    # 未检测到任何蓝色区域
    if n == 0:
        return {
            "status": "negative",
            "confidence": float(prob.max()),
            "area_ratio": 0.0
        }

    # 寻找最大的连通区域
    sizes = ndimage.sum(binary, labeled, range(1, n + 1))
    cc = (labeled == int(np.argmax(sizes)) + 1).astype(np.uint8)
    ar = float(cc.sum()) / (IMG_SIZE * IMG_SIZE)
    
    # 判断结果
    result = "positive" if ar > area_thresh else "negative"
    conf = float(prob[cc.astype(bool)].mean()) if result == "positive" else float(prob.max())
    
    return {
        "status": result,
        "confidence": conf,
        "area_ratio": ar
    }

# ================= HTTP 路由 =================

@app.route('/predict', methods=['POST'])
def predict_endpoint():
    if model is None:
        return jsonify({"status": "error", "message": "模型未正确加载"}), 500

    try:
        data = request.get_json()
        if not data or 'image' not in data:
            return jsonify({"status": "error", "message": "JSON 缺失 'image' 字段"}), 400

        # 获取参数 (允许 Kotlin 动态覆盖阈值，否则使用默认值)
        prob_thresh = float(data.get('prob_thresh', 0.5))
        area_thresh = float(data.get('area_thresh', 0.001))

        # 1. Base64 解码并转为 PIL Image
        img_data = base64.b64decode(data['image'])
        pil_img = Image.open(io.BytesIO(img_data))

        # 2. 运行推理
        result = run_predict(pil_img, prob_thresh, area_thresh)
        result["message"] = "success"

        return jsonify(result), 200

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# ================= 启动服务 =================

if __name__ == "__main__":
    load_global_model()
    # 默认运行在 5000 端口
    app.run(host="0.0.0.0", port=32256, debug=False)