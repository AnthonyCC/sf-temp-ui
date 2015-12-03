package com.freshdirect.webapp.ajax.expresscheckout.csr.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.csr.service.CustomerServiceRepresentativeService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;

public class CustomerServiceRepresentativeServlet extends BaseJsonServlet {

    private static final long serialVersionUID = 1756532198006179724L;

    private static final String TECHNICAL_DIFFICULTY_MESSAGE_KEY = "technical_difficulty";
    private static final String TECHNICAL_DIFFICULTY_MESSAGE_TEXT = "Could not load checkout data due to technical difficulty.";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        FormDataRequest csrRequest = parseRequestData(request, FormDataRequest.class);
        ValidationResult validationResult = new ValidationResult();
        FormDataResponse csrResponse = FormDataService.defaultService().prepareFormDataResponse(csrRequest, validationResult);
        CustomerServiceRepresentativeService.defaultService().updateCustomerServiceRepresentativeInfo(csrRequest, user);
        if (validationResult.getErrors().isEmpty()) {
            try {
                Map<String, Object> checkoutData = SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, PageAction.APPLY_CSR_METHOD, validationResult);
                csrResponse.getSubmitForm().setResult(checkoutData);
            } catch (FDResourceException e) {
                validationResult.getErrors().add(new ValidationError(TECHNICAL_DIFFICULTY_MESSAGE_KEY, TECHNICAL_DIFFICULTY_MESSAGE_TEXT));
            } catch (JspException e) {
                validationResult.getErrors().add(new ValidationError(TECHNICAL_DIFFICULTY_MESSAGE_KEY, TECHNICAL_DIFFICULTY_MESSAGE_TEXT));
            } catch (RedirectToPage e) {
                validationResult.getErrors().add(new ValidationError(TECHNICAL_DIFFICULTY_MESSAGE_KEY, TECHNICAL_DIFFICULTY_MESSAGE_TEXT));
            } catch (IOException e) {
                validationResult.getErrors().add(new ValidationError(TECHNICAL_DIFFICULTY_MESSAGE_KEY, TECHNICAL_DIFFICULTY_MESSAGE_TEXT));
            } catch (TemplateException e) {
                validationResult.getErrors().add(new ValidationError(TECHNICAL_DIFFICULTY_MESSAGE_KEY, TECHNICAL_DIFFICULTY_MESSAGE_TEXT));
            }
        }
        csrResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
        writeResponseData(response, csrResponse);
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

}
