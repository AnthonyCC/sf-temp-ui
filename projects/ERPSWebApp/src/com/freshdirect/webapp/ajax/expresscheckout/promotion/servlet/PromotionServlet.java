package com.freshdirect.webapp.ajax.expresscheckout.promotion.servlet;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.promotion.service.PromotionService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class PromotionServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 3581392785228447116L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		FormDataRequest promotionRequestData = parseRequestData(request, FormDataRequest.class);
		try {
			if ("applyPromotion".equals(promotionRequestData.getFormData().get("action"))) {
				List<ValidationError> validationErrors = PromotionService.defaultService().applyPromotionCode(promotionRequestData, (FDSessionUser) user, request.getSession());
				loadUpdatedCartAndPopulateResponse(request, response, user, promotionRequestData, validationErrors);
			} else if ("removePromotion".equals(promotionRequestData.getFormData().get("action"))) {
				PromotionService.defaultService().removePromotion(request.getSession(), user);
				loadUpdatedCartAndPopulateResponse(request, response, user, promotionRequestData, Collections.<ValidationError>emptyList());
			}
		} catch (FDResourceException e) {
			returnHttpError(500, MessageFormat.format("Failed to apply promotion code for user[{0}].", user.getIdentity().getErpCustomerPK()), e);
		} catch (JspException exception) {
			returnHttpError(
					500,
					MessageFormat.format("Failed to update cart for user[{0}] after applying promotion code[{1}]", user.getIdentity().getErpCustomerPK(),
							promotionRequestData.getFormData().get(PromotionService.PROMOTION_CODE_FIELD_ID)), exception);
		}
	}
	
	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	private void loadUpdatedCartAndPopulateResponse(HttpServletRequest request, HttpServletResponse response, FDUserI user, FormDataRequest promotionRequestData, List<ValidationError> validationErrors)
			throws HttpErrorResponse, FDResourceException, JspException {
		FormDataResponse responseData = new FormDataResponse();
		SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(promotionRequestData.getFormId());
		submitForm.setSuccess(validationErrors.isEmpty());
		if (validationErrors.isEmpty()) {
			CartData cartData = CartDataService.defaultService().loadCartData(request, user);
			submitForm.getResult().put("subTotalBox", cartData.getSubTotalBox());
		}
		responseData.setFormSubmit(submitForm);
		ValidationResult validationResult = new ValidationResult();
		validationResult.setErrors(validationErrors);
		validationResult.setFdform(promotionRequestData.getFormId());
		responseData.setValidationResult(validationResult);
		writeResponseData(response, responseData);
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
