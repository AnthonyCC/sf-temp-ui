package com.freshdirect.transadmin.web.validation;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.util.TransStringUtil;

public abstract class AbstractValidator implements Validator  {
	
	protected void validateLength(String field, String value, int length, Errors errors) {
		
		if(!TransStringUtil.isEmpty(value)
				&& value.length() > length) {
			errors.rejectValue(field, "app.error.117", new Object[]{""+length},"Field Length cannot exceed");	
		}		
	}
	
	protected void validateNumericLength(String field, BigDecimal value, Errors errors) {
		
		if(value != null 
				&& !TransStringUtil.isEmpty(value.toString()) 
				&& !TransStringUtil.isValidDecimalFormat(value.toString())) {
			System.out.println("value.toString() >"+value.doubleValue());
			errors.rejectValue(field, "app.error.118", null);			
		}		
	}
}
