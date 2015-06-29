package com.freshdirect.webapp.ajax.expresscheckout.restriction.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SinglePageCheckoutData;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.restriction.service.RestrictionService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.RedirectService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.util.FDEventUtil;

public class RestrictionServlet extends BaseJsonServlet {

	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest restrictionRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			String formId = restrictionRequestData.getFormId();
			String action = FormDataService.defaultService().get(restrictionRequestData, "action");
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(restrictionRequestData.getFormId());
			final FormDataResponse restrictionResponse = createRestrictionResponse(restrictionRequestData, validationResult);

			boolean cartChanged = false;
			if ("restriction_ageverification".equals(formId)) {
				if ("removeAlcohol".equalsIgnoreCase(action)) {
					user.getShoppingCart().removeAlcoholicLines();
					cartChanged = true;
				} else if ("applyAgeVerification".equals(action)) {
					RestrictionService.defaultService().applyAgeVerificationForAlcohol(user);
				}
			} else if ("restriction_outside_ny".equals(formId) && "removeAlcohol".equalsIgnoreCase(action)) {
				user.getShoppingCart().removeAlcoholicLines();
				cartChanged = true;
			} else if ("restriction_address".equals(formId) && "removeAlcohol".equalsIgnoreCase(action)) {
				user.getShoppingCart().removeAlcoholicLines();
				cartChanged = true;
			} else if ("restriction_pickup".equals(formId) && "removeWineAndSpirit".equalsIgnoreCase(action)) {
				user.getShoppingCart().removeWineAndSpiritLines();
				cartChanged = true;
			} else if ("restriction_timeslot".equals(formId) && "removeAlcohol".equalsIgnoreCase(action)) {
				user.getShoppingCart().removeAlcoholicLines();
				cartChanged = true;
			} else if ("restriction_ebt".equals(formId) && "removeEbtIneligibleItems".equalsIgnoreCase(action)) {
				FDCartModel cart = user.getShoppingCart();
				for (FDCartLineI cartLine : cart.getEbtIneligibleOrderLines()) {
					cart.removeOrderLineById(cartLine.getRandomId());
					// Create FD remove cart event.
					FDEventUtil.logRemoveCartEvent(cartLine, request);
				}
				cartChanged = true;
			}
			if (cartChanged) {
				CartData loadCartData = CartDataService.defaultService().loadCartData(request, user);
				restrictionResponse.getSubmitForm().getResult().put("cartData", SoyTemplateEngine.convertToMap(loadCartData));
			}
			SinglePageCheckoutData checkoutData = SinglePageCheckoutFacade.defaultFacade().load(user, request);
			restrictionResponse.getSubmitForm().getResult().put("restriction", checkoutData.getRestriction());
			restrictionResponse.getSubmitForm().getResult().put("atpFailure", checkoutData.getAtpFailure());
			String orderMinimumType = AvailabilityService.defaultService().selectAlcoholicOrderMinimumType(user, action);
			String redirectUrl = RedirectService.defaultService().populateRedirectUrl("/expressco/view_cart.jsp", "warning_message", orderMinimumType);
			restrictionResponse.getSubmitForm().getResult().put("redirectUrl", redirectUrl);

			restrictionResponse.getSubmitForm().setSuccess(restrictionResponse.getValidationResult().getErrors().isEmpty());
			writeResponseData(response, restrictionResponse);
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to submit restriction action.", e);
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

	private FormDataResponse createRestrictionResponse(final FormDataRequest restrictionRequest, final ValidationResult validationResult) {
		final FormDataResponse restrictionResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(restrictionRequest.getFormId());
		restrictionResponse.setFormSubmit(submitForm);
		restrictionResponse.setValidationResult(validationResult);
		return restrictionResponse;
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
