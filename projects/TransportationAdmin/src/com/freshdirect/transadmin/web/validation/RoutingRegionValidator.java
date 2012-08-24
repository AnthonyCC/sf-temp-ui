package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnRegion;

public class RoutingRegionValidator extends AbstractValidator {	
	
	
	public boolean supports(Class clazz) {
		return TrnRegion.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnRegion model = (TrnRegion)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "code", "app.error.112", new Object[]{"Region Code"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Region Name"},"required field");			
		
		validateLength("code", model.getCode(), 8, errors);
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);		
	
	}
}
