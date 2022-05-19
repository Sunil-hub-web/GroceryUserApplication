package com.kissanfactory.groceryuserapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {

    @Expose
    @SerializedName("_id")
    private String id;
    @Expose
    @SerializedName("default")
    private boolean selected;
    private String city;
    private String state;
    private String locality;
    private String country;
    private int zip;
    private String house;
    private String street;
    private Double latitude;
    private Double longitude;

    public Address() {
    }

    protected Address(Parcel in) {
        id = in.readString();
        city = in.readString();
        state = in.readString();
        street = in.readString();
        locality = in.readString();
        country = in.readString();
        house = in.readString();
        zip = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    /*public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }*/

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", locality='" + locality + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                ", house='" + house + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(street);
        parcel.writeString(locality);
        parcel.writeString(country);
        parcel.writeString(house);
        parcel.writeInt(zip);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
