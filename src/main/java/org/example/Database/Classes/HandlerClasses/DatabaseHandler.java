package org.example.Database.Classes.HandlerClasses;

import org.example.Database.Classes.ClassesForDatabase.Tables.*;
import org.example.Database.Enums.ConfigEnums.DatabaseConfigs;
import org.example.Database.Enums.EnumsForDatabase.Tables.*;

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

    public Brand insertAndGetBrand(Brand brand) {
        comandString = "INSERT INTO "+Tables.BRANDS.getTitle()+"("+ Brands.BRAND.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, brand.getBrand());
            prSt.executeUpdate();

            resSet=getBrandID(brand);
            resSet.next();
            brand.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return brand;
    }

    private ResultSet getBrandID(Brand brand) {
        comandString="SELECT * FROM "+Tables.BRANDS.getTitle()+" WHERE "+Brands.BRAND.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1,brand.getBrand());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void deleteBrand(Brand brand) {
        comandString="DELETE FROM "+Tables.BRANDS.getTitle()+" WHERE "+Brands.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(brand.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBrand(Brand brand) {
        System.out.println(brand);
        comandString = "UPDATE "+Tables.BRANDS.getTitle()+" SET "+Brands.BRAND.getTitle()+"=? WHERE "+Brands.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, brand.getBrand());
            prSt.setString(2, String.valueOf(brand.getID()));

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

    private ResultSet getBuyerID(Buyer buyer) {
        comandString="SELECT * FROM "+Tables.BUYERS.getTitle()+" WHERE "+ Buyers.PHONE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1,buyer.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public Buyer insertAndGetBuyer(Buyer buyer) {
        comandString = "INSERT INTO "+Tables.BUYERS.getTitle()+"("+Buyers.NAME.getTitle()+"," +
                Buyers.PHONE.getTitle()+","+Buyers.EMAIL.getTitle()+") VALUES (?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, buyer.getName());
            prSt.setString(2,buyer.getPhone());
            prSt.setString(3,buyer.getEmail());
            prSt.executeUpdate();

            resSet=getBuyerID(buyer);
            resSet.next();
            buyer.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buyer;
    }

    public void deleteBuyer(Buyer buyer) {
        comandString="DELETE FROM "+Tables.BUYERS.getTitle()+" WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(buyer.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBuyer(Buyer buyer) {
        comandString = "UPDATE "+Tables.BUYERS.getTitle()+" SET "+Buyers.NAME.getTitle()+"=?,"+Buyers.PHONE.getTitle()+"=?,"+Buyers.EMAIL.getTitle()+"=? WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, buyer.getName());
            prSt.setString(2,buyer.getPhone());
            prSt.setString(3,buyer.getEmail());
            prSt.setString(4, String.valueOf(buyer.getID()));

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

    public void deleteConsultant(Consultant consultant) {
        comandString="DELETE FROM "+Tables.CONSULTANTS.getTitle()+" WHERE "+Buyers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(consultant.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet getConsultantID(Consultant consultant) {
        comandString="SELECT * FROM "+Tables.CONSULTANTS.getTitle()+" WHERE "+ Consultants.PHONE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1,consultant.getPhone());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public Consultant insertAndGetConsultant(Consultant consultant) {
        comandString = "INSERT INTO "+Tables.CONSULTANTS.getTitle()+"("+ Consultants.NAME.getTitle()+"," +
                Consultants.PHONE.getTitle()+","+Consultants.RATING.getTitle()+") VALUES (?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, consultant.getName());
            prSt.setString(2,consultant.getPhone());
            prSt.setString(3, String.valueOf(consultant.getRating()));
            prSt.executeUpdate();

            resSet=getConsultantID(consultant);
            resSet.next();
            consultant.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultant;
    }

    public void updateConsultant(Consultant consultant) {
        comandString = "UPDATE "+Tables.CONSULTANTS.getTitle()+" SET "+Consultants.NAME+"=?,"+Consultants.PHONE+"=?,"+Consultants.RATING+"=? WHERE "+Consultants.ID+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, consultant.getName());
            prSt.setString(2, consultant.getPhone());
            prSt.setString(3, String.valueOf(consultant.getRating()));
            prSt.setString(4, String.valueOf(consultant.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void deleteCountry(Country country) {
        comandString="DELETE FROM "+Tables.COUNTRIES.getTitle()+" WHERE "+ Countries.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(country.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Country insertAndGetCountry(Country countryOfManufacture) {
        comandString = "INSERT INTO "+Tables.COUNTRIES.getTitle()+"("+ Countries.COUNTRY.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryOfManufacture.getCountry());
            prSt.executeUpdate();

            resSet=getCountryID(countryOfManufacture);
            resSet.next();
            countryOfManufacture.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryOfManufacture;
    }
    private ResultSet getCountryID(Country countryOfManufacture) {
        comandString="SELECT * FROM "+Tables.COUNTRIES.getTitle()+" WHERE "+ Countries.COUNTRY.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryOfManufacture.getCountry());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void updateCountry(Country country) {
        comandString = "UPDATE "+Tables.COUNTRIES.getTitle()+" SET "+ Countries.COUNTRY.getTitle()+"=? WHERE "+ Countries.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, country.getCountry());
            prSt.setString(2, String.valueOf(country.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void deleteType(TypeOfGadget type) {
        comandString="DELETE FROM "+Tables.TYPES.getTitle()+" WHERE "+ TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TypeOfGadget insertAndGetType(TypeOfGadget type) {
        comandString = "INSERT INTO "+Tables.TYPES.getTitle()+"("+ TypesOfGadgets.TYPE.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());
            prSt.executeUpdate();

            resSet=getTypeID(type);
            resSet.next();
            type.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return type;
    }

    private ResultSet getTypeID(TypeOfGadget type) {
        comandString="SELECT * FROM "+Tables.TYPES.getTitle()+" WHERE "+ TypesOfGadgets.TYPE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void updateType(TypeOfGadget type) {
        comandString = "UPDATE "+Tables.TYPES.getTitle()+" SET "+TypesOfGadgets.TYPE.getTitle()+"=? WHERE "+TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());
            prSt.setString(2, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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

    public void deleteProvider(Provider provider) {
        comandString="DELETE FROM "+Tables.PROVIDERS.getTitle()+" WHERE "+ Providers.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(provider.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Provider insertAndGetProvider(Provider provider) {
        comandString = "INSERT INTO "+Tables.PROVIDERS.getTitle()+"("+Providers.NAME.getTitle()+","+
                Providers.PHONE_NUMBER.getTitle()+","+
                Providers.EMAIL.getTitle()+","+
                Providers.COUNTRY.getTitle()+") VALUES (?,?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, provider.getName());
            prSt.setString(2, provider.getPhone());
            prSt.setString(3, provider.getEmail());
            prSt.setString(4, String.valueOf(provider.getCountry()));
            prSt.executeUpdate();

            resSet=insertProviderID(provider);
            resSet.next();
            provider.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return provider;
    }

    private ResultSet insertProviderID(Provider provider) {
        comandString="SELECT * FROM "+Tables.PROVIDERS.getTitle()+" WHERE "+ Providers.NAME.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, provider.getName());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void updateProvider(Provider provider) {
        comandString = "UPDATE "+Tables.PROVIDERS.getTitle()+" SET "+
                Providers.NAME.getTitle()+"=?,"+
                Providers.PHONE_NUMBER.getTitle()+"=?,"+
                Providers.EMAIL.getTitle()+"=?,"+
                Providers.COUNTRY.getTitle()+"=? WHERE "+Gadgets.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, provider.getName());
            prSt.setString(2, provider.getPhone());
            prSt.setString(3, provider.getEmail());
            prSt.setString(4, String.valueOf(provider.getCountry()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        comandString = "INSERT INTO "+Tables.PURCHASES.getTitle()+"("+Purchases.GADGET.getTitle()+","+
                Purchases.DATE.getTitle()+","+
                Purchases.PAYMENT.getTitle()+","+
                Purchases.BUYER.getTitle()+","+
                Purchases.CONSULTANT.getTitle()+") VALUES (?,?,?,?,?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getGadget()));
            prSt.setString(2, String.valueOf(purchaseTable.getDate()));
            prSt.setString(3, String.valueOf(purchaseTable.getPayment()));
            prSt.setString(4, String.valueOf(purchaseTable.getBuyer()));
            prSt.setString(5, String.valueOf(purchaseTable.getConsultant()));
            prSt.executeUpdate();

            resSet=insertPurchaseID(purchaseTable);
            resSet.afterLast();
            resSet.previous();
            purchaseTable.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchaseTable;
    }

    private ResultSet insertPurchaseID(PurchaseTable purchaseTable) {
        comandString="SELECT * FROM "+Tables.PURCHASES.getTitle()+" WHERE "+ Purchases.DATE.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(purchaseTable.getDate()));

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public ResultSet selectPayments() {
        comandString="SELECT * FROM "+Tables.PAYMENTS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deletePayment(Payment payment) {
        comandString="DELETE FROM "+Tables.PAYMENTS.getTitle()+" WHERE "+ Payments.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(payment.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Payment insertAndGetPayment(Payment payment) {
        comandString = "INSERT INTO "+Tables.PAYMENTS.getTitle()+"("+Payments.PAYMENT.getTitle()+") VALUES (?)";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1,payment.getPayment());
            prSt.executeUpdate();

            resSet=insertPaymentID(payment);
            resSet.next();
            payment.setID(resSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payment;
    }

    private ResultSet insertPaymentID(Payment payment) {
        comandString="SELECT * FROM "+Tables.PAYMENTS.getTitle()+" WHERE "+ Payments.PAYMENT.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, payment.getPayment());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void updatePayment(Payment payment) {
        comandString = "UPDATE "+Tables.PAYMENTS.getTitle()+" SET "+
                Payments.PAYMENT.getTitle()+"=? WHERE "+Payments.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, payment.getPayment());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
