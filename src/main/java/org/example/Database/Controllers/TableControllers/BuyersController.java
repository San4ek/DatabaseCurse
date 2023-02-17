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
import org.example.Database.Classes.ClassesForDatabase.Buyer;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Classes.HandlerClasses.EmailMessage;
import org.example.Database.Classes.HandlerClasses.WebServer;
import org.example.Database.Enums.ConfigEnums.MessageConfig;
import org.example.Database.Enums.ConfigEnums.ServerConfigs;
import org.example.Database.Enums.EnumsForDatabase.Buyers;
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
    private final ObservableList<Boolean> flags = FXCollections.observableArrayList();
    private final ArrayList<String> phoneList = new ArrayList<>();

    private Buyer rowDataBuyer = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        for (int i = 0; i < 3; ++i) {
            flags.add(true);
        }

        flags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flags.contains(true)));

        addButton.setDisable(true);

        backButton.setOnAction(actionEvent -> Scenes.MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

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

        buyersTable.setItems(data);

        FilteredList<Buyer> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>("");
        AtomicReference<String> phoneString = new AtomicReference<>("");
        AtomicReference<String> emailString = new AtomicReference<>("");

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            nameString.set(newValue);

            flags.set(0, newValue == null || !newValue.matches("^([A-Z])([a-z]*)$"));

            if (rowDataBuyer != null) {
                addButton.setDisable((rowDataBuyer.getName().equals(newValue) &&
                        rowDataBuyer.getPhone().equals(phoneString.get()) &&
                        rowDataBuyer.getEmail().equals(emailString.get())) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(buyer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return buyer.getName().contains(newValue) &&
                        buyer.getPhone().contains(phoneString.get()) &&
                        buyer.getEmail().contains(emailString.get());
            });
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flags.set(1, newValue == null || newValue.length() != 9 || phoneList.contains(newValue));

            if (rowDataBuyer != null) {
                addButton.setDisable((rowDataBuyer.getName().equals(nameString.get()) &&
                        rowDataBuyer.getPhone().equals(newValue) &&
                        rowDataBuyer.getEmail().equals(emailString.get())) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(buyer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return buyer.getPhone().contains(newValue) &&
                        buyer.getName().contains(nameString.get()) &&
                        buyer.getEmail().contains(emailString.get());
            });
        });

        emailField.setTextFormatter(new TextFormatter<>(UnaryOperators.getEmailValidationFormatter()));
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            emailString.set(newValue);

            flags.set(2, newValue == null || newValue.isEmpty() || !newValue.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[ a-zA-Z0-9.-]+$"));

            if (rowDataBuyer != null) {
                addButton.setDisable((rowDataBuyer.getName().equals(nameString.get()) &&
                        rowDataBuyer.getPhone().equals(phoneString.get()) &&
                        rowDataBuyer.getEmail().equals(newValue)) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(buyer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return buyer.getEmail().contains(newValue) &&
                        buyer.getName().contains(nameString.get()) &&
                        buyer.getPhone().contains(phoneString.get());
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
                    addBuyerToFields(rowData);
                    prepareTableForChanges(rowData);
                    convertAddToChg(row);
                }
            }));

            return row;
        });
    }

    void onAddEvent() {
        final Buyer buyer = new Buyer(nameField.getText(), phoneField.getText(), emailField.getText());
        if (EmailMessage.isSentMessage(ServerConfigs.LINK.getTitle() + ServerConfigs.PATH.getTitle(), emailField.getText(), ServerConfigs.GOAL.getTitle())) {
            WebServer.startWebServer(buyer, data);
            clearFields();
        } else messageLabel.setText(MessageConfig.NOT_APPROVED.getTitle());
    }

    private void prepareTableForChanges(Buyer rowData) {
        phoneList.remove(rowData.getPhone());
        flags.set(1, false);
    }

    private void resetChanges() {
        phoneList.add(rowDataBuyer.getPhone());
        rowDataBuyer = null;
    }

    private void onChangeEvent(Buyer buyer) {
        rowDataBuyer = null;
        hideRstButton();
        addBuyerInf(buyer);
        databaseHandler.updateBuyer(buyer);
        convertChgToAdd();
        clearFields();
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

    private void addBuyerInf(Buyer buyer) {
        data.add(buyer);
        phoneList.add(buyer.getPhone());
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void convertAddToChg(TableRow<Buyer> row) {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> {
            deleteBuyerInf(row);
            onChangeEvent(new Buyer(rowDataBuyer.getID(), nameField.getText(),
                    phoneField.getText(),
                    emailField.getText()));
        });
    }

    private void deleteBuyerInf(TableRow<Buyer> row) {
        row.setDisable(false);
        data.remove(rowDataBuyer);
    }

    private void addBuyerToFields(Buyer rowData) {
        nameField.setText(rowData.getName());
        phoneField.setText(rowData.getPhone());
        emailField.setText(rowData.getEmail());
    }
}