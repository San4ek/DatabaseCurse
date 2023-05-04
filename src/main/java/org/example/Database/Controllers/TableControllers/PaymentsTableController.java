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
import org.example.Database.Classes.ClassesForDatabase.Tables.PaymentTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Payments;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PaymentsTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<PaymentTable> paymentTable;

    @FXML
    private TableColumn<PaymentTable, PaymentTable> deleteColumn;

    @FXML
    private TableColumn<PaymentTable, Integer> idColumn;

    @FXML
    private TableColumn<PaymentTable, String> paymentColumn;

    @FXML
    private TextField paymentField;

    @FXML
    private Button resetButton;

    private final ObservableList<PaymentTable> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet payments= databaseHandler.selectPayments();
    private final ArrayList<String> paymentList = new ArrayList<>();
    private PaymentTable rowDataPaymentTable = null;
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();

    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<PaymentTable>> information=(payments, data) -> {
            try {
                while (payments.next()) {
                    data.add(new PaymentTable(payments.getInt(1), payments.getString(2)));
                    paymentList.add(payments.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(payments,data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Payments.ID.getTitle()));

        paymentColumn.setCellValueFactory(new PropertyValueFactory<>(Payments.PAYMENT.getTitle()));

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(PaymentTable paymentTable, boolean empty) {
                super.updateItem(paymentTable, empty);

                if (paymentTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(()->databaseHandler.deletePayment(paymentTable))).start();
                    paymentList.remove(paymentTable.getPayment());

                    data.remove(paymentTable);
                });
            }
        });

        FilteredList<PaymentTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> paymentString = new AtomicReference<>(emptyString);

        paymentField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        paymentField.textProperty().addListener((observable, oldValue, newValue) -> {

            paymentString.set(newValue);

            flagsOnSearch.set(0,paymentString.get().equals(emptyString) || paymentList.contains(paymentString.get()));

            if (rowDataPaymentTable != null) {
                flagsOnChange.set(0,paymentString.get().equals(rowDataPaymentTable.getPayment()));
            }

            filteredData.setPredicate(paymentTable -> isCoincidence(paymentTable,paymentString));
        });

        SortedList<PaymentTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(paymentTable.comparatorProperty());
        paymentTable.setItems(sortedData);

        paymentTable.setRowFactory(param -> {
            TableRow<PaymentTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(paymentTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataPaymentTable = rowData;
                    addPaymentToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(PaymentTable paymentTable, AtomicReference<String> paymentString) {
        return paymentTable.getPayment().toLowerCase().contains(paymentString.get().toLowerCase());
    }

    private void addFlags() {
        for (int i = 0; i < 1; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetPayment(new PaymentTable(paymentField.getText())));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataPaymentTable =null;
    }

    private void prepareTableForChanges() {
        paymentList.remove(rowDataPaymentTable.getPayment());
    }

    private void resetChanges() {
        paymentList.add(rowDataPaymentTable.getPayment());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent(PaymentTable paymentTable) {
        setRowDataNull();
        hideRstButton();
        addGadgetInf(paymentTable);
        databaseHandler.updatePayment(paymentTable);
        convertChgToAdd();
        clearFields();
    }

    private void clearFields() {
        paymentField.setText(emptyString);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addGadgetInf(PaymentTable paymentTable) {
        data.add(paymentTable);
        paymentList.add(paymentTable.getPayment());
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
        addButton.setOnAction(actionEvent -> {
            deleteGadgetInf();
            rowDataPaymentTable.setPayment(paymentField.getText());
            onChangeEvent(rowDataPaymentTable);
        });
    }

    private void deleteGadgetInf() {
        data.remove(rowDataPaymentTable);
    }

    private void addPaymentToFields() {
        paymentField.setText(rowDataPaymentTable.getPayment());
    }
}
