/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001-2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;
import java.text.*;
import javax.servlet.http.*;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.*;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.payment.fraud.PaymentFraudManager;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.fdstore.*;
import com.freshdirect.delivery.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.framework.webapp.*;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.commons.lang.StringUtils;

import org.apache.log4j.*;

/**
 * One of the ugliest things in ERPSWebApp.
 *
 * @version $Revision$
 * @author $Author$
 */
public class PaymentMethodUtil implements PaymentMethodName { //AddressName,
    
    private static Category LOGGER = LoggerFactory.getInstance( PaymentMethodUtil.class );
    
    private final static int INVALID			= -1;
    private final static int VISA				= 0;
    private final static int MASTERCARD			= 1;
    private final static int AMERICAN_EXPRESS	= 2;
    private final static int DISCOVER			= 3;
    
    private PaymentMethodUtil() {
    }
    
    public static void addPaymentMethod(HttpServletRequest request, ActionResult result, ErpPaymentMethodI paymentMethod) throws FDResourceException {
        try {
            FDCustomerManager.addPaymentMethod(AccountActivityUtil.getActionInfo(request.getSession()), paymentMethod);
        } catch (ErpDuplicatePaymentMethodException ex) {
            LOGGER.debug(ex);
            result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
            FDSessionUser sessionuser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
            sessionuser.setInvalidPaymentMethod(paymentMethod);
        } catch (ErpPaymentMethodException ex) {
            LOGGER.debug(ex);
            result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
            FDSessionUser sessionuser = (FDSessionUser) request.getSession().getAttribute(SessionName.USER);
            sessionuser.setInvalidPaymentMethod(paymentMethod);
        }
                
    }
    
    public static void editPaymentMethod(HttpServletRequest request, ActionResult result, ErpPaymentMethodI paymentMethod) throws FDResourceException {
        try {
            FDCustomerManager.updatePaymentMethod(AccountActivityUtil.getActionInfo(request.getSession()), paymentMethod);
        } catch (ErpDuplicatePaymentMethodException ex) {
            LOGGER.info(ex);
            result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
        } catch (ErpPaymentMethodException ex) {
            LOGGER.debug(ex);
            result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER));
        }
    }
    
    public static void deletePaymentMethod(HttpServletRequest request, ActionResult result, String paymentId) throws FDResourceException {
		FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
        ErpPaymentMethodI paymentMethod = FDCustomerManager.getPaymentMethod(info.getIdentity(), paymentId);
        if (paymentMethod == null) {
            throw new FDResourceException("payment method not found");
        }
        
        FDCustomerManager.removePaymentMethod(info, paymentMethod);
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
        
        processForm(request, result, identity, paymentMethod);
        return paymentMethod;
    }
    
    public static ErpPaymentMethodI processForm(HttpServletRequest request, ActionResult result, FDIdentity identity) throws FDResourceException {

    	ErpPaymentMethodI paymentMethod = null;
		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(RequestUtil.getRequestParameter(request,PaymentMethodName.PAYMENT_METHOD_TYPE));
		paymentMethod = PaymentManager.createInstance(paymentMethodType);
        processForm(request, result, identity, paymentMethod);
        return paymentMethod;
    }
    
    private static void processForm(HttpServletRequest request, ActionResult result, FDIdentity identity, ErpPaymentMethodI paymentMethod) throws FDResourceException {
        String month = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_EXP_MONTH);
        String year = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_EXP_YEAR);
        String cardType = RequestUtil.getRequestParameter(request,PaymentMethodName.CARD_BRAND);
        String accountNumber = RequestUtil.getRequestParameter(request,PaymentMethodName.ACCOUNT_NUMBER);
        String abaRouteNumber = RequestUtil.getRequestParameter(request,PaymentMethodName.ABA_ROUTE_NUMBER);
        String bankName = RequestUtil.getRequestParameter(request,PaymentMethodName.BANK_NAME);
        String bankAccountType = RequestUtil.getRequestParameter(request,PaymentMethodName.BANK_ACCOUNT_TYPE);
        String bypassBadAccountCheck = RequestUtil.getRequestParameter(request,PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK);
        String csv=RequestUtil.getRequestParameter(request,PaymentMethodName.CSV);
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
        if((accountNumber != null && accountNumber.startsWith("xxx"))){
        	accountNumber = paymentMethod.getAccountNumber();
        	verifyBankAccountNumber = false;
        }
        
        Calendar expCal = new GregorianCalendar();
        if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){
	        result.addError(
	        cardType == null || cardType.length() <= 0,
	        PaymentMethodName.CARD_BRAND,SystemMessageList.MSG_REQUIRED
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
	        
	        if(FDStoreProperties.isPaymentMethodVerificationEnabled()) {
	        	result.addError(
		    	        csv == null || csv.length() <= 0,
		    	        PaymentMethodName.CSV,SystemMessageList.MSG_REQUIRED
		    	        );
	        	
	        	if(EnumCardType.AMEX.equals(EnumCardType.getCardType(cardType))) {
	        		result.addError(
			    	        csv != null & csv.length() !=4,
			    	        PaymentMethodName.CSV,SystemMessageList.MSG_CVV_INCORRECT
			    	        );
	        	} else {
	        		result.addError(
			    	        csv != null & csv.length() !=3,
			    	        PaymentMethodName.CSV,SystemMessageList.MSG_CVV_INCORRECT
			    	        );
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
            paymentMethod.setAccountNumber(scrubAccountNumber(accountNumber));
            paymentMethod.setCardType(EnumCardType.getCardType(cardType));
            paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
            paymentMethod.setAbaRouteNumber(abaRouteNumber);
            paymentMethod.setBankName(bankName);
            paymentMethod.setAddress1(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ADDRESS_1.getCode(),true));
            paymentMethod.setAddress2(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ADDRESS_2.getCode(),true));
            paymentMethod.setApartment(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_APARTMENT.getCode(),true));
            paymentMethod.setCity(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_CITY.getCode(),true));
            paymentMethod.setState(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_STATE.getCode(),true));
            paymentMethod.setZipCode(RequestUtil.getRequestParameter(request,EnumUserInfoName.BIL_ZIPCODE.getCode(),true));
            paymentMethod.setCountry("US");
            paymentMethod.setCVV(csv);
            if(StringUtil.isEmpty(paymentMethod.getCustomerId())) {
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
            result.addError(
            paymentMethod.getState() == null || paymentMethod.getState().trim().length() <= 0,
            EnumUserInfoName.BIL_STATE.getCode(), SystemMessageList.MSG_REQUIRED
            );
            result.addError(
            paymentMethod.getZipCode() == null || "".equals(paymentMethod.getZipCode()) || paymentMethod.getZipCode().trim().length() <= 0,
            EnumUserInfoName.BIL_ZIPCODE.getCode(), SystemMessageList.MSG_REQUIRED
            );
        }
    }
    
    public static String getDisplayableAccountNumber(ErpPaymentMethodI paymentMethod) {
    	return ((ErpPaymentMethodModel)paymentMethod).getMaskedAccountNumber();
    }
    
    public static void validatePaymentMethod(HttpServletRequest request, ErpPaymentMethodI paymentMethod, ActionResult result, FDUserI user,boolean verifyCC) throws FDResourceException {
		String bypassBadAccountCheck = RequestUtil.getRequestParameter(request,PaymentMethodName.BYPASS_BAD_ACCOUNT_CHECK);
		FDActionInfo action= AccountActivityUtil.getActionInfo(request.getSession(), "");
		
    	validatePaymentMethod(action, paymentMethod, result, user, request.getAttribute("gift_card") != null, request.getAttribute("donation") != null, bypassBadAccountCheck != null && !bypassBadAccountCheck.trim().equals(""),verifyCC );
    }
    
    public static void validatePaymentMethod ( FDActionInfo action, ErpPaymentMethodI paymentMethod, ActionResult result, FDUserI user, boolean gift_card, boolean donation, boolean bypassBadAccountCheck, boolean verifyCC ) throws FDResourceException {
    	boolean isFiftyStateValidationReqd=true;
    	//check name on card
        result.addError(
        paymentMethod.getName()==null || paymentMethod.getName().trim().length() < 1,
        PaymentMethodName.ACCOUNT_HOLDER, SystemMessageList.MSG_REQUIRED
        );
        
        if(EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())){        	
	        //check brand
	        result.addError(
	        paymentMethod.getCardType() == null || EnumCardType.getCardType(paymentMethod.getCardType().getFdName()) == null,
	        PaymentMethodName.CARD_BRAND,SystemMessageList.MSG_REQUIRED
	        );
	        
	        // Check card number
	        result.addError(
	        paymentMethod.getCardType() == null || (paymentMethod.getAccountNumber() == null || "".equals(paymentMethod.getAccountNumber()) || !validateCreditCardNumber(paymentMethod.getAccountNumber(), paymentMethod.getCardType().getFdName())),
	        PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_INVALID_ACCOUNT_NUMBER
	        );
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
			
	        result.addError(
	        paymentMethod.getExpirationDate() == null || checkDate.after(paymentMethod.getExpirationDate()),
	        "expiration", SystemMessageList.MSG_CARD_EXPIRATION_DATE
	        );
	        if(!result.isFailure() && FDStoreProperties.isPaymentMethodVerificationEnabled()&& !paymentMethod.isBypassAVSCheck()&& verifyCC) {
	        	
	        	
	        	try {
	        		
	        		ErpAuthorizationModel auth=FDCustomerManager.verify(action, paymentMethod);
	        		if(auth==null) {
	        			result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_TECHNICAL_ERROR));
	        		} else {
	        			if(auth.isApproved()) {
		        			result.addError(
		        					!auth.isCVVMatch(),
					    	        PaymentMethodName.CSV,SystemMessageList.MSG_CVV_INCORRECT
					    	        );
		        			//result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_INVALID_ADDRESS));
		        			result.addError(
		        					!ErpServicesProperties.isAvsAddressMatchReqd() && !auth.hasAvsMatched(),
		        					EnumUserInfoName.BIL_ZIPCODE.getCode(),SystemMessageList.MSG_ZIP_CODE
					    	        );
		        			result.addError(
		        					ErpServicesProperties.isAvsAddressMatchReqd() & !auth.hasAvsMatched(),
		        					EnumUserInfoName.BIL_ADDRESS_1.getCode(),SystemMessageList.MSG_INVALID_ADDRESS
					    	        );
		        			
	        			} else {
	        				result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_AUTH_FAILED));
	        			}
	        			if(result.isSuccess())
	        				paymentMethod.setAvsCkeckFailed(false);
	        		}
	        	} catch(ErpAuthorizationException e) {
	        		result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_TECHNICAL_ERROR+" "+e.toString()));
	        	}catch (ErpTransactionException e) {
	        		result.addError(new ActionError("payment_method_fraud", SystemMessageList.MSG_TECHNICAL_ERROR+" "+e.toString()));
				}
	        }
        } else if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
        	        	
	        if (paymentMethod.getAbaRouteNumber() != null && !"".equals(paymentMethod.getAbaRouteNumber())) {
	        	paymentMethod.setAbaRouteNumber(StringUtils.leftPad(paymentMethod.getAbaRouteNumber(), 9, "0"));
	        }
	        
	        // Check account number	        
            String accountNumber = paymentMethod.getAccountNumber();
            
	        result.addError(
	        		accountNumber == null || "".equals(accountNumber),
					PaymentMethodName.ACCOUNT_NUMBER, SystemMessageList.MSG_REQUIRED
			);
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
        if (result.isSuccess()&& !EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {   
        	
        	AddressModel cleanAddress = scrubAddress(paymentMethod.getAddress(), result,isFiftyStateValidationReqd);
    		paymentMethod.setAddress1(cleanAddress.getAddress1());
    		paymentMethod.setAddress2(cleanAddress.getAddress2());

        }
        
    }
    
    
    private static AddressModel scrubAddress(AddressModel address, ActionResult result,boolean isFiftyStateValidationReqd) throws FDResourceException {

		result.addError(address.getCity() == null || "".equals(address.getCity()),
				EnumUserInfoName.BIL_CITY.getCode(), SystemMessageList.MSG_REQUIRED);

		result.addError(address.getState() == null || "".equals(address.getState()) || !AddressUtil.validateState(address.getState()),
		EnumUserInfoName.BIL_STATE.getCode(), SystemMessageList.MSG_UNRECOGNIZE_STATE);
        
		result.addError(address.getZipCode() == null || "".equals(address.getZipCode()),
				 EnumUserInfoName.BIL_ZIPCODE.getCode(), SystemMessageList.MSG_ZIP_CODE);
		
		if(!AddressUtil.validateTriState(address.getState())){
			//If its a non-tri state address there is no way validate it currently. Accept the address as is.
			return address;
		}
		
		// this work is temp stalled since qa got problem
		//if(!isFiftyStateValidationReqd) return address;
		
		DlvAddressVerificationResponse response = FDDeliveryManager.getInstance().scrubAddress(address);
        String apartment = address.getApartment();
        
        LOGGER.debug("Scrubbing response: " + response.getResult());
        if (!EnumAddressVerificationResult.ADDRESS_OK.equals(response.getResult())) {
            
            result.addError(EnumAddressVerificationResult.NOT_VERIFIED.equals(response.getResult()),
            EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
            
            result.addError(EnumAddressVerificationResult.ADDRESS_BAD.equals(response.getResult()),
			EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_INVALID_ADDRESS);
            
            result.addError(EnumAddressVerificationResult.STREET_WRONG .equals(response.getResult()),
			EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_UNRECOGNIZE_ADDRESS);
            
            result.addError(EnumAddressVerificationResult.BUILDING_WRONG.equals(response.getResult()),
			EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_UNRECOGNIZE_STREET_NUMBER);
            
            result.addError(EnumAddressVerificationResult.APT_WRONG.equals(response.getResult()),
			EnumUserInfoName.BIL_APARTMENT.getCode(),
            ((apartment == null) || (apartment.length() < 1)) ? SystemMessageList.MSG_APARTMENT_REQUIRED : SystemMessageList.MSG_UNRECOGNIZE_APARTMENT_NUMBER
            );
            
            result.addError(EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE.equals(response.getResult()),
			EnumUserInfoName.BIL_ADDRESS_1.getCode(), SystemMessageList.MSG_UNRECOGNIZE_ADDRESS);
            
            //
            // return original broken address is there was an error
            //
            return address;
        }
        //
        // all's well, return fixed/cleaned corrected address
        //
        address.setAddress1(response.getAddress().getAddress1());
        address.setAddress2(response.getAddress().getAddress2());
        return address;
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
            
            return (checksum % 10) == 0;
            
        } catch (NumberFormatException ne) {
            return false;
        }
    }
    
    protected static int validateCardBrand(String number, String brand) {
        String digit2 = number.substring(0,2);
        
        // for VISA prefix is 4 and lenght must be 13 || 16
        if (number.startsWith("4") && EnumCardType.VISA.getFdName().equalsIgnoreCase(brand))  {
            if (number.length() == 13 || number.length() == 16){
                return VISA;
            }
            
            // for MASTERCARD prefix is between 51..55 and length must be 16
        } else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0 && EnumCardType.MC.getFdName().equalsIgnoreCase(brand)) {
            if (number.length() == 16){
                return MASTERCARD;
            }
            
            //for AMEX prefix is 34 || 37 and length must be 15
        } else if ((digit2.equals("34") || digit2.equals("37")) && EnumCardType.AMEX.getFdName().equalsIgnoreCase(brand)) {
            if (number.length() == 15){
                return AMERICAN_EXPRESS;
            }
            
            //for DISC prefix is 60 and length must be 16
        } else if(digit2.equals("60") && EnumCardType.DISC.getFdName().equalsIgnoreCase(brand)){
            if(number.length() == 16){
                return DISCOVER;
            }
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
        paymentMethod.setAddress1("23-30 Borden Ave");
        paymentMethod.setCity("Long Island City");
        paymentMethod.setState("NY");
        paymentMethod.setZipCode("11101");
        paymentMethod.setCountry("US");
    	return paymentMethod;
    }
}