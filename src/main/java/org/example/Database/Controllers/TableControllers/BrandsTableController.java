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
import org.example.Database.Classes.ClassesForDatabase.Tables.BrandTable;
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


public class BrandsTableController implements Initializable {

    private final ObservableList<BrandTable> data = FXCollections.observableArrayList();
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
    private TableColumn<BrandTable, String> brandColumn;

    @FXML
    private TableView<BrandTable> brandTable;

    @FXML
    private TableColumn<BrandTable, BrandTable> deleteColumn;

    @FXML
    private TableColumn<BrandTable, Integer> idColumn;

    private final ArrayList<String> brandList = new ArrayList<>();
    private BrandTable rowDataBrandTable = null;
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();

    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<BrandTable>> information=(brands, data) -> {
            try {
                while (brands.next()) {
                    data.add(new BrandTable(brands.getInt(1), brands.getString(2)));
                    brandList.add(brands.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(brands,data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.ID.getTitle()));

        brandColumn.setCellValueFactory(new PropertyValueFactory<>(Brands.BRAND.getTitle()));

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(BrandTable brandTable, boolean empty) {
                super.updateItem(brandTable, empty);

                if (brandTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(()->databaseHandler.deleteBrand(brandTable))).start();
                    brandList.remove(brandTable.getBrand());

                    data.remove(brandTable);
                });
            }
        });

        FilteredList<BrandTable> filteredData = new FilteredList<>(data);

        AtomicReference<String> brandString = new AtomicReference<>(emptyString);

        brandField.setTextFormatter(new TextFormatter<>(UnaryOperators.getBrandValidationFormatter()));
        brandField.textProperty().addListener((observable, oldValue, newValue) -> {

            brandString.set(newValue);

            System.out.println(brandString);
            flagsOnSearch.set(0, brandString.get().equals(emptyString) || brandList.contains(brandString.get()));
            System.out.println(flagsOnSearch.get(0));

            if (rowDataBrandTable != null) {
                flagsOnChange.set(0,brandString.get().equalsIgnoreCase(rowDataBrandTable.getBrand()));
            }

            filteredData.setPredicate(brandTable -> isCoincidence(brandTable,brandString));
        });

        SortedList<BrandTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(brandTable.comparatorProperty());
        brandTable.setItems(sortedData);

        brandTable.setRowFactory(param -> {
            TableRow<BrandTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(brandTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataBrandTable = rowData;
                    addBrandToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(BrandTable brandTable, AtomicReference<String> brandString) {
        return brandTable.getBrand().toLowerCase().contains(brandString.get().toLowerCase());
    }

    private void addFlags() {
        for (int i = 0; i < 1; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetBrand(new BrandTable(brandField.getText())));
        brandList.add(brandField.getText());
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataBrandTable =null;
    }

    private void prepareTableForChanges() {
        brandList.remove(rowDataBrandTable.getBrand());
    }

    private void resetChanges() {
        brandList.add(rowDataBrandTable.getBrand());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteBrandInf();
        updateRowDataBrand();
        hideRstButton();
        addBrandInf();
        databaseHandler.updateBrand(rowDataBrandTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataBrand() {
        rowDataBrandTable.setBrand(brandField.getText());
    }

    private void clearFields() {
        brandField.setText(emptyString);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBrandInf() {
        data.add(rowDataBrandTable);
        brandList.add(rowDataBrandTable.getBrand());
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
        data.remove(rowDataBrandTable);
    }

    private void addBrandToFields() {
        brandField.setText(rowDataBrandTable.getBrand());
    }
}
