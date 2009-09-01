package com.freshdirect.cms.ui.service;

import java.io.PrintWriter;
import java.io.StringWriter;


public class ServerException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ServerException() {
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }


}
