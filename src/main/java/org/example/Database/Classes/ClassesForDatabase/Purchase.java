package org.example.Database.Classes.ClassesForDatabase;

import java.util.Date;

public class Purchase {
    private int ID;
    private final int Gadget;
    private final Date Date;
    private final int Payment;
    private final int Buyer;
    private final int Consultant;

    public Purchase(int ID, int gadget, java.util.Date date, int payment, int buyer, int consultant) {
        this.ID = ID;
        Gadget = gadget;
        Date = date;
        Payment = payment;
        Buyer = buyer;
        Consultant = consultant;
    }

    public Purchase(int gadget, java.util.Date date, int payment, int buyer, int consultant) {
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

    public java.util.Date getDate() {
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
}
