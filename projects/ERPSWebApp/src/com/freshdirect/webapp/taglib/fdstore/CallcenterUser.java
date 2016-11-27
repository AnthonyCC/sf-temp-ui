/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CallcenterUser implements HttpSessionBindingListener, SessionName, java.io.Serializable {

	private static Category LOGGER = LoggerFactory.getInstance( CallcenterUser.class );

	private String id;
	private EnumCallcenterUserLevel level;

	public CallcenterUser() {
		this.id = null;
		this.level = null;
	}

	public CallcenterUser(String s, EnumCallcenterUserLevel l) {
		this.id = s;
		this.level = l;
	}

	public String getId() { return this.id; }
	public EnumCallcenterUserLevel getLevel() { return this.level; }

	/** @return boolean indicating whether user is a CUSTOMER SERVICE REP or higher. */
	public boolean isCSR() { return (this.level.equals(EnumCallcenterUserLevel.CSR) || isCSRHybrid()); }

	/** @return boolean indicating whether user is a CUSTOMER SERVICE REP HYBRID or higher. */
	public boolean isCSRHybrid() { return (this.level.equals(EnumCallcenterUserLevel.CSR_HYBRID) || isSupervisor()); }
	
	/** @return boolean indicating whether user is a SUPERVISOR or higher. */
	public boolean isSupervisor() {	return (this.level.equals(EnumCallcenterUserLevel.SUPERVISOR) || isAdministrator()); }

	/** @return boolean indicating whether user is an ADMINISTRATOR */
	public boolean isAdministrator() { return this.level.equals(EnumCallcenterUserLevel.ADMINISTRATOR); }

	/** @return boolean indicating whether user is a DISPATCHER */
	public boolean isDispatcher() { return this.level.equals(EnumCallcenterUserLevel.DISPATCHER); }


	public void valueBound(HttpSessionBindingEvent event) {
		LOGGER.debug("CallcenterUser (" + this.id + ", LEVEL: " + this.level.getDescription() + ") bound to session");
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		LOGGER.debug("CallcenterUser unbound from session");
	}

}
