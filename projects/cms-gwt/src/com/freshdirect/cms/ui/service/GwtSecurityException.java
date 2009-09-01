package com.freshdirect.cms.ui.service;

public class GwtSecurityException extends RuntimeException {

	private static final long	serialVersionUID	= 4334921576604015505L;

	public GwtSecurityException() {
    }

    public GwtSecurityException(String message) {
        super(message);
    }

    public GwtSecurityException(Throwable cause) {
        super(cause);
    }

    public GwtSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}
