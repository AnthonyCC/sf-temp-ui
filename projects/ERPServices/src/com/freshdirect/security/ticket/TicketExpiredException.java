package com.freshdirect.security.ticket;

public class TicketExpiredException extends Exception {

	private static final long serialVersionUID = 3172931078777385911L;

	public TicketExpiredException() {
		super();
	}

	public TicketExpiredException(String message) {
		super(message);
	}

	public TicketExpiredException(Throwable cause) {
		super(cause);
	}

	public TicketExpiredException(String message, Throwable cause) {
		super(message, cause);
	}

}
