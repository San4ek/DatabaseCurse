package org.example.Database.Enums.EnumsForFX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public enum Scenes {
    BRAND("Tables/Brand.fxml"),
    MENU("Menus/Menu.fxml"),
    BUYERS("Tables/Buyers.fxml");

    private String title;

    Scenes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return "/org.example.Database/"+title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScene(Stage stage) {
        Parent root=null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(this.getTitle())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(Objects.requireNonNull(root)));
    }
}
