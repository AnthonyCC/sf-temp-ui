package com.freshdirect.webapp.taglib.fdstore.depot;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class DepotLoginControllerTag extends AbstractControllerTag implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance(DepotLoginControllerTag.class);

	protected String depotCode;
	protected String accessCode;

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {

			if ("checkByDepotCode".equalsIgnoreCase(this.getActionName())) {
				performCheckByDepotCode(request, actionResult);
			}
		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action " + this.getActionName(), ex);
			actionResult.addError(
				new ActionError(EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return true;
	}

	protected void performCheckByDepotCode(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		populateForm(request);
		validateForm(actionResult);

		if (!actionResult.isSuccess()) {
			return;
		}

		if (!FDDepotManager.getInstance().checkAccessCode(depotCode, accessCode)) {
			actionResult.addError(
				new ActionError(EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode(), 
            		MessageFormat.format(SystemMessageList.MSG_DEPOT_WRONGCODE, 
            		new Object[] { UserUtil.getCustomerServiceContact(request)})));
			
			return;
		}
		
		this.loginUser();

	}

	protected void loginUser() throws FDResourceException {
		LOGGER.debug("Will let you in....");

		HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (((user == null) || user.isDepotUser() || user.isHomeUser()) || ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
			//
			// if there is no user object or a dummy user object created in CallCenter, make a new user for this depot
			// make sure to hang on to the cart that might be in progress in CallCenter
			//
			FDCartModel oldCart = null;
			if (user != null) {
				oldCart = user.getShoppingCart();
			}
			user = new FDSessionUser(FDCustomerManager.createNewDepotUser(depotCode, EnumServiceType.DEPOT), session);
			if (oldCart != null) {
				user.setShoppingCart(oldCart);
			}
		} else {
			//
			// otherwise, just update the depotCode in their existing object
			//
			LOGGER.debug("Setting the Depot Name!");
			user.setDepotCode(depotCode);
			FDIdentity identity = user.getIdentity();
			FDCustomerManager.storeUser(user.getUser());

			if (identity != null) {
				FDCustomerManager.setDepotCode(identity, depotCode);
			}
		}
		//
		// user is it least in pickup range, they may be in home delivery range once they register
		//
		Set availableServices = new HashSet();
		availableServices.add(EnumServiceType.PICKUP);
		user.setSelectedServiceType(EnumServiceType.PICKUP);
		user.setAvailableServices(availableServices);

		session.setAttribute(USER, user);
		CookieMonster.storeCookie(user, response);
	}

	public void populateForm(HttpServletRequest request) {
		accessCode = request.getParameter(EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode());
		if (depotCode == null) {
			//
			// only look for depotCode as a request parameter if
			// it hasn't been explicitly set as an attribute of the tag
			//
			depotCode = request.getParameter(EnumUserInfoName.DLV_DEPOT_CODE.getCode());
		}
	}

	public void validateForm(ActionResult result) {
		result.addError(
			depotCode == null || depotCode.length() < 1,
			EnumUserInfoName.DLV_DEPOT_CODE.getCode(),
			SystemMessageList.MSG_REQUIRED);

		result.addError(
			accessCode == null || accessCode.length() < 1,
			EnumUserInfoName.DLV_DEPOT_REG_CODE.getCode(),
			SystemMessageList.MSG_REQUIRED);
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
	}

}
