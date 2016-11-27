package com.freshdirect.weblogic.util;

public class CreateDomainException extends Exception {
	private static final long serialVersionUID = 2279508967964464163L;

	public CreateDomainException() {
	}

	public CreateDomainException(String message) {
		super(message);
	}

	public CreateDomainException(Throwable cause) {
		super(cause);
	}

	public CreateDomainException(String message, Throwable cause) {
		super(message, cause);
	}
}
