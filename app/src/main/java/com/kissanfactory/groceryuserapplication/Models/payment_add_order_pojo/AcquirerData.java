package com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcquirerData implements Serializable
{

@SerializedName("transaction_id")
@Expose
private Object transactionId;
private final static long serialVersionUID = -8021492936532127955L;

public Object getTransactionId() {
return transactionId;
}

public void setTransactionId(Object transactionId) {
this.transactionId = transactionId;
}

}