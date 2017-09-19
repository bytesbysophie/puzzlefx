package luxmeter.puzzlefx.ui;

import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.image.*;
import java.io.*;

/**
 * Created by skrueger on 18.09.2017.
 */
public class ImageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ImageHandler.class);

    // Used for setting the Image of a Game
    public static Image byteArrayToImage(byte[] imageByteArray, int width, int height, boolean preserveRatio, boolean smooth){
        InputStream newInputStream = new ByteArrayInputStream(imageByteArray);
        Image resizedImage = new Image(newInputStream,
                width,
                height,
                preserveRatio,
                smooth);
        return resizedImage;
    }

    // Used for setting the Image of a Piece
    public static Image byteArrayToImage(byte[] imageByteArray, int width, int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByteArray);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {
            LOG.error("Image could not be processed: " + ex);
        }
        return image;
    }

    // Used for saving an Image in a Byte Array for Serialization
    public static byte[] imageToByteArray(Image originalImage){
        BufferedImage image = SwingFXUtils.fromFXImage(originalImage, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"png", os);
        } catch (IOException e) {
            LOG.error("Image could not be processed: " + e);
        }
        return os.toByteArray();
    }
}


