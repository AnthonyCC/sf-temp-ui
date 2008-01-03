package com.freshdirect.webapp.taglib.crm;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.adapter.CustomerRatingAdaptor;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class CrmResubmitOrdersTag extends AbstractControllerTag {

	private String id;
	private static Category LOGGER = LoggerFactory.getInstance(CrmResubmitOrdersTag.class);

	public void setId(String id) {
		this.id = id;
	}
	
	protected  boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String saleIds[] = request.getParameterValues("resubmitSaleId");
		if (saleIds!=null) {
			int idIdx=0;
			actionResult.addError(new ActionError("submitted", "There were "+saleIds.length+" orders marked for resubmittal."));
			for (idIdx = 0; idIdx < saleIds.length; idIdx++) {
				try {
					FDOrderI order = FDCustomerManager.getOrder(saleIds[idIdx]);
					String erpCustomerId = order.getCustomerId();
					FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(erpCustomerId);
					FDIdentity fdIdentity = new FDIdentity(erpCustomerId, fdCustomer.getPK().getId());
					
					FDUser user = FDCustomerManager.recognize(fdIdentity);
					//THis change was made as part of PERF-22.
					//BEGIN
					/*
					 * Actually you don't have to make call to user.getAdjustedValidOrderCount() which will
					 * actually invoke FDCUstomerManager.getOrderHistoryInfo() to load all the order history
					 * info which is unnecessary and expensive. So replaced it with FDCustomerManager.getValidOrderCount().
					 * No need of adjusted valid order count here as it always going to be valid order count here.
					 */
					//CustomerRatingAdaptor cra = new CustomerRatingAdaptor(fdCustomer.getProfile(),user.isCorporateUser(),user.getAdjustedValidOrderCount());
					CustomerRatingAdaptor cra = new CustomerRatingAdaptor(fdCustomer.getProfile(),user.isCorporateUser(),FDCustomerManager.getValidOrderCount(fdIdentity));
					//END
					CallCenterServices.resubmitOrder(saleIds[idIdx],cra);
					actionResult.addError(new ActionError("submitted_"+idIdx, "Order id: "+saleIds[idIdx]+" was resubmitted."));
				} catch (FDResourceException ex) {
					LOGGER.warn("Caught FDResoureException in CrmResubmitOrdersTag.performAction() ",ex);
					throw new JspException(ex.getMessage());
				} catch (ErpTransactionException ex) {
					LOGGER.warn("Caught ErpTransactionException in CrmResubmitOrdersTag.performAction() ",ex);
					actionResult.addError(new ActionError("order_status", "Order id: "+saleIds[idIdx]+" is not in the proper state to be resubmitted."));
				} catch (FDAuthenticationException ae){
					LOGGER.warn("Caught FDAuthenticationException in CrmResubmitOrdersTag.performAction() ",ae);
					actionResult.addError(new ActionError("cannot_authenticate", "Order id: "+saleIds[idIdx]+" user could not be authenticated."));
				}
			}
		} else {
			actionResult.addError(new ActionError("submitted", "There were no orders marked for resubmittal."));
		}
		
		pageContext.setAttribute(this.id, Collections.EMPTY_LIST);
		return true;
	}
	
	/**
	 * template method to handle get request if need be
	 */
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		List orderList = null;
		try {
				String[] status = {EnumSaleStatus.NOT_SUBMITTED.getStatusCode(), EnumSaleStatus.MODIFIED.getStatusCode(),
						EnumSaleStatus.MODIFIED_CANCELED.getStatusCode(), EnumSaleStatus.NEW.getStatusCode() };
				orderList =  CallCenterServices.getNSMOrders();
			} catch (FDResourceException fdre) {
				LOGGER.warn("Caught ErpTransactionException in CrmResubmitOrdersTag.performAction() ",fdre);
				throw new JspException(" Caught FDResoureceException. ",fdre);
			}
			if (orderList==null) orderList = Collections.EMPTY_LIST;
			pageContext.setAttribute(this.id, orderList);
			return true;
	}


	 public static class TagEI extends TagExtraInfo {
		   public VariableInfo[] getVariableInfo(TagData data) {
	        return new VariableInfo[] {
	            new VariableInfo(data.getAttributeString("id"),
	                            "java.util.List",
	                            true,
	                            VariableInfo.NESTED),
					            new VariableInfo(data.getAttributeString("result"),
		                            "com.freshdirect.framework.webapp.ActionResult",
		                            true,
		                            VariableInfo.NESTED)             
	        };

	    }
	}

	
}
