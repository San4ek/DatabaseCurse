package org.example.Database.Classes.ClassesForDatabase.Tables;

public class ConsultantTable {
    private int ID;
    private String Name;
    private String Phone;
    private double rating;

    public ConsultantTable(int ID, String name, String phone, double rating) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        this.rating = rating;
    }

    public ConsultantTable(String name, String phone, double rating) {
        Name = name;
        Phone = phone;
        this.rating = rating;
    }

    public ConsultantTable(int ID, String name) {
        this.ID=ID;
        Name=name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public double getRating() {
        return rating;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return ID+" "+Name;
    }
}
