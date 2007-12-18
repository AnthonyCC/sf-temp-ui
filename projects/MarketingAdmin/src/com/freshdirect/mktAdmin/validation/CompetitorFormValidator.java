package com.freshdirect.mktAdmin.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.mktAdmin.model.CompetitorAddressModel;

public class CompetitorFormValidator implements Validator {

	public boolean supports(Class clazz) {
		return CompetitorAddressModel.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		CompetitorAddressModel model = (CompetitorAddressModel) obj;        
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "companyName", "app.error.112", new Object[]{"CompanyName"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "address1", "app.error.112", new Object[]{"address1"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "city", "app.error.112", new Object[]{"city"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "state", "app.error.112", new Object[]{"state"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "zipCode", "app.error.112", new Object[]{"zipCode"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "competitorType", "app.error.112", new Object[]{"competitorType"},"required field");
		
//		errors.rejectValue("address1", "app.error.112", new Object[]{"address1"},"required field");
//		errors.rejectValue("city", "app.error.112", new Object[]{"city"},"required field");
//		errors.rejectValue("state", "app.error.112", new Object[]{"state"},"required field");
//		errors.rejectValue("zipCode", "app.error.112", new Object[]{"zipCode"},"required field");
//		errors.rejectValue("competitorType", "app.error.112", new Object[]{"competitorType"},"required field");		
	}
}
