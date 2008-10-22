package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.AssignmentCommand;

public class AssignmentValidator extends AbstractValidator {
	
	public boolean supports(Class clazz) {
		return AssignmentCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		AssignmentCommand model = (AssignmentCommand)obj;
		
		checkDate("planDate", model.getPlanDate(), errors);		
		
		ValidationUtils.rejectIfEmpty(errors, "zoneId", "app.error.112", new Object[]{"Zone"},"required field");
	}
	
	private void checkDate(String field, String date, Errors errors) {
		
		try {
			TransStringUtil.getDate(date);
		} catch(ParseException exp) {
			errors.rejectValue(field, "typeMismatch.date", new Object[]{},"Invalid Date");			
		}
		
	}
}