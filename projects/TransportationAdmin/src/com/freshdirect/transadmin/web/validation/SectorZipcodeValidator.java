package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.SectorZipcode;

public class SectorZipcodeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return SectorZipcode.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		SectorZipcode model = (SectorZipcode) obj;
						
		ValidationUtils.rejectIfEmpty(errors, "zipcode", "app.error.112", new Object[]{"Zipcode"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "sector", "app.error.112", new Object[]{"Sector"},"required field");
		
		if(model != null && model.getSector() == null){
			errors.rejectValue("sector", "app.error.112", new Object[]{"Sector"},"required field");
		}
	
	}
}