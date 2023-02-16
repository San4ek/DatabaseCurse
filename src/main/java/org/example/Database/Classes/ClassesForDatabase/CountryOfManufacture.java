package org.example.Database.Classes.ClassesForDatabase;

public class CountryOfManufacture {
    private int ID;
    private final String Name;

    public CountryOfManufacture(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public CountryOfManufacture(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }
}
