package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.freshdirect.transadmin.model.TrnTruck;

public class TruckValidator implements Validator {	
	
	public boolean supports(Class clazz) {
		return TrnTruck.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "truckNumber", "app.error.112", new Object[]{"Truck Number"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "licensePlate", "app.error.112", new Object[]{"First Name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "truckType", "app.error.112", new Object[]{"License Plate"},"required field");					
		
	}
	

}
