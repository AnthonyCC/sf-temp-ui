package com.freshdirect.fdlogistics.exception;

import com.freshdirect.fdstore.FDException;

public class RestClientServiceException extends FDException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3830751929435262666L;

	public RestClientServiceException(Exception e, String id) {
		super(e, id);
	}

	public RestClientServiceException(Exception e) {
		super(e);
	}

	public RestClientServiceException(String msg) {
		super(msg);
	}
	
}
