package com.kissanfactory.groceryuserapplication.Models.viewallTopProducts;

import java.io.Serializable;
import java.util.List;

import com.kissanfactory.groceryuserapplication.Models.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopProductViewAlltDTO implements Serializable
{

@SerializedName("code")
@Expose
private Integer code;
@SerializedName("err")
@Expose
private Boolean err;
@SerializedName("data")
@Expose
private List<Product> data = null;
private final static long serialVersionUID = -3168249991919086648L;

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

public List<Product> getData() {
return data;
}

public void setData(List<Product> data) {
this.data = data;
}

}