package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class UserValidationUtil {

	/**
	 * @return boolean true if order minimum conditions are met 
	 */

	public static boolean validateCartNotEmpty(HttpServletRequest request, ActionResult result) {
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		if (user.getShoppingCart().getOrderLines().isEmpty()) {
			result.addError(new ActionError("order_minimum", SystemMessageList.MSG_CHECKOUT_CART_EMPTY));
			return false;
		}
		return true;
	}
	
	public static boolean validateRecipientNotEmpty(HttpServletRequest request, ActionResult result) {
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		if (user.getRecipientList().size() == 0) {
			result.addError(new ActionError("order_minimum", SystemMessageList.MSG_CHECKOUT_RECIPIENT_EMPTY));
			return false;
		}
		return true;
	}

	public static boolean validateContainsDlvPassOnly(HttpServletRequest request, ActionResult result) {
		//Check to see if cart contains only delivery pass.
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();
		//if(cart.containsDlvPassOnly() && DeliveryPassUtil.isEligibleStatus(status)) {
		if(cart.containsDlvPassOnly()) {
			result.addError(new ActionError("error_dlv_pass_only", SystemMessageList.MSG_CONTAINS_DLV_PASS_ONLY));
			return true;
		}
		return false;
	}
	
	public static boolean validateOrderMinimum(HttpServletRequest request, ActionResult result) throws FDResourceException {
		
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		
		if (!validateCartNotEmpty(request,result))	{
			return false;
		}
		
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		CallcenterUser ccUser = (CallcenterUser) session.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
		if (ccUser!=null ||agent!=null) {
			// callcenter users/CRM agents users are allowed to place orders less than $40
			return true;
		}
	   
		if (!user.isOrderMinimumMet()) {

			Double subTotal = new Double(user.getShoppingCart().getSubTotal());
			Double minimumOrder =  new Double(user.getMinimumOrderAmount());
			/*
			ErpAddressModel dlvAddress = user.getShoppingCart().getDeliveryAddress();
			boolean isPickup =
				user.isPickupOnly()
					|| (dlvAddress instanceof ErpDepotAddressModel && ((ErpDepotAddressModel) dlvAddress).isPickup());
			*/
			result.addError(
				new ActionError(
					"order_minimum",
					MessageFormat.format(
						SystemMessageList.MSG_CHECKOUT_BELOW_MINIMUM,
						new Object[] { subTotal, minimumOrder })));

			return false;

		}

		return true;			
	}
	
	public static boolean validateRecipientListEmpty(HttpServletRequest request, ActionResult result) throws FDResourceException {
		
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	   
		if (user.getRecipientList().size() == 0) {
			result.addError(
				new ActionError(
					"recipients_empty",
						SystemMessageList.MSG_CHECKOUT_RECIPIENTS_EMPTY));

			return true;

		}

		return false;			
	}
	
	public static boolean validateBulkRecipientListEmpty(HttpServletRequest request, ActionResult result) throws FDResourceException {
		
		HttpSession session = request.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	   
		if (user.getBulkRecipentList().size() == 0) {
			result.addError(
				new ActionError(
					"recipients_empty",
						SystemMessageList.MSG_CHECKOUT_RECIPIENTS_EMPTY));

			return true;

		}

		return false;			
	}
}
