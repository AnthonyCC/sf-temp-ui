package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.Zone;

public class ScribValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Scrib.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Scrib model = (Scrib)obj;
		if(model.getStartTime()==null)
		{
			errors.rejectValue("startTimeS", "app.error.112", new Object[]{"Start Time"},"required field");
		}
		if(model.getFirstDeliveryTime()==null)
		{
			errors.rejectValue("firstDlvTimeS", "app.error.112", new Object[]{"First Dlv Time"},"required field");
		}
		if(model.getLastDeliveryTime()==null)
		{
			errors.rejectValue("endDlvTimeS", "app.error.112", new Object[]{"Last Dlv Time"},"required field");
		}
		if(model.getScribDate()==null)
		{
			errors.rejectValue("scribDate", "app.error.112", new Object[]{"Date"},"required field");
		}
		if(model.getZone()==null)
		{
			errors.rejectValue("zoneS", "app.error.112", new Object[]{"Zone"},"required field");
		}
		if(model.getSupervisorCode()==null||model.getSupervisorCode().trim().length()==0)
		{
			errors.rejectValue("supervisorCode", "app.error.112", new Object[]{"Supervisor"},"required field");
		}
		if(model.getCutOffTime()==null)
		{
			errors.rejectValue("cutOffTimeS", "app.error.112", new Object[]{"CutOff Time"},"required field");
		}
	}
	

}
