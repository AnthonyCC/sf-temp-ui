package com.freshdirect.security.ticket;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 *@deprecated Please use the TicketController and TicketServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface TicketServiceSB extends EJBObject {
	/**
	 * Create the ticket. Returns null if the ticket already exists.
	 * 
	 * @param ticket
	 * @return null if ticket already exists
	 * @throws RemoteException
	 */
	@Deprecated
	Ticket createTicket(Ticket ticket) throws RemoteException;

	/**
	 * Updates the ticket's 'used' attribute in DB. Returns the original ticket
	 * or null if the ticket does not exist in the DB.
	 * 
	 * @param ticket
	 * @return null if ticket does not exist
	 * @throws RemoteException
	 */
	@Deprecated
	Ticket updateTicket(Ticket ticket) throws RemoteException;

	/**
	 * Retrieves a ticket based on its key.
	 * 
	 * @param key
	 * @return null if the ticket does not exist
	 * @throws RemoteException
	 */
	@Deprecated
	Ticket retrieveTicket(String key) throws RemoteException;
}
