package org.example.Database.Controllers.ViewControllers;

import javafx.collections.FXCollections;
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
import org.example.Database.Classes.ClassesForDatabase.Views.PurchaseView;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Views.PurchasesView;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PurchasesViewController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ChoiceBox<BuyerTable> buyerBox;

    @FXML
    private TableColumn<PurchaseView, String> buyerColumn;

    @FXML
    private ChoiceBox<ConsultantTable> consultantBox;

    @FXML
    private TableColumn<PurchaseView, String> consultantColumn;

    @FXML
    private TableColumn<PurchaseView, LocalDate> dateColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<GadgetTable> gadgetBox;

    @FXML
    private TableColumn<PurchaseView, String> gadgetColumn;

    @FXML
    private TableColumn<PurchaseView, Integer> idColumn;

    @FXML
    private ChoiceBox<PaymentTable> paymentBox;

    @FXML
    private TableColumn<PurchaseView, String> paymentColumn;

    @FXML
    private TableView<PurchaseView> purchaseViewTable;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet purchasesView = databaseHandler.selectPurchasesView();
    private final ResultSet gadgets=databaseHandler.selectGadgets();
    private final ResultSet payments = databaseHandler.selectPayments();
    private final ResultSet buyers = databaseHandler.selectBuyers();
    private final ResultSet consultants = databaseHandler.selectConsultants();
    private final ObservableList<GadgetTable> gadgetTableList = FXCollections.observableArrayList();
    private final ObservableList<BuyerTable> buyerTableList =FXCollections.observableArrayList();
    private final ObservableList<PaymentTable> paymentTableList = FXCollections.observableArrayList();
    private final ObservableList<ConsultantTable> consultantTableList =FXCollections.observableArrayList();
    private final ObservableList<PurchaseView> data = FXCollections.observableArrayList();
    private final String all = "All";
    private final int zero = 0;
    private final GadgetTable zeroGadgetTable = new GadgetTable(zero,all);
    private final PaymentTable zeroPaymentTable = new PaymentTable(zero,all);
    private final BuyerTable zeroBuyerTable =new BuyerTable(zero,all);
    private final ConsultantTable zeroConsultantTable =new ConsultantTable(zero,all);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillBoxes();

        AddInformation<ResultSet, ObservableList<PurchaseView>> information = (purchasesView, data) -> {
            try {
                while (purchasesView.next()) {
                    data.add(new PurchaseView(purchasesView.getInt(1),
                            purchasesView.getString(2),
                            purchasesView.getDate(3).toLocalDate(),
                            purchasesView.getString(4),
                            purchasesView.getString(5),
                            purchasesView.getString(6)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(purchasesView, data);

        backButton.setOnAction(actionEvent -> Scenes.VIEWS_MENU.setScene((Stage) backButton.getScene().getWindow()));

        idColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.ID.getTitle()));

        gadgetColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.GADGET.getTitle()));
        gadgetColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.GADGETS.setScene((Stage) backButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.DATE.getTitle()));

        paymentColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.PAYMENT.getTitle()));
        paymentColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.PAYMENTS.setScene((Stage) backButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        buyerColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.BUYER.getTitle()));
        buyerColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.BUYERS.setScene((Stage) backButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        consultantColumn.setCellValueFactory(new PropertyValueFactory<>(PurchasesView.CONSULTANT.getTitle()));
        consultantColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) {
                            Scenes.CONSULTANTS.setScene((Stage) backButton.getScene().getWindow());
                        }
                    });
                }
            }
        });

        FilteredList<PurchaseView> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> gadgetValue = new AtomicReference<>(all);
        AtomicReference<LocalDate> dateValue=new AtomicReference<>(null);
        AtomicReference<String> paymentValue = new AtomicReference<>(all);
        AtomicReference<String> buyerValue = new AtomicReference<>(all);
        AtomicReference<String> consultantValue = new AtomicReference<>(all);

        gadgetBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            gadgetValue.set(newValue.getName());

            filteredData.setPredicate(purchaseView -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseView));
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

            filteredData.setPredicate(purchaseView -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseView));
        });

        paymentBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            paymentValue.set(newValue.getPayment());

            filteredData.setPredicate(purchaseView -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseView));
        });

        buyerBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            buyerValue.set(newValue.getName());

            filteredData.setPredicate(purchaseView -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseView));
        });

        consultantBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            consultantValue.set(newValue.getName());

            filteredData.setPredicate(purchaseView -> isCoincidence(gadgetValue,dateValue,paymentValue,buyerValue,consultantValue, purchaseView));
        });

        SortedList<PurchaseView> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(purchaseViewTable.comparatorProperty());
        purchaseViewTable.setItems(sortedData);
    }

    private boolean isCoincidence(AtomicReference<String> gadgetValue, AtomicReference<LocalDate> dateValue, AtomicReference<String> paymentValue, AtomicReference<String> buyerValue, AtomicReference<String> consultantValue, PurchaseView purchaseView) {
        return (gadgetValue.get().equals(all) || gadgetValue.get().equals(purchaseView.getGadget())) &&
                (dateValue.get()==null || dateValue.get()==purchaseView.getDate()) &&
                (paymentValue.get().equals(all) || paymentValue.get().equals(purchaseView.getPayment())) &&
                (buyerValue.get().equals(all) || buyerValue.get().equals(purchaseView.getBuyer())) &&
                (consultantValue.get().equals(all) || consultantValue.get().equals(purchaseView.getConsultant()));
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
}
