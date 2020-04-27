package com.eiadmreh.easylearn;

public class ToolinCart {
    private String tName;
    private long tCode;
    private double tPrice;
    private double TotalPrice;
    private int tQuantity;

    public ToolinCart(String name, long code, double price, int quantity, double totalPrice) {
        this.tName = name;
        this.tCode = code;
        this.tPrice = price;
        TotalPrice = totalPrice;
        this.tQuantity = quantity;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public long gettCode() {
        return tCode;
    }

    public void settCode(long tCode) {
        this.tCode = tCode;
    }

    public double gettPrice() {
        return tPrice;
    }

    public void settPrice(double tPrice) {
        this.tPrice = tPrice;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public int gettQuantity() {
        return tQuantity;
    }

    public void settQuantity(int tQuantity) {
        this.tQuantity = tQuantity;
    }
    @Override
    public String toString() {
        return  "Name=" + tName +
                "\nCode=" + tCode +
                "\nPrice=" + tPrice +
                "\nQuantity=" + tQuantity+
                "\nTotalPrice=" + TotalPrice ;
    }
}
