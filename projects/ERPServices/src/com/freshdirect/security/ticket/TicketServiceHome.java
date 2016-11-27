package com.freshdirect.security.ticket;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface TicketServiceHome extends EJBHome {
	public static final String JNDI_HOME = "freshdirect.ticketservice";

	public TicketServiceSB create() throws CreateException, RemoteException;
}
