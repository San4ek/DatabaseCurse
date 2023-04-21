package org.example.Database.Classes.ClassesForDatabase.Tables;

public class BuyerTable {
    private int ID;
    private String Name;
    private String Phone;
    private String Email;

    public BuyerTable(int ID, String name, String phone, String email) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        Email = email;
    }

    public BuyerTable(String name, String phone, String email) {
        Name = name;
        Phone = phone;
        Email = email;
    }

    public BuyerTable(int ID, String name) {
        this.ID=ID;
        Name = name;
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

    @Override
    public String toString() {
        return ID+" "+Name;
    }
}
