package com.ttl.alu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class Launcher extends Application {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private static final String WINDOW_TITLE = "Texas Instruments - 74181 (ALU) - Emulator";

    @Override
    public void start(Stage stage) throws IOException {
        // Setting title
        stage.setTitle(WINDOW_TITLE);
        // Invoking scene handler
        stage.setScene(new Scene(load(getClass().getResource("/views/CircuitView.fxml"),
                                      ResourceBundle.getBundle("preferences"))));
        // Setting window size
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        // Setting icon
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/32.jpg").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/64.jpg").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/128.jpg").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/256.jpg").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/512.jpg").openStream()));
        // Enabling
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
