package org.example.Database.Enums.EnumsForDatabase;

public enum Purchases {
    ID("ID"),
    GADGET("Gadget"),
    DATE("Date"),
    PAYMENT("Payment"),
    BUYER("Buyer"),
    CONSULTANT("Consultant");

    private String title;

    Purchases(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
