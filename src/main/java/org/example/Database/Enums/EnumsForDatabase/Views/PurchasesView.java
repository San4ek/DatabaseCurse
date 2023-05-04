package org.example.Database.Enums.EnumsForDatabase.Views;

public enum PurchasesView {
    ID("ID"),
    GADGET("Gadget"),
    DATE("Date"),
    PAYMENT("Payment"),
    BUYER("Buyer"),
    CONSULTANT("Consultant");

    private String title;

    PurchasesView(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
