package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.Zone;

public class ZoneValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Zone.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Zone model = (Zone)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"name"},"required field");
		
		if(model != null && (model.getTrnZoneType() == null || model.getTrnZoneType() == null) || "null".equals(model.getTrnZoneType())) {
			errors.rejectValue("trnZoneType", "app.error.112", new Object[]{"trnZoneType"},"required field");
		}
				
		if(model != null && (model.getArea() == null || model.getArea() == null) || "null".equals(model.getArea())) {
			errors.rejectValue("area", "app.error.112", new Object[]{"Area"},"required field");
		}
		
		if(model != null && (model.getRegion() == null || model.getRegion() == null) || "null".equals(model.getRegion())) {
			errors.rejectValue("region", "app.error.112", new Object[]{"Region"},"required field");
		}
		
		if(model != null && (model.getDefaultServiceTimeType()== null || model.getDefaultServiceTimeType() == null) || "null".equals(model.getDefaultServiceTimeType())) {
			errors.rejectValue("defaultServiceTimeType", "app.error.112", new Object[]{"ServiceTimeType"},"required field");
		}
	}
	

}
