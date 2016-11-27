package com.freshdirect.webapp.ajax.expresscheckout.service;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.webapp.ajax.data.PageAction;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataResponse;
import com.freshdirect.webapp.ajax.expresscheckout.data.SubmitForm;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;

public class FormDataService {

	private static final FormDataService INSTANCE = new FormDataService();

	private FormDataService() {
	}

	public static FormDataService defaultService() {
		return INSTANCE;
	}

    public String get(FormDataRequest data, String key) {
        return (String) (data.getFormData() != null ? data.getFormData().get(key) : null);
	}
	
    public Boolean getBoolean(FormDataRequest data, String key) {
        return (Boolean) (data.getFormData() != null ? data.getFormData().get(key) : null);
    }

	public Map<String, String> getSimpleMap(FormDataRequest data) {
		Map<String, String> result = new HashMap<String, String>();
		for (String key : data.getFormData().keySet()) {
			result.put(key, get(data, key));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getComplexObject(FormDataRequest data, String key, Class<T> klass) {
		return (T) data.getFormData().get(key);
	}
	
	public PageAction getPageAction(FormDataRequest data) {
		String action = get(data, "action");
		return PageAction.parse(action);
	}
	
	public FormDataResponse prepareFormDataResponse(final FormDataRequest formDataRequest, final ValidationResult formValidation) {
		final FormDataResponse formDataResponse = new FormDataResponse();
		final SubmitForm submitForm = new SubmitForm();
		submitForm.setFormId(formDataRequest.getFormId());
		submitForm.setSuccess(formValidation.getErrors().isEmpty());
		formDataResponse.setFormSubmit(submitForm);
		formDataResponse.setValidationResult(formValidation);
		formValidation.setFdform(formDataRequest.getFormId());
		return formDataResponse;
	}
}
