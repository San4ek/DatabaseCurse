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
import org.example.Database.Classes.ClassesForDatabase.Tables.BrandTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.CountryTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.ProviderTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.TypeOfGadgetTable;
import org.example.Database.Classes.ClassesForDatabase.Views.GadgetView;
import org.example.Database.Classes.ConfigClasses.UnaryOperators;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForDatabase.Views.GadgetsView;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.example.Database.Interfaces.AddInformation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class GadgetsViewController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<GadgetView, String> brandColumn;

    @FXML
    private ChoiceBox<BrandTable> brandBox;

    @FXML
    private TableView<GadgetView> gadgetTable;

    @FXML
    private ChoiceBox<CountryTable> countryBox;

    @FXML
    private TableColumn<GadgetView, String> countryColumn;

    @FXML
    private TableColumn<GadgetView, Integer> idColumn;

    @FXML
    private TableColumn<GadgetView, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<GadgetView, String> providerColumn;

    @FXML
    private ChoiceBox<ProviderTable> providerBox;

    @FXML
    private TableColumn<GadgetView, String> typeColumn;

    @FXML
    private ChoiceBox<TypeOfGadgetTable> typeBox;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet countries = databaseHandler.selectCountries();
    private final ResultSet brands = databaseHandler.selectBrands();
    private final ResultSet types = databaseHandler.selectTypes();
    private final ResultSet providers = databaseHandler.selectProviders();
    private final ResultSet gadgets = databaseHandler.selectGadgets();
    private final ObservableList<GadgetView> data = FXCollections.observableArrayList();
    private final String all = "All";
    private final int zero = 0;
    private final CountryTable zeroCountryTable = new CountryTable(zero, all);
    private final TypeOfGadgetTable zeroType = new TypeOfGadgetTable(zero, all);
    private final BrandTable zeroBrandTable = new BrandTable(zero, all);
    private final ProviderTable zeroProviderTable = new ProviderTable(0, all);
    private final ObservableList<ProviderTable> providerTableList =FXCollections.observableArrayList();
    private final ObservableList<TypeOfGadgetTable> typeList=FXCollections.observableArrayList();
    private final ObservableList<BrandTable> brandTableList =FXCollections.observableArrayList();
    private final ObservableList<CountryTable> countryTableList =FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillBoxes();

        AddInformation<ResultSet, ObservableList<GadgetView>> information = (gadgets, data) -> {
            try {
                while (gadgets.next()) {
                    data.add(new GadgetView(gadgets.getInt(1),
                            gadgets.getString(2),
                            gadgets.getString(3),
                            gadgets.getString(4),
                            gadgets.getString(5),
                            gadgets.getString(6)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        information.addInf(gadgets, data);

        backButton.setOnAction(actionEvent -> Scenes.VIEWS_MENU.setScene((Stage) backButton.getScene().getWindow()));

        idColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.ID.getTitle()));

        typeColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.TYPE.getTitle()));
        typeColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.TYPES_OF_GADGETS.setScene((Stage) backButton.getScene().getWindow());
                    });
                }
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.NAME.getTitle()));

        brandColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.BRAND.getTitle()));
        brandColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.BRAND.setScene((Stage) backButton.getScene().getWindow());
                    });
                }
            }
        });

        countryColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.COUNTRY.getTitle()));
        countryColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.COUNTRIES.setScene((Stage) backButton.getScene().getWindow());
                    });
                }
            }
        });

        providerColumn.setCellValueFactory(new PropertyValueFactory<>(GadgetsView.PROVIDER.getTitle()));
        providerColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setTooltip(new Tooltip(item));
                    setOnMouseClicked(event -> {
                        if (event.getButton().name().equals(MouseButton.SECONDARY.name()) && event.getClickCount()==2) Scenes.PROVIDERS.setScene((Stage) backButton.getScene().getWindow());
                    });
                }
            }
        });

        gadgetTable.setItems(data);

        FilteredList<GadgetView> filteredData = new FilteredList<>(data, b -> true);

        AtomicReference<String> typeValue = new AtomicReference<>(all);
        AtomicReference<String> nameString = new AtomicReference<>(all);
        AtomicReference<String> brandValue = new AtomicReference<>(all);
        AtomicReference<String> countryValue = new AtomicReference<>(all);
        AtomicReference<String> providerValue = new AtomicReference<>(all);

        typeBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            typeValue.set(newValue.getType());

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, providerValue, gadget));
        });

        nameField.setTextFormatter(new TextFormatter<>(UnaryOperators.getNameValidationFormatter()));
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {

            nameString.set(newValue);

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, providerValue, gadget));
        });

        brandBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            brandValue.set(newValue.getBrand());

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, providerValue, gadget));
        });

        countryBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            countryValue.set(newValue.getCountry());

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, providerValue, gadget));
        });

        providerBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            providerValue.set(newValue.getName());

            filteredData.setPredicate(gadget -> isCoincidence(typeValue, nameString, brandValue, countryValue, providerValue, gadget));
        });

        SortedList<GadgetView> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(gadgetTable.comparatorProperty());
        gadgetTable.setItems(sortedData);
    }

    private boolean isCoincidence(AtomicReference<String> typeValue, AtomicReference<String> nameValue, AtomicReference<String> brandValue, AtomicReference<String> countryValue, AtomicReference<String> providerValue, GadgetView gadgetView) {
        return (typeValue.get().equals(all) || typeValue.get().equals(gadgetView.getType())) &&
                gadgetView.getName().contains(nameValue.get()) &&
                (brandValue.get().equals(all) || brandValue.get().equals(gadgetView.getBrand())) &&
                (countryValue.get().equals(all) || countryValue.get().equals(gadgetView.getCountry())) &&
                (providerValue.get().equals(all) || providerValue.get().equals(gadgetView.getProvider()));
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
}
