package com.freshdirect.fdlogistics.exception;

import com.freshdirect.fdstore.FDException;


public class FDLogisticsServiceException extends FDException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3830751929435262666L;

	public FDLogisticsServiceException(Exception e, String id) {
		super(e, id);
	}

	public FDLogisticsServiceException(Exception e) {
		super(e);
	}

	public FDLogisticsServiceException(String msg) {
		super(msg);
	}
	
}
