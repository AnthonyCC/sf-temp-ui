/*
 * DeliveryConfirmControllerTag.java
 *
 * Created on May 22, 2002, 7:59 PM
 */

package com.freshdirect.webapp.taglib.dlv;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.customer.*;
import com.freshdirect.delivery.*;

import com.freshdirect.framework.webapp.*;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class DeliveryConfirmControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(DeliveryConfirmControllerTag.class);
	private static final String PROCESSING_REQUEST = "processingRequest";

	private String id;
	private String result;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getResult() {
		return this.result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
		
		ActionResult errors = new ActionResult();
		String action = request.getParameter("action");
		LOGGER.debug("action value: " + action);
		if ("POST".equals(request.getMethod())) {
			String orderNumber = request.getParameter("order_number");

			try {
				if ("get_details".equalsIgnoreCase(action)) {
						
						DlvSaleInfo saleInfo = DlvPaymentManager.getInstance().getSaleInfo(orderNumber);
						ErpDeliveryInfoModel dlvInfo = DlvPaymentManager.getInstance().getDeliveryInfo(orderNumber);
	
						pageContext.setAttribute(id, dlvInfo);
						pageContext.setAttribute("status", saleInfo.getStatus());
						pageContext.setAttribute("ORDER_NUMBER", orderNumber);
	
				} else if ("confirm_order".equalsIgnoreCase(action)) {
					//check if this is a double click
					if(session.getAttribute(PROCESSING_REQUEST) != null){
						LOGGER.warn("Double click ignoring the second request");
						return EVAL_BODY_BUFFERED;
					}
				
					session.setAttribute(PROCESSING_REQUEST, Boolean.TRUE);
					DlvPaymentManager.getInstance().deliveryConfirm(orderNumber);

				} else if ("refused_order".equalsIgnoreCase(action)) {
					LOGGER.debug("Going to create a Return");
					boolean fullReturn = request.getParameter("full_return") != null;
					boolean alcoholOnly = request.getParameter("alcohol_only") != null;				
					DlvPaymentManager.getInstance().addReturn(orderNumber, fullReturn, alcoholOnly);

				} else if("redelivery".equalsIgnoreCase(action)){
					LOGGER.debug("Going to create a Redelivery");
					DlvPaymentManager.getInstance().addRedelivery(orderNumber);
				}

			}catch (DlvResourceException de) {
				LOGGER.warn("DlvResource: ", de);
				errors.addError(new ActionError("resource_error", "Due to technical difficulties could not confirm order # " + orderNumber));
			}catch (ErpSaleNotFoundException se) {
				LOGGER.warn("SaleNotFound for "+orderNumber, se);
				errors.addError(new ActionError("resource_error", "Cannot find the order #" + orderNumber));
			}finally{
				session.removeAttribute(PROCESSING_REQUEST);
			}
			
		}
		pageContext.setAttribute(result, errors);
		return EVAL_BODY_BUFFERED;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				
				new VariableInfo(data.getAttributeString("id"), "com.freshdirect.customer.ErpDeliveryInfoModel", true, VariableInfo.NESTED),
				new VariableInfo("ORDER_NUMBER", "java.lang.String", true, VariableInfo.NESTED),
				new VariableInfo("status", "com.freshdirect.customer.EnumSaleStatus", true, VariableInfo.NESTED),
				new VariableInfo(data.getAttributeString("result"), "com.freshdirect.framework.webapp.ActionResult", true, VariableInfo.NESTED)};
		}
	}

}
