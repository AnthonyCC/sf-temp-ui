/*
 * DlvPassAvailabilityControllerTag.java
 *
 * Created on March 4, 2002, 11:21 AM
 */

package com.freshdirect.webapp.taglib.dlvpass;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.deliverypass.EnumDlvPassProfileType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class DlvPassAvailabilityControllerTag extends AbstractControllerTag {
	private static final String REASON_NOT_ELIGIBLE = "Not currently eligible for DeliveryPass. Please contact Customer Service.";
	private static final String REASON_TOO_MANY_PASSES = "Too many DeliveryPasses added";
	private static final String REASON_PASS_EXISTS = "Account contains an active or pending DeliveryPass.<br>Please see the Your Account section for more information";
	private static final String REASON_MAX_PASSES="Account already has the maximum number of allowable DeliveryPasses.";
	private static final String REASON_PROMOTIONAL_PASS="Not currently eligible for DeliveryPasses. Please contact Customer Service at {0}. ";
	private static final String REASON_MULTIPLE_AUTORENEW_PASSES="You already have a DeliveryPass scheduled to automatically renew.";
	
	private static Category LOGGER = LoggerFactory.getInstance(DlvPassAvailabilityControllerTag.class);
	private final static Comparator<FDCartLineI> PRICE_COMPARATOR = new Comparator<FDCartLineI>() {
		public int compare(FDCartLineI o1, FDCartLineI o2) {
			Double amt2 = new Double(o2.getPrice());
			Double amt1 = new Double(o1.getPrice());
			return amt2.compareTo(amt1);
		}
	};

	private String id;
	
	public void setId(String id){
		this.id = id;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		LOGGER.debug("Inside performAction of DlvPassAvailabilityControllerTag");
		try{
			
			HttpSession session = pageContext.getSession();
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			FDCartModel cart = user.getShoppingCart();
			int dlvPassCount = cart.getDeliveryPassCount();
			if(dlvPassCount == 0){
				return true;
			}
			Map<String,FDAvailabilityInfo> unavMap = cart.getUnavailabilityMap();
			//addedList - contains list of passes that was added to the cart during this session.
			List<FDCartLineI> dlvPasses = new ArrayList<FDCartLineI>();
			boolean lastPurchasedPass = false;
			for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				String key = new Integer(line.getRandomId()).toString();
				if(line.lookupFDProduct().isDeliveryPass() && !(unavMap.containsKey(key))){
					dlvPasses.add(line);
					if(line instanceof FDModifyCartLineI)
						lastPurchasedPass = true;
				}
			}
			if(dlvPasses.size() == 1 && lastPurchasedPass){
				//There is no new delivery passes added to cart during modify order. So return
				return true;
			}
			List<DlvPassAvailabilityInfo> unavailableList = new ArrayList<DlvPassAvailabilityInfo>();
			if(!user.isEligibleForDeliveryPass()){
				addToUnavailableList(dlvPasses, unavailableList, REASON_NOT_ELIGIBLE);
			}
			else if(user.getDlvPassInfo().getUsablePassCount()>=FDStoreProperties.getMaxDlvPassPurchaseLimit()) {
				
				addToUnavailableList(dlvPasses, unavailableList, REASON_MAX_PASSES);
			}
			else {
				//User is eligible for delivery pass.
				EnumDlvPassStatus status = user.getDeliveryPassStatus();

				/*if(!DeliveryPassUtil.isEligibleStatus(status) && !DeliveryPassUtil.isOriginalOrder(user)){
					addToUnavailableList(dlvPasses, unavailableList, REASON_PASS_EXISTS);
				}else {*///--commented for DP1.1
				
					//User has eligible status to buy a pass. Pull out the eligible and ineligible passes.
					List<FDCartLineI> eligibleList = new ArrayList<FDCartLineI>();
					List<FDCartLineI> ineligibleList = new ArrayList<FDCartLineI>();
					checkForEligiblePasses(user, dlvPasses, eligibleList, ineligibleList,unavailableList,request);
					//addToUnavailableList(ineligibleList, unavailableList, REASON_NOT_ELIGIBLE);
					if(eligibleList.size() > 1) {
						/*
						 * There are more than one delivery pass. Keep the high 
						 * priced delivery pass in the cart. Push the remaining passes
						 * into the unavailable list.
						 */
						List<FDCartLineI> lowPriceList = retainHighPricedDlvPass(eligibleList);
						addToUnavailableList(lowPriceList, unavailableList, REASON_TOO_MANY_PASSES);
					}
					//}--commented for DP1.1
			}
			cart.setUnavailablePasses(unavailableList);
			pageContext.setAttribute(this.id, unavailableList);
		}catch(FDResourceException e){
			throw new JspException(e);
		}
		LOGGER.debug("Before returning true ");
		return true;
	}

	private List<FDCartLineI> retainHighPricedDlvPass(List<FDCartLineI> eligibleList) {
		Collections.sort(eligibleList, PRICE_COMPARATOR);
		Iterator<FDCartLineI> i=eligibleList.iterator();
		//Skip the high priced delivery pass(first item) from the list.
		i.next();
		List<FDCartLineI> lowPriceList = new ArrayList<FDCartLineI>();
		while(i.hasNext()){
			FDCartLineI line = (FDCartLineI)i.next();
			lowPriceList.add(line);	
		}
		return lowPriceList;
	}
	
	private void checkForEligiblePasses(FDUserI user, List<FDCartLineI> dlvPasses, List<FDCartLineI> eligibleList, List<FDCartLineI> inEligibleList, List<DlvPassAvailabilityInfo> unavailableList,HttpServletRequest request) throws FDResourceException{
		EnumDlvPassProfileType profileType = user.getEligibleDeliveryPass();
		Iterator<FDCartLineI> i=dlvPasses.iterator();
		FDCartLineI line=null;
		DeliveryPassType type=null;
		int autoRenewPassCount=user.getDlvPassInfo().getAutoRenewUsablePassCount();
		while(i.hasNext()){
			line = (FDCartLineI)i.next();
			type = DeliveryPassType.getEnum(line.lookupFDProduct().getSkuCode());
			
			if( autoRenewPassCount>0 && type.isAutoRenewDP()) {
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList, REASON_MULTIPLE_AUTORENEW_PASSES);
			}
			else if(user.getDlvPassInfo().isFreeTrialRestricted() && type.isFreeTrialDP()) {
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList, REASON_NOT_ELIGIBLE);
			}
			else if((type.isUnlimited() && (EnumDlvPassProfileType.UNLIMITED.equals(profileType)||EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED.equals(profileType)||EnumDlvPassProfileType.AMAZON_PRIME.equals(profileType)))
					|| (!type.isUnlimited() && EnumDlvPassProfileType.BSGS.equals(profileType))){
				if( EnumDlvPassProfileType.UNLIMITED.equals(profileType) &&
					( FDStoreProperties.getUnlimitedAmazonPrimeProfile().equals(type.getProfileValue())||
					  FDStoreProperties.getUnlimitedPromotionalProfile().equals(type.getProfileValue())		
					)	
				   ) {
					inEligibleList.add(line);
					addToUnavailableList(line, unavailableList, formatPhoneMsg(REASON_PROMOTIONAL_PASS,request));
				}
				else {
					eligibleList.add(line);
				}
			}else if((type.isUnlimited() && EnumDlvPassProfileType.BSGS.equals(profileType))
					|| (!type.isUnlimited() && (EnumDlvPassProfileType.UNLIMITED.equals(profileType)||EnumDlvPassProfileType.PROMOTIONAL_UNLIMITED.equals(profileType)||EnumDlvPassProfileType.AMAZON_PRIME.equals(profileType)))){
				inEligibleList.add(line);
				addToUnavailableList(line, unavailableList, REASON_NOT_ELIGIBLE);
			}

		}
		
		
	}
	
	private void addToUnavailableList(FDCartLineI line, List<DlvPassAvailabilityInfo> unavailableList, String reasonCode){
		    if(line!=null) {
		    	DlvPassAvailabilityInfo info = new DlvPassAvailabilityInfo(new Integer(line.getRandomId()), reasonCode);
		    	unavailableList.add(info);
		    }
		
	}
	
	private void addToUnavailableList(List<FDCartLineI> sourceList, List<DlvPassAvailabilityInfo> unavailableList, String reasonCode){
		Iterator<FDCartLineI> i=sourceList.iterator();
		while(i.hasNext()){
			FDCartLineI line = i.next();
			DlvPassAvailabilityInfo info = new DlvPassAvailabilityInfo(new Integer(line.getRandomId()), reasonCode);
			unavailableList.add(info);
		}
	}
	private String formatPhoneMsg(String pattern,HttpServletRequest request) {
		return MessageFormat.format(
			pattern,
			new Object[] {UserUtil.getCustomerServiceContact(request)});
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
	/**
	 * @return false to SKIP_BODY without redirect
	 */
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException{
		return true;
	}
}