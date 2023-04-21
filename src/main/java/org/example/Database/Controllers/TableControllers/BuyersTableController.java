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
import org.example.Database.Classes.ClassesForDatabase.Tables.BuyerTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Classes.HandlerClasses.EmailMessage;
import org.example.Database.Classes.HandlerClasses.WebServer;
import org.example.Database.Enums.ConfigEnums.MessageConfig;
import org.example.Database.Enums.ConfigEnums.ServerConfigs;
import org.example.Database.Enums.EnumsForDatabase.Tables.Buyers;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class BuyersTableController implements Initializable {

    @FXML
    private Button resetButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<BuyerTable> buyersTable;

    @FXML
    private TableColumn<BuyerTable, BuyerTable> deleteColumn;

    @FXML
    private TableColumn<BuyerTable, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<BuyerTable, Integer> idColumn;

    @FXML
    private TableColumn<BuyerTable, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<BuyerTable, String> phoneColumn;

    @FXML
    private TextField phoneField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet buyers = databaseHandler.selectBuyers();
    private final ObservableList<BuyerTable> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();

    private final ArrayList<String> phoneList = new ArrayList<>();

    private BuyerTable rowDataBuyerTable = null;

    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        setFlags();

        AddInformation<ResultSet, ObservableList<BuyerTable>> information = (buyers, data) -> {
            try {
                while (buyers.next()) {
                    data.add(new BuyerTable(buyers.getInt(1),
                            buyers.getString(2),
                            buyers.getString(3),
                            buyers.getString(4)));
                    phoneList.add(buyers.getString(3));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(buyers, data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Buyers.ID.getTitle()));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Buyers.NAME.getTitle()));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Buyers.PHONE.getTitle()));

        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Buyers.EMAIL.getTitle()));

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(BuyerTable buyerTable, boolean empty) {
                super.updateItem(buyerTable, empty);

                if (buyerTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteBuyer(buyerTable))).start();
                    phoneList.remove(buyerTable.getPhone());

                    data.remove(buyerTable);
                });
            }
        });

        FilteredList<BuyerTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>(emptyString);
        AtomicReference<String> phoneString = new AtomicReference<>(emptyString);
        AtomicReference<String> emailString = new AtomicReference<>(emptyString);

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(0, nameString.get().equals(emptyString) || !nameString.get().matches("^([A-Z])([a-z]*)$"));

            if (rowDataBuyerTable != null) {
                flagsOnChange.set(0,nameString.get().equalsIgnoreCase(rowDataBuyerTable.getName()));
            }

            filteredData.setPredicate(buyer -> isConfidence(buyer,nameString,phoneString,emailString));
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flagsOnSearch.set(1, phoneString.get() == null || phoneString.get().length() != 9 || phoneList.contains(phoneString.get()));

            if (rowDataBuyerTable != null) {
                flagsOnChange.set(1,phoneString.get().equals(rowDataBuyerTable.getPhone()));
            }

            filteredData.setPredicate(buyer -> {

                if (phoneString.get() == null) {
                    return true;
                }

                return isConfidence(buyer,nameString,phoneString,emailString);
            });
        });

        emailField.setTextFormatter(new TextFormatter<>(UnaryOperators.getEmailValidationFormatter()));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailString.set(newValue);

            flagsOnSearch.set(2, emailString.get() == null || !emailString.get().matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[ a-zA-Z0-9.-]+$"));

            if (rowDataBuyerTable != null) {
                flagsOnChange.set(2,emailString.get().equals(rowDataBuyerTable.getEmail()));
            }

            filteredData.setPredicate(buyer -> {

                if (emailString.get() == null) {
                    return true;
                }

                return isConfidence(buyer,nameString,phoneString,emailString);
            });
        });

        SortedList<BuyerTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(buyersTable.comparatorProperty());
        buyersTable.setItems(sortedData);

        buyersTable.setRowFactory(param -> {
            TableRow<BuyerTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(buyersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataBuyerTable = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(BuyerTable buyerTable, AtomicReference<String> nameString, AtomicReference<String> phoneString, AtomicReference<String> emailString) {
        return buyerTable.getName().contains(nameString.get()) &&
                buyerTable.getPhone().contains(phoneString.get()) &&
                buyerTable.getEmail().contains(emailString.get());
    }

    private void setFlags() {
        for (int i = 0; i < 3; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    void onAddEvent() {
        final BuyerTable buyerTable = new BuyerTable(nameField.getText(), phoneField.getText(), emailField.getText());
        if (EmailMessage.isSentMessage(ServerConfigs.LINK.getTitle() + ServerConfigs.PATH.getTitle(), emailField.getText(), ServerConfigs.GOAL.getTitle())) {
            WebServer.startWebServer(buyerTable, data);
            setFlags();
            clearFields();
            setRowDataNull();
        } else messageLabel.setText(MessageConfig.NOT_APPROVED.getTitle());
    }

    private void setRowDataNull() {
        rowDataBuyerTable =null;
    }

    private void prepareTableForChanges() {
        phoneList.remove(rowDataBuyerTable.getPhone());
    }

    private void resetChanges() {
        phoneList.add(rowDataBuyerTable.getPhone());
        setFlags();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteBuyerInf();
        updateRowDataBuyer();
        hideRstButton();
        addBuyerInf();
        databaseHandler.updateBuyer(rowDataBuyerTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataBuyer() {
        rowDataBuyerTable.setEmail(emailField.getText());
        rowDataBuyerTable.setPhone(phoneField.getText());
        rowDataBuyerTable.setName(nameField.getText());
    }

    private void clearFields() {
        nameField.setText(emptyString);
        phoneField.setText(emptyString);
        emailField.setText(emptyString);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf() {
        data.add(rowDataBuyerTable);
        phoneList.add(rowDataBuyerTable.getPhone());
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

    private void deleteBuyerInf() {
        data.remove(rowDataBuyerTable);
    }

    private void addBuyerToFields() {
        nameField.setText(rowDataBuyerTable.getName());
        phoneField.setText(rowDataBuyerTable.getPhone());
        emailField.setText(rowDataBuyerTable.getEmail());
    }
}