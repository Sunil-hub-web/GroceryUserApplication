package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class PaymentHIstoryREsponse {

    private String msg;
    private int code;
    private List<PaymentHistory> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PaymentHistory> getData() {
        return data;
    }

    public void setData(List<PaymentHistory> data) {
        this.data = data;
    }
}
