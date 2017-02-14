package com.freshdirect.webapp.taglib.location;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel;
import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.fdstore.StateCounty;
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
import com.freshdirect.webapp.util.StandingOrderHelper;

public class LocationHandlerTag extends SimpleTagSupport {
	
	public static final String SELECTED_ADDRESS_ATTR = "locationHandlerSelectedAddress";
	public static final String ALL_PICKUP_DEPOTS_ATTR =	"locationHandlerAllPickupDepots";
	public static final String SELECTED_PICKUP_DEPOT_ID_ATTR = "locationHandlerSelectedPickDepotId";
	public static final String ACTION_RESULT_ATTR = "locationHandlerActionResult";
	public static final String SERVER_ERROR_ATTR = "locationHandlerServerErrorResult";
	public static final String DISABLED_ATTR = "locationHandlerDisabled";
	public static final String SERVICE_TYPE_MODIFICATION_ENABLED = "locationHandlerServiceTypeModificationEnabled";
	public static final String FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER = "futureZoneNotificationEmail";
	public static final String FUTURE_ZONE_NOTIFICATION_DLVTYPE_PARAMETER = "futureZoneNotificationDlvType";
	public static final String FUTURE_ZONE_NOTIFICATION_ZIPCODE_PARAMETER = "futureZoneNotificationZipCode";
	public static final String ZIP_CODE_PARAMETER = "zipCode";
	public static final String ZIP_CODE_PATTERN = "\\d{5}";
	
	public static boolean isDeliveryZone = false;

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
			
			if (user==null && !"ifDeliveryZone".equalsIgnoreCase(action)){
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
				} else if ("ifDeliveryZone".equalsIgnoreCase(action)){
					
					//find out if Fresh Direct delivers to this zip code
					isDeliveryZone = hasFdxService();
					
				} else if ("futureZoneNotificationFdx".equalsIgnoreCase(action)){
					doFutureZoneNotificationActionFdx();
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
        // Clear Standing Order Session 
		StandingOrderHelper.clearSO3Context(user, request, null);
		DeliveryAddressManipulator m = new DeliveryAddressManipulator(ctx, result, "setDeliveryAddress");
		m.setLocationHandlerMode(true);
		try {
			if(!(user.isVoucherHolder() && user.getMasqueradeContext() == null))
				m.performSetDeliveryAddress("");
		} catch (RedirectToPage e) {
			LOGGER.debug(e); //do not do redirect, determine success based on result instead
		}
		handleNewAddressSet();
		CmRegistrationTag.setPendingAddressChangeEvent(ctx.getSession());
	}
	
	public static boolean hasFdService(String zipCode) {
		try {
			FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FD);
			Set<EnumServiceType> availServices = result.getAvailableServices();

			//remove pickup for this check - APPDEV-5901
			availServices.remove(EnumServiceType.PICKUP);
			
			if (!availServices.isEmpty()) { return true; }
		} catch (FDResourceException e) {
			//LOGGER.debug(e);
		}
		
		return false;
	}
	
	public static boolean hasFdxService(String zipCode) {
		try {
			FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FDX);
			Set<EnumServiceType> availServices = result.getAvailableServices();
			
			//remove pickup
			availServices.remove(EnumServiceType.PICKUP);
			
			if (!availServices.isEmpty()) { return true; }
		} catch (FDResourceException e) {
			LOGGER.debug(e);
		}
		
		return false;
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
				address.setState(WordUtils.capitalizeFully(stateCounty.getState()));
				address.setCity(WordUtils.capitalizeFully(stateCounty.getCity()));
			}
			//no error check needed here, front end will display no delivery error if needed
			handleNewServiceResult(
				FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(
					zipCode, 
					(user.getUserContext()!=null && user.getUserContext().getStoreContext()!=null)
						? user.getUserContext().getStoreContext().getEStoreId()
						: EnumEStoreId.FD
				)
			);
			user.setAddress(address);
			handleNewAddressSet();
			user.updateUserState(); //based on DeliveryAddressManipulator.performSetDeliveryAddress()
			FDCustomerManager.storeUser(user.getUser());
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
				handleNewServiceResult(FDDeliveryManager.getInstance().getDeliveryServicesByAddress(address));
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

	private void handleNewServiceResult(FDDeliveryServiceSelectionResult serviceResult) throws FDResourceException{

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
	private boolean handleNewServiceType(EnumServiceType serviceType){
		boolean needToUpdate = (serviceType != user.getSelectedServiceType()); 
		if (needToUpdate) {
			user.setSelectedServiceType(serviceType);
			user.setZPServiceType(serviceType); //added for zone pricing to keep user service type up-to-date
		}
		return needToUpdate;
	}
	
	
	/** based on SiteAccessControllerTag.saveEmail() */
	private void doFutureZoneNotificationAction() throws FDResourceException{
		
		String email = request.getParameter(FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER);
		EnumServiceType dlvType = EnumServiceType.getEnum(request.getParameter(FUTURE_ZONE_NOTIFICATION_DLVTYPE_PARAMETER));
		String zipCode = request.getParameter(FUTURE_ZONE_NOTIFICATION_ZIPCODE_PARAMETER);
		
		if (dlvType == null) {
			//fall back to auto
			dlvType = user.getSelectedServiceType();
		}
		if (zipCode == null) {
			//fall back to auto
			zipCode = user.getZipCode();
		}
		
		if (!zipCode.matches(ZIP_CODE_PATTERN) || zipCode.equals("00000")){
			result.addError(true, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
		}
		
		if(!result.isFailure() && EmailUtil.isValidEmailAddress(email)){
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, zipCode, dlvType);
			user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
		} else {
			result.addError(true, FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER, SystemMessageList.MSG_EMAIL_FORMAT);
		}
	}

	/** based on SiteAccessControllerTag.saveEmail() */
	private void doFutureZoneNotificationActionFdx() throws FDResourceException{
		String email = request.getParameter("email");
		String zipCode = processZipCodeField();
		
		if(EmailUtil.isValidEmailAddress(email) ){
			FDDeliveryManager.getInstance().saveFutureZoneNotification(email, zipCode, user.getSelectedServiceType());
			
			user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
		} else {
			result.addError(true, FUTURE_ZONE_NOTIFICATION_EMAIL_PARAMETER, SystemMessageList.MSG_EMAIL_FORMAT);
		}
	}
	
	private boolean hasFdxService() {
		String zipCode = processZipCodeField();
		
		try {
			FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, EnumEStoreId.FDX);
			Set<EnumServiceType> availServices = result.getAvailableServices();
			if (!availServices.isEmpty()) {				
				return true;
			}
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	
	private void doSetServiceTypeAction() throws FDResourceException{
		if (isServiceTypeModificationEnabled()) {
			EnumServiceType enumServiceType = null;
		
			String url = request.getRequestURI().toLowerCase(); 
			if (url.startsWith("/cos.jsp")){ //if user lands on the Corporate Home Page - for header consistency
				enumServiceType = EnumServiceType.CORPORATE;
			
			} else if (url.startsWith("/index.jsp") || url.startsWith("/welcome.jsp")){ //if user lands on the Residential Home Pages
				enumServiceType = EnumServiceType.HOME;
				
			} 		

			if (enumServiceType != null && handleNewServiceType(enumServiceType)){
				user.updateUserState(); //for robustness only
				FDCustomerManager.storeUser(user.getUser());
			}
		}
	}
	
	private void doExportAttributes() throws FDResourceException{

		AddressModel selectedAddress = user.getSelectedAddress();
		ctx.setAttribute(SELECTED_ADDRESS_ATTR, selectedAddress);

		if (selectedAddress instanceof ErpDepotAddressModel){
			ctx.setAttribute(SELECTED_PICKUP_DEPOT_ID_ATTR, ((ErpDepotAddressModel)selectedAddress).getLocationId());
		}
		
		List<FDDeliveryDepotLocationModel> allPickupDepots = new ArrayList<FDDeliveryDepotLocationModel>();
		for (FDDeliveryDepotModel pickupDepot : FDDeliveryManager.getInstance().getPickupDepots()){
				if (!"HAM".equalsIgnoreCase(pickupDepot.getDepotCode()) && !pickupDepot.isDeactivated()) { //based on i_pickup_depot_locations.jspf
					for (FDDeliveryDepotLocationModel location : pickupDepot.getLocations()){
						allPickupDepots.add(location);
					}
				}
			}
		ctx.setAttribute(ALL_PICKUP_DEPOTS_ATTR, allPickupDepots);
		
		String uri = request.getRequestURI().toLowerCase();
		if (uri.contains("/checkout/") || uri.contains("/login/")){
			ctx.setAttribute(DISABLED_ATTR, true);
		}
		
		ctx.setAttribute(SERVICE_TYPE_MODIFICATION_ENABLED, isServiceTypeModificationEnabled());
	}
	
	private boolean isServiceTypeModificationEnabled(){
		return user.isUserCreatedInThisSession() && (user.getLevel() < FDUserI.RECOGNIZED);
	}
	
	public static String formatAddressText(AddressModel address){
		return StringUtil.concatWithDelimiters(new String[]{address.getAddress1(), address.getAddress2(), address.getCity(), address.getState()}, new String[]{" ", ", ", ", "});
	}
	
	public static String formatAddressTextWithZip(AddressModel address){
		return StringUtil.concatWithDelimiters(new String[]{address.getAddress1(), address.getAddress2(), address.getCity(), address.getState(), address.getZipCode()}, new String[]{" ", ", ", ", ", " "});
	}
	
	public static String formatAddressShortText(AddressModel address){
		return StringUtil.concatWithDelimiters(new String[]{address.getCity(), address.getState()}, new String[]{", "});
	}
	
	public void setAction(String action){
		this.action = action;
	}

}