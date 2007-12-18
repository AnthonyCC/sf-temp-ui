package com.freshdirect.webapp.taglib.dlv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.delivery.DlvPaymentManager;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.BodyTagSupport;

public class DlvUnconfirmControllerTag extends BodyTagSupport {

	public DlvUnconfirmControllerTag() {
		super();
	}
	
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
						pageContext.setAttribute(id, saleInfo);
						pageContext.setAttribute("ORDER_NUMBER", orderNumber);

				} else if ("unconfirm_order".equalsIgnoreCase(action)) {
					//check if this is a double click
					if(session.getAttribute(PROCESSING_REQUEST) != null){
						LOGGER.warn("Double click ignoring the second request");
						return EVAL_BODY_BUFFERED;
					}
			
					session.setAttribute(PROCESSING_REQUEST, Boolean.TRUE);
					DlvPaymentManager.getInstance().unconfirmOrder(orderNumber);

				} 
			}catch (DlvResourceException de) {
				LOGGER.warn("DlvResource: ", de);
				errors.addError(new ActionError("resource_error", de.getMessage()+" order # " + orderNumber));
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
			
				new VariableInfo(data.getAttributeString("id"), "com.freshdirect.customer.DlvSaleInfo", true, VariableInfo.NESTED),
				new VariableInfo("ORDER_NUMBER", "java.lang.String", true, VariableInfo.NESTED),
				new VariableInfo(data.getAttributeString("result"), "com.freshdirect.framework.webapp.ActionResult", true, VariableInfo.NESTED)};
		}
	}


}
