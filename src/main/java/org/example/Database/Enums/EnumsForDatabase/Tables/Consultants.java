package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Consultants {
    ID("ID"),
    NAME("Name"),
    PHONE("Phone"),
    RATING("Rating");

    private String title;

    Consultants(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
