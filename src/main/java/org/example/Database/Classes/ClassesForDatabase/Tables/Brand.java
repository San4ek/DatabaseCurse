package org.example.Database.Classes.ClassesForDatabase.Tables;

public class Brand {
    private String Brand;
    private int ID;

    public Brand(int ID, String brand) {
        Brand = brand;
        this.ID = ID;
    }

    public Brand(String brand) {
        Brand = brand;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return ID+" "+Brand;
    }
}
