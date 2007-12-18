package com.freshdirect.webapp.taglib.fdstore.depot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class PickupLoginControllerTag extends AbstractControllerTag {

	private final static Category LOGGER = LoggerFactory.getInstance(PickupLoginControllerTag.class);

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		if (user == null) try {

			user = new FDSessionUser(FDCustomerManager.createNewUser((String)null, EnumServiceType.PICKUP), session);

			session.setAttribute(SessionName.USER, user);
		
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
			CookieMonster.storeCookie(user, response);

		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action", ex);
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
	}

}
