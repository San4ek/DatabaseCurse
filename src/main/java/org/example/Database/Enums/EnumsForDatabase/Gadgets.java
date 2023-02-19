package org.example.Database.Enums.EnumsForDatabase;

public enum Gadgets {
    ID("ID"),
    TYPE("Type"),
    NAME("Name"),
    BRAND("Brand"),
    COUNTRY_OF_MANUFACTURE("Country"),
    WARRANTY("Warranty"),
    SERVICE_LIFE("ServiceLife"),
    COST("Cost"),
    PROVIDER("Provider");

    private String title;

    Gadgets(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
