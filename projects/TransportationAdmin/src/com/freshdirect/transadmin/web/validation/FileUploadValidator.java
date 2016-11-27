package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.FileUploadCommand;

public class FileUploadValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return FileUploadCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
						
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeScenario", "app.error.112", new Object[]{"Scenario"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "file", "app.error.112", new Object[]{"File"},"required field");
		
	}
	

}