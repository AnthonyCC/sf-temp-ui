/**
 * @author ekracoff
 * Created on Apr 22, 2005*/

package com.freshdirect.ocf.core;


public abstract class Action extends Entity implements ActionI {
	private Flight flight;

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

}