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
import org.example.Database.Classes.ClassesForDatabase.Tables.Buyer;
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

public class BuyersController implements Initializable {

    @FXML
    private Button resetButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Buyer> buyersTable;

    @FXML
    private TableColumn<Buyer, Buyer> deleteColumn;

    @FXML
    private TableColumn<Buyer, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<Buyer, Integer> idColumn;

    @FXML
    private TableColumn<Buyer, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Buyer, String> phoneColumn;

    @FXML
    private TextField phoneField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet buyers = databaseHandler.selectBuyers();
    private final ObservableList<Buyer> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();

    private final ArrayList<String> phoneList = new ArrayList<>();

    private Buyer rowDataBuyer = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        for (int i = 0; i < 3; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }

        AddInformation<ResultSet, ObservableList<Buyer>> information = (buyers, data) -> {
            try {
                while (buyers.next()) {
                    data.add(new Buyer(buyers.getInt(1),
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

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));

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
            protected void updateItem(Buyer buyer, boolean empty) {
                super.updateItem(buyer, empty);

                if (buyer == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteBuyer(buyer))).start();
                    phoneList.remove(buyer.getPhone());

                    data.remove(buyer);
                });
            }
        });

        FilteredList<Buyer> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>("");
        AtomicReference<String> phoneString = new AtomicReference<>("");
        AtomicReference<String> emailString = new AtomicReference<>("");

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(0, nameString.get() == null || !nameString.get().matches("^([A-Z])([a-z]*)$"));

            if (rowDataBuyer != null) {
                flagsOnChange.set(0,nameString.get().equalsIgnoreCase(rowDataBuyer.getName()));
            }

            filteredData.setPredicate(buyer -> {

                if (nameString.get() == null) {
                    return true;
                }

                return isConfidence(buyer,nameString,phoneString,emailString);
            });
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flagsOnSearch.set(1, phoneString.get() == null || phoneString.get().length() != 9 || phoneList.contains(phoneString.get()));

            if (rowDataBuyer != null) {
                flagsOnChange.set(1,phoneString.get().equals(rowDataBuyer.getPhone()));
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

            if (rowDataBuyer != null) {
                flagsOnChange.set(2,emailString.get().equals(rowDataBuyer.getEmail()));
            }

            filteredData.setPredicate(buyer -> {

                if (emailString.get() == null) {
                    return true;
                }

                return isConfidence(buyer,nameString,phoneString,emailString);
            });
        });

        SortedList<Buyer> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(buyersTable.comparatorProperty());
        buyersTable.setItems(sortedData);

        buyersTable.setRowFactory(param -> {
            TableRow<Buyer> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(buyersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataBuyer = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(Buyer buyer, AtomicReference<String> nameString, AtomicReference<String> phoneString, AtomicReference<String> emailString) {
        return buyer.getName().contains(nameString.get()) &&
                buyer.getPhone().contains(phoneString.get()) &&
                buyer.getEmail().contains(emailString.get());
    }

    void onAddEvent() {
        final Buyer buyer = new Buyer(nameField.getText(), phoneField.getText(), emailField.getText());
        if (EmailMessage.isSentMessage(ServerConfigs.LINK.getTitle() + ServerConfigs.PATH.getTitle(), emailField.getText(), ServerConfigs.GOAL.getTitle())) {
            WebServer.startWebServer(buyer, data);
            clearFields();
        } else messageLabel.setText(MessageConfig.NOT_APPROVED.getTitle());
    }

    private void prepareTableForChanges() {
        phoneList.remove(rowDataBuyer.getPhone());
    }

    private void resetChanges() {
        phoneList.add(rowDataBuyer.getPhone());
        flagsOnChange.setAll(false);
        rowDataBuyer = null;
    }

    private void onChangeEvent() {
        deleteBuyerInf();
        updateRowDataBuyer();
        hideRstButton();
        addBuyerInf();
        databaseHandler.updateBuyer(rowDataBuyer);
        convertChgToAdd();
        clearFields();
        rowDataBuyer = null;
    }

    private void updateRowDataBuyer() {
        rowDataBuyer.setEmail(emailField.getText());
        rowDataBuyer.setPhone(phoneField.getText());
        rowDataBuyer.setName(nameField.getText());
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf() {
        data.add(rowDataBuyer);
        phoneList.add(rowDataBuyer.getPhone());
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
        data.remove(rowDataBuyer);
    }

    private void addBuyerToFields() {
        nameField.setText(rowDataBuyer.getName());
        phoneField.setText(rowDataBuyer.getPhone());
        emailField.setText(rowDataBuyer.getEmail());
    }
}