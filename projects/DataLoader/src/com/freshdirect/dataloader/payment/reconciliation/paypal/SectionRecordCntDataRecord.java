package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;

public class SectionRecordCntDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969486987804976129L;

	private int rowCount = 0;

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
