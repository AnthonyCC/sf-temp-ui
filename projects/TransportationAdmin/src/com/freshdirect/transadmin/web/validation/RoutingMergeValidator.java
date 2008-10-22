package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.RoutingMergeCommand;

public class RoutingMergeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return RoutingMergeCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
						
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "cutOff", "app.error.112", new Object[]{"Cut Off"},"required field");		
		
	}
	

}