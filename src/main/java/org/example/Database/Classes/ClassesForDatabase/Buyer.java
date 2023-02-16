package org.example.Database.Classes.ClassesForDatabase;

public class Buyer {
    private int ID;
    private String Name;
    private String Phone;
    private String Email;

    public Buyer(int ID, String name, String phone, String email) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        Email = email;
    }

    public Buyer(String name, String phone, String email) {
        Name = name;
        Phone = phone;
        Email = email;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
