package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnDispatchPlan;

public class PlanValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return TrnDispatchPlan.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnDispatchPlan model = (TrnDispatchPlan)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "planDate", "app.error.112", new Object[]{"Plan Date"},"required field");
				
				
		if(model != null && (model.getTrnZone() == null || model.getTrnZone().getZoneId() == null)) {
			errors.rejectValue("zone", "app.error.112", new Object[]{"Zone"},"required field");
		}
		
		if(model != null && (model.getTrnTimeslot() == null || model.getTrnTimeslot().getSlotId() == null)) {
			errors.rejectValue("timeslot", "app.error.112", new Object[]{"Time Slot"},"required field");
		}
		
		/*if(model != null && (model.getTrnDriver() == null || model.getTrnDriver().getEmployeeId() == null)) {
			errors.rejectValue("driver", "app.error.112", new Object[]{"Driver"},"required field");
		}
		
		if(model != null && (model.getTrnPrimaryHelper() == null || model.getTrnPrimaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("primaryHelper", "app.error.112", new Object[]{"Helper1"},"required field");
		}
		
		if(model != null && (model.getTrnSecondaryHelper() == null || model.getTrnSecondaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("secondaryHelper", "app.error.112", new Object[]{"Helper2"},"required field");
		}*/
		
	}

}
