package com.freshdirect.webapp.ajax.expresscheckout.etip.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.etip.service.ETipService;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class ETipServlet extends BaseJsonServlet {
	
	private static final long serialVersionUID = -5048089104730660954L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		FormDataRequest eTipRequest = parseRequestData(request, FormDataRequest.class);
		
		try {
			PageAction pageAction = FormDataService.defaultService().getPageAction(eTipRequest);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse eTipResponse = FormDataService.defaultService().prepareFormDataResponse(eTipRequest, validationResult);
            
            List<ValidationError> validationErrors = ETipService.defaultService().applyETip(eTipRequest, (FDSessionUser) user, request.getSession());
            validationResult.getErrors().addAll(validationErrors);
            
            if(validationErrors.isEmpty()) {
            	eTipResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
            }
            
            eTipResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            writeResponseData(response, eTipResponse);
			
		} catch(Exception e) {
			
		}
	}

	@Override
	protected boolean synchronizeOnUser() {		
		return false;
	}

}
