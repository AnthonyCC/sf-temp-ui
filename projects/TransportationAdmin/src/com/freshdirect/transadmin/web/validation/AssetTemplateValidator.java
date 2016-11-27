package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.AssetTemplate;

public class AssetTemplateValidator extends AbstractValidator {	
		
	public boolean supports(Class clazz) {
		return AssetTemplate.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		AssetTemplate model = (AssetTemplate)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "assetName", "app.error.112", new Object[]{"Asset Template Name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "assetType", "app.error.112", new Object[]{"Asset Type"},"required field");			
				
		
	}
	
}
