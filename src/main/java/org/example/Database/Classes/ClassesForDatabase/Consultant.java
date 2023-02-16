package org.example.Database.Classes.ClassesForDatabase;

public class Consultant {
    private int ID;
    private final String Name;
    private final String Phone;
    private final int rating;

    public Consultant(int ID, String name, String phone, int rating) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        this.rating = rating;
    }

    public Consultant(String name, String phone, int rating) {
        Name = name;
        Phone = phone;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }
}
