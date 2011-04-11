package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.VIRRecord;

public class VIRRecordValidator extends AbstractValidator {	
		
	public boolean supports(Class clazz) {
		return VIRRecord.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		VIRRecord model = (VIRRecord)obj;
		ValidationUtils.rejectIfEmpty(errors, "truckNumber", "app.error.112", new Object[]{"Truck Number"},"required");
		ValidationUtils.rejectIfEmpty(errors, "issueType", "app.error.112", new Object[]{"Issue Type"},"required");
		ValidationUtils.rejectIfEmpty(errors, "issueSubType", "app.error.112", new Object[]{"Issue SubType"},"required");	
		validateLength("comments", model.getComments(), 256, errors);
	}
	
}
