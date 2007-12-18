/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class LoadUserTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER 	= LoggerFactory.getInstance( LoadUserTag.class );

	private FDIdentity newIdentity;

	public void setNewIdentity(FDIdentity id) {
		this.newIdentity = id;
	}

	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		FDUser newUser = this.recognizeUser(newIdentity);
		newUser.isLoggedIn(true);
		session.removeAttribute(SessionName.USER);
        session.setAttribute(SessionName.USER, new FDSessionUser(newUser, session));
		return SKIP_BODY;
	} 

	private FDUser recognizeUser(FDIdentity identity) throws JspException {
		try {
			return FDCustomerManager.recognize(identity);
		} catch(FDResourceException ex) {
			LOGGER.error("FDResourceException trying to recognize user: " + identity, ex);
			throw new JspException(ex.getMessage());
		} catch (FDAuthenticationException ex) {
            LOGGER.error("FDAuthenticationException trying to recognize user: " + identity, ex);
            throw new JspException(ex.getMessage());
        }
	}
}