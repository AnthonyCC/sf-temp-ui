package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmAddressControllerTag extends AbstractControllerTag {
	
	private ErpAddressModel address;
 
	public void setAddress(ErpAddressModel address){
		this.address = address;
	}

	
	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			if(this.getActionName().equalsIgnoreCase("addAddress")){
				this.addAddress(request, actionResult);
			}
			if(this.getActionName().equalsIgnoreCase("deleteAddress")){
				this.deleteAddress(request, actionResult);
			}
			if(this.getActionName().equalsIgnoreCase("editAddress")){
				this.editAddress(request, actionResult);
			}
		}catch(FDResourceException e){
			throw new JspException(e);
		}
		return true;
	}
	
	private void editAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populateAddress(request);
		this.validateAddress(actionResult);
		this.validateAltDeliveryFields(actionResult);
		if(actionResult.isSuccess()){
			if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
				AddressUtil.updateShipToAddress(request, actionResult, CrmSession.getUser(pageContext.getSession()), this.address.getPK().getId(), this.address);
			}
		}
	}

	private void deleteAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		FDUserI user = CrmSession.getUser(pageContext.getSession());
		if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
			String addressPk = request.getParameter("deleteAddressId");
			AddressUtil.deleteShipToAddress(user.getIdentity(), addressPk, actionResult, request);
		}
	}

	private void addAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		this.populateAddress(request);
		this.validateAddress(actionResult);
		this.validateAltDeliveryFields(actionResult);
		
		if(actionResult.isSuccess()){
			if(CrmSession.verifyCaseAttachment(pageContext.getSession(), actionResult)){
				try{
					
					FDCustomerManager.addShipToAddress(AccountActivityUtil.getActionInfo(request.getSession()), !CrmSession.getUser(pageContext.getSession()).isDepotUser(), this.address);
				}catch(ErpDuplicateAddressException e){
					actionResult.addError(true, "dupilcateAddress", e.getMessage());
				}
			}
		}
	}
	
	private void populateAddress(HttpServletRequest request){
		EnumServiceType serviceType = EnumServiceType.getEnum(NVL.apply(request.getParameter(EnumUserInfoName.DLV_SERVICE_TYPE.getCode()), ""));
		this.address.setServiceType(serviceType);
		this.address.setFirstName(NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "").trim());
		this.address.setLastName(NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "").trim());
		this.address.setCompanyName(NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "").trim());
		this.address.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());
		this.address.setAddress2(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "").trim());
		this.address.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		this.address.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		this.address.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim());
		this.address.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim());
		//Replace Special Characters (_, ~, ^, *, &) from the input by the customers
		this.address.setInstructions(NVL.apply(request.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode()).replaceAll(FDStoreProperties.getDlvInstructionsSpecialChar(), " "), "").trim());
		String phone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode()), "").trim();
		String ext = NVL.apply(request.getParameter(EnumUserInfoName.DLV_HOME_PHONE_EXT.getCode()), "").trim();
		this.address.setPhone(new PhoneNumber(phone, ext));
		String altContactPhone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode()), "").trim();
		String altContactExt = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode()), "").trim();
		this.address.setAltContactPhone(new PhoneNumber(altContactPhone, altContactExt));
		EnumDeliverySetting altDelivery = EnumDeliverySetting.getDeliverySetting(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode()), ""));
		this.address.setAltDelivery(altDelivery);
		if(EnumDeliverySetting.NEIGHBOR.equals(altDelivery)){
			this.address.setAltFirstName(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode()), "").trim());
			this.address.setAltLastName(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_LASTNAME.getCode()), "").trim());
			this.address.setAltApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_APARTMENT.getCode()), "").trim());
			String altDlvPhone =NVL.apply(request.getParameter(EnumUserInfoName.DLV_ALT_PHONE.getCode()), "").trim();
			if (!"".equals(altDlvPhone)) {
				this.address.setAltPhone(new PhoneNumber(altDlvPhone));
			}
		}

		// the user has seen the unattended delivery notice, thus if he has "unchecked" the option, 
        // he has explicitly opted out
        EnumUnattendedDeliveryFlag unattendedDeliveryFLag = EnumUnattendedDeliveryFlag.OPT_OUT;
        String unattendedDeliveryStr = request.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode());
        if (unattendedDeliveryStr != null) {
        	if (unattendedDeliveryStr.equals("OPT_IN")) unattendedDeliveryFLag = EnumUnattendedDeliveryFlag.OPT_IN;
        	
        	this.address.setUnattendedDeliveryInstructions(
        		NVL.apply(request.getParameter(
        				EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode()).replaceAll(
        						FDStoreProperties.getDlvInstructionsSpecialChar(), " "), "").trim());
     
        }
        this.address.setUnattendedDeliveryFlag(unattendedDeliveryFLag);
	}
	
	private void validateAddress(ActionResult result) throws FDResourceException{
		result.addError("".equals(this.address.getFirstName()), EnumUserInfoName.DLV_FIRST_NAME.getCode(), "required");
		result.addError("".equals(this.address.getLastName()), EnumUserInfoName.DLV_LAST_NAME.getCode(), "required");
		result.addError("".equals(this.address.getAddress1()), EnumUserInfoName.DLV_ADDRESS_1.getCode(), "required");
		result.addError("".equals(this.address.getCity()), EnumUserInfoName.DLV_CITY.getCode(), "required");
		result.addError("".equals(this.address.getState()), EnumUserInfoName.DLV_STATE.getCode(), "required");
		result.addError(
			!AddressUtil.validateState(this.address.getState()),
			EnumUserInfoName.DLV_STATE.getCode(),
			SystemMessageList.MSG_UNRECOGNIZE_STATE);
		result.addError("".equals(this.address.getZipCode()), EnumUserInfoName.DLV_ZIPCODE.getCode(), "required");
		result.addError(address.getPhone() == null || "".equals(this.address.getPhone().getPhone()), EnumUserInfoName.DLV_HOME_PHONE.getCode(), "required");
		if(result.isSuccess()){
			AddressModel scrubbedAddress = AddressUtil.scrubAddress(this.address, result);
			if(result.isSuccess()){
				this.address.setAddressInfo(scrubbedAddress.getAddressInfo());
				if(scrubbedAddress.getAddressType().equals(EnumAddressType.FIRM) && !this.address.getServiceType().equals(EnumServiceType.CORPORATE)){
					result.addError(new ActionError(EnumUserInfoName.DLV_SERVICE_TYPE.getCode(), SystemMessageList.MSG_COMMERCIAL_ADDRESS));
				}
			}
			
			if("SUFFOLK".equals(FDDeliveryManager.getInstance().getCounty(scrubbedAddress)) && (this.address.getAltContactPhone() == null || "".equals(this.address.getAltContactPhone().getPhone()))){
				result.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
				return;
			}
		}
		
	}
	
	private void validateAltDeliveryFields(ActionResult result) {
        
		// validate fields
		EnumDeliverySetting altDelivery = this.address.getAltDelivery();

		if (EnumDeliverySetting.NEIGHBOR.equals(altDelivery)) {
            String altDlvField = this.address.getAltLastName();
			result.addError(
				altDlvField==null || altDlvField.length() < 1,
				EnumUserInfoName.DLV_ALT_LASTNAME.getCode(), SystemMessageList.MSG_REQUIRED
			);
			
			altDlvField = this.address.getAltFirstName();			
			result.addError(
				altDlvField==null || altDlvField.length() < 1,
				EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(), SystemMessageList.MSG_REQUIRED
			);
			altDlvField = this.address.getAltApartment();
			result.addError(
				altDlvField==null || altDlvField.length() < 1,
				EnumUserInfoName.DLV_ALT_APARTMENT.getCode(), SystemMessageList.MSG_REQUIRED
			);
			PhoneNumber altDlvPhone = this.address.getAltPhone();
			result.addError(
				altDlvPhone==null || "".equals(altDlvPhone.getPhone()),
				EnumUserInfoName.DLV_ALT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED
			);
 		}
	}

	
	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}
}
