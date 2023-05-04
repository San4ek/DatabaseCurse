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
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.example.Database.Classes.ClassesForDatabase.Tables.CountryTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.ProviderTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Gadgets;
import org.example.Database.Enums.EnumsForDatabase.Tables.Providers;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ProvidersTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private ChoiceBox<CountryTable> countryBox;

    @FXML
    private TableColumn<ProviderTable, Integer> countryColumn;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ProviderTable> providersTable;

    @FXML
    private TableColumn<ProviderTable, ProviderTable> deleteColumn;

    @FXML
    private TableColumn<ProviderTable, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<ProviderTable, Integer> idColumn;

    @FXML
    private TableColumn<ProviderTable, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<ProviderTable, Integer> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private Button resetButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet countries = databaseHandler.selectCountries();
    private final ResultSet providers = databaseHandler.selectProviders();
    private final ObservableList<ProviderTable> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private final ArrayList<String> phoneList = new ArrayList<>();
    private ProviderTable rowDataProviderTable = null;
    private final String all = "All";
    private final int zero = 0;
    private final CountryTable zeroCountryTable = new CountryTable(zero, all);
    private final ObservableList<CountryTable> countryTableList = FXCollections.observableArrayList();

    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillBoxes();

        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<ProviderTable>> information = (providers, data) -> {
            try {
                while (providers.next()) {
                    data.add(new ProviderTable(providers.getInt(1),
                            providers.getString(2),
                            providers.getString(3),
                            providers.getString(4),
                            providers.getInt(5)));
                    phoneList.add(providers.getString(3));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(providers, data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        addButton.setDisable(true);

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Providers.ID.getTitle()));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Providers.NAME.getTitle()));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Providers.PHONE_NUMBER.getTitle()));

        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Providers.EMAIL.getTitle()));

        countryColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.COUNTRY.getTitle()));
        countryColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getCountryByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount() == 2)
                            Scenes.COUNTRIES.setScene((Stage) addButton.getScene().getWindow());
                    });
                }
            }
        });

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(ProviderTable providerTable, boolean empty) {
                super.updateItem(providerTable, empty);

                if (providerTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteProvider(providerTable))).start();
                    phoneList.remove(providerTable.getPhone());

                    data.remove(providerTable);
                });
            }
        });

        providersTable.setItems(data);

        FilteredList<ProviderTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>(emptyString);
        AtomicReference<String> phoneString = new AtomicReference<>(emptyString);
        AtomicReference<String> emailString = new AtomicReference<>(emptyString);
        AtomicReference<Integer> countryValue = new AtomicReference<>(zero);

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(0, nameString.get().equals(emptyString) || !nameString.get().matches("^([A-Z])([a-z]*)$"));

            if (rowDataProviderTable != null) {
                flagsOnChange.set(0, nameString.get().equals(rowDataProviderTable.getName()));
            }

            filteredData.setPredicate(providerTable -> isCoincidence(nameString, phoneString, emailString, countryValue, providerTable));
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flagsOnSearch.set(1, phoneString.get().equals(emptyString) || phoneString.get().length() != 9 || phoneList.contains(phoneString.get()));

            if (rowDataProviderTable != null) {
                flagsOnChange.set(1, phoneString.get().equals(rowDataProviderTable.getPhone()));
            }

            filteredData.setPredicate(providerTable -> isCoincidence(nameString, phoneString, emailString, countryValue, providerTable));
        });

        emailField.setTextFormatter(new TextFormatter<>(UnaryOperators.getEmailValidationFormatter()));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailString.set(newValue);

            flagsOnSearch.set(2, emailString.get().equals(emptyString) || !emailString.get().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[ a-zA-Z0-9.-]+$"));

            if (rowDataProviderTable != null) {
                flagsOnChange.set(2, emailString.get().equals(rowDataProviderTable.getEmail()));
            }

            filteredData.setPredicate(providerTable -> isCoincidence(nameString, phoneString, emailString, countryValue, providerTable));
        });

        countryBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            countryValue.set(newValue.getID());

            flagsOnSearch.set(3, countryValue.get() == zero);

            if (rowDataProviderTable != null) {
                flagsOnChange.set(3, countryValue.get() == rowDataProviderTable.getCountry());
            }

            filteredData.setPredicate(providerTable -> isCoincidence(nameString, phoneString, emailString, countryValue, providerTable));
        });

        SortedList<ProviderTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(providersTable.comparatorProperty());
        providersTable.setItems(sortedData);

        providersTable.setRowFactory(param -> {
            TableRow<ProviderTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().name().equals(MouseButton.PRIMARY.name()) && rowData.equals(providersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataProviderTable = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(AtomicReference<String> nameValue, AtomicReference<String> phoneValue, AtomicReference<String> emailString, AtomicReference<Integer> countryValue, ProviderTable providerTable) {
        return providerTable.getName().contains(nameValue.get()) &&
                providerTable.getPhone().contains(phoneValue.get()) &&
                providerTable.getEmail().contains(emailString.get()) &&
                (countryValue.get() == zero || countryValue.get() == providerTable.getCountry());
    }

    private void addFlags() {
        for (int i = 0; i < 4; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }
    void onAddEvent() {
        data.add(databaseHandler.insertAndGetProvider(new ProviderTable(nameField.getText(),
                phoneField.getText(), emailField.getText(),
                countryBox.getValue().getID())));
        phoneList.add(phoneField.getText());
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataProviderTable =null;
    }

    private void prepareTableForChanges() {
        phoneList.remove(rowDataProviderTable.getPhone());
    }

    private void resetChanges() {
        phoneList.add(rowDataProviderTable.getPhone());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteProviderInf();
        updateRowDataProvider();
        hideRstButton();
        addProviderInf();
        databaseHandler.updateProvider(rowDataProviderTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataProvider() {
        rowDataProviderTable.setName(nameField.getText());
        rowDataProviderTable.setPhone(phoneField.getText());
        rowDataProviderTable.setEmail(emailField.getText());
        rowDataProviderTable.setCountry(countryBox.getValue().getID());
    }

    private void clearFields() {
        nameField.setText(emptyString);
        phoneField.setText(emptyString);
        emailField.setText(emptyString);
        countryBox.setValue(zeroCountryTable);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addProviderInf() {
        data.add(rowDataProviderTable);
        phoneList.add(rowDataProviderTable.getName());
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

    private void deleteProviderInf() {
        data.remove(rowDataProviderTable);
    }

    private void addBuyerToFields() {
        nameField.setText(rowDataProviderTable.getName());
        phoneField.setText(rowDataProviderTable.getPhone());
        emailField.setText(rowDataProviderTable.getEmail());
        countryBox.setValue(findCountry());
    }

    private CountryTable findCountry() {
        for (CountryTable countryTable : countryBox.getItems()) {
            if (countryTable.getID() == rowDataProviderTable.getCountry()) return countryTable;
        }

        return null;
    }

    private void addCountriesToBox() {
        try {
            while (countries.next()) {
                countryTableList.add(new CountryTable(countries.getInt(1), countries.getString(2)));
            }
            countryTableList.add(zeroCountryTable);
            countryBox.setItems(countryTableList);
            countryBox.setValue(zeroCountryTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillBoxes() {
        addCountriesToBox();
    }

    private String getCountryByID(int countryID) {
        for (CountryTable countryTable : countryTableList) {
            if (countryTable.getID() == countryID) return countryTable.getCountry();
        }

        return null;
    }
}
