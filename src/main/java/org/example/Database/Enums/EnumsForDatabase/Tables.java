package org.example.Database.Enums.EnumsForDatabase;

public enum Tables {
    BRANDS("brands"),
    BUYERS("buyers"),
    CONSULTANTS("consultants"),
    COUNTRIES_OF_MANUFACTURE("countries of manufacture"),
    GADGETS("gadgets"),
    PROVIDERS("providers"),
    PURCHASES("purchases"),
    TYPES_OF_GADGETS("types of gadgets");

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
