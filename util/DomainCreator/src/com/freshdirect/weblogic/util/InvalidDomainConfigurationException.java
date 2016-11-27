package com.freshdirect.weblogic.util;

public class InvalidDomainConfigurationException extends Exception {
	private static final long serialVersionUID = 2933867642581233798L;

	public InvalidDomainConfigurationException() {
		super();
	}

	public InvalidDomainConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDomainConfigurationException(String message) {
		super(message);
	}

	public InvalidDomainConfigurationException(Throwable cause) {
		super(cause);
	}
}
