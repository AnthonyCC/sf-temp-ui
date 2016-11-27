package com.freshdirect.delivery;

/**
 * This exception is reserved for the case when timeslot became full
 * 
 * @author segabor
 *
 */
public class ReservationUnavailableException extends ReservationException {

	public ReservationUnavailableException() {
	}

	public ReservationUnavailableException(String msg) {
		super(msg);
	}
}
