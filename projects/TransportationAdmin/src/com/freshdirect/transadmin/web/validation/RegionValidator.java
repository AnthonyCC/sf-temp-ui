package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;

public class RegionValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Region.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Region model = (Region)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "code", "app.error.112", new Object[]{"Code"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "description", "app.error.112", new Object[]{"description"},"required field");
			
		
		
	}
	

}
