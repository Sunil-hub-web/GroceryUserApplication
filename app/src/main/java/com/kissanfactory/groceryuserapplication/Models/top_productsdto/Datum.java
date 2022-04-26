package com.kissanfactory.groceryuserapplication.Models.top_productsdto;

import java.io.Serializable;
import java.util.List;

import com.kissanfactory.groceryuserapplication.Models.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("data")
    @Expose
    private List<Product> data = null;
    private final static long serialVersionUID = -1059391347400815157L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }

}