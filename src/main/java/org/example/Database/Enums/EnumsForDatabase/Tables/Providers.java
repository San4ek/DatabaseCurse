package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Providers {
    ID("ID"),
    NAME("Name"),
    PHONE_NUMBER("Phone"),
    EMAIL("Email"),
    COUNTRY("Country");

    private String title;

    Providers(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
