package luxmeter.puzzlefx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Represents a piece of the puzzle and contains Piece logic.
 */
public final class Piece {
    private Image originalImage;
    private int width = 0;
    private int height = 0;
    private int xPos = 0;
    private int yPos = 0;
    private Piece(Image originalImage, int xPos, int yPos, int width, int height) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
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
                pieces.add(new Piece(originalImage, originalXPos, originalYPos, width, height));
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
}
