package org.example.Database.Controllers.MenuControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private Button tablesMenuButton;

    @FXML
    private Button viewsMenuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablesMenuButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) tablesMenuButton.getScene().getWindow()));
        viewsMenuButton.setOnAction(actionEvent -> Scenes.VIEWS_MENU.setScene((Stage) viewsMenuButton.getScene().getWindow()));
    }
}