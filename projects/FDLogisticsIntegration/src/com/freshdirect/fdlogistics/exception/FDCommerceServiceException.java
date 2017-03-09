package com.freshdirect.fdlogistics.exception;

import com.freshdirect.fdstore.FDException;


public class FDCommerceServiceException extends FDException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3830751929435262666L;

	public FDCommerceServiceException(Exception e, String id) {
		super(e, id);
	}

	public FDCommerceServiceException(Exception e) {
		super(e);
	}

	public FDCommerceServiceException(String msg) {
		super(msg);
	}
	
}
