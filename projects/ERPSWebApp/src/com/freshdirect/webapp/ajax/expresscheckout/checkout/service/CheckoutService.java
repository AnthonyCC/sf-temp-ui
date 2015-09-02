package com.freshdirect.webapp.ajax.expresscheckout.checkout.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
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
import com.freshdirect.webapp.ajax.expresscheckout.restriction.service.RestrictionService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.coremetrics.CmConversionEventTag;
import com.freshdirect.webapp.taglib.coremetrics.CmShop9Tag;
import com.freshdirect.webapp.taglib.fdstore.CheckoutControllerTag;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CheckoutService {

	private static final CheckoutService INSTANCE = new CheckoutService();

	private static final Logger LOGGER = LoggerFactory.getInstance(CheckoutService.class);

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
			PageViewTagModel pvTagModel = new PageViewTagModel();
			pvTagModel.setCategoryId( CmContext.getContext().prefixedCategoryId( CustomCategory.CHECKOUT.toString()));
			pvTagModel.setPageId("unavailability");
			PageViewTagModelBuilder.decoratePageIdWithCatId(pvTagModel);
			atpFailureData.addCoremetrics(pvTagModel.toStringList());
            if (!atpFailureData.getNonReplaceableLines().isEmpty() || !atpFailureData.getReplaceableLines().isEmpty() || atpFailureData.getNotMetMinAmount() != null) {
                unavailabilityData = atpFailureData;
            }
		}
        return unavailabilityData;
	}

	public FormRestriction checkPlaceOrder(FDUserI user) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
		return RestrictionService.defaultService().verifyEbtPaymentRestriction(user);
	}

	public FormDataResponse submitOrder(FDUserI user, FormDataRequest requestData, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		FormDataResponse responseData = createResponseData(requestData);
		String actionName = FormDataService.defaultService().get(requestData, "action");
        String billingReference = FormDataService.defaultService().get(requestData, "billingReference");
        session.setAttribute(SessionName.PAYMENT_BILLING_REFERENCE, billingReference);
		boolean checkoutPageReloadNeeded = false;
		FormRestriction restriction = preCheckOrder(user);
		UnavailabilityData atpFailureData = null;
		if (restriction == null || restriction.isPassed()) {
			atpFailureData = applyAtpCheck(user);
		}
		FormRestriction checkPlaceOrderResult = null;
		ActionResult actionResult = new ActionResult();
		FDCartModel cart = user.getShoppingCart();
		List<ValidationError> checkEbtAddressPaymentSelectionError = DeliveryAddressService.defaultService().checkEbtAddressPaymentSelectionByZipCode(user, cart.getDeliveryAddress().getZipCode());
        if (restriction == null && atpFailureData == null && checkEbtAddressPaymentSelectionError.isEmpty()) {
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
					user.setSuspendShowPendingOrderOverlay(false);
					user.setShowPendingOrderOverlay(true);
					// prepare and store model for Coremetrics report
					CmConversionEventTag.buildPendingOrderModifiedModels(session, cart);
					CmShop9Tag.buildPendingModels(session, cart);
					((FDSessionUser) user).saveCart(true);
				}
				if (Action.SUCCESS.equalsIgnoreCase(outcome)) {
					String orderId = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
                    responseData.getSubmitForm().getResult().put(SinglePageCheckoutFacade.REDIRECT_URL_JSON_KEY, "/expressco/success.jsp?orderId=" + orderId);
					responseData.getSubmitForm().setSuccess(true);
					session.removeAttribute(SessionName.MODIFY_CART_PRESELECTION_COMPLETED);
                    session.removeAttribute(SessionName.PAYMENT_BILLING_REFERENCE);
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
}
