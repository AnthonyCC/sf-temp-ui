package com.freshdirect.webapp.ajax.expresscheckout.location.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.location.service.DeliveryAddressService;
import com.freshdirect.webapp.ajax.expresscheckout.payment.service.PaymentService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;

public class DeliveryAddressServlet extends BaseJsonServlet {

	private static final String ADDRESS_BY_ID_KEY = "address_by_id";

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			FormDataRequest deliveryAddressRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			PageAction pageAction = FormDataService.defaultService().getPageAction(deliveryAddressRequest);
			ValidationResult validationResult = new ValidationResult();
			FormDataResponse deliveryAddressResponse = FormDataService.defaultService().prepareFormDataResponse(deliveryAddressRequest, validationResult);

			if (pageAction != null) {
				switch (pageAction) {
				case GET_DELIVERY_ADDRESS_METHOD: {
					String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
					deliveryAddressResponse.getSubmitForm().getResult().put(ADDRESS_BY_ID_KEY, SinglePageCheckoutFacade.defaultFacade().loadAddressById(user, deliveryAddressId));
					break;
				}
				case ADD_DELIVERY_ADDRESS_METHOD: {
					List<ValidationError> validationErrors = DeliveryAddressService.defaultService().addDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					validationResult.getErrors().addAll(validationErrors);
					break;
				}
				case EDIT_DELIVERY_ADDRESS_METHOD: {
					List<ValidationError> validationErrors = DeliveryAddressService.defaultService().editDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					validationResult.getErrors().addAll(validationErrors);
					break;
				}
				case DELETE_DELIVERY_ADDRESS_METHOD: {
					DeliveryAddressService.defaultService().deleteDeliveryAddressMethod(deliveryAddressRequest, request.getSession(), user);
					break;
				}
				case SELECT_DELIVERY_ADDRESS_METHOD: {
					String deliveryAddressId = FormDataService.defaultService().get(deliveryAddressRequest, "id");
					String ebtPaymentRemovalApproved = FormDataService.defaultService().get(deliveryAddressRequest, "ebtPaymentRemovalApproved");
					List<ValidationError> validationErrors = new ArrayList<ValidationError>();
					ErpAddressModel deliveryAddress = user.getShoppingCart().getDeliveryAddress();
					if (ebtPaymentRemovalApproved == null && deliveryAddressId != null && deliveryAddress != null && !deliveryAddressId.equals(deliveryAddress)) {
						validationErrors.addAll(DeliveryAddressService.defaultService().checkEbtAddressPaymentSelectionByAddressId(user, deliveryAddressId));
					}
					if (validationErrors.isEmpty()) {
						String pickupPhone = FormDataService.defaultService().get(deliveryAddressRequest, deliveryAddressId +  "_phone");
						validationErrors = DeliveryAddressService.defaultService().selectDeliveryAddressMethod(deliveryAddressId, pickupPhone, pageAction.actionName, request.getSession(), user);
						PaymentService.defaultService().deselectEbtPayment(user, request.getSession());
					}
					validationResult.getErrors().addAll(validationErrors);
				}
				default:
					break;
				}
				if (!PageAction.GET_DELIVERY_ADDRESS_METHOD.equals(pageAction)) {
					deliveryAddressResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
				}
			}
			deliveryAddressResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
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

}
