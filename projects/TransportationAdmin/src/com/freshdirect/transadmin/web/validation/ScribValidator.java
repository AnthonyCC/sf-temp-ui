package com.freshdirect.transadmin.web.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;

import com.freshdirect.transadmin.model.Scrib;

public class ScribValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Scrib.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		List<ValidationInfo> errorList = validateEx(obj);
		
		for(ValidationInfo error : errorList){
			if(error.getLevel().equals(ValidationInfo.ERROR)){
				if(error.getArgs() == null){
					errors.rejectValue(error.getField(), error.getKey(), error.getDefaultMessage());
				}else{
					errors.rejectValue(error.getField(), error.getKey(), error.getArgs(),error.getDefaultMessage());
				}
			}
		}
	}
	
	public List<ValidationInfo> validateEx(Object obj) {
		
		List<ValidationInfo> errorList = new ArrayList<ValidationInfo>();
		Scrib model = (Scrib)obj;
		if(model != null && model.getOriginFacility() == null) {
			errorList.add(new ValidationInfo("originFacility", "app.error.112", new Object[]{"Origin Facility"},"required field", ValidationInfo.ERROR));
		}
		if(model != null && model.getDestinationFacility() == null) {
			errorList.add(new ValidationInfo("destinationFacility", "app.error.112", new Object[]{"Destination Facility"},"required field", ValidationInfo.ERROR));
		}
		if (model.getStartTime() == null) {
			errorList.add(new ValidationInfo("startTimeS", "app.error.112", new Object[]{"Truck Dispatch Time"},"required field", ValidationInfo.ERROR));
		}
		if (model.getDispatchGroup() == null) {
			errorList.add(new ValidationInfo("dispatchGroupS", "app.error.112", new Object[]{"Dispatch group time"},"required field", ValidationInfo.ERROR));
		}
		if (model.getEndTime() == null) {
			errorList.add(new ValidationInfo("endTimeS", "app.error.112", new Object[]{"Truck end time"},"required field", ValidationInfo.ERROR));
		}
		if (model.getScribDate() == null) {
			errorList.add(new ValidationInfo("scribDate", "app.error.112", new Object[]{"Date"},"required field", ValidationInfo.ERROR));
		}
		if (model.getSupervisorCode() == null
				|| model.getSupervisorCode().trim().length() == 0) {
			errorList.add(new ValidationInfo("supervisorCode", "app.error.112", new Object[]{"Supervisor"},"required field", ValidationInfo.ERROR));
		}
		if (model.getCutOffTime() == null) {
			errorList.add(new ValidationInfo("cutOffTimeS", "app.error.112", new Object[]{"CutOff Time"},"required field", ValidationInfo.ERROR));
		}
		
		if(model.getStartTime() != null && model.getDispatchGroup() != null
				&& model.getStartTime().before(model.getDispatchGroup())) {
			errorList.add(new ValidationInfo("startTimeS", "app.error.152", null, "Truck dispatch time cannot be before dispatch group time", ValidationInfo.ERROR));
		}
		
		if(model.getStartTime() != null && model.getEndTime() != null && model.getEndTime().before(model.getStartTime())) {
			errorList.add(new ValidationInfo("endTimeS", "app.error.151", null, "Truck end time cannot be before truck dispatch time", ValidationInfo.WARN));
		}
		
		return errorList;
	}

	

}
