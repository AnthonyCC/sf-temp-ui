package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnZoneType;

public class ZoneTypeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnZoneType.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnZoneType model = (TrnZoneType)obj;
		//  need to decide about the validation part		
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Zone Type Name"},"required field");		
		
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);
	}
}
