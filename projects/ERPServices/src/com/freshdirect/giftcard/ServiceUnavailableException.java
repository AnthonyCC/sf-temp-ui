package com.freshdirect.giftcard;

import com.freshdirect.fdstore.FDException;

public class ServiceUnavailableException extends Exception {

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }
    
    

}
