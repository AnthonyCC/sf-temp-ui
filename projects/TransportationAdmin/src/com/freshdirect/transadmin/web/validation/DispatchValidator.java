package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnDispatch;

public class DispatchValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return TrnDispatch.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnDispatch model = (TrnDispatch)obj;
		//  need to decide about the validation part
		//ValidationUtils.rejectIfEmpty(errors, "dispatchDate", "app.error.112", new Object[]{"Dispatch Day"},"required field");
				
		/*if(model != null && (model.getTrnZone() == null || model.getTrnZone().getZoneId() == null)) {
			errors.rejectValue("zone", "app.error.112", new Object[]{"Zone"},"required field");
		}*/
		
		if(model != null && (model.getTrnTimeslot() == null || model.getTrnTimeslot().getSlotId() == null)) {
			errors.rejectValue("timeslot", "app.error.112", new Object[]{"Time Slot"},"required field");
		}
		
		if(model != null && (model.getTrnDriver() == null || model.getTrnDriver().getEmployeeId() == null)) {
			errors.rejectValue("driver", "app.error.112", new Object[]{"Driver"},"required field");
		}
		
		/*if(model != null && (model.getTrnPrimaryHelper() == null || model.getTrnPrimaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("primaryHelper", "app.error.112", new Object[]{"Helper1"},"required field");
		}
		
		if(model != null && (model.getTrnSecondaryHelper() == null || model.getTrnSecondaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("secondaryHelper", "app.error.112", new Object[]{"Helper2"},"required field");
		}*/
		
		if(model != null && (model.getTrnSupervisor() == null || model.getTrnSupervisor().getEmployeeId() == null)) {
			errors.rejectValue("supervisor", "app.error.112", new Object[]{"Supervisor"},"required field");
		}
		
		if(model != null && (model.getTrnTruck() == null || model.getTrnTruck().getTruckId() == null)) {
			errors.rejectValue("truck", "app.error.112", new Object[]{"Truck"},"required field");
		}
		
		if(model != null && (model.getTrnRoute() == null || model.getTrnRoute().getRouteId() == null)) {
			errors.rejectValue("route", "app.error.112", new Object[]{"Route"},"required field");
		}
		
	}

}
