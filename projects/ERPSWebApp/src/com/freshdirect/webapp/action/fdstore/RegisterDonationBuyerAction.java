package com.freshdirect.webapp.action.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.RegistrationResult;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.webapp.action.fdstore.RegistrationAction.AccountInfo;
import com.freshdirect.webapp.action.fdstore.RegistrationAction.ContactInfo;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class RegisterDonationBuyerAction extends RegistrationAction {
	public RegisterDonationBuyerAction(int regType) {
		super(regType);
		// TODO Auto-generated constructor stub
	}

	private static Category LOGGER = LoggerFactory.getInstance(RegisterGiftCardBuyerAction.class);
	
	public String execute() throws Exception {
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		ActionResult actionResult = this.getResult();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		ContactInfo cInfo = new ContactInfo(request);
		AccountInfo aInfo = new AccountInfo(request);

		aInfo.validate(actionResult);
		cInfo.validate(actionResult, EnumServiceType.WEB);
		if (!actionResult.isSuccess()) {
			return ERROR;
		}
		/*
		if (user == null) {
            FDUser robotUser = new FDUser(new PrimaryKey("robot"));
            Set availableServices = new HashSet();
            if("professional".equals(request.getParameter("serviceType"))) {
            	availableServices.add(EnumServiceType.GIFT_CARD_CORPORATE);
                robotUser.setSelectedServiceType(EnumServiceType.GIFT_CARD_CORPORATE);
            } else {
            	availableServices.add(EnumServiceType.GIFT_CARD_PERSONAL);
                robotUser.setSelectedServiceType(EnumServiceType.GIFT_CARD_PERSONAL);
            }
            robotUser.setAvailableServices(availableServices);
            robotUser.isLoggedIn(false);
            user = new FDSessionUser(robotUser, session);
            
        }
        */
		session.setAttribute(SessionName.USER, user);
		//System.out.println("user cookie:  " + user.getUser().getCookie() + " user primary key = " + user.getUser().getPrimaryKey() + " identity = " + user.getUser().getIdentity().toString());
		FDCustomerManager.storeUser(user.getUser());
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
				//Set the default billing address provided by customer.
				ErpPaymentMethodI paymentMethod = PaymentMethodUtil.processForm(request, actionResult, user.getIdentity());
				erpCustomer.setSapBillToAddress(paymentMethod.getAddress());
				FDCustomerModel fdCustomer = new FDCustomerModel();

				fdCustomer.setPasswordHint(aInfo.getPasswordHint());

				try {
					FDIdentity regIdent = this.doRegistration(fdCustomer, erpCustomer, null, EnumServiceType.HOME);
					//user.getShoppingCart().setZoneInfo(zoneInfo);
					user.setIdentity(regIdent);
					user.invalidateCache();
					user.isLoggedIn(true);
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
			
			/*boolean inCallCenter = "callcenter".equalsIgnoreCase(application);
			
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
			}*/
		
			//Set the
			
			if (actionResult.isSuccess()) {
				user.isLoggedIn(true);
			}
		
			return SUCCESS;
		
	}
	
	protected FDIdentity doRegistration(
			FDCustomerModel fdCustomer,
			ErpCustomerModel erpCustomer,
			FDSurveyResponse survey,
			EnumServiceType serviceType) throws ErpDuplicateUserIdException, FDResourceException {

			HttpSession session = this.getWebActionContext().getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
			RegistrationResult regResult = FDCustomerManager.register(AccountActivityUtil.getActionInfo(session), erpCustomer,
				fdCustomer, user.getCookie(), user.isPickupOnly(), user.isEligibleForSignupPromotion(), survey, serviceType, true);
			
			FDIdentity identity = regResult.getIdentity();

			//if (regResult.hasPossibleFraud()) {
			//	LOGGER.info("Possible Fraud condition. Allowing registration but will redirect to page with FRAUD WARNING.");
			//	this.setSuccessPage(this.fraudPage);
			//	LOGGER.debug("successPage re-set to " + this.fraudPage);
			//}
			return identity;

		}

}
