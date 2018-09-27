package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

public class StoreCreditData {

    private String amount;
    private String order;
    private String store;
    private String status;
    private String type;
    private String date;

    public StoreCreditData(com.freshdirect.webapp.ajax.storecredits.data.StoreCreditData storeCreditData) {
        this.amount = storeCreditData.getAmount();
        this.order = storeCreditData.getOrder();
        this.store = storeCreditData.getStore();
        this.status = storeCreditData.getStatus();
        this.type = storeCreditData.getType();
        this.date = storeCreditData.getDate();
    }

    public static List<StoreCreditData> wrap(List<com.freshdirect.webapp.ajax.storecredits.data.StoreCreditData> credits) {
        List<StoreCreditData> storeCredits = new ArrayList<StoreCreditData>();
        for (com.freshdirect.webapp.ajax.storecredits.data.StoreCreditData storeCreditData : credits) {
            storeCredits.add(new StoreCreditData(storeCreditData));
        }
        return storeCredits;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
