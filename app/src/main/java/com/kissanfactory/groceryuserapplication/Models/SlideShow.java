package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class SlideShow {
    private String _id;
    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "SlideShow{" +
                "_id='" + _id + '\'' +
                ", images=" + images +
                '}';
    }
}
