/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class CookieMonster {

	private static Category LOGGER = LoggerFactory.getInstance(CookieMonster.class);

	private final static String COOKIE_NAME = "FDUser";
	private final static int COOKIE_MAXAGE = 365 * 24 * 60 * 60;

	public static FDSessionUser loadCookie(HttpServletRequest request) throws FDResourceException {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		Cookie cookie = null;
		for (int i = 0; i < cookies.length; i++) {
			if (COOKIE_NAME.equals(cookies[i].getName())) {
				cookie = cookies[i];
				break;
			}
		}
		if (cookie == null) {
			return null;
		}
		String val = cookie.getValue();
		LOGGER.debug("Found cookie " + COOKIE_NAME + " = " + val);

		try {
			com.freshdirect.fdstore.customer.FDUser user = FDCustomerManager.recognize(val);
			return new FDSessionUser(user, request.getSession());

		} catch (FDAuthenticationException ex) {
			LOGGER.warn(ex);
			return null;
		}

	}

	public static void storeCookie(FDSessionUser user, HttpServletResponse response) {
		String cookieId = user.getCookie();
		Cookie cookie = new Cookie(COOKIE_NAME, cookieId);
		cookie.setMaxAge(COOKIE_MAXAGE);
		cookie.setPath("/");
		response.addCookie(cookie);
		user.setCookie(cookieId);
		LOGGER.debug("Set cookie " + COOKIE_NAME + " = " + cookie.getValue());
	}

	public static void clearCookie(HttpServletResponse response) {
		Cookie c = new Cookie(COOKIE_NAME, "");
		c.setMaxAge(0);
		c.setPath("/");
		response.addCookie(c);
		LOGGER.debug("Cleared cookie " + COOKIE_NAME);
	}
			
}