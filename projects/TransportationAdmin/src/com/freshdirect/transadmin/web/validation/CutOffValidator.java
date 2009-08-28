package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnCutOff;

public class CutOffValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnCutOff.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnCutOff model = (TrnCutOff)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "sequenceNo", "app.error.112", new Object[]{"Sequence No"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"CutOff Name"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "cutOffTime", "app.error.112", new Object[]{"CutOff Time"},"required field");
				
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);	
		
	}
}
