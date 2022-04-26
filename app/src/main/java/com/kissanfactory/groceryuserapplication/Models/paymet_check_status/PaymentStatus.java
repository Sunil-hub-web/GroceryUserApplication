package com.kissanfactory.groceryuserapplication.Models.paymet_check_status;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatus implements Serializable
{

@SerializedName("id")
@Expose
private String id;
@SerializedName("entity")
@Expose
private String entity;
@SerializedName("amount")
@Expose
private Integer amount;
@SerializedName("currency")
@Expose
private String currency;
@SerializedName("status")
@Expose
private String status;
@SerializedName("order_id")
@Expose
private String orderId;
@SerializedName("invoice_id")
@Expose
private Object invoiceId;
@SerializedName("international")
@Expose
private Boolean international;
@SerializedName("method")
@Expose
private String method;
@SerializedName("amount_refunded")
@Expose
private Integer amountRefunded;
@SerializedName("refund_status")
@Expose
private Object refundStatus;
@SerializedName("captured")
@Expose
private Boolean captured;
@SerializedName("description")
@Expose
private String description;
@SerializedName("card_id")
@Expose
private Object cardId;
@SerializedName("bank")
@Expose
private Object bank;
@SerializedName("wallet")
@Expose
private String wallet;
@SerializedName("vpa")
@Expose
private Object vpa;
@SerializedName("email")
@Expose
private String email;
@SerializedName("contact")
@Expose
private String contact;

//    private List<Notes> images;
@SerializedName("fee")
@Expose
private Integer fee;
@SerializedName("tax")
@Expose
private Integer tax;
@SerializedName("error_code")
@Expose
private Object errorCode;
@SerializedName("error_description")
@Expose
private Object errorDescription;
@SerializedName("error_source")
@Expose
private Object errorSource;
@SerializedName("error_step")
@Expose
private Object errorStep;
@SerializedName("error_reason")
@Expose
private Object errorReason;
@SerializedName("acquirer_data")
@Expose
private AcquirerData acquirerData;
@SerializedName("created_at")
@Expose
private Integer createdAt;
private final static long serialVersionUID = 3838304395825016375L;

//    public List<Notes> getImages() {
//        return images;
//    }
//
//    public void setImages(List<Notes> images) {
//        this.images = images;
//    }



    public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getEntity() {
return entity;
}

public void setEntity(String entity) {
this.entity = entity;
}

public Integer getAmount() {
return amount;
}

public void setAmount(Integer amount) {
this.amount = amount;
}

public String getCurrency() {
return currency;
}

public void setCurrency(String currency) {
this.currency = currency;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getOrderId() {
return orderId;
}

public void setOrderId(String orderId) {
this.orderId = orderId;
}

public Object getInvoiceId() {
return invoiceId;
}

public void setInvoiceId(Object invoiceId) {
this.invoiceId = invoiceId;
}

public Boolean getInternational() {
return international;
}

public void setInternational(Boolean international) {
this.international = international;
}

public String getMethod() {
return method;
}

public void setMethod(String method) {
this.method = method;
}

public Integer getAmountRefunded() {
return amountRefunded;
}

public void setAmountRefunded(Integer amountRefunded) {
this.amountRefunded = amountRefunded;
}

public Object getRefundStatus() {
return refundStatus;
}

public void setRefundStatus(Object refundStatus) {
this.refundStatus = refundStatus;
}

public Boolean getCaptured() {
return captured;
}

public void setCaptured(Boolean captured) {
this.captured = captured;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public Object getCardId() {
return cardId;
}

public void setCardId(Object cardId) {
this.cardId = cardId;
}

public Object getBank() {
return bank;
}

public void setBank(Object bank) {
this.bank = bank;
}

public String getWallet() {
return wallet;
}

public void setWallet(String wallet) {
this.wallet = wallet;
}

public Object getVpa() {
return vpa;
}

public void setVpa(Object vpa) {
this.vpa = vpa;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getContact() {
return contact;
}

public void setContact(String contact) {
this.contact = contact;
}
//
//public Notes getNotes() {
//return notes;
//}
//
//public void setNotes(Notes notes) {
//this.notes = notes;
//}

public Integer getFee() {
return fee;
}

public void setFee(Integer fee) {
this.fee = fee;
}

public Integer getTax() {
return tax;
}

public void setTax(Integer tax) {
this.tax = tax;
}

public Object getErrorCode() {
return errorCode;
}

public void setErrorCode(Object errorCode) {
this.errorCode = errorCode;
}

public Object getErrorDescription() {
return errorDescription;
}

public void setErrorDescription(Object errorDescription) {
this.errorDescription = errorDescription;
}

public Object getErrorSource() {
return errorSource;
}

public void setErrorSource(Object errorSource) {
this.errorSource = errorSource;
}

public Object getErrorStep() {
return errorStep;
}

public void setErrorStep(Object errorStep) {
this.errorStep = errorStep;
}

public Object getErrorReason() {
return errorReason;
}

public void setErrorReason(Object errorReason) {
this.errorReason = errorReason;
}

public AcquirerData getAcquirerData() {
return acquirerData;
}

public void setAcquirerData(AcquirerData acquirerData) {
this.acquirerData = acquirerData;
}

public Integer getCreatedAt() {
return createdAt;
}

public void setCreatedAt(Integer createdAt) {
this.createdAt = createdAt;
}

}