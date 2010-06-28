package com.freshdirect.transadmin.web.validation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.routing.manager.UtilityManager;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvServiceTimeScenarioValidator extends AbstractValidator {	
	
	private String bigDecimalPattern = "\\d{0,1}\\.\\d{0,10}";
	
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
		
		validateNumericLength("defaultCartonCount", model.getDefaultCartonCount(), errors);		
		validateNumericLength("defaultCaseCount", model.getDefaultCaseCount(), errors);
		validateNumericLength("defaultFreezerCount", model.getDefaultFreezerCount(), errors);
		
		if("X".equalsIgnoreCase(model.getNeedsLoadBalance())) {
			if(model != null && (model.getBalanceBy() == null || model.getBalanceBy() == null) || "null".equals(model.getBalanceBy())) {
				errors.rejectValue("balanceBy", "app.error.112", new Object[]{"Balance By"},"required field");
			}
			ValidationUtils.rejectIfEmpty(errors, "loadBalanceFactor", "app.error.112", new Object[]{"Balance By Factor"},"required field");
			if(!errors.hasFieldErrors("loadBalanceFactor")) {
				validateNumericLengthEx("loadBalanceFactor", model.getLoadBalanceFactor(), errors);
			}
		}
				
		if(model.getLateDeliveryFactor() != null) {
			validateNumericLengthEx("lateDeliveryTimeWindowFactor", model.getLateDeliveryFactor(), errors);
		}
	}
	
	protected void validateNumericLengthEx(String field, BigDecimal value, Errors errors) {
		
		if((value != null 
				&& !TransStringUtil.isEmpty(value.toString()) 
				&& !TransStringUtil.isValidDecimalFormat(value.toString(), bigDecimalPattern))
				|| (value != null && value.doubleValue() > 1.0)) {			
			errors.rejectValue(field, "app.error.118", null);			
		}		
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