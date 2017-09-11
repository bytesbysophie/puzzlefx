package luxmeter.puzzlefx.model;

import javafx.scene.image.Image;

import java.io.*;
import java.util.List;

/**
 * Created by skrueger on 11.09.2017.
 * Represents a Game including Image and Pieces.
 */
public class Game implements Serializable {

    private byte[] image;
    private Image resizedImage;
    private List<Piece> piecesInNormalOrder;
    private List<Piece> shuffledPieces;

    /*Es muss ein Input Stream übergeben werden, dieser muss zwei mal ausgelesen werden, einmal als byte[] speichern*/
    public Game(InputStream imageFilePath) {
        boolean preserveRatio = true;
        boolean smooth = true;

        this.resizedImage = new Image(imageFilePath,
                AppConstants.MAX_WINDOW_WIDTH,
                AppConstants.MAX_WINDOW_HEIGHT,
                preserveRatio,
                smooth);

        this.piecesInNormalOrder = Piece.createPieces(resizedImage,
                AppConstants.NUM_HORIZONTAL_SLICES,
                AppConstants.NUM_VERTICAL_SLICES);
        this.shuffledPieces = Piece.shufflePieces(piecesInNormalOrder);

        //TODO: Image zum Seriaisieren in byte[] image einlesen
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
