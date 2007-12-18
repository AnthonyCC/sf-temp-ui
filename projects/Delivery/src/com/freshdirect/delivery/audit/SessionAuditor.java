/**
 * 
 */
package com.freshdirect.delivery.audit;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.audit.ejb.SessionAuditorHome;
import com.freshdirect.delivery.audit.ejb.SessionAuditorSB;
import com.freshdirect.framework.util.DateRange;

/**
 * @author kocka
 *
 */
public class SessionAuditor implements SessionAuditorI {
	
	private static SessionAuditor instance = new SessionAuditor();
	SessionAuditorHome auditorHome = null;
	
	public static SessionAuditor getInstance(){
		return instance;
	}

	public List getSessionLogEntries(DateRange range) throws DlvResourceException {
		try {
			lookupSessionAuditorHome();
			SessionAuditorSB auditorSB = auditorHome.create();
			return auditorSB.getSessionLogEntries(range);
		} catch (NamingException e) {
			throw new DlvResourceException(e);
		} catch (RemoteException e) {
			throw new DlvResourceException(e);
		} catch (CreateException e) {
			throw new DlvResourceException(e);
		}
	}

	public void userLoggedIn(String sessionId, String username, String role) throws DlvResourceException {
		try {
			lookupSessionAuditorHome();
			SessionAuditorSB auditorSB = auditorHome.create();
			auditorSB.userLoggedIn(sessionId, username, role);
		} catch (NamingException e) {
			throw new DlvResourceException(e);
		} catch (RemoteException e) {
			throw new DlvResourceException(e);
		} catch (CreateException e) {
			throw new DlvResourceException(e);
		}
	}

	public void userLogggedOut(String sessionId) throws DlvResourceException {
		try {
			lookupSessionAuditorHome();
			SessionAuditorSB auditorSB = auditorHome.create();
			auditorSB.userLogggedOut(sessionId);
		} catch (NamingException e) {
			throw new DlvResourceException(e);
		} catch (RemoteException e) {
			throw new DlvResourceException(e);
		} catch (CreateException e) {
			throw new DlvResourceException(e);
		}
	}
	protected void lookupSessionAuditorHome() throws NamingException {
		if(auditorHome != null)
			return;
		Context ctx = null;
		try {
			ctx = DlvProperties.getInitialContext();
			this.auditorHome = (SessionAuditorHome) ctx.lookup( DlvProperties.getSessionAuditorHome() );
		} finally {
			try {
				ctx.close();
			} catch (NamingException e) {}
		}
	}

	public void userInteraction(String sessionId) throws DlvResourceException {
		try {
			lookupSessionAuditorHome();
			SessionAuditorSB auditorSB = auditorHome.create();
			auditorSB.userInteraction(sessionId);
		} catch (NamingException e) {
			throw new DlvResourceException(e);
		} catch (RemoteException e) {
			throw new DlvResourceException(e);
		} catch (CreateException e) {
			throw new DlvResourceException(e);
		}
	}

}
