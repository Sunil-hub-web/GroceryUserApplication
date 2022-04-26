package com.kissanfactory.groceryuserapplication.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Product {
    @Expose
    @SerializedName("_id")
    private String id;
    private String productId;
    private long totalRating;
    private long avgRating;
    private long sold;
    private float discount;
    private int inStock;
    private float price;
    // add the arrays here
    private String title;
    private String category;
    private String categoryId;
    private String soldBy;
    private String addedBy;
    private String description;
    private String productName;
    private int productQuantity;
    private List<String> images;
    private boolean liked = false;
    private List<Object> weight;
    private String type;
    private String experience;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }



    public List<Object> getWeight() {
        return weight;
    }

    public void setWeight(List<Object> weight) {
        this.weight = weight;
    }


    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId() {
        this.productId = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(long totalRating) {
        this.totalRating = totalRating;
    }

    public long getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(long avgRating) {
        this.avgRating = avgRating;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", totalRating=" + totalRating +
                ", avgRating=" + avgRating +
                ", sold=" + sold +
                ", discount=" + discount +
                ", inStock=" + inStock +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", soldBy='" + soldBy + '\'' +
                ", addedBy='" + addedBy + '\'' +
                ", description='" + description + '\'' +
                ", productName='" + productName + '\'' +
                ", productQuantity=" + productQuantity +
                ", images=" + images +
                ", liked=" + liked +
                ", weight=" + weight +
                ", type='" + type + '\'' +
                ", experience='" + experience + '\'' +
                '}';
    }
}
