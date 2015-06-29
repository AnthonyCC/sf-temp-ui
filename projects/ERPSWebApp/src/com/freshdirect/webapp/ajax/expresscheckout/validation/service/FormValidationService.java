package com.freshdirect.webapp.ajax.expresscheckout.validation.service;

import java.util.List;

import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;

public interface FormValidationService {

	List<ValidationError> prepareAndValidate(FormDataRequest request);

}
