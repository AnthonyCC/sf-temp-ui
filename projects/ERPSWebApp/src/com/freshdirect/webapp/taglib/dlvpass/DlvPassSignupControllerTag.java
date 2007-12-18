/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.dlvpass;

/**
 * 
 * @author skrishnasamy
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class DlvPassSignupControllerTag extends AbstractControllerTag {
	//Optional Attribute.
	boolean callCenter = false;
	private static Category LOGGER = LoggerFactory.getInstance( DlvPassSignupControllerTag.class);
	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = request.getParameter("action");
		try{
			if(actionName.equals("signup")){
				HttpSession session = pageContext.getSession();
				FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
				String signupUrl = null;
				boolean productDiscontinued = false;
				EnumDlvPassProfileType profileType = user.getEligibleDeliveryPass();
				String profileValue="";
				if (EnumDlvPassProfileType.AMAZON_PRIME.equals(profileType)) {
					profileValue=FDStoreProperties.getUnlimitedAmazonPrimeProfile();
					productDiscontinued=isProductDiscontinued(profileType,profileValue);
					if(productDiscontinued) {
						profileType=EnumDlvPassProfileType.UNLIMITED;
						profileValue=FDStoreProperties.getUnlimitedProfilePosfix();
						productDiscontinued=isProductDiscontinued(profileType,profileValue);
					}
				}
				else if (EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED.equals(profileType)) {
					profileValue=FDStoreProperties.getUnlimitedPromotionalProfile();
					productDiscontinued=isProductDiscontinued(profileType,profileValue);
					if(productDiscontinued) {
						profileType=EnumDlvPassProfileType.UNLIMITED;
						profileValue=FDStoreProperties.getUnlimitedProfilePosfix();
						productDiscontinued=isProductDiscontinued(profileType,profileValue);
					}
				}
				else if(EnumDlvPassProfileType.UNLIMITED.equals(profileType)){
					profileValue=FDStoreProperties.getUnlimitedProfilePosfix();
					productDiscontinued=isProductDiscontinued(profileType,profileValue);
				}
				else if(EnumDlvPassProfileType.BSGS.equals(profileType)){
					profileValue=FDStoreProperties.getBSGSProfilePosfix();
					productDiscontinued=isProductDiscontinued(profileType,profileValue);
				}
				
				/*else if(EnumDlvPassProfileType.UNLIMITED.equals(profileType)){
					
					DeliveryPassType passType=null;
					List unlimitedList= DeliveryPassType.getUnlimitedTypes();
					Iterator iter = unlimitedList.iterator();
					while(iter.hasNext()) {
						passType=(DeliveryPassType)iter.next();
						if(isUnlimitedProfile(passType.getProfileValue())) {
							productInfo = FDCachedFactory.getProductInfo(passType.getCode());
							if(!productInfo.isDiscontinued()){
								productDiscontinued = false;
								break;
							}else{
								productDiscontinued = true;
							}
						}
					}
					
					productDiscontinued = productInfo.isDiscontinued();
				}else if(EnumDlvPassProfileType.BSGS.equals(profileType)){
					
					List bsgsList = DeliveryPassType.getBSGSTypes();
					Iterator iter = bsgsList.iterator();
					while(iter.hasNext()){
						DeliveryPassType bsgs = (DeliveryPassType)iter.next();
						productInfo = FDCachedFactory.getProductInfo(bsgs.getCode());
						if(!productInfo.isDiscontinued()){
							productDiscontinued = false;
							break;
						}else{
							productDiscontinued = true;
						}
					}
				}
				else */
				
				if(productDiscontinued){
					LOGGER.error("Product was discontinued in SAP.");
					actionResult.addError(new ActionError("dlvpass_discontinued", SystemMessageList.MSG_PASS_DISCONTINUED));
				} 
				else {
					signupUrl=getSignupUrl(profileType);
					this.setSuccessPage(signupUrl);
				}	
					/*else {
				}
					
					signupUrl = FDStoreProperties.getUnlimitedSignUpUrl(callCenter);
					if(EnumDlvPassProfileType.BSGS.equals(user.getEligibleDeliveryPass())){
						signupUrl = FDStoreProperties.getBSGSSignUpUrl(callCenter);
					}
					else if(EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED.equals(user.getEligibleDeliveryPass()) && !productDiscontinued) {
						signupUrl = FDStoreProperties.getUnlimitedPromotionalSignUpUrl(callCenter);
					}
					else if(EnumDlvPassProfileType.AMAZON_PRIME.equals(user.getEligibleDeliveryPass()) && !productDiscontinued) {
						signupUrl = FDStoreProperties.getUnlimitedAmazonPrimeSignUpUrl(callCenter);
					}
					else if(EnumDlvPassProfileType.UNLIMITED.equals(user.getEligibleDeliveryPass())) {
						signupUrl = FDStoreProperties.getUnlimitedSignUpUrl(callCenter);
					}
					this.setSuccessPage(signupUrl);
				}*/				
			}
			else if(actionName.equalsIgnoreCase("FLIP_AUTORENEW")) {
				HttpSession session = pageContext.getSession();
				FDSessionUser currentUser = (FDSessionUser)session.getAttribute(SessionName.USER);
				CrmAgentModel agentModel = CrmSession.getCurrentAgent(session);
				EnumTransactionSource source=EnumTransactionSource.WEBSITE;
				String initiator="CUSTOMER";
				if(agentModel!=null) {
					source=EnumTransactionSource.CUSTOMER_REP;
					initiator=agentModel.getUserId();
				}
				String customerID=currentUser.getIdentity().getErpCustomerPK();
				//FDCustomerManager.flipAutoRenewDP(customerID);
				FDCustomerManager.flipAutoRenewDP(customerID, source, initiator);
			}
		}
		catch (Exception ex) {
			LOGGER.error("Error occurred while redirecting to Delivery Pass Signup page.");
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
 		return true;
	}
	
	
	private boolean isUnlimitedProfile(String profileValue) {
		boolean isUnlimited=false;
		
		if(profileValue==null ||(profileValue!=null)&& profileValue.trim().equals("")) {
			isUnlimited=true;
		}
		else {
			
		}
		
		return isUnlimited;
	}
	
	
	private boolean isProductDiscontinued(EnumDlvPassProfileType profileType, String profileValue) throws FDResourceException, FDSkuNotFoundException {
		
		List dlvPassTypes=getDeliveryPassTypes(profileType, profileValue);
		if(dlvPassTypes==null||(dlvPassTypes!=null &&dlvPassTypes.size()==0))
			return true;
		
		DeliveryPassType dpType=null;
		FDProductInfo productInfo=null;
		boolean productDiscontinued=false;
		for(int i=0;i<dlvPassTypes.size();i++) {
			dpType=(DeliveryPassType)dlvPassTypes.get(i);
			productInfo=FDCachedFactory.getProductInfo(dpType.getCode());
			if(!productInfo.isDiscontinued()){
				productDiscontinued = false;
				break;
			}else{
				productDiscontinued = true;
			}
			
		}
		return productDiscontinued;
	}
	
	private String getSignupUrl(EnumDlvPassProfileType profileType) {
		
		
		String signupUrl=FDStoreProperties.getUnlimitedSignUpUrl(callCenter);
		if(EnumDlvPassProfileType.BSGS.equals(profileType) ){
			signupUrl = FDStoreProperties.getBSGSSignUpUrl(callCenter);
		}
		else if(EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED.equals(profileType)) {
			signupUrl = FDStoreProperties.getUnlimitedPromotionalSignUpUrl(callCenter);
		}
		else if(EnumDlvPassProfileType.AMAZON_PRIME.equals(profileType) ) {
			signupUrl = FDStoreProperties.getUnlimitedAmazonPrimeSignUpUrl(callCenter);
		}
		else if(EnumDlvPassProfileType.UNLIMITED.equals(profileType)) {
			signupUrl = FDStoreProperties.getUnlimitedSignUpUrl(callCenter);
		}
		return signupUrl;
	}
	


	public static class TagEI extends AbstractControllerTag.TagEI {
		//default impl
	}

	public void setCallCenter(boolean callCenter) {
		this.callCenter = callCenter;
	}

	private List getDeliveryPassTypes(EnumDlvPassProfileType profileType, String profileValue) {
		
		DeliveryPassType dlvPassType=null;
		List responseList=new ArrayList();
		List dpTypes=null;
		if(profileValue.indexOf(FDStoreProperties.getBSGSProfilePosfix())!=-1) {
			return DeliveryPassType.getBSGSTypes();
		}
		else if(profileType.equals(EnumDlvPassProfileType.UNLIMITED)) {
			dpTypes=DeliveryPassType.getUnlimitedTypes();
			if((dpTypes==null)||(dpTypes!=null &&dpTypes.size()!=0)) {
		    	for(int i=0;i<dpTypes.size();i++) {
		    		dlvPassType=(DeliveryPassType)dpTypes.get(i);
		    		if((dlvPassType.getProfileValue()==null) ||((dlvPassType.getProfileValue()!=null)&&dlvPassType.getProfileValue().trim().equals(""))) {
		    			responseList.add(dlvPassType);
		    		}
		    	}
		    }
		}
		else {
			dpTypes=DeliveryPassType.getEnumList();
			if(dpTypes!=null) {
				for(int i=0;i<dpTypes.size();i++) {
					dlvPassType=(DeliveryPassType)dpTypes.get(i);
					if((dlvPassType.getProfileValue()!=null)&& dlvPassType.getProfileValue().equals(profileValue)) {
						responseList.add(dlvPassType);
					}
				}
			}
		}
		return responseList;
	}


}
