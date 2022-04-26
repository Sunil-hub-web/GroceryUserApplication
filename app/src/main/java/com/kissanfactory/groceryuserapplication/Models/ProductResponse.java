package com.kissanfactory.groceryuserapplication.Models;

import com.kissanfactory.groceryuserapplication.Models.Product_Details.ProductDetail;

public class ProductResponse {
    private int code;
    private boolean err;
    private Product productDetails;
    private ProductDetail productDetailsearch;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public Product getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Product productDetails) {
        this.productDetails = productDetails;
    }

    public ProductDetail getProductDetailsearch() {
        return productDetailsearch;
    }

    public void setProductDetailsearch(ProductDetail productDetailsearch) {
        this.productDetailsearch = productDetailsearch;
    }
}
