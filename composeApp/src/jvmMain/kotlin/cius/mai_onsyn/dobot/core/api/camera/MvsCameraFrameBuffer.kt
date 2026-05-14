package cius.mai_onsyn.dobot.core.api.camera

import MvCameraControlWrapper.MvCameraControlDefines
import java.awt.image.BufferedImage

class CameraFrameBuffer {
    private val frameOut = MvCameraControlDefines.MV_FRAME_OUT()
    private var localBuffer = ByteArray(0)

    private val dataLock = Any()

    fun getFrameOut(): MvCameraControlDefines.MV_FRAME_OUT {
        return frameOut
    }

    fun saveBuffer() = synchronized(dataLock) {
        if (localBuffer.size < frameOut.buffer.size) {
            localBuffer = ByteArray(frameOut.buffer.size)
        }
        System.arraycopy(frameOut.buffer, 0, localBuffer, 0, frameOut.buffer.size)
    }

    /**
     * 注意画面撕裂
     */
    fun getBuffer(): ByteArray {
        return localBuffer
    }

    fun getFrame(buffer: ByteArray) = synchronized(dataLock) {
        System.arraycopy(buffer, 0, frameOut.buffer, 0, buffer.size)
    }

    fun getFrameInfo(): MvCameraControlDefines.MV_FRAME_OUT_INFO {
        return frameOut.mvFrameOutInfo
    }

    fun toBufferedImage(): BufferedImage = synchronized(dataLock) {
        val bf = BufferedImage(
            frameOut.mvFrameOutInfo.width.toInt(),
            frameOut.mvFrameOutInfo.height.toInt(),
            BufferedImage.TYPE_3BYTE_BGR
        )
        bf.raster.setDataElements(
            0, 0,
            frameOut.mvFrameOutInfo.width.toInt(),
            frameOut.mvFrameOutInfo.height.toInt(),
            localBuffer
        )
        return bf
    }
}