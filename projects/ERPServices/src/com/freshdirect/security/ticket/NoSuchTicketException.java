package com.freshdirect.security.ticket;

import com.freshdirect.fdstore.FDException;

public class NoSuchTicketException extends FDException {
	private static final long serialVersionUID = 7536743177526607861L;

	public NoSuchTicketException() {
		super();
	}

	public NoSuchTicketException(String message) {
		super(message);
	}
}
