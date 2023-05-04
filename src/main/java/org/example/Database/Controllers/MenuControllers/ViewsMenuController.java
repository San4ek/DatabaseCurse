package org.example.Database.Controllers.MenuControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewsMenuController implements Initializable {

    @FXML
    private Button gadgetsViewButton;

    @FXML
    private Button purchasesViewButton;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gadgetsViewButton.setOnAction(actionEvent -> Scenes.GADGETS_VIEW.setScene((Stage) gadgetsViewButton.getScene().getWindow()));
        purchasesViewButton.setOnAction(actionEvent -> Scenes.PURCHASES_VIEW.setScene((Stage) purchasesViewButton.getScene().getWindow()));
        backButton.setOnAction(actionEvent -> Scenes.MAIN_MENU.setScene((Stage) backButton.getScene().getWindow()));
    }
}
