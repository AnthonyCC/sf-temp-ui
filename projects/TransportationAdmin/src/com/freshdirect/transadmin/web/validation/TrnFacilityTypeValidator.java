package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnFacilityType;

public class TrnFacilityTypeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnFacilityType.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnFacilityType model = (TrnFacilityType)obj;
		
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "description", "app.error.112", new Object[]{"name"},"required field");
		
		
		
	}
	

}
