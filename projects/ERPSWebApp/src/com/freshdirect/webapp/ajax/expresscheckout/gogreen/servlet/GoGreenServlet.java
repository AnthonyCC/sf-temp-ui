package com.freshdirect.webapp.ajax.expresscheckout.gogreen.servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.gogreen.service.GoGreenService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow;

public class GoGreenServlet extends BaseJsonServlet {

	private static final long serialVersionUID = 7603225763950961920L;
	 private static final Logger LOG = LoggerFactory.getInstance(CmsFilteringFlow.class);
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		FormDataRequest goGreenFormData = parseRequestData(request, FormDataRequest.class);
		try {
			GoGreenService.defaultService().saveGoGreenOption(goGreenFormData, user);
			FormDataResponse responseData = new FormDataResponse();
			SubmitForm submitForm = new SubmitForm();
			submitForm.setFormId(goGreenFormData.getFormId());
			submitForm.setSuccess(true);
			responseData.setFormSubmit(submitForm);
			ValidationResult validationResult = new ValidationResult();
			validationResult.setFdform(goGreenFormData.getFormId());
			responseData.setValidationResult(validationResult);
			writeResponseData(response, responseData);
		} catch (FDResourceException e) {
			  LOG.warn("Exception while Saving GoGreen Flag: ", e);
		}
		catch (Exception e) {
		  LOG.warn("Exception while Saving GoGreen Flag: ", e);
	 	}
	}

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}
}
