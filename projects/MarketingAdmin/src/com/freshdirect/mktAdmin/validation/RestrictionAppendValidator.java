package com.freshdirect.mktAdmin.validation;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.mktAdmin.model.CompetitorAddressModel;
import com.freshdirect.mktAdmin.model.RestrictionListAppendBean;

public class RestrictionAppendValidator implements Validator {

	public boolean supports(Class clazz) {
		return RestrictionListAppendBean.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		RestrictionListAppendBean model = (RestrictionListAppendBean) obj;        
		//  need to decide about the validation part
		if(model.getCustomerIds()==null || model.getCustomerIds().trim().length()==0){
		  ValidationUtils.rejectIfEmpty(errors, "customerIds", "app.error.112", new Object[]{"customerIds"},"required field");
		}
//		errors.rejectValue("address1", "app.error.112", new Object[]{"address1"},"required field");
//		errors.rejectValue("city", "app.error.112", new Object[]{"city"},"required field");
//		errors.rejectValue("state", "app.error.112", new Object[]{"state"},"required field");
//		errors.rejectValue("zipCode", "app.error.112", new Object[]{"zipCode"},"required field");
//		errors.rejectValue("competitorType", "app.error.112", new Object[]{"competitorType"},"required field");		
	}
	
	}
