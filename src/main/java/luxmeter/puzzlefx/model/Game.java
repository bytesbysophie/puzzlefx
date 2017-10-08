package luxmeter.puzzlefx.model;

import javafx.scene.image.Image;
import luxmeter.puzzlefx.ui.ImageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by skrueger on 11.09.2017.
 * Represents a Game including Image and Pieces.
 * As objects of type Image cannot be serialized, a Byte Array is used for this purpose.
 */
public class Game implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Game.class);
    private transient String savePath;
    private transient Image resizedImage;
    private byte[] resizedImageByte;
    private int width = 0;
    private int height = 0;
    private List<Piece> piecesInNormalOrder;
    private List<Piece> shuffledPieces;

    public Game(byte[] imageByteArray, Image resizedImage) {
        this.savePath = null;
        this.resizedImageByte = imageByteArray;

        this.resizedImage = resizedImage;

        this.width = (int) resizedImage.getWidth();
        this.height = (int) resizedImage.getHeight();

        if (resizedImage.getPixelReader() == null) {
            LOG.error("Something is wrong with the selected image");
            this.resizedImage = null;
        }
        else {
            this.piecesInNormalOrder = Piece.createPieces(resizedImage,
                    AppConstants.NUM_HORIZONTAL_SLICES,
                    AppConstants.NUM_VERTICAL_SLICES);
            this.shuffledPieces = Piece.shufflePieces(piecesInNormalOrder);
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public Image getResizedImage() {
        return resizedImage;
    }

    public List<Piece> getPiecesInNormalOrder() {
        return piecesInNormalOrder;
    }

    public List<Piece> getShuffledPieces() {
        return shuffledPieces;
    }

    // Generate Image attributes (resizedImage, piecesInNormalOrder, shuffledPieces) from the saved Byte Arrays
    public void initializeLoadedGame(String savePath){
        this.setSavePath(savePath);
        this.resizedImage = ImageHandler.byteArrayToImage(this.resizedImageByte,this.width,this.height);

        for (int i = 0; i < this.piecesInNormalOrder.size(); i++) {
            this.piecesInNormalOrder.get(i).setOriginalImageFromByteArray();
        }

        for (int i = 0; i < this.shuffledPieces.size(); i++) {
            this.shuffledPieces.get(i).setOriginalImageFromByteArray();
        }
    }

    // Factory Method to provide a new Game from an InputStream
    public static Game createGameFromInputStream(InputStream inputStream){
        byte[] imageByteArray = null;

        try {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            imageByteArray = output.toByteArray();
        } catch (IOException e) {
            LOG.error("A error occurred while processing the InputStream:", e);
        }

        Image resizedImage = ImageHandler.byteArrayToImage(imageByteArray,
                AppConstants.MAX_WINDOW_WIDTH,
                AppConstants.MAX_WINDOW_HEIGHT,
                true,
                true );

        return new Game(imageByteArray, resizedImage);
    }
}
