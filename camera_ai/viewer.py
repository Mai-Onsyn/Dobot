# -*- coding: utf-8 -*-
# MB 滴定终点检测工具
# 用法: python viewer.py
# 空格键拍照，首次使用需框选滤纸ROI区域

import datetime
import json
import os
import sys
import threading
import tkinter as tk
from tkinter import filedialog, messagebox

import cv2
import numpy as np
import torch
from PIL import Image, ImageTk
from scipy import ndimage

_HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, _HERE)
from models.model_unet_cbam_gate import create_model

IMG_SIZE    = 512
MEAN        = np.array([0.485, 0.456, 0.406], dtype=np.float32)
STD         = np.array([0.229, 0.224, 0.225], dtype=np.float32)
PROB_THRESH = 0.5
AREA_THRESH = 0.001
PANEL       = 260
ROI_FILE    = os.path.join(_HERE, "roi_config.json")
DEFAULT_WEIGHT = os.path.join(_HERE, "weights", "T2_VarI_Bottleneck_Enc_f0.pth")

_MVS_IMPORT  = r"C:\Win_GY_Code\MVS\Python\MvImport"
_MVS_RUNTIME = r"C:\Program Files (x86)\Common Files\MVS\Runtime\Win64_x64"
if _MVS_IMPORT not in sys.path:
    sys.path.insert(0, _MVS_IMPORT)
if _MVS_RUNTIME not in os.environ.get("PATH", ""):
    os.environ["PATH"] = _MVS_RUNTIME + os.pathsep + os.environ.get("PATH", "")
if hasattr(os, "add_dll_directory"):
    try:
        os.add_dll_directory(_MVS_RUNTIME)
    except OSError:
        pass
try:
    from ctypes import *
    from MvCameraControl_class import *
    _MVS_AVAILABLE = True
except (ImportError, OSError):
    _MVS_AVAILABLE = False


def _hik_grab_one():
    MvCamera.MV_CC_Initialize()
    deviceList = MV_CC_DEVICE_INFO_LIST()
    ret = MvCamera.MV_CC_EnumDevices(MV_GIGE_DEVICE | MV_USB_DEVICE, deviceList)
    if ret != 0 or deviceList.nDeviceNum == 0:
        MvCamera.MV_CC_Finalize()
        raise RuntimeError("未找到海康相机，请检查连接")

    cam = MvCamera()
    dev_info = cast(deviceList.pDeviceInfo[0], POINTER(MV_CC_DEVICE_INFO)).contents
    if cam.MV_CC_CreateHandle(dev_info) != 0:
        MvCamera.MV_CC_Finalize()
        raise RuntimeError("创建相机句柄失败")

    try:
        if cam.MV_CC_OpenDevice(MV_ACCESS_Exclusive, 0) != 0:
            raise RuntimeError("打开相机失败")
        cam.MV_CC_SetEnumValue("TriggerMode", MV_TRIGGER_MODE_OFF)
        cam.MV_CC_SetEnumValue("ExposureAuto", 2)
        cam.MV_CC_SetEnumValue("GainAuto",     2)
        if cam.MV_CC_StartGrabbing() != 0:
            raise RuntimeError("开始取流失败")

        # 丢弃前几帧等曝光稳定
        stDrop = MV_FRAME_OUT()
        memset(byref(stDrop), 0, sizeof(stDrop))
        for _ in range(5):
            ret = cam.MV_CC_GetImageBuffer(stDrop, 1000)
            if ret == 0 and stDrop.pBufAddr is not None:
                cam.MV_CC_FreeImageBuffer(stDrop)

        stFrame = MV_FRAME_OUT()
        memset(byref(stFrame), 0, sizeof(stFrame))
        ret = cam.MV_CC_GetImageBuffer(stFrame, 3000)
        if ret != 0 or stFrame.pBufAddr is None:
            raise RuntimeError(f"取帧失败 ret=0x{ret:x}")

        w, h = stFrame.stFrameInfo.nWidth, stFrame.stFrameInfo.nHeight
        buf_len = w * h * 3
        dst_buf = (c_ubyte * buf_len)()

        conv = MV_CC_PIXEL_CONVERT_PARAM_EX()
        memset(byref(conv), 0, sizeof(conv))
        conv.nWidth         = w
        conv.nHeight        = h
        conv.pSrcData       = stFrame.pBufAddr
        conv.nSrcDataLen    = stFrame.stFrameInfo.nFrameLen
        conv.enSrcPixelType = stFrame.stFrameInfo.enPixelType
        conv.enDstPixelType = PixelType_Gvsp_RGB8_Packed
        conv.pDstBuffer     = dst_buf
        conv.nDstBufferSize = buf_len
        cam.MV_CC_ConvertPixelTypeEx(conv)
        cam.MV_CC_FreeImageBuffer(stFrame)

        arr = np.frombuffer(dst_buf, dtype=np.uint8).reshape(h, w, 3)
        pil = Image.fromarray(arr)
    finally:
        cam.MV_CC_StopGrabbing()
        cam.MV_CC_CloseDevice()
        cam.MV_CC_DestroyHandle()
        MvCamera.MV_CC_Finalize()

    return pil


def load_roi():
    if os.path.exists(ROI_FILE):
        d = json.load(open(ROI_FILE))
        return (d["x1"], d["y1"], d["x2"], d["y2"])
    return None

def save_roi(roi):
    json.dump({"x1": roi[0], "y1": roi[1], "x2": roi[2], "y2": roi[3]},
              open(ROI_FILE, "w"))


def load_model(weight_path, device):
    m = create_model(attention_type="cbam", use_color_prior=True,
                     residual_gate=True, use_encoder_spatial=True)
    m.load_state_dict(torch.load(weight_path, map_location=device))
    return m.to(device).eval()


def run_predict(model, device, pil_image, roi=None):
    if roi:
        pil_image = pil_image.crop(roi)
    img = pil_image.convert("RGB").resize((IMG_SIZE, IMG_SIZE), Image.BILINEAR)
    arr = (np.array(img, dtype=np.float32) / 255.0 - MEAN) / STD
    t = torch.from_numpy(arr.transpose(2, 0, 1)).float().unsqueeze(0).to(device)
    with torch.no_grad():
        logit, _ = model(t)
        prob = torch.sigmoid(logit).squeeze().cpu().numpy()

    binary = (prob > PROB_THRESH).astype(np.uint8)
    labeled, n = ndimage.label(binary)
    if n == 0:
        return dict(result="negative", confidence=float(prob.max()),
                    area_ratio=0.0, prob=prob, mask=np.zeros_like(binary),
                    crop=pil_image)

    sizes = ndimage.sum(binary, labeled, range(1, n + 1))
    cc = (labeled == int(np.argmax(sizes)) + 1).astype(np.uint8)
    ar = float(cc.sum()) / (IMG_SIZE * IMG_SIZE)
    result = "positive" if ar > AREA_THRESH else "negative"
    conf = float(prob[cc.astype(bool)].mean()) if result == "positive" else float(prob.max())
    return dict(result=result, confidence=conf, area_ratio=ar,
                prob=prob, mask=cc, crop=pil_image)


def make_panels(res):
    orig = np.array(res["crop"].convert("RGB").resize((IMG_SIZE, IMG_SIZE), Image.BILINEAR))
    heat = cv2.cvtColor(cv2.applyColorMap((res["prob"]*255).astype(np.uint8),
                                          cv2.COLORMAP_JET), cv2.COLOR_BGR2RGB)
    overlay = orig.copy()
    blue = np.zeros_like(orig)
    blue[res["mask"].astype(bool)] = [30, 100, 240]
    overlay = cv2.addWeighted(overlay, 0.55, blue, 0.45, 0)
    return orig, heat, overlay


def to_tk(arr, size):
    return ImageTk.PhotoImage(Image.fromarray(arr).resize((size, size), Image.BILINEAR))


class RoiSelector(tk.Toplevel):
    MAX_W, MAX_H = 900, 650

    def __init__(self, parent, pil_image):
        super().__init__(parent)
        self.title("框选ROI — 拖拽选定滤纸区域，然后点「确认」")
        self.resizable(False, False)
        self.grab_set()

        self._orig = pil_image
        self._ow, self._oh = pil_image.size
        self.result = None

        scale = min(self.MAX_W / self._ow, self.MAX_H / self._oh, 1.0)
        self._cw = int(self._ow * scale)
        self._ch = int(self._oh * scale)
        self._scale = scale

        disp = pil_image.resize((self._cw, self._ch), Image.BILINEAR)
        self._tk_img = ImageTk.PhotoImage(disp)

        self._canvas = tk.Canvas(self, width=self._cw, height=self._ch,
                                 cursor="crosshair")
        self._canvas.pack()
        self._canvas.create_image(0, 0, anchor=tk.NW, image=self._tk_img)

        tk.Label(self, text="拖拽画框 → 点确认保存；点跳过则不裁剪",
                 font=("微软雅黑", 10), fg="#555").pack(pady=4)

        row = tk.Frame(self)
        row.pack(pady=(0, 8))
        tk.Button(row, text="确认", width=10, command=self._confirm,
                  bg="#3a7bd5", fg="white", relief=tk.FLAT).pack(side=tk.LEFT, padx=6)
        tk.Button(row, text="跳过（不裁剪）", width=14, command=self.destroy,
                  bg="#555", fg="white", relief=tk.FLAT).pack(side=tk.LEFT, padx=6)
        tk.Button(row, text="清除已保存ROI", width=14, command=self._clear,
                  bg="#883333", fg="white", relief=tk.FLAT).pack(side=tk.LEFT, padx=6)

        self._rect = None
        self._x0 = self._y0 = 0
        self._canvas.bind("<ButtonPress-1>",   self._on_press)
        self._canvas.bind("<B1-Motion>",       self._on_drag)
        self._canvas.bind("<ButtonRelease-1>", self._on_release)

        existing = load_roi()
        if existing:
            ex1 = int(existing[0] * scale)
            ey1 = int(existing[1] * scale)
            ex2 = int(existing[2] * scale)
            ey2 = int(existing[3] * scale)
            self._rect = self._canvas.create_rectangle(
                ex1, ey1, ex2, ey2, outline="lime", width=2, dash=(4, 2))
            self._cx0, self._cy0 = ex1, ey1
            self._cx1, self._cy1 = ex2, ey2

        self.update_idletasks()
        px = parent.winfo_x() + (parent.winfo_width()  - self.winfo_reqwidth())  // 2
        py = parent.winfo_y() + (parent.winfo_height() - self.winfo_reqheight()) // 2
        self.geometry(f"+{max(px,0)}+{max(py,0)}")

    def _on_press(self, e):
        self._x0, self._y0 = e.x, e.y
        if self._rect:
            self._canvas.delete(self._rect)
            self._rect = None

    def _on_drag(self, e):
        if self._rect:
            self._canvas.delete(self._rect)
        self._rect = self._canvas.create_rectangle(
            self._x0, self._y0, e.x, e.y, outline="yellow", width=2)
        self._cx1, self._cy1 = e.x, e.y

    def _on_release(self, e):
        self._cx0, self._cy0 = self._x0, self._y0
        self._cx1, self._cy1 = e.x, e.y
        if self._rect:
            self._canvas.delete(self._rect)
        self._rect = self._canvas.create_rectangle(
            self._cx0, self._cy0, self._cx1, self._cy1,
            outline="lime", width=2)

    def _confirm(self):
        if not hasattr(self, "_cx0"):
            messagebox.showwarning("提示", "请先拖拽画出区域", parent=self)
            return
        x1 = int(min(self._cx0, self._cx1) / self._scale)
        y1 = int(min(self._cy0, self._cy1) / self._scale)
        x2 = int(max(self._cx0, self._cx1) / self._scale)
        y2 = int(max(self._cy0, self._cy1) / self._scale)
        if x2 - x1 < 20 or y2 - y1 < 20:
            messagebox.showwarning("提示", "框选区域太小，请重新画", parent=self)
            return
        self.result = (x1, y1, x2, y2)
        save_roi(self.result)
        self.destroy()

    def _clear(self):
        if os.path.exists(ROI_FILE):
            os.remove(ROI_FILE)
        if self._rect:
            self._canvas.delete(self._rect)
            self._rect = None
        messagebox.showinfo("已清除", "ROI配置已删除，下次将使用全图", parent=self)


class App(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("MB 滴定终点检测")
        self.configure(bg="#1e1e1e")
        self.resizable(False, False)

        self._model      = None
        self._busy       = False
        self._panels_rgb = None
        self._orig_path  = None
        self._roi        = load_roi()
        self.device      = torch.device("cpu")

        self._build()
        self.update_idletasks()
        self.geometry("+100+40")
        self.bind("<space>", lambda _: self._capture())

        threading.Thread(target=self._bg_load, daemon=True).start()
        self.after(300, self._check_model)

    def _build(self):
        BG = "#1e1e1e"

        self._status = tk.StringVar(value="模型加载中，请稍候…")
        tk.Label(self, textvariable=self._status, bg="#2a2a2a", fg="#aaaaaa",
                 font=("微软雅黑", 10), anchor="w", padx=10).pack(fill=tk.X)

        self._roi_var = tk.StringVar(value=self._roi_text())
        tk.Label(self, textvariable=self._roi_var, bg="#2a2a2a", fg="#5599ff",
                 font=("微软雅黑", 9), anchor="w", padx=10).pack(fill=tk.X)

        self._res_var = tk.StringVar(value="—")
        self._res_lbl = tk.Label(self, textvariable=self._res_var,
                                 font=("微软雅黑", 32, "bold"),
                                 fg="white", bg="#333333", pady=10)
        self._res_lbl.pack(fill=tk.X, padx=12, pady=(10, 0))

        self._detail = tk.StringVar(value="")
        tk.Label(self, textvariable=self._detail, font=("微软雅黑", 11),
                 fg="#bbbbbb", bg=BG).pack(pady=(4, 8))

        row = tk.Frame(self, bg=BG)
        row.pack(padx=12)
        for attr, title in [("_lbl0","裁剪区域（原图）"),
                             ("_lbl1","概率热力图"),
                             ("_lbl2","掩码叠加")]:
            col = tk.Frame(row, bg=BG)
            col.pack(side=tk.LEFT, padx=6)
            tk.Label(col, text=title, bg=BG, fg="#888888",
                     font=("微软雅黑", 10)).pack()
            box = tk.Frame(col, width=PANEL, height=PANEL, bg="#2a2a2a",
                           relief=tk.GROOVE, borderwidth=2)
            box.pack()
            box.pack_propagate(False)
            lbl = tk.Label(box, bg="#2a2a2a")
            lbl.pack(fill=tk.BOTH, expand=True)
            setattr(self, attr, lbl)

        btn_row = tk.Frame(self, bg=BG)
        btn_row.pack(pady=12)
        self._open_btn = tk.Button(btn_row, text="打开图片",
                                   command=self._open,
                                   font=("微软雅黑", 12), relief=tk.FLAT,
                                   bg="#3a7bd5", fg="white", padx=20, pady=8)
        self._open_btn.pack(side=tk.LEFT, padx=6)

        tk.Button(btn_row, text="重设ROI",
                  command=self._reset_roi,
                  font=("微软雅黑", 12), relief=tk.FLAT,
                  bg="#555", fg="white", padx=20, pady=8).pack(side=tk.LEFT, padx=6)

        self._save_btn = tk.Button(btn_row, text="保存结果图",
                                   command=self._save,
                                   font=("微软雅黑", 12), relief=tk.FLAT,
                                   bg="#555", fg="white",
                                   padx=20, pady=8, state=tk.DISABLED)
        self._save_btn.pack(side=tk.LEFT, padx=6)

        log_frame = tk.Frame(self, bg=BG)
        log_frame.pack(fill=tk.X, padx=12, pady=(0, 10))
        tk.Label(log_frame, text="检测记录（空格键拍照）", bg=BG,
                 fg="#666", font=("微软雅黑", 9)).pack(anchor="w")
        self._log_text = tk.Text(log_frame, height=5, bg="#2a2a2a",
                                 fg="#cccccc", font=("Consolas", 10),
                                 relief=tk.FLAT, state=tk.DISABLED)
        self._log_text.pack(fill=tk.X)
        self._log_text.tag_config("pos", foreground="#66ff66")
        self._log_text.tag_config("neg", foreground="#ff6666")

    def _roi_text(self):
        if self._roi:
            x1, y1, x2, y2 = self._roi
            return f"ROI: ({x1},{y1}) → ({x2},{y2})   宽{x2-x1}×高{y2-y1}px"
        return "ROI: 未设置（将使用全图）"

    def _bg_load(self):
        try:
            self._model = load_model(DEFAULT_WEIGHT, self.device)
        except Exception as e:
            print(f"[ERROR] {e}")
            self._model = "err"

    def _check_model(self):
        if self._model is None:
            self.after(300, self._check_model)
            return
        if self._model == "err":
            self._status.set("模型加载失败，请检查 weights/ 目录")
        else:
            self._status.set("就绪 — 空格键拍照，或点「打开图片」")

    def _open(self):
        if self._model is None:
            messagebox.showinfo("提示", "模型还在加载，请稍候…", parent=self)
            return
        if self._model == "err":
            messagebox.showerror("错误", "模型加载失败", parent=self)
            return
        if self._busy:
            return
        path = filedialog.askopenfilename(
            parent=self, title="选择图片",
            filetypes=[("图片", "*.jpg *.jpeg *.png *.bmp"), ("所有文件", "*.*")])
        if not path:
            return
        pil = Image.open(path)
        self._orig_path = path
        if self._roi is None:
            sel = RoiSelector(self, pil)
            self.wait_window(sel)
            if sel.result:
                self._roi = sel.result
            self._roi_var.set(self._roi_text())
        self._run_infer(pil)

    def _capture(self):
        if not _MVS_AVAILABLE:
            messagebox.showerror("错误", "未找到海康MVS SDK\n请确认已安装MVS", parent=self)
            return
        if self._model is None:
            messagebox.showinfo("提示", "模型还在加载，请稍候…", parent=self)
            return
        if self._model == "err":
            messagebox.showerror("错误", "模型加载失败", parent=self)
            return
        if self._busy:
            return
        self._status.set("正在连接相机…")
        self.update_idletasks()
        try:
            pil = _hik_grab_one()
        except Exception as e:
            messagebox.showerror("相机错误", str(e), parent=self)
            self._status.set(f"相机错误: {e}")
            return
        save_path = os.path.join(_HERE, "camera_capture.jpg")
        pil.save(save_path)
        self._orig_path = save_path
        if self._roi is None:
            sel = RoiSelector(self, pil)
            self.wait_window(sel)
            if sel.result:
                self._roi = sel.result
            self._roi_var.set(self._roi_text())
        self._run_infer(pil)

    def _reset_roi(self):
        if self._orig_path is None:
            messagebox.showinfo("提示", "请先打开或拍摄一张图片", parent=self)
            return
        pil = Image.open(self._orig_path)
        self._roi = None
        sel = RoiSelector(self, pil)
        self.wait_window(sel)
        if sel.result:
            self._roi = sel.result
        self._roi_var.set(self._roi_text())
        if self._roi:
            self._run_infer(pil)

    def _run_infer(self, pil):
        self._busy = True
        self._res_var.set("推理中…")
        self._res_lbl.config(bg="#333333")
        self._detail.set("")
        self._status.set(f"处理: {os.path.basename(self._orig_path)}")
        threading.Thread(target=self._infer, args=(pil,), daemon=True).start()

    def _infer(self, pil):
        try:
            res = run_predict(self._model, self.device, pil, roi=self._roi)
            orig, heat, overlay = make_panels(res)
            self._panels_rgb = (orig, heat, overlay)
            self.after(0, self._show_result, res)
        except Exception as e:
            self.after(0, self._show_error, str(e))

    def _show_result(self, res):
        if res["result"] == "positive":
            self._res_var.set("饱  和  ✓")
            self._res_lbl.config(bg="#1a5c1a")
        else:
            self._res_var.set("未 饱 和  ✗")
            self._res_lbl.config(bg="#6e1a1a")
        self._detail.set(
            f"置信度 {res['confidence']:.1%}    面积占比 {res['area_ratio']:.2%}")
        self._status.set("完成")
        orig, heat, overlay = self._panels_rgb
        self._tk0 = to_tk(orig,    PANEL)
        self._tk1 = to_tk(heat,    PANEL)
        self._tk2 = to_tk(overlay, PANEL)
        self._lbl0.config(image=self._tk0)
        self._lbl1.config(image=self._tk1)
        self._lbl2.config(image=self._tk2)
        self._save_btn.config(state=tk.NORMAL)
        self._busy = False

        t = datetime.datetime.now().strftime("%H:%M:%S")
        tag = "pos" if res["result"] == "positive" else "neg"
        sym = "✓ 阳性" if res["result"] == "positive" else "✗ 阴性"
        line = f"[{t}]  {sym}  置信度={res['confidence']:.1%}  面积={res['area_ratio']:.2%}\n"
        self._log_text.config(state=tk.NORMAL)
        self._log_text.insert(tk.END, line, tag)
        self._log_text.see(tk.END)
        self._log_text.config(state=tk.DISABLED)

    def _show_error(self, msg):
        self._res_var.set("出错")
        self._status.set(f"错误: {msg}")
        self._busy = False

    def _save(self):
        if not self._panels_rgb or not self._orig_path:
            return
        orig, heat, overlay = self._panels_rgb
        out = os.path.splitext(self._orig_path)[0] + "_result.jpg"
        Image.fromarray(np.concatenate([orig, heat, overlay], axis=1)).save(out)
        self._status.set(f"已保存: {os.path.basename(out)}")


if __name__ == "__main__":
    App().mainloop()
