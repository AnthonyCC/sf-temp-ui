
package com.freshdirect.webapp.taglib.fdstore;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractControllerTag;


public class CheckAvailableTimeslotsTag extends AbstractControllerTag {

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {

	 	String actionName = this.getActionName();
	 	
	 	if ("makeAddress".equalsIgnoreCase(actionName)){
	 		
	 		AddressForm form = new AddressForm();
	 		
	 		form.populateForm(request);
	 		form.validateForm(result);
	 		
	 		if (!result.isSuccess()) //return with error(s)
	 			return true;
	 			
	 		Calendar tomorrow = Calendar.getInstance();
	 		tomorrow.add(Calendar.DAY_OF_YEAR,1);
	 		
	 		try {
	 			//TODO have to fix this 
		 		DlvServiceSelectionResult serviceResult = FDDeliveryManager.getInstance().checkAddress(form.getAddress());
		 		EnumDeliveryStatus status = serviceResult.getServiceStatus(EnumServiceType.HOME);
		 		if (!EnumDeliveryStatus.DELIVER.equals(status)) {
		 			//
		 			// we don't deliver to this address
		 			//
		 			result.addError(new ActionError(EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(), SystemMessageList.MSG_DONT_DELIVER_TO_ADDRESS));
		 		}
		 		
	 		} catch (FDInvalidAddressException fdiae) {
	 			//
	 			// this address doesn't make sense
	 			//
	 			result.addError(new ActionError(EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), SystemMessageList.MSG_UNRECOGNIZE_ADDRESS));
	 			
	 		} catch (FDResourceException fdre) {
	 			//
	 			// technical difficulties
	 			//
	 			result.addError(new ActionError("technicalDifficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
	 		}
	 		
	 		
	 		if(result.isSuccess()){

	 			FDSessionUser user = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);
	 			user.getShoppingCart().setDeliveryAddress(form.getAddress());
	 			pageContext.getSession().setAttribute(SessionName.USER, user);

	 		}


	 	}
	 	return true;

	 }


	 private static class AddressForm implements WebFormI {

	 	private String address1;
	 	private String zipCode;

	 	public void populateForm(HttpServletRequest request) {
	 		this.address1 = request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode());
	 		this.zipCode = request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode());
	 	}


	 	public void validateForm(ActionResult result) {
	 		result.addError(address1==null || address1.length() < 1,
	 			 EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED
        	);
        	if(zipCode==null || zipCode.length() < 5) {
            	result.addError(new ActionError(EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED));
        	}
        	
        	
	 	}

	 	public ErpAddressModel getAddress(){

	 		ErpAddressModel address = new ErpAddressModel();
	 		address.setAddress1(this.address1);
	 		address.setZipCode(this.zipCode);

	 		return address;
	 	}
	 }

    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}

