/**
 * @author ekracoff
 * Created on Apr 14, 2005*/

package com.freshdirect.ocf.core;

import java.io.Serializable;


public interface ActionI extends Serializable {

	public void execute(OcfTableI customers) throws OcfRecoverableException;
	
	public void setFlight(Flight flight);
	
	public Flight getFlight();
}