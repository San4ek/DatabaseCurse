package org.example.Database.Classes.ClassesForDatabase.Views;

public class GadgetView {
    private int ID;
    private String Type;
    private String Name;
    private String Brand;
    private String Country;
    private String Provider;

    public GadgetView(int ID, String type, String name, String brand, String country, String provider) {
        this.ID = ID;
        Type = type;
        Name = name;
        Brand = brand;
        Country = country;
        Provider = provider;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }
}
