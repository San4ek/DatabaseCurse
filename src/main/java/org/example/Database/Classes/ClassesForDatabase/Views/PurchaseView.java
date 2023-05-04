package org.example.Database.Classes.ClassesForDatabase.Views;

import java.time.LocalDate;

public class PurchaseView {
    private int ID;
    private String Gadget;
    private LocalDate Date;
    private String Payment;
    private String Buyer;
    private String Consultant;

    public PurchaseView(int ID, String gadget, LocalDate date, String payment, String buyer, String consultant) {
        this.ID = ID;
        Gadget = gadget;
        Date = date;
        Payment = payment;
        Buyer = buyer;
        Consultant = consultant;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGadget() {
        return Gadget;
    }

    public void setGadget(String gadget) {
        Gadget = gadget;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getBuyer() {
        return Buyer;
    }

    public void setBuyer(String buyer) {
        Buyer = buyer;
    }

    public String getConsultant() {
        return Consultant;
    }

    public void setConsultant(String consultant) {
        Consultant = consultant;
    }
}
