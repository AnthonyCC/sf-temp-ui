package com.freshdirect.fdstore;

import javax.servlet.jsp.JspException;

public class FDNotFoundException extends JspException {

    private static final long serialVersionUID = 1L;

    public FDNotFoundException(String message) {
        super(message);
    }
}
