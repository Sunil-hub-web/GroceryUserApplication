package com.kissanfactory.groceryuserapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favourite {
    @Expose
    @SerializedName("_id")
    private String id;

    private Product productId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Favourite{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                '}';
    }
}
