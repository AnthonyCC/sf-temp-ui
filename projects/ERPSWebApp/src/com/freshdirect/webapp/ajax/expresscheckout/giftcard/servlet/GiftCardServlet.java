package com.freshdirect.webapp.ajax.expresscheckout.giftcard.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.giftcard.service.GiftCardService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
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
        PageAction pageAction = FormDataService.defaultService().getPageAction(data);
        try {
            final FormDataResponse responseData = FormDataService.defaultService().prepareFormDataResponse(data, validationResult);
            if (PageAction.APPLY_GIFT_CARD.equals(pageAction)) {
                HttpSession session = request.getSession();
                FDActionInfo info = AccountActivityUtil.getActionInfo(session);
                String customerServiceContact = UserUtil.getCustomerServiceContact(request);
                List<ValidationError> validationErrors = GiftCardService.defaultService().applyGiftCard(data, (FDSessionUser) user, info, customerServiceContact);
                validationResult.getErrors().addAll(validationErrors);
            } else if (PageAction.REMOVE_GIFT_CARD.equals(pageAction)) {
                GiftCardService.defaultService().removeGiftCard(user);
            }
            responseData.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            if (responseData.getSubmitForm().isSuccess()) {
                Map<String, Object> singlePageCheckoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult);
                responseData.getSubmitForm().setResult(singlePageCheckoutData);
            }
            writeResponseData(response, responseData);
        } catch (JspException exception) {
            returnHttpError(
                    500,
                    MessageFormat.format("Failed to update cart for user[{0}] after applying gift card[{1}]", user.getIdentity().getErpCustomerPK(),
                            data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), exception);
        } catch (FDResourceException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] gift card[{1}]", pageAction.actionName, data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), e);
        } catch (IOException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] gift card[{1}]", pageAction.actionName, data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), e);
        } catch (TemplateException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] gift card[{1}]", pageAction.actionName, data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), e);
        } catch (RedirectToPage e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] gift card[{1}]", pageAction.actionName, data.getFormData().get(GiftCardService.GIVEX_NUM_FIELD_ID)), e);
        }
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }
}
