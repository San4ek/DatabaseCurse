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
import org.example.Database.Classes.ClassesForDatabase.TypeOfGadget;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.TypesOfGadgets;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TypesOfGadgetsController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<TypeOfGadget, TypeOfGadget> deleteColumn;

    @FXML
    private TableColumn<TypeOfGadget, Integer> idColumn;

    @FXML
    private Button resetButton;

    @FXML
    private TableColumn<TypeOfGadget, String> typeColumn;

    @FXML
    private TextField typeField;

    @FXML
    private TableView<TypeOfGadget> typeTable;

    private final ArrayList<String> typeList = new ArrayList<>();
    private TypeOfGadget rowDataType = null;
    private final ObservableList<Boolean> flags = FXCollections.observableArrayList();
    private final ObservableList<TypeOfGadget> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet types = databaseHandler.selectTypes();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideRstButton();

        flags.add(true);

        AddInformation<ResultSet, ObservableList<TypeOfGadget>> information=(types, data) -> {
            try {
                while (types.next()) {
                    data.add(new TypeOfGadget(types.getInt(1), types.getString(2)));
                    typeList.add(types.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(types,data);

        flags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flags.contains(true)));

        idColumn.setCellValueFactory(new PropertyValueFactory<>(TypesOfGadgets.ID.getTitle()));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>(TypesOfGadgets.TYPE.getTitle()));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(TypeOfGadget type, boolean empty) {
                super.updateItem(type, empty);

                if (type == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(()->databaseHandler.deleteType(type))).start();
                    typeList.remove(type.getType());

                    data.remove(type);
                });
            }
        });

        backButton.setOnAction(actionEvent -> Scenes.MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        FilteredList<TypeOfGadget> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> countryString = new AtomicReference<>("");

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {

            flags.set(0, newValue == null || newValue.isEmpty()|| typeList.contains(newValue));

            if (rowDataType != null) {
                addButton.setDisable(rowDataType.getType().equals(countryString.get()) || flags.contains(true));
            }

            filteredData.setPredicate(type -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                return type.getType().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<TypeOfGadget> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(typeTable.comparatorProperty());
        typeTable.setItems(sortedData);

        typeField.setTextFormatter(new TextFormatter<>(UnaryOperators.getTypeValidationFormatter()));

        typeTable.setRowFactory(param -> {
            TableRow<TypeOfGadget> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearField();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && rowData.equals(typeTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataType = rowData;
                    addBrandToField(rowData);
                    prepareTableForChanges(rowData);
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private void convertAddToChg() {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> {
            deleteBuyerInf();
            onChangeEvent(new TypeOfGadget(rowDataType.getID(), typeField.getText()));
        });
    }

    private void deleteBuyerInf() {
        data.remove(rowDataType);
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void prepareTableForChanges(TypeOfGadget rowData) {
        typeList.remove(rowData.getType());
        flags.set(0, false);
    }

    private void clearField() {
        typeField.clear();
    }

    private void resetChanges() {
        typeList.add(rowDataType.getType());
        rowDataType = null;
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetType(new TypeOfGadget(typeField.getText())));
        typeTable.setItems(data);
        typeField.clear();
    }

    private void addBrandToField(TypeOfGadget rowData) {
        typeField.setText(rowData.getType());
    }

    private void onChangeEvent(TypeOfGadget type) {
        rowDataType = null;
        hideRstButton();
        addBuyerInf(type);
        databaseHandler.updateType(type);
        convertChgToAdd();
        clearField();
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addBuyerInf(TypeOfGadget type) {
        data.add(type);
        typeList.add(type.getType());
    }
}
