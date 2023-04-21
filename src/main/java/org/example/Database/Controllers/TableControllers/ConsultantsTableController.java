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
import org.example.Database.Classes.ClassesForDatabase.Tables.ConsultantTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Consultants;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ConsultantsTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<ConsultantTable> consultantsTable;

    @FXML
    private TableColumn<ConsultantTable, ConsultantTable> deleteColumn;

    @FXML
    private TableColumn<ConsultantTable, Integer> idColumn;

    @FXML
    private TableColumn<ConsultantTable, Integer> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<ConsultantTable, String> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private TableColumn<ConsultantTable, Integer> ratingColumn;

    @FXML
    private Spinner<Double> ratingSpinner;

    @FXML
    private Button resetButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet consultants = databaseHandler.selectConsultants();
    private final ObservableList<ConsultantTable> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private final ArrayList<String> phoneList = new ArrayList<>();
    private ConsultantTable rowDataConsultantTable = null;
    private final double nullSpinnerValue=0.0;
    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        setFlags();

        AddInformation<ResultSet, ObservableList<ConsultantTable>> information = (consultants, data) -> {
            try {
                while (consultants.next()) {
                    data.add(new ConsultantTable(consultants.getInt(1),
                            consultants.getString(2),
                            consultants.getString(3),
                            consultants.getDouble(4)));
                    phoneList.add(consultants.getString(3));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(consultants, data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        idColumn.setCellValueFactory(new PropertyValueFactory<>(Consultants.ID.getTitle()));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Consultants.NAME.getTitle()));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Consultants.PHONE.getTitle()));

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>(Consultants.RATING.getTitle()));

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(ConsultantTable consultantTable, boolean empty) {
                super.updateItem(consultantTable, empty);

                if (consultantTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteConsultant(consultantTable))).start();
                    phoneList.remove(consultantTable.getPhone());

                    data.remove(consultantTable);
                });
            }
        });

        FilteredList<ConsultantTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>(emptyString);
        AtomicReference<String> phoneString = new AtomicReference<>(emptyString);
        AtomicReference<Double> ratingValue = new AtomicReference<>(nullSpinnerValue);

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(0, nameString.get().equals(emptyString) || !nameString.get().matches("^([A-Z])([a-z]*)$"));

            if (rowDataConsultantTable != null) {
                flagsOnChange.set(0,nameString.get().equals(rowDataConsultantTable.getName()));
            }

            filteredData.setPredicate(consultantTable -> isConfidence(consultantTable,nameString,phoneString,ratingValue));
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flagsOnSearch.set(1, phoneString.get().equals(emptyString) || phoneString.get().length() != 9 || phoneList.contains(phoneString.get()));

            if (rowDataConsultantTable != null) {
                flagsOnChange.set(1,phoneString.get().equals(rowDataConsultantTable.getPhone()));
            }

            filteredData.setPredicate(consultantTable -> isConfidence(consultantTable,nameString,phoneString,ratingValue));
        });

        ratingSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            ratingValue.set(newValue);

            flagsOnSearch.set(2, ratingValue.get() == nullSpinnerValue);

            if (rowDataConsultantTable != null) {
                flagsOnChange.set(2,ratingValue.get()== rowDataConsultantTable.getRating());
                System.out.println(flagsOnChange);
            }

            filteredData.setPredicate(consultantTable -> isConfidence(consultantTable,nameString,phoneString,ratingValue));
        });

        SortedList<ConsultantTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(consultantsTable.comparatorProperty());
        consultantsTable.setItems(sortedData);

        consultantsTable.setRowFactory(param -> {
            TableRow<ConsultantTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(consultantsTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataConsultantTable = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(ConsultantTable consultantTable, AtomicReference<String> nameString, AtomicReference<String> phoneString, AtomicReference<Double> ratingValue) {
        return (ratingValue.get()==nullSpinnerValue || consultantTable.getRating() == ratingValue.get()) &&
                consultantTable.getName().contains(nameString.get()) &&
                consultantTable.getPhone().contains(phoneString.get());
    }

    private void setFlags() {
        for (int i = 0; i < 3; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetConsultant(new ConsultantTable(nameField.getText(), phoneField.getText(), ratingSpinner.getValue())));
        setFlags();
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataConsultantTable =null;
    }

    private void prepareTableForChanges() {
        phoneList.remove(rowDataConsultantTable.getPhone());
    }

    private void resetChanges() {
        phoneList.add(rowDataConsultantTable.getPhone());
        setFlags();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteBuyerInf();
        updateRowDataConsultant();
        hideRstButton();
        addBuyerInf();
        databaseHandler.updateConsultant(rowDataConsultantTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataConsultant() {
        rowDataConsultantTable.setName(nameField.getText());
        rowDataConsultantTable.setPhone(phoneField.getText());
        rowDataConsultantTable.setRating(ratingSpinner.getValue());
    }

    private void clearFields() {
        nameField.setText(emptyString);
        phoneField.setText(emptyString);
        ratingSpinner.getValueFactory().setValue(nullSpinnerValue);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf() {
        data.add(rowDataConsultantTable);
        phoneList.add(rowDataConsultantTable.getPhone());
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
        data.remove(rowDataConsultantTable);
    }

    private void addBuyerToFields() {
        nameField.setText(rowDataConsultantTable.getName());
        phoneField.setText(rowDataConsultantTable.getPhone());
        ratingSpinner.getValueFactory().setValue(rowDataConsultantTable.getRating());
    }
}