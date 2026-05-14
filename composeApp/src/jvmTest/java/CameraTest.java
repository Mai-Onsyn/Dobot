import cius.mai_onsyn.dobot.core.api.camera.MvsCamera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CameraTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        MvsCamera camera = new MvsCamera();


        MvsCamera.Companion.initialize();

        camera.open(MvsCamera.Companion.listDevices().getFirst());
        camera.startGrabbing();

        camera.awaitFrame(2000);
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            BufferedImage img = camera.getBuffer().toBufferedImage();
            ImageIO.write(img, "png", new File("D:/Users/Desktop/frame_" + i + ".png"));
        }

        camera.stopGrabbing();
        camera.close();


        MvsCamera.Companion.finalized();
    }
}
