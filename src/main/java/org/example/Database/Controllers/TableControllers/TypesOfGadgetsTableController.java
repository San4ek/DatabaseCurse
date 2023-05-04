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
import org.example.Database.Classes.ClassesForDatabase.Tables.TypeOfGadgetTable;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Controllers.ViewControllers.GadgetsViewController;
import org.example.Database.Enums.EnumsForDatabase.Tables.TypesOfGadgets;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TypesOfGadgetsTableController extends GadgetsViewController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<TypeOfGadgetTable, TypeOfGadgetTable> deleteColumn;

    @FXML
    private TableColumn<TypeOfGadgetTable, Integer> idColumn;

    @FXML
    private Button resetButton;

    @FXML
    private TableColumn<TypeOfGadgetTable, String> typeColumn;

    @FXML
    private TextField typeField;

    @FXML
    private TableView<TypeOfGadgetTable> typeTable;

    private final ArrayList<String> typeList = new ArrayList<>();
    private TypeOfGadgetTable rowDataType = null;
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange=FXCollections.observableArrayList();
    private final ObservableList<TypeOfGadgetTable> data = FXCollections.observableArrayList();
    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet types = databaseHandler.selectTypes();
    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<TypeOfGadgetTable>> information=(types, data) -> {
            try {
                while (types.next()) {
                    data.add(new TypeOfGadgetTable(types.getInt(1), types.getString(2)));
                    typeList.add(types.getString(2));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(types,data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        idColumn.setCellValueFactory(new PropertyValueFactory<>(TypesOfGadgets.ID.getTitle()));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>(TypesOfGadgets.TYPE.getTitle()));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(TypeOfGadgetTable type, boolean empty) {
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

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());

        FilteredList<TypeOfGadgetTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> typeString = new AtomicReference<>(emptyString);

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {

            typeString.set(newValue);

            flagsOnSearch.set(0, typeString.get().equals(emptyString) || typeList.contains(typeString.get()));

            if (rowDataType != null) {
                addButton.setDisable(rowDataType.getType().equals(typeString.get()) || flagsOnSearch.contains(true));
            }

            filteredData.setPredicate(type -> isCoincidence(typeString,type));
        });

        SortedList<TypeOfGadgetTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(typeTable.comparatorProperty());
        typeTable.setItems(sortedData);

        typeField.setTextFormatter(new TextFormatter<>(UnaryOperators.getTypeValidationFormatter()));

        typeTable.setRowFactory(param -> {
            TableRow<TypeOfGadgetTable> row = new TableRow<>();

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

    private boolean isCoincidence(AtomicReference<String> typeValue, TypeOfGadgetTable type) {
        return type.getType().toLowerCase().contains(typeValue.get().toLowerCase());
    }

    private void addFlags() {
        for (int i = 0; i < 1; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    private void convertAddToChg() {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> {
            deleteTypeInf();
            onChangeEvent(new TypeOfGadgetTable(rowDataType.getID(), typeField.getText()));
        });
    }

    private void deleteTypeInf() {
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

    private void prepareTableForChanges(TypeOfGadgetTable rowData) {
        typeList.remove(rowData.getType());
    }

    private void clearField() {
        typeField.setText(emptyString);
    }

    private void resetChanges() {
        typeList.add(rowDataType.getType());
        clearField();
        setRowDataNull();
    }

    private void onAddEvent() {
        data.add(databaseHandler.insertAndGetType(new TypeOfGadgetTable(typeField.getText())));
        clearField();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataType=null;
    }

    private void addBrandToField(TypeOfGadgetTable rowData) {
        typeField.setText(rowData.getType());
    }

    private void onChangeEvent(TypeOfGadgetTable type) {
        setRowDataNull();
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

    private void addBuyerInf(TypeOfGadgetTable type) {
        data.add(type);
        typeList.add(type.getType());
    }
}
