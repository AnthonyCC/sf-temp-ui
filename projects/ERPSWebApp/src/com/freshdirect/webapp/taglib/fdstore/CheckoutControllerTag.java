/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ActionWarning;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.HttpContextAware;
import com.freshdirect.webapp.action.ResultAware;
import com.freshdirect.webapp.action.fdstore.ChooseTimeslotAction;
import com.freshdirect.webapp.action.fdstore.SubmitOrderAction;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

/**
 * 
 * @version $Revision$
 * @author $Author$
 */
public class CheckoutControllerTag extends AbstractControllerTag {

	private static Category LOGGER = LoggerFactory
			.getInstance(CheckoutControllerTag.class);

	private String ccdProblemPage = "/checkout/step_3_choose.jsp";
	private String authCutoffPage = "/checkout/account_problem.jsp";
	private String ageVerificationPage = "/checkout/step_2_verify_age.jsp";
	private String noContactPhonePage = "/checkout/step_1_edit.jsp";
	private String backToViewCart = "/checkout/view_cart.jsp";
	private String ccdAddCardPage = "/checkout/step_3_card_add.jsp";
	private String gcFraudPage = "/gift_card/purchase/purchase_giftcard.jsp";
	private String gcAVSExceptionPage="/gift_card/purchase/purchase_giftcard.jsp";	
	
	public void setCcdProblemPage(String ccdProblemPage) {
		this.ccdProblemPage = ccdProblemPage;
	}

	public void setAuthCutoffPage(String authCutoffPage) {
		this.authCutoffPage = authCutoffPage;
	}

	public void setNoContactPhonePage(String noContactPhonePage) {
		this.noContactPhonePage = noContactPhonePage;
	}
	
	public void setGCFraudPage(String gcFraudPage) {
		this.gcFraudPage = gcFraudPage;
	}
	
	protected boolean performAction(HttpServletRequest request, ActionResult result) throws JspException {
		String action = this.getActionName();
		HttpSession session = request.getSession();
		boolean saveCart = false;
		
		try {
			if ("setDeliveryAddress".equalsIgnoreCase(action)) {
				performSetDeliveryAddress(request, result);
				if (result.isSuccess()) {
					UserValidationUtil.validateContainsDlvPassOnly(request, result);
					UserValidationUtil.validateOrderMinimum(request, result);
					FDSessionUser currentUser = (FDSessionUser) pageContext.getSession().getAttribute( SessionName.USER );
					boolean makeGoodOrder = request.getParameter("makeGoodOrder") != null;
					if(!makeGoodOrder) {
						//Set the selected gift carts for processing.
						currentUser.getShoppingCart().setSelectedGiftCards(currentUser.getGiftCardList().getSelectedGiftcards());
					}
				}
			} else if ("editAndSetDeliveryAddress".equalsIgnoreCase(action)) {
				performEditAndSetDeliveryAddress(request, result);
								
			} else if ("deleteDeliveryAddress".equalsIgnoreCase(action)) {
				performDeleteDeliveryAddress(request, result);
				
			} else if ("reserveDeliveryTimeSlot".equalsIgnoreCase(action)) {
				String outcome = performReserveDeliveryTimeSlot(request, result);
				if(outcome.equals(Action.NONE)){
					return false;
				}				

			} else if ("submitOrder".equalsIgnoreCase(action)) {
				LOGGER.debug("AVAILABILITY IS: " + getCart().getAvailability());
				if (!getCart().isAvailabilityChecked()) {
					
					this.setSuccessPage(backToViewCart);
					return true;
				}
				String outcome = performSubmitOrder(request, result);
				// add logic to process make good order complaint.
				if("true".equals(session.getAttribute("makeGoodOrder"))){
					ErpComplaintModel complaintModel = (ErpComplaintModel)session.getAttribute(SessionName.MAKEGOOD_COMPLAINT);
					String makeGoodOrderId = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
					complaintModel.setMakegood_sale_id(makeGoodOrderId);
					try{
						addMakeGoodComplaint(result, complaintModel);
					}catch(Exception e){
						LOGGER.error("Add 0 credit complaint failed for make good order:"+makeGoodOrderId, e);
						//create case here
					}
					session.removeAttribute("makeGoodOrder");
					session.removeAttribute("referencedOrder");
				}
				
				saveCart = true;
				if (outcome.equals( Action.NONE )) {
					return false;		// SKIP_BODY
				}
			} else if ("gc_submitGiftCardOrder".equalsIgnoreCase(action) ||("gc_submitGiftCardBulkOrder".equalsIgnoreCase(action)) || ("gc_onestep_submitGiftCardOrder".equalsIgnoreCase(action)) || "rh_submitDonationOrder".equalsIgnoreCase(action) || "rh_onestep_submitDonationOrder".equalsIgnoreCase(action)) {
				// allane:  added this b/c we skip confirm order page in gc checkout.  payment needs to be set at the same time as checkout.
				FDSessionUser currentUser = (FDSessionUser) pageContext.getSession().getAttribute( SessionName.USER );				
				if("gc_onestep_submitGiftCardOrder".equalsIgnoreCase(action)) {
					
					
					
					if(result.isSuccess()) { 
						performAddAndSetPaymentMethod(request, result);
						if(result.getError("payment_method_fraud") != null){
							currentUser.setGCSignupError(true);
							redirectTo(this.ccdProblemPage);
						}else if(result.getErrors()!=null && result.getErrors().size()>0){
							currentUser.setGCSignupError(true);
							List errList=new ArrayList();
							Collection col=result.getErrors();
							Iterator iterator=col.iterator();
							while(iterator.hasNext()){
								ActionError tmpResult=(ActionError)iterator.next();
								errList.add(tmpResult.getDescription());
							}
							currentUser.setOneTimeGCPaymentError(errList);
							redirectTo(this.ccdProblemPage);
						}
						
					}
				} else if ("rh_onestep_submitDonationOrder".equalsIgnoreCase(action)){
					if(result.isSuccess()) { 
						String optinInd = request.getParameter("optinInd");
						boolean optIn = false;
						if(null != optinInd && !"".equals(optinInd)){
							if(optinInd.equalsIgnoreCase("optin")){				
								optIn = true;
							}
						}else{
							result.addError(new ActionError("Opt_in_required", SystemMessageList.MSG_RH_OPTIN_REQUIRED));
//							return true;
						}
						performAddAndSetPaymentMethod(request, result);
						if(result.getError("payment_method_fraud") != null){
							currentUser.setGCSignupError(true);
							redirectTo(this.ccdProblemPage);
						}
					}
				}
				else {
					performSetPaymentMethod(request, result);
				}
				if(result.isSuccess()){
					String outcome = performSubmitOrder(request, result);				
					saveCart = true;
					
					if(result.getError("address_verification_failed") != null){
						//currentUser.setGCSignupError(true);
						redirectTo(this.gcAVSExceptionPage);
					}
					else if(result.getError("gc_payment_auth_failed") != null){
						//currentUser.setGCSignupError(true);
						redirectTo(this.gcAVSExceptionPage);
					}
					
					
					if (outcome.equals( Action.NONE )) {
						return false;		// SKIP_BODY
					} 	
				}
			} 					
			else if ("setPaymentMethod".equalsIgnoreCase(action)) {
				performSetPaymentMethod(request, result);
				if (result.isSuccess()) {
					UserValidationUtil.validateOrderMinimum(request, result);
					FDSessionUser currentUser = (FDSessionUser) pageContext.getSession().getAttribute( SessionName.USER );
					String app = (String) pageContext.getSession().getAttribute(SessionName.APPLICATION);
					if (currentUser.isPromotionAddressMismatch() && !"CALLCENTER".equals(app)) {
						this.setSuccessPage("/checkout/step_3_waive.jsp");
					}
				}

			} else if ("setNoPaymentMethod".equalsIgnoreCase(action)) {
				performSetNoPaymentMethod(request, result);
				if (result.isSuccess()) {
					UserValidationUtil.validateOrderMinimum(request, result);
					FDSessionUser currentUser = (FDSessionUser) pageContext.getSession().getAttribute( SessionName.USER );
					String app = (String) pageContext.getSession().getAttribute(SessionName.APPLICATION);
					if (currentUser.isPromotionAddressMismatch() && !"CALLCENTER".equals(app)) {
						this.setSuccessPage("/checkout/step_3_waive.jsp");
					}
				}

			}else if ("addAndSetPaymentMethod".equalsIgnoreCase(action)) {
				performAddAndSetPaymentMethod(request, result);
				if (result.isSuccess()) {
					UserValidationUtil.validateOrderMinimum(request, result);
				}
				
			} else if("addPaymentMethod".equalsIgnoreCase(action) || "gc_addPaymentMethod".equalsIgnoreCase(action)) {
				performAddPaymentMethod(request, result);
				
			} else if ("setPaymentAndSubmit".equalsIgnoreCase(action)) {
				if (UserValidationUtil.validateOrderMinimum(request, result)) {
					String outcome = performSetPaymentAndSubmit(request, result);
					saveCart = true;
					if (outcome.equals( Action.NONE )) {
						return false;		// SKIP_BODY
					}
				}

			} else if ("deletePaymentMethod".equalsIgnoreCase(action)) {
				performDeletePaymentMethod(request, result);
				if ((request.getRequestURI().toLowerCase().indexOf("gift_card")>-1)){
					this.setSuccessPage("/gift_card/purchase/purchase_giftcard.jsp");	
				}else if ((request.getRequestURI().toLowerCase().indexOf("robin_hood")>-1))  {
					this.setSuccessPage("/robin_hood/rh_submit_order.jsp");
				}
				
				
			} else if ("setDeliveryAddressAndPayment".equalsIgnoreCase(action)) {
				performSetDeliveryAddressAndPayment(request, result);
				if (result.isSuccess()) {
					UserValidationUtil.validateOrderMinimum(request, result);
				}

			}
			
			// if there is alcohol in the cart and the age verification has not been set then send to age verification page
			String app = (String) pageContext.getSession().getAttribute(SessionName.APPLICATION);
			if(	action!=null && action.toLowerCase().indexOf("submit")==-1 && isAgeVerificationNeeded(app,request)) { 
				this.setSuccessPage(this.ageVerificationPage);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Error performing action "+action, ex);
			result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		} finally {
			if (saveCart) {
				//
				// checkpoint: force a save cart
				//
				FDSessionUser theUser = (FDSessionUser) pageContext.getSession().getAttribute( SessionName.USER );
				theUser.saveCart(true);
			}
		}

		return true;
	}

	protected boolean performGetAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		// if there is alcohol in the cart and the age verification has not been
		// set then send to age verification page
		try {
			String app = (String) pageContext.getSession().getAttribute(
					SessionName.APPLICATION);
			if (isAgeVerificationNeeded(app, request)) {
				redirectTo(ageVerificationPage);
				return false;
			}
		} catch (Exception ex) {
			LOGGER.error("Error checking for age verification condition", ex);
			actionResult.addError(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		return true;

	}

	protected String performSetPaymentAndSubmit(HttpServletRequest request,
			ActionResult result) throws Exception {
		setPaymentMethod(request, result);
		if (result.isSuccess()) {
			return performSubmitOrder(request, result);
		}
		return Action.ERROR;
	}

	protected void performDeleteDeliveryAddress(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		String shipToAddressId = request.getParameter("deleteShipToAddressId");
		if (shipToAddressId == null) {
			shipToAddressId = (String) request
					.getAttribute("deleteShipToAddressId");
		}

		AddressUtil.deleteShipToAddress(getIdentity(), shipToAddressId, result,
				request);
	}

	protected String performReserveDeliveryTimeSlot(HttpServletRequest request,
			ActionResult result) throws Exception {
		setPhoneCharge(request, result);
		return setAndReserveDeliveryTimeSlot(request, result);
	}

	protected void performSetPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		setPaymentMethod(request, result);
		if (result.isSuccess()) {
			applyCustomerCredits();
		}
	}

	protected void performSetNoPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		setNoPaymentMethod(request, result);
		if (result.isSuccess()) {
			applyCustomerCredits();
		}
	}
	
	protected void performAddAndSetPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		//
		// add the payment method
		//
		FDIdentity identity = getIdentity();

		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(
				request, result, identity);
		if (result.isSuccess()) {
			PaymentMethodUtil.validatePaymentMethod(request, paymentMethod,
					result, getUser());
			if (EnumPaymentMethodType.ECHECK.equals(paymentMethod
					.getPaymentMethodType())) {
				String terms = request.getParameter(PaymentMethodName.TERMS);
				result
						.addError(terms == null || terms.length() <= 0,
								PaymentMethodName.TERMS,
								SystemMessageList.MSG_REQUIRED);
				if (result.isSuccess()
						&& !PaymentMethodUtil.hasECheckAccount(getUser()
								.getIdentity())) {
					paymentMethod.setIsTermsAccepted(true);
				}
			}
			if (result.isSuccess() && identity != null) {
				PaymentMethodUtil.addPaymentMethod(request, result,
						paymentMethod);
			}
		}
		if(identity == null){
			result.addError(new ActionError("unexpected_error", "User Identity cannot be Null"));
			return;
		}
		//
		// return the ID of the payment method (should only be one)
		//
		List payMethods = FDCustomerFactory.getErpCustomer(identity)
				.getPaymentMethods();
		ListIterator listIt = payMethods.listIterator();
		String paymentId = null;
		while (listIt.hasNext()) {
			ErpPaymentMethodI payment = (ErpPaymentMethodI) listIt.next();
			paymentId = ((ErpPaymentMethodModel) payment).getPK().getId();
		}

		setPaymentMethod(request, result, paymentId, request
				.getParameter("billingRef"), false, "");
		if (result.isSuccess()) {
			applyCustomerCredits();
		}

	}

	protected void performAddPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(
				request, result, getIdentity());

		if (result.isSuccess()) {
			PaymentMethodUtil.validatePaymentMethod(request, paymentMethod,
					result, getUser());
			if (EnumPaymentMethodType.ECHECK.equals(paymentMethod
					.getPaymentMethodType())) {
				String terms = request.getParameter(PaymentMethodName.TERMS);
				result
						.addError(terms == null || terms.length() <= 0,
								PaymentMethodName.TERMS,
								SystemMessageList.MSG_REQUIRED);
				if (result.isSuccess()
						&& !PaymentMethodUtil.hasECheckAccount(getUser()
								.getIdentity())) {
					paymentMethod.setIsTermsAccepted(true);
				}
			}
			if (result.isSuccess()) {
				PaymentMethodUtil.addPaymentMethod(request, result,
						paymentMethod);
			}
		}

	}

	protected void performDeletePaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		String paymentId = request.getParameter("deletePaymentId");
		if (paymentId == null || paymentId.length() <= 0) {
			throw new FDResourceException("deletePaymentId not specified");
		}
		PaymentMethodUtil.deletePaymentMethod(request, result, paymentId);
	}

	protected void performSetDeliveryAddressAndPayment(
			HttpServletRequest request, ActionResult result)
			throws FDResourceException, JspException {
		this.performSetDeliveryAddress(request, result);
		if (result.isSuccess()) {
			setPaymentMethod(request, result);
			if (result.isSuccess()) {
				applyCustomerCredits();
			}
		}
	}

	protected void performEditAndSetDeliveryAddress(HttpServletRequest request,
			ActionResult result) throws FDResourceException {

		String addressId = request.getParameter("updateShipToAddressId");

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		addressForm.validateForm(result);
		if (!result.isSuccess())
			return;

		AddressModel deliveryAddress = addressForm.getDeliveryAddress();
		DeliveryAddressValidator dav = new DeliveryAddressValidator(
				deliveryAddress);
		if (!dav.validateAddress(result)) {
			return;
		}
		AddressModel dlvAddress = dav.getScrubbedAddress();

		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, 7);
		DlvZoneInfoModel zoneInfo = AddressUtil.getZoneInfo(request,
				dlvAddress, result, date.getTime());
		if (!result.isSuccess())
			return;

		ErpAddressModel erpAddress = addressForm.getErpAddress();
		erpAddress.setFrom(dlvAddress);
		erpAddress.setAddressInfo(dlvAddress.getAddressInfo());

		if ("SUFFOLK".equals(FDDeliveryManager.getInstance().getCounty(
				dlvAddress))
				&& erpAddress.getAltContactPhone() == null) {
			result.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE
					.getCode(), SystemMessageList.MSG_REQUIRED);
			return;
		}

		HttpSession session = (HttpSession) pageContext.getSession();
		boolean foundFraud = AddressUtil.updateShipToAddress(request, result,
				this.getUser(), addressId, erpAddress);

		if (!result.isSuccess())
			return;

		ErpAddressModel thisAddress = FDCustomerManager.getShipToAddress(
				getIdentity(), addressId);

		String zoneId = zoneInfo.getZoneCode();
		if (zoneId != null && zoneId.length() > 0) {
			LOGGER
					.debug("success! adding address to cart & setting defaultShipToAddress.");
			FDCustomerManager.setDefaultShipToAddressPK(getIdentity(),
					((ErpAddressModel) thisAddress).getPK().getId());
			FDCartModel cart = getCart();
			cart.setZoneInfo(zoneInfo);
			cart.setDeliveryAddress(thisAddress);
			setCart(cart);
		}

		FDSessionUser user = (FDSessionUser) session
				.getAttribute(SessionName.USER);
		if (foundFraud) {
			user.invalidateCache();
		}
		user.updateUserState();
		if (user.isFraudulent()) {
			PromotionI promo = user.getRedeemedPromotion();
			if (promo != null
					&& !user.getPromotionEligibility().isEligible(
							promo.getPromotionCode())) {
				user.setRedeemedPromotion(null);
			}
		}
		session.setAttribute(SessionName.USER, user);
	}

	protected void performSetDeliveryAddress(HttpServletRequest request,
			ActionResult result) throws FDResourceException, JspException {
		String addressOrLocation = request.getParameter("selectAddressList");

		if (addressOrLocation != null && addressOrLocation.startsWith("field_")) {
			addressOrLocation = request.getParameter(addressOrLocation
					.substring("field_".length()));
		}

		if (addressOrLocation == null || addressOrLocation.length() < 1) {
			result.addError(new ActionError("address",
					"Please select a delivery address."));
			return;
		}

		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session
				.getAttribute(SessionName.USER);
		if (addressOrLocation.startsWith("DEPOT_")) {
			String locationId = addressOrLocation.substring("DEPOT_".length());
			this.setDepotDeliveryLocation(locationId, request, result);
		} else {
			this.setRegularDeliveryAddress(user, result, addressOrLocation,
					request);
		}
		FDCartModel cart = user.getShoppingCart();
		if (user.getSelectedServiceType() == EnumServiceType.HOME
				&& (user.isDlvPassActive() || cart.getDeliveryPassCount() > 0)
				&& !(cart.isDlvPromotionApplied())) {
			cart.setDlvPassApplied(true);
		}

		user.updateUserState();
		session.setAttribute(SessionName.USER, user);
	}

	private void setRegularDeliveryAddress(FDUserI user, ActionResult result,
			String addressPK, HttpServletRequest request)
			throws FDResourceException, JspException {
		FDIdentity identity = user.getIdentity();

		// locate the shipto address with the specified PK
		ErpAddressModel shippingAddress = FDCustomerManager.getShipToAddress(
				identity, addressPK);

		if (shippingAddress == null) {
			throw new FDResourceException("Specified address doesn't exist");
		}

		AddressModel address = AddressUtil
				.scrubAddress(shippingAddress, result);
		// if it is a Hamptons address without the altContactNumber have user
		// edit and provide it.
		if ("SUFFOLK"
				.equals(FDDeliveryManager.getInstance().getCounty(address))
				&& shippingAddress.getAltContactPhone() == null) {
			result.addError(true, "missingContactPhone",
					SystemMessageList.MSG_REQUIRED);
			this.redirectTo(this.noContactPhonePage + "?addressId=" + addressPK
					+ "&missingContactPhone=true");
			return;
		}
		EnumRestrictedAddressReason reason = FDDeliveryManager.getInstance()
				.checkAddressForRestrictions(address);
		if (!EnumRestrictedAddressReason.NONE.equals(reason)) {
			result.addError(true, "undeliverableAddress",
					SystemMessageList.MSG_RESTRICTED_ADDRESS);
		}
		if (!result.isSuccess())
			return;

		// since address looks alright need geocode
		try {
			DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager
					.getInstance().geocodeAddress(address);
			String geocodeResult = geocodeResponse.getResult();

			if (!"GEOCODE_OK".equalsIgnoreCase(geocodeResult)) {
				//
				// since geocoding is not happening silently ignore it
				LOGGER.warn("GEOCODE FAILED FOR ADDRESS :" + address);
				// actionResult.addError(true,
				// EnumUserInfoName.DLV_ADDRESS_1.getCode(),
				// SystemMessageList.MSG_INVALID_ADDRESS);

			} else {
				LOGGER
						.debug("setRegularDeliveryAddress : geocodeResponse.getAddress() :"
								+ geocodeResponse.getAddress());
				address = geocodeResponse.getAddress();
			}

		} catch (FDInvalidAddressException iae) {
			LOGGER
					.warn("GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :"
							+ address + "EXCEPTION :" + iae);
			// actionResult.addError(true,
			// EnumUserInfoName.DLV_ADDRESS_1.getCode(),
			// SystemMessageList.MSG_INVALID_ADDRESS);
		}

		int validCount = this.getUser().getOrderHistory().getValidOrderCount();
		if (validCount < 1) {

			String specialInstructions = (request
					.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS
							.getCode()));
			String altDelivery = request
					.getParameter(EnumUserInfoName.DLV_ALTERNATE_DELIVERY
							.getCode());

			if (specialInstructions != null) {
				shippingAddress
						.setInstructions(specialInstructions.replaceAll(
								FDStoreProperties
										.getDlvInstructionsSpecialChar(), " "));
			}
			if (altDelivery != null) {
				shippingAddress.setAltDelivery(EnumDeliverySetting.DOORMAN);
			}
		}

		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, 7);

		DlvZoneInfoModel dlvResponse = AddressUtil.getZoneInfo(request,
				address, result, date.getTime());
		if (!result.isSuccess())
			return;
		AddressInfo info = address.getAddressInfo();
		if (info == null) {
			info = new AddressInfo();
		}
		info.setZoneId(dlvResponse.getZoneId());
		info.setZoneCode(dlvResponse.getZoneCode());
		address.setAddressInfo(info);

		//
		// set the scrubbed address on the erpAddress
		//
		shippingAddress.setAddressInfo(address.getAddressInfo());

		// check unattended delivery at this time
		//
		// if the user opted out or has not seen the unattended delivery notice,
		// simply ignore
		// if he opted in, check if the unattended delivery is available for the
		// zone.
		// if not, set the flag to DO_NOT_USE. This will ensure that Unattended
		// Delivery
		// instructions are not written on to SAP
		if (EnumUnattendedDeliveryFlag.OPT_IN.equals(shippingAddress
				.getUnattendedDeliveryFlag())) {
			// TODO IMPORTANT: this checks date for today + 7
			if (!dlvResponse.isUnattended()) {
				shippingAddress
						.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.DISCARD_OPT_IN);
				LOGGER
						.debug("Overriding user preferences for unattended delivery: zone is not eligible for requested time");
			} else {
				LOGGER
						.debug("Keeping user unattended delivery instructions: "
								+ (shippingAddress
										.getUnattendedDeliveryInstructions() == null ? "OK"
										: shippingAddress
												.getUnattendedDeliveryInstructions()));

			}
		} else if ("true".equals(request
				.getParameter(EnumUserInfoName.DLV_UNATTENDED_CONSENT_SEEN
						.getCode()))) {

			if (dlvResponse.isUnattended()) {

				if ("OPT_IN"
						.equals(request
								.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT
										.getCode()))) {
					shippingAddress
							.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.OPT_IN);
					shippingAddress
							.setUnattendedDeliveryInstructions(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_OPT
									.getCode());
					String unattendedInstructions = request
							.getParameter(EnumUserInfoName.DLV_UNATTENDED_DELIVERY_INSTRUCTIONS
									.getCode());
					if ("".equals(unattendedInstructions))
						unattendedInstructions = null;
					shippingAddress
							.setUnattendedDeliveryInstructions(unattendedInstructions);
				} else {
					shippingAddress
							.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.OPT_OUT);
					shippingAddress.setUnattendedDeliveryInstructions(null);
				}
				AddressUtil.updateShipToAddress(request, result,
						this.getUser(), shippingAddress.getPK().getId(),
						shippingAddress);

			}

		}

		FDCartModel cart = getCart();
		cart.setZoneInfo(dlvResponse);
		cart.setDeliveryAddress(shippingAddress);
		cart.setChargeWaived(EnumChargeType.DELIVERY, false, null);
		user.setSelectedServiceType(address.getServiceType());
		setCart(cart);

		FDCustomerManager.setDefaultShipToAddressPK(identity, shippingAddress
				.getPK().getId());
		FDCustomerManager.setDefaultDepotLocationPK(identity, null);
	}

	private void setDepotDeliveryLocation(String locationId,
			HttpServletRequest request, ActionResult result)
			throws FDResourceException {
		FDIdentity identity = getIdentity();

		LOGGER.debug("Setting depot delivery location ");
		DlvDepotModel depot = FDDepotManager.getInstance()
				.getDepotByLocationId(locationId);

		DlvLocationModel location = depot.getLocation(locationId);

		if (location == null) {
			throw new FDResourceException("Specified location doesn't exist");
		}
		PhoneNumber contactPhone = null;
		if (depot.isPickup()) {
			/*
			 * [segabor] DEAD CODE ALERT: contactNumber never gets non-null
			 * value
			 */
			String contactNumber = request.getParameter("contact_phone_"
					+ locationId);
			LOGGER.debug("setDepotDeliveryLocation(): contactNumber="
					+ contactNumber);

			if (contactNumber != null && !"".equals(contactNumber)) {
				contactPhone = new PhoneNumber(contactNumber);

				ErpCustomerInfoModel infoModel = FDCustomerFactory
						.getErpCustomerInfo(identity);
				if (!contactPhone.equals(infoModel.getOtherPhone())) {
					infoModel.setOtherPhone(contactPhone);
					FDCustomerManager
							.updateCustomerInfo(AccountActivityUtil
									.getActionInfo(pageContext.getSession()),
									infoModel);
				}
			}
		}

		if (depot != null) {

			// since address need geocode

			AddressModel addrModel = location.getAddress();
			try {
				DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager
						.getInstance().geocodeAddress(addrModel);
				String geocodeResult = geocodeResponse.getResult();

				if (!"GEOCODE_OK".equalsIgnoreCase(geocodeResult)) {
					//
					// since geocoding is not happening silently ignore it
					LOGGER
							.warn("GEOCODE FAILED FOR ADDRESS in setDepotDeliveryLocation :"
									+ addrModel);
					// actionResult.addError(true,
					// EnumUserInfoName.DLV_ADDRESS_1.getCode(),
					// SystemMessageList.MSG_INVALID_ADDRESS);

				} else {
					LOGGER
							.debug("setDepotDeliveryLocation : geocodeResponse.getAddress() :"
									+ geocodeResponse.getAddress());
					addrModel = geocodeResponse.getAddress();
				}

			} catch (FDInvalidAddressException iae) {
				LOGGER
						.warn("GEOCODE FAILED FOR ADDRESS setRegularDeliveryAddress  FDInvalidAddressException :"
								+ addrModel + "EXCEPTION :" + iae);
				// actionResult.addError(true,
				// EnumUserInfoName.DLV_ADDRESS_1.getCode(),
				// SystemMessageList.MSG_INVALID_ADDRESS);
			}

			FDCartModel cart = getCart();
			ErpDepotAddressModel address = new ErpDepotAddressModel(addrModel);
			address.setRegionId(depot.getRegionId());
			address.setZoneCode(location.getZoneCode());
			address.setLocationId(location.getPK().getId());
			address.setFacility(location.getFacility());
			if (this.getUser().isCorporateUser()) {
				String instructions = NVL.apply(request
						.getParameter("corpDlvInstructions"), "");
				address.setInstructions(instructions);
			} else {
				address.setInstructions(location.getInstructions());
			}
			address.setPickup(depot.isPickup());
			address.setDeliveryChargeWaived(location.getDeliveryChargeWaived());

			ErpCustomerModel erpCustomer = FDCustomerFactory
					.getErpCustomer(getIdentity().getErpCustomerPK());
			address.setFirstName(erpCustomer.getCustomerInfo().getFirstName());
			address.setLastName(erpCustomer.getCustomerInfo().getLastName());
			if (contactPhone != null) {
				address.setPhone(contactPhone);
			} else {
				address.setPhone(erpCustomer.getCustomerInfo()
						.getBusinessPhone());
			}

			// get the real zoneInfo object from deliveryManager
			DlvZoneInfoModel zoneInfo = FDDeliveryManager.getInstance()
					.getZoneInfoForDepot(depot.getRegionId(),
							location.getZoneCode(), new Date());

			cart.setZoneInfo(zoneInfo);
			cart.setDeliveryAddress(address);
			FDUserI user = this.getUser();
			user
					.setSelectedServiceType(address.isPickup() ? EnumServiceType.PICKUP
							: EnumServiceType.DEPOT);
			user.setShoppingCart(cart);
			pageContext.getSession().setAttribute(SessionName.USER, user);

			FDCustomerManager.setDefaultDepotLocationPK(identity, locationId);
			FDCustomerManager.setDefaultShipToAddressPK(identity, null);
		}
	}

	protected void configureAction(Action action, ActionResult result) {
		if (action instanceof HttpContextAware) {
			HttpContext ctx = new HttpContext(this.pageContext.getSession(),
					(HttpServletRequest) this.pageContext.getRequest(),
					(HttpServletResponse) this.pageContext.getResponse());

			((HttpContextAware) action).setHttpContext(ctx);
		}

		if (action instanceof ResultAware) {
			((ResultAware) action).setResult(result);
		}
	}

	protected String performSubmitOrder(HttpServletRequest request,
			ActionResult result) throws Exception {

		SubmitOrderAction soa = new SubmitOrderAction();
		this.configureAction(soa, result);
		soa.setAuthCutoffPage(authCutoffPage);
		soa.setCcdProblemPage(ccdProblemPage);
		soa.setCcdAddCardPage(ccdAddCardPage);
		soa.setGCFraudPage(gcFraudPage);
		
		if (this.getActionName().equals("gc_submitGiftCardOrder")||(this.getActionName().equals("gc_submitGiftCardBulkOrder"))) {
			if(this.getActionName().equals("gc_submitGiftCardBulkOrder")){
				return soa.gcExecute(false, true);
			}else{
				return soa.gcExecute(false, false);
			}
		}else if( this.getActionName().equals("gc_onestep_submitGiftCardOrder")){
			return soa.gcExecute(true, false);
		}else if(this.getActionName().equals("rh_submitDonationOrder") || this.getActionName().equals("rh_onestep_submitDonationOrder")){
			return soa.donationOrderExecute();
		}
		return soa.execute();
	}

	private String setAndReserveDeliveryTimeSlot(HttpServletRequest request,
			ActionResult result) throws Exception {
		ChooseTimeslotAction cta = new ChooseTimeslotAction();
		this.configureAction(cta, result);
		return cta.execute();
	}

	private void setPhoneCharge(HttpServletRequest request, ActionResult result)
			throws FDResourceException {
		HttpSession session = request.getSession();
		String app = (String) session.getAttribute(SessionName.APPLICATION);
		if (!"CALLCENTER".equalsIgnoreCase(app)) {
			return;
		}
		// Changes as part of PERF-22 task.
		// BEGIN
		/*
		 * int phoneOrders = 0;
		 * 
		 * FDIdentity identity = getIdentity(); Collection orderInfos =
		 * FDCustomerManager.getOrderHistoryInfo(identity).getFDOrderInfos();
		 * for (Iterator it = orderInfos.iterator(); it.hasNext(); ) {
		 * FDOrderInfoI orderInfo = (FDOrderInfoI) it.next(); if
		 * (orderInfo.getOrderSource
		 * ().equals(EnumTransactionSource.CUSTOMER_REP)) { phoneOrders++; } }
		 */
		int phoneOrders = getUser().getOrderHistory().getPhoneOrderCount();
		// END
		if (phoneOrders >= 3) {
			LOGGER.debug("setting phone handling charge of $"
					+ ErpServicesProperties.getPhoneHandlingFee());
			FDCartModel cart = getCart();
			cart.setChargeAmount(EnumChargeType.PHONE, Double
					.parseDouble(ErpServicesProperties.getPhoneHandlingFee()));
		}

	}

	private void setPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		String paymentId = request.getParameter("paymentMethodList");
		String billingRef = request.getParameter("billingRef");
		boolean makeGoodOrder = false;
		String referencedOrder = "";
		String app = (String) pageContext.getSession().getAttribute(
				SessionName.APPLICATION);
		if ("CALLCENTER".equalsIgnoreCase(app)) {
			makeGoodOrder = request.getParameter("makeGoodOrder") != null;
			referencedOrder = NVL.apply(
					request.getParameter("referencedOrder"), "").trim();
			if (makeGoodOrder && "".equals(referencedOrder)) {
				result
						.addError(true, "referencedOrder",
								"Reference Order number is required for a make good order");
				return;
			}
		}
		this.setPaymentMethod(request, result, paymentId, billingRef,
				makeGoodOrder, referencedOrder);
	}

	private void setNoPaymentMethod(HttpServletRequest request,
			ActionResult result) throws FDResourceException {
		String paymentId = request.getParameter("paymentMethodList");
		String billingRef = request.getParameter("billingRef");
		boolean makeGoodOrder = false;
		String referencedOrder = "";
		String app = (String) pageContext.getSession().getAttribute(
				SessionName.APPLICATION);
		if ("CALLCENTER".equalsIgnoreCase(app)) {
			makeGoodOrder = request.getParameter("makeGoodOrder") != null;
			referencedOrder = NVL.apply(
					request.getParameter("referencedOrder"), "").trim();
			if (makeGoodOrder && "".equals(referencedOrder)) {
				result
						.addError(true, "referencedOrder",
								"Reference Order number is required for a make good order");
				return;
			}
		}

		FDIdentity identity = getIdentity();
		ErpPaymentMethodI paymentMethod = PaymentMethodUtil.createGiftCardPaymentMethod(this.getUser());

		FDCartModel cart = getCart();
		paymentMethod.setBillingRef(billingRef);
		paymentMethod.setPaymentType(makeGoodOrder ? EnumPaymentType.MAKE_GOOD
				: EnumPaymentType.REGULAR);
		paymentMethod.setReferencedOrder(referencedOrder);
		cart.setPaymentMethod(paymentMethod);
		setCart(cart);
		this.getUser().setPostPromoConflictEnabled(true);
		this.getUser().updateUserState();
		//session.setAttribute(SessionName.USER, this.getUser());	
	}

	
	private boolean isAgeVerificationNeeded(String app,
			HttpServletRequest request) throws FDResourceException {
		FDCartModel cart = getCart();
		return (cart.containsAlcohol() && !"CALLCENTER".equalsIgnoreCase(app)
				&& !cart.isAgeVerified() && request.getRequestURI().indexOf(
				"/step_1") == -1);
	}

	private void setPaymentMethod(HttpServletRequest request,
			ActionResult result, String paymentId, String billingRef,
			boolean makeGoodOrder, String referencedOrder)
			throws FDResourceException {
		//
		// check for a valid payment ID
		//
		if ((paymentId == null) || ("".equals(paymentId))) {
			result.addError(new ActionError("paymentMethodList",
					"You must select a payment method."));
			return;
		}

		FDIdentity identity = getIdentity();

		//
		// search for the payment method with the matching ID
		//
		Collection paymentMethods = FDCustomerManager
				.getPaymentMethods(identity);
		ErpPaymentMethodI paymentMethod = null;

		for (Iterator iterator = paymentMethods.iterator(); iterator.hasNext();) {
			ErpPaymentMethodI item = (ErpPaymentMethodI) iterator.next();
			if (((ErpPaymentMethodModel) item).getPK().getId()
					.equals(paymentId)) {
				paymentMethod = item;
				break;
			}
		}

		if (paymentMethod == null) {
			result.addError(new ActionError("paymentMethodList",
					SystemMessageList.MSG_REQUIRED));
			return;
		}

		//
		// set payment in cart and store cart if valid payment found
		//
		PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, result,
				getUser());

		FDCartModel cart = getCart();
		paymentMethod.setBillingRef(billingRef);
		paymentMethod.setPaymentType(makeGoodOrder ? EnumPaymentType.MAKE_GOOD
				: EnumPaymentType.REGULAR);
		paymentMethod.setReferencedOrder(referencedOrder);
		cart.setPaymentMethod(paymentMethod);
		setCart(cart);

		HttpSession session = (HttpSession) pageContext.getSession();
		FDUserI user = getUser();

		//
		// set default payment method and check for unique billing address, if
		// required
		//
		FDActionInfo info = AccountActivityUtil.getActionInfo(session);
		FDCustomerManager.setDefaultPaymentMethod(info,
				((ErpPaymentMethodModel) paymentMethod).getPK());

		if (user.isDepotUser()) {
			if (user.isEligibleForSignupPromotion()) {
				if (FDCustomerManager.checkBillToAddressFraud(info,
						paymentMethod)) {

					session.setAttribute(SessionName.SIGNUP_WARNING,
							MessageFormat.format(
									SystemMessageList.MSG_NOT_UNIQUE_INFO,
									new Object[] { user
											.getCustomerServiceContact() }));

				}
			}
		}

		FDSessionUser currentUser = (FDSessionUser) pageContext.getSession()
				.getAttribute(SessionName.USER);
		currentUser.setPostPromoConflictEnabled(true);
		currentUser.updateUserState();
		session.setAttribute(SessionName.USER, currentUser);

	}

	private FDIdentity getIdentity() {
		return this.getUser().getIdentity();
	}

	private void setCart(FDCartModel cart) {
		FDUserI user = this.getUser();
		if (this.getActionName().indexOf("gc_") != -1) {
			user.setGiftCart(cart);
		} else if(this.getActionName().indexOf("rh_") != -1){
			user.setDonationCart(cart);
		}else {
			user.setShoppingCart(cart);
		}
		pageContext.getSession().setAttribute(SessionName.USER, user);
	}

	private FDCartModel getCart() {
		if (this.getActionName().indexOf("gc_") != -1) {
			return this.getUser().getGiftCart();
		}else if(this.getActionName().indexOf("rh_") != -1) {
			return this.getUser().getDonationCart();
		}
		return this.getUser().getShoppingCart();
	}

	private FDUserI getUser() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		return user;
	}

	private void applyCustomerCredits() throws FDResourceException {
		if(this.getActionName().equalsIgnoreCase("rh_onestep_submitDonationOrder")||this.getActionName().equalsIgnoreCase("rh_submitDonationOrder")){//Store credits should not be applied for Robin Hood.
			return;
		}
		FDIdentity identity = getIdentity();
		FDCartModel cart = getCart();
		FDCustomerCreditUtil.applyCustomerCredit(cart, identity);
		setCart(cart);
	}

	private void addMakeGoodComplaint(ActionResult result,
			ErpComplaintModel complaintModel) throws FDResourceException,
			ErpComplaintException {

		NumberFormat currencyFormatter = java.text.NumberFormat
				.getCurrencyInstance(Locale.US);

		LOGGER.debug("Creating credits for the following departments:");
		LOGGER.debug("  Method\t\tDepartment\t\t\tAmount\t\t\tReason");
		LOGGER.debug("  ------\t\t----------\t\t\t------\t\t\t------");
		List lines = complaintModel.getComplaintLines();
		for (Iterator it = lines.iterator(); it.hasNext();) {
			ErpComplaintLineModel line = (ErpComplaintLineModel) it.next();
			LOGGER.debug(line.getMethod().getStatusCode() + "\t\t"
					+ line.getDepartmentCode() + "\t\t\t"
					+ currencyFormatter.format(line.getAmount()) + "\t\t\t"
					+ line.getReason().getReason());
		}
		LOGGER.debug("  Credit Notes: " + complaintModel.getDescription());
		HttpSession session = pageContext.getSession();
		FDIdentity identity = ((FDUserI) session.getAttribute(SessionName.USER))
				.getIdentity();
		String orderId = (String) session.getAttribute("referencedOrder");
		FDCustomerManager.addComplaint(complaintModel, orderId, identity);
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

}