package com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentAddOrderDto implements Serializable
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
@SerializedName("noti")
@Expose
private Noti noti;
private final static long serialVersionUID = -5392338576361802822L;

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

public Noti getNoti() {
return noti;
}

public void setNoti(Noti noti) {
this.noti = noti;
}

}
