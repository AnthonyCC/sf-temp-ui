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
			  if (model.getExtraTimeNeeded() == null) {
				  model.setExtraTimeNeeded(new Integer(0));
			  }
			  validateIntegerMinMax("extraTimeNeeded", model.getExtraTimeNeeded(), 1, 60, errors);
		}
		
		if ("1".equals(model.getWalkup())) {
			  if (model.getWalkUpFloors() == null) {
				  model.setWalkUpFloors(new Integer(0));
			  }
			   validateIntegerMinMax("walkUpFloors", model.getWalkUpFloors(), 1, 20, errors);
		}
	}
}
