package org.example.Database.Enums.ConfigEnums;

public enum MessageConfig {
    APPROVING("Approving..."),
    NOT_APPROVED("No connection or incorrect email"),
    NO_MESSAGE("");

    private String title;

    MessageConfig(String title) {
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}