/*
 * Created on Mar 3, 2004
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvApartmentRange;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumAddressExceptionReason;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class ZipPlus4AddressTag extends AbstractControllerTag implements SessionName {

	private static final long	serialVersionUID	= -8398592411912966288L;

	private static Category LOGGER = LoggerFactory.getInstance(ZipPlus4AddressTag.class);

	private String id;
	private AddressModel dlvAddress = new AddressModel();

	List<AddressModel> suggestions; // a holder for suggested addresses if the original address is not unique
	EnumAddressVerificationResult verificationResult;
	String geocodeResult;
	List<DlvApartmentRange> aptRanges;

	public void setId(String id) {
		this.id = id;
	}

	private void setAddress() {
		pageContext.setAttribute(id, dlvAddress);
	}

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		setAddress();
		return true;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		String actionName = this.getActionName();
		try {
			if ("addressCheck".equalsIgnoreCase(actionName)) {
				this.performAddressCheck(request, actionResult);
			} else if ("addApartment".equalsIgnoreCase(actionName)) {
				this.performAddApartment(request, actionResult);
			}

		} catch (FDResourceException ex) {
			LOGGER.error("Error performing action " + actionName, ex);
			actionResult.addError(
				new ActionError(EnumUserInfoName.TECHNICAL_DIFFICULTY.getCode(), SystemMessageList.MSG_TECHNICAL_ERROR));
		}

		setAddress();

		return true;
	}

	protected void performAddressCheck(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {

		populateAddress(request);
		validateAddress(actionResult);

		DlvAddressVerificationResponse verifyResponse = FDDeliveryManager.getInstance().scrubAddress(dlvAddress);

		//
		// set to scrubbed address
		//
		dlvAddress = verifyResponse.getAddress();

		verificationResult = verifyResponse.getResult();

		addVerificationResultErrors(verificationResult, actionResult);
		
		if (!EnumAddressVerificationResult.ADDRESS_BAD.equals(verificationResult)) {
			//
			// geocode address
			//
			try {
				DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress(this.dlvAddress);
				this.geocodeResult = geocodeResponse.getResult();
				this.dlvAddress = geocodeResponse.getAddress();
				
			} catch (FDInvalidAddressException iae) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return;
			}

			addGeocodeResultErrors(geocodeResult, actionResult);

		}

		if (EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE.equals(verificationResult)) {
			//
			// get a list of suggestions to use instead of the non-unique address entered by the user
			//
			try {
				suggestions = FDDeliveryManager.getInstance().findSuggestionsForAmbiguousAddress(dlvAddress);
				pageContext.setAttribute("suggestions", suggestions);
			} catch (FDInvalidAddressException iae) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return;
			}
		} else if (
			EnumAddressVerificationResult.ADDRESS_OK.equals(verificationResult)
				|| EnumAddressVerificationResult.APT_WRONG.equals(verificationResult)) {
			//
			// get building apartments
			//
			try {
				aptRanges = FDDeliveryManager.getInstance().findApartmentRanges(dlvAddress);
				pageContext.setAttribute("aptRanges", aptRanges);
				
				DlvServiceSelectionResult serviceResult=FDDeliveryManager.getInstance().checkAddress(dlvAddress);
				EnumDeliveryStatus status=serviceResult.getServiceStatus(dlvAddress.getServiceType());
				pageContext.setAttribute("deliveryStatus", status);
				
				Calendar date = new GregorianCalendar();
				date.add(Calendar.DATE, 7);
				DlvZoneInfoModel zoneInfo=FDDeliveryManager.getInstance().getZoneInfo(dlvAddress, date.getTime());
				pageContext.setAttribute("zoneInfo", zoneInfo);
				
				String county = FDDeliveryManager.getInstance().getCounty(dlvAddress.getCity(), dlvAddress.getState());
				pageContext.setAttribute("county", county);
				
				Set<EnumServiceType> availServices = FDDeliveryManager.getInstance().checkAddress(dlvAddress).getAvailableServices();
				pageContext.setAttribute("availServices", availServices);
			} catch (InvalidAddressException iae) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return;
			} catch (FDInvalidAddressException fdiae) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
				return;
			}
		}

		if (!actionResult.isSuccess()) {
			return;
		}

	}

	protected void performAddApartment(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		
		this.populateAddress(request);
		this.validateAddress(actionResult);
		
		FDDeliveryManager.getInstance().scrubAddress(dlvAddress);
		
		String county = FDDeliveryManager.getInstance().getCounty(dlvAddress.getCity(), dlvAddress.getState());
		if(county == null){
			actionResult.addError(new ActionError(EnumUserInfoName.DLV_CITY.getCode(), "Please enter a valid city, state combination"));
		}
		
		if (actionResult.isSuccess()) {
			try {
				String streetAddress = dlvAddress.getAddress1();
				if(dlvAddress.getScrubbedStreet() == null){
					streetAddress = AddressScrubber.standardizeForUSPS(dlvAddress.getAddress1());
				}
				
//				CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
				String agentId = CrmSession.getCurrentAgentStr(request.getSession());
				
				ExceptionAddress ex = new ExceptionAddress();
				ex.setStreetAddress(streetAddress);
				ex.setAptNumLow(this.dlvAddress.getApartment());
				ex.setAptNumHigh(this.dlvAddress.getApartment());
				ex.setZip(this.dlvAddress.getZipCode());
				ex.setAddressType(EnumAddressType.HIGHRISE);
				ex.setReason(EnumAddressExceptionReason.ADD_APT);
//				ex.setUserId(agent.getUserId());
				ex.setUserId(agentId);
				ex.setCounty(county);
				ex.setState(this.dlvAddress.getState());
				ex.setCity(this.dlvAddress.getCity());
				
				FDDeliveryManager.getInstance().addExceptionAddress(ex);
			} catch (InvalidAddressException e) {
				e.printStackTrace();
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
			}
		}
		
	}

	public void populateAddress(HttpServletRequest request) throws FDResourceException {
		dlvAddress.setAddress1(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim());
		dlvAddress.setAddress2(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "").trim());
		dlvAddress.setApartment(NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim());
		dlvAddress.setCity(NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim());
		dlvAddress.setState(NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), ""));
		dlvAddress.setZipCode(NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), ""));
	}
	
	private void validateAddress(ActionResult result) {

		result.addError(
			"".equals(dlvAddress.getAddress1()),
			EnumUserInfoName.DLV_ADDRESS_1.getCode(),
			SystemMessageList.MSG_REQUIRED);
		result.addError("".equals(dlvAddress.getCity()), EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);
		if (dlvAddress.getState().length() < 2) {
			result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
		} else {
			result.addError(
				!AddressUtil.validateState(dlvAddress.getState()),
				EnumUserInfoName.DLV_STATE.getCode(),
				SystemMessageList.MSG_UNRECOGNIZE_STATE);
		}

		result.addError(
			dlvAddress.getZipCode().length() < 5,
			EnumUserInfoName.DLV_ZIPCODE.getCode(),
			SystemMessageList.MSG_REQUIRED);
		
		if ("addApartment".equalsIgnoreCase(this.getActionName())) {
			result.addError(
			"".equals(dlvAddress.getApartment()),
			EnumUserInfoName.DLV_APARTMENT.getCode(),
			SystemMessageList.MSG_REQUIRED);
		}
	}

	private void addVerificationResultErrors(EnumAddressVerificationResult result, ActionResult actionResult) {
		
		if (EnumAddressVerificationResult.ADDRESS_BAD.equals(result)){
			actionResult.addError(true, "dlv_address", EnumAddressVerificationResult.ADDRESS_BAD.getCode());
			//assume address1 indecipherable if has entry
			if (!"".equals(dlvAddress.getAddress1())) {
				actionResult.addError(true, EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
			}
		} else if (!EnumAddressVerificationResult.ADDRESS_OK.equals(result)) {
			actionResult.addError(true, "dlv_address", result.getCode());
		}

	}

	private void addGeocodeResultErrors(String result, ActionResult actionResult) {
		if (!result.equals("GEOCODE_OK")) {
			actionResult.addError(true, "dlv_address_geocode", result);
		}
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),
				new VariableInfo(data.getAttributeString("id"), "com.freshdirect.common.address.AddressModel", true, VariableInfo.NESTED),
				new VariableInfo("suggestions", "java.util.ArrayList", true, VariableInfo.NESTED),
				new VariableInfo("aptRanges", "java.util.List", true, VariableInfo.NESTED),
				new VariableInfo("availServices", "java.util.Set", true, VariableInfo.NESTED)};
		}
	}

}
