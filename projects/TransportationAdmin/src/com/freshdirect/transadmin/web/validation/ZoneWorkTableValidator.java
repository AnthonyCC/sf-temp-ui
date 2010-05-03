package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;

import com.freshdirect.transadmin.model.ZoneWorkTableForm;

public class ZoneWorkTableValidator extends AbstractValidator {

	@Override
	public boolean supports(Class clazz) {
		return ZoneWorkTableForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ZoneWorkTableForm model=(ZoneWorkTableForm) obj;
		
		if(model != null && (model.getZoneWorkTable() == null || "null".equals(model.getZoneWorkTable()))) {
			errors.rejectValue("zoneWorkTable", "app.error.112", new Object[]{"ZoneWorkTable"},"required field");
		}
				
		if(model != null && (model.getType() == null || "null".equals(model.getType()))) {
			errors.rejectValue("type", "app.error.112", new Object[]{"Type"},"required field");
		}
		
	/*	if(model != null && (model.getEnvironment() == null || "null".equals(model.getEnvironment()))) {
			errors.rejectValue("environment", "app.error.112", new Object[]{"Environment"},"required field");
		}*/

	}

}
