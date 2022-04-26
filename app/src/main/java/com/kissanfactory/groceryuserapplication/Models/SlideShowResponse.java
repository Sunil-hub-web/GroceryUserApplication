package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class SlideShowResponse {
    private int code;
    private List<SlideShow> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SlideShow> getData() {
        return data;
    }

    public void setData(List<SlideShow> data) {
        this.data = data;
    }
}
