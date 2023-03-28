package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Brands {
    ID("ID"),
    BRAND("Brand");

    private String title;

    Brands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}