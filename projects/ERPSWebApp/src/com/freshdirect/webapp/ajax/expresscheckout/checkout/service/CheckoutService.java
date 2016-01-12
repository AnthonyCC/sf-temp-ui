package com.freshdirect.webapp.ajax.expresscheckout.checkout.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.Action;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.checkout.UnavailabilityPopulator;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormRestriction;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.PaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.restriction.service.RestrictionService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.DeliveryAddressManipulator;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility.PostAction;
import com.freshdirect.webapp.crm.util.MakeGoodOrderUtility.SessionParamGetter;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.coremetrics.CmConversionEventTag;
import com.freshdirect.webapp.taglib.coremetrics.CmShop9Tag;
import com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CheckoutService {

	private static final CheckoutService INSTANCE = new CheckoutService();

	private static final Logger LOGGER = LoggerFactory.getInstance(CheckoutService.class);
	
//	private static final String CUSTOMER_NOTIFY_STATUS = "Pending";
//	private static final String MASTERPASS_TRANSACTIONID="transactionId";
	private static final String EWALLET_SESSION_ATTRIBUTE_NAME="EWALLET_CARD_TYPE";
	private static final String MP_EWALLET_CARD="MP_CARD";
	private static final String WALLET_SESSION_CARD_ID="WALLET_CARD_ID";
	private final String EWALLET_ERROR_CODE = "WALLET_ERROR";



	private CheckoutService() {
	}

	public static CheckoutService defaultService() {
		return INSTANCE;
	}

	public FormRestriction preCheckOrder(FDUserI user) throws FDResourceException, IOException, TemplateException {
		FDCartModel cart = user.getShoppingCart();
		FormRestriction restriction = null;
		if (cart.containsAlcohol()) {
			restriction = RestrictionService.defaultService().verifyRestriction(user);
		}
		return restriction;
	}

	public UnavailabilityData applyAtpCheck(FDUserI user) throws FDResourceException {
        UnavailabilityData unavailabilityData = null;
		FDCartModel cart = user.getShoppingCart();
		if (cart.getDeliveryAddress() != null && cart.getDeliveryReservation() != null) {
            AvailabilityService.defaultService().checkCartAtpAvailability(user);
            UnavailabilityData atpFailureData = UnavailabilityPopulator.createUnavailabilityData((FDSessionUser) user);
            
            if (CmContextUtility.isCoremetricsAvailable(user)) {
				PageViewTagModel pvTagModel = new PageViewTagModel();
				pvTagModel.setCategoryId( CmContext.getContext().prefixedCategoryId( CustomCategory.CHECKOUT.toString()));
				pvTagModel.setPageId("unavailability");
				PageViewTagModelBuilder.decoratePageIdWithCatId(pvTagModel);
				atpFailureData.addCoremetrics(pvTagModel.toStringList());
            }
			
            if (!atpFailureData.getNonReplaceableLines().isEmpty() || !atpFailureData.getReplaceableLines().isEmpty() || atpFailureData.getNotMetMinAmount() != null) {
                unavailabilityData = atpFailureData;
            }
            else{
            	AvalaraContext avalaraContext = new AvalaraContext(cart);
            	avalaraContext.setCommit(false);
            	avalaraContext.setReturnTaxValue(cart.getAvalaraTaxValue(avalaraContext));
            }
		}
        return unavailabilityData;
	}
	
	public boolean applyETip(FDUserI user, String tipAmount, boolean isCustomTip) {				
		
		FDCartModel cart = user.getShoppingCart();
		
		if(tipAmount.startsWith("$")) {
			tipAmount = tipAmount.substring(1);
		}
		
		cart.setTip(Double.parseDouble(tipAmount));
		cart.setCustomTip(isCustomTip);
		
		return true;
	}

    public boolean checkAtpCheckEligibleByRestrictions(FormRestriction restriction) {
        return restriction == null || restriction.isPassed();
    }

	public FormRestriction checkPlaceOrder(FDUserI user) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
		return RestrictionService.defaultService().verifyEbtPaymentRestriction(user);
	}

	public FormDataResponse submitOrder(final FDUserI user, FormDataRequest requestData, final HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		FormDataResponse responseData = createResponseData(requestData);
		String actionName = FormDataService.defaultService().get(requestData, "action");
        String billingReference = FormDataService.defaultService().get(requestData, "billingReference");
        session.setAttribute(SessionName.PAYMENT_BILLING_REFERENCE, billingReference);
		boolean checkoutPageReloadNeeded = false;
		FormRestriction restriction = preCheckOrder(user);
		UnavailabilityData atpFailureData = null;
        if (checkAtpCheckEligibleByRestrictions(restriction)) {
			atpFailureData = applyAtpCheck(user);
		}
		FormRestriction checkPlaceOrderResult = null;
		ActionResult actionResult = new ActionResult();
		FDCartModel cart = user.getShoppingCart();
		List<ValidationError> checkEbtAddressPaymentSelectionError = DeliveryAddressService.defaultService().checkEbtAddressPaymentSelectionByZipCode(user, cart.getDeliveryAddress().getZipCode());
        DeliveryAddressManipulator.checkAddressRestriction(false, actionResult, cart.getDeliveryAddress());
        if (restriction == null && atpFailureData == null && checkEbtAddressPaymentSelectionError.isEmpty() && actionResult.isSuccess()) {
			checkPlaceOrderResult = checkPlaceOrder(user);
			if (checkPlaceOrderResult.isPassed()) {
                if (user.isCorporateUser()) {
                    cart.getPaymentMethod().setBillingRef(billingReference);
                }
				LOGGER.debug("AVAILABILITY IS: " + cart.getAvailability());
				String outcome = null;
				if (cart.isAvailabilityChecked()) {
                    outcome = CheckoutControllerTag.performSubmitOrder(user, actionName, actionResult, session, request, response, CheckoutControllerTag.AUTHORIZATION_CUTOFF_PAGE,
                            null, null, null);
                    // makegood phase
    				MasqueradeContext masqueradeContext = user.getMasqueradeContext();
    				String masqueradeMakeGoodOrderId = masqueradeContext==null ? null : masqueradeContext.getMakeGoodFromOrderId();
    				if ( masqueradeMakeGoodOrderId!=null ) {
    					MakeGoodOrderUtility.processComplaint(masqueradeContext,
							new SessionParamGetter(session),
							new PostAction() {
								@Override
								public void handle(MasqueradeContext masqueradeContext) {
									session.removeAttribute( "makeGoodOrder" );
									session.removeAttribute( "referencedOrder" );
									session.removeAttribute(SessionName.MAKEGOOD_COMPLAINT);
									
									if (masqueradeContext!=null){
										masqueradeContext.clearMakeGoodContext();
									}
								}
							},
							outcome);
    				}
					user.setSuspendShowPendingOrderOverlay(false);
					user.setShowPendingOrderOverlay(true);
					// prepare and store model for Coremetrics report
					//   EXCEPT for make-good sessions!
					if ( masqueradeMakeGoodOrderId == null ) {
						CmConversionEventTag.buildPendingOrderModifiedModels(session, cart);
						CmShop9Tag.buildPendingModels(session, cart);
					}
					((FDSessionUser) user).saveCart(true);
				}
				if (Action.SUCCESS.equalsIgnoreCase(outcome)) {
					String orderId = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
                    responseData.getSubmitForm().getResult().put(SinglePageCheckoutFacade.REDIRECT_URL_JSON_KEY, "/expressco/success.jsp?orderId=" + orderId);
					responseData.getSubmitForm().setSuccess(true);
					
					// Remove the EWallet Payment MEthod ID from session
					if(session.getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
						session.removeAttribute(EWALLET_SESSION_ATTRIBUTE_NAME);
					}if(session.getAttribute(WALLET_SESSION_CARD_ID) != null){
						session.removeAttribute(WALLET_SESSION_CARD_ID);
					}
					
					session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
                    session.removeAttribute(SessionName.PAYMENT_BILLING_REFERENCE);
                    session.setAttribute(SessionName.ORDER_SUBMITTED_FLAG_FOR_SEM_PIXEL, true);
				} else {
					checkoutPageReloadNeeded = true;
				}
			} else {
				checkoutPageReloadNeeded = true;
			}
		} else {
			checkoutPageReloadNeeded = true;
		}
		if (checkoutPageReloadNeeded) {
			SinglePageCheckoutData checkoutData = SinglePageCheckoutFacade.defaultFacade().load(user, request);
			
			removeOlderEwalletPaymentMethod(checkoutData.getPayment(),request);
			checkEWalletCard(checkoutData.getPayment(),request);
			
            checkoutData.setAtpFailure(atpFailureData);
			if (checkPlaceOrderResult != null && !checkPlaceOrderResult.isPassed()) {
				checkoutData.setRestriction(checkPlaceOrderResult);
			}
			responseData.getSubmitForm().setResult(SoyTemplateEngine.convertToMap(checkoutData));
			responseData.getSubmitForm().setSuccess(false);
            String paymentAuthorizationFailMessage = (String) session.getAttribute(SessionName.ORDER_AUTHORIZATION_FAILURE_MESSAGE);
            if (paymentAuthorizationFailMessage != null) {
                responseData.getValidationResult().getErrors().add(new ValidationError("orderSubmit", paymentAuthorizationFailMessage));
            }
            String orderAuthorizationCutoffFailRedirectUrl = (String) session.getAttribute(SessionName.ORDER_AUTHORIZATION_CUTOFF_FAILURE_REDIRECT_URL);
            if (orderAuthorizationCutoffFailRedirectUrl != null) {
                responseData.getSubmitForm().getResult().put(SinglePageCheckoutFacade.REDIRECT_URL_JSON_KEY, orderAuthorizationCutoffFailRedirectUrl);
            }
			for (ActionError error : actionResult.getErrors()) {
				responseData.getValidationResult().getErrors().add(new ValidationError("orderSubmit", error.getDescription()));
			}
			responseData.getValidationResult().getErrors().addAll(checkEbtAddressPaymentSelectionError);
		}
		return responseData;
	}

	private FormDataResponse createResponseData(FormDataRequest requestData) {
		FormDataResponse responseData = new FormDataResponse();
		SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(requestData.getFormId());
		responseData.setFormSubmit(submitForm);
		ValidationResult validationResult = new ValidationResult();
		validationResult.setFdform(requestData.getFormId());
		responseData.setValidationResult(validationResult);
		return responseData;
	}
	
		/**
		 * @param txNotifyModel
		 */
		/*private void updateIntoEWalletTxNotify(ErpEWalletTxNotifyModel txNotifyModel){
	    	FDCustomerManager.updateEWalletTxnNotify(txNotifyModel);
	    }
			*/
	
	/**
	 * @param formpaymentData
	 * @param request
	 */
	private void removeOlderEwalletPaymentMethod(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null) {
			List<PaymentData> payments = formpaymentData.getPayments();
			List<PaymentData> paymentsNew = new ArrayList<PaymentData>();
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			for (PaymentData data : payments) {
				if(data.geteWalletID() == null){
					paymentsNew.add(data);
				}else{
					int ewalletId = EnumEwalletType.getEnum("MP").getValue();
					if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
						paymentsNew.add(data);
					}
				}
			}
			formpaymentData.setPayments(paymentsNew);
		}
	}
	/**
	 * @param formpaymentData
	 * @param request
	 */
	private void checkEWalletCard(FormPaymentData formpaymentData,HttpServletRequest request){
		if (formpaymentData != null) {
			// Remove Error Message From session
			if(request.getSession().getAttribute(EWALLET_ERROR_CODE) != null ){
				request.getSession().removeAttribute(EWALLET_ERROR_CODE);
			}
			List<PaymentData> payments = formpaymentData.getPayments();
			String session_card = "";
			if(request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME) != null){
				session_card = request.getSession().getAttribute(EWALLET_SESSION_ATTRIBUTE_NAME).toString();
			}
			String selectedWalletCardId="";
			if(request.getSession().getAttribute(WALLET_SESSION_CARD_ID) != null ){
				selectedWalletCardId = request.getSession().getAttribute(WALLET_SESSION_CARD_ID).toString();
			}
			if(formpaymentData.getSelected() != null && formpaymentData.getSelected().equals(selectedWalletCardId)){
				for (PaymentData data : payments) {
					data.setMpLogoURL(FDStoreProperties.getMasterpassLogoURL());
					if( (session_card != null && session_card.equals(MP_EWALLET_CARD)) ){
						int ewalletId = EnumEwalletType.getEnum("MP").getValue();
						if(data.geteWalletID()!=null && data.geteWalletID().equals(""+ewalletId) && selectedWalletCardId.equals(data.getId())){
							data.setSelected(true);
							formpaymentData.setSelected(data.getId());
						}else{
							data.setSelected(false);
						}
					}
				}
			}
		}
	}
}
