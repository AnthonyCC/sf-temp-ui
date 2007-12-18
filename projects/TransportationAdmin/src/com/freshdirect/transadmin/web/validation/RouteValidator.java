package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnRoute;

public class RouteValidator implements Validator {	
	
	public boolean supports(Class clazz) {
		return TrnRoute.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnRoute model = (TrnRoute)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "routeNumber", "app.error.112", new Object[]{"Route Number"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "routeAmPm", "app.error.112", new Object[]{"Route Timing"},"required field");
						
		if(model != null && (model.getTrnZone() == null || model.getTrnZone().getZoneId() == null)) {
			errors.rejectValue("supervisor", "app.error.112", new Object[]{"Zone"},"required field");
		}
		
		if(model != null && (model.getTrnSupervisor() == null || model.getTrnSupervisor().getEmployeeId() == null)) {
			errors.rejectValue("supervisor", "app.error.112", new Object[]{"Supervisor"},"required field");
		}		
		
		
	}
	

}
