package luxmeter.puzzlefx;

import javafx.scene.image.Image;

/**
 * Represents a piece of the puzzle
 */
public final class Piece {
    private Image originalImage;
    private int width = 0;
    private int height = 0;
    private int srcX = 0;
    private int srcY = 0;

    public Piece(Image originalImage, int srcX, int srcY, int width, int height) {
        this.width = width;
        this.height = height;
        this.srcX = srcX;
        this.srcY = srcY;
        this.originalImage = originalImage;
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

    public int getSrcX() {
        return srcX;
    }

    public int getSrcY() {
        return srcY;
    }
}
