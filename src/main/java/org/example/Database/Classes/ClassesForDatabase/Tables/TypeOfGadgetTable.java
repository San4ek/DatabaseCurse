package org.example.Database.Classes.ClassesForDatabase.Tables;

public class TypeOfGadgetTable {
    private int ID;
    private final String Type;

    public TypeOfGadgetTable(int ID, String type) {
        this.ID = ID;
        Type = type;
    }

    public TypeOfGadgetTable(String type) {
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

    @Override
    public String toString() {
        return ID+" "+Type;
    }
}
