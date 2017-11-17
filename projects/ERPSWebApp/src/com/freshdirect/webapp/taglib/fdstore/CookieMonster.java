package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class CookieMonster {

    private static final Category LOGGER = LoggerFactory.getInstance(CookieMonster.class);

    private static final String COOKIE_NAME = "FDUser";
    private static final int COOKIE_MAXAGE = 365 * 24 * 60 * 60;

	public static String getCookie(HttpServletRequest request){
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
		return cookie.getValue();
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