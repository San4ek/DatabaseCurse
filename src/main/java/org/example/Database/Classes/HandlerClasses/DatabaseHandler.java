package org.example.Database.Classes.HandlerClasses;

import org.example.Database.Classes.ClassesForDatabase.Buyer;
import org.example.Database.Enums.EnumsForDatabase.Brands;
import org.example.Database.Enums.EnumsForDatabase.Buyers;
import org.example.Database.Classes.ClassesForDatabase.Brand;
import org.example.Database.Enums.ConfigEnums.DatabaseConfigs;
import org.example.Database.Enums.EnumsForDatabase.Tables;

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
        comandString="DELETE FROM "+Tables.BRANDS.getTitle()+" WHERE "+Brands.BRAND.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, brand.getBrand());

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        comandString="DELETE FROM "+Tables.BUYERS.getTitle()+" WHERE "+Buyers.NAME.getTitle()+"=?";
        try {
            prSt= getConnection().prepareStatement(comandString);
            prSt.setString(1, buyer.getName());

            prSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getBuyersPhone() {
        comandString="SELECT "+Buyers.PHONE+" FROM "+Tables.BUYERS.getTitle();
        try {
            prSt = getConnection().prepareStatement(comandString);

            resSet=prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resSet;
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
}
