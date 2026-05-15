# MB 滴定终点检测工具

亚甲蓝（Methylene Blue）滴定终点自动识别系统，结合海康威视（Hikvision）工业相机实现自动化检测。

## 📁 目录结构

```text
相机程序/
├── viewer.py              # 主程序 (GUI 界面及控制逻辑)
├── models/
│   └── model_unet_cbam_gate.py  # 深度学习模型定义 (Attention-ResUNet)
└── weights/
    └── T2_VarI_Bottleneck_Enc_f0.pth  # 训练好的模型权重文件
```

## 🛠️ 环境要求

1. **Python 3.8+**
   安装必要依赖库：
   ```bash
   pip install torch torchvision opencv-python pillow scipy numpy
   ```

2. **海康 MVS 软件** (必须安装)
   - 提供工业相机驱动及 Python 调用接口。
   - 默认安装在 `C:\Program Files (x86)\MVS`。

## ⚙️ 关键配置 【需根据实际环境修改】

若程序提示找不到相机 SDK 或 MVS 报错，请检查 `viewer.py` 顶部的以下路径配置：

*   **`_MVS_IMPORT`**: 指向 MVS 安装目录下的 Python 示例代码路径。
    *   *当前配置*: `r"C:\Win_GY_Code\MVS\Python\MvImport"`
*   **`_MVS_RUNTIME`**: 指向 MVS 的运行时库 (DLL) 路径。
    *   *当前配置*: `r"C:\Program Files (x86)\Common Files\MVS\Runtime\Win64_x64"`

## 🚀 运行方式

在项目根目录下执行：
```bash
python viewer.py
```

## 📖 使用说明

*   **空格键 (Space)**：触发相机单次拍照并立即进行 AI 推理。
*   **打开图片**：支持加载本地 `JPG/PNG` 图片进行算法效果测试（无需连接相机）。
*   **重设 ROI**：重新选择感兴趣区域（滤纸圆心区域）。
*   **首次使用**：
    1. 点击拍照或打开图片后，若未设置 ROI，程序会弹出框选窗口。
    2. 拖拽鼠标选定滤纸所在的圆形/方形区域。
    3. 点击“确认”保存，配置将持久化到 `roi_config.json`，下次启动自动加载。

## 📊 结果判定

*   🟢 **绿色「饱  和 ✓」**：检测到蓝色色晕出现，滴定到达终点。
*   🔴 **红色「未 饱 和 ✗」**：未见明显色晕，建议继续滴加滴定液。
*   **置信度/面积占比**：底部状态栏显示模型判定的置信水平及色晕覆盖比例。

## ⚠️ 注意事项

1. **原图备份**：程序每次拍照的原始图像会覆盖保存为 `camera_capture.jpg`，便于复核。
2. **光照环境**：建议在稳定的白色光源下使用，以获得最高的检测精度。
3. **模型加载**：程序启动时会在后台加载模型权重，若权重文件缺失，程序将提示错误。
