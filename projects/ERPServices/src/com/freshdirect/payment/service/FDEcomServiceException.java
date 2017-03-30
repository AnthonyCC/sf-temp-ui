package com.freshdirect.payment.service;

public class FDEcomServiceException  extends Exception {
	
	public FDEcomServiceException(Exception e, String id) {
		super(e);
	}

	public FDEcomServiceException(Exception e) {
		super(e);
	}

	public FDEcomServiceException(String msg) {
		super(msg);
	}

}
