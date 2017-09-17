package luxmeter.puzzlefx.model;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by skrueger on 11.09.2017.
 * Represents a Game including Image and Pieces.
 */
public class Game implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Game.class);
    private byte[] image;
    private Image resizedImage;
    private List<Piece> piecesInNormalOrder;
    private List<Piece> shuffledPieces;

    public Game(byte[] imageByteArray) {
        this.image = imageByteArray;

        InputStream inputStream = new ByteArrayInputStream(imageByteArray);
        boolean preserveRatio = true;
        boolean smooth = true;

        this.resizedImage = new Image(inputStream,
                AppConstants.MAX_WINDOW_WIDTH,
                AppConstants.MAX_WINDOW_HEIGHT,
                preserveRatio,
                smooth);

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

    public Image getResizedImage() {
        return resizedImage;
    }

    public List<Piece> getPiecesInNormalOrder() {
        return piecesInNormalOrder;
    }

    public List<Piece> getShuffledPieces() {
        return shuffledPieces;
    }
}
