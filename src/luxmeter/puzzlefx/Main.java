package luxmeter.puzzlefx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("app.css");
        stage.setScene(scene);
        stage.setTitle(AppConstants.WINDOW_TITLE);
        stage.setResizable(false);
        stage.show();
    }
}