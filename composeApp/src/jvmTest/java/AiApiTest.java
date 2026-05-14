import cius.mai_onsyn.dobot.core.api.visual_ai.VisualClient;
import cius.mai_onsyn.dobot.core.api.visual_ai.VisualResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class AiApiTest {
    public static void main(String[] args) {
        try (VisualClient client = new VisualClient()) {
            BufferedImage img = ImageIO.read(new File(
                    "D:\\Users\\Desktop\\相机程序\\Data_Clean_Source\\2_Unlabeled_Pool\\val\\positive\\DropPointImage_2025-08-19 15_45_25.jpg"));

            VisualResponse response = client.predictFromImage(img);
            System.out.println(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
