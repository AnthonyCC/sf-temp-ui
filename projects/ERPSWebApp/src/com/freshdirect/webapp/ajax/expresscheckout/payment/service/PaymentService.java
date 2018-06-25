package com.freshdirect.webapp.ajax.expresscheckout.payment.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.storeapi.content.ComparatorChain;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentEditData;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.PaymentValidationDataService;
import com.freshdirect.webapp.checkout.PaymentMethodManipulator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodName;
import com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class PaymentService {

	private static Category		LOGGER	= LoggerFactory.getInstance( PaymentService.class );
	
    private static final PaymentService INSTANCE = new PaymentService();
	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String MP_EWALLET_CARD="MP_CARD";
	private static final String WALLET_SESSION_CARD_ID="WALLET_CARD_ID";
	private static final String EWALLET_TXN_ID_ATTRIBUTE_NAME = "transactionId";

    private PaymentService() {
    }

    public static PaymentService defaultService() {
        return INSTANCE;
    }

    public List<ValidationError> addPaymentMethod(FormDataRequest paymentRequestData, HttpServletRequest request, FDUserI user) throws FDResourceException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(PaymentValidationDataService.defaultService().prepareAndValidate(paymentRequestData));
        if (validationErrors.isEmpty()) {
            ErpPaymentMethodI paymentMethod = parsePaymentMethodForm(paymentRequestData, user);
            ActionResult actionResult = new ActionResult();
            PaymentMethodManipulator.performAddPaymentMethodInternal(paymentMethod, actionResult, request);
            processErrors(validationErrors, actionResult);
        }
        return validationErrors;
    }

    public List<ValidationError> editPaymentMethod(FormDataRequest paymentRequestData, HttpServletRequest request, FDUserI user) throws FDResourceException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        validationErrors.addAll(PaymentValidationDataService.defaultService().prepareAndValidate(paymentRequestData));
        if (validationErrors.isEmpty()) {
            ErpPaymentMethodI paymentMethod = parsePaymentMethodForm(paymentRequestData, user);
            ActionResult actionResult = new ActionResult();
            PaymentMethodManipulator.performEditPaymentMethod(request, paymentMethod, actionResult, user);
            processErrors(validationErrors, actionResult);
        }
        return validationErrors;
    }

    public void deletePaymentMethod(FormDataRequest paymentRequestData, HttpServletRequest request) throws FDResourceException {
        String paymentId = FormDataService.defaultService().get(paymentRequestData, "id");
        PaymentMethodManipulator.performDeletePaymentMethod(request, null, paymentId);
    }

    public List<ValidationError> selectPaymentMethod(String paymentId, String actionName, HttpServletRequest request) throws FDResourceException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        ActionResult result = new ActionResult();

        FormDataRequest paymentRequestData;
        boolean paymentSaveAsDefault = false;
		try {
			paymentRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			/* expects string */
			paymentSaveAsDefault = Boolean.parseBoolean(FormDataService.defaultService().get(paymentRequestData, "paymentSetAsDefault"));
		} catch (HttpErrorResponse e) {
			// TODO Auto-generated catch block
			LOGGER.debug("Exception occured in selectPaymentMethod()");
		}
        String billingRef = null; // TODO: needed? CORPORATE with zero payment
        // method.
        HttpSession session = request.getSession();
        PaymentMethodManipulator.setPaymentMethod(paymentId, billingRef, request, session, result, actionName, ((paymentSaveAsDefault)?"Y":"N"), false);
        for (ActionError error : result.getErrors()) {
            validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
        }
        if (validationErrors.isEmpty()) {
            PaymentMethodManipulator.applyCustomerCredits("selectPaymentMethod", (FDUserI) session.getAttribute(SessionName.USER), request.getSession(), false);
            session.removeAttribute(SessionName.CART_PAYMENT_SELECTION_DISABLED);
        }
        return validationErrors;
    }

    public List<ValidationError> setNoPaymentMethod(FDUserI user, HttpServletRequest request) throws FDResourceException {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();
        ActionResult result = new ActionResult();
        String billingRef = null; // TODO: needed? CORPORATE with zero payment
        String referencedOrder = null; // is this ok?
        boolean makeGoodOrder = false; // is this ok?
        PaymentMethodManipulator.setNoPaymentMethod(request.getSession(), user, user.getShoppingCart(), "setNoPaymentMethod", billingRef, referencedOrder, makeGoodOrder, result);

        for (ActionError error : result.getErrors()) {
            validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
        }
        // TODO anything else?
        return validationErrors;
    }

    public void deselectEbtPayment(FDUserI user, HttpSession session) {
        FDCartModel cart = user.getShoppingCart();
        ErpPaymentMethodI paymentMethod = cart.getPaymentMethod();
        if (cart.getDeliveryAddress() != null && paymentMethod != null && !user.isEbtAccepted() && EnumPaymentMethodType.EBT.equals(paymentMethod.getPaymentMethodType())) {
            cart.setPaymentMethod(null);
            session.setAttribute(SessionName.CART_PAYMENT_SELECTION_DISABLED, Boolean.TRUE);
        }
    }

    public PaymentEditData loadUserPaymentMethod(FDUserI user, String paymentId) {
        PaymentEditData paymentEditData = null;
        ErpPaymentMethodI payment = null;
        for (ErpPaymentMethodI paymentMethod : user.getPaymentMethods()) {
            if (paymentMethod.getPK().getId().equals(paymentId)) {
                payment = paymentMethod;
                break;
            }
        }
        if (payment != null) {
            paymentEditData = populatePaymentEditData(payment);
        }
        return paymentEditData;
    }

    private void sortPaymentMethods(FDUserI user, List<ErpPaymentMethodI> paymentMethods) throws FDResourceException {
        FDOrderI lastOrder = FDCustomerManager.getLastOrder(user.getIdentity(), user.getUserContext().getStoreContext().getEStoreId());
        ErpPaymentMethodI lastUsedPaymentMethod = null;
        if (lastOrder != null) {
            lastUsedPaymentMethod = lastOrder.getPaymentMethod();
            // This check is needed to handle removed payments
            if (paymentMethods.contains(lastUsedPaymentMethod)) {
                paymentMethods.remove(lastUsedPaymentMethod);
            } else {
                lastUsedPaymentMethod = null;
            }
        }
        sortPaymentMethodsByIdReserved(paymentMethods);
        if (lastUsedPaymentMethod != null) {
            paymentMethods.add(0, lastUsedPaymentMethod);
        }
    }

    private static final Comparator<ErpPaymentMethodI> PAYMENT_COMPARATOR_BY_ID = new Comparator<ErpPaymentMethodI>() {

        @Override
        public int compare(ErpPaymentMethodI o1, ErpPaymentMethodI o2) {
            Long id1 = Long.parseLong(o1.getPK().getId());
            Long id2 = Long.parseLong(o2.getPK().getId());
            return id1.compareTo(id2);
        }

    };

    private static final Comparator<ErpPaymentMethodI> PAYMENT_COMPARATOR_BY_ID_REVERSED = ComparatorChain
            .<ErpPaymentMethodI> reverseOrder(ComparatorChain.create(PAYMENT_COMPARATOR_BY_ID));

    public void sortPaymentMethodsByIdReserved(List<ErpPaymentMethodI> paymentMethods) {
        Collections.sort(paymentMethods, PAYMENT_COMPARATOR_BY_ID_REVERSED);
    }

    private PaymentEditData populatePaymentEditData(ErpPaymentMethodI paymentMethod) {
        PaymentEditData data = new PaymentEditData();
        data.setAbaRouteNumber(paymentMethod.getAbaRouteNumber());
        data.setApartment(paymentMethod.getApartment());
        if (paymentMethod.getBankAccountType() != null) {
            data.setBankAccountType(paymentMethod.getBankAccountType().getName());
        }
        data.setBankName(paymentMethod.getBankName());
        data.setBillingAddress1(paymentMethod.getAddress1());
        data.setBillingAddress2(paymentMethod.getAddress2());
        data.setBillingCity(paymentMethod.getCity());
        data.setBillingCountry(paymentMethod.getCountry());
        data.setBillingState(paymentMethod.getState());
        data.setBillingZipcode(paymentMethod.getZipCode());
        data.setCardBrand(paymentMethod.getCardType().getFdName());
        data.setCardHolderName(paymentMethod.getName());
        if (paymentMethod.getExpirationDate() != null) {
            data.setCardMonth(DateUtil.formatDateByMonth(paymentMethod.getExpirationDate()));
            data.setCardYear(DateUtil.formatDateByYear(paymentMethod.getExpirationDate()));
        }
        data.setCardNum(paymentMethod.getAccountNumber());
        data.setCsv(paymentMethod.getCVV());
        data.setId(paymentMethod.getPK().getId());
        data.seteWalletID(paymentMethod.geteWalletID());
        if (paymentMethod.getPaymentMethodType() != null) {
            data.setPaymentMethodType(paymentMethod.getPaymentMethodType().getName());
        }
        data.setPhone(paymentMethod.getBestNumberForBillingInquiries());
        return data;
    }

    public List<PaymentData> loadUserPaymentMethods(FDUserI user, HttpServletRequest request, List<ErpPaymentMethodI> paymentMethods) throws FDResourceException {
        List<PaymentData> paymentDatas = new ArrayList<PaymentData>();
//        List<ErpPaymentMethodI> paymentMethods = (List<ErpPaymentMethodI>) user.getPaymentMethods();
        if(null == paymentMethods){
        	paymentMethods = (List<ErpPaymentMethodI>) user.getPaymentMethods();
        }       
        paymentMethods = PaymentMethodManipulator.disconnectInvalidPayPalWallet(paymentMethods, request);
        if (user.isECheckRestricted()) {
			paymentMethods = PaymentMethodManipulator.removeEcheckAccounts(paymentMethods);
		}
        sortPaymentMethods(user, paymentMethods);
        String selectedPaymentId = null;
        Boolean cartPaymentSelectionDisabled = (Boolean) request.getSession().getAttribute(SessionName.CART_PAYMENT_SELECTION_DISABLED);
        List<ValidationError> selectionError = new ArrayList<ValidationError>();
        if (cartPaymentSelectionDisabled == null || !cartPaymentSelectionDisabled) {
        	if(vaidateSO3Payment(user,paymentMethods)){
        		selectedPaymentId=user.getCurrentStandingOrder().getPaymentMethodId();
        	}
        	else if((user.getShoppingCart() instanceof FDModifyCartModel) && paymentMethods.size() > 0 && null ==request.getAttribute("pageAction")){
        		selectedPaymentId= paymentMethods.get(0).getPK().getId();
        	}
        	else if (user.getShoppingCart().getPaymentMethod() == null || (FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user)
        					&& null ==request.getAttribute("pageAction")) && null== request.getSession().getAttribute("selectedPaymentId")) {
        		 user.refreshFdCustomer();
        		 // FIN-78
        		 if(user.getMasqueradeContext()!=null && user.getMasqueradeContext().getParentOrderId()!=null){
        			 FDOrderI orderInfo = FDCustomerManager.getOrder(user.getMasqueradeContext().getParentOrderId());
        			 if(orderInfo!=null){
        				 selectedPaymentId= orderInfo.getPaymentMethod()!=null?(orderInfo.getPaymentMethod().getPK()!=null?orderInfo.getPaymentMethod().getPK().getId():null):null;
        				 LOGGER.debug("ADD-ON order masquerade by "+user.getMasqueradeContext().getAgentId()+
        						 ", payment ID: " +selectedPaymentId+" is setted from parentOrder: "+orderInfo.getErpSalesId());
        			 }
        		 }else{
					if (!StandingOrderHelper.isSO3StandingOrder(user)) {
						selectedPaymentId = user.getFDCustomer().getDefaultPaymentMethodPK();
						LOGGER.debug("default payment method setted, paymentID: " + selectedPaymentId);
					}
        		 }
        		 selectionError = selectPaymentMethod(selectedPaymentId, PageAction.SELECT_PAYMENT_METHOD.actionName, request);
            } else {
                PrimaryKey paymentMethodPrimaryKey = user.getShoppingCart().getPaymentMethod().getPK();
                if (paymentMethodPrimaryKey != null) {
                    selectedPaymentId = paymentMethodPrimaryKey.getId();
                    request.getSession().setAttribute("selectedPaymentId", selectedPaymentId);
                }
            }
        }
        for (int i = 0; i < paymentMethods.size(); i++) {
        	ErpPaymentMethodI paymentMethod =paymentMethods.get(i);
        	if(!(paymentMethod instanceof ErpGiftCardModel)) {//exclude giftcards
	            PaymentData paymentData = createPaymentData(paymentMethods.get(i));
	            
                //set as default
                if (paymentData.getId().equals(user.getFDCustomer().getDefaultPaymentMethodPK())) {
                	paymentData.setDefault(true);
                } else {
                	paymentData.setDefault(false);
                }
                
	            if ((cartPaymentSelectionDisabled == null || !cartPaymentSelectionDisabled) && (paymentData.getId().equals(selectedPaymentId) || (selectedPaymentId == null && i == 0))
	                    && selectionError.isEmpty()) {
	                paymentData.setSelected(true);
	                
	            }
	            paymentDatas.add(paymentData);
        	}
        }
        // Update Shopping Cart if Wallet Cart is selected and available in session
        updateShoppingCartWithWalletCard(user, request, paymentMethods);
        return paymentDatas;
    }

    protected boolean vaidateSO3Payment(FDUserI user,
			List<ErpPaymentMethodI> paymentMethods) {
    	boolean isValidPayment=false;
        if(StandingOrderHelper.isSO3StandingOrder(user) && null!=user.getCurrentStandingOrder().getPaymentMethodId()){
        	for(ErpPaymentMethodI erpPaymentMethodI:paymentMethods){
        		if(erpPaymentMethodI.getPK().getId().equals(user.getCurrentStandingOrder().getPaymentMethodId())){
        			isValidPayment=true;
        			break;
        		}
        	}
        }
    	
    	return isValidPayment;
	}

	/**
     * @param user
     * @param request
     * @param paymentMethods
     */
    private void updateShoppingCartWithWalletCard(FDUserI user, HttpServletRequest request,List<ErpPaymentMethodI> paymentMethods){
    	String session_card = "";
		if(request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
			session_card = request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME).toString();
		}
		String selectedWalletCardId="";
		if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
			selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
		}
		
		if(session_card != null && selectedWalletCardId != null && session_card.equals(MP_EWALLET_CARD)){
			for (ErpPaymentMethodI paymentMethod: paymentMethods) {
				if(paymentMethod.getPK().getId().equals(selectedWalletCardId)){
					String trxnId = "";
					Object trxnIdObj = request.getSession().getAttribute(EWALLET_TXN_ID_ATTRIBUTE_NAME);
					if (trxnIdObj != null && trxnIdObj instanceof String) {
						trxnId = (String)trxnIdObj;
						paymentMethod.seteWalletTrxnId(trxnId);
					}
					user.getShoppingCart().setPaymentMethod(paymentMethod);
					break;
				}
			}
		}
    }
    
    public List<PaymentData> loadCartPayment(final FDCartI cart, final FDUserI user) {
        List<PaymentData> paymentDatas = new ArrayList<PaymentData>();
        ErpPaymentMethodI payment = cart.getPaymentMethod();
        PaymentData paymentData = createPaymentData(payment);
        paymentData.setSelected(true);
        paymentDatas.add(paymentData);
        return paymentDatas;
    }

    private PaymentData createPaymentData(final ErpPaymentMethodI payment) {
        PaymentData paymentData = new PaymentData();
        paymentData.setId(payment.getPK().getId());
        paymentData.setTitle(payment.getName());
        if (payment.getCardType() != null) {
            paymentData.setType(payment.getCardType().getDisplayName());
        }
        paymentData.setDebit(payment.isDebitCard());
        if (!EnumCardType.GCP.equals(payment.getCardType())) {
            paymentData.setNameOnCard(payment.getName());
            String maskedAccountNumber = payment.getMaskedAccountNumber();
            if (maskedAccountNumber != null) {
                if (8 < maskedAccountNumber.length()) {
                    maskedAccountNumber = maskedAccountNumber.substring(maskedAccountNumber.length() - 8);
                }
                paymentData.setAccountNumber(maskedAccountNumber);
            }
            paymentData.setBestNumber(payment.getBestNumberForBillingInquiries());
            if (payment.getExpirationDate() != null) {
                paymentData.setExpiration(DateUtil.getCreditCardExpiryDate(payment.getExpirationDate()));
            }
            paymentData.setAddress1(payment.getAddress1());
            paymentData.setAddress2(payment.getAddress2());
            paymentData.setApartment(payment.getApartment());
            paymentData.setCity(payment.getCity());
            paymentData.setZip(payment.getZipCode());
            paymentData.setState(payment.getState());
            paymentData.setAbaRouteNumber(payment.getAbaRouteNumber());
            if (payment.getBankAccountType() != null) {
                paymentData.setBankAccountType(payment.getBankAccountType().getDescription());
            }
            paymentData.setReferencedOrderId(payment.getReferencedOrder());
            paymentData.setPaymentTypeDescription(payment.getPaymentType().getDescription());
			paymentData.seteWalletID(payment.geteWalletID());
        	paymentData.setVendorEWalletID(payment.getVendorEWalletID());
        	paymentData.setMpLogoURL(FDStoreProperties.getMasterpassLogoURL());
            paymentData.setBankName(payment.getBankName());
            paymentData.setEmailID(payment.getEmailID());
        }
        return paymentData;
    }

    private void processErrors(List<ValidationError> validationErrors, ActionResult actionResult) {
        for (ActionError error : actionResult.getErrors()) {
            if (PaymentMethodName.TERMS.equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else if (EnumUserInfoName.BIL_ZIPCODE.getCode().equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else if (PaymentMethodName.CSV.equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else if (EnumUserInfoName.BIL_ADDRESS_1.getCode().equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else if (EnumUserInfoName.BIL_APARTMENT.getCode().equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else if (EnumUserInfoName.CARD_HOLDER.getCode().equals(error.getType())) {
                validationErrors.add(new ValidationError(error.getType(), error.getDescription()));
            } else {
                validationErrors.add(new ValidationError(PaymentMethodName.ACCOUNT_NUMBER, error.getDescription()));
            }
        }
    }

    private ErpPaymentMethodI parsePaymentMethodForm(FormDataRequest paymentRequestData, FDUserI user) {
        Map<String, String> formData = FormDataService.defaultService().getSimpleMap(paymentRequestData);
        String actionName = formData.get("action");
        String paymentId = formData.get("id");
        EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(formData.get(PaymentMethodName.PAYMENT_METHOD_TYPE));
        ErpPaymentMethodI paymentMethod = null;
        if (paymentId != null && !paymentId.isEmpty()) {
            for (ErpPaymentMethodI payment : user.getPaymentMethods()) {
                if (payment.getPK().getId().equals(paymentId)) {
                    paymentMethod = payment;
                }
            }
        } else {
            paymentMethod = PaymentManager.createInstance(paymentMethodType);
        }
        String month = formData.get(PaymentMethodName.CARD_EXP_MONTH);
        String year = formData.get(PaymentMethodName.CARD_EXP_YEAR);
        String cardType = formData.get(PaymentMethodName.CARD_BRAND);
        String accountNumber = formData.get(PaymentMethodName.ACCOUNT_NUMBER);
        String abaRouteNumber = formData.get(PaymentMethodName.ABA_ROUTE_NUMBER);
        String bankName = formData.get(PaymentMethodName.BANK_NAME);
        String bankAccountType = formData.get(PaymentMethodName.BANK_ACCOUNT_TYPE);
        String csv = formData.get(PaymentMethodName.CSV);
        if ("editPaymentMethod".equalsIgnoreCase(actionName) && EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
            accountNumber = paymentMethod.getAccountNumber();
        }
        Calendar expCal = new GregorianCalendar();
        if (EnumPaymentMethodType.CREDITCARD.equals(paymentMethod.getPaymentMethodType())) {
            SimpleDateFormat sf = new SimpleDateFormat("MMyyyy");
            Date date = sf.parse((month.trim().length() == 1 ? "0" + month.trim() : month.trim()) + year.trim(), new ParsePosition(0));
            expCal.setTime(date);
            expCal.set(Calendar.DATE, expCal.getActualMaximum(Calendar.DATE));
        } else if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()) && abaRouteNumber != null && !"".equals(abaRouteNumber)) {
            abaRouteNumber = StringUtils.leftPad(abaRouteNumber, 9, "0");
        }
        paymentMethod.setExpirationDate(expCal.getTime());
        paymentMethod.setName(formData.get(PaymentMethodName.ACCOUNT_HOLDER));
        if (accountNumber != null && !accountNumber.equals(paymentMethod.getMaskedAccountNumber())) {
            paymentMethod.setAccountNumber(PaymentMethodUtil.scrubAccountNumber(accountNumber));
        }
        paymentMethod.setCardType(EnumCardType.getCardType(cardType));
        paymentMethod.setBankAccountType(EnumBankAccountType.getEnum(bankAccountType));
        paymentMethod.setAbaRouteNumber(abaRouteNumber);
        paymentMethod.setBankName(bankName);
        paymentMethod.setAddress1(formData.get(EnumUserInfoName.BIL_ADDRESS_1.getCode()));
        paymentMethod.setAddress2(formData.get(EnumUserInfoName.BIL_ADDRESS_2.getCode()));
        paymentMethod.setApartment(formData.get(EnumUserInfoName.BIL_APARTMENT.getCode()));
        paymentMethod.setCity(formData.get(EnumUserInfoName.BIL_CITY.getCode()));
        paymentMethod.setState(formData.get(EnumUserInfoName.BIL_STATE.getCode()));
        paymentMethod.setZipCode(formData.get(EnumUserInfoName.BIL_ZIPCODE.getCode()));
        paymentMethod.setCountry(formData.get(EnumUserInfoName.BIL_COUNTRY.getCode()));
        
        paymentMethod.seteWalletID(formData.get(EnumUserInfoName.EWALLET_ID.getCode()));
        paymentMethod.setVendorEWalletID(formData.get(EnumUserInfoName.VENDOR_EWALLETID.getCode()));
        
        if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType())) {
            paymentMethod.setCountry("US");
        }
        paymentMethod.setCVV(csv);
        if (StringUtil.isEmpty(paymentMethod.getCustomerId()) && user.getIdentity() != null) {
            paymentMethod.setCustomerId(user.getIdentity().getErpCustomerPK());
        }
        paymentMethod.setBestNumberForBillingInquiries(formData.get("phone"));

        String terms = formData.get(PaymentMethodName.TERMS);
        if (EnumPaymentMethodType.ECHECK.equals(paymentMethod.getPaymentMethodType()) && "on".equals(terms) && !PaymentMethodUtil.hasECheckAccount(user.getIdentity())) {
            paymentMethod.setIsTermsAccepted(true);
        }

        return paymentMethod;
    }
}
