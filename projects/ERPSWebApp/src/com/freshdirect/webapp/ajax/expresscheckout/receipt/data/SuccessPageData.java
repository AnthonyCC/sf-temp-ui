package com.freshdirect.webapp.ajax.expresscheckout.receipt.data;

public class SuccessPageData {

	private String header;
	private String rightBlock;
	private String orderId;
	private ReceiptData receipt;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getRightBlock() {
		return rightBlock;
	}

	public void setRightBlock(String rightBlock) {
		this.rightBlock = rightBlock;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public ReceiptData getReceipt() {
		return receipt;
	}

	public void setReceipt(ReceiptData receipt) {
		this.receipt = receipt;
	}
}
