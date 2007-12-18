package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnZone;

public class ZoneValidator implements Validator {	
	
	public boolean supports(Class clazz) {
		return TrnZone.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnZone model = (TrnZone)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "zoneNumber", "app.error.112", new Object[]{"Zone Number"},"required field");
						
		if(model != null && (model.getTrnSupervisor() == null || model.getTrnSupervisor().getEmployeeId() == null)) {
			errors.rejectValue("supervisor", "app.error.112", new Object[]{"Supervisor"},"required field");
		}				
		
	}
	

}
