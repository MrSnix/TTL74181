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
    private static final boolean MAXIMIZED = false;

    private static final String WINDOW_TITLE = "Fairchild Semiconductor - 74181 (ALU) - Emulator";

    @Override
    public void start(Stage stage) throws IOException {
        // Setting title
        stage.setTitle(WINDOW_TITLE);
        // Invoking scene handler
        stage.setScene(new Scene(load(getClass().getResource("/views/CircuitView.fxml"),
                                      ResourceBundle.getBundle("preferences")), WIDTH, HEIGHT));
        // Setting window size
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        // Setting as maximised by default
        stage.setMaximized(MAXIMIZED);
        // Setting icon
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/32.png").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/64.png").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/128.png").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/256.png").openStream()));
        stage.getIcons().add(new Image(getClass().getResource("/img/icons/512.png").openStream()));
        // Enabling
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
