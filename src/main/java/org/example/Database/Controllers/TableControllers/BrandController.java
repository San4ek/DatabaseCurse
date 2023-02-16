package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Database.Classes.ClassesForDatabase.Brand;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.Database.Enums.EnumsForDatabase.Brands.BRAND;
import static org.example.Database.Enums.EnumsForDatabase.Brands.ID;

public class BrandController implements Initializable {

    private final ObservableList<Brand> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet brands= databaseHandler.selectBrands();

    @FXML
    private Button backButton;

    @FXML
    private TextField brandField;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Brand, String> brandColumn;

    @FXML
    private TableView<Brand> brandTable;

    @FXML
    private TableColumn<Brand, Brand> deleteColumn;

    @FXML
    private TableColumn<Brand, Integer> idColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        AddInformation<ResultSet, ObservableList<Brand>> information=(brands, data) -> {
            try {
                while (brands.next()) {
                    data.add(new Brand(brands.getInt(1), brands.getString(2)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(brands,data);

        idColumn.setCellValueFactory(new PropertyValueFactory<>(ID.getFieldTitle()));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>(BRAND.getFieldTitle()));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Brand brand, boolean empty) {
                super.updateItem(brand, empty);

                if (brand == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    Runnable deleteBrand = () -> {
                        databaseHandler.deleteBrand(brand);
                        return;
                    };
                    Thread deleteBrandThread = new Thread(deleteBrand, "Delete brand thread");
                    deleteBrandThread.start();


                    data.remove(brand);
                });
            }
        });

        backButton.setOnAction(actionEvent -> Scenes.MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> {
            data.add(databaseHandler.insertAndGetBrand(new Brand(brandField.getText())));
            brandTable.setItems(data);
            brandField.clear();
        });

        FilteredList<Brand> filteredData = new FilteredList<>(data, b -> true);

        brandField.textProperty().addListener((observable, oldValue, newValue) -> {

            AtomicBoolean flag = new AtomicBoolean(false);

            filteredData.setPredicate(brand -> {

                if (newValue == null || newValue.isEmpty()) {
                    addButton.setDisable(true);
                    return true;
                }

                if (brand.getBrand().equalsIgnoreCase(newValue)) {
                    flag.set(true);
                }
                addButton.setDisable(flag.get());
                return brand.getBrand().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<Brand> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(brandTable.comparatorProperty());
        brandTable.setItems(sortedData);

        brandField.setTextFormatter(new TextFormatter<>(UnaryOperators.getBrandValidationFormatter()));
    }
}
