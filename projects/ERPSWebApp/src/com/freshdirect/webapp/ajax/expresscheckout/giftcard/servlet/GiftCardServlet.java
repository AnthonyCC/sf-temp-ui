package com.freshdirect.webapp.ajax.expresscheckout.giftcard.servlet;

import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.service.CartDataService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.giftcard.service.GiftCardService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class GiftCardServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8731504475637344130L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		FormDataRequest data = parseRequestData(request, FormDataRequest.class);
		ValidationResult validationResult = new ValidationResult();
		validationResult.setFdform(data.getFormId());
		String action = FormDataService.defaultService().get(data, "action");
		String actionName = null;
		try {
			if ("applyGiftCard".equals(action)) {
				actionName = "apply";
				HttpSession session = request.getSession();
				FDActionInfo info = AccountActivityUtil.getActionInfo(session);
				String customerServiceContact = UserUtil.getCustomerServiceContact(request);
				List<ValidationError> validationErrors = GiftCardService.defaultService().applyGiftCard(data, (FDSessionUser) user, info, customerServiceContact);
				validationResult.getErrors().addAll(validationErrors);
				loadUpdatedCartAndPopulateResponse(request, response, user, data, validationResult);
			} else if ("removeGiftCard".equals(action)) {
				actionName = "remove";
				GiftCardService.defaultService().removeGiftCard(user);
				loadUpdatedCartAndPopulateResponse(request, response, user, data, validationResult);
			}
		} catch (JspException exception) {
			returnHttpError(
					500,
					MessageFormat.format("Failed to update cart for user[{0}] after applying gift card[{1}]", user.getIdentity().getErpCustomerPK(),
							data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), exception);
		} catch (FDResourceException e) {
			returnHttpError(500, MessageFormat.format("Failed to [{0}] gift card[{1}]", actionName, data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), e);
		}
	}

	private void loadUpdatedCartAndPopulateResponse(HttpServletRequest request, HttpServletResponse response, FDUserI user, FormDataRequest data, ValidationResult validationResult)
			throws HttpErrorResponse, FDResourceException, JspException {
		FormDataResponse responseData = new FormDataResponse();
		SubmitForm submitForm = new SubmitForm();
		submitForm.setSuccess(validationResult.getErrors().isEmpty());
		submitForm.setFormId(data.getFormId());
		if (validationResult.getErrors().isEmpty()) {
			CartData cartData = CartDataService.defaultService().loadCartData(request, user);
			submitForm.getResult().put("subTotalBox", cartData.getSubTotalBox());
		}
		responseData.setFormSubmit(submitForm);
		responseData.setValidationResult(validationResult);
		writeResponseData(response, responseData);
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
