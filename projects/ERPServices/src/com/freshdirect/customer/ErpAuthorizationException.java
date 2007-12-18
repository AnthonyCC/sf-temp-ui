/*
 * ErpAuthorizationException.java
 *
 * Created on January 28, 2002, 5:52 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;

public class ErpAuthorizationException extends ExceptionSupport {

	/** Creates new ErpAuthorizationException */
    public ErpAuthorizationException() {
		super();
    }
	
	public ErpAuthorizationException(String message) {
		super(message);
    }
	
	public ErpAuthorizationException(Exception ex) {
		super(ex);
    }
	
	public ErpAuthorizationException(Exception ex, String message) {
		super(ex, message);
    }

}
