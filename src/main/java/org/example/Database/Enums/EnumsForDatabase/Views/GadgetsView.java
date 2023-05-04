package org.example.Database.Enums.EnumsForDatabase.Views;

public enum GadgetsView {
    ID("ID"),
    TYPE("Type"),
    NAME("Name"),
    BRAND("Brand"),
    COUNTRY("Country"),
    PROVIDER("Provider");

    private String title;

    GadgetsView(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
