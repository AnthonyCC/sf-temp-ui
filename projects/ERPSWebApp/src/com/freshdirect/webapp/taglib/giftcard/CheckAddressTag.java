
package com.freshdirect.webapp.taglib.giftcard;

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
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class CheckAddressTag extends AbstractControllerTag {

	 protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {

	 	String actionName = this.getActionName();
	 	
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
	 	return true;

	 }


	 private static class AddressForm implements WebFormI {

	 	private String address1;
	 	private String address2;
	 	private String apartment;
	 	private String zipCode;
	 	private String city;
	 	private String state;

	 	public void populateForm(HttpServletRequest request) {
	 		this.address1 = request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode());
	 		this.address2 =NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "").trim();
	 		this.zipCode = request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode());
	 		this.city= NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim();
			this.state=NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim();	
	 	}


	 	public void validateForm(ActionResult result) {
	 		result.addError(address1==null || address1.length() < 1,
	 			"incomplete_addr","Incomplete Address"
        	);
        	if(zipCode==null || zipCode.length() < 5){
            	result.addError(new ActionError ("incomplete_addr","Incomplete Address"));
        	}
        	
        	if(city==null || city.trim().length()==0) {
            	result.addError(new ActionError("incomplete_addr","Incomplete Address"));
        	}
        	if (state==null || state.trim().length()==0) {
				result.addError(true, "incomplete_addr","Incomplete Address");
			}      	        	
	 	}

	 	public ErpAddressModel getAddress(){

	 		ErpAddressModel address = new ErpAddressModel();
	 		address.setAddress1(this.address1);
	 		address.setAddress2(this.address2);
	 		address.setZipCode(this.zipCode);
            address.setCity(this.city);
            address.setState(this.state);
	 		return address;
	 	}
	 }

    public static class TagEI extends AbstractControllerTag.TagEI {
        // default impl
    }

}

