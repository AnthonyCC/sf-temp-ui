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
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpFraudException;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.payments.util.PaymentMethodDefaultComparator;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.payment.BINCache;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

/**
 * One of the ugliest things in ERPSWebApp.
 *
 * @version $Revision$
 * @author $Author$
 */

/**
 * IF YOU ARE IN THIS CLASS MOST LIKELY YOU WILL HAVE TO DOUBLE CHECK 
 * CrmPaymentMethodControllerTag
 *  com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil
 *  com.freshdirect.webapp.ajax.expresscheckout.validation.service.ValidationProviderService
 *  com.freshdirect.fdstore.payments.util.PaymentMethodUtil
 *  Especially if you are validating  BANKING account INFORMATION. PROCESSFORM LINE 277!
 */
public class PaymentMethodUtil implements PaymentMethodName { //AddressName,
    
    private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodUtil.class );
    
    private final static int INVALID			= -1;
    private final static int VISA				= 0;
    private final static int MASTERCARD			= 1;
    private final static int AMERICAN_EXPRESS	= 2;
    private final static int DISCOVER			= 3;
    private final static String NAME_REGEX      ="^[^\\n]*[A-Za-z]+[^\\n]*$";//"^[\\w.-_@(){}/?#$&!+%*<>=,\\s:;'|\"\\\\/`~]*[A-Za-z]+[\\w.-_@(){}/?#$&!+%*<>=,\\s:;'|\"\\\\/`~]*$"; 
    private static final String REGEX = "%&<";

    private PaymentMethodUtil() {
    }
    
    public static void addPaymentMethod(HttpServletRequest request, ActionResult result, ErpPaymentMethodI paymentMethod) throws FDResourceException {
    	FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
    	FDSessionUser sessionuser = null;
        try {
        	sessionuser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        	BINCache binCache = BINCache.getInstance();
        	 boolean isDebitCard = false;
             if(paymentMethod.getCardType().equals(EnumCardType.VISA) || paymentMethod.getCardType().equals(EnumCardType.MC)){
             	isDebitCard = binCache.isDebitCard(paymentMethod.getAccountNumber(), paymentMethod.getCardType());
             }
	        paymentMethod.setDebitCard(isDebitCard);
            FDCustomerManager.addPaymentMethod(info, paymentMethod, sessionuser.isPaymentechEnabled(), FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, sessionuser));
            sessionuser.refreshFdCustomer();
        } catch (ErpPaymentMethodException ex) {
            /*LOGGER.debug(ex);
            result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
            sessionuser.setInvalidPaymentMethod(paymentMethod);*/
        	if("AVS".equals(ex.getMessage()))
	        	result.addError(true,EnumUserInfoName.BIL_ZIPCODE.getCode(),SystemMessageList.MSG_ZIP_CODE);
        	else if("CVV".equals(ex.getMessage()))
        		result.addError(true,PaymentMethodName.CSV,SystemMessageList.MSG_CVV_INCORRECT);
        	else result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
			
        }catch (ErpFraudException ex) {
        	result.addError(new ActionError("deactivated_Account", MessageFormat.format(SystemMessageList.MSG_DEACTIVATED,new Object[] { UserUtil.getCustomerServiceContact(request) })));
       	
        	//request.getSession().invalidate();
        }
                
    }
    
    public static void editPaymentMethod(HttpServletRequest request, ActionResult result, ErpPaymentMethodI paymentMethod) throws FDResourceException {
    	FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
        try {
            FDCustomerManager.updatePaymentMethod(info, paymentMethod);
        }  catch (ErpPaymentMethodException ex) {
            LOGGER.debug(ex);
            if("AVS".equals(ex.getMessage()))
	        	result.addError(true,EnumUserInfoName.BIL_ZIPCODE.getCode(),SystemMessageList.MSG_ZIP_CODE);
        	else if("CVV".equals(ex.getMessage()))
        		result.addError(true,PaymentMethodName.CSV,SystemMessageList.MSG_CVV_INCORRECT);
        	else result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
        }
    }
    
    public static void deletePaymentMethod(HttpServletRequest request, ActionResult result, String paymentId) throws FDResourceException {
		FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
        ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(info.getIdentity(), paymentId);
        if (paymentMethod == null) {
            throw new FDResourceException("payment method not found");
        }
        FDSessionUser fdUser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
        if(StandingOrderHelper.isEligibleForSo3_0(fdUser)){
        	StandingOrderHelper.evaluteSOPaymentId(request.getSession(), fdUser, paymentId);
        }
        FDCustomerManager.removePaymentMethod(info, paymentMethod, 
        		FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, fdUser));
        if(paymentId.equalsIgnoreCase(fdUser.getFDCustomer().getDefaultPaymentMethodPK())){
        	fdUser.getShoppingCart().setPaymentMethod(null);
        }
        fdUser.refreshFdCustomer();
    }
    
    
    public static ErpPaymentMethodI processEditForm(HttpServletRequest request, ActionResult result, FDIdentity identity) throws FDResourceException {
        String paymentId = RequestUtil.getRequestParameter(request,"paymentId");
        if(paymentId == null || paymentId.length() <= 0){
            throw new FDResourceException("Payment ID not specified");
        }
        
        ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(identity, paymentId);
        if(paymentMethod == null) {
            throw new FDResourceException("Payment method not found");
        }
        
        processForm(request, result, identity, paymentMethod, EnumAccountActivityType.UPDATE_PAYMENT_METHOD);
        return paymentMethod;
    }
    
    public static ErpPaymentMethodI processForm(HttpServletRequest request, ActionResult result, FDIdentity identity) throws FDResourceException {

    	ErpPaymentMethodI paymentMethod = null;
		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(RequestUtil.getRequestParameter(request,PaymentMethodName.PAYMENT_METHOD_TYPE));
		paymentMethod = PaymentManager.createInstance(paymentMethodType);
        processForm(request, result, identity, paymentMethod, EnumAccountActivityType.ADD_PAYMENT_METHOD);
        return paymentMethod;
    }
    
    private static void processForm(HttpServletRequest request, ActionResult result, FDIdentity identity, ErpPaymentMethodI paymentMethod, EnumAccountActivityType activityType)
            throws FDResourceException {
        String month = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_EXP_MONTH);
        String year = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_EXP_YEAR);
        String cardType = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_BRAND);
        String accountNumber = RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_NUMBER);
        String abaRouteNumber = RequestUtil.getRequestParameter(request,PaymentMethodName.ABA_ROUTE_NUMBER);
        String bankName = RequestUtil.getRequestParameter(request,PaymentMethodName.BANK_NAME);
        String bankAccountType = RequestUtil.getRequestParameter(request,PaymentMethodName.BANK_ACCOUNT_TYPE);
        String bypassBadAccountCheck = RequestUtil.getRequestParameter(request,PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK);
        String csv= request.getParameter(PaymentMethodName.CSV);
        String name=RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_HOLDER);
        boolean verifyBankAccountNumber = true;
        
        /* Check Account number is null or blank - This validation was moved up to the resolve the issue we had with Ingrian Encryption. Issue was Ingrian unable
           to encrypt blank account. The code need to refactored in near future as there are redundant validations in the existing
           code.
         */   
	     //Fix BEGIN
	     result.addError(
	    		 accountNumber == null || accountNumber.trim().length() == 0 || scrubAccountNumber(accountNumber).length() ==0,
	             PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_REQUIRED
	             );
	     //Fix END 
        
        if (EnumAccountActivityType.UPDATE_PAYMENT_METHOD.equals(activityType) && EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
			accountNumber = paymentMethod.getAccountNumber();
			verifyBankAccountNumber = false;
		}
        
        CharSequence inputStr = name;   
        Pattern pattern = Pattern.compile(NAME_REGEX,Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(inputStr);  
        if(!matcher.matches()){  
        	if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){
        	result.addError(true,
	    	        PaymentMethodName.ACCOUNT_HOLDER,SystemMessageList.MSG_INVALID_CC_NAME
	    	        );
        	}else{
        	result.addError(true,
	    	        PaymentMethodName.ACCOUNT_HOLDER,SystemMessageList.MSG_INVALID_CHK_NAME
	    	        );
        	}
        }  
        result.addError(name == null ||"".equals(name),
    	        PaymentMethodName.ACCOUNT_HOLDER,SystemMessageList.MSG_REQUIRED
    	        );
        
        Calendar expCal = new GregorianCalendar();
        if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){
	        result.addError(
	        cardType == null || cardType.length() <= 0,
	        PaymentMethodName.CARD_BRAND,SystemMessageList.MSG_REQUIRED
	        );
	        result.addError(
	        EnumCardType.getEnum(cardType) == null,
	        PaymentMethodName.CARD_BRAND,SystemMessageList.MSG_INVALID_CARD_TYPE
	        );	        
	        result.addError(
	        month == null || year == null || month.trim().length() <= 0 || year.trim().length() <= 0,
	        "expiration", SystemMessageList.MSG_REQUIRED
	        );
	        
	        if (result.isSuccess()) {
		        SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
	            Date date = sf.parse(month.trim()+year.trim(), new ParsePosition(0));
	            expCal.setTime(date);
	            expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
	        }
	        // check cvv code only if the following conditions is true
	        // 1) isPaymentMethodVerificationEnabled is true
	        // 2) payment is credit card
			if (FDStoreProperties.isPaymentMethodVerificationEnabled()
					&& EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {

				result.addError(csv == null || csv.isEmpty(), PaymentMethodName.CSV, SystemMessageList.MSG_REQUIRED);

				if (EnumCardType.AMEX.equals(EnumCardType.getCardType(cardType))) {
					result.addError(csv != null && csv.length() != 0 && csv.length() != 4, PaymentMethodName.CSV,
							SystemMessageList.MSG_CVV_INCORRECT);
				} else {
					result.addError(csv != null && csv.length() != 0 && csv.length() != 3, PaymentMethodName.CSV,
							SystemMessageList.MSG_CVV_INCORRECT);
				}

			}
        } else if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
	        if (abaRouteNumber != null && !"".equals(abaRouteNumber)) {
	        	abaRouteNumber = StringUtils.leftPad(abaRouteNumber, 9, "0");
	        }        	
	        result.addError(
	    	        bankAccountType == null || bankAccountType.length() <= 0,
	    	        PaymentMethodName.BANK_ACCOUNT_TYPE,SystemMessageList.MSG_REQUIRED
	    	        );	        
	        result.addError(
		        !validateAbaRouteNumber(abaRouteNumber),
				PaymentMethodName.ABA_ROUTE_NUMBER, SystemMessageList.MSG_INVALID_ABA_ROUTE_NUMBER
			);
	        result.addError(
	    	        abaRouteNumber == null || abaRouteNumber.length() <= 0,
	    	        PaymentMethodName.ABA_ROUTE_NUMBER,SystemMessageList.MSG_REQUIRED
	    	        );
	        result.addError(
	    	        bankName == null || bankName.length() <= 0,
	    	        PaymentMethodName.BANK_NAME,SystemMessageList.MSG_REQUIRED
	    	        );
	       if (accountNumber != null && !"".equals(accountNumber)) { 

            	if (verifyBankAccountNumber) {
	        	
		            String accountNumberVerify = RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_NUMBER_VERIFY);
			        // Check account number verify is entered correctly
			        result.addError(
			        accountNumberVerify != null && !accountNumberVerify.equalsIgnoreCase(accountNumber),
					PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER_VERIFY
					);
			        // Check account number verify
			        result.addError(
			        accountNumberVerify == null || "".equals(accountNumberVerify),
					PaymentMethodName.ACCOUNT_NUMBER_VERIFY, SystemMessageList.MSG_REQUIRED
					);
			        
			        // Check to see that account number DOESNT contain a letter (a=z or A-Z) appdev 6789
			        result.addError(
			        		accountNumber.matches(".*[a-zA-Z]+.*"),
					PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_ACCOUNT_NUMBER_ILLEGAL_ALPHA
					);
	
			        // Check account number has at least 5 digits
			        String scrubbedAccountNumber = scrubAccountNumber(accountNumber);
			        			       
			        result.addError(scrubbedAccountNumber.length() < 5,
					PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_ACCOUNT_NUMBER_LENGTH
					);
			        
			        if( EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()))
			        {
			        	result.addError(scrubbedAccountNumber.length() > 25,
			        			PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
			        	);			        	
			        }
		        }
            } else {
		        result.addError(true,	// account number missing
						PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_REQUIRED);	        	

            }
	        	        
        }
        if (result.isSuccess()) {
            
            paymentMethod.setExpirationDate(expCal.getTime());
            paymentMethod.setName(RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_HOLDER));
            if(!accountNumber.equals(paymentMethod.getMaskedAccountNumber()))
            	paymentMethod.setAccountNumber(scrubAccountNumber(accountNumber));
            
            if(!EnumAccountActivityType.UPDATE_PAYMENT_METHOD.equals(activityType)){
            	paymentMethod.setCardType(EnumCardType.getCardType(cardType));
            	paymentMethod.setAbaRouteNumber(abaRouteNumber);            	
            }
            paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
//            paymentMethod.setAbaRouteNumber(abaRouteNumber);
            paymentMethod.setBankName(bankName);
            paymentMethod.setAddress1(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ADDRESS_1.getCode(),true));
            paymentMethod.setAddress2(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ADDRESS_2.getCode(),true));
            paymentMethod.setApartment(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_APARTMENT.getCode(),true));
            paymentMethod.setCity(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_CITY.getCode(),true));
            paymentMethod.setState(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_STATE.getCode(),true));
            paymentMethod.setZipCode(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ZIPCODE.getCode(),true));
            paymentMethod.setCountry(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_COUNTRY.getCode(),true));
            if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
            	paymentMethod.setCountry("US");
            }
            
            paymentMethod.setCVV(csv);
            BINCache binCache = BINCache.getInstance();
            boolean isDebitCard = false;
            if(paymentMethod.getCardType().equals(EnumCardType.VISA) || paymentMethod.getCardType().equals(EnumCardType.MC)){
            	isDebitCard = binCache.isDebitCard(accountNumber, paymentMethod.getCardType());
            }
	        
	        paymentMethod.setDebitCard(isDebitCard);
	       
            if(StringUtil.isEmpty(paymentMethod.getCustomerId()) && identity!=null) {
            	paymentMethod.setCustomerId(identity.getErpCustomerPK());
            }
           
            boolean isBadAccount = PaymentFraudManager.checkBadAccount(paymentMethod, false);            
	        result.addError(
	        	 isBadAccount &&
				(bypassBadAccountCheck == null || "".equals(bypassBadAccountCheck)), 
				PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK,SystemMessageList.MSG_REQUIRED
	        );        	
	        result.addError(
		        	 isBadAccount &&
					(bypassBadAccountCheck == null || "".equals(bypassBadAccountCheck)), 
					PaymentMethodName.ACCOUNT_NUMBER,SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
		        );        	
            result.addError(
            paymentMethod.getName() == null || paymentMethod.getName().trim().length() <= 0,
            PaymentMethodName.ACCOUNT_HOLDER,SystemMessageList.MSG_REQUIRED
            );

            result.addError(
            paymentMethod.getAddress1() == null || paymentMethod.getAddress1().trim().length() <= 0,
            EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
            paymentMethod.getCity() == null || paymentMethod.getCity().trim().length() <= 0,
            EnumUserInfoName.BIL_CITY.getCode(), SystemMessageList.MSG_REQUIRED
            );
            
            String _country=paymentMethod.getCountry();
            String _state=paymentMethod.getState();
            result.addError(_state == null || _state.trim().length() <= 0
            		||(BillingCountryInfo.getEnum(_country)!=null && (false==BillingCountryInfo.getEnum(_country).hasRegion(_state) )),
            EnumUserInfoName.BIL_STATE.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
            paymentMethod.getZipCode() == null || "".equals(paymentMethod.getZipCode()) || paymentMethod.getZipCode().trim().length() <= 0,
            EnumUserInfoName.BIL_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED
            );
            
            result.addError(
            		_country == null || "".equals(_country)|| _country.trim().length() <= 0||
            		null==BillingCountryInfo.getEnum(_country),
                    EnumUserInfoName.BIL_COUNTRY.getCode(), SystemMessageList.MSG_REQUIRED
                    );
            
            
        }
    }
    
    
   	public static String getDisplayableAccountNumber(ErpPaymentMethodI paymentMethod) {
    	return ((ErpPaymentMethodModel)paymentMethod).getMaskedAccountNumber();
    }
    
    public static void validatePaymentMethod(HttpServletRequest request, ErpPaymentMethodI paymentMethod, ActionResult result, FDUserI user,boolean verifyCC, EnumAccountActivityType activityType) throws FDResourceException {
		String bypassBadAccountCheck = RequestUtil.getRequestParameter(request,PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK);
		FDActionInfo action= AccountActivityUtil.getActionInfo(request.getSession(), "");
		
    	validatePaymentMethod(action, paymentMethod, result, user, request.getAttribute("gift_card") != null, request.getAttribute("donation") != null, bypassBadAccountCheck != null && !bypassBadAccountCheck.trim().equals(""),verifyCC,request,activityType );
    }
    
    private static boolean checkAccountNumber(EnumAccountActivityType activityType) {
    	
    	if(EnumAccountActivityType.ADD_PAYMENT_METHOD.equals(activityType) ) 
    		return true;
    	return false;
    }
    public static void validatePaymentMethod ( FDActionInfo action, ErpPaymentMethodI paymentMethod, ActionResult result, FDUserI user, boolean gift_card, boolean donation, boolean bypassBadAccountCheck, boolean verifyCC,HttpServletRequest request,EnumAccountActivityType activityType  ) throws FDResourceException {
    	
    	
    	boolean isFiftyStateValidationReqd=true;
    	//check name on card		 //APPDEV-6661 check special charecters
        result.addError(
        		paymentMethod.getName()==null || paymentMethod.getName().trim().length() < 1 || !isValidString(paymentMethod.getName()) ,
        		PaymentMethodName.ACCOUNT_HOLDER, SystemMessageList.MSG_INVALID_CC_NAME
        		);
        
        if((EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())||EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) && checkAccountNumber(activityType)){
        	 result.addError( StringUtil.isEmpty(paymentMethod.getAccountNumber()),PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_REQUIRED );
        	 if( EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
	        	 result.addError(
	        		        paymentMethod.getCardType() == null || ( !validateCreditCardNumber(paymentMethod.getAccountNumber(), paymentMethod.getCardType().getFdName())),
	        		        PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
	        		        );
        	 }
        	 
        }
        if(EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())){
        	result.addError( StringUtil.isEmpty(paymentMethod.getAccountNumber()),PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_REQUIRED );
        	if(!StringUtil.isEmpty(paymentMethod.getAccountNumber()))
	       	    result.addError(paymentMethod.getAccountNumber().length()<=5,
	       		        PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
	       		        );
   	    } 
        if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){        	
	        //check brand
	        result.addError(
	        paymentMethod.getCardType() == null || EnumCardType.getCardType(paymentMethod.getCardType().getFdName()) == null,
	        PaymentMethodName.CARD_BRAND,SystemMessageList.MSG_REQUIRED
	        );
	        
	        // Check card number

			String csv = paymentMethod.getCVV();
			// 1) isPaymentMethodVerificationEnabled = true
			// 2) verifyCC = true
			if (FDStoreProperties.isPaymentMethodVerificationEnabled() && verifyCC) {
				boolean isEmptyCvv = csv == null || csv.isEmpty() || csv.trim().length() == 0;
				if (isEmptyCvv) {
					result.addError(true, PaymentMethodName.CSV, SystemMessageList.MSG_REQUIRED);
				} else if (EnumCardType.AMEX.equals(paymentMethod.getCardType())) {
					result.addError(csv.length() < 4, PaymentMethodName.CSV, SystemMessageList.MSG_CVV_INCORRECT);
				} else {
					result.addError(csv.length() < 3, PaymentMethodName.CSV, SystemMessageList.MSG_CVV_INCORRECT);
				}

			}
           /* String name=RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_HOLDER);
            result.addError(name == null ||"".equals(name),
	    	        PaymentMethodName.ACCOUNT_HOLDER,SystemMessageList.MSG_REQUIRED
	    	        );
           */
	        FDReservation reservation = null;
	        if ( gift_card ) {
		        //check expiration date
				FDCartModel cart = user.getGiftCart();
				reservation = cart.getDeliveryReservation();
				isFiftyStateValidationReqd = false;
	        } else if ( donation ) {
	        	FDCartModel cart = user.getDonationCart();
				reservation = cart.getDeliveryReservation();
				isFiftyStateValidationReqd = false;
	        } else {
		        //check expiration date
				FDCartModel cart = user.getShoppingCart();
				reservation = cart.getDeliveryReservation();
	        }
			Date checkDate = new Date();
			//validate against delivery date
			if(reservation != null && reservation.getTimeslot()!=null){				
				checkDate = reservation.getStartTime(); 
			}
			// PayPal Changes
			if(paymentMethod.geteWalletID() ==null || ( paymentMethod.geteWalletID() !=null  && !paymentMethod.geteWalletID().equals("2"))){
				result.addError(
			        paymentMethod.getExpirationDate() == null || checkDate.after(paymentMethod.getExpirationDate()),
			        "expiration", SystemMessageList.MSG_CARD_EXPIRATION_DATE
			        );
			}
	        
        }
        if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())||EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) { 
	        result.addError(
	        		paymentMethod.getAddress1()==null || paymentMethod.getAddress1().trim().length() < 1,
					EnumUserInfoName.BIL_ADDRESS_1.getCode(),SystemMessageList.MSG_REQUIRED
	    	        );
	        result.addError(
	        		paymentMethod.getZipCode()==null || paymentMethod.getZipCode().trim().length() < 1,
					EnumUserInfoName.BIL_ZIPCODE.getCode(),SystemMessageList.MSG_REQUIRED
	    	        );
	        
	        result.addError(
	        		paymentMethod.getCity()==null || paymentMethod.getCity().trim().length() < 1,
					EnumUserInfoName.BIL_CITY.getCode(),SystemMessageList.MSG_REQUIRED
	    	        );
	        String _country=paymentMethod.getCountry();
            String _state=paymentMethod.getState();
            //System.out.println("BillingCountryInfo.getEnum(_country).hasRegion(_state)"+BillingCountryInfo.getEnum(_country).hasRegion(_state));
            result.addError(_state == null || _state.trim().length() <= 0
            		||(BillingCountryInfo.getEnum(_country)!=null && (false==BillingCountryInfo.getEnum(_country).hasRegion(_state) )),
            EnumUserInfoName.BIL_STATE.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
            		_country == null || "".equals(_country)|| _country.trim().length() <= 0||
            		null==BillingCountryInfo.getEnum(_country),
                    EnumUserInfoName.BIL_COUNTRY.getCode(), SystemMessageList.MSG_REQUIRED
                    );
	        if(paymentMethod.getCountry()!=null) {
	        	BillingCountryInfo bc=BillingCountryInfo.getEnum(paymentMethod.getCountry());
	        	Pattern zChk=null;
	        	if(bc!=null)
	        		zChk=bc.getZipCheckPattern();
	        	if(zChk!=null) {
	        		 result.addError(
	        				 !zChk.matcher(paymentMethod.getZipCode()).matches(),
	     					EnumUserInfoName.BIL_ZIPCODE.getCode(),SystemMessageList.MSG_ZIP_CODE
	     	    	        );
	        	}
	        	
	        }
        
	        
        
        }else if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
        	        	
	        if (paymentMethod.getAbaRouteNumber() != null && !"".equals(paymentMethod.getAbaRouteNumber())) {
	        	paymentMethod.setAbaRouteNumber(StringUtils.leftPad(paymentMethod.getAbaRouteNumber(), 9, "0"));
	        }
	        
            paymentMethod.setCountry("US");
            if( checkAccountNumber(activityType)) {
            
		        // Check account number	        
	            String accountNumber = paymentMethod.getAccountNumber();
	            
		        
		        result.addError(
		        		accountNumber != null && accountNumber.length()>17,
						PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
				);
		        
		        // Check aba route number number
		        result.addError(
		        !validateAbaRouteNumber(paymentMethod.getAbaRouteNumber()),
				PaymentMethodName.ABA_ROUTE_NUMBER, SystemMessageList.MSG_INVALID_ABA_ROUTE_NUMBER
				);
		        // Check aba route number number
		        result.addError(
		        paymentMethod.getAbaRouteNumber() == null || "".equals(paymentMethod.getAbaRouteNumber()),
				PaymentMethodName.ABA_ROUTE_NUMBER, SystemMessageList.MSG_REQUIRED
				);
            }
	        // Check bank account type
	        result.addError(
	        paymentMethod.getBankAccountType() == null,
			PaymentMethodName.BANK_ACCOUNT_TYPE, SystemMessageList.MSG_REQUIRED
			);
	        // Check bank name
	        result.addError(
	        paymentMethod.getBankName() == null || "".equals(paymentMethod.getBankName()),
			PaymentMethodName.BANK_NAME, SystemMessageList.MSG_REQUIRED
			);
	        // Check Bill Address 
			result.addError("".equals(paymentMethod.getAddress1()), EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_REQUIRED);
			result.addError("".equals(paymentMethod.getCity()), EnumUserInfoName.BIL_CITY.getCode(), SystemMessageList.MSG_REQUIRED);
			result.addError("".equals(paymentMethod.getState()), EnumUserInfoName.BIL_STATE.getCode(), SystemMessageList.MSG_REQUIRED);
			result.addError("".equals(paymentMethod.getZipCode()), EnumUserInfoName.BIL_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED);
			
			if(result.isSuccess()){
				result.addError(
		        	PaymentFraudManager.checkBadAccount(paymentMethod, false) && !bypassBadAccountCheck, 
					PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK,SystemMessageList.MSG_REQUIRED
		        );
			}
        }
        
        //
        // standardize billing address on all credit cards
        //
        /*
         * mshetty- This scrub functionality breaks eCheck functionality will SmartyStreet. Not required to scrub payment address.
         * if (result.isSuccess()&& !EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())&&!EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) {   
        	
        	AddressModel cleanAddress = scrubAddress(paymentMethod.getAddress(), result,isFiftyStateValidationReqd);
    		paymentMethod.setAddress1(cleanAddress.getAddress1());
    		paymentMethod.setAddress2(cleanAddress.getAddress2());

        }*/
        
    }
    
   
        
    /**
     * this method takes a credit card number and removes dashes or spaces and all non numeric numbers from it
     *
     * @param String number to scrub
     * @return dry cleaned number
     */
    public static String scrubAccountNumber(String number){
        StringBuffer digitsOnly = new StringBuffer();
        for (int i=0; i<number.length(); i++) {
            char c = number.charAt(i);
            if (Character.isDigit(c)) {
                digitsOnly.append(c);
            }
        }
        return digitsOnly.toString();
    }
    
    /**
     * this method takes a potential credit card number and checks against Luhn check on it
     * including the prefix check making sure that the it is from a one of correct providers
     *
     * @param String number to check
     * @return true if it is a valid credit card number
     */
    public static boolean validateCreditCardNumber(String number, String brand){
        if (validateCardBrand(number, brand) == INVALID){
            return false;
        }
        //now we know it is a number and brand is valid, next we'll check for mod10
        try {
            int checksum = 0;
            boolean timesTwo = false;
            
            for (int i = number.length()-1; i >= 0; i--) {
                int k = 0;
                if(timesTwo){
                    k = Integer.parseInt(number.substring(i, i+1)) * 2;
                    if (k > 9) {
                        k = k - 9;
                    }
                }else{
                    k = Integer.parseInt(number.substring(i, i+1));
                }
                checksum += k;
                timesTwo = !timesTwo;
            }
            
            System.out.println((checksum % 10) == 0);
            return (checksum % 10) == 0;
            
        } catch (NumberFormatException ne) {
            return false;
        }
    }
    
    protected static int validateCardBrand(String number, String brand) {
    	if(number.length()<2)
    		return INVALID;
        String digit2 = number.substring(0,2);
        
        // for VISA prefix is 4 and lenght must be 13 || 16
        if (number.startsWith("4") && EnumCardType.VISA.getFdName().equalsIgnoreCase(brand))  {
            if (number.length() == 13 || number.length() == 16){
                return VISA;
            }
            
            
        } //else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0 && EnumCardType.MC.getFdName().equalsIgnoreCase(brand)) {
        //https://github.com/thephpleague/omnipay-common/pull/88/files#diff-96ff2b6b860cf039a754070984b61486R122
        else if(EnumCardType.MC.getFdName().equalsIgnoreCase(brand) && Pattern.compile("^(5[1-5]\\d{4}|677189)\\d{10}$|^(222[1-9]|2[3-6]\\d{2}|27[0-1]\\d|2720)\\d{12}").matcher(number).matches()) {
            if (number.length() == 16){
                return MASTERCARD;
            }
            
            //for AMEX prefix is 34 || 37 and length must be 15
        } else if ((digit2.equals("34") || digit2.equals("37")) && EnumCardType.AMEX.getFdName().equalsIgnoreCase(brand)) {
            if (number.length() == 15){
                return AMERICAN_EXPRESS;
            }
            
            //Discover card numbers begin with 6011 or 65 and length is 16
        } else if(Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{12}$").matcher(number).matches()) {
        
        	return DISCOVER;
        } 
        return INVALID;
    }

    /**
     * this method takes a potential aba route number number and checks against a checksum on it
     * @param String aba route number to check
     * @return true if it is a valid aba route number
     */
    public static boolean validateAbaRouteNumber(String number){
        try {

            if (number == null || number.length() != 9) {
            	return false;            	
            }
                    
            int district = Integer.parseInt(number.substring(0, 2));
            if (!(district >= 1 && district <= 12) && !(district >= 21 && district <= 32)) {
            	return false;  // not a valid district or thrift institution
            }
            
            int checksum = 0;
            for (int i = 0; i < 9; i++) {
                int k = 0;
                k = Integer.parseInt(number.substring(i, i+1));
                int multiplier = 0;
                if (i == 0 || i == 3 || i == 6) {
                	multiplier = 3;
                } else if (i == 1 || i == 4 || i == 7) {
                	multiplier = 7;                	
                } else if (i == 2 || i == 5 || i == 8) {
                	multiplier = 1;                	
                }
                checksum += (k*multiplier);                	
            }
            return (checksum % 10) == 0;
            
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public static boolean hasECheckAccount(FDIdentity identity) {
    	
    	try {
	    	Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods(identity);
	
	    	//
	    	//    	   EChecks terms only needs to be displayed when adding the first Echeck account
	    	//
	    	
	    	for ( ErpPaymentMethodI thisPaymentMethod : paymentMethods ) {
	    		if (EnumPaymentMethodType.ECHECK.equals(thisPaymentMethod.getPaymentMethodType())) {
	    			return true;
	    		}
	    	}
	    	return false;
    	} catch (FDResourceException e) {
    		LOGGER.error(e);
	    	return false;
    	}
    }
    
    public static ErpPaymentMethodI createGiftCardPaymentMethod(FDUserI user) throws FDResourceException {
    	ErpPaymentMethodI paymentMethod = PaymentManager.createInstance(EnumPaymentMethodType.GIFTCARD);
        paymentMethod.setName(user.getFirstName() + " "+ user.getLastName());
        paymentMethod.setAccountNumber("1000");
        paymentMethod.setAddress1(FDStoreProperties.getFdDefaultBillingStreet());
        paymentMethod.setCity(FDStoreProperties.getFdDefaultBillingTown());
        paymentMethod.setState(FDStoreProperties.getFdDefaultBillingState());
        paymentMethod.setZipCode(FDStoreProperties.getFdDefaultBillingPostalcode());
        paymentMethod.setCountry(FDStoreProperties.getFdDefaultBillingCountry());
    	return paymentMethod;
    }
    
    public static String getAuthFailWarningMessage(String authFailMsg ) {
    	if(StringUtil.isEmpty(authFailMsg)) return "";
    	String msg=SystemMessageList.MSG_AUTH_FAIL_WARNING_3;
    	String code=authFailMsg.length()>=3?authFailMsg.substring(0,3):authFailMsg;
    	int val=0;
    	try {val=Integer.parseInt(code);}catch(Exception e) {}
    	switch(val) {
    	case 606 :case 261 :case 267 :case 610:msg=SystemMessageList.MSG_AUTH_FAIL_WARNING_1;break;
    	case 201 :case 591 :case 825 :case 304 :case 813:msg=SystemMessageList.MSG_AUTH_FAIL_WARNING_2;break;
    	case 522 :case 595 :case 603 :case 605 :case 754 :case 903 :case 904:msg=SystemMessageList.MSG_AUTH_FAIL_WARNING_4;break;
    	
    	}
    	return msg;
    	
    }
    public static String getAuthFailErrorMessage(String authFailMsg ) {
    	
    	if(StringUtil.isEmpty(authFailMsg)) return "";
    	
    	String msg=SystemMessageList.MSG_AUTH_FAIL_ERR_3;
    	String code=authFailMsg.length()>=3?authFailMsg.substring(0,3):authFailMsg;
    	int val=0;
    	try {val=Integer.parseInt(code);}catch(Exception e) {}
    	switch(val) {
	    	case 606 :case 261 :case 267 :case 610:msg=SystemMessageList.MSG_AUTH_FAIL_ERR_1;break;
	    	case 201 :case 591 :case 825 :case 304 :case 813:msg=SystemMessageList.MSG_AUTH_FAIL_ERR_2;break;
	    	case 522 :case 595 :case 603 :case 605 :case 754 :case 903 :case 904:msg=SystemMessageList.MSG_AUTH_FAIL_ERR_4;break;
    	}
    	return msg;
    	
    }

    public static String getPayPalAuthFailErrorMessage(String authFailMsg ) {
    	String msg =SystemMessageList.MSG_PAYPAL_AUTH_FAIL_ERR_1;
    	
    	if(authFailMsg != null && !authFailMsg.isEmpty()){
    		if(authFailMsg.equalsIgnoreCase("PayPal Buyer Revoked Pre-Approved Payment Authorization")){
    			msg =SystemMessageList.MSG_PAYPAL_AUTH_FAIL_ERR;
    		}else{
    			msg = authFailMsg;
    		}
    	}
    	return msg;
    	
    }
/**
     * @param token
     * @return
     */
    public static boolean isVaultTokeValid(String token, String customerId){
    	try {
			return FDCustomerManager.isValidVaultToken(token,customerId);
		} catch (FDResourceException e) {
			return true;
		}
    }
    
    public static void sortPaymentMethodsByPriority(List<ErpPaymentMethodI> paymentMethods){
    	Collections.sort(paymentMethods, new PaymentMethodDefaultComparator());
    }
    
    public static boolean isValidString(String value) {

		String str2[]=value.split("");
		for (int i = 0; i < str2.length; i++) {
			if (!"".equalsIgnoreCase(str2[i]) && REGEX.contains(str2[i])) {
				return false;
			}
		}
		return true;
	}
}