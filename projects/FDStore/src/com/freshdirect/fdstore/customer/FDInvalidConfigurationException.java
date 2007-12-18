/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */
 
package com.freshdirect.fdstore.customer;

import com.freshdirect.fdstore.FDException;

/**
 * FDInvalidConfigurationException
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public class FDInvalidConfigurationException extends FDException {
	
	public static class Unavailable extends FDInvalidConfigurationException {
		public Unavailable(String s) { super(s); }
	}
	
	public static class InvalidSalesUnit extends FDInvalidConfigurationException {
		public InvalidSalesUnit(String s) { super(s); }
	}
	
	public static class MissingVariation extends FDInvalidConfigurationException {
		public MissingVariation(String s) { super(s); }
	}
	
	public static class InvalidOption extends FDInvalidConfigurationException {
		public InvalidOption(String s) { super(s); }
	}
		

	/**
	 * 
	 */
	public FDInvalidConfigurationException() {
		super();
	}

	/**
	 * @param message
	 */
	public FDInvalidConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param ex
	 */
	public FDInvalidConfigurationException(Exception ex) {
		super(ex);
	}

	/**
	 * @param ex
	 * @param message
	 */
	public FDInvalidConfigurationException(Exception ex, String message) {
		super(ex, message);
	}

}
