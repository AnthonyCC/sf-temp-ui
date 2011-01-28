package com.freshdirect.webapp.taglib.dlvpass;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.deliverypass.DeliveryPassInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.deliverypass.DeliveryPassUtil;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.callcenter.ComplaintCreatorTag;

import org.apache.log4j.Category;

public class CancelDPControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER 	= LoggerFactory.getInstance( ComplaintCreatorTag.class );
	private List passes;

	public void setPasses(List passes) {
		this.passes = passes;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			String orderAssigned = request.getParameter("orderAssigned");
			String cancelReason = request.getParameter("cancelReason");
			String notes = request.getParameter("notes");
			
			if(getActionName().equals("")) {
				setActionName(request.getParameter("action_name"));
			}
			StringBuffer buffer = null;
			
			HttpSession session = pageContext.getSession();
			FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
			
			CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);
			CrmManager crmManager=CrmManager.getInstance();
			if ("cancel_pass".equalsIgnoreCase(this.getActionName())||"cancel_RTU_pass".equalsIgnoreCase(this.getActionName())) {
				int index=Integer.parseInt(request.getParameter("passNum"));
				DeliveryPassModel dlvPass=((DeliveryPassInfo)passes.get(index)).getModel();
				crmManager.cancelDeliveryPass(dlvPass, agentModel, notes, cancelReason, orderAssigned);
				currentUser.updateDlvPassInfo();
				buffer = new StringBuffer(SystemMessageList.MSG_DLV_PASS_CANCELLED);
				//Load the delivery pass status from DB.
			}
			if(buffer==null) {
				this.setSuccessPage("/main/delivery_pass.jsp");
			}
			else if("cancel_RTU_pass".equalsIgnoreCase(this.getActionName())) {
				this.setSuccessPage("/includes/deliverypass/pass_history_usage.jsp?successMsg="+buffer.toString());
			}
			pageContext.getSession().removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);

		}catch (FDResourceException e) {
			actionResult.setError(e.getMessage());
			throw new JspException(e);
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
