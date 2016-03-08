package com.freshdirect.webapp.ajax.expresscheckout.receipt.data;

public class SuccessPageData {

	private String header;
	private String rightBlock;
	private String orderId;
	private ReceiptData receipt;
    private String soName;
    private String soOrderDate;
    
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

	/**
	 * @return the soName
	 */
	public String getSoName() {
		return soName;
	}

	/**
	 * @param soName the soName to set
	 */
	public void setSoName(String soName) {
		this.soName = soName;
	}

	/**
	 * @return the soOrderDate
	 */
	public String getSoOrderDate() {
		return soOrderDate;
	}

	/**
	 * @param soOrderDate the soOrderDate to set
	 */
	public void setSoOrderDate(String soOrderDate) {
		this.soOrderDate = soOrderDate;
	}
	
}
