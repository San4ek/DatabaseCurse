package org.example.Database.Enums.EnumsForDatabase;

public enum Brands {
    ID("ID"),
    BRAND("Brand");

    private String title;

    Brands(String title) {
        this.title = title;
    }

    public String getTitle() {
        return '`'+title+'`';
    }

    public String getFieldTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}