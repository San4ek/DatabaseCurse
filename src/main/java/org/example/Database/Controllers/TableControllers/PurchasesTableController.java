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
import org.example.Database.Classes.ClassesForDatabase.Tables.*;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Purchases;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PurchasesTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<BuyerTable> buyerBox;

    @FXML
    private TableColumn<PurchaseTable, Integer> buyerColumn;

    @FXML
    private ChoiceBox<ConsultantTable> consultantBox;

    @FXML
    private TableColumn<PurchaseTable, Integer> consultantColumn;

    @FXML
    private TableColumn<PurchaseTable, LocalDate> dateColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<PurchaseTable, PurchaseTable> deleteColumn;

    @FXML
    private ChoiceBox<GadgetTable> gadgetBox;

    @FXML
    private TableColumn<PurchaseTable, Integer> gadgetColumn;

    @FXML
    private TableView<PurchaseTable> purchaseTable;

    @FXML
    private TableColumn<PurchaseTable, Integer> idColumn;

    @FXML
    private ChoiceBox<PaymentTable> paymentBox;

    @FXML
    private TableColumn<PurchaseTable, String> paymentColumn;

    @FXML
    private Button resetButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet purchases = databaseHandler.selectPurchases();
    private final ResultSet gadgets = databaseHandler.selectGadgets();
    private final ResultSet payments = databaseHandler.selectPayments();
    private final ResultSet buyers = databaseHandler.selectBuyers();
    private final ResultSet consultants = databaseHandler.selectConsultants();
    private final ObservableList<GadgetTable> gadgetTableList =FXCollections.observableArrayList();
    private final ObservableList<BuyerTable> buyerTableList =FXCollections.observableArrayList();
    private final ObservableList<PaymentTable> paymentTableList = FXCollections.observableArrayList();
    private final ObservableList<ConsultantTable> consultantTableList =FXCollections.observableArrayList();
    private final ObservableList<PurchaseTable> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private PurchaseTable rowDataPurchaseTable = null;
    private final String all = "All";
    private final int zero = 0;
    private final GadgetTable zeroGadgetTable = new GadgetTable(zero,all);
    private final PaymentTable zeroPaymentTable = new PaymentTable(zero,all);
    private final BuyerTable zeroBuyerTable =new BuyerTable(zero,all);
    private final ConsultantTable zeroConsultantTable =new ConsultantTable(zero,all);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillBoxes();

        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<PurchaseTable>> information = (purchases, data) -> {
            try {
                while (purchases.next()) {
                    data.add(new PurchaseTable(purchases.getInt(1),
                            purchases.getInt(2),
                            purchases.getDate(3).toLocalDate(),
                            purchases.getInt(4),
                            purchases.getInt(5),
                            purchases.getInt(6)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(purchases, data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        addButton.setDisable(true);

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.ID.getTitle()));

        gadgetColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.GADGET.getTitle()));
        gadgetColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getGadgetByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.GADGETS.setScene((Stage) addButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.DATE.getTitle()));

        paymentColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.PAYMENT.getTitle()));
        paymentColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(getPaymentByID(Integer.parseInt(item))));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.PAYMENTS.setScene((Stage) addButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        buyerColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.BUYER.getTitle()));
        buyerColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getBuyerByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.BUYERS.setScene((Stage) addButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        consultantColumn.setCellValueFactory(new PropertyValueFactory<>(Purchases.CONSULTANT.getTitle()));
        consultantColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getConsultantByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.CONSULTANTS.setScene((Stage) addButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(PurchaseTable purchaseTable, boolean empty) {
                super.updateItem(purchaseTable, empty);

                if (purchaseTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deletePurchase(purchaseTable))).start();

                    data.remove(purchaseTable);
                });
            }
        });

        FilteredList<PurchaseTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<Integer> gadgetValue = new AtomicReference<>(zero);
        AtomicReference<LocalDate> dateValue=new AtomicReference<>(null);
        AtomicReference<Integer> paymentValue = new AtomicReference<>(zero);
        AtomicReference<Integer> buyerValue = new AtomicReference<>(zero);
        AtomicReference<Integer> consultantValue = new AtomicReference<>(zero);

        gadgetBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            gadgetValue.set(newValue.getID());

            flagsOnSearch.set(0,gadgetValue.get()==zero);

            if (rowDataPurchaseTable != null) {
                flagsOnChange.set(0, gadgetValue.get()== rowDataPurchaseTable.getGadget());
            }

            filteredData.setPredicate(purchaseTable -> {

                if (gadgetValue.get()==zero) {
                    return true;
                }

                return isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseTable);
            });
        });

        datePicker.setEditable(false);
        datePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item.isAfter(LocalDate.now())) {
                    setStyle("-fx-background-color: #ffc0cb;");
                    setDisable(true);
                }
            }
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            dateValue.set(newValue);

            flagsOnSearch.set(1,dateValue.get()==null || !dateValue.get().equals(LocalDate.now()));

            filteredData.setPredicate(purchaseTable -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseTable));
        });

        paymentBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            paymentValue.set(newValue.getID());

            flagsOnSearch.set(2, paymentValue.get()==zero);

            if (rowDataPurchaseTable != null) {
                flagsOnChange.set(2, paymentValue.get() == rowDataPurchaseTable.getPayment());
            }

            filteredData.setPredicate(purchaseTable -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseTable));
        });

        buyerBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            buyerValue.set(newValue.getID());

            flagsOnSearch.set(3, buyerValue.get()==zero);

            if (rowDataPurchaseTable != null) {
                flagsOnChange.set(3, buyerValue.get()== rowDataPurchaseTable.getBuyer());
            }

            filteredData.setPredicate(purchaseTable -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseTable));
        });

        consultantBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            consultantValue.set(newValue.getID());

            flagsOnSearch.set(4, consultantValue.get()==zero);

            if (rowDataPurchaseTable != null) {
                flagsOnChange.set(4, consultantValue.get()== rowDataPurchaseTable.getConsultant());
            }

            filteredData.setPredicate(purchaseTable -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseTable));
        });

        SortedList<PurchaseTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(purchaseTable.comparatorProperty());
        purchaseTable.setItems(sortedData);

        purchaseTable.setRowFactory(param -> {
            TableRow<PurchaseTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().name().equals(MouseButton.PRIMARY.name()) && rowData.equals(purchaseTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataPurchaseTable = rowData;
                    addPurchaseToFields();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(AtomicReference<Integer> gadgetValue, AtomicReference<LocalDate> dateValue, AtomicReference<Integer> paymentValue, AtomicReference<Integer> buyerValue, AtomicReference<Integer> consultantValue, PurchaseTable purchaseTable) {
        return (gadgetValue.get()==zero || gadgetValue.get()== purchaseTable.getGadget()) &&
                (dateValue.get()==null || dateValue.get()==purchaseTable.getDate()) &&
                (paymentValue.get()==zero || paymentValue.get()== purchaseTable.getPayment()) &&
                (buyerValue.get()==zero || buyerValue.get()== purchaseTable.getBuyer()) &&
                (consultantValue.get()==zero || consultantValue.get()== purchaseTable.getConsultant());
    }

    private void addFlags() {
        for (int i = 0; i < 5; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetPurchase(new PurchaseTable(gadgetBox.getValue().getID(),
                LocalDate.now(),
                paymentBox.getValue().getID(),
                buyerBox.getValue().getID(),
                consultantBox.getValue().getID()
                )));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataPurchaseTable=null;
    }

    private void resetChanges() {
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deletePurchaseInf();
        updateRowDataPurchase();
        hideRstButton();
        addPurchaseInf();
        databaseHandler.deletePurchase(rowDataPurchaseTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataPurchase() {
        rowDataPurchaseTable.setGadget(gadgetBox.getValue().getID());
        rowDataPurchaseTable.setPayment(paymentBox.getValue().getID());
        rowDataPurchaseTable.setBuyer(buyerBox.getValue().getID());
        rowDataPurchaseTable.setConsultant(consultantBox.getValue().getID());
    }

    private void clearFields() {
        gadgetBox.setValue(zeroGadgetTable);
        paymentBox.setValue(zeroPaymentTable);
        datePicker.setDisable(false);
        setNullDate();
        buyerBox.setValue(zeroBuyerTable);
        consultantBox.setValue(zeroConsultantTable);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addPurchaseInf() {
        data.add(rowDataPurchaseTable);
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

    private void deletePurchaseInf() {
        data.remove(rowDataPurchaseTable);
    }

    private void addPurchaseToFields() {
        gadgetBox.setValue(findGadget());
        paymentBox.setValue(findPayment());
        datePicker.setValue(rowDataPurchaseTable.getDate());
        datePicker.setDisable(true);
        buyerBox.setValue(findBuyer());
        consultantBox.setValue(findConsultant());
    }

    private GadgetTable findGadget() {
        for (GadgetTable gadgetTable : gadgetBox.getItems()) {
            if (gadgetTable.getID() == rowDataPurchaseTable.getGadget()) return gadgetTable;
        }

        return null;
    }

    private PaymentTable findPayment() {
        for (PaymentTable paymentTable : paymentBox.getItems()) {
            if (paymentTable.getID() == rowDataPurchaseTable.getPayment()) return paymentTable;
        }

        return null;
    }

    private BuyerTable findBuyer() {
        for (BuyerTable buyerTable : buyerBox.getItems()) {
            if (buyerTable.getID() == rowDataPurchaseTable.getBuyer()) return buyerTable;
        }

        return null;
    }

    private ConsultantTable findConsultant() {
        for (ConsultantTable consultantTable : consultantBox.getItems()) {
            if (consultantTable.getID() == rowDataPurchaseTable.getConsultant()) return consultantTable;
        }

        return null;
    }

    private void addGadgetsToBox() {
        try {
            while (gadgets.next()) {
                gadgetTableList.add(new GadgetTable(gadgets.getInt(1), gadgets.getString(2)));
            }
            gadgetTableList.add(zeroGadgetTable);
            gadgetBox.setItems(gadgetTableList);
            gadgetBox.setValue(zeroGadgetTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setNullDate() {
        datePicker.setValue(null);
    }

    private void addPaymentsToBox() {
        try {
            while (payments.next()) {
                paymentTableList.add(new PaymentTable(payments.getInt(1), payments.getString(2)));
            }
            paymentTableList.add(zeroPaymentTable);
            paymentBox.setItems(paymentTableList);
            paymentBox.setValue(zeroPaymentTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBuyersToBox() {
        try {
            while (buyers.next()) {
                buyerTableList.add(new BuyerTable(buyers.getInt(1), buyers.getString(2)));
            }
            buyerTableList.add(zeroBuyerTable);
            buyerBox.setItems(buyerTableList);
            buyerBox.setValue(zeroBuyerTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addConsultantsToBox() {
        try {
            while (consultants.next()) {
                consultantTableList.add(new ConsultantTable(consultants.getInt(1),
                        consultants.getString(2),
                        consultants.getString(3),
                        consultants.getDouble(4)));
            }
            consultantTableList.add(zeroConsultantTable);
            consultantBox.setItems(consultantTableList);
            consultantBox.setValue(zeroConsultantTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillBoxes() {
        addConsultantsToBox();
        addGadgetsToBox();
        setNullDate();
        addBuyersToBox();
        addPaymentsToBox();
    }

    private String getGadgetByID(int gadgetID) {
        for (GadgetTable gadgetTable : gadgetTableList) {
            if (gadgetTable.getID()==gadgetID) return gadgetTable.getName();
        }

        return null;
    }

    private String getPaymentByID(int paymentD) {
        for (PaymentTable paymentTable : paymentTableList) {
            if (paymentTable.getID()==paymentD) return paymentTable.getPayment();
        }

        return null;
    }

    private String getBuyerByID(int buyerID) {
        for (BuyerTable buyerTable : buyerTableList) {
            if (buyerTable.getID()==buyerID) return "Name: "+ buyerTable.getName()+"\n"+
                    "Phone: "+ buyerTable.getPhone()+"\n"+
                    "Email: "+ buyerTable.getEmail();
        }

        return null;
    }

    private String getConsultantByID(int consultantID) {
        for (ConsultantTable consultantTable : consultantTableList) {
            if (consultantTable.getID()==consultantID) return "Name: "+ consultantTable.getName()+"\n"+
                    "Phone: "+ consultantTable.getPhone()+"\n"+
                    "Rating: "+ consultantTable.getRating();
        }

        return null;
    }
}
