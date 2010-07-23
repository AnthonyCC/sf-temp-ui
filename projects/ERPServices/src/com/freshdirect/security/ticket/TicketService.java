package com.freshdirect.security.ticket;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author csongor
 * 
 */
public class TicketService {
	private static final Logger LOGGER = LoggerFactory.getInstance(TicketService.class);
	
	private static TicketService instance;

	private static TicketServiceHome ticketServiceHome;

	private static java.util.Random rand = new java.util.Random();

	private static String generateKey() {
		StringBuffer buff = new StringBuffer();
		for (int j = 0; j < 3; j++)
			buff.append(Long.toString(Math.abs(rand.nextLong()), 36).toUpperCase());
		return buff.toString();
	}

	private synchronized static TicketServiceHome getTicketServiceHome() throws FDResourceException {
		if (ticketServiceHome == null) {
			Context ctx = null;
			try {
				ctx = ErpServicesProperties.getInitialContext();
				ticketServiceHome = (TicketServiceHome) ctx.lookup(TicketServiceHome.JNDI_HOME);
			} catch (NamingException e) {
				throw new FDResourceException(e, "could not get initial context");
			} finally {
				try {
					if (ctx != null) {
						ctx.close();
					}
				} catch (NamingException e) {
					LOGGER.warn("cannot close Context while trying to cleanup", e);
				}
			}
		}
		return ticketServiceHome;
	}

	private synchronized static void invalidateTicketServiceHome() {
		ticketServiceHome = null;
	}

	/**
	 * @return the one and only instance of the service
	 */
	public synchronized static TicketService getInstance() {
		if (instance == null)
			return instance = new TicketService();
		else
			return instance;
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
			sb = getTicketServiceHome().create();
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
		} catch (CreateException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
		}
	}

	public Ticket reload(Ticket ticket) throws NoSuchTicketException, FDResourceException {
		try {
			TicketServiceSB sb = getTicketServiceHome().create();
			ticket = sb.retrieveTicket(ticket.getKey());
			if (ticket == null)
				throw new NoSuchTicketException();

			return ticket;
		} catch (RemoteException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
		} catch (CreateException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
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
			sb = getTicketServiceHome().create();
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
		} catch (CreateException e) {
			invalidateTicketServiceHome();
			throw new FDResourceException(e);
		}
	}
}
