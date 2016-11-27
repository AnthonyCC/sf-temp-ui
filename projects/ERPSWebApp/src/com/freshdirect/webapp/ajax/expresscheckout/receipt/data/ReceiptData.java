package com.freshdirect.webapp.ajax.expresscheckout.receipt.data;

import java.util.List;

import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;

public class ReceiptData {

	private String total;
	private String totalLabel;
	private List<CartSubTotalFieldData> receiptLines;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalLabel() {
		return totalLabel;
	}

	public void setTotalLabel(String totalLabel) {
		this.totalLabel = totalLabel;
	}

	public List<CartSubTotalFieldData> getReceiptLines() {
		return receiptLines;
	}

	public void setReceiptLines(List<CartSubTotalFieldData> receiptLines) {
		this.receiptLines = receiptLines;
	}

}
