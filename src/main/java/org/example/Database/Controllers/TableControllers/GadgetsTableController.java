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
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Tables.Gadgets;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class GadgetsTableController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<GadgetTable, Integer> brandColumn;

    @FXML
    private ChoiceBox<BrandTable> brandBox;

    @FXML
    private TableView<GadgetTable> gadgetTable;

    @FXML
    private TableColumn<GadgetTable, Integer> costColumn;

    @FXML
    private Spinner<Integer> costSpinner;

    @FXML
    private ChoiceBox<CountryTable> countryBox;

    @FXML
    private TableColumn<GadgetTable, Integer> countryColumn;

    @FXML
    private TableColumn<GadgetTable, GadgetTable> deleteColumn;

    @FXML
    private TableColumn<GadgetTable, Integer> idColumn;

    @FXML
    private TableColumn<GadgetTable, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<GadgetTable, Integer> providerColumn;

    @FXML
    private ChoiceBox<ProviderTable> providerBox;

    @FXML
    private Button resetButton;

    @FXML
    private TableColumn<GadgetTable, Integer> serviceColumn;

    @FXML
    private Spinner<Integer> serviceSpinner;

    @FXML
    private TableColumn<GadgetTable, Integer> typeColumn;

    @FXML
    private ChoiceBox<TypeOfGadgetTable> typeBox;

    @FXML
    private TableColumn<GadgetTable, Integer> warrantyColumn;

    @FXML
    private Spinner<Integer> warrantySpinner;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet countries = databaseHandler.selectCountries();
    private final ResultSet brands = databaseHandler.selectBrands();
    private final ResultSet types = databaseHandler.selectTypes();
    private final ResultSet providers = databaseHandler.selectProviders();
    private final ResultSet gadgets = databaseHandler.selectGadgets();
    private final ObservableList<GadgetTable> data = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnSearch = FXCollections.observableArrayList();
    private final ObservableList<Boolean> flagsOnChange = FXCollections.observableArrayList();
    private final ArrayList<String> nameList = new ArrayList<>();
    private GadgetTable rowDataGadgetTable = null;
    private final String all = "All";
    private final int zero = 0;
    private final CountryTable zeroCountryTable = new CountryTable(zero, all);
    private final TypeOfGadgetTable zeroType = new TypeOfGadgetTable(zero, all);
    private final BrandTable zeroBrandTable = new BrandTable(zero, all);
    private final ProviderTable zeroProviderTable = new ProviderTable(0, all);
    ObservableList<ProviderTable> providerTableList =FXCollections.observableArrayList();
    ObservableList<TypeOfGadgetTable> typeList=FXCollections.observableArrayList();
    ObservableList<BrandTable> brandTableList =FXCollections.observableArrayList();
    ObservableList<CountryTable> countryTableList =FXCollections.observableArrayList();
    private final String emptyString="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillBoxes();

        hideRstButton();

        addFlags();

        AddInformation<ResultSet, ObservableList<GadgetTable>> information = (gadgets, data) -> {
            try {
                while (gadgets.next()) {
                    data.add(new GadgetTable(gadgets.getInt(1),
                            gadgets.getInt(2),
                            gadgets.getString(3),
                            gadgets.getInt(4),
                            gadgets.getInt(5),
                            gadgets.getInt(6),
                            gadgets.getInt(7),
                            gadgets.getInt(8),
                            gadgets.getInt(9)));
                    nameList.add(gadgets.getString(3));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(gadgets, data);

        flagsOnSearch.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(flagsOnSearch.contains(true)));
        flagsOnChange.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!flagsOnChange.contains(false)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        addButton.setOnAction(actionEvent -> onAddEvent());


        idColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.ID.getTitle()));

        typeColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.TYPE.getTitle()));
        typeColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getTypeByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.TYPES_OF_GADGETS.setScene((Stage) addButton.getScene().getWindow());
                    });
                }
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.NAME.getTitle()));

        brandColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.BRAND.getTitle()));
        brandColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getBrandByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.BRAND.setScene((Stage) addButton.getScene().getWindow());
                    });
                }
            }
        });

        countryColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.COUNTRY.getTitle()));
        countryColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getCountryByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.COUNTRIES.setScene((Stage) addButton.getScene().getWindow());
                    });
                }
            }
        });

        warrantyColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.WARRANTY.getTitle()));

        serviceColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.SERVICE_LIFE.getTitle()));

        costColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.COST.getTitle()));

        providerColumn.setCellValueFactory(new PropertyValueFactory<>(Gadgets.PROVIDER.getTitle()));
        providerColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                    setTooltip(new Tooltip(getProviderByID(item)));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.PROVIDERS.setScene((Stage) addButton.getScene().getWindow());
                    });
                }
            }
        });

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(GadgetTable gadgetTable, boolean empty) {
                super.updateItem(gadgetTable, empty);

                if (gadgetTable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    (new Thread(() -> databaseHandler.deleteGadget(gadgetTable))).start();
                    nameList.remove(gadgetTable.getName());

                    data.remove(gadgetTable);
                });
            }
        });

        gadgetTable.setItems(data);

        FilteredList<GadgetTable> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<Integer> typeValue = new AtomicReference<>(zero);
        AtomicReference<String> nameString = new AtomicReference<>(emptyString);
        AtomicReference<Integer> brandValue = new AtomicReference<>(zero);
        AtomicReference<Integer> countryValue = new AtomicReference<>(zero);
        AtomicReference<Integer> warrantyValue = new AtomicReference<>(zero);
        AtomicReference<Integer> serviceValue = new AtomicReference<>(zero);
        AtomicReference<Integer> costValue = new AtomicReference<>(zero);
        AtomicReference<Integer> providerValue = new AtomicReference<>(zero);

        typeBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            typeValue.set(newValue.getID());

            flagsOnSearch.set(0,typeValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(0, typeValue.get()== rowDataGadgetTable.getType());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            flagsOnSearch.set(1, nameString.get().equals(emptyString) || !nameString.get().matches("^([A-Z])([a-z]*)$") || nameList.contains(nameString.get()));

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(1, nameString.get().equals(rowDataGadgetTable.getName()));
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        brandBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            brandValue.set(newValue.getID());

            flagsOnSearch.set(2, brandValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(2, brandValue.get()== rowDataGadgetTable.getBrand());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        countryBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            countryValue.set(newValue.getID());

            flagsOnSearch.set(3, countryValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(3, countryValue.get()== rowDataGadgetTable.getCountry());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        warrantySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            warrantyValue.set(newValue);

            flagsOnSearch.set(4, warrantyValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(4, warrantyValue.get()== rowDataGadgetTable.getWarranty());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        serviceSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            serviceValue.set(newValue);

            flagsOnSearch.set(5, serviceValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(5, serviceValue.get()== rowDataGadgetTable.getServiceLife());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        costSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {

            costValue.set(newValue);

            flagsOnSearch.set(6, costValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(6, costValue.get()== rowDataGadgetTable.getCost());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        providerBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            providerValue.set(newValue.getID());

            flagsOnSearch.set(7, providerValue.get()==zero);

            if (rowDataGadgetTable != null) {
                flagsOnChange.set(7, providerValue.get()== rowDataGadgetTable.getProvider());
            }

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, warrantyValue, serviceValue, costValue, providerValue, gadget));
        });

        SortedList<GadgetTable> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(gadgetTable.comparatorProperty());
        gadgetTable.setItems(sortedData);

        gadgetTable.setRowFactory(param -> {
            TableRow<GadgetTable> row = new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().name().equals(MouseButton.PRIMARY.name()) && rowData.equals(gadgetTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataGadgetTable = rowData;
                    addBuyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isCoincidence(AtomicReference<Integer> typeValue, AtomicReference<String> nameValue, AtomicReference<Integer> brandValue, AtomicReference<Integer> countryValue, AtomicReference<Integer> warrantyValue, AtomicReference<Integer> serviceValue, AtomicReference<Integer> costValue, AtomicReference<Integer> providerValue, GadgetTable gadgetTable) {
        return (typeValue.get()==zero || typeValue.get()== gadgetTable.getType()) &&
                gadgetTable.getName().contains(nameValue.get()) &&
                (brandValue.get()==zero || brandValue.get()== gadgetTable.getBrand()) &&
                (countryValue.get()==zero || countryValue.get()== gadgetTable.getCountry()) &&
                (warrantyValue.get()==zero || warrantyValue.get()== gadgetTable.getWarranty()) &&
                (serviceValue.get()==zero || serviceValue.get()== gadgetTable.getServiceLife()) &&
                (costValue.get()==zero || costValue.get()== gadgetTable.getCost()) &&
                (providerValue.get()==zero || providerValue.get()== gadgetTable.getProvider());
    }

    private void addFlags() {
        for (int i = 0; i < 8; ++i) {
            flagsOnSearch.add(true);
            flagsOnChange.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetGadget(new GadgetTable(typeBox.getValue().getID(),
                nameField.getText(), brandBox.getValue().getID(),
                countryBox.getValue().getID(),
                warrantySpinner.getValue(),
                serviceSpinner.getValue(),
                costSpinner.getValue(),
                providerBox.getValue().getID())));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataGadgetTable=null;
    }

    private void prepareTableForChanges() {
        nameList.remove(rowDataGadgetTable.getName());
    }

    private void resetChanges() {
        nameList.add(rowDataGadgetTable.getName());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteGadgetInf();
        updateRowDataGadget();
        hideRstButton();
        addGadgetInf();
        databaseHandler.updateGadget(rowDataGadgetTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataGadget() {
        rowDataGadgetTable.setType(typeBox.getValue().getID());
        rowDataGadgetTable.setName(nameField.getText());
        rowDataGadgetTable.setBrand(brandBox.getValue().getID());
        rowDataGadgetTable.setCountry(countryBox.getValue().getID());
        rowDataGadgetTable.setWarranty(warrantySpinner.getValue());
        rowDataGadgetTable.setServiceLife(serviceSpinner.getValue());
        rowDataGadgetTable.setCost(costSpinner.getValue());
        rowDataGadgetTable.setProvider(providerBox.getValue().getID());
    }

    private void clearFields() {
        typeBox.setValue(zeroType);
        nameField.setText(emptyString);
        brandBox.setValue(zeroBrandTable);
        countryBox.setValue(zeroCountryTable);
        warrantySpinner.getValueFactory().setValue(zero);
        serviceSpinner.getValueFactory().setValue(zero);
        costSpinner.getValueFactory().setValue(zero);
        providerBox.setValue(zeroProviderTable);
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addGadgetInf() {
        data.add(rowDataGadgetTable);
        nameList.add(rowDataGadgetTable.getName());
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

    private void deleteGadgetInf() {
        data.remove(rowDataGadgetTable);
    }

    private void addBuyerToFields() {
        typeBox.setValue(findType());
        nameField.setText(rowDataGadgetTable.getName());
        brandBox.setValue(findBrand());
        countryBox.setValue(findCountry());
        warrantySpinner.getValueFactory().setValue(rowDataGadgetTable.getWarranty());
        serviceSpinner.getValueFactory().setValue(rowDataGadgetTable.getServiceLife());
        costSpinner.getValueFactory().setValue(rowDataGadgetTable.getCost());
        providerBox.setValue(findProvider());
    }

    private TypeOfGadgetTable findType() {
        for (TypeOfGadgetTable type : typeBox.getItems()) {
            if (type.getID() == rowDataGadgetTable.getType()) return type;
        }

        return null;
    }

    private BrandTable findBrand() {
        for (BrandTable brandTable : brandBox.getItems()) {
            if (brandTable.getID() == rowDataGadgetTable.getBrand()) return brandTable;
        }

        return null;
    }

    private ProviderTable findProvider() {
        for (ProviderTable providerTable : providerBox.getItems()) {
            if (providerTable.getID() == rowDataGadgetTable.getProvider()) return providerTable;
        }

        return null;
    }

    private CountryTable findCountry() {
        for (CountryTable countryTable : countryBox.getItems()) {
            if (countryTable.getID() == rowDataGadgetTable.getCountry()) return countryTable;
        }

        return null;
    }

    private void addCountriesToBox() {
        try {
            while (countries.next()) {
                countryTableList.add(new CountryTable(countries.getInt(1), countries.getString(2)));
            }
            countryTableList.add(zeroCountryTable);
            countryBox.setItems(countryTableList);
            countryBox.setValue(zeroCountryTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addBrandsToBox() {
        try {
            while (brands.next()) {
                brandTableList.add(new BrandTable(brands.getInt(1), brands.getString(2)));
            }
            brandTableList.add(zeroBrandTable);
            brandBox.setItems(brandTableList);
            brandBox.setValue(zeroBrandTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTypesToBox() {
        try {
            while (types.next()) {
                typeList.add(new TypeOfGadgetTable(types.getInt(1), types.getString(2)));
            }
            typeList.add(zeroType);
            typeBox.setItems(typeList);
            typeBox.setValue(zeroType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addProvidersToBox() {
        try {
            while (providers.next()) {
                providerTableList.add(new ProviderTable(providers.getInt(1), providers.getString(2), providers.getString(3),providers.getString(4), providers.getInt(5)));
            }
            providerTableList.add(zeroProviderTable);
            providerBox.setItems(providerTableList);
            providerBox.setValue(zeroProviderTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillBoxes() {
        addProvidersToBox();
        addCountriesToBox();
        addBrandsToBox();
        addTypesToBox();
    }

    private String getTypeByID(int typeID) {
        for (TypeOfGadgetTable type : typeList) {
            if (type.getID()==typeID) return type.getType();
        }

        return null;
    }

    private String getBrandByID(int brandID) {
        for (BrandTable brandTable : brandTableList) {
            if (brandTable.getID()==brandID) return brandTable.getBrand();
        }

        return null;
    }

    private String getCountryByID(int countryID) {
        for (CountryTable countryTable : countryTableList) {
            if (countryTable.getID()==countryID) return countryTable.getCountry();
        }

        return null;
    }

    private String getProviderByID(int providerID) {
        for (ProviderTable providerTable : providerTableList) {
            if (providerTable.getID()==providerID) return "Name: "+ providerTable.getName()+"\n"+
                                                        "Phone: "+ providerTable.getPhone()+"\n"+
                                                        "Email: "+ providerTable.getEmail()+"\n"+
                                                        "Address: "+ providerTable.getCountry();
        }

        return null;
    }
}