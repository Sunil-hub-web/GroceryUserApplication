package com.kissanfactory.groceryuserapplication.Models.Product_Details;

import java.util.List;

import com.kissanfactory.groceryuserapplication.Models.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {

@SerializedName("code")
@Expose
private Integer code;
@SerializedName("err")
@Expose
private Boolean err;
@SerializedName("productDetails")
@Expose
private List<Product> productDetails = null;

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

public List<Product> getProductDetails() {
return productDetails;
}

public void setProductDetails(List<Product> productDetails) {
this.productDetails = productDetails;
}

}