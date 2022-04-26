package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class CartItems {
    public String _id = "";
    public String userId = "";

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private List<Cart> cart;

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "cart=" + cart +
                '}';
    }
}
