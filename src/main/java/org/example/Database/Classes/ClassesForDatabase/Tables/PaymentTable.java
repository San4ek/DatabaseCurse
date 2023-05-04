package org.example.Database.Classes.ClassesForDatabase.Tables;

public class PaymentTable {
    private int ID;
    private String Payment;

    public PaymentTable(int ID, String index) {
        this.ID = ID;
        Payment = index;
    }

    public PaymentTable(String payment) {
        Payment = payment;
    }

    public int getID() {
        return ID;
    }

    public String getPayment() {
        return Payment;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    @Override
    public String toString() {
        return ID +" "+Payment;
    }
}