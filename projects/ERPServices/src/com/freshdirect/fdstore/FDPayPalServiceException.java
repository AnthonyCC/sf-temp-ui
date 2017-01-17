package com.freshdirect.fdstore;

public class FDPayPalServiceException  extends Exception {
	
	public FDPayPalServiceException(Exception e, String id) {
		super(e);
	}

	public FDPayPalServiceException(Exception e) {
		super(e);
	}

	public FDPayPalServiceException(String msg) {
		super(msg);
	}

}
