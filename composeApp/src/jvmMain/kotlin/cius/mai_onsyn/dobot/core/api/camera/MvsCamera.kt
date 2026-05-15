package cius.mai_onsyn.dobot.core.api.camera

import MvCameraControlWrapper.CameraControlException
import MvCameraControlWrapper.MvCameraControl
import MvCameraControlWrapper.MvCameraControlDefines
import cius.mai_onsyn.dobot.log
import java.awt.image.BufferedImage
import kotlin.concurrent.thread

private const val TIMEOUT_MILLIS = 1000L
class MvsCamera {
    private val buffer = CameraFrameBuffer()
    private var handle: MvCameraControlDefines.Handle? = null
    private var active = false
    private var captured = false

    private val loopLock = Object()

    private var grabber: Thread? = null

    companion object {
        fun listDevices(): List<MvCameraControlDefines.MV_CC_DEVICE_INFO?>? {
            try {
                val deviceList = MvCameraControl.MV_CC_EnumDevices(MvCameraControlDefines.MV_USB_DEVICE)
                return deviceList
            } catch (e: CameraControlException) {
                log.error("Enumerate devices failed!", e)
                return null
            }
        }

        fun initialize(): Boolean {
            val b = checkNRet(MvCameraControl.MV_CC_Initialize())
            if (b) log.info("Camera initialized")
            else log.error("Camera initialize failed!")
            return b
        }

        fun finalized(): Boolean {
            val b = checkNRet(MvCameraControl.MV_CC_Finalize())
            if (b) log.info("Camera finalized")
            else log.error("Camera finalize failed!")
            return b
        }

        private fun checkNRet(nRet: Int): Boolean {
            val success = nRet == MvCameraControlDefines.MV_OK
            if (!success) {
                log.warn("Camera operation failed with error code: $nRet")
            }
            return success
        }
    }

    fun getBuffer(): CameraFrameBuffer {
        return buffer
    }

    fun open(deviceInfo: MvCameraControlDefines.MV_CC_DEVICE_INFO): Boolean {
        try {
            handle = MvCameraControl.MV_CC_CreateHandle(deviceInfo)
            var b = checkNRet(MvCameraControl.MV_CC_OpenDevice(handle))
            b = b && checkNRet(MvCameraControl.MV_CC_SetEnumValueByString(handle, "TriggerMode", "Off"))

            log.info("Open device success")
            return b
        } catch (e: CameraControlException) {
            log.error("Failed to open device!", e)
            return false
        }
    }

    fun startGrabbing(): Boolean {
        checkCameraOpened()

        active = true
        if (checkNRet(MvCameraControl.MV_CC_StartGrabbing(handle))) {
            log.info("Start grabbing")

            grabber = thread {
                getImageLoop()
            }
            return true
        } else {
            log.error("Start grabbing failed!")
            return false
        }
    }

    fun stopGrabbing(): Boolean {
        checkCameraOpened()

        active = false
        synchronized(loopLock) {
            loopLock.wait(TIMEOUT_MILLIS)
        }
        if (checkNRet(MvCameraControl.MV_CC_StopGrabbing(handle))) {
            log.info("Stop grabbing")
            return true
        } else {
            log.error("Stop grabbing failed!")
            return false
        }
    }

    fun close(): Boolean {
        checkCameraOpened()

        val b = if (checkNRet(MvCameraControl.MV_CC_CloseDevice(handle))) {
            checkNRet(MvCameraControl.MV_CC_DestroyHandle(handle))
        } else false
        handle = null
        if (b) log.info("Camera closed")
        else log.error("Camera close failed!")
        return b
    }

    private fun checkCameraOpened() {
        if (handle == null)
            throw IllegalStateException("Camera is not opened!")
    }

    private fun getImageLoop() {
        while (active) {
            synchronized(this) {
                try {
                    val nRet = MvCameraControl.MV_CC_GetImageBuffer(handle, buffer.getFrameOut(), TIMEOUT_MILLIS)
                    if (checkNRet(nRet)) {
                        log.trace("GetImage, width: ${buffer.getFrameInfo().width}, height: ${buffer.getFrameInfo().height}, frameNum: ${buffer.getFrameInfo().frameNum}")
                        buffer.saveBuffer()
                        checkNRet(MvCameraControl.MV_CC_FreeImageBuffer(handle, buffer.getFrameOut()))
                        captured = true
                    }
                } catch (e: Exception) {
                    log.error("Error while grabbing image", e)
                    Thread.sleep(10)
                }
            }
            synchronized(loopLock) {
                loopLock.notifyAll()
            }
            if (!captured) {
                Thread.yield()
            }
        }
    }

    fun awaitFrame(timeout: Long = TIMEOUT_MILLIS): Boolean {
        val startTime = System.currentTimeMillis()
        synchronized(loopLock) {
            while (active && buffer.getFrameInfo().width == 0.toShort()) {
                val elapsed = System.currentTimeMillis() - startTime
                val remaining = timeout - elapsed
                if (remaining <= 0) break

                try {
                    loopLock.wait(remaining)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    return false
                }
            }
        }
        return buffer.getFrameInfo().width > 0
    }

    // --- 增加两个变量用于复用，避免频繁 GC ---
    private var colorBuffer = ByteArray(0)
    private var colorImage: BufferedImage? = null

    // --- 修改 convertToBgr ---
    fun convertToBgr(frame: MvCameraControlDefines.MV_FRAME_OUT): ByteArray {
        val info = frame.mvFrameOutInfo
        val outBufferSize = info.width * info.height * 3

        // 核心改动 1：复用数组，只有在画面尺寸变化时才重新分配
        if (colorBuffer.size != outBufferSize) {
            colorBuffer = ByteArray(outBufferSize)
        }

        val stConvertParam = MvCameraControlDefines.MV_CC_PIXEL_CONVERT_PARAM_EX()
        stConvertParam.width = info.width.toInt()
        stConvertParam.height = info.height.toInt()
        stConvertParam.srcPixelType = info.pixelType
        stConvertParam.srcData = frame.buffer
        stConvertParam.srcDataLen = info.frameLen

        // 注意：这里改成 BGR8_Packed，否则配合你的 TYPE_3BYTE_BGR 会出现红蓝反色
        stConvertParam.dstPixelType = MvCameraControlDefines.MvGvspPixelType.PixelType_Gvsp_RGB8_Packed
        stConvertParam.dstBuffer = colorBuffer
        stConvertParam.dstBufferSize = outBufferSize

        val nRet = MvCameraControl.MV_CC_ConvertPixelTypeEx(handle, stConvertParam)
        if (nRet == MvCameraControlDefines.MV_OK) {
            return colorBuffer
        }
        return ByteArray(0)
    }

    // --- 修改 capture ---
    fun capture(): BufferedImage? {
        try {
            val width = buffer.getFrameInfo().width.toInt()
            val height = buffer.getFrameInfo().height.toInt()

            if (width <= 0 || height <= 0) return null

            // 核心改动 2：复用 BufferedImage 对象
            if (colorImage == null || colorImage!!.width != width || colorImage!!.height != height) {
                colorImage = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
            }

            val bgrData = convertToBgr(buffer.getFrameOut())
            if (bgrData.isNotEmpty()) {
                colorImage!!.raster.setDataElements(0, 0, width, height, bgrData)
            }
            return colorImage
        } catch (e: Exception) {
            log.error("capture error", e)
            return null
        }
    }
}