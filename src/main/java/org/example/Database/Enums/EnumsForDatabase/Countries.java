package org.example.Database.Enums.EnumsForDatabase;

public enum Countries {
    ID("ID"),
    COUNTRY("Country");

    private String title;

    Countries(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
