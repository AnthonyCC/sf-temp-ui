package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.RoutingOutCommand;

public class RoutingOutValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return RoutingOutCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		RoutingOutCommand model = (RoutingOutCommand)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "cutOff", "app.error.112", new Object[]{"Cut Off"},"required field");		
		
		if((model.getDepotRoutingFile() == null || model.getDepotRoutingFile().length == 0)
							&& (model.getTruckRoutingFile() == null || model.getTruckRoutingFile().length == 0)) {
			errors.rejectValue("truckRoutingFile", "app.error.112", new Object[]{"Truck or Depot File"},"required field");
			errors.rejectValue("depotRoutingFile", "app.error.112", new Object[]{"Truck or Depot File"},"required field");
		}
	}
	

}
