package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.RoutingMergeCommand;

public class RoutingMergeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return RoutingMergeCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
						
		RoutingMergeCommand model = (RoutingMergeCommand)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "cutOff", "app.error.112", new Object[]{"Cut Off"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "truckFile", "app.error.112", new Object[]{"Route Smart Truck"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "orderFile", "app.error.112", new Object[]{"Route Smart Order"},"required field");
		if((model.getDepotRoutingFile() == null || model.getDepotRoutingFile().length == 0)
				&& (model.getTruckRoutingFile() == null || model.getTruckRoutingFile().length == 0)) {
				errors.rejectValue("truckRoutingFile", "app.error.112", new Object[]{"Truck or Depot File"},"required field");
				errors.rejectValue("depotRoutingFile", "app.error.112", new Object[]{"Truck or Depot File"},"required field");
		}	
	}
}