package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.DlvServiceTimeType;

public class DlvServiceTimeTypeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DlvServiceTimeType.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DlvServiceTimeType model = (DlvServiceTimeType)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "code", "app.error.112", new Object[]{"Code"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Name"},"required field");	
		ValidationUtils.rejectIfEmpty(errors, "fixedServiceTime", "app.error.112", new Object[]{"Fixed Service Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "stopServiceTime", "app.error.112", new Object[]{"Stop Service Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "variableServiceTime", "app.error.112", new Object[]{"Variable Service Time"},"required field");

		
		validateLength("code", model.getCode(), 8, errors);
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);	
		validateNumericLength("fixedServiceTime", model.getFixedServiceTime(), errors);
		validateNumericLength("stopServiceTime", model.getStopServiceTime(), errors);
		validateNumericLength("variableServiceTime", model.getVariableServiceTime(), errors);
	}
	

}
