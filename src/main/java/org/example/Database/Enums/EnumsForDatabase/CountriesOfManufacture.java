package org.example.Database.Enums.EnumsForDatabase;

public enum CountriesOfManufacture {
    ID("ID"),
    Name("Name");

    private String title;

    CountriesOfManufacture(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
