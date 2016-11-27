package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.io.Serializable;

public class FileHeaderDataRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2984280121473113609L;

	private int fileNo = 0;

	public int getFileNo() {
		return fileNo;
	}

	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}


}
