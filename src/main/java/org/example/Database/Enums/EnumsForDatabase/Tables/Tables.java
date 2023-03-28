package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Tables {
    BRANDS("brands"),
    BUYERS("buyers"),
    CONSULTANTS("consultants"),
    COUNTRIES("countries"),
    GADGETS("gadgets"),
    PROVIDERS("providers"),
    PURCHASES("purchases"),
    TYPES("types"),
    PAYMENTS("payments");

    private String title;

    Tables(String title) {
        this.title = title;
    }

    public String getTitle() {
        return '`'+title+'`';
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
