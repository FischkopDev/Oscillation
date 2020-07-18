package de.home_skrobanek.schwingungen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowFX extends Application {

    public static Scene loginScene;
    public static AnchorPane loginPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/MainEnglish.fxml"));
        try {
            loginPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        loginScene = new Scene(loginPane, 900, 500);

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
        stage.setScene(loginScene);
        stage.setTitle("Vibration calculation | Timo Skrobanek");
        stage.setResizable(false);
        stage.show();
    }
}
