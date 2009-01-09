
package com.freshdirect.transadmin.web.validation;

import java.math.BigDecimal;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.util.TransStringUtil;

public class GeoRestrictionValidator extends AbstractValidator {	
	
	private String bigDecimalPattern = "\\d{0,1}\\.\\d{0,10}";
	
	public boolean supports(Class clazz) {
		return GeoRestriction.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		GeoRestriction model = (GeoRestriction)obj;
		
		System.out.println("###########\n#########\n#########\n");
		
		System.out.println(model);

		if( model.getName() == null || "".equals(model.getName() )) {
			  ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Name"},"required field");
		}
		
		if(model.getComments() == null || "".equals(model.getComments() )) {
			ValidationUtils.rejectIfEmpty(errors, "comments", "app.error.112", new Object[]{"Comments"},"required field");
		}
		
		if(model.getMessage() == null || "".equals(model.getMessage() )) {
			ValidationUtils.rejectIfEmpty(errors, "message", "app.error.112", new Object[]{"Message"},"required field");
		}
		
		System.out.println("###########\n#########\n#########\n");
		

		
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
