package luxmeter.puzzlefx.ui;

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
import luxmeter.puzzlefx.model.Game;
import luxmeter.puzzlefx.model.Piece;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
    // Drawing Surface (Canvas)
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private Game game;

    @FXML
    private StackPane rootPane;
    private SwapHandler swapHandler;

    // first method to be called by the JavaFX framework
    // needs to set member class variables properly
    // otherwise methods depending on them might throw an NullPointerException
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // need to define an initial size for the canvas otherwise we won't see anything
        canvas = new Canvas(AppConstants.MAX_WINDOW_WIDTH, AppConstants.MAX_WINDOW_HEIGHT);

        graphicsContext = canvas.getGraphicsContext2D();

        fillBackgroundWithColor(Color.BLACK);
        InputStream imageStream = getClass().getResourceAsStream(AppConstants.IMAGE_LOCATION);
        game = Game.createGameFromInputStream(imageStream);
        Image originalImage = game.getResizedImage();

        changeImage();
        addCanvasToRootPane(originalImage);
    }

    public Game getGame() {
        return game;
    }

    public void drawShuffledPieces() {
        if (game == null
                || game.getResizedImage() == null
                || game.getResizedImage().getPixelReader() == null) {
            return;
        }
        fillBackgroundWithColor(Color.BLACK);

        if (game.getShuffledPieces().isEmpty()) {
            return;
        }

        Image originalImage = game.getShuffledPieces().get(0).getOriginalImage();
        Iterator<Piece> iterator = game.getShuffledPieces().iterator();
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

    private void addCanvasToRootPane(Image image) {
        if (image == null || image.getPixelReader() == null) {
            return;
        }
        rootPane.setPrefWidth(image.getWidth());
        rootPane.setPrefHeight(image.getHeight());
        rootPane.getChildren().add(canvas);
        StackPane.setAlignment(canvas, Pos.TOP_CENTER);
    }

    private void fillBackgroundWithColor(Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void changeImage(){
        drawShuffledPieces();
        if (swapHandler != null) {
            canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, swapHandler);
        }
        swapHandler = new SwapHandler(this);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, swapHandler);
    }

    // Create new Game with selected File and call changeImage() to update GUI with the new Game
    public void newGame() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", AppConstants.SUPPORTED_IMAGE_EXTENSION);
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                InputStream input = new FileInputStream(selectedFile.getAbsolutePath());
                game = Game.createGameFromInputStream(input);
                changeImage();
            } catch (FileNotFoundException e) {
                LOG.error("Image could not be loaded: ", e);
            }
        }
    }

    // Load Game from local hard drive. Only files with defined Game extension accepted.
    public void loadGame() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Game Files", ("*" + AppConstants.GAME_EXTENSION));
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Load Game");
        File selectedFile = fileChooser.showOpenDialog(null);

        if(selectedFile != null){
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                game = (Game) objectInputStream.readObject();
                game.initializeLoadedGame(selectedFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                LOG.error("Game could not be loaded: ", e);
            } catch (IOException ioe) {
                LOG.error("Game could not be loaded: ", ioe);
            } catch (ClassNotFoundException cnfe) {
                LOG.error("Game could not be loaded: ", cnfe);
            }

            changeImage();
        }
    }

    // Save Game to known path. Open saveGameAs() if savePath of the Game is not set yet.
    public void saveGame() {
        if(game.getSavePath() == null){
            saveGameAs();
        } else {
            try {
                FileOutputStream outputStream = new FileOutputStream(game.getSavePath());
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                oos.writeObject(this.game);
            } catch (IOException e) {
                LOG.error("Game could not be saved: " + e);
            }
        }
    }

    // Save Game to path selected by the user using the defined Game extension.
    public void saveGameAs() {
        String errorMessage = "Game could not be saved: ";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game As...");
        fileChooser.setInitialFileName("new");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Game Files", ("*" + AppConstants.GAME_EXTENSION));
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showSaveDialog(null);

        if(selectedFile != null) {
            try {
                String selectedPath = selectedFile.getAbsolutePath();
                FileOutputStream outputStream = new FileOutputStream(selectedPath);
                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                oos.writeObject(this.game);
            } catch (FileNotFoundException e) {
                LOG.error(errorMessage, e);
            } catch (IOException ioe) {
                LOG.error(errorMessage, ioe);
            }
        }
    }

    public void quitApplication() {
        Platform.exit();
    }
}