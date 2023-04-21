package org.example.Database.Enums.ConfigEnums;

public enum ServerConfigs {
    PATH("/approving"),
    LINK("https://27a9-46-216-118-111.ngrok-free.app"),
    GOAL("Approving");

    private final String title;

    ServerConfigs(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
