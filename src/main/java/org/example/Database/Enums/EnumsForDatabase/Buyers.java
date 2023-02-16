package org.example.Database.Enums.EnumsForDatabase;

public enum Buyers {
    ID("ID"),
    NAME("Name"),
    PHONE("Phone"),
    EMAIL("Email");

    private String title;

    Buyers(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
