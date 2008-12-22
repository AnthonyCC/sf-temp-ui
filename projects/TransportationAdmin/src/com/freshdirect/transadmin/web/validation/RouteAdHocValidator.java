package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnRoute;

public class RouteAdHocValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnAdHocRoute.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnAdHocRoute model = (TrnAdHocRoute)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "routeNumber", "app.error.112", new Object[]{"Route Number"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "routeAmPm", "app.error.112", new Object[]{"Route Timing"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "description", "app.error.112", new Object[]{"description"},"required field");
			
		
		
	}
	

}
