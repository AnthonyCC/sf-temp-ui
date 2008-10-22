package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnArea;

public class AreaValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return TrnArea.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		TrnArea model = (TrnArea)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "code", "app.error.112", new Object[]{"Area Code"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Area Name"},"required field");			
		ValidationUtils.rejectIfEmpty(errors, "prefix", "app.error.112", new Object[]{"Prefix"},"required field");
				
		if(model != null && (model.getDeliveryModel() == null || model.getDeliveryModel() == null) || "null".equals(model.getDeliveryModel())) {
			errors.rejectValue("deliveryModel", "app.error.112", new Object[]{"Delivery Model"},"required field");
		}
		
		validateLength("code", model.getCode(), 8, errors);
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);		
		validateLength("prefix", model.getPrefix(), 8, errors);
	
	}
}
