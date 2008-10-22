package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.util.TransStringUtil;

public abstract class AbstractValidator implements Validator  {
	
	protected void validateLength(String field, String value, int length, Errors errors) {
		
		if(!TransStringUtil.isEmpty(value)
				&& value.length() > length) {
			errors.rejectValue(field, "app.error.116", new Object[]{""+length},"Field Length cannot exceed");	
		}		
	}
}
