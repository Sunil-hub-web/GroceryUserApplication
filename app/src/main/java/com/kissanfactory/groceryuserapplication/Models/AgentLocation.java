package com.kissanfactory.groceryuserapplication.Models;

public class AgentLocation {
    private String _id;
    private CurrentLocation currentLocation;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public CurrentLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(CurrentLocation currentLocation) {
        this.currentLocation = currentLocation;
    }
}
