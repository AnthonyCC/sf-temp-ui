package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class ErpAddressVerificationException extends ExceptionSupport {


    public ErpAddressVerificationException() {		
    }
	
	public ErpAddressVerificationException(String message) {
		super(message);
    }
	
	public ErpAddressVerificationException(Exception ex) {
		super(ex);
    }
	

	
	public ErpAddressVerificationException(Exception ex, String message) {
		super(ex,message);
	}

}
