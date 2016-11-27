package com.freshdirect.security.ticket;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author csongor
 * 
 */
public class TicketService {
	private static final Logger LOGGER = LoggerFactory.getInstance(TicketService.class);
	
	private static TicketService instance;

	private static TicketServiceSB ticketServiceSB;

	private static java.util.Random rand = new java.util.Random();

	private static String generateKey() {
		StringBuffer buff = new StringBuffer();
		for (int j = 0; j < 3; j++)
			buff.append(Long.toString(Math.abs(rand.nextLong()), 36).toUpperCase());
		return buff.toString();
	}

	private synchronized static TicketServiceSB getTicketService() throws FDResourceException {
		if (ticketServiceSB == null) {
		    ticketServiceSB = ERPServiceLocator.getInstance().getTicketServiceSessionBean();
		}
		return ticketServiceSB;
	}

	private synchronized static void invalidateTicketServiceHome() {
	    ticketServiceSB = null;
	}

	/**
	 * @return the one and only instance of the service
	 */
	public synchronized static TicketService getInstance() {
		if (instance == null) {
			return instance = new TicketService();
		} else {
			return instance;
		}
	}

	private TicketService() {

	}

	public Ticket create(String owner, String purpose, int expiration) throws IllegalArgumentException, FDResourceException {
		if (owner == null || owner.length() == 0)
			throw new IllegalArgumentException("issuer cannot be null or empty");
		if (purpose == null || purpose.length() == 0)
			throw new IllegalArgumentException("purpose cannot be null or empty");
		TicketServiceSB sb;
		try {
			sb = getTicketService();
			int remainingTries = 5;
			while (remainingTries > 0) {
				Ticket ticket = new Ticket(generateKey(), owner, purpose, expiration, false);
				ticket = sb.createTicket(ticket);
				if (ticket != null)
					return ticket;

				remainingTries--;
			}
			throw new FDResourceException("failed to generate unique key in 5 attempts");
		} catch (RemoteException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
		} catch (EJBException e) {
                    invalidateTicketServiceHome();
                    throw e;
		}
	}

	public Ticket reload(Ticket ticket) throws NoSuchTicketException, FDResourceException {
		try {
			TicketServiceSB sb = getTicketService();
			ticket = sb.retrieveTicket(ticket.getKey());
			if (ticket == null)
				throw new NoSuchTicketException();

			return ticket;
		} catch (RemoteException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
                } catch (EJBException e) {
                    invalidateTicketServiceHome();
                    throw e;
		}
	}

	public Ticket use(String key, String user, String purpose) throws IllegalArgumentException, NoSuchTicketException,
			IllegalTicketUseException, TicketExpiredException, FDResourceException {
		if (key == null || key.length() == 0)
			throw new IllegalArgumentException("key cannot be null or empty");
		if (user == null || user.length() == 0)
			throw new IllegalArgumentException("user cannot be null or empty");
		if (purpose == null || purpose.length() == 0)
			throw new IllegalArgumentException("purpose cannot be null or empty");
		TicketServiceSB sb;
		try {
			sb = getTicketService();
			Ticket ticket = sb.retrieveTicket(key);
			if (ticket == null)
				throw new NoSuchTicketException();
			ticket.use(user, purpose);
			ticket = sb.updateTicket(ticket);
			if (ticket == null)
				throw new NoSuchTicketException();
			else
				return ticket;
		} catch (RemoteException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
                } catch (EJBException e) {
                    invalidateTicketServiceHome();
                    throw e;
		}
	}
}
