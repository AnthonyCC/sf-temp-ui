package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;

public class FileFooterDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1984797606841217205L;

	private int rowCount = 0;

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
}
