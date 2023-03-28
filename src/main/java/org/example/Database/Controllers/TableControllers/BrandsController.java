package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Database.Classes.ClassesForDatabase.Tables.Brand;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Brands;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


public class BrandsController implements Initializable {

    private final ObservableList<Brand> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet brands= databaseHandler.selectBrands();

    @FXML
    private Button backButton;

    @FXML
    private Button resetButton;

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

    private final ArrayList<String> brandList = new ArrayList<>();
    private Brand rowDataBrand = null;
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        flagsOnSearch.add(true);
        flagsOnChange.add(false);

        AddInformation<ResultSet, ObservableList<Brand>> information=(brands, data) -> {
            try {
                while (brands.next()) {
                    data.add(new Brand(brands.getInt(1), brands.getString(2)));
                    brandList.add(brands.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(brands,data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.ID.getTitle()));

        brandColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.BRAND.getTitle()));

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
                    (new Thread(()->databaseHandler.deleteBrand(brand))).start();
                    brandList.remove(brand.getBrand());

                    data.remove(brand);
                });
            }
        });

        FilteredList<Brand> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> brandString = new AtomicReference<>("");

        brandField.setTextFormatter(new TextFormatter<>(UnaryOperators.getBrandValidationFormatter()));
        brandField.textProperty().addListener((observable, oldValue, newValue) -> {

            brandString.set(newValue);

            flagsOnSearch.set(0, brandString.get() == null || brandList.contains(brandString.get()));

            if (rowDataBrand != null) {
                flagsOnChange.set(0,brandString.get().equalsIgnoreCase(rowDataBrand.getBrand()));
            }

            filteredData.setPredicate(brand -> {

                if (brandString.get() == null) {
                    return true;
                }

                return isCoincidence(brand,brandString);
            });
        });

        SortedList<Brand> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(brandTable.comparatorProperty());
        brandTable.setItems(sortedData);

        brandTable.setRowFactory(param -> {
            TableRow<Brand> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(brandTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataBrand = rowData;
                    addBrandToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(Brand brand, AtomicReference<String> brandString) {
        return brand.getBrand().toLowerCase().contains(brandString.get().toLowerCase());
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetBrand(new Brand(brandField.getText())));
        brandTable.setItems(data);
        flagsOnChange.setAll(false);
        clearFields();
    }

    private void prepareTableForChanges() {
        brandList.remove(rowDataBrand.getBrand());
    }

    private void resetChanges() {
        brandList.add(rowDataBrand.getBrand());
        flagsOnChange.setAll(false);
        rowDataBrand = null;
    }

    private void onChangeEvent() {
        deleteBrandInf();
        updateRowDataBrand();
        hideRstButton();
        addBrandInf();
        databaseHandler.updateBrand(rowDataBrand);
        convertChgToAdd();
        clearFields();
        rowDataBrand = null;
    }

    private void updateRowDataBrand() {
        rowDataBrand.setBrand(brandField.getText());
    }

    private void clearFields() {
        brandField.clear();
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBrandInf() {
        data.add(rowDataBrand);
        brandList.add(rowDataBrand.getBrand());
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void convertAddToChg() {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onChangeEvent());
    }

    private void deleteBrandInf() {
        data.remove(rowDataBrand);
    }

    private void addBrandToFields() {
        brandField.setText(rowDataBrand.getBrand());
    }
}
