package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.DlvServiceTime;

public class DlvServiceTimeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DlvServiceTime.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DlvServiceTime model = (DlvServiceTime)obj;
		System.out.println("Errors:"+errors.getFieldErrors());
				
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeId.serviceTimeType", "app.error.112", new Object[]{"Service Time Type"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeId.zoneType", "app.error.112", new Object[]{"Service Type"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "fixedServiceTime", "app.error.112", new Object[]{"Fixed Service Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "variableServiceTime", "app.error.112", new Object[]{"Variable Service Time"},"required field");
		
		if(model != null && (model.getZoneType() == null || model.getZoneType() == null) || "null".equals(model.getZoneType())) {
			errors.rejectValue("zoneType", "app.error.112", new Object[]{"Zone Type"},"required field");
		}
		
		if(model != null && (model.getServiceTimeType() == null || model.getServiceTimeType() == null) || "null".equals(model.getServiceTimeType())) {
			errors.rejectValue("serviceTimeType", "app.error.112", new Object[]{"Service Time Type"},"required field");
		}
		
		if(model != null && (model.getServiceTimeType() == null || model.getServiceTimeType() == null) || "null".equals(model.getServiceTimeType())) {
			errors.rejectValue("zoneType", "app.error.112", new Object[]{"Service Time Type"},"required field");
		}
		
		validateNumericLength("fixedServiceTime", model.getFixedServiceTime(), errors);
		validateNumericLength("variableServiceTime", model.getVariableServiceTime(), errors);			
		
	}
	

}
