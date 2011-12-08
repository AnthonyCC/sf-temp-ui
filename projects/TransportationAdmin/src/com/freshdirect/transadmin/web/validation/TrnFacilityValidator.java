package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnFacility;

public class TrnFacilityValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnFacility.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnFacility model = (TrnFacility)obj;
		
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "description", "app.error.112", new Object[]{"Description"},"required field");
				
		if(model != null && (model.getTrnFacilityType() == null || model.getTrnFacilityType() == null) || "null".equals(model.getTrnFacilityType())) {
			errors.rejectValue("trnFacilityType", "app.error.112", new Object[]{"Facility Type"},"required field");
		}
				
		if(model != null && model.getName()!= null && String.valueOf(model.getName()).trim().length() >= 10) {
			errors.rejectValue("name", "app.error.147", new Object[]{"Facility code"},"");
		}
		
		
	}
	

}
