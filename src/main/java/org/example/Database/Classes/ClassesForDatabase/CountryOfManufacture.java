package org.example.Database.Classes.ClassesForDatabase;

public class CountryOfManufacture {
    private int ID;
    private final String Country;

    public CountryOfManufacture(int ID, String name) {
        this.ID = ID;
        Country = name;
    }

    public CountryOfManufacture(String name) {
        Country = name;
    }

    public int getID() {
        return ID;
    }

    public String getCountry() {
        return Country;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
