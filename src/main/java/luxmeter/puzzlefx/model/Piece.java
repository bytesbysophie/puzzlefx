package luxmeter.puzzlefx.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.Image;
import luxmeter.puzzlefx.ui.ImageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents a piece of the puzzle and contains Piece logic.
 * As objects of type Image cannot be serialized, a Byte Array is used for this purpose.
 */
public final class Piece implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Piece.class);

    private transient Image originalImage;
    private byte[] orignalImageByte;
    private int width = 0;
    private int height = 0;
    private int xPos = 0;
    private int yPos = 0;

    private Piece(byte[] originalImageByte, Image originalImage, int xPos, int yPos, int width, int height) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.orignalImageByte = originalImageByte;
        this.originalImage = originalImage;
    }

    public static List<Piece> createPieces(Image originalImage, int numHorizontalSlices, int numVerticalSlices) {
        List<Piece> pieces = new ArrayList();
        for (int h = 0; h < numHorizontalSlices; h++) {
            int originalXPos = (int) ((originalImage.getWidth() / numHorizontalSlices) * h);
            int width = (int) (originalImage.getWidth() / numHorizontalSlices);

            for (int v = 0; v < numVerticalSlices; v++) {
                int originalYPos = (int) ((originalImage.getHeight() / numVerticalSlices) * v);
                int height = (int) (originalImage.getHeight() / numVerticalSlices);
                //pieces.add(new Piece(originalImage, originalXPos, originalYPos, width, height));
                pieces.add(createPieceFromImage(originalImage, originalXPos, originalYPos, width, height));
            }
        }
        return Collections.unmodifiableList(pieces);
    }

    public static List<Piece> shufflePieces(List<Piece> pieces) {
        List<Piece> shuffledPieces = new ArrayList<>(pieces);
        Collections.shuffle(shuffledPieces);
        if (isReshuffleNeeded(pieces, shuffledPieces)) {
            shuffledPieces = shufflePieces(pieces);
        }
        return shuffledPieces;
    }

    private static boolean isReshuffleNeeded(List<Piece> pieces, List<Piece> shuffledPieces) {
        boolean reshuffleNeeded = true;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) != shuffledPieces.get(i)) {
                reshuffleNeeded = false;
            }
        }
        return reshuffleNeeded;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    //  Generate the Image attribute from a given Byte Array (needed to initialize a loaded game)
    public void setOriginalImageFromByteArray(){
        Image originalImage = ImageHandler.byteArrayToImage(this.orignalImageByte,this.width,this.height);
        this.setOriginalImage(originalImage);
    }

    // Factory Method that generates the requested constructor param byte[] originalImageByte
    public static Piece createPieceFromImage(Image originalImage,int xPos, int yPos, int width, int height) {
        byte[] imageByteArray = ImageHandler.imageToByteArray(originalImage);
        return new Piece(imageByteArray, originalImage, xPos, yPos, width, height);
    }
}
