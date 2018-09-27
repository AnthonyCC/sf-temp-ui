package com.freshdirect.backoffice.selfcredit.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class OrderLineParam implements Serializable {

    private String olCreditAmount;
    private String olCreditQtyChanged;
    private String olOrigQty;
    private String olOrigPrice;
    private String olSkuCode;
    private String olCreditDept;
    private String olCreditQtyReturned;
    private String olCreditMethod;
    private String olTaxRate;
    private String olDepositValue;
    private String orderLineId;
    private String olCartNum;
    private String olRetAmount;
    private String olCreditQty;
    private String olCreditReason;

    @JsonGetter("ol_credit_amount")
    public String getOlCreditAmount() {
        return olCreditAmount;
    }

    @JsonSetter("olCreditAmount")
    public void setOlCreditAmount(String olCreditAmount) {
        this.olCreditAmount = olCreditAmount;
    }

    @JsonGetter("ol_credit_qty_changed")
    public String getOlCreditQtyChanged() {
        return olCreditQtyChanged;
    }

    @JsonSetter("olCreditQtyChanged")
    public void setOlCreditQtyChanged(String olCreditQtyChanged) {
        this.olCreditQtyChanged = olCreditQtyChanged;
    }

    @JsonGetter("ol_orig_qty")
    public String getOlOrigQty() {
        return olOrigQty;
    }

    @JsonSetter("olOrigQty")
    public void setOlOrigQty(String olOrigQty) {
        this.olOrigQty = olOrigQty;
    }

    @JsonGetter("ol_orig_price")
    public String getOlOrigPrice() {
        return olOrigPrice;
    }

    @JsonSetter("olOrigPrice")
    public void setOlOrigPrice(String olOrigPrice) {
        this.olOrigPrice = olOrigPrice;
    }

    @JsonGetter("ol_sku_code")
    public String getOlSkuCode() {
        return olSkuCode;
    }

    @JsonSetter("olSkuCode")
    public void setOlSkuCode(String olSkuCode) {
        this.olSkuCode = olSkuCode;
    }

    @JsonGetter("ol_credit_dept")
    public String getOlCreditDept() {
        return olCreditDept;
    }

    @JsonSetter("olCreditDept")
    public void setOlCreditDept(String olCreditDept) {
        this.olCreditDept = olCreditDept;
    }

    @JsonGetter("ol_credit_qty_returned")
    public String getOlCreditQtyReturned() {
        return olCreditQtyReturned;
    }

    @JsonSetter("olCreditQtyReturned")
    public void setOlCreditQtyReturned(String olCreditQtyReturned) {
        this.olCreditQtyReturned = olCreditQtyReturned;
    }

    @JsonGetter("ol_credit_method")
    public String getOlCreditMethod() {
        return olCreditMethod;
    }

    @JsonSetter("olCreditMethod")
    public void setOlCreditMethod(String olCreditMethod) {
        this.olCreditMethod = olCreditMethod;
    }

    @JsonGetter("ol_tax_rate")
    public String getOlTaxRate() {
        return olTaxRate;
    }

    @JsonSetter("olTaxRate")
    public void setOlTaxRate(String olTaxRate) {
        this.olTaxRate = olTaxRate;
    }

    @JsonGetter("ol_deposit_value")
    public String getOlDepositValue() {
        return olDepositValue;
    }

    @JsonSetter("olDepositValue")
    public void setOlDepositValue(String olDepositValue) {
        this.olDepositValue = olDepositValue;
    }

    @JsonGetter("orderlineId")
    public String getOrderLineId() {
        return orderLineId;
    }

    @JsonSetter("orderLineId")
    public void setOrderLineId(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    @JsonGetter("ol_cartnum")
    public String getOlCartNum() {
        return olCartNum;
    }

    @JsonSetter("olCartNum")
    public void setOlCartNum(String olCartNum) {
        this.olCartNum = olCartNum;
    }

    @JsonGetter("ol_ret_amount")
    public String getOlRetAmount() {
        return olRetAmount;
    }

    @JsonSetter("olRetAmount")
    public void setOlRetAmount(String olRetAmount) {
        this.olRetAmount = olRetAmount;
    }

    @JsonGetter("ol_credit_qty")
    public String getOlCreditQty() {
        return olCreditQty;
    }

    @JsonSetter("olCreditQty")
    public void setOlCreditQty(String olCreditQty) {
        this.olCreditQty = olCreditQty;
    }

    @JsonGetter("ol_credit_reason")
    public String getOlCreditReason() {
        return olCreditReason;
    }

    @JsonSetter("olCreditReason")
    public void setOlCreditReason(String olCreditReason) {
        this.olCreditReason = olCreditReason;
    }
}
