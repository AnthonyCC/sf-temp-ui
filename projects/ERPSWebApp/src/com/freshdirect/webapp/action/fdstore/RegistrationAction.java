package com.freshdirect.webapp.action.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyFactory;
import com.freshdirect.fdstore.survey.FDSurveyQuestion;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.fdstore.survey.SurveyKey;
import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.AccountUtil;

public class RegistrationAction extends WebActionSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RegistrationAction.class);

	private static boolean ALLOW_ALL = false;
	private final int regType;
	private String statusChangePage;
	private boolean signupFromCheckout;
	private String successPage;
	private String fraudPage;
	boolean restrictedAddress = false;

	public RegistrationAction(int regType) {
		this.regType = regType;
	}

	public void setStatusChangePage(String statusChangePage) {
		this.statusChangePage = statusChangePage;
	}

	public void setSignupFromCheckout(boolean signupFromCheckout) {
		this.signupFromCheckout = signupFromCheckout;
	}

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public String getSuccessPage() {
		return this.successPage;
	}

	public void setFraudPage(String fraudPage) {
		this.fraudPage = fraudPage;
	}

	public String execute() throws Exception {
	    //ALLOW_ALL = true;
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		boolean startedAsHomeUser = user.isHomeUser();
		boolean startedAsPickupOnly = user.isPickupOnly();
		boolean startedPromoEligible = user.isEligibleForSignupPromotion();

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);
		AddressInfo addInfo = new AddressInfo(request);

		EnumServiceType serviceType = addInfo.getAddressType();
		aInfo.validate(actionResult);
		addInfo.validate(actionResult);
		cInfo.validate(actionResult, serviceType);

		if (!actionResult.isSuccess() /*&& !ALLOW_ALL*/) {
			return ERROR;
		}

		//
		// Scrub the delivery address contained within the request
		// we'll bail out here if the
		//
		
		// VALIDATE DELIVERY ADDRESS
		// NOTE: don't be strict in USPS service checking if user is pickup or depot
		AddressModel dlvAddress = addInfo.getDlvAddress();
		dlvAddress.setServiceType(serviceType);
		DeliveryAddressValidator validator = new DeliveryAddressValidator(dlvAddress, user.isHomeUser() || user.isCorporateUser());
		
		boolean addressValid = validator.validateAddress(actionResult);
		if (!actionResult.isSuccess()  /*&& !ALLOW_ALL*/) {
			return ERROR;
		}

		AddressModel address = validator.getScrubbedAddress();


		if (validator.getServiceResult() != null && validator.getServiceResult().isServiceRestricted()) {
			restrictedAddress = true;
			session.setAttribute(SessionName.BLOCKED_ADDRESS_WARNING, Boolean.TRUE);
		}

		if (addressValid /*|| ALLOW_ALL*/) {

			this.reclassifyUser(user, address, serviceType, validator.getServiceResult());

			String application = (String) session.getAttribute(SessionName.APPLICATION);

			//
			// Absence of an FDIdentity in session means this is a new registration.
			// Presence of an FDIdentity might indicate a registered user but failed predicate function
			// (like adding a credit card in CallCenter), so don't attempt to re-register if one is found.
			//
			if (user.getIdentity() == null) {

				ErpCustomerModel erpCustomer = aInfo.getErpCustomerModel();
				ErpCustomerInfoModel customerInfo = new ErpCustomerInfoModel();
				aInfo.decorateCustomerInfo(customerInfo);
				cInfo.decorateCustomerInfo(customerInfo);
				customerInfo.setRegRefTrackingCode(user.getLastRefTrackingCode());
				// changes done by gopal
				customerInfo.setReferralProgId(user.getLastRefProgId());
				customerInfo.setReferralProgInvtId(user.getLastRefProgInvtId());
				
				erpCustomer.setCustomerInfo(customerInfo);

				ErpAddressModel erpAddress = new ErpAddressModel(address);
				erpAddress.setFirstName(customerInfo.getFirstName());
				erpAddress.setLastName(customerInfo.getLastName());
				erpAddress.setPhone(customerInfo.getHomePhone());
				erpAddress.setAddressInfo(address.getAddressInfo());
				erpAddress.setServiceType(serviceType);
				erpAddress.setCompanyName(addInfo.getCompanyName());

				erpCustomer.addShipToAddress(erpAddress);

				FDCustomerModel fdCustomer = new FDCustomerModel();

				fdCustomer.setPasswordHint(aInfo.getPasswordHint());
				fdCustomer.setDepotCode(user.getDepotCode());
				fdCustomer.setDefaultDepotLocationPK(addInfo.getLocationId());
				fdCustomer.setDepotCode(user.getDepotCode());

				FDSurveyResponse survey = aInfo.getMarketingSurvey(new SurveyKey(EnumSurveyType.REGISTRATION_SURVEY, serviceType), request);

				try {
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, survey, serviceType);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
					user.setZipCode(erpAddress.getZipCode());
					user.setSelectedServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
					//Added the following line for zone pricing to keep user service type up-to-date.
					user.setZPServiceType(AddressUtil.getDeliveryServiceType(erpAddress));
					user.updateUserState();
					//Set the Default Delivery pass status.
					FDUserDlvPassInfo dlvpassInfo = new FDUserDlvPassInfo(EnumDlvPassStatus.NONE, null, null, null,0,0,0,false,0,null,0);
					user.getUser().setDlvPassInfo(dlvpassInfo);
					session.setAttribute(SessionName.USER, user);
				} catch (ErpDuplicateUserIdException de) {
					LOGGER.warn("User registration failed due to duplicate id", de);
					actionResult.addError(new ActionError(EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_UNIQUE_USERNAME));
				}

			}

			boolean inCallCenter = "callcenter".equalsIgnoreCase(application);
			
			if (inCallCenter && actionResult.isSuccess()) {
				//
				// Do CallCenter-specific stuff
				//
				LOGGER.debug("Creating payment method for customer, check first to see if basic info entered account num");
				if (!"".equals(request.getParameter(PaymentMethodName.ACCOUNT_NUMBER))) {
					LOGGER.debug("CREATING PAYMENT METHOD");
					ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
					PaymentMethodUtil.validatePaymentMethod(request, paymentMethod, actionResult, user);
					if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
				        String terms = request.getParameter(PaymentMethodName.TERMS);
				        actionResult.addError(
				    	        terms == null || terms.length() <= 0,
				    	        PaymentMethodName.TERMS,SystemMessageList.MSG_REQUIRED
				    	        );
				        if (actionResult.isSuccess()  && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
				        	paymentMethod.setIsTermsAccepted(true);
				        }
					}
					if (actionResult.isSuccess())
						PaymentMethodUtil.addPaymentMethod(request, actionResult, paymentMethod);
					
				} else {
					LOGGER.debug("NO CARD REDIRECT");
					this.setSuccessPage("/main/account_details.jsp?cc=no");
				}
			}

			//
			// we need to see if the user's delivery status changed as a result of registration
			//
			if (this.statusChangePage != null) {
				boolean promoChanged = startedPromoEligible != user.isEligibleForSignupPromotion();
				boolean dlvChanged = (startedAsHomeUser && user.isPickupOnly())
					|| (startedAsPickupOnly && user.isHomeUser())
					|| (user.isDepotUser() && user.isHomeUser())
					|| restrictedAddress;
				//we can never get in here now, there's no SIGNUP promos anymore...
				if (user.isEligibleForSignupPromotion() && (promoChanged || dlvChanged || !signupFromCheckout)) {
					//make sure we use ?/& depending on if there's already query params
					this.setSuccessPage(this.statusChangePage + ( ((this.statusChangePage).indexOf("?") == -1) ? "?" : "&" ) + "promoChange=" + promoChanged + "&dlvChange=" + dlvChanged);
				}
			}
			//Set the
			return SUCCESS;
		} else {
			// Delivery Address check failed
			return ERROR;
		}
	}

	private void reclassifyUser(
		FDSessionUser user,
		AddressModel address,
		EnumServiceType serviceType,
		DlvServiceSelectionResult serviceResult) throws FDResourceException {
		
		HttpSession session = this.getWebActionContext().getSession();

		/*if (serviceResult == null && ALLOW_ALL) {
		    serviceResult = new DlvServiceSelectionResult();
		    serviceResult.addServiceStatus(serviceType, EnumDeliveryStatus.DELIVER);
		}*/
		
		// user.setZipCode(address.getZipCode());
		user.setAddress(address);

		EnumDeliveryStatus status = serviceResult.getServiceStatus(serviceType);
		
		if (EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status) || EnumDeliveryStatus.COS_ENABLED.equals(status)) {
			user.setSelectedServiceType(serviceType);
			//Added the following line for zone pricing to keep user service type up-to-date.
			user.setZPServiceType(serviceType);
		} else {
			user.setSelectedServiceType(EnumServiceType.PICKUP);
			//Added the following line for zone pricing to keep user service type up-to-date.
			user.setZPServiceType(EnumServiceType.PICKUP);
		}
		user.setAvailableServices(serviceResult.getAvailableServices());
		
		session.setAttribute(SessionName.USER, user);
		FDCustomerManager.storeUser(user.getUser());
	}

	protected FDIdentity doRegistration(
		FDCustomerModel fdCustomer,
		ErpCustomerModel erpCustomer,
		FDSurveyResponse survey,
		EnumServiceType serviceType) throws ErpDuplicateUserIdException,ErpDuplicatePaymentMethodException,
		ErpPaymentMethodException,FDResourceException {

		HttpSession session = this.getWebActionContext().getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		RegistrationResult regResult = FDCustomerManager.register(AccountActivityUtil.getActionInfo(session), erpCustomer,
			fdCustomer, user.getCookie(), user.isPickupOnly(), user.isEligibleForSignupPromotion(), survey, serviceType);
		FDIdentity identity = regResult.getIdentity();

		if (regResult.hasPossibleFraud()) {
			LOGGER.info("Possible Fraud condition. Allowing registration but will redirect to page with FRAUD WARNING.");
			this.setSuccessPage(this.fraudPage);
			LOGGER.debug("successPage re-set to " + this.fraudPage);
		}
		System.out.println("RegistrationAction: doRegistration() " + identity);
		return identity;

	}
	
	class ContactInfo {

		private String title;
		private String firstName;
		private String lastName;

		private String department;
		private String employeeId;

		private String homePhone;
		private String homePhoneExt;
		private String workPhone;
		private String workPhoneExt;
		private String cellPhone;
		private String cellPhoneExt;

		public ContactInfo(HttpServletRequest request) {
			this.initialize(request);
		}

		private void initialize(HttpServletRequest request) {

			this.title = NVL.apply(request.getParameter(title), "");
			this.firstName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "").trim();
			this.lastName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "").trim();

			this.homePhone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_HOME_PHONE.getCode()), "").trim();
			this.homePhoneExt = NVL.apply(request.getParameter("homephoneext"), "").trim();

			this.workPhone = NVL.apply(request.getParameter("busphone"), "").trim();
			this.workPhoneExt = NVL.apply(request.getParameter("busphoneext"), "").trim();

			this.cellPhone = NVL.apply(request.getParameter("cellphone"), "").trim();
			this.cellPhoneExt = NVL.apply(request.getParameter("cellphoneext"), "").trim();
			this.department = NVL.apply(request.getParameter("workDepartment"), "").trim();
			this.employeeId = NVL.apply(request.getParameter("employeeId"), "").trim();
		}

		public void validate(ActionResult actionResult, EnumServiceType serviceType) {
			actionResult.addError("".equals(this.lastName), EnumUserInfoName.DLV_LAST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);
			
			actionResult.addError("".equals(this.firstName), EnumUserInfoName.DLV_FIRST_NAME.getCode(),
				SystemMessageList.MSG_REQUIRED);

			if ((regType == AccountUtil.DEPOT_USER || regType == AccountUtil.HOME_USER) || EnumServiceType.HOME.equals(serviceType)) {
				actionResult.addError("".equals(this.homePhone) || homePhone.length() < 10, EnumUserInfoName.DLV_HOME_PHONE
					.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			
			if (regType == AccountUtil.DEPOT_USER || EnumServiceType.CORPORATE.equals(serviceType)) {
				actionResult.addError("".equals(this.workPhone) || this.workPhone.length() < 10, "busphone",
					SystemMessageList.MSG_REQUIRED);
			}
			
			actionResult.addError(!validatePhoneNumber(this.homePhone), EnumUserInfoName.DLV_HOME_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );
			actionResult.addError(!validatePhoneNumber(this.workPhone), EnumUserInfoName.DLV_WORK_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );
			actionResult.addError(!validatePhoneNumber(this.cellPhone),  EnumUserInfoName.DLV_ALT_PHONE.getCode(), SystemMessageList.MSG_NUM_REQ );

			if (regType == AccountUtil.DEPOT_USER) {
				actionResult.addError("".equals(this.department), "workDepartment", SystemMessageList.MSG_REQUIRED);
				try {
					//getUser().getDepotCode();
					DlvDepotModel depot = FDDepotManager.getInstance().getDepot("");
					if (depot != null && depot.getRequireEmployeeId()) {
						actionResult.addError(employeeId == null || employeeId.length() < 1, "employeeId",
							SystemMessageList.MSG_REQUIRED);
					}
				} catch (FDResourceException fdre) {
					LOGGER.error("Difficulty looking up depot details", fdre);
					actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
				}
			}
		}
		
		private boolean validatePhoneNumber(String phoneNumber){
			phoneNumber = phoneNumber.trim();
			if(phoneNumber != null && !"".equals(phoneNumber) && phoneNumber.length() > 0 && phoneNumber.length() < 10){
				return false;
			}
			return true;
		}

		public void decorateCustomerInfo(ErpCustomerInfoModel customerInfo) {

			customerInfo.setTitle(this.title);
			customerInfo.setBusinessPhone(new PhoneNumber(this.workPhone, this.workPhoneExt));
			customerInfo.setCellPhone(new PhoneNumber(this.cellPhone, this.cellPhoneExt));
			customerInfo.setFirstName(this.firstName);
			customerInfo.setLastName(this.lastName);
			customerInfo.setHomePhone(new PhoneNumber(this.homePhone, this.homePhoneExt));
			customerInfo.setWorkDepartment(this.department);
			customerInfo.setEmployeeId(this.employeeId);
		}
	}

	static class AccountInfo {
		private String emailAddress;
		private String repeatEmailAddress;
		private String altEmailAddress;

		private String password;
		private String repeatPassword;
		private String passwordHint;

		private String deliveryInstructions;

		private boolean receiveNews;
		private boolean plainTextEmail;
		private boolean termsAccepted;

		public AccountInfo(HttpServletRequest request) {
			this.initialize(request);
		}


        private void initialize(HttpServletRequest request) {
			this.emailAddress = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "").trim();
			this.repeatEmailAddress = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "").trim();
			this.altEmailAddress = NVL.apply(request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode()), "").trim();

			this.password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "").trim();
			this.repeatPassword = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_PASSWORD.getCode()), "").trim();
			this.passwordHint = NVL.apply(request.getParameter("password_hint"), "").trim();

			this.receiveNews = true;
			this.plainTextEmail = false;
			
			this.termsAccepted = request.getParameter("terms") != null;

			this.deliveryInstructions = NVL.apply(request.getParameter(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode()),
				"none").trim();
			

		}

		public void validate(ActionResult actionResult) {
			actionResult.addError("".equals(emailAddress), EnumUserInfoName.EMAIL.getCode(), SystemMessageList.MSG_REQUIRED);

			actionResult.addError(!actionResult.hasError(EnumUserInfoName.EMAIL.getCode())
				&& !EmailUtil.isValidEmailAddress(emailAddress), EnumUserInfoName.EMAIL.getCode(),
				SystemMessageList.MSG_EMAIL_FORMAT);

			if ("".equals(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_REQUIRED));
			} else if (!emailAddress.equals(repeatEmailAddress)) {
				actionResult.addError(new ActionError(EnumUserInfoName.REPEAT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_REPEAT));
			}

			actionResult.addError(!"".equals(altEmailAddress) && !EmailUtil.isValidEmailAddress(altEmailAddress),
				EnumUserInfoName.ALT_EMAIL.getCode(), SystemMessageList.MSG_EMAIL_FORMAT);

			AccountUtil.validatePassword(actionResult, password, repeatPassword);

			actionResult
				.addError("".equals(passwordHint), EnumUserInfoName.PASSWORD_HINT.getCode(), SystemMessageList.MSG_REQUIRED);

			actionResult.addError(!termsAccepted, "terms", SystemMessageList.MSG_AGREEMENT_CHECK);
		}

		public void decorateCustomerInfo(ErpCustomerInfoModel customerInfo) {
			customerInfo.setEmail(this.emailAddress);
			customerInfo.setUnsubscribeDate(this.receiveNews ? null : new java.util.Date());
			customerInfo.setAlternateEmail(altEmailAddress.trim());
			customerInfo.setReceiveNewsletter(this.receiveNews);
			customerInfo.setEmailPlaintext(this.plainTextEmail);
		}

		public ErpCustomerModel getErpCustomerModel() {
			ErpCustomerModel erpCustomer = new ErpCustomerModel();
			erpCustomer.setUserId(this.emailAddress);
			erpCustomer.setPasswordHash(MD5Hasher.hash(this.password));
			erpCustomer.setActive(true);
			return erpCustomer;
		}

		public FDSurveyResponse getMarketingSurvey(SurveyKey surveyKey, HttpServletRequest request) throws FDResourceException {

			FDSurveyResponse surveyResponse = new FDSurveyResponse(null, surveyKey);
			FDSurvey survey = FDSurveyFactory.getInstance().getSurvey(surveyKey);
			
			for (FDSurveyQuestion q : survey.getQuestions()) {
			    String[] value = request.getParameterValues(q.getName());
			    if (value != null && value.length > 0) {
			        surveyResponse.addAnswer(q.getName(), value);
			    }
			}
			
                        return surveyResponse.getAnswers().isEmpty() ? null : surveyResponse;
		}

		public String getPasswordHint() {
			return this.passwordHint;
		}
	}

	private class AddressInfo {

		private String addressOrLocation;
		private String locationId;

		private EnumServiceType serviceType;
		private String companyName;
		private String street1;
		private String street2;
		private String apt;
		private String city;
		private String state;
		private String zipcode;

		public AddressInfo(HttpServletRequest request) {
			this.initialize(request);
		}

		private void initialize(HttpServletRequest request) {
			this.serviceType = (EnumServiceType) NVL.apply(EnumServiceType.getEnum(request
				.getParameter(EnumUserInfoName.DLV_SERVICE_TYPE.getCode())), EnumServiceType.HOME);
			this.companyName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "");
			this.street1 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "").trim();
			this.street2 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "").trim();
			this.apt = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "").trim();
			this.city = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "").trim();
			this.state = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "").trim();
			this.zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "").trim();
			this.addressOrLocation = NVL.apply(request.getParameter("selectAddressList"), "").trim();

			if (!"".equals(this.addressOrLocation) && addressOrLocation.indexOf("DEPOT_") > -1) {
				locationId = this.addressOrLocation.substring(6, addressOrLocation.length());
			}
		}

		public void validate(ActionResult result) {

			if (EnumServiceType.CORPORATE.equals(this.serviceType)) {
				result
					.addError("".equals(companyName), EnumUserInfoName.DLV_COMPANY_NAME.getCode(), SystemMessageList.MSG_REQUIRED);
			}
			result.addError("".equals(street1), EnumUserInfoName.DLV_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			result.addError("".equals(city), EnumUserInfoName.DLV_CITY.getCode(), SystemMessageList.MSG_REQUIRED);

			if (state.length() < 2) {
				result.addError(new ActionError(EnumUserInfoName.DLV_STATE.getCode(), SystemMessageList.MSG_REQUIRED));
			} else {
				result.addError(!AddressUtil.validateState(state), EnumUserInfoName.DLV_STATE.getCode(),
					SystemMessageList.MSG_UNRECOGNIZE_STATE);
			}

			result.addError(zipcode.length() < 5, EnumUserInfoName.DLV_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);

			if (regType == AccountUtil.DEPOT_USER) {
				result.addError(locationId == null || locationId.length() < 1, "locationId", SystemMessageList.MSG_REQUIRED);
			}

		}

		public AddressModel getDlvAddress() {
			AddressModel address = new AddressModel();
			address.setAddress1(street1);
			address.setAddress2(street2);
			address.setApartment(apt);
			address.setCity(city);
			address.setState(state);
			address.setZipCode(zipcode);
			return address;
		}

		public String getLocationId() {
			return this.locationId;
		}

		public EnumServiceType getAddressType() {
			return this.serviceType;
		}

		public String getCompanyName() {
			return this.companyName;
		}
	}

}