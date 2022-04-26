package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailsChildDto implements Serializable
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
@SerializedName("data")
@Expose
private List<Datum> data = null;
private final static long serialVersionUID = 270725131255734269L;

/**
* No args constructor for use in serialization
*
*/
public PaymentDetailsChildDto() {
}

/**
*
* @param msg
* @param code
* @param err
* @param data
*/
public PaymentDetailsChildDto(Integer code, Boolean err, String msg, List<Datum> data) {
super();
this.code = code;
this.err = err;
this.msg = msg;
this.data = data;
}

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

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

}