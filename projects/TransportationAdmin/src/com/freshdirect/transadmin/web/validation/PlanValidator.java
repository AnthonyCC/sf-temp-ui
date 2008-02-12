package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;
import java.util.Calendar;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.util.TransStringUtil;

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
		
		boolean hasTimeSlots = true;
		if(model != null && (model.getTrnTimeslot() == null || model.getTrnTimeslot().getSlotId() == null)) {
			errors.rejectValue("timeslot", "app.error.112", new Object[]{"Start Time Slot"},"required field");
			hasTimeSlots = false;
		}
		
		if(model != null && (model.getTrnEndTimeslot() == null || model.getTrnEndTimeslot().getSlotId() == null)) {
			errors.rejectValue("endTimeslot", "app.error.112", new Object[]{"End Time Slot"},"required field");
			hasTimeSlots = false;
		}
				
		if(hasTimeSlots) {
			checkDate("timeslot", model.getTrnTimeslot().getSlotId(), model.getTrnEndTimeslot().getSlotId(), errors);
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
	
	private void checkDate(String field, String startTime, String endTime, Errors errors) {
		
		try {
			if(TransStringUtil.compareTime(startTime, endTime)) {
				errors.rejectValue(field, "app.error.116", new Object[]{},"Invalid Time");
			}
		} catch(NumberFormatException exp) {
			errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Invalid Time");			
		}
		
	}

}
