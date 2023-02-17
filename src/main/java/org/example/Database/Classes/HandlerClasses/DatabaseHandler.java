package org.example.Database.Classes.HandlerClasses;

import org.example.Database.Classes.ClassesForDatabase.*;
import org.example.Database.Enums.EnumsForDatabase.*;
import org.example.Database.Enums.ConfigEnums.DatabaseConfigs;

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
        comandString = "UPDATE "+Tables.BRANDS+" SET "+Brands.BRAND+"=? WHERE "+Brands.ID+"=?";
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
        comandString = "UPDATE "+Tables.BUYERS+" SET "+Buyers.NAME+"=?,"+Buyers.PHONE+"=?,"+Buyers.EMAIL+"=? WHERE "+Buyers.ID+"=?";
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
        comandString = "UPDATE "+Tables.CONSULTANTS+" SET "+Consultants.NAME+"=?,"+Consultants.PHONE+"=?,"+Consultants.RATING+"=? WHERE "+Consultants.ID+"=?";
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
        comandString="SELECT * FROM "+Tables.COUNTRIES_OF_MANUFACTURE.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteCountry(CountryOfManufacture country) {
        comandString="DELETE FROM "+Tables.COUNTRIES_OF_MANUFACTURE.getTitle()+" WHERE "+ CountriesOfManufacture.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(country.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CountryOfManufacture insertAndGetCountry(CountryOfManufacture countryOfManufacture) {
        comandString = "INSERT INTO "+Tables.COUNTRIES_OF_MANUFACTURE.getTitle()+"("+ CountriesOfManufacture.COUNTRY.getTitle()+") VALUES (?)";
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
    private ResultSet getCountryID(CountryOfManufacture countryOfManufacture) {
        comandString="SELECT * FROM "+Tables.COUNTRIES_OF_MANUFACTURE.getTitle()+" WHERE "+ CountriesOfManufacture.COUNTRY.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, countryOfManufacture.getCountry());

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void updateCountry(CountryOfManufacture country) {
        comandString = "UPDATE "+Tables.COUNTRIES_OF_MANUFACTURE+" SET "+CountriesOfManufacture.COUNTRY.getTitle()+"=? WHERE "+CountriesOfManufacture.ID.getTitle()+"=?";
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
        comandString="SELECT * FROM "+Tables.TYPES_OF_GADGETS.getTitle();
        try {
            prSt= getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resSet;
    }

    public void deleteType(TypeOfGadget type) {
        comandString="DELETE FROM "+Tables.TYPES_OF_GADGETS.getTitle()+" WHERE "+ TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public TypeOfGadget insertAndGetType(TypeOfGadget type) {
        comandString = "INSERT INTO "+Tables.TYPES_OF_GADGETS.getTitle()+"("+ TypesOfGadgets.TYPE.getTitle()+") VALUES (?)";
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
        comandString="SELECT * FROM "+Tables.TYPES_OF_GADGETS.getTitle()+" WHERE "+ TypesOfGadgets.TYPE.getTitle()+"=?";
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
        comandString = "UPDATE "+Tables.TYPES_OF_GADGETS+" SET "+TypesOfGadgets.TYPE.getTitle()+"=? WHERE "+TypesOfGadgets.ID.getTitle()+"=?";
        try {
            prSt = getConnection().prepareStatement(comandString);
            prSt.setString(1, type.getType());
            prSt.setString(2, String.valueOf(type.getID()));

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
