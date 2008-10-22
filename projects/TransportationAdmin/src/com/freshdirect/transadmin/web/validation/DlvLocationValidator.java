package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;

import com.freshdirect.transadmin.model.DlvLocation;

public class DlvLocationValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DlvLocation.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DlvLocation model = (DlvLocation)obj;
		//  need to decide about the validation part
		//ValidationUtils.rejectIfEmpty(errors, "srubbedStreet", "app.error.112", new Object[]{"Street"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "apartment", "app.error.112", new Object[]{"Street"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "city", "app.error.112", new Object[]{"City"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "state", "app.error.112", new Object[]{"State"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "zip", "app.error.112", new Object[]{"Zipcode"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "country", "app.error.112", new Object[]{"Country"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "longitude", "app.error.112", new Object[]{"Longitude"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "latitude", "app.error.112", new Object[]{"Latitude"},"required field");		
		
	}
	

}
