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
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.checkout.service.CheckoutService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormRestriction;
import com.freshdirect.webapp.ajax.expresscheckout.restriction.service.RestrictionService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.util.FDEventUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class RestrictionServlet extends BaseJsonServlet {


	private static final long serialVersionUID = -7582639712245761241L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {
		try {
			if (!StandingOrderHelper.isSO3StandingOrder(user)) {
				FormRestriction restriction = CheckoutService.defaultService().preCheckOrder(user);
				writeResponseData(response, restriction);
			}
		} catch (FDResourceException e) {
			returnHttpError(500, "Failed to load restriction.", e);
		} catch (IOException e) {
			returnHttpError(500, "Failed to load restriction.", e);
		} catch (TemplateException e) {
			returnHttpError(500, "Failed to load restriction.", e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		try {
			final FormDataRequest restrictionRequestData = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
			String formId = restrictionRequestData.getFormId();
			PageAction action = FormDataService.defaultService().getPageAction(restrictionRequestData);
			ValidationResult validationResult = new ValidationResult();
			final FormDataResponse restrictionResponse = FormDataService.defaultService().prepareFormDataResponse(restrictionRequestData, validationResult);
			if ("restriction_ageverification".equals(formId)) {
				if (PageAction.REMOVE_ALCOHOL_FROM_CART.equals(action)) {
					user.getShoppingCart().removeAlcoholicLines();
				} else if (PageAction.APPLY_AGE_VERIFICATION_FOR_ALCOHOL_IN_CART.equals(action)) {
					RestrictionService.defaultService().applyAgeVerificationForAlcohol(user);
				}
			} else if ("restriction_outside_ny".equals(formId) && PageAction.REMOVE_ALCOHOL_FROM_CART.equals(action)) {
				user.getShoppingCart().removeAlcoholicLines();
			} else if ("restriction_address".equals(formId) && PageAction.REMOVE_ALCOHOL_FROM_CART.equals(action)) {
				user.getShoppingCart().removeAlcoholicLines();
			} else if ("restriction_pickup".equals(formId) && PageAction.REMOVE_WINE_AND_SPIRITS_FROM_CART.equals(action)) {
				user.getShoppingCart().removeWineAndSpiritLines();
			} else if ("restriction_timeslot".equals(formId) && PageAction.REMOVE_ALCOHOL_FROM_CART.equals(action)) {
				user.getShoppingCart().removeAlcoholicLines();
			} else if ("restriction_ebt".equals(formId) && PageAction.REMOVE_EBT_INELIGIBLE_ITEMS_FROM_CART.equals(action)) {
				FDCartModel cart = user.getShoppingCart();
				for (FDCartLineI cartLine : cart.getEbtIneligibleOrderLines()) {
					cart.removeOrderLineById(cartLine.getRandomId());
					// Create FD remove cart event.
					FDEventUtil.logRemoveCartEvent(cartLine, request);
				}
			}
			restrictionResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, action, validationResult));
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

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
