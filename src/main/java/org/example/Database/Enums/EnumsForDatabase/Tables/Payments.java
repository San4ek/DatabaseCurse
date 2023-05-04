package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Payments {

    ID("ID"),
    PAYMENT("Payment");

    private String title;

    Payments(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
