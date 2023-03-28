package org.example.Database.Enums.EnumsForFX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public enum Scenes {
    BRAND("Tables/Brands.fxml"),
    MAIN_MENU("Menus/MainMenu.fxml"),
    TABLES_MENU("Menus/TablesMenu.fxml"),
    VIEWS_MENU("Menus/ViewsMenu.fxml"),
    BUYERS("Tables/Buyers.fxml"),
    CONSULTANTS("Tables/Consultants.fxml"),
    COUNTRIES("Tables/Countries.fxml"),
    TYPES_OF_GADGETS("Tables/TypesOfGadgets.fxml"),
    GADGETS("Tables/Gadgets.fxml"),
    PROVIDERS("Tables/Providers.fxml"),
    PAYMENTS("Tables/Payments.fxml"),
    PURCHASES("Tables/Purchases.fxml"),
    GADGETS_VIEW("Views/GadgetsView.fxml"),
    PURCHASES_VIEW("Views/PurchasesView.fxml");

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
