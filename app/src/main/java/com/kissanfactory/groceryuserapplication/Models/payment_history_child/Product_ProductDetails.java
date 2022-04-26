package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_ProductDetails implements Serializable
{

@SerializedName("discount")
@Expose
private Integer discount;
@SerializedName("images")
@Expose
private List<String> images = null;
@SerializedName("weight")
@Expose
private List<String> weight = null;
@SerializedName("subcategoryId")
@Expose
private String subcategoryId;
@SerializedName("totalRating")
@Expose
private Integer totalRating;
@SerializedName("avgRating")
@Expose
private Integer avgRating;
@SerializedName("sold")
@Expose
private Integer sold;
@SerializedName("offers")
@Expose
private List<Object> offers = null;
@SerializedName("benifits")
@Expose
private List<Object> benifits = null;
@SerializedName("colors")
@Expose
private List<Object> colors = null;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("addedBy")
@Expose
private String addedBy;
@SerializedName("title")
@Expose
private String title;
@SerializedName("price")
@Expose
private Integer price;
@SerializedName("type")
@Expose
private String type;
@SerializedName("description")
@Expose
private String description;
@SerializedName("soldBy")
@Expose
private String soldBy;
@SerializedName("inStock")
@Expose
private Integer inStock;
@SerializedName("experience")
@Expose
private String experience;
@SerializedName("categoryId")
@Expose
private String categoryId;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("__v")
@Expose
private Integer v;
private final static long serialVersionUID = -8968764861096314853L;

/**
* No args constructor for use in serialization
*
*/
public Product_ProductDetails() {
}

/**
*
* @param offers
* @param sold
* @param images
* @param addedBy
* @param soldBy
* @param discount
* @param weight
* @param totalRating
* @param description
* @param title
* @param type
* @param experience
* @param colors
* @param createdAt
* @param benifits
* @param price
* @param v
* @param subcategoryId
* @param avgRating
* @param inStock
* @param id
* @param categoryId
* @param updatedAt
*/
public Product_ProductDetails(Integer discount, List<String> images, List<String> weight, String subcategoryId, Integer totalRating, Integer avgRating, Integer sold, List<Object> offers, List<Object> benifits, List<Object> colors, String id, String addedBy, String title, Integer price, String type, String description, String soldBy, Integer inStock, String experience, String categoryId, String createdAt, String updatedAt, Integer v) {
super();
this.discount = discount;
this.images = images;
this.weight = weight;
this.subcategoryId = subcategoryId;
this.totalRating = totalRating;
this.avgRating = avgRating;
this.sold = sold;
this.offers = offers;
this.benifits = benifits;
this.colors = colors;
this.id = id;
this.addedBy = addedBy;
this.title = title;
this.price = price;
this.type = type;
this.description = description;
this.soldBy = soldBy;
this.inStock = inStock;
this.experience = experience;
this.categoryId = categoryId;
this.createdAt = createdAt;
this.updatedAt = updatedAt;
this.v = v;
}

public Integer getDiscount() {
return discount;
}

public void setDiscount(Integer discount) {
this.discount = discount;
}

public List<String> getImages() {
return images;
}

public void setImages(List<String> images) {
this.images = images;
}

public List<String> getWeight() {
return weight;
}

public void setWeight(List<String> weight) {
this.weight = weight;
}

public String getSubcategoryId() {
return subcategoryId;
}

public void setSubcategoryId(String subcategoryId) {
this.subcategoryId = subcategoryId;
}

public Integer getTotalRating() {
return totalRating;
}

public void setTotalRating(Integer totalRating) {
this.totalRating = totalRating;
}

public Integer getAvgRating() {
return avgRating;
}

public void setAvgRating(Integer avgRating) {
this.avgRating = avgRating;
}

public Integer getSold() {
return sold;
}

public void setSold(Integer sold) {
this.sold = sold;
}

public List<Object> getOffers() {
return offers;
}

public void setOffers(List<Object> offers) {
this.offers = offers;
}

public List<Object> getBenifits() {
return benifits;
}

public void setBenifits(List<Object> benifits) {
this.benifits = benifits;
}

public List<Object> getColors() {
return colors;
}

public void setColors(List<Object> colors) {
this.colors = colors;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getAddedBy() {
return addedBy;
}

public void setAddedBy(String addedBy) {
this.addedBy = addedBy;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public Integer getPrice() {
return price;
}

public void setPrice(Integer price) {
this.price = price;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getSoldBy() {
return soldBy;
}

public void setSoldBy(String soldBy) {
this.soldBy = soldBy;
}

public Integer getInStock() {
return inStock;
}

public void setInStock(Integer inStock) {
this.inStock = inStock;
}

public String getExperience() {
return experience;
}

public void setExperience(String experience) {
this.experience = experience;
}

public String getCategoryId() {
return categoryId;
}

public void setCategoryId(String categoryId) {
this.categoryId = categoryId;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}