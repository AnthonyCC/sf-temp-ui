package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.web.model.DispatchSheetCommand;

public class PlanValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return DispatchSheetCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DispatchSheetCommand model = (DispatchSheetCommand)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "dispatchDay", "app.error.112", new Object[]{"Dispatch Day"},"required field");
				
		if(model != null && (model.getZoneId() == null)) {
			errors.rejectValue("zoneId", "app.error.112", new Object[]{"Zone"},"required field");
		}
		
		if(model != null && (model.getSlotId() == null)) {
			errors.rejectValue("slotId", "app.error.112", new Object[]{"Time Slot"},"required field");
		}
		
		if(model != null && (model.getDriverId() == null)) {
			errors.rejectValue("driverId", "app.error.112", new Object[]{"Driver"},"required field");
		}
				
		
	}

}
