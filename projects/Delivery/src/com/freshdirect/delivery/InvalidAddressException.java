package com.freshdirect.delivery;

import com.freshdirect.framework.core.ExceptionSupport;

/** Indicates an error that occurred while attempting to validate an address.
  *	@author Erik Klein, Versatile Consulting Inc.
  */
public class InvalidAddressException extends ExceptionSupport
{
	/** Constructor.
	  *	@param String _message containing the message that explains the exception that occurred.
	  */
    public InvalidAddressException(String _message)
    {
        super(_message);
    }
    
    /**
     * Specifically indicates a geocoding exception.
     * These exceptions maybe handled gracefully, e.g. when displaying addresses.
     * @author istvan
     *
     */
    public static class GeocodingException extends InvalidAddressException {
    	public GeocodingException(String _message) {
    		super(_message);
    	}
    }
}