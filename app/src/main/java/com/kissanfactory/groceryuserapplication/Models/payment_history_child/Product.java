package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("productId")
@Expose
private String productId;
@SerializedName("product")
@Expose
private Product_ProductDetails product;
@SerializedName("productQuantity")
@Expose
private Integer productQuantity;
private final static long serialVersionUID = 5033095189406941985L;

/**
* No args constructor for use in serialization
*
*/
public Product() {
}

/**
*
* @param product
* @param productQuantity
* @param productId
* @param id
*/
public Product(String id, String productId, Product_ProductDetails product, Integer productQuantity) {
super();
this.id = id;
this.productId = productId;
this.product = product;
this.productQuantity = productQuantity;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getProductId() {
return productId;
}

public void setProductId(String productId) {
this.productId = productId;
}

public Product_ProductDetails getProduct() {
return product;
}

public void setProduct(Product_ProductDetails product) {
this.product = product;
}

public Integer getProductQuantity() {
return productQuantity;
}

public void setProductQuantity(Integer productQuantity) {
this.productQuantity = productQuantity;
}

}