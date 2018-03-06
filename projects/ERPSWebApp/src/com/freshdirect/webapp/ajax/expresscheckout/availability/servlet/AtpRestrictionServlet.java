package com.freshdirect.webapp.ajax.expresscheckout.availability.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.checkout.UnavailabilityPopulator;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.availability.service.AvailabilityService;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.service.SinglePageCheckoutFacade;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class AtpRestrictionServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 8187391559285687167L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user)
			throws HttpErrorResponse {

		if (FDStoreProperties.getAtpAvailabiltyMockEnabled()) {
			UnavailabilityData atpFailureData = UnavailabilityPopulator.createUnavailabilityData((FDSessionUser) user);
			if (!atpFailureData.getNonReplaceableLines().isEmpty() || !atpFailureData.getReplaceableLines().isEmpty()
					|| atpFailureData.getNotMetMinAmount() != null || !atpFailureData.getPasses().isEmpty()) {
				writeResponseData(response, atpFailureData);
			} else {
				writeResponseData(response, null);
			}
		} else {
			writeResponseData(response, null);
		}

	}
	
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            FormDataRequest atpCheckRequest = BaseJsonServlet.parseRequestData(request, FormDataRequest.class);
            PageAction pageAction = FormDataService.defaultService().getPageAction(atpCheckRequest);
            ValidationResult validationResult = new ValidationResult();
            FormDataResponse atpCheckResponse = FormDataService.defaultService().prepareFormDataResponse(atpCheckRequest, validationResult);
            List<String> removeCartLineIds = (List<String>) atpCheckRequest.getFormData().get("removableStockUnavailabilityCartLineIds");
            AvailabilityService.defaultService().adjustCartAvailability(request, removeCartLineIds, user);
            AvailabilityService.defaultService().checkCartAvailabilityAdjustResult(user);
            atpCheckResponse.getSubmitForm().setResult(SinglePageCheckoutFacade.defaultFacade().loadByPageAction(user, request, pageAction, validationResult));
            atpCheckResponse.getSubmitForm().setSuccess(validationResult.getErrors().isEmpty());
            writeResponseData(response, atpCheckResponse);
        } catch (FDResourceException e) {
            BaseJsonServlet.returnHttpError(500, "Failed to check ATP restrictions for user.");
        } catch (IOException e) {
            returnHttpError(500, "Failed to load Learn More media for restriction message.", e);
        } catch (TemplateException e) {
            returnHttpError(500, "Failed to render Learn More HTML media for restricion message.", e);
        } catch (JspException e) {
            returnHttpError(500, "Failed to check delivery pass status.", e);
        } catch (RedirectToPage e) {
            returnHttpError(500, "Failed to load checkout page data due to technical difficulties.", e);
        } catch (FDInvalidConfigurationException e) {
            returnHttpError(500, "Failed to refresh user content.", e);
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
