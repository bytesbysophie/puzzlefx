package luxmeter.puzzlefx.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import luxmeter.puzzlefx.model.AppConstants;
import luxmeter.puzzlefx.model.Piece;

public class Controller implements Initializable {
    // Drawing Surface (Canvas)
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private List<Piece> piecesInNormalOrder;
    private List<Piece> shuffledPieces;

    @FXML
    private StackPane rootPane;
    private SwapHandler swapHandler;

    // first method to be called by the JavaFX framework
    // needs to set member class variables properly
    // otherwise methods depending on them might throw an NullPointerException
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // need to define an initial size for the canvas otherwise we won't see anything
        canvas = new Canvas(AppConstants.MAX_WINDOW_WIDTH, AppConstants.MAX_WINDOW_HEGHT);

        graphicsContext = canvas.getGraphicsContext2D();

        fillBackgroundWithColor(Color.BLACK);
        InputStream imageStream = getClass().getResourceAsStream(AppConstants.IMAGE_LOCATION);
        Image originalImage = getResizedImage(imageStream);

        //TODO: Avoid redundant code (same as in method changeImage())
        piecesInNormalOrder = Piece.createPieces(originalImage,
                AppConstants.NUM_HORIZONTAL_SLICES,
                AppConstants.NUM_VERTICAL_SLICES);
        shuffledPieces = Piece.shufflePieces(piecesInNormalOrder);
        drawShuffledPieces();

        swapHandler = new SwapHandler(this);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, swapHandler);

        addCanvasToRootPane(originalImage);
    }

    public List<Piece> getPiecesInNormalOrder() {
        return piecesInNormalOrder;
    }

    public List<Piece> getShuffledPieces() {
        return shuffledPieces;
    }

    public void drawShuffledPieces() {
        fillBackgroundWithColor(Color.BLACK);

        if (shuffledPieces.isEmpty()) {
            return;
        }

        Image originalImage = shuffledPieces.get(0).getOriginalImage();
        Iterator<Piece> iterator = shuffledPieces.iterator();
        for (int h = 0; h < AppConstants.NUM_HORIZONTAL_SLICES; h++) {
            int originalXPos = (int) ((originalImage.getWidth() / AppConstants.NUM_HORIZONTAL_SLICES) * h);

            for (int v = 0; v < AppConstants.NUM_VERTICAL_SLICES; v++) {
                int originalYPos = (int) ((originalImage.getHeight() / AppConstants.NUM_VERTICAL_SLICES) * v);
                Piece piece = iterator.hasNext() ? iterator.next() : null;
                if (piece != null) {
                    graphicsContext.getPixelWriter().setPixels(
                            originalXPos, originalYPos,
                            piece.getWidth(), piece.getHeight(),
                            piece.getOriginalImage().getPixelReader(),
                            piece.getXPos(), piece.getYPos());
                }
            }
        }
    }

    private Image getResizedImage(InputStream stream) {
        boolean preserveRatio = true;
        boolean smooth = true;

        return new Image(stream,
                AppConstants.MAX_WINDOW_WIDTH,
                AppConstants.MAX_WINDOW_HEGHT,
                preserveRatio,
                smooth);
    }

    private void addCanvasToRootPane(Image image) {
        rootPane.setPrefWidth(image.getWidth());
        rootPane.setPrefHeight(image.getHeight());
        rootPane.getChildren().add(canvas);
        StackPane.setAlignment(canvas, Pos.TOP_CENTER);
    }

    private void fillBackgroundWithColor(Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void changeImage(Image newImage){
        if (!shuffledPieces.isEmpty()) {
            shuffledPieces.clear();
        }

        piecesInNormalOrder = Piece.createPieces(newImage,
                AppConstants.NUM_HORIZONTAL_SLICES,
                AppConstants.NUM_VERTICAL_SLICES);
        shuffledPieces = Piece.shufflePieces(piecesInNormalOrder);
        drawShuffledPieces();

        canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, swapHandler);
        swapHandler = new SwapHandler(this);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, swapHandler);
    }

    public void openFile(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        String selectedPath = selectedFile.getAbsolutePath();

        try {
            //TODO: 
            InputStream newImageStream = new FileInputStream(selectedPath);
            Image newImage = getResizedImage(newImageStream);
            changeImage(newImage);
        } catch (IOException e) {
            System.out.print("Image upload failed:");
            System.out.println(e.getMessage());
        }
    }

    public void saveFile(){
        //TODO: Implement "save" functionality
        System.out.println("saveFile()");
    }

    public void saveFileAs(){
        //TODO: Implement "save as" functionality
        System.out.println("saveFileAs()");
    }

    public void quitApplication(){
        Platform.exit();
    }
}
