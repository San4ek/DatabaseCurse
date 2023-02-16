package org.example.Database.Classes.MainClass;

import javafx.application.Application;
import javafx.stage.Stage;

import org.example.Database.Enums.EnumsForFX.Scenes;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scenes.MENU.setScene(stage);
        stage.show();
    }
}