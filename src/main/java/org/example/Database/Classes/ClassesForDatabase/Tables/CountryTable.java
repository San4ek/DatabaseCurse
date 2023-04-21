package org.example.Database.Classes.ClassesForDatabase.Tables;

public class CountryTable {
    private int ID;
    private String Country;

    public CountryTable(int ID, String name) {
        this.ID = ID;
        Country = name;
    }

    public CountryTable(String name) {
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

    public void setCountry(String country) {
        Country = country;
    }

    @Override
    public String toString() {
        return ID+" "+Country;
    }
}
