package org.example.Database.Classes.ClassesForDatabase;

public class Provider {
    private int ID;
    private final String Name;
    private String Phone;
    private String Email;
    private int Country;

    public Provider(int ID, String name, String phone, String email, int country) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        Email = email;
        Country = country;
    }

    public Provider(String name, String phone, String email, int country) {
        Name = name;
        Phone = phone;
        Email = email;
        Country = country;
    }

    public Provider(int ID, String name) {
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

    public String getEmail() {
        return Email;
    }

    public int getCountry() {
        return Country;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return ID +" "+ Name;
    }
}
