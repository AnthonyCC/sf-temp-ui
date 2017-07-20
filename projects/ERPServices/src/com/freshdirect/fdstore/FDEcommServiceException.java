package com.freshdirect.fdstore;

public class FDEcommServiceException  extends Exception {
	
	public FDEcommServiceException(Exception e, String id) {
		super(e);
	}

	public FDEcommServiceException(Exception e) {
		super(e);
	}

	public FDEcommServiceException(String msg) {
		super(msg);
	}

}
