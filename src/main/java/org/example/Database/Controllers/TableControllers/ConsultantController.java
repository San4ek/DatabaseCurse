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
import org.example.Database.Classes.ClassesForDatabase.Consultant;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Consultants;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class ConsultantController implements Initializable {

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
    private final ObservableList<Boolean> flags = FXCollections.observableArrayList();
    private final ArrayList<String> phoneList = new ArrayList<>();

    private Consultant rowDataConsultant = null;

    private final double nullSpinnerValue=0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        for (int i = 0; i < 3; ++i) {
            flags.add(true);
        }

        flags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flags.contains(true)));

        addButton.setDisable(true);

        addButton.setOnAction(actionEvent -> onAddEvent());

        backButton.setOnAction(actionEvent -> Scenes.MENU.setScene((Stage) backButton.getScene().getWindow()));

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

        consultantsTable.setItems(data);

        FilteredList<Consultant> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> nameString = new AtomicReference<>("");
        AtomicReference<String> phoneString = new AtomicReference<>("");
        AtomicReference<Double> ratingValue = new AtomicReference<>(nullSpinnerValue);

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flags.set(0, newValue == null || !newValue.matches("^([A-Z])([a-z]*)$"));

            if (rowDataConsultant != null) {
                addButton.setDisable((rowDataConsultant.getName().equals(newValue) &&
                        rowDataConsultant.getPhone().equals(phoneString.get()) &&
                        rowDataConsultant.getRating() == ratingValue.get()) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(consultant -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return consultant.getName().contains(newValue) &&
                        consultant.getPhone().contains(phoneString.get()) &&
                        (ratingValue.get()==0.0 || consultant.getRating() == ratingValue.get());
            });
        });

        phoneField.setTextFormatter(new TextFormatter<>(UnaryOperators.getPhoneValidationFormatter()));
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            phoneString.set(newValue);

            flags.set(1, newValue == null || newValue.length() != 9 || phoneList.contains(newValue));

            if (rowDataConsultant != null) {
                addButton.setDisable((rowDataConsultant.getName().equals(nameString.get()) &&
                        rowDataConsultant.getPhone().equals(newValue) &&
                        rowDataConsultant.getRating() == ratingValue.get()) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(consultant -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return consultant.getPhone().contains(newValue) &&
                        consultant.getName().contains(nameString.get()) &&
                        (ratingValue.get()==0.0 || consultant.getRating() == ratingValue.get());
            });
        });

        ratingSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            ratingValue.set(newValue);

            flags.set(2, newValue == nullSpinnerValue);

            if (rowDataConsultant != null) {
                addButton.setDisable((rowDataConsultant.getName().equals(nameString.get()) &&
                        rowDataConsultant.getPhone().equals(phoneString.get()) &&
                        rowDataConsultant.getRating() == ratingValue.get()) ||
                        flags.contains(true));
            }

            filteredData.setPredicate(consultant -> {
                    if (ratingValue.get()==nullSpinnerValue) {
                        return true;
                    }

                    return consultant.getRating() == ratingValue.get() &&
                    consultant.getName().contains(nameString.get()) &&
                    consultant.getPhone().contains(phoneString.get());
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
                    addBuyerToFields(rowData);
                    prepareTableForChanges(rowData);
                    convertAddToChg(row);
                }
            }));

            return row;
        });
    }

    private void onAddEvent() {
        final Consultant consultant = new Consultant(nameField.getText(), phoneField.getText(), ratingSpinner.getValue());
        data.add(databaseHandler.insertAndGetConsultant(consultant));
        clearFields();
    }

    private void prepareTableForChanges(Consultant rowData) {
        phoneList.remove(rowData.getPhone());
        flags.set(1, false);
    }

    private void resetChanges() {
        phoneList.add(rowDataConsultant.getPhone());
        rowDataConsultant = null;
    }

    private void onChangeEvent(Consultant consultant) {
        rowDataConsultant = null;
        hideRstButton();
        addBuyerInf(consultant);
        databaseHandler.updateConsultant(consultant);
        convertChgToAdd();
        clearFields();
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

    private void addBuyerInf(Consultant consultant) {
        data.add(consultant);
        phoneList.add(consultant.getPhone());
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void convertAddToChg(TableRow<Consultant> row) {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> {
            deleteBuyerInf(row);
            onChangeEvent(new Consultant(rowDataConsultant.getID(), nameField.getText(),
                    phoneField.getText(),
                    ratingSpinner.getValue()));
        });
    }

    private void deleteBuyerInf(TableRow<Consultant> row) {
        row.setDisable(false);
        data.remove(rowDataConsultant);
    }

    private void addBuyerToFields(Consultant rowData) {
        nameField.setText(rowData.getName());
        phoneField.setText(rowData.getPhone());
        ratingSpinner.getValueFactory().setValue(rowData.getRating());
    }
}