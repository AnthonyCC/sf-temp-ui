package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.ZoneTypeCommand;

public class ZoneTypeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return ZoneTypeCommand.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		ZoneTypeCommand model = (ZoneTypeCommand)obj;
		//  need to decide about the validation part		
		ValidationUtils.rejectIfEmpty(errors, "name", "app.error.112", new Object[]{"Zone Type Name"},"required field");		
		
		validateLength("name", model.getName(), 32, errors);
		validateLength("description", model.getDescription(), 256, errors);
		
		if(model.getDriverMax()!=null) {
			validateIntegerMinMax("driverMax",model.getDriverMax(),1,10,errors);
		}
		if(model.getHelperMax()!=null) {
			validateIntegerMinMax("helperMax",model.getHelperMax(),1,10,errors);
		}
		if(model.getRunnerMax()!=null) {
			validateIntegerMinMax("runnerMax",model.getRunnerMax(),1,10,errors);
		}
		
		if(model.getDriverReq()!=null) {
			validateIntegerMinMax("driverReq",model.getDriverReq(),0,10,errors);
		}
		if(model.getHelperReq()!=null) {
			validateIntegerMinMax("helperReq",model.getHelperReq(),0,10,errors);
		}
		if(model.getRunnerReq()!=null) {
			validateIntegerMinMax("runnerReq",model.getRunnerReq(),0,10,errors);
		}
		
		if(model.getDriverReq()!=null && model.getDriverMax()==null) {
			ValidationUtils.rejectIfEmpty(errors, "driverMax", "app.error.121", new Object[]{"Driver Max."},"required field");
		}
		if(model.getHelperReq()!=null && model.getHelperMax()==null) {
			ValidationUtils.rejectIfEmpty(errors, "helperMax", "app.error.121", new Object[]{"Helper Max."},"required field");
		}
		if(model.getRunnerReq()!=null && model.getRunnerMax()==null) {
			ValidationUtils.rejectIfEmpty(errors, "runnerMax", "app.error.121", new Object[]{"Runner Max."},"required field");
		}
		
		if(model.getDriverMax()!=null && model.getDriverReq()==null) {
			ValidationUtils.rejectIfEmpty(errors, "driverReq", "app.error.112", new Object[]{"Driver Req."},"required field");
		}
		if(model.getHelperMax()!=null && model.getHelperReq()==null) {
			ValidationUtils.rejectIfEmpty(errors, "helperReq", "app.error.112", new Object[]{"Helper Req."},"required field");
		}
		if(model.getRunnerMax()!=null && model.getRunnerReq()==null) {
			ValidationUtils.rejectIfEmpty(errors, "runnerReq", "app.error.112", new Object[]{"Runner Req."},"required field");
		}
		
		if(hasValues(model.getDriverReq(),model.getDriverMax()) && (model.getDriverReq().intValue()>model.getDriverMax().intValue())) {
			errors.rejectValue("driverReq", "app.error.121", new Object[]{"Driver Req."},"Req cannot be greater than Max.");
		}
		if(hasValues(model.getHelperReq(),model.getHelperMax()) && (model.getHelperReq().intValue()>model.getHelperMax().intValue())) {
			errors.rejectValue("helperReq", "app.error.121", new Object[]{"Helper Req."},"Req cannot be greater than Max.");
		}
		if(hasValues(model.getRunnerReq(),model.getRunnerMax()) && (model.getRunnerReq().intValue()>model.getRunnerMax().intValue())) {
			errors.rejectValue("runnerReq", "app.error.121", new Object[]{"Runner Req."},"Req cannot be greater than Max.");
		}
	}
	
	private boolean hasValues(Integer req,Integer max) {
		
		if(req==null || max==null)
			return false;
		return true;
		
	}
}
