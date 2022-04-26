package com.kissanfactory.groceryuserapplication.Models.top_productsdto;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopProductDTO implements Serializable
{

@SerializedName("code")
@Expose
private Integer code;
@SerializedName("err")
@Expose
private Boolean err;
@SerializedName("data")
@Expose
private List<Datum> data = null;
private final static long serialVersionUID = -41968483005946031L;

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

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

}