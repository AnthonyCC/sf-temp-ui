package com.freshdirect.security.ticket;

public class IllegalTicketUseException extends Exception {
	
	private static final long serialVersionUID = 947643641917398003L;

	public IllegalTicketUseException() {
	}

	public IllegalTicketUseException(String message) {
		super(message);
	}
}
