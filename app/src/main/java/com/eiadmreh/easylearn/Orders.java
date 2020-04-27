package com.eiadmreh.easylearn;

import java.util.Date;

public class Orders {
    private String ClientName;
    private String OrderNumber;
    private String date;
    private String OrderPrice;

    public Orders() {
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.OrderPrice = orderPrice;
    }


}
