package com.kissanfactory.groceryuserapplication.Models.payment_create;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentCreateDto implements Serializable
{

@SerializedName("code")
@Expose
private Integer code;
@SerializedName("err")
@Expose
private Boolean err;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("payment")
@Expose
private Payment payment;
@SerializedName("data")
@Expose
private Data data;
private final static long serialVersionUID = 7386654514235543241L;

public Integer getCode() {
return code;
}

public void setCode(Integer code) {
this.code = code;
}

public Boolean getErr() {
return err;
}

public void setErr(Boolean err) {
this.err = err;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public Payment getPayment() {
return payment;
}

public void setPayment(Payment payment) {
this.payment = payment;
}

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

}