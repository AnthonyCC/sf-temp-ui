package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;


public class TruckPreferenceValidator extends AbstractValidator {

	public boolean supports(Class clazz) {
		return WebEmployeeInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		WebEmployeeInfo model = (WebEmployeeInfo)obj;
		ValidationUtils.rejectIfEmpty(errors, "empRole", "app.error.112", new Object[]{" Role Types"},"required field");

	}


}

