package org.example.Database.Classes.ClassesForDatabase.Tables;

public class ProviderTable {
    private int ID;
    private String Name;
    private String Phone;
    private String Email;
    private int Country;

    public ProviderTable(int ID, String name, String phone, String email, int country) {
        this.ID = ID;
        Name = name;
        Phone = phone;
        Email = email;
        Country = country;
    }

    public ProviderTable(String name, String phone, String email, int country) {
        Name = name;
        Phone = phone;
        Email = email;
        Country = country;
    }

    public ProviderTable(int ID, String name) {
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

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setCountry(int country) {
        Country = country;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return ID +" "+ Name;
    }
}
