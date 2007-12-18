/*
 * FDInvalidAddressException.java
 *
 * Created on October 17, 2001, 5:38 PM
 */

package com.freshdirect.fdstore;

/**
 *
 * @author  knadeem
 * @version 
 */
import com.freshdirect.framework.core.ExceptionSupport;
public class FDInvalidAddressException extends ExceptionSupport {

    /**
     * Creates new <code>FDInvalidAddressException</code> without detail message.
     */
    public FDInvalidAddressException() {
    }


    /**
     * Constructs an <code>FDInvalidAddressException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FDInvalidAddressException(String msg) {
        super(msg);
    }
    
    /**
     * Specifically indicates a geocoding exception.
     * @author istvan
     *
     */
    public static class GeocodingException extends FDInvalidAddressException {
    	
    	public GeocodingException(String msg) {
    		super(msg);
    	}
    	
    	public GeocodingException() {}
    }
}


