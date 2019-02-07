package com.freshdirect.webapp.taglib.crm;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmResubmitOrderControllerTag extends AbstractControllerTag {

	private final String RESUBMIT_ACTION = "resubmit";
	private final String AUTHORIZE_ACTION = "authorize";

	private String orderId;

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {

		boolean actionPerformed = false;
		if (RESUBMIT_ACTION.equalsIgnoreCase(this.getActionName())) {
			this.resubmitOrder(request, result);
			if(result.isSuccess()){
				StringBuffer successPage = new StringBuffer();
				successPage.append(this.getSuccessPage());
				if (this.getSuccessPage().indexOf("?") < 0) {
					successPage.append("?");
				} else {
					successPage.append("&");
				}
				successPage.append("status=resubmitted");
				setSuccessPage(successPage.toString());
				actionPerformed = true;
			}
		} else if (AUTHORIZE_ACTION.equalsIgnoreCase(this.getActionName())) {
			this.authorizeOrder(request, result);
			if(result.isSuccess()){
				StringBuffer successPage = new StringBuffer();
				successPage.append(this.getSuccessPage());
				if (this.getSuccessPage().indexOf("?") < 0) {
					successPage.append("?");
				} else {
					successPage.append("&");
				}
				successPage.append("status=authorized");
				setSuccessPage(successPage.toString());
				actionPerformed = true;
			}
		}
		
		if(!actionPerformed) {
			this.setSuccessPage(null);
		}

		return true;
	}

	protected void resubmitOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Cancel Modify order: load the shopping cart with the old items
		//
		try {
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			FDOrderI order = FDCustomerManager.getOrderForCRM(this.orderId);
			CustomerRatingAdaptor cra = new CustomerRatingAdaptor(user.getFDCustomer().getProfile(), user.isCorporateUser(), user
				.getAdjustedValidOrderCount());
			CallCenterServices.resubmitOrder(this.orderId, cra,order.getOrderType());

		} catch (FDResourceException ex) {
			throw new JspException(ex.getMessage());
		} catch (ErpTransactionException ex) {
			results.addError(new ActionError("order_status", "This order is not in the proper state to be resubmitted."));
		}
	}
	protected void authorizeOrder(HttpServletRequest request, ActionResult results) throws JspException {
		HttpSession session = request.getSession();

		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
		if (currentUser.getLevel() < FDUserI.SIGNED_IN) {
			throw new JspException("No customer was found for the requested action.");
		}
		//
		// Cancel Modify order: load the shopping cart with the old items
		//
		try {
			//FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		
			FDOrderI order = FDCustomerManager.getOrderForCRM(this.orderId);
			Date now=DateUtil.getCurrentTime();
			if( ( EnumSaleStatus.INPROCESS_NO_AUTHORIZATION.equals(order.getOrderStatus()) &&
				  now.after(order.getDeliveryReservation().getStartTime())
				 ) || EnumSaleStatus.AUTHORIZATION_FAILED.equals(order.getOrderStatus())
			   ){
				FDCustomerManager.authorizeSale(this.orderId, true);
			}
			else {
				results.addError(new ActionError("order_status", "This order is not in the proper state to be authorized."));
			}


		} catch (FDResourceException ex) {
			throw new JspException(ex.getMessage());
		} /*catch (ErpTransactionException ex) {
			results.addError(new ActionError("order_status", "This order is not in the proper state to be authorized."));
		}*/
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}

}
