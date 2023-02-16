package org.example.Database.Controllers.MenuControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button brandsButton;

    @FXML
    private Button buyersButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        brandsButton.setOnAction(actionEvent -> Scenes.BRAND.setScene((Stage) brandsButton.getScene().getWindow()));
        buyersButton.setOnAction(actionEvent -> Scenes.BUYERS.setScene((Stage) buyersButton.getScene().getWindow()));
    }
}
