package org.example.Database.Classes.ClassesForDatabase;

public class Country {
    private int ID;
    private final String Country;

    public Country(int ID, String name) {
        this.ID = ID;
        Country = name;
    }

    public Country(String name) {
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

    @Override
    public String toString() {
        return ID+" "+Country;
    }
}
