package com.freshdirect.webapp.taglib.crm;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringUtils;
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

public class CrmReverseAuthOrdersTag extends AbstractControllerTag {

	private String id;
	private static Category LOGGER = LoggerFactory.getInstance(CrmReverseAuthOrdersTag.class);

	public void setId(String id) {
		this.id = id;
	}
	
	protected  boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		
		String saleIds[] = request.getParameterValues("resubmitSaleId");
		if (saleIds!=null) {
			int idIdx=0;
			actionResult.addError(new ActionError("submitted", "There were "+saleIds.length+" orders marked for reverse auth."));
			for (idIdx = 0; idIdx < saleIds.length; idIdx++) {
				try {
					CallCenterServices.reverseAuthOrder(saleIds[idIdx]);
					
						actionResult.addError(new ActionError("submitted_"+idIdx, "Order id: "+saleIds[idIdx]+" was reverse authorized."));
					
				} catch (FDResourceException ex) {
					LOGGER.warn("Caught FDResoureException in CrmReverseAuthOrdersTag.performAction() ",ex);
					throw new JspException(ex.getMessage());
				} catch (ErpTransactionException ex) {
					LOGGER.warn("Caught ErpTransactionException in CrmReverseAuthOrdersTag.performAction() ",ex);
					actionResult.addError(new ActionError("submitted_"+idIdx, "Order id: "+saleIds[idIdx]+" was not reverse authorized because "+ex.getMessage()));
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
			
			   String date=request.getParameter("deliveryDate");
			   date=(null==date)?"":("".equals(date.trim()))?"":date.trim();
			 if(!StringUtils.isEmpty(date))
				orderList = CallCenterServices.getReverseAuthOrders(date);
			} catch (FDResourceException fdre) {
				LOGGER.warn("Caught Exception in CrmReverseAuthOrdersTag.performAction() ",fdre);
				throw new JspException(fdre.getMessage());
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
