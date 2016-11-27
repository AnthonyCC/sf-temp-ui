
package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TimeslotRestriction;
import com.freshdirect.transadmin.model.TimeslotRestrictionCommand;
import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotRestrictionValidator extends AbstractValidator {

	private String bigDecimalPattern = "\\d{0,1}\\.\\d{0,10}";

	public boolean supports(Class clazz) {
		return TimeslotRestrictionCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {

		TimeslotRestrictionCommand model = (TimeslotRestrictionCommand)obj;

		if( model.getDayOfWeek() == null || "".equals(model.getDayOfWeek() )) {
			  ValidationUtils.rejectIfEmpty(errors, "dayOfWeek", "app.error.112", new Object[]{"dayOfWeek"},"required field");
		}

		if(model.getZoneCode() == null || "".equals(model.getZoneCode())) {
			ValidationUtils.rejectIfEmpty(errors, "zoneCode", "app.error.112", new Object[]{"zoneCode"},"required field");
		}
		if(model.getStartTime() == null) {
			ValidationUtils.rejectIfEmpty(errors, "startTime", "app.error.112", new Object[]{"startTime"},"required field");
		}

		if(model.getEndTime() == null) {
			ValidationUtils.rejectIfEmpty(errors, "endTime", "app.error.112", new Object[]{"endTime"},"required field");
		}

		if(errors.getErrorCount()==0) {
				try {
					if(TransStringUtil.getServerTime(model.getEndTime()).before(TransStringUtil.getServerTime(model.getStartTime()))){
						errors.rejectValue("endTime", "app.error.124", null,"");
					}
				} catch (ParseException e) {
					errors.rejectValue("endTime", "typeMismatch.time", new Object[]{},"Invalid Time");
				}
			}
		}
}
