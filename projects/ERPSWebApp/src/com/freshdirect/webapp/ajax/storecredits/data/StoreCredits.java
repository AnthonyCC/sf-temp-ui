package com.freshdirect.webapp.ajax.storecredits.data;

import java.util.List;

public class StoreCredits {

    private String sort;
    private String dir;
    private List<StoreCreditData> pendingCredits;
    private List<StoreCreditData> approvedCredits;
    private String totalAmount;
    private String totalAmountFD;
    private String totalAmountFK;

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

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTotalAmountFD() {
		return totalAmountFD;
	}

	public void setTotalAmountFD(String totalAmountFD) {
		this.totalAmountFD = totalAmountFD;
	}

	public String getTotalAmountFK() {
		return totalAmountFK;
	}

	public void setTotalAmountFK(String totalAmountFK) {
		this.totalAmountFK = totalAmountFK;
	}
}
