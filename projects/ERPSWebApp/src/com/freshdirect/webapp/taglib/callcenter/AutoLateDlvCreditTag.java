package com.freshdirect.webapp.taglib.callcenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.customer.EnumComplaintLineType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassExtendReason;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class AutoLateDlvCreditTag extends AbstractControllerTag {
	
	private static final long serialVersionUID = 1L;
	
	private static final Category LOGGER = LoggerFactory.getInstance(AutoLateDlvCreditTag.class);


	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {		
		HttpSession session = (HttpSession) request.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		System.out.println("Came to execure AutoLateDlvCreditTag");
		try {			
			if("submitted".equals(request.getParameter("action"))) {
				System.out.println("Action is submitted");
				//Process credits
				String autoId = request.getParameter("autoId");
				String orderId = "";
				//Get all the selected sale Id's and start credit processing
				Map<String, String[]> parameters = request.getParameterMap();
				for(String parameter : parameters.keySet()) {
					if(parameter.toLowerCase().startsWith("saleid|") || parameter.toLowerCase().startsWith("dlvPassSaleId|")) {
					    if(parameter.toLowerCase().startsWith("saleid|")) {
					        String[] values = parameters.get(parameter);
					        String value = values[0];
					        if(value != null) {
					        	orderId = value;				 
					        	
					        	//Check if the order has been already credited
					        	boolean isCredited = CrmManager.getInstance().isOrderCreditedForLateDelivery(orderId);
					        	
					        	if(!isCredited) {
						        	//Credit the user with delivery fee
					        		issueLateCredit(orderId, autoId, agent, false);
					        	}
					        }
					    } else if(parameter.toLowerCase().startsWith("dlvPassSaleId|")) {
					    	String[] values = parameters.get(parameter);
					        String value = values[0];
					        if(value != null) {
					        	orderId = value;
					        	//Extend dlv PAss
					        	CustomerCreditModel ccm = CrmManager.getInstance().getOrderForLateCredit(orderId, autoId);
					        	
					        	DeliveryPassModel dpm = CrmManager.getInstance().getDeliveryPassInfo(ccm.getDlvPassId());		
					        	
					        	//check if DP is already extended
					        	if(!CrmManager.getInstance().isDlvPassAlreadyExtended(orderId, ccm.getCustomerId())) {
					        	
						        	//Check if the DP is expired.
						        	if(dpm.getExpirationDate().before(new java.util.Date())) {
						        		//DP is expired, see if there is any active DP to renew
						        		DeliveryPassModel altDp = CrmManager.getInstance().getActiveDP(ccm.getCustomerId());
						        		if(altDp == null) {
						        			//No more DP's for the user. Credit the original amount
						        			issueLateCredit(orderId, autoId, agent, true);
						        		} else {
						        			//extend DP by one week
						        			extendDP(altDp, agent, orderId, ccm);
						        		}
						        	} else {
						        		//extend DP by one week
						        		extendDP(dpm, agent, orderId, ccm);
						        	}
					        	} else {
					        		LOGGER.info("Already extended the deliverypass:" + orderId + "-dpId:" + ccm.getDlvPassId());
					        	}
					        }
					    }
					    //Mark the saleId as approved
						CrmManager.getInstance().updateAutoLateCredit(autoId, orderId);
					}
				}
				//Update rest of the records as not approved by the user
				CrmManager.getInstance().updateLateCreditsRejected(autoId, agent.getUserId());
			}			
		} catch(FDResourceException e) {
			
		}
		return true;
	}
	
	private static void extendDP(DeliveryPassModel dpm, CrmAgentModel agent, String orderId, CustomerCreditModel ccm ) throws FDResourceException {
		int numDays = 7;
		try {			
			CrmManager.getInstance().incrExpirationPeriod(dpm, agent, numDays, "Late Delivery Extn", ccm.getNewCode(), orderId);
			FDIdentity identity = new FDIdentity(ccm.getCustomerId(), ccm.getFdCustomerId());
			FDCustomerInfo custInfo = FDCustomerManager.getCustomerInfo(identity);
			FDCustomerManager.doEmail(FDEmailFactory.getInstance().createDPCreditEmail(custInfo, orderId, 1,EnumDlvPassProfileType.UNLIMITED.getName()));
		} catch (CrmAuthorizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void issueLateCredit(String orderId, String autoId, CrmAgentModel agent, boolean dpCredit) throws FDResourceException {
		//Create FDORderI object
		FDOrderI order = FDCustomerManager.getOrder(orderId);
		CustomerCreditModel ccm = CrmManager.getInstance().getOrderForLateCredit(orderId, autoId);
		//Create complaint
		ErpComplaintModel complaintModel = new ErpComplaintModel();
		Map complaintReasons = CallCenterServices.getComplaintReasons(false);
		
		//Create complin line
		List<ErpComplaintLineModel> lines = new ArrayList<ErpComplaintLineModel>();
		ErpComplaintLineModel line = new ErpComplaintLineModel();
        // Set up the Complaint Line Model with proper info	        
        line.setType(EnumComplaintLineType.DEPARTMENT);
        //line.setQuantity(null);
        if(dpCredit) {
        	line.setAmount(ccm.getOriginalAmount());
        } else {
        	line.setAmount(ccm.getRemainingAmount());
        }
        
        ErpComplaintReason ecr = ComplaintUtil.getReasonByCompCode(ccm.getNewCode());
        if(ecr != null) {
        	line.setReason( ecr );
        } else {
        	List list = (List) complaintReasons.get("GDW");
        	if(list == null)
            	list = Collections.EMPTY_LIST;
        	if(list.size() > 0)
                line.setReason( (ErpComplaintReason) list.get(0));
        }
        line.setMethod( EnumComplaintLineMethod.STORE_CREDIT );
        
        lines.add(line);
        complaintModel.addComplaintLines(lines);
        complaintModel.setType(EnumComplaintType.getEnum(complaintModel.getComplaintMethod()));
        
        //set complaint details
        complaintModel.setCreatedBy(agent.getUserId());
        complaintModel.setDescription("Late Delivery Credit for $" + ccm.getOriginalAmount());
        complaintModel.setCreateDate(new java.util.Date());
        complaintModel.setStatus(EnumComplaintStatus.PENDING);
        //email options - don;t send for now
        complaintModel.setEmailOption(EnumSendCreditEmail.SEND_ON_COMPLAINT_CREATION);
        complaintModel.setCustomerEmail(new ErpCustomerEmailModel());
        
        System.out.println("Almost done with complaint:"+ (complaintModel.describe()));
        
        //addcomplaint
        boolean autoApproveAuthorized = true;
        try {
			FDCustomerManager.addComplaint(complaintModel, orderId, new FDIdentity(ccm.getCustomerId(), ccm.getFdCustomerId()),autoApproveAuthorized, ccm.getOriginalAmount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static class TagEI extends AbstractControllerTag.TagEI {
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),};

		}
	}

}