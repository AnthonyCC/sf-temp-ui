package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;

public class ReportRecordCntDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2940029434524154468L;

	private int rowCount = 0;

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
