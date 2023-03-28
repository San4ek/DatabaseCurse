package org.example.Database.Enums.ConfigEnums;

public enum ServerConfigs {
    PATH("/approving"),
    LINK("https://dba5-46-216-119-82.eu.ngrok.io"),
    GOAL("Approving");

    private final String title;

    ServerConfigs(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
