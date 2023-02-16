package org.example.Database.Classes.ClassesForDatabase;

public class Provider {
    private int ID;
    private final String Name;
    private final int Phone;
    private final String Email;
    private final String Address;

    public Provider(int ID, String name, int phone, String email, String address) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        Email = email;
        Address = address;
    }

    public Provider(String name, int phone, String email, String address) {
        Name = name;
        Phone = phone;
        Email = email;
        Address = address;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public int getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getAddress() {
        return Address;
    }
}
