package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;
import java.util.Calendar;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CopyPlanCommand;

public class CopyPlanValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return CopyPlanCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		CopyPlanCommand model = (CopyPlanCommand)obj;
		
		if(TransStringUtil.isEmpty(model.getSourceDate())) {
			errors.rejectValue("sourceDate", "app.error.112", new Object[]{"Source Week of"},"required field");
		} else {
			checkDate("sourceDate", model.getSourceDate(), errors);
		}
		
		if(TransStringUtil.isEmpty(model.getDestinationDate())) {
			errors.rejectValue("destinationDate", "app.error.112", new Object[]{"Destination Week of"},"required field");
		} else {
			checkDate("destinationDate", model.getDestinationDate(), errors);
		}			
		
		
	}
	
	private void checkDate(String field, String date, Errors errors) {
		
		try {
			if(TransStringUtil.getClientDayofWeek(date) != Calendar.MONDAY) {
				errors.rejectValue(field, "app.error.115", new Object[]{"Monday"},"Invalid Date");
			}
		} catch(ParseException exp) {
			errors.rejectValue(field, "typeMismatch.date", new Object[]{},"Invalid Date");			
		}
		
	}
}