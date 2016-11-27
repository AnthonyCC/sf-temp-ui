package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.Zone;

public class ZoneValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return Zone.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		Zone model = (Zone)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"name"},"required field");
		
		if(model != null && (model.getTrnZoneType() == null || model.getTrnZoneType() == null) || "null".equals(model.getTrnZoneType())) {
			errors.rejectValue("trnZoneType", "app.error.112", new Object[]{"trnZoneType"},"required field");
		}
				
		if(model != null && (model.getArea() == null || model.getArea() == null) || "null".equals(model.getArea())) {
			errors.rejectValue("area", "app.error.112", new Object[]{"Area"},"required field");
		}
		
		if(model != null && (model.getRegion() == null || model.getRegion() == null) || "null".equals(model.getRegion())) {
			errors.rejectValue("region", "app.error.112", new Object[]{"Region"},"required field");
		}
		
		if(model != null && (model.getServiceTimeType() == null || model.getServiceTimeType() == null) || "null".equals(model.getServiceTimeType())) {
			errors.rejectValue("serviceTimeType", "app.error.112", new Object[]{"Service Time Type"},"required field");
		}

		if(model != null && model.getSvcAdjReductionFactor()!=null && model.getSvcAdjReductionFactor().doubleValue()>1) {
			errors.rejectValue("svcAdjReductionFactor", "app.error.121", new Object[]{"Service Adjustment Reduction Factor"},"Req cannot be greater than Max.");
		}
		
		if(model != null && model.getETAInterval()!=null && model.getETAInterval().intValue() <= 0) {
			errors.rejectValue("ETAInterval", "app.error.156", new Object[]{"ETAInterval"},"ETA interval can't be less than 1 min(s)");
		}
		
		if(model != null && model.getETAInterval() == null 
				&& ("X".equals(model.getEmailETAEnabled()) || "X".equals(model.getManifestETAEnabled()) || "X".equals(model.getSmsETAEnabled()))) {
			errors.rejectValue("ETAInterval", "app.error.112", new Object[]{"ETAInterval"},"required field");
		}
	}
	

}
