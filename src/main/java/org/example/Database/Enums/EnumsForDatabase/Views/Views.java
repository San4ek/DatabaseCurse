package org.example.Database.Enums.EnumsForDatabase.Views;

public enum Views {
    GADGETS("gadgetsview"),
    PURCHASES("purchasesview");
    private String title;

    Views(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
