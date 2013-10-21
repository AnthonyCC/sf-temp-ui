package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;

import com.freshdirect.transadmin.model.Scrib;

public class ScribValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Scrib.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Scrib model = (Scrib)obj;
		if(model != null && model.getOriginFacility() == null) {
			errors.rejectValue("originFacility", "app.error.112", new Object[]{"Origin Facility"},"required field");
		}
		
		if(model != null && model.getDestinationFacility() == null) {
			errors.rejectValue("destinationFacility", "app.error.112", new Object[]{"Destination Facility"},"required field");
		}
		if (model.getStartTime() == null) {
			errors.rejectValue("startTimeS", "app.error.112", new Object[]{"Start Time"},"required field");
		}
		if (model.getDispatchGroup() == null) {
			errors.rejectValue("dispatchGroupS", "app.error.112", new Object[]{"Dispatch group time"},"required field");
		}
		if (model.getEndTime() == null) {
			errors.rejectValue("endTimeS", "app.error.112", new Object[]{"Truck end time"},"required field");
		}
		if (model.getScribDate() == null) {
			errors.rejectValue("scribDate", "app.error.112", new Object[]{"Date"},"required field");
		}
		if (model.getSupervisorCode() == null
				|| model.getSupervisorCode().trim().length() == 0) {
			errors.rejectValue("supervisorCode", "app.error.112", new Object[]{"Supervisor"},"required field");
		}
		if (model.getCutOffTime() == null) {
			errors.rejectValue("cutOffTimeS", "app.error.112", new Object[]{"CutOff Time"},"required field");
		}
		
		if(model.getStartTime() != null && model.getDispatchGroup() != null
				&& model.getStartTime().before(model.getDispatchGroup())) {
			errors.rejectValue("startTimeS", "app.error.152", "Invalid Time");
		}
		
		if(model.getStartTime() != null && model.getEndTime() != null && model.getEndTime().before(model.getStartTime())) {
			errors.rejectValue("endTimeS", "app.error.151", "Invalid Time");
		}
	}

}
