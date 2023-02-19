package org.example.Database.Classes.ClassesForDatabase;

public class Gadget {
    private int ID;
    private final int Type;
    private final String Name;
    private final int Brand;
    private final int Country;
    private final int Warranty;
    private final int ServiceLife;
    private final int Cost;
    private final int Provider;

    public Gadget(int ID, int type, String name, int brand, int countryOfManufacture, int warranty, int serviceLife, int cost, int provider) {
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

    public Gadget(int type, String name, int brand, int country, int warranty, int serviceLife, int cost, int provider) {
        Type = type;
        Name = name;
        Brand = brand;
        Country = country;
        Warranty = warranty;
        ServiceLife = serviceLife;
        Cost = cost;
        Provider = provider;
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
}
