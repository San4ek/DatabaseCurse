package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum TypesOfGadgets {
    ID("ID"),
    TYPE("Type");

    private String title;

    TypesOfGadgets(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
