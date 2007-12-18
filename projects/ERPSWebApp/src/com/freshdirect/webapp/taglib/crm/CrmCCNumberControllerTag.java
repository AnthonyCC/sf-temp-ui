package com.freshdirect.webapp.taglib.crm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmCCNumberControllerTag extends AbstractControllerTag {
	
	private String orderId;
	private String id;
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		if(!agent.isAuthorizedToSeeAuthAndCCInfo()){
			actionResult.addError(true, "authentication", "You are not authorized to see this info.");
			return true;
		}
		String password = NVL.apply(request.getParameter("password"), "");
		if("".equals(password)){
			actionResult.addError(true, "password", SystemMessageList.MSG_REQUIRED);
			return true;
		}
		try {
			CrmManager.getInstance().loginAgent(agent.getUserId(), password);
			FDOrderI order = FDCustomerManager.getOrder(this.orderId);
			List ccList = new ArrayList();
			ccList.add(order.getPaymentMethod());
			pageContext.setAttribute(this.id, ccList);
			CrmManager.getInstance().logViewAccount(agent, order.getCustomerId());
			
 		} catch (FDResourceException e) {
			actionResult.addError(true, "technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
		} catch (CrmAuthenticationException e) {
			actionResult.addError(true, "authentication", "Password is wrong");
		}
		return true;
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				 new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),
				new VariableInfo(
					data.getAttributeString("id"),
					"java.util.List",
					true,
					VariableInfo.NESTED )
					
			};
		}
	}
}
