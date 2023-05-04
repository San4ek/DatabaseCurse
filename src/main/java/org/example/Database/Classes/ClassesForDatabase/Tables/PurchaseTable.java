package org.example.Database.Classes.ClassesForDatabase.Tables;

import java.time.LocalDate;

public class PurchaseTable {
    private int ID;
    private int Gadget;
    private LocalDate Date;
    private int Payment;
    private int Buyer;
    private int Consultant;

    public PurchaseTable(int ID, int gadget, LocalDate date, int payment, int buyer, int consultant) {
        this.ID = ID;
        Gadget = gadget;
        Date = date;
        Payment = payment;
        Buyer = buyer;
        Consultant = consultant;
    }

    public PurchaseTable(int gadget, LocalDate date, int payment, int buyer, int consultant) {
        Gadget = gadget;
        Date = date;
        Payment = payment;
        Buyer = buyer;
        Consultant = consultant;
    }

    public int getID() {
        return ID;
    }

    public int getGadget() {
        return Gadget;
    }

    public LocalDate getDate() {
        return Date;
    }

    public int getPayment() {
        return Payment;
    }

    public int getBuyer() {
        return Buyer;
    }

    public int getConsultant() {
        return Consultant;
    }

    public void setGadget(int gadget) {
        Gadget = gadget;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public void setPayment(int payment) {
        Payment = payment;
    }

    public void setBuyer(int buyer) {
        Buyer = buyer;
    }

    public void setConsultant(int consultant) {
        Consultant = consultant;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
