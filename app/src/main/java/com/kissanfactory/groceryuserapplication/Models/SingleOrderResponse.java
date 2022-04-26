package com.kissanfactory.groceryuserapplication.Models;

public class SingleOrderResponse {

    private String msg;
    private Order data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
