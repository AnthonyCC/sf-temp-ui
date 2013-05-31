package com.freshdirect.webapp.taglib.location;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class LocationHandlerTag extends SimpleTagSupport {
	
	public static String SELECTED_ADDRESS_ATTR = "locationHandlerSelectedAddress";
	public static String ALL_PICKUP_DEPOTS_ATTR =	"locationHandlerAllPickupDepots";
	public static String SELECTED_PICKUP_DEPOT_ID_ATTR = "locationHandlerSelectedPickDepotId";
	public static String ACTION_RESULT_ATTR = "locationHandlerActionResult";
	public static String SERVER_ERROR_ATTR = "locationHandlerServerErrorResult";
	public static String DISABLED_ATTR = "locationHandlerDisabled";
	
	public static String SERVICE_TYPE_PARAMETER = "setServiceType";
	public static String FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER = "futureZoneNotificationEmail";
	public static String ZIP_CODE_PARAMETER = "zipCode";
	
	public static String ZIP_CODE_PATTERN = "\\d{5}";

	private static final Logger LOGGER = LoggerFactory.getInstance(LocationHandlerTag.class);
	
	private String action;
	private PageContext ctx;
	private HttpServletRequest request;
	private FDSessionUser user;
	private ActionResult result = new ActionResult();
	
	@Override
	public void doTag() {
		try {
			ctx = (PageContext) getJspContext();
			request = (HttpServletRequest) ctx.getRequest();
			user = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
			
			if (user==null){
				ctx.setAttribute(SERVER_ERROR_ATTR, "Server error occured. Sorry for your inconvinience, please refresh this page.");
			
			} else {
				LOGGER.debug("action: " + action);

				if ("selectAddress".equalsIgnoreCase(action)){
					doSelectAddressAction();
				} else if ("setZipCode".equalsIgnoreCase(action)){
					doSetZipCodeAction();
				} else if ("setMoreInfo".equalsIgnoreCase(action)){
					doSetMoreInfoAction();
				} else if ("futureZoneNotification".equalsIgnoreCase(action)){
					doFutureZoneNotificationAction();
				} else { //no action parameter
					doSetServiceTypeAction();
				}
				
				doExportAttributes();
			}
			
		} catch (Exception e) {
			LOGGER.error(e);
			ctx.setAttribute(SERVER_ERROR_ATTR, e.getMessage());
		}

		ctx.setAttribute(ACTION_RESULT_ATTR, result);
	}
	
	/** based on step_1_choose.jsp*/
	private void doSelectAddressAction() throws JspException, FDResourceException{
		DeliveryAddressManipulator m = new DeliveryAddressManipulator(ctx, result, "setDeliveryAddress");
		m.setLocationHandlerMode(true);
		try {
			m.performSetDeliveryAddress("");
		} catch (RedirectToPage e) {
			LOGGER.debug(e); //do not do redirect, determine success based on result instead
		}
		handleNewAddressSet();
		CmRegistrationTag.setPendingAddressChangeEvent(ctx.getSession());
	}
	
	private void doSetZipCodeAction() throws FDResourceException{
		String zipCode = processZipCodeField();

		if (result.isSuccess()){
			AddressModel address=new AddressModel();
			address.setZipCode(zipCode);

			StateCounty stateCounty = FDDeliveryManager.getInstance().lookupStateCountyByZip(zipCode);
			if (stateCounty == null){
				LOGGER.info("stateCountry is null for zip: "+ zipCode);
			} else {
				address.setState(stateCounty.getState());
				address.setCity(stateCounty.getCity());
			}
			//no error check needed here, front end will display no delivery error if needed
			handleNewServiceResult(FDDeliveryManager.getInstance().checkZipCode(zipCode));
			user.setAddress(address);
			handleNewAddressSet();
			user.updateUserState(); //based on DeliveryAddressManipulator.performSetDeliveryAddress()
		}
	}

	
	/** based on SiteAccessControllerTag.populate(), validate() and checkByAddress() */
	private void doSetMoreInfoAction() throws FDResourceException{
		
		AddressModel address=new AddressModel();
		address.setZipCode(processZipCodeField());
		address.setAddress1(processRequiredField(EnumUserInfoName.DLV_ADDRESS_1));		
		address.setApartment(processFieldValue(EnumUserInfoName.DLV_APARTMENT.getCode()));
		address.setCity(processRequiredField(EnumUserInfoName.DLV_CITY));
		address.setState(processRequiredField(EnumUserInfoName.DLV_STATE));				

		if(result.isSuccess()){
			address = AddressUtil.scrubAddress(address, true, result);
		}
		if(result.isSuccess()){
			try {
				handleNewServiceResult(FDDeliveryManager.getInstance().checkAddress(address));
				user.setAddress(address);
				handleNewAddressSet();
				CmRegistrationTag.setPendingAddressChangeEvent(ctx.getSession()); //send CM tag after reload
			} catch (FDInvalidAddressException e) {
				result.addError(true, EnumUserInfoName.DLV_CANT_GEOCODE.getCode(), MessageFormat.format(SystemMessageList.MSG_CANT_GEOCODE_ZIP_CHECK, new Object[] {UserUtil.getCustomerServiceContact(request)}));
			}
		}
	}

	private String processFieldValue(String fieldName) {
		return NVL.apply(request.getParameter(fieldName),"").trim();
	}

	
	private String processRequiredField(EnumUserInfoName userInfo){
		String fieldName = userInfo.getCode();
		String fieldValue = processFieldValue(fieldName);
		if ("".equals(fieldValue)){
			result.addError(true, fieldName, SystemMessageList.MSG_REQUIRED);
		}
		return fieldValue;
	}

	private String processZipCodeField(){
		String zipCode = processRequiredField(EnumUserInfoName.DLV_ZIPCODE);
		if (!zipCode.matches(ZIP_CODE_PATTERN) || zipCode.equals("00000")){
			result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
		}
		return zipCode;
	}

	private void handleNewServiceResult(DlvServiceSelectionResult serviceResult) throws FDResourceException{

		Set<EnumServiceType> availableServices = serviceResult.getAvailableServices();
		user.setAvailableServices(availableServices);

		if (user.getSelectedServiceType()==EnumServiceType.PICKUP){
			handleNewServiceType(ServiceTypeUtil.getPreferedServiceType(availableServices));
		}
	}
	
	private void handleNewAddressSet(){
		user.setMoreInfoPopupShownForCurrentAddress(false); //reset for new address
		user.setFutureZoneNotificationEmailSentForCurrentAddress(false);
	}
	
	/** based on DeliveryAddressManipulator logic*/
	private void handleNewServiceType(EnumServiceType serviceType){
		user.setSelectedServiceType(serviceType);
		user.setZPServiceType(serviceType); //added for zone pricing to keep user service type up-to-date
	}
	
	
	/** based on SiteAccessControllerTag.saveEmail() */
	private void doFutureZoneNotificationAction() throws FDResourceException{
		
		String email = request.getParameter(FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER);
		
		if(EmailUtil.isValidEmailAddress(email)){
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, user.getZipCode(),user.getSelectedServiceType());
			user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
		} else {
			result.addError(true, FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER, SystemMessageList.MSG_EMAIL_FORMAT);
		}
	}
	
	private void doSetServiceTypeAction(){
		String serviceType = request.getParameter(SERVICE_TYPE_PARAMETER);

		if (serviceType!=null && !"".equals(serviceType)){
			try {
				handleNewServiceType(EnumServiceType.valueOf(serviceType));
				user.updateUserState(); //for robustness only
			} catch (IllegalArgumentException e){
				LOGGER.error("Cannot set service type to "+serviceType);
				result.addError(true, SERVICE_TYPE_PARAMETER, "Error unknown service type");
			}
		}
	}
	
	private void doExportAttributes() throws FDResourceException{

		AddressModel selectedAddress = user.getSelectedAddress();
		ctx.setAttribute(SELECTED_ADDRESS_ATTR, selectedAddress);

		if (selectedAddress instanceof ErpDepotAddressModel){
			ctx.setAttribute(SELECTED_PICKUP_DEPOT_ID_ATTR, ((ErpDepotAddressModel)selectedAddress).getLocationId());
		}
		
		List<DlvLocationModel> allPickupDepots = new ArrayList<DlvLocationModel>();
		for (Object pickupDepotObj : FDDepotManager.getInstance().getPickupDepots()){
			if (pickupDepotObj instanceof DlvDepotModel){
				DlvDepotModel pickupDepot = (DlvDepotModel) pickupDepotObj;
				if (!"HAM".equalsIgnoreCase(pickupDepot.getDepotCode())) { //based on i_pickup_depot_locations.jspf
					for (Object locationObj : pickupDepot.getLocations()){
						if (locationObj instanceof DlvLocationModel){
							allPickupDepots.add((DlvLocationModel) locationObj);
						}
					}
				}
			}
		}
		ctx.setAttribute(ALL_PICKUP_DEPOTS_ATTR, allPickupDepots);
		
		String uri = request.getRequestURI().toLowerCase();
		if (uri.contains("/checkout/") || uri.contains("/login/")){
			ctx.setAttribute(DISABLED_ATTR, true);
		}
	}
	
	public static String formatAddressText(AddressModel address){
		return StringUtil.concatWithDelimiters(new String[]{address.getAddress1(), address.getAddress2(), address.getCity(), address.getState()}, new String[]{" ", ", ", ", "});
	}
	
	public static String formatAddressShortText(AddressModel address){
		return StringUtil.concatWithDelimiters(new String[]{address.getCity(), address.getState()}, new String[]{", "});
	}
	
	public void setAction(String action){
		this.action = action;
	}

}