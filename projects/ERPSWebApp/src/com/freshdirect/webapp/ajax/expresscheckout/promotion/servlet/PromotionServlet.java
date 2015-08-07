package com.freshdirect.webapp.ajax.expresscheckout.promotion.servlet;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.promotion.service.PromotionService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class PromotionServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 3581392785228447116L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        FormDataRequest promotionRequest = parseRequestData(request, FormDataRequest.class);
        try {
            PageAction pageAction = FormDataService.defaultService().getPageAction(promotionRequest);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse promotionResponse = FormDataService.defaultService().prepareFormDataResponse(promotionRequest, validationResult);
            switch (pageAction) {
                case APPLY_PROMOTION:
                    List<ValidationError> validationErrors = PromotionService.defaultService().applyPromotionCode(promotionRequest, (FDSessionUser) user, request.getSession());
                    validationResult.getErrors().addAll(validationErrors);
                    if (validationErrors.isEmpty()) {
                        promotionResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
                    }
                    break;
                case REMOVE_PROMOTION:
                    PromotionService.defaultService().removePromotion(request.getSession(), user);
                    promotionResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
                    break;
                default:
                    break;
            }
            promotionResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            writeResponseData(response, promotionResponse);
        } catch (FDResourceException e) {
            returnHttpError(500, MessageFormat.format("Failed to apply promotion code for user[{0}].", user.getIdentity().getErpCustomerPK()), e);
        } catch (JspException exception) {
            returnHttpError(500, MessageFormat.format("Failed to update cart for user[{0}] after applying promotion code[{1}]", user.getIdentity().getErpCustomerPK(),
                    promotionRequest.getFormData().get(PromotionService.PROMOTION_CODE_FIELD_ID)), exception);
        } catch (IOException e) {
            returnHttpError(500, "Failed to load media for restriction message.", e);
        } catch (TemplateException e) {
            returnHttpError(500, "Failed to render  for restricion message.", e);
        } catch (RedirectToPage e) {
            returnHttpError(500, "Failed to load checkout page data due to technical difficulties.", e);
        }
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }
}
