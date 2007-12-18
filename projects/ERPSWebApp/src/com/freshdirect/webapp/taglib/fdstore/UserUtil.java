package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;

public class UserUtil {

	public static void createSessionUser(HttpServletRequest request, HttpServletResponse response, FDUser loginUser)
		throws FDResourceException {
		HttpSession session = request.getSession();

		FDSessionUser sessionUser = new FDSessionUser(loginUser, session);
		sessionUser.isLoggedIn(true);

		CookieMonster.storeCookie(sessionUser, response);
		sessionUser.updateUserState();

		session.setAttribute(SessionName.USER, sessionUser);
	}
	
	public static String getCustomerServiceContact(HttpServletRequest request) {
		FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
		if (user==null) {
			return SystemMessageList.CUSTOMER_SERVICE_CONTACT;
		}
		return user.getCustomerServiceContact();
	}


}
