package com.freshdirect.webapp.ajax.expresscheckout.location.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;

public class DeliveryAddressServlet extends BaseJsonServlet {

	private static final String ADDRESS_BY_ID_KEY = "address_by_id";

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			FormDataRequest deliveryAddressRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			String action = FormDataService.defaultService().get(deliveryAddressRequest, "action");
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(deliveryAddressRequest.getFormId());
			FormDataResponse deliveryAddressResponse = createDeliveryAddressResponse(deliveryAddressRequest, validationResult);

			boolean cartChanged = false;
			if ("getDeliveryAddressMethod".equals(action)) {
				String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
				deliveryAddressResponse.getSubmitForm().getResult().put(ADDRESS_BY_ID_KEY, SinglePageCheckoutFacade.defaultFacade().loadAddressById(user, deliveryAddressId));
			} else {
				if ("addDeliveryAddressMethod".equals(action)) {
					List<ValidationError> validationErrors = DeliveryAddressService.defaultService().addDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					validationResult.getErrors().addAll(validationErrors);
					cartChanged = true;
				} else if ("editDeliveryAddressMethod".equals(action)) {
					List<ValidationError> validationErrors = DeliveryAddressService.defaultService().editDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					validationResult.getErrors().addAll(validationErrors);
					cartChanged = true;
				} else if ("deleteDeliveryAddressMethod".equals(action)) {
					DeliveryAddressService.defaultService().deleteDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					cartChanged = true;
				} else if ("selectDeliveryAddressMethod".equals(action)) {
					String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
					String ebtPaymentRemovalApproved = FormDataService.defaultService().get(deliveryAddressRequest, "ebtPaymentRemovalApproved");
					List<ValidationError> validationErrors = new ArrayList<ValidationError>();
					if (ebtPaymentRemovalApproved == null) {
						validationErrors.addAll(DeliveryAddressService.defaultService().checkEbtAddressPaymentSelection(user, deliveryAddressId));
					}
					if (validationErrors.isEmpty()) {
						String pickupPhone = FormDataService.defaultService().get(deliveryAddressRequest, deliveryAddressId +  "_phone");
						validationErrors = DeliveryAddressService.defaultService().selectDeliveryAddressMethod(deliveryAddressId, pickupPhone, action, request.getSession(), user);
						PaymentService.defaultService().deselectEbtPayment(user, request.getSession());
						cartChanged = true;
					}
					validationResult.getErrors().addAll(validationErrors);
				}
				deliveryAddressResponse.getSubmitForm().setResult(SoyTemplateEngine.convertToMap(SinglePageCheckoutFacade.defaultFacade().load(user, request)));
			}
			deliveryAddressResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
			if (cartChanged && validationResult.getErrors().isEmpty()) {
				CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
				deliveryAddressResponse.getSubmitForm().getResult().put("cartData", SoyTemplateEngine.convertToMap(loadCartData));
			}
			writeResponseData(response, deliveryAddressResponse);
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to submit delivery address action.", e);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
		} catch (JspException e) {
			returnHttpError(500, "Failed to check delivery pass status.", e);
		} catch (RedirectToPage e) {
			returnHttpError(500, "Failed to load checkout page data due to technical difficulties.", e);
		}
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.SIGNED_IN;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	private FormDataResponse createDeliveryAddressResponse(FormDataRequest deliveryAddressRequest, ValidationResult validationResult) throws FDResourceException {
		final FormDataResponse deliveryAddressResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(deliveryAddressRequest.getFormId());
		deliveryAddressResponse.setFormSubmit(submitForm);
		deliveryAddressResponse.setValidationResult(validationResult);
		return deliveryAddressResponse;
	}

}
