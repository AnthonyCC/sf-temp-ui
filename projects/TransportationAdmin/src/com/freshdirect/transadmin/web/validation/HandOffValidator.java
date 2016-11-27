package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.HandOffCommand;
import com.freshdirect.transadmin.web.model.RoutingMergeCommand;

public class HandOffValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return HandOffCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
						
		HandOffCommand model = (HandOffCommand)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "cutOff", "app.error.112", new Object[]{"Cut Off"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeScenario", "app.error.112", new Object[]{"Scenario"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "deliveryDate", "app.error.112", new Object[]{"Delivery Date"},"required field");		
	}
}