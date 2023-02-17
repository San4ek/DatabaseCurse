package org.example.Database.Classes.ClassesForDatabase;

public class TypeOfGadget {
    private int ID;
    private final String Type;

    public TypeOfGadget(int ID, String type) {
        this.ID = ID;
        Type = type;
    }

    public TypeOfGadget(String type) {
        Type = type;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return Type;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
