/**
 * 
 */
package com.freshdirect.dlvadmin.utils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.audit.SessionAuditor;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kocka
 *
 */
public class UserSessionListener implements HttpSessionListener {

	private static final  Logger LOGGER = LoggerFactory.getInstance(UserSessionListener.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		LOGGER.info("Session created: " + sessionEvent.getSession().getId());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		try {
			SessionAuditor.getInstance().userLogggedOut(sessionEvent.getSession().getId());
		} catch (DlvResourceException e) {
			LOGGER.warn("Unable to record logout event", e);
		}
	}

}
