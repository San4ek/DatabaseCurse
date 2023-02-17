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

    @FXML
    private Button consultantsButton;

    @FXML
    private Button countriesButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        brandsButton.setOnAction(actionEvent -> Scenes.BRAND.setScene((Stage) brandsButton.getScene().getWindow()));
        buyersButton.setOnAction(actionEvent -> Scenes.BUYERS.setScene((Stage) buyersButton.getScene().getWindow()));
        consultantsButton.setOnAction(actionEvent -> Scenes.CONSULTANTS.setScene((Stage) consultantsButton.getScene().getWindow()));
        countriesButton.setOnAction(actionEvent -> Scenes.COUNTRIES_OF_MANUFACTURE.setScene((Stage) countriesButton.getScene().getWindow()));
    }
}
