package org.example.Database.Classes.ClassesForDatabase.Tables;

public class GadgetTable {
    private int ID=0;
    private int Type=0;
    private String Name;
    private int Brand=0;
    private int Country=0;
    private int Warranty=0;
    private int ServiceLife=0;
    private int Cost=0;
    private int Provider=0;

    public GadgetTable(int ID, int type, String name, int brand, int countryOfManufacture, int warranty, int serviceLife, int cost, int provider) {
        this.ID = ID;
        Type = type;
        Name = name;
        Brand = brand;
        Country = countryOfManufacture;
        Warranty = warranty;
        ServiceLife = serviceLife;
        Cost = cost;
        Provider = provider;
    }

    public GadgetTable(int type, String name, int brand, int country, int warranty, int serviceLife, int cost, int provider) {
        Type = type;
        Name = name;
        Brand = brand;
        Country = country;
        Warranty = warranty;
        ServiceLife = serviceLife;
        Cost = cost;
        Provider = provider;
    }

    public GadgetTable(int ID, String name) {
        this.ID=ID;
        Name=name;
    }

    public int getID() {
        return ID;
    }

    public int getType() {
        return Type;
    }

    public String getName() {
        return Name;
    }

    public int getBrand() {
        return Brand;
    }

    public int getCountry() {
        return Country;
    }

    public int getWarranty() {
        return Warranty;
    }

    public int getServiceLife() {
        return ServiceLife;
    }

    public int getCost() {
        return Cost;
    }

    public int getProvider() {
        return Provider;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBrand(int brand) {
        Brand = brand;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public void setWarranty(int warranty) {
        Warranty = warranty;
    }

    public void setServiceLife(int serviceLife) {
        ServiceLife = serviceLife;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public void setProvider(int provider) {
        Provider = provider;
    }

    @Override
    public String toString() {
        return ID+" "+Name;
    }
}
