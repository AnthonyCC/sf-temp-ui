package com.freshdirect.webapp.ajax.expresscheckout.etip.servlet;

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
import com.freshdirect.webapp.ajax.expresscheckout.etip.service.ETipService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;

public class ETipServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -5048089104730660954L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        FormDataRequest eTipRequest = parseRequestData(request, FormDataRequest.class);
        ValidationResult validationResult = new ValidationResult();
        validationResult.setFdform(eTipRequest.getFormId());
        PageAction pageAction = FormDataService.defaultService().getPageAction(eTipRequest);
        try {
            FormDataResponse eTipResponse = FormDataService.defaultService().prepareFormDataResponse(eTipRequest, validationResult);
            List<ValidationError> validationErrors;
            validationErrors = ETipService.defaultService().applyETip(eTipRequest, user, request.getSession());
            validationResult.getErrors().addAll(validationErrors);
            if (validationErrors.isEmpty()) {
                eTipResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
            }
            eTipResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            writeResponseData(response, eTipResponse);
        } catch (FDResourceException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] e tip [{1}]", pageAction.actionName, eTipRequest.getFormData().get(ETipService.ETIP_FIELD_CODE_ID)), e);
        } catch (IOException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] e tip [{1}]", pageAction.actionName, eTipRequest.getFormData().get(ETipService.ETIP_FIELD_CODE_ID)), e);
        } catch (TemplateException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] e tip [{1}]", pageAction.actionName, eTipRequest.getFormData().get(ETipService.ETIP_FIELD_CODE_ID)), e);
        } catch (JspException e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] e tip [{1}]", pageAction.actionName, eTipRequest.getFormData().get(ETipService.ETIP_FIELD_CODE_ID)), e);
        } catch (RedirectToPage e) {
            returnHttpError(500, MessageFormat.format("Failed to [{0}] e tip [{1}]", pageAction.actionName, eTipRequest.getFormData().get(ETipService.ETIP_FIELD_CODE_ID)), e);
        }
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

}
