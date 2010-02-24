/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateAddressException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpInvalidPasswordException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.webapp.action.HttpContext;
import com.freshdirect.webapp.action.fdstore.RegistrationAction;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.util.AccountUtil;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RegistrationControllerTag extends AbstractControllerTag implements SessionName { //AddressName,

	private static Category LOGGER = LoggerFactory.getInstance(RegistrationControllerTag.class);

	private String fraudPage;
	private String statusChangePage;
	private boolean signupFromCheckout;
	private int registrationType;

	public void setFraudPage(String s) {
		this.fraudPage = s;
	}

	public void setStatusChangePage(String s) {
		this.statusChangePage = s;
	}

	public void setSignupFromCheckout(boolean b) {
		this.signupFromCheckout = b;
	}

	public void setRegistrationType(int registrationType) {
		this.registrationType = registrationType;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) {
		String actionName = this.getActionName();
		try {
			if ("register".equalsIgnoreCase(actionName)) {
				RegistrationAction ra = new RegistrationAction(this.registrationType);

				HttpContext ctx =
					new HttpContext(
						this.pageContext.getSession(),
						(HttpServletRequest) this.pageContext.getRequest(),
						(HttpServletResponse) this.pageContext.getResponse());

				ra.setHttpContext(ctx);
				ra.setResult(actionResult);
				ra.setFraudPage(this.fraudPage);
				ra.setSignupFromCheckout(this.signupFromCheckout);
				ra.setStatusChangePage(this.statusChangePage);
				ra.setSuccessPage(this.getSuccessPage());

				ra.execute();
				this.setSuccessPage(ra.getSuccessPage()); //reset if changed.

			} else if ("addDeliveryAddress".equalsIgnoreCase(actionName)) {
				this.performAddDeliveryAddress(request, actionResult);

			} else if ("editDeliveryAddress".equalsIgnoreCase(actionName)) {
				this.performEditDeliveryAddress(request, actionResult);

			} else if ("deleteDeliveryAddress".equalsIgnoreCase(actionName)) {
				this.performDeleteDeliveryAddress(request, actionResult);

			} else if ("changeUserID".equalsIgnoreCase(actionName)) {
				this.performChangeUserID(request, actionResult);

			} else if ("changePassword".equalsIgnoreCase(actionName)) {
				this.performChangePassword(request, actionResult);

			} else if ("changeContactInfo".equalsIgnoreCase(actionName)) {
				this.performChangeContactInfo(request, actionResult);
				
			} else if ("changeEmailPreference".equalsIgnoreCase(actionName)) {
				this.performChangeEmailPreference(request, actionResult);
			}

		} catch (Exception ex) {
			LOGGER.error("Error performing action " + actionName, ex);
			actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
		}

		return true;
	}

	protected void performDeleteDeliveryAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		String shipToAddressId = request.getParameter("deleteShipToAddressId");
		AddressUtil.deleteShipToAddress(getIdentity(), shipToAddressId, actionResult, request);
		//check that if this address had any outstanding reservations.
		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		FDReservation reservation = user.getReservation();
		if (reservation != null) {
			reservation = FDCustomerManager.validateReservation(user, reservation);
			user.setReservation(reservation);
			session.setAttribute(USER, user);
			if(reservation == null){
				session.setAttribute(REMOVED_RESERVATION, Boolean.TRUE);
			}
		}
	}



	// [segabor] refactored from performEditDeliveryAddress and performAddDeliveryAddess
	public static ErpAddressModel checkDeliveryAddressInForm(HttpServletRequest request, ActionResult actionResult, HttpSession session) throws FDResourceException {

		AddressForm addressForm = new AddressForm();
		addressForm.populateForm(request);
		addressForm.validateForm(actionResult);
		if (!actionResult.isSuccess())
			return null;

		AddressModel deliveryAddress = addressForm.getDeliveryAddress();
		deliveryAddress.setServiceType(addressForm.getDeliveryAddress().getServiceType());
		DeliveryAddressValidator validator = new DeliveryAddressValidator(deliveryAddress);
		
		if (!validator.validateAddress(actionResult)) {
			return  null;
		}
	
		AddressModel scrubbedAddress = validator.getScrubbedAddress(); // get 'normalized' address

		if (validator.isAddressDeliverable()) {
			FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
			if (user.isPickupOnly() && user.getOrderHistory().getValidOrderCount()==0) {
				//
				// now eligible for home/corporate delivery and still not placed an order.
				//
				user.setSelectedServiceType(scrubbedAddress.getServiceType());
				user.setZipCode(scrubbedAddress.getZipCode());
				FDCustomerManager.storeUser(user.getUser());
				session.setAttribute(USER, user);
			}else {
				//Already is a home or a corporate customer.
				if(user.getOrderHistory().getValidOrderCount()==0) {
					//check if customer has no order history.					
					user.setSelectedServiceType(scrubbedAddress.getServiceType());
					user.setZipCode(scrubbedAddress.getZipCode());
					FDCustomerManager.storeUser(user.getUser());
					session.setAttribute(USER, user);
				}
			}
		}
		
		ErpAddressModel erpAddress = addressForm.getErpAddress();
		erpAddress.setFrom(scrubbedAddress);
		erpAddress.setCity(scrubbedAddress.getCity());
		erpAddress.setAddressInfo(scrubbedAddress.getAddressInfo());				

		LOGGER.debug("ErpAddressModel:"+scrubbedAddress);

		if("SUFFOLK".equals(FDDeliveryManager.getInstance().getCounty(scrubbedAddress)) && erpAddress.getAltContactPhone() == null){
			actionResult.addError(true, EnumUserInfoName.DLV_ALT_CONTACT_PHONE.getCode(), SystemMessageList.MSG_REQUIRED);
			return null;
		}

		return erpAddress;
	}
	

	protected void performEditDeliveryAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);

		// call common delivery address check
		ErpAddressModel erpAddress = checkDeliveryAddressInForm(request, actionResult, session);
		if (erpAddress == null) {
			return;
		}
		
		
		String shipToAddressId = request.getParameter("updateShipToAddressId");
		boolean foundFraud = AddressUtil.updateShipToAddress(request, actionResult, user, shipToAddressId, erpAddress);
		if(foundFraud){
			/*session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
				SystemMessageList.MSG_NOT_UNIQUE_INFO,
				new Object[] {user.getCustomerServiceContact()}));*/
			this.applyFraudChange(user);
		}
		/*
		if(user.getOrderHistory().getValidOrderCount()==0)
		{
		  user.setZipCode(erpAddress.getZipCode());
		  user.setSelectedServiceType(erpAddress.getServiceType());
  		  //user.resetPricingContext();
		}
		*/
		FDReservation reservation = user.getReservation();
		if(reservation != null){
			reservation = FDCustomerManager.validateReservation(user, reservation);
			user.setReservation(reservation);
			session.setAttribute(USER, user);
			if(reservation == null){
				session.setAttribute(REMOVED_RESERVATION, Boolean.TRUE);
			}
		}
	}

	protected void performAddDeliveryAddress(HttpServletRequest request, ActionResult actionResult) throws FDResourceException {
		HttpSession session = (HttpSession) pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);

		// call common delivery address check
		ErpAddressModel erpAddress = checkDeliveryAddressInForm(request, actionResult, session);
		if (erpAddress == null) {
			return;
		}


		try {
			boolean foundFraud =
				FDCustomerManager.addShipToAddress(AccountActivityUtil.getActionInfo(session), !user.isDepotUser(), erpAddress);			
			if (foundFraud) {
//				session.setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
//					SystemMessageList.MSG_NOT_UNIQUE_INFO,
//					new Object[] {user.getCustomerServiceContact()}));
				this.applyFraudChange(user);
			}
			/*
			if(user.getOrderHistory().getValidOrderCount()==0)
			{
				user.setZipCode(erpAddress.getZipCode());
				user.setSelectedServiceType(erpAddress.getServiceType());
			}	
			*/

		} catch (ErpDuplicateAddressException ex) {
			LOGGER.warn(
				"AddressUtil:addShipToAddress(): ErpDuplicateAddressException caught while trying to add a shipping address to the customer info:",
				ex);
			actionResult.addError(
				new ActionError(
					"duplicate_user_address",
					"The information entered for this address matches an existing address in your account."));
		}
	}

	protected void performChangeUserID(HttpServletRequest request, ActionResult result) throws FDResourceException {
		ErpCustomerInfoModel cim = null;
		FDIdentity identity = getIdentity();
		String userId = request.getParameter(EnumUserInfoName.EMAIL.getCode());
		String repeatUserId = request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode());

		result.addError(
			(userId == null || userId.trim().length() < 1),
			EnumUserInfoName.EMAIL.getCode(),
			SystemMessageList.MSG_REQUIRED);

		if (!result.hasError(EnumUserInfoName.EMAIL.getCode()) && !com.freshdirect.mail.EmailUtil.isValidEmailAddress(userId)) {
			result.addError(new ActionError(EnumUserInfoName.EMAIL_FORMAT.getCode(), SystemMessageList.MSG_EMAIL_FORMAT));
		}

		if (repeatUserId == null || repeatUserId.length() < 1) {
			result.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED));
		} else if (!userId.equals(repeatUserId)) {
			result.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT));
		}

		if (result.isSuccess()) {
			try {
				//
				// Update UserId first since if new user id is a duplicate it will not perform the second change (of user email address)
				//
				FDCustomerManager.updateUserId(AccountActivityUtil.getActionInfo(pageContext.getSession()), userId);

				//
				// No errors updating UserId, so update the email address too
				//
				cim = FDCustomerFactory.getErpCustomerInfo(identity);
				cim.setEmail(userId);
				FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);

			} catch (ErpDuplicateUserIdException ex) {
				LOGGER.warn("New userId already exists in system", ex);
				result.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
			}
		}
	}

	protected void performChangeContactInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {
		String lastName = request.getParameter("last_name");
		String firstName = request.getParameter("first_name");
		String title = request.getParameter("title");
		String altEmail = request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode());
		String otherPhone = request.getParameter("other_phone");
		String homePhone = request.getParameter("homephone");
		String homePhoneExt = request.getParameter("ext");
		String otherPhoneExt = request.getParameter("other_ext");

		String busPhone = request.getParameter("busphone");
		String busPhoneExt = request.getParameter("busphoneext");
		String cellPhone = request.getParameter("cellphone");
		String cellPhoneExt = request.getParameter("cellphoneext");
		homePhone = homePhone != null && homePhone.trim().length() == 0 ? null : homePhone;
		busPhone = busPhone != null && busPhone.trim().length() == 0 ? null : busPhone;
		cellPhone = cellPhone != null && cellPhone.trim().length() == 0 ? null : cellPhone;

		String workDept = request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode());
		String employeeId = request.getParameter("employeeId");

		result.addError(
			lastName == null || lastName.trim().length() < 1,
			EnumUserInfoName.DLV_LAST_NAME.getCode(),
			SystemMessageList.MSG_REQUIRED);

		result.addError(
			altEmail != null && !"".equals(altEmail) && !EmailUtil.isValidEmailAddress(altEmail),
			EnumUserInfoName.ALT_EMAIL.getCode(),
			SystemMessageList.MSG_EMAIL_FORMAT);

		result.addError(
			firstName == null || firstName.trim().length() < 1,
			EnumUserInfoName.DLV_FIRST_NAME.getCode(),
			SystemMessageList.MSG_REQUIRED);

		FDUserI user = getUser();

		if (!user.isCorporateUser()) {
			result.addError(
				homePhone == null,
				EnumUserInfoName.DLV_HOME_PHONE.getCode(),
				SystemMessageList.MSG_REQUIRED);
		}

		if (user.isDepotUser()) {

			result.addError(
				busPhone == null,
				EnumUserInfoName.DLV_WORK_PHONE.getCode(),
				SystemMessageList.MSG_REQUIRED);

			result.addError(
				workDept == null || workDept.trim().length() < 1,
				EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode(),
				SystemMessageList.MSG_REQUIRED);

			com.freshdirect.delivery.depot.DlvDepotModel depot = FDDepotManager.getInstance().getDepot(user.getDepotCode());
			if (depot.getRequireEmployeeId()) {

				result.addError(
					employeeId == null || employeeId.trim().length() < 1,
					EnumUserInfoName.DLV_EMPLOYEE_ID.getCode(),
					SystemMessageList.MSG_REQUIRED);

			}

		}

		result.addError(
				homePhone != null && PhoneNumber.normalize(homePhone).length() != 10,
				EnumUserInfoName.DLV_HOME_PHONE.getCode(),
				SystemMessageList.MSG_PHONE_FORMAT);
		result.addError(
				busPhone != null && PhoneNumber.normalize(busPhone).length() != 10,
				EnumUserInfoName.DLV_WORK_PHONE.getCode(),
				SystemMessageList.MSG_PHONE_FORMAT);
		result.addError(
				cellPhone != null && PhoneNumber.normalize(cellPhone).length() != 10,
				EnumUserInfoName.DLV_CELL_PHONE.getCode(),
				SystemMessageList.MSG_PHONE_FORMAT);
		
		if (!result.isSuccess()) {
			return;
		}

		FDIdentity identity = getIdentity();
		ErpCustomerInfoModel cim = null;
		cim = FDCustomerFactory.getErpCustomerInfo(identity);
		cim.setFirstName(firstName);
		cim.setLastName(lastName);
		cim.setTitle(title);
		cim.setHomePhone(new PhoneNumber(homePhone, homePhoneExt));
		cim.setOtherPhone(new PhoneNumber(otherPhone, otherPhoneExt));
		cim.setAlternateEmail(altEmail);
		cim.setBusinessPhone(new PhoneNumber(busPhone, busPhoneExt));
		cim.setCellPhone(new PhoneNumber(cellPhone, cellPhoneExt));

		cim.setWorkDepartment(workDept);
		cim.setEmployeeId(employeeId);

		LOGGER.debug("Updating customer info");
		boolean foundFraud = FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
		LOGGER.debug("Customer info updated");
		/*if(foundFraud){
			pageContext.getSession().setAttribute(SessionName.SIGNUP_WARNING, MessageFormat.format(
				SystemMessageList.MSG_NOT_UNIQUE_INFO,
				new Object[] {user.getCustomerServiceContact()}));
			this.applyFraudChange(user);
		}*/
	}
	
	private void applyFraudChange(FDUserI user) throws FDResourceException{
		user.invalidateCache();
		if(user.isFraudulent()){
			user.updateUserState();
			PromotionI promo = user.getRedeemedPromotion();
			if(promo != null && !user.getPromotionEligibility().isEligible(promo.getPromotionCode())) {
				user.setRedeemedPromotion(null);
			}
		}
		pageContext.getSession().setAttribute(SessionName.USER, user);
	}
	
	protected void performChangeEmailPreference(HttpServletRequest request, ActionResult result) throws FDResourceException {

		boolean receiveNews = "yes".equalsIgnoreCase(request.getParameter("receive_mail"));
		boolean plainTextEmail = request.getParameter("isSendPlainTextEmail") != null;
		boolean sendOptinNewsletter = request.getParameter("isSendOptinNewsletter") != null;

		
		if (!result.isSuccess()) {
			return;
		}

		FDIdentity identity = getIdentity();
		ErpCustomerInfoModel cim = null;
		cim = FDCustomerFactory.getErpCustomerInfo(identity);
		
		cim.setReceiveNewsletter(receiveNews);
		if(receiveNews){
			cim.setUnsubscribeDate(null);
		} else if(cim.getUnsubscribeDate() == null){
			cim.setUnsubscribeDate(new java.util.Date());
		}
		
		cim.setEmailPlaintext(plainTextEmail);
		cim.setReceiveOptinNewsletter(sendOptinNewsletter);

		LOGGER.debug("Updating customer email preference");
		FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), cim);
		LOGGER.debug("Customer email preference updated");

	}

	protected void performChangePassword(HttpServletRequest request, ActionResult result) throws FDResourceException {
		FDIdentity identity = getIdentity();
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim();
		String confirmPassword = NVL.apply(request.getParameter("confirmPassword"), "").trim();

		AccountUtil.validatePassword(result, password, confirmPassword);

		if (!result.isSuccess()) {
			return;
		}

		try {
			ErpCustomerModel cm = FDCustomerFactory.getErpCustomer(identity);

			FDCustomerManager.changePassword(
				AccountActivityUtil.getActionInfo(request.getSession()),
				cm.getCustomerInfo().getEmail(),
				password);

		} catch (ErpInvalidPasswordException ex) {
			LOGGER.info("new password too short", ex);
			result.addError(new ActionError(EnumUserInfoName.PASSWORD.getCode(), SystemMessageList.MSG_PASSWORD_LENGTH));
		}

	}

	protected FDIdentity getIdentity() {
		HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
		return (user == null) ? null : user.getIdentity();
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}

	private FDUserI getUser() {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(USER);
		return user;
	}
}




