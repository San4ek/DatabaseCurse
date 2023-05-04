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
import org.example.Database.Classes.ClassesForDatabase.Tables.CountryTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Countries;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class CountriesTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField countryField;
    @FXML
    private TableColumn<CountryTable, String> countryColumn;

    @FXML
    private TableView<CountryTable> countryTable;

    @FXML
    private TableColumn<CountryTable, CountryTable> deleteColumn;

    @FXML
    private TableColumn<CountryTable, Integer> idColumn;

    @FXML
    private Button resetButton;

    private final ArrayList<String> countryList = new ArrayList<>();
    private CountryTable rowDataCountryTable = null;
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private final ObservableList<CountryTable> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet countries = databaseHandler.selectCountries();

    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<CountryTable>> information=(countries, data) -> {
            try {
                while (countries.next()) {
                    data.add(new CountryTable(countries.getInt(1), countries.getString(2)));
                    countryList.add(countries.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(countries,data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Countries.ID.getTitle()));

        countryColumn.setCellValueFactory(new PropertyValueFactory<>(Countries.COUNTRY.getTitle()));

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(CountryTable countryTable, boolean empty) {
                super.updateItem(countryTable, empty);

                if (countryTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(()->databaseHandler.deleteCountry(countryTable))).start();
                    countryList.remove(countryTable.getCountry());

                    data.remove(countryTable);
                });
            }
        });

        FilteredList<CountryTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> countryString = new AtomicReference<>(emptyString);

        countryField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        countryField.textProperty().addListener((observable, oldValue, newValue) -> {

            countryString.set(newValue);

            flagsOnSearch.set(0, countryString.get().equals(emptyString) || countryList.contains(countryString.get()));

            if (rowDataCountryTable != null) {
                flagsOnChange.set(0,countryString.get().equalsIgnoreCase(rowDataCountryTable.getCountry()));
            }

            filteredData.setPredicate(country -> isConfidence(country,countryString));
        });

        SortedList<CountryTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(countryTable.comparatorProperty());
        countryTable.setItems(sortedData);

        countryTable.setRowFactory(param -> {
            TableRow<CountryTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(countryTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataCountryTable = rowData;
                    addBrandToField();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(CountryTable countryTable, AtomicReference<String> countryString) {
        return countryTable.getCountry().toLowerCase().contains(countryString.get().toLowerCase());
    }

    private void addFlags() {
        for (int i = 0; i < 1; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetCountry(new CountryTable(countryField.getText())));
        countryList.add(countryField.getText());
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataCountryTable =null;
    }

    private void prepareTableForChanges() {
        countryList.remove(rowDataCountryTable.getCountry());
    }

    private void resetChanges() {
        countryList.add(rowDataCountryTable.getCountry());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteCountryInf();
        updateRowDataCountry();
        hideRstButton();
        addCountryInf();
        databaseHandler.updateCountry(rowDataCountryTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataCountry() {
        rowDataCountryTable.setCountry(countryField.getText());
    }

    private void clearFields() {
        countryField.setText(emptyString);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addCountryInf() {
        data.add(rowDataCountryTable);
        countryList.add(rowDataCountryTable.getCountry());
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

    private void deleteCountryInf() {
        data.remove(rowDataCountryTable);
    }

    private void addBrandToField() {
        countryField.setText(rowDataCountryTable.getCountry());
    }

}
