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
import org.example.Database.Classes.ClassesForDatabase.Brand;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Brands;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


public class BrandController implements Initializable {

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

    private final ObservableList<Boolean> flags = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        flags.add(true);

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

        flags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flags.contains(true)));

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.ID.getTitle()));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.ID.getTitle()));
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

        backButton.setOnAction(actionEvent -> Scenes.MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        FilteredList<Brand> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> brandString = new AtomicReference<>("");

        brandField.textProperty().addListener((observable, oldValue, newValue) -> {

            flags.set(0, newValue == null || newValue.isEmpty()|| brandList.contains(newValue));

            if (rowDataBrand != null) {
                addButton.setDisable(rowDataBrand.getBrand().equals(brandString.get()) || flags.contains(true));
            }

            filteredData.setPredicate(brand -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return brand.getBrand().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<Brand> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(brandTable.comparatorProperty());
        brandTable.setItems(sortedData);

        brandField.setTextFormatter(new TextFormatter<>(UnaryOperators.getBrandValidationFormatter()));

        brandTable.setRowFactory(param -> {
            TableRow<Brand> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearField();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(brandTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataBrand = rowData;
                    addBrandToField(rowData);
                    prepareTableForChanges(rowData);
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private void convertAddToChg() {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> {
            deleteBuyerInf();
            onChangeEvent(new Brand(rowDataBrand.getID(), brandField.getText()));
        });
    }

    private void deleteBuyerInf() {
        data.remove(rowDataBrand);
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void prepareTableForChanges(Brand rowData) {
        brandList.remove(rowData.getBrand());
        flags.set(0, false);
    }

    private void clearField() {
        brandField.clear();
    }

    private void resetChanges() {
        brandList.add(rowDataBrand.getBrand());
        rowDataBrand = null;
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetBrand(new Brand(brandField.getText())));
        brandTable.setItems(data);
        brandField.clear();
    }

    private void addBrandToField(Brand rowData) {
        brandField.setText(rowData.getBrand());
    }

    private void onChangeEvent(Brand brand) {
        rowDataBrand = null;
        hideRstButton();
        addBuyerInf(brand);
        databaseHandler.updateBrand(brand);
        convertChgToAdd();
        clearField();
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf(Brand brand) {
        data.add(brand);
        brandList.add(brand.getBrand());
    }
}
