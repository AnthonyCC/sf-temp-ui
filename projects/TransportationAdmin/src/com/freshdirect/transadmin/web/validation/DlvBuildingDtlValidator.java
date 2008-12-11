package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvBuildingDtlValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return DlvBuildingDtl.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		DlvBuildingDtl model = (DlvBuildingDtl)obj;
		//  need to decide about the validation part
		//tbr ValidationUtils.rejectIfEmpty(errors, "svcScrubbedStreet", "app.error.112", new Object[]{"Street"},"required field");
		//if (model.getExtraTimeNeeded() != null && model.getDifficultToDeliver() == "1")
		  //validateIntegerMinMax("extraTimeNeeded", model.getExtraTimeNeeded(), 1, 60, errors);
		//if (model.getWalkUpFloors() != null  && (model.getWalkup() == "1")
		   //validateIntegerMinMax("walkUpFloors", model.getWalkUpFloors(), 1, 20, errors);

		if("1".equals(model.getDifficultToDeliver())) {
			  ValidationUtils.rejectIfEmpty(errors, "extraTimeNeeded", "app.error.112", new Object[]{"Extra Time Needed"},"required field");
			  if(model.getExtraTimeNeeded() != null && !"".equals(model.getExtraTimeNeeded()))
				  validateIntegerMinMax("extraTimeNeeded", "extra Time Needed", model.getExtraTimeNeeded(), 1, 60, errors);
			  ValidationUtils.rejectIfEmpty(errors, "difficultReason", "app.error.112", new Object[]{"Reason For Difficulty"},"required field");
		}
		
		if ("1".equals(model.getWalkup())) {
			     ValidationUtils.rejectIfEmpty(errors, "walkUpFloors", "app.error.112", new Object[]{"Walk Up Floors"},"required field");
				  if(model.getWalkUpFloors() != null  && !"".equals(model.getWalkUpFloors()))
					  validateIntegerMinMax("walkUpFloors", "Walk Up Floors", model.getWalkUpFloors(), 1, 20, errors);
		}
	}
}
