package com.freshdirect.transadmin.web.validation;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AreaValidator extends AbstractValidator {	
	
	private String bigDecimalPattern = "\\d{0,1}\\.\\d{0,10}";
	
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
		
		if("X".equalsIgnoreCase(model.getNeedsLoadBalance())) {
			if(model != null && (model.getBalanceBy() == null || model.getBalanceBy() == null) || "null".equals(model.getBalanceBy())) {
				errors.rejectValue("balanceBy", "app.error.112", new Object[]{"Balance By"},"required field");
			}
			ValidationUtils.rejectIfEmpty(errors, "loadBalanceFactor", "app.error.112", new Object[]{"Balance By Factor"},"required field");
			if(!errors.hasFieldErrors("loadBalanceFactor")) {
				validateNumericLength("loadBalanceFactor", model.getLoadBalanceFactor(), errors);
			}
		}
		
		ValidationUtils.rejectIfEmpty(errors, "deliveryRate", "app.error.112", new Object[]{"Delivery Rate"},"required field");
		if(!errors.hasFieldErrors("deliveryRate")) {
			validateNumericLength("deliveryRate", model.getLoadBalanceFactor(), errors);
		}
		
		/*if("X".equalsIgnoreCase(model.getIsDepot()) && !"X".equals(model.getActive())) {
			errors.rejectValue("isDepot", "app.actionmessage.144", new Object[]{},"");
		}*/
		
		
	}
	
	protected void validateNumericLength(String field, BigDecimal value, Errors errors) {
		
		if((value != null 
				&& !TransStringUtil.isEmpty(value.toString()) 
				&& !TransStringUtil.isValidDecimalFormat(value.toString(), bigDecimalPattern))
				|| (value != null && value.doubleValue() > 1.0)) {			
			errors.rejectValue(field, "app.error.118", null);			
		}		
	}
}
