package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingDetails implements Serializable {

    @SerializedName("ordered")
    @Expose
    private String ordered;
    @SerializedName("packed")
    @Expose
    private Boolean packed;
    @SerializedName("shipped")
    @Expose
    private Boolean shipped;
    @SerializedName("delivered")
    @Expose
    private Boolean delivered;
    @SerializedName("return")
    @Expose
    private Boolean _return;
    @SerializedName("replaced")
    @Expose
    private Boolean replaced;
    @SerializedName("cancel")
    @Expose
    private Boolean cancel;
    private final static long serialVersionUID = 8478622943403843546L;

    /**
     * No args constructor for use in serialization
     */
    public TrackingDetails() {
    }

    /**
     * @param cancel
     * @param ordered
     * @param _return
     * @param shipped
     * @param replaced
     * @param delivered
     * @param packed
     */
    public TrackingDetails(String ordered, Boolean packed, Boolean shipped, Boolean delivered, Boolean _return, Boolean replaced, Boolean cancel) {
        super();
        this.ordered = ordered;
        this.packed = packed;
        this.shipped = shipped;
        this.delivered = delivered;
        this._return = _return;
        this.replaced = replaced;
        this.cancel = cancel;
    }

    public String getOrdered() {
        return ordered;
    }

    public void setOrdered(String ordered) {
        this.ordered = ordered;
    }

    public Boolean getPacked() {
        return packed;
    }

    public void setPacked(Boolean packed) {
        this.packed = packed;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getReturn() {
        return _return;
    }

    public void setReturn(Boolean _return) {
        this._return = _return;
    }

    public Boolean getReplaced() {
        return replaced;
    }
}