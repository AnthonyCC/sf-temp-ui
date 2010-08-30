package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.Asset;

public class AssetValidator extends AbstractValidator {	
		
	public boolean supports(Class clazz) {
		return Asset.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Asset model = (Asset)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "assetNo", "app.error.112", new Object[]{"Asset No"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "assetDescription", "app.error.112", new Object[]{"Asset Description"},"required field");			
				
		
	}
	
}
