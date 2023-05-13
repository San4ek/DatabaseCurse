package org.example.Database.Classes.HandlerClasses;

import org.example.Database.Classes.ClassesForDatabase.Tables.*;
import org.example.Database.Enums.ConfigEnums.DatabaseConfigs;
import org.example.Database.Enums.EnumsForDatabase.Tables.*;
import org.example.Database.Enums.EnumsForDatabase.Views.Views;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class DatabaseHandler {
    private Connection connection;
    private ResultSet resSet;
    private PreparedStatement prSt;

    private String comandString;

    public Connection getConnection() {
        String connectionString = "jdbc:mysql://" + DatabaseConfigs.HOST.getTitle() + ":" + DatabaseConfigs.PORT.getTitle() + "/" + DatabaseConfigs.NAME.getTitle();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(connectionString, DatabaseConfigs.USER.getTitle(), DatabaseConfigs.PASSWORD.getTitle());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public ResultSet selectBrands() {
        comandString = "SELECT * FROM "+ Tables.BRANDS.getTitle();
        try {
            prSt = getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public BrandTable insertAndGetBrand(BrandTable brandTable) {
        comandString = "INSERT INTO "+Tables.BRANDS.getTitle()+"("+ Brands.BRAND.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());
            prSt.executeUpdate();

            resSet=getBrandID(brandTable);
            resSet.next();
            brandTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brandTable;
    }

    private ResultSet getBrandID(BrandTable brandTable) {
        comandString="SELECT * FROM "+Tables.BRANDS.getTitle()+" WHERE "+Brands.BRAND.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void deleteBrand(BrandTable brandTable) {
        comandString="DELETE FROM "+Tables.BRANDS.getTitle()+" WHERE "+Brands.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(brandTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBrand(BrandTable brandTable) {
        System.out.println(brandTable);
        comandString = "UPDATE "+Tables.BRANDS.getTitle()+" SET "+Brands.BRAND.getTitle()+"=? WHERE "+Brands.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, brandTable.getBrand());
            prSt.setString(2, String.valueOf(brandTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectBuyers() {
        comandString="SELECT * FROM "+Tables.BUYERS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    private ResultSet getBuyerID(BuyerTable buyerTable) {
        comandString="SELECT * FROM "+Tables.BUYERS.getTitle()+" WHERE "+ Buyers.PHONE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public BuyerTable insertAndGetBuyer(BuyerTable buyerTable) {
        comandString = "INSERT INTO "+Tables.BUYERS.getTitle()+"("+Buyers.NAME.getTitle()+"," +
                Buyers.PHONE.getTitle()+","+Buyers.EMAIL.getTitle()+") VALUES (?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getName());
            prSt.setString(2, buyerTable.getPhone());
            prSt.setString(3, buyerTable.getEmail());
            prSt.executeUpdate();

            resSet=getBuyerID(buyerTable);
            resSet.next();
            buyerTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyerTable;
    }

    public void deleteBuyer(BuyerTable buyerTable) {
        comandString="DELETE FROM "+Tables.BUYERS.getTitle()+" WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(buyerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBuyer(BuyerTable buyerTable) {
        comandString = "UPDATE "+Tables.BUYERS.getTitle()+" SET "+Buyers.NAME.getTitle()+"=?,"+Buyers.PHONE.getTitle()+"=?,"+Buyers.EMAIL.getTitle()+"=? WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, buyerTable.getName());
            prSt.setString(2, buyerTable.getPhone());
            prSt.setString(3, buyerTable.getEmail());
            prSt.setString(4, String.valueOf(buyerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet selectConsultants() {
        comandString="SELECT * FROM "+Tables.CONSULTANTS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteConsultant(ConsultantTable consultantTable) {
        comandString="DELETE FROM "+Tables.CONSULTANTS.getTitle()+" WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(consultantTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet getConsultantID(ConsultantTable consultantTable) {
        comandString="SELECT * FROM "+Tables.CONSULTANTS.getTitle()+" WHERE "+ Consultants.PHONE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public ConsultantTable insertAndGetConsultant(ConsultantTable consultantTable) {
        comandString = "INSERT INTO "+Tables.CONSULTANTS.getTitle()+"("+ Consultants.NAME.getTitle()+"," +
                Consultants.PHONE.getTitle()+","+Consultants.RATING.getTitle()+") VALUES (?,?,?)";

        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getName());
            prSt.setString(2, consultantTable.getPhone());
            prSt.setString(3, String.valueOf(consultantTable.getRating()));
            prSt.executeUpdate();

            resSet=getConsultantID(consultantTable);
            resSet.next();
            consultantTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return consultantTable;
    }

    public void updateConsultant(ConsultantTable consultantTable) {
        comandString = "UPDATE "+Tables.CONSULTANTS.getTitle()+" SET "+Consultants.NAME+"=?,"+Consultants.PHONE+"=?,"+Consultants.RATING+"=? WHERE "+Consultants.ID+"=?";

        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, consultantTable.getName());
            prSt.setString(2, consultantTable.getPhone());
            prSt.setString(3, String.valueOf(consultantTable.getRating()));
            prSt.setString(4, String.valueOf(consultantTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectCountries() {
        comandString="SELECT * FROM "+Tables.COUNTRIES.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteCountry(CountryTable countryTable) {
        comandString="DELETE FROM "+Tables.COUNTRIES.getTitle()+" WHERE "+ Countries.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(countryTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CountryTable insertAndGetCountry(CountryTable countryTableOfManufacture) {
        comandString = "INSERT INTO "+Tables.COUNTRIES.getTitle()+"("+ Countries.COUNTRY.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryTableOfManufacture.getCountry());
            prSt.executeUpdate();

            resSet=getCountryID(countryTableOfManufacture);
            resSet.next();
            countryTableOfManufacture.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return countryTableOfManufacture;
    }
    private ResultSet getCountryID(CountryTable countryTableOfManufacture) {
        comandString="SELECT * FROM "+Tables.COUNTRIES.getTitle()+" WHERE "+ Countries.COUNTRY.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryTableOfManufacture.getCountry());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateCountry(CountryTable countryTable) {
        comandString = "UPDATE "+Tables.COUNTRIES.getTitle()+" SET "+ Countries.COUNTRY.getTitle()+"=? WHERE "+ Countries.ID.getTitle()+"=?";

        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryTable.getCountry());
            prSt.setString(2, String.valueOf(countryTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectTypes() {
        comandString="SELECT * FROM "+Tables.TYPES.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteType(TypeOfGadgetTable type) {
        comandString="DELETE FROM "+Tables.TYPES.getTitle()+" WHERE "+ TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TypeOfGadgetTable insertAndGetType(TypeOfGadgetTable type) {
        comandString = "INSERT INTO "+Tables.TYPES.getTitle()+"("+ TypesOfGadgets.TYPE.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());
            prSt.executeUpdate();

            resSet=getTypeID(type);
            resSet.next();
            type.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return type;
    }

    private ResultSet getTypeID(TypeOfGadgetTable type) {
        comandString="SELECT * FROM "+Tables.TYPES.getTitle()+" WHERE "+ TypesOfGadgets.TYPE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateType(TypeOfGadgetTable type) {
        comandString = "UPDATE "+Tables.TYPES.getTitle()+" SET "+TypesOfGadgets.TYPE.getTitle()+"=? WHERE "+TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());
            prSt.setString(2, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectProviders() {
        comandString="SELECT * FROM "+Tables.PROVIDERS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteGadget(GadgetTable gadgetTable) {
        comandString="DELETE FROM "+Tables.GADGETS.getTitle()+" WHERE "+ Gadgets.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GadgetTable insertAndGetGadget(GadgetTable gadgetTable) {
        comandString = "INSERT INTO "+Tables.GADGETS.getTitle()+"("+Gadgets.TYPE.getTitle()+","+
                Gadgets.NAME.getTitle()+","+
                Gadgets.BRAND.getTitle()+","+
                Gadgets.COUNTRY.getTitle()+","+
                Gadgets.WARRANTY.getTitle()+","+
                Gadgets.SERVICE_LIFE.getTitle()+","+
                Gadgets.COST.getTitle()+","+
                Gadgets.PROVIDER.getTitle()+") VALUES (?,?,?,?,?,?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getType()));
            prSt.setString(2, gadgetTable.getName());
            prSt.setString(3, String.valueOf(gadgetTable.getBrand()));
            prSt.setString(4, String.valueOf(gadgetTable.getCountry()));
            prSt.setString(5, String.valueOf(gadgetTable.getWarranty()));
            prSt.setString(6, String.valueOf(gadgetTable.getServiceLife()));
            prSt.setString(7, String.valueOf(gadgetTable.getCost()));
            prSt.setString(8, String.valueOf(gadgetTable.getProvider()));
            prSt.executeUpdate();

            resSet=getGadgetID(gadgetTable);
            resSet.next();
            gadgetTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gadgetTable;
    }

    private ResultSet getGadgetID(GadgetTable gadgetTable) {
        comandString="SELECT * FROM "+Tables.GADGETS.getTitle()+" WHERE "+ Gadgets.NAME.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, gadgetTable.getName());

            resSet=prSt.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateGadget(GadgetTable gadgetTable) {
        comandString = "UPDATE "+Tables.GADGETS.getTitle()+" SET "+
                Gadgets.TYPE.getTitle()+"=?,"+
                Gadgets.NAME.getTitle()+"=?,"+
                Gadgets.BRAND.getTitle()+"=?,"+
                Gadgets.COUNTRY.getTitle()+"=?,"+
                Gadgets.SERVICE_LIFE.getTitle()+"=?,"+
                Gadgets.COST.getTitle()+"=?,"+
                Gadgets.PROVIDER.getTitle()+"=? WHERE "+Gadgets.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(gadgetTable.getType()));
            prSt.setString(2, gadgetTable.getName());
            prSt.setString(3, String.valueOf(gadgetTable.getBrand()));
            prSt.setString(4, String.valueOf(gadgetTable.getCountry()));
            prSt.setString(5, String.valueOf(gadgetTable.getWarranty()));
            prSt.setString(6, String.valueOf(gadgetTable.getServiceLife()));
            prSt.setString(7, String.valueOf(gadgetTable.getCost()));
            prSt.setString(8, String.valueOf(gadgetTable.getType()));
            prSt.setString(9,String.valueOf(gadgetTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectGadgets() {
        comandString="SELECT * FROM "+Tables.GADGETS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteProvider(ProviderTable providerTable) {
        comandString="DELETE FROM "+Tables.PROVIDERS.getTitle()+" WHERE "+ Providers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(providerTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProviderTable insertAndGetProvider(ProviderTable providerTable) {
        comandString = "INSERT INTO "+Tables.PROVIDERS.getTitle()+"("+Providers.NAME.getTitle()+","+
                Providers.PHONE_NUMBER.getTitle()+","+
                Providers.EMAIL.getTitle()+","+
                Providers.COUNTRY.getTitle()+") VALUES (?,?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());
            prSt.setString(2, providerTable.getPhone());
            prSt.setString(3, providerTable.getEmail());
            prSt.setString(4, String.valueOf(providerTable.getCountry()));
            prSt.executeUpdate();

            resSet=insertProviderID(providerTable);
            resSet.next();
            providerTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return providerTable;
    }

    private ResultSet insertProviderID(ProviderTable providerTable) {
        comandString="SELECT * FROM "+Tables.PROVIDERS.getTitle()+" WHERE "+ Providers.NAME.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updateProvider(ProviderTable providerTable) {
        comandString = "UPDATE " + Tables.PROVIDERS.getTitle() + " SET " +
                Providers.NAME.getTitle() + "=?," +
                Providers.PHONE_NUMBER.getTitle() + "=?," +
                Providers.EMAIL.getTitle() + "=?," +
                Providers.COUNTRY.getTitle() + "=? WHERE " + Gadgets.ID.getTitle() + "=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, providerTable.getName());
            prSt.setString(2, providerTable.getPhone());
            prSt.setString(3, providerTable.getEmail());
            prSt.setString(4, String.valueOf(providerTable.getCountry()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectPurchases() {
        comandString="SELECT * FROM "+Tables.PURCHASES.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deletePurchase(PurchaseTable purchaseTable) {
        comandString="DELETE FROM "+Tables.PURCHASES.getTitle()+" WHERE "+ Purchases.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PurchaseTable insertAndGetPurchase(PurchaseTable purchaseTable) {
        comandString = "INSERT INTO " + Tables.PURCHASES.getTitle() + "(" + Purchases.GADGET.getTitle() + "," +
                Purchases.DATE.getTitle() + "," +
                Purchases.PAYMENT.getTitle() + "," +
                Purchases.BUYER.getTitle() + "," +
                Purchases.CONSULTANT.getTitle() + ") VALUES (?,?,?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getGadget()));
            prSt.setString(2, String.valueOf(purchaseTable.getDate()));
            prSt.setString(3, String.valueOf(purchaseTable.getPayment()));
            prSt.setString(4, String.valueOf(purchaseTable.getBuyer()));
            prSt.setString(5, String.valueOf(purchaseTable.getConsultant()));
            prSt.executeUpdate();

            resSet = selectPurchases();
            int id=0;
            while (resSet.next()) {
                id= resSet.getInt(1);
            }
            purchaseTable.setID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return purchaseTable;
    }

    public ResultSet selectPayments() {
        comandString = "SELECT * FROM " + Tables.PAYMENTS.getTitle();
        try {
            prSt = getConnection().prepareStatement(comandString);

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deletePayment(PaymentTable paymentTable) {
        comandString = "DELETE FROM " + Tables.PAYMENTS.getTitle() + " WHERE " + Payments.ID.getTitle() + "=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(paymentTable.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PaymentTable insertAndGetPayment(PaymentTable paymentTable) {
        comandString = "INSERT INTO " + Tables.PAYMENTS.getTitle() + "(" + Payments.PAYMENT.getTitle() + ") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, paymentTable.getPayment());
            prSt.executeUpdate();

            resSet = insertPaymentID(paymentTable);
            resSet.next();
            paymentTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return paymentTable;
    }

    private ResultSet insertPaymentID(PaymentTable paymentTable) {
        comandString = "SELECT * FROM " + Tables.PAYMENTS.getTitle() + " WHERE " + Payments.PAYMENT.getTitle() + "=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, paymentTable.getPayment());

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void updatePayment(PaymentTable paymentTable) {
        comandString = "UPDATE " + Tables.PAYMENTS.getTitle() + " SET " +
                Payments.PAYMENT.getTitle() + "=? WHERE " + Payments.ID.getTitle() + "=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, paymentTable.getPayment());

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet selectPurchasesView() {
        comandString = "SELECT * FROM " + Views.GADGETS.getTitle();
        try {
            prSt = getConnection().prepareStatement(comandString);

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }
}
