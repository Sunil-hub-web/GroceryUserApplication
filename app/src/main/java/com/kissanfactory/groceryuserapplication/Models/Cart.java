package com.kissanfactory.groceryuserapplication.Models;

public class Cart {
    private String _id;
    private int quantity;
    private String name;
    private Product item;
    private String image;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getItem() {
        return item;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "quantity=" + quantity +
                ", name='" + name + '\'' +
                ", item=" + item +
                '}';
    }
}
