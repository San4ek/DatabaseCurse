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
import org.example.Database.Classes.ClassesForDatabase.Tables.Consultant;
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

public class ConsultantsController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<Consultant> consultantsTable;

    @FXML
    private TableColumn<Consultant, Consultant> deleteColumn;

    @FXML
    private TableColumn<Consultant, Integer> idColumn;

    @FXML
    private TableColumn<Consultant, Integer> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Consultant, String> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private TableColumn<Consultant, Integer> ratingColumn;

    @FXML
    private Spinner<Double> ratingSpinner;

    @FXML
    private Button resetButton;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet consultants = databaseHandler.selectConsultants();
    private final ObservableList<Consultant> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private final ArrayList<String> phoneList = new ArrayList<>();
    private Consultant rowDataConsultant = null;
    private final double nullSpinnerValue=0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        for (int i = 0; i < 3; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }

        AddInformation<ResultSet, ObservableList<Consultant>> information = (consultants, data) -> {
            try {
                while (consultants.next()) {
                    data.add(new Consultant(consultants.getInt(1),
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

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnChange.contains(true) || flagsOnSearch.contains(true)));

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
            protected void updateItem(Consultant consultant, boolean empty) {
                super.updateItem(consultant, empty);

                if (consultant == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteConsultant(consultant))).start();
                    phoneList.remove(consultant.getPhone());

                    data.remove(consultant);
                });
            }
        });

        FilteredList<Consultant> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>("");
        AtomicReference<String> phoneString = new AtomicReference<>("");
        AtomicReference<Double> ratingValue = new AtomicReference<>(nullSpinnerValue);

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(0, nameString.get() == null || !nameString.get().matches("^([A-Z])([a-z]*)$"));

            if (rowDataConsultant != null) {
                flagsOnChange.set(0,nameString.get().equals(rowDataConsultant.getName()));
            }

            filteredData.setPredicate(consultant -> {

                if (nameString.get()==null) {
                    return true;
                }

                return isConfidence(consultant,nameString,phoneString,ratingValue);
            });
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flagsOnSearch.set(1, phoneString.get() == null || phoneString.get().length() != 9 || phoneList.contains(phoneString.get()));

            if (rowDataConsultant != null) {
                flagsOnChange.set(1,phoneString.get().equals(rowDataConsultant.getPhone()));
            }

            filteredData.setPredicate(consultant -> {

                if (phoneString.get()==null) {
                    return true;
                }

                return isConfidence(consultant,nameString,phoneString,ratingValue);
            });
        });

        ratingSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            ratingValue.set(newValue);

            flagsOnSearch.set(2, ratingValue.get() == nullSpinnerValue);

            if (rowDataConsultant != null) {
                flagsOnChange.set(2,ratingValue.get()==rowDataConsultant.getRating());
            }

            filteredData.setPredicate(consultant -> {
                    if (ratingValue.get()==nullSpinnerValue) {
                        return true;
                    }

                    return isConfidence(consultant,nameString,phoneString,ratingValue);
            });
        });

        SortedList<Consultant> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(consultantsTable.comparatorProperty());
        consultantsTable.setItems(sortedData);

        consultantsTable.setRowFactory(param -> {
            TableRow<Consultant> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(consultantsTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataConsultant = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(Consultant consultant, AtomicReference<String> nameString, AtomicReference<String> phoneString, AtomicReference<Double> ratingValue) {
        return consultant.getRating() == ratingValue.get() &&
                consultant.getName().contains(nameString.get()) &&
                consultant.getPhone().contains(phoneString.get());
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetConsultant(new Consultant(nameField.getText(), phoneField.getText(), ratingSpinner.getValue())));
        consultantsTable.setItems(data);
        flagsOnChange.setAll(false);
        clearFields();
    }

    private void prepareTableForChanges() {
        phoneList.remove(rowDataConsultant.getPhone());
    }

    private void resetChanges() {
        phoneList.add(rowDataConsultant.getPhone());
        flagsOnChange.setAll(false);
        rowDataConsultant = null;
    }

    private void onChangeEvent() {
        deleteBuyerInf();
        updateRowDataConsultant();
        hideRstButton();
        addBuyerInf();
        databaseHandler.updateConsultant(rowDataConsultant);
        convertChgToAdd();
        clearFields();
        rowDataConsultant = null;
    }

    private void updateRowDataConsultant() {
        rowDataConsultant.setName(nameField.getText());
        rowDataConsultant.setPhone(phoneField.getText());
        rowDataConsultant.setRating(ratingSpinner.getValue());
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        ratingSpinner.getValueFactory().setValue(nullSpinnerValue);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf() {
        data.add(rowDataConsultant);
        phoneList.add(rowDataConsultant.getPhone());
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
        data.remove(rowDataConsultant);
    }

    private void addBuyerToFields() {
        nameField.setText(rowDataConsultant.getName());
        phoneField.setText(rowDataConsultant.getPhone());
        ratingSpinner.getValueFactory().setValue(rowDataConsultant.getRating());
    }
}