package com.freshdirect.webapp.taglib.dlvpass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmManager;
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
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.callcenter.ComplaintCreatorTag;
import org.apache.log4j.Category;

public class DeliveryPassControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER 	= LoggerFactory.getInstance( ComplaintCreatorTag.class );
	private DeliveryPassModel dlvPass;
	private int increment;
	private int noOfWeeks;
	//private String info;
	
	public void setNoOfWeeks(int noOfWeeks) {
		this.noOfWeeks = noOfWeeks;
	}
	
	public void setIncrement(int delta) {
		this.increment = delta;
	}

	public void setDlvPass(DeliveryPassModel dlvPass) {
		this.dlvPass = dlvPass;
	}

	/*public void setInfo(String infoName) {
		this.info = infoName;
	}*/

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			String orderAssigned = request.getParameter("orderAssigned");
			String extendReason = request.getParameter("extendReason");
			String cancelReason = request.getParameter("cancelReason");
			String notes = request.getParameter("notes");
			//int count=Integer.parseInt(request.getParameter("incrCount").trim());
			int count=0;
			StringBuffer buffer = null;
			
			HttpSession session = pageContext.getSession();
			FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
			
			CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);
			CrmManager crmManager=CrmManager.getInstance();
			if ("incr_dlvcount".equalsIgnoreCase(this.getActionName())) {
				count=Integer.parseInt(request.getParameter("incrCount").trim());
				FDCustomerInfo custInfo=getCustomerInfo(session);
				setIncrement(count);
				crmManager.incrDeliveryCount(this.dlvPass, agentModel, increment, notes, extendReason, orderAssigned);
				
				sendEmail(custInfo, orderAssigned,count,EnumDlvPassProfileType.BSGS);
				if(count==1) {
					buffer = new StringBuffer(SystemMessageList.MSG_DLV_PASS_SINGLE_CREDIT);
				}
				else {
					buffer = new StringBuffer(count+SystemMessageList.MSG_DLV_PASS_MULTIPLE_CREDIT);
				}
				
			}
			if ("incr_expperiod".equalsIgnoreCase(this.getActionName())) {
				count=Integer.parseInt(request.getParameter("incrCount").trim());
				FDCustomerInfo custInfo=getCustomerInfo(session);
				
				setNoOfWeeks(count);
				crmManager.incrExpirationPeriod(this.dlvPass, agentModel, this.noOfWeeks * 7, notes, extendReason, orderAssigned);
				sendEmail(custInfo,orderAssigned, count,EnumDlvPassProfileType.UNLIMITED);
				
				if(count==1) {
					buffer = new StringBuffer(SystemMessageList.MSG_DLV_PASS_EXTENDED_SINGLE_WEEK);
				}
				else {
					buffer = new StringBuffer(count+SystemMessageList.MSG_DLV_PASS_EXTENDED_MULTIPLE_WEEKS);
				}
			}

			if ("cancel_pass".equalsIgnoreCase(this.getActionName())) {
				crmManager.cancelDeliveryPass(this.dlvPass, agentModel, notes, cancelReason, orderAssigned);
				//Load the delivery pass status from DB.
				currentUser.updateDlvPassInfo();
				buffer = new StringBuffer(SystemMessageList.MSG_DLV_PASS_CANCELLED);
			}
			if(buffer==null) {
				this.setSuccessPage("/main/delivery_pass.jsp");
			}
			else {
				this.setSuccessPage("/main/delivery_pass.jsp?successMsg="+buffer.toString());
			}
			pageContext.getSession().removeAttribute(DlvPassConstants.DLV_PASS_SESSION_ID);

		}catch(CrmAuthorizationException e){
			actionResult.addError(new ActionError("unauthorized_msg", e.getMessage()));
		}catch (FDResourceException e) {
			actionResult.setError(e.getMessage());
			throw new JspException(e);
		}
		return true;
	}

	private FDCustomerInfo getCustomerInfo(HttpSession session) throws JspException{
		
		FDIdentity identity;
		FDCustomerInfo custInfo; 
		try {
			 identity = ((FDUserI)session.getAttribute(SessionName.USER)).getIdentity();
			 custInfo = FDCustomerManager.getCustomerInfo(identity);
		} catch (FDResourceException ex) {
			LOGGER.warn("FDResourceException while building ErpComplaintModel", ex);
			throw new JspException(ex.getMessage());
		}
		return custInfo;
		
	}
	
	private void sendEmail(FDCustomerInfo customer, String saleId, int creditCount, EnumDlvPassProfileType dlvPassProfile) throws FDResourceException {
		
		FDCustomerManager.doEmail(FDEmailFactory.getInstance().createDPCreditEmail(customer, saleId, creditCount,dlvPassProfile.getName()));
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
