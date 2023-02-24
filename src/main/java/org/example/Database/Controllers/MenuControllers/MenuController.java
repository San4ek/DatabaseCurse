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

    @FXML
    private Button typesButton;

    @FXML
    private Button gadgetsButton;

    @FXML
    private Button providersButton;

    @FXML
    private Button purchasesButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        brandsButton.setOnAction(actionEvent -> Scenes.BRAND.setScene((Stage) brandsButton.getScene().getWindow()));
        buyersButton.setOnAction(actionEvent -> Scenes.BUYERS.setScene((Stage) buyersButton.getScene().getWindow()));
        consultantsButton.setOnAction(actionEvent -> Scenes.CONSULTANTS.setScene((Stage) consultantsButton.getScene().getWindow()));
        countriesButton.setOnAction(actionEvent -> Scenes.COUNTRIES.setScene((Stage) countriesButton.getScene().getWindow()));
        typesButton.setOnAction(actionEvent -> Scenes.TYPES_OF_GADGETS.setScene((Stage) typesButton.getScene().getWindow()));
        gadgetsButton.setOnAction(actionEvent -> Scenes.GADGETS.setScene((Stage) gadgetsButton.getScene().getWindow()));
        providersButton.setOnAction(actionEvent -> Scenes.PROVIDERS.setScene((Stage) providersButton.getScene().getWindow()));
        purchasesButton.setOnAction( actionEvent -> Scenes.PURCHASES.setScene((Stage) purchasesButton.getScene().getWindow()));
    }
}
