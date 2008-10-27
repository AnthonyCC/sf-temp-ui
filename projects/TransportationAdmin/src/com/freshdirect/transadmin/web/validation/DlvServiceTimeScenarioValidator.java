package com.freshdirect.transadmin.web.validation;

import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.routing.manager.UtilityManager;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;

public class DlvServiceTimeScenarioValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DlvServiceTimeScenario.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DlvServiceTimeScenario model = (DlvServiceTimeScenario)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "code", "app.error.112", new Object[]{"Code"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeFactorFormula", "app.error.112", new Object[]{"Service Time Factor Formula"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "serviceTimeFormula", "app.error.112", new Object[]{"Service Time Formula"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "defaultCartonCount", "app.error.112", new Object[]{"Default Carton Count"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "defaultCaseCount", "app.error.112", new Object[]{"Default Case Count"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "defaultFreezerCount", "app.error.112", new Object[]{"Default Freezer Count"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "orderSizeFormula", "app.error.112", new Object[]{"Order Size Formula"},"required field");
				
		if(model != null && (model.getZoneType() == null || model.getZoneType() == null) || "null".equals(model.getZoneType())) {
			errors.rejectValue("zoneType", "app.error.112", new Object[]{"Zone Type"},"required field");
		}
		
		if(model != null && (model.getServiceTimeType() == null || model.getServiceTimeType() == null) || "null".equals(model.getServiceTimeType())) {
			errors.rejectValue("serviceTimeType", "app.error.112", new Object[]{"Service Time Type"},"required field");
		}
		
		UtilityManager manager = new UtilityManager();
		if(!manager.isValidExpression(model.getServiceTimeFactorFormula(), getServiceTimeFactorVariables())) {
			errors.rejectValue("serviceTimeFactorFormula", "app.error.124", new Object[]{},"Invalid Service Time Factor Formula");
		}
		
		if(!manager.isValidExpression(model.getServiceTimeFormula(), getServiceTimeVariables())) {
			errors.rejectValue("serviceTimeFormula", "app.error.125", new Object[]{},"Invalid Service Time Formula");
		}
		
		if(!manager.isValidExpression(model.getOrderSizeFormula(), getOrderSizeVariables())) {
			errors.rejectValue("orderSizeFormula", "app.error.126", new Object[]{},"Invalid Order Size Formula");
		}
		
		validateLength("code", model.getCode(), 8, errors);
		validateLength("description", model.getDescription(), 256, errors);
		
		validateLength("serviceTimeFactorFormula", model.getServiceTimeFactorFormula(), 256, errors);
		validateLength("serviceTimeFormula", model.getServiceTimeFormula(), 256, errors);
		validateLength("serviceTimeFormula", model.getOrderSizeFormula(), 256, errors);
		
	}
	
	private List getServiceTimeFactorVariables() {
		return Arrays.asList(new String[]{"x","y","z"});
	}
	
	private List getServiceTimeVariables() {
		return Arrays.asList(new String[]{"a","b","m"});
	}
		
	
	private List getOrderSizeVariables() {
		return Arrays.asList(new String[]{"x","y","z"});
	}
	

}