package com.freshdirect.transadmin.web.validation;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.CrisisManagerCommand;

public class CrisisManagerValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return CrisisManagerCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
						
		CrisisManagerCommand model = (CrisisManagerCommand)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "cutOff", "app.error.112", new Object[]{"Cut Off"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "destinationDate", "app.error.112", new Object[]{"Destination Date"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "selectedDate", "app.error.112", new Object[]{"Selected Date"},"required field");
		
		if(model != null && model.getSelectedDate() != null && model.getSelectedDate().before(new Date())) {
			errors.rejectValue("selectedDate", "app.error.142", new Object[]{"SelectedDate Date"},"can't be past date");
		}
		if(model != null && model.getSelectedDate() != null && model.getSelectedDate().equals(new Date())) {
			errors.rejectValue("selectedDate", "app.error.143", new Object[]{"SelectedDate Date"},"can't be current date");
		}
		if(model != null && model.getDestinationDate() != null && model.getSelectedDate()!= null && model.getDestinationDate().before(model.getSelectedDate())) {
			errors.rejectValue("destinationDate", "app.error.144", new Object[]{"Destination Date"},"can't be before selected date");
		}
	}
}