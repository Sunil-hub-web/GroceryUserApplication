package com.kissanfactory.groceryuserapplication.Models;

public class NoMsgResponse {
    private boolean err;

    private int code;

    private AddFavourite favourite;

    public AddFavourite getFavourite() {
        return favourite;
    }

    public void setFavourite(AddFavourite favourite) {
        this.favourite = favourite;
    }

    public boolean isErr() {
        return err;
    }

    public void setErr(boolean err) {
        this.err = err;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
