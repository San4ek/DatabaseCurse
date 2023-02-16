package org.example.Database.Enums.ConfigEnums;

public enum DatabaseConfigs {
    PASSWORD("12345678"),
    USER("root"),
    HOST("localhost"),
    NAME("shop"),
    PORT("3306");

    private  String title;

    DatabaseConfigs(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
