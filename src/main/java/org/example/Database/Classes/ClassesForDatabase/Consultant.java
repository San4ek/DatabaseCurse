package org.example.Database.Classes.ClassesForDatabase;

public class Consultant {
    private int ID;
    private final String Name;
    private String Phone;
    private double rating;

    public Consultant(int ID, String name, String phone, double rating) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        this.rating = rating;
    }

    public Consultant(String name, String phone, double rating) {
        Name = name;
        Phone = phone;
        this.rating = rating;
    }

    public Consultant(int ID, String name) {
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

    @Override
    public String toString() {
        return ID+" "+Name;
    }
}
