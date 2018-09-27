package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class StoreCredits extends Message {

    private String sort;
    private String dir;
    private List<StoreCreditData> pendingCredits;
    private List<StoreCreditData> approvedCredits;
    private double totalAmount;
    private double totalAmountFD;
    private double totalAmountFK;

    public StoreCredits(com.freshdirect.webapp.ajax.storecredits.data.StoreCredits storeCredits) {
        this.sort = storeCredits.getSort();
        this.dir = storeCredits.getDir();
        this.pendingCredits = StoreCreditData.wrap(storeCredits.getPendingCredits());
        this.approvedCredits = StoreCreditData.wrap(storeCredits.getApprovedCredits());
        this.totalAmount = storeCredits.getTotalAmount();
        this.totalAmountFD = storeCredits.getTotalAmountFD();
        this.totalAmountFK = storeCredits.getTotalAmountFK();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public List<StoreCreditData> getPendingCredits() {
        return pendingCredits;
    }

    public void setPendingCredits(List<StoreCreditData> pendingCredits) {
        this.pendingCredits = pendingCredits;
    }

    public List<StoreCreditData> getApprovedCredits() {
        return approvedCredits;
    }

    public void setApprovedCredits(List<StoreCreditData> approvedCredits) {
        this.approvedCredits = approvedCredits;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmountFD() {
        return totalAmountFD;
    }

    public void setTotalAmountFD(double totalAmountFD) {
        this.totalAmountFD = totalAmountFD;
    }

    public double getTotalAmountFK() {
        return totalAmountFK;
    }

    public void setTotalAmountFK(double totalAmountFK) {
        this.totalAmountFK = totalAmountFK;
    }
}
