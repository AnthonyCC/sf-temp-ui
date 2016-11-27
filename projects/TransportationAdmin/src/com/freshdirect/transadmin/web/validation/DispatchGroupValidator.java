package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.DispatchGroup;

public class DispatchGroupValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DispatchGroup.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DispatchGroup model = (DispatchGroup) obj;
				
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Group Name"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "groupTime", "app.error.112", new Object[]{"Group Time"},"required field");
				
		validateLength("name", model.getName(), 32, errors);		
		
	}
}
