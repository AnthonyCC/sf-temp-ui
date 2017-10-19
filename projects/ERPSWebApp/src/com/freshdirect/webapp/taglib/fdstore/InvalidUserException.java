package com.freshdirect.webapp.taglib.fdstore;

public class InvalidUserException extends Exception {

    private static final long serialVersionUID = 1777973345499359532L;

    public InvalidUserException(String message) {
        super(message);
    }
}
