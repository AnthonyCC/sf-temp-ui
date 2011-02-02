/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.WebFormI;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class AddressForm implements WebFormI { //, AddressName
  
    private String street1;
    private String street2;
    private String apt;
    private String city;
    private String state;
    private String zipcode;
    private String lastName;
	private String firstName;
    private String homePhone;
    private String homePhoneExt;
    private String altContactPhone;
    private String altContactExt;
    private String instructions;
    private String companyName;
    private EnumServiceType serviceType;
    
    private String alternateDeliverySetting = ""; // (none, doorman, neighbor);
    
    private String altDlvLastName = "";
    private String altDlvFirstName = "";
    private String altDlvPhone = "";
    private String altDlvExt = "";
    private String altDlvApartment = "";
    
    private EnumUnattendedDeliveryFlag unattendedDeliveryFLag = EnumUnattendedDeliveryFlag.NOT_SEEN;
    private String unattendedDeliveryInstructions = "";

	protected String getParam(HttpServletRequest request, String fieldName) {
		return NVL.apply(request.getParameter(fieldName), "").trim();
	}
	
    public void populateForm(HttpServletRequest request) {
        
        street1 = getParam(request, EnumUserInfoName.DLV_ADDRESS_1.getCode());
        street2 = getParam(request, EnumUserInfoName.DLV_ADDRESS_2.getCode());
        apt = getParam(request, EnumUserInfoName.DLV_APARTMENT.getCode());
        city = getParam(request, EnumUserInfoName.DLV_CITY.getCode());
        state = getParam(request, EnumUserInfoName.DLV_STATE.getCode());
        zipcode = getParam(request, EnumUserInfoName.DLV_ZIPCODE.getCode());
        
        lastName = getParam(request, EnumUserInfoName.DLV_LAST_NAME.getCode());
        firstName = getParam(request, EnumUserInfoName.DLV_FIRST_NAME.getCode());
        
        homePhone = request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode());
        if (homePhone == null) {
            homePhone = request.getParameter("homephone");
        }
        homePhoneExt = request.getParameter("homephoneext");
        
        altContactPhone = this.getParam(request, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode());
        altContactExt = this.getParam(request, EnumUserInfoName.DLV_ALT_CONTACT_EXT.getCode());
        
        //Replace the special characters that may exist in customer's input with spaces.
        
        instructions = NVL.apply(request.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode()).replaceAll(FDStoreProperties.getDlvInstructionsSpecialChar(), " "), "");
        
        alternateDeliverySetting = request.getParameter(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode());
        
        altDlvLastName = request.getParameter(EnumUserInfoName.DLV_ALT_LASTNAME.getCode());
        altDlvFirstName = request.getParameter(EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode());
        altDlvPhone = request.getParameter(EnumUserInfoName.DLV_ALT_PHONE.getCode());
        altDlvExt = request.getParameter(EnumUserInfoName.DLV_ALT_EXT.getCode());
        altDlvApartment = request.getParameter(EnumUserInfoName.DLV_ALT_APARTMENT.getCode());
        
        companyName = request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode());
        serviceType = EnumServiceType.getEnum(request.getParameter(EnumUserInfoName.DLV_SERVICE_TYPE.getCode()));
        
        //
        // must check for nulls since we may not be explicitly asking for delivery name/phone in the form
        // in this case, we want to defualt to the customer name and home phone number
		// 
		// !!! what kind of crap is this? :)
        //
        if ( "".equals(firstName) ) {
            firstName = getParam(request, "first_name");
        }
        if ( "".equals(lastName) ) {
            lastName = getParam(request, "last_name");
        }
        if ( homePhone == null || homePhone.trim().equals("") ) {
            homePhone = request.getParameter("homephone");
        }
        
        if ( homePhone == null || homePhone.trim().equals("") ) {
            homePhone = request.getParameter("homephone");
        }
        
        
        
        if ("true".equals(request.getParameter(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN.getCode()))) {
        
        	// the user has seen the unattended delivery notice, thus if he has "unchecked" the option, 
        	// he has explicitly opted out
        	unattendedDeliveryFLag = EnumUnattendedDeliveryFlag.OPT_OUT;
        	String unattendedDeliveryStr = request.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT.getCode());
        	if (unattendedDeliveryStr != null) {
        		if (unattendedDeliveryStr.equals("OPT_IN")) unattendedDeliveryFLag = EnumUnattendedDeliveryFlag.OPT_IN;
        	
        		unattendedDeliveryInstructions = NVL.apply(request.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS.getCode()), "");
        		unattendedDeliveryInstructions = unattendedDeliveryInstructions.replaceAll(FDStoreProperties.getDlvInstructionsSpecialChar(), " ");
        	}
        }
        
    }
    
	protected String getFirstName() {
		return this.firstName;
	}
	
	protected String getLastName() {
		return this.lastName;
	}

    public AddressModel getDeliveryAddress() {
		AddressModel address = new AddressModel();
        this.decorateAddress(address);
        return address;
    }

	public ErpAddressModel getErpAddress() {
		
		ErpAddressModel erpAddress = new ErpAddressModel();

		this.decorateAddress(erpAddress);	
		
		erpAddress.setCompanyName(companyName);
		erpAddress.setServiceType(serviceType);	
		
        erpAddress.setFirstName(firstName);
        erpAddress.setLastName(lastName);
        erpAddress.setPhone( new PhoneNumber(homePhone, homePhoneExt) );
        if(!"".equals(altContactPhone)){
        	erpAddress.setAltContactPhone(new PhoneNumber(altContactPhone, altContactExt));
        }
        erpAddress.setInstructions(instructions);
     
        if ((alternateDeliverySetting == null) && "".equals(alternateDeliverySetting)) {
            erpAddress.setAltDelivery(EnumDeliverySetting.NONE);
        } else {
            erpAddress.setAltDelivery(EnumDeliverySetting.getDeliverySetting(alternateDeliverySetting));
            if (EnumDeliverySetting.NEIGHBOR.getDeliveryCode().equalsIgnoreCase(alternateDeliverySetting)) {
            	if ((altDlvFirstName != null) && (!altDlvFirstName.equals(""))) {
                    erpAddress.setAltFirstName(altDlvFirstName);
                }
                if ((altDlvLastName != null) && (!altDlvLastName.equals(""))) {
                    erpAddress.setAltLastName(altDlvLastName);
                }
                if ((altDlvApartment != null) && (!altDlvApartment.equals(""))) {
                    erpAddress.setAltApartment(altDlvApartment);
                }
                if ((altDlvPhone != null) && (!altDlvPhone.equals(""))) {
                    erpAddress.setAltPhone( new PhoneNumber(altDlvPhone, altDlvExt) );
                }
            }
        }
        
        
        erpAddress.setUnattendedDeliveryFlag(unattendedDeliveryFLag);
        erpAddress.setUnattendedDeliveryInstructions(unattendedDeliveryInstructions);
        
		return erpAddress;
	}    

	private void decorateAddress(AddressModel address) {
        address.setAddress1(street1);
        address.setAddress2(street2);
        address.setApartment(apt);
        address.setCity(city);
        address.setState(state);
        address.setZipCode(zipcode);
        address.setCompanyName(companyName);
        address.setServiceType(serviceType);

	}

	public void validateForm(ActionResult result) {
		this.validateDeliveryFields(result);
		this.validateAltDeliveryFields(result);
	}
	
    
    private void validateDeliveryFields(ActionResult result) {
        
		result.addError("".equals(lastName), EnumUserInfoName.DLV_LAST_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(firstName), EnumUserInfoName.DLV_FIRST_NAME.getCode(), SystemMessageList.MSG_REQUIRED);

        result.addError(
	        homePhone==null || homePhone.length() < 10,
	        EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_REQUIRED
        );

		result.addError("".equals(street1), EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
        result.addError("".equals(city), EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);

        if (state.length() < 2) {
            result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
        } else {
			result.addError(!AddressUtil.validateState(state), EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_UNRECOGNIZE_STATE);
        }

        result.addError(zipcode.length() < 5, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);
        
        if(EnumServiceType.CORPORATE.equals(serviceType)){
        	result.addError("".equals(companyName)||companyName==null, EnumUserInfoName.DLV_COMPANY_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
        	/* remove alt contact as being required for COS users */
        	//result.addError("".equals(altContactPhone)||altContactPhone==null, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
        }
    }
    

    private void validateAltDeliveryFields(ActionResult result) {
        
        // validate fields
        if (EnumDeliverySetting.NEIGHBOR.getDeliveryCode().equalsIgnoreCase(alternateDeliverySetting)) {
            
            result.addError(
	            altDlvLastName==null || altDlvLastName.length() < 1,
				EnumUserInfoName.DLV_ALT_LASTNAME.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
	            altDlvFirstName==null || altDlvFirstName.length() < 1,
				EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
	            altDlvApartment==null || altDlvApartment.length() < 1,
				EnumUserInfoName.DLV_ALT_APARTMENT.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
	            altDlvPhone==null || altDlvPhone.length()<1,
	            EnumUserInfoName.DLV_ALT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED
            );
            
        }
    }
  
}