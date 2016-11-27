package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class EmployeeValidator extends AbstractValidator {

	public boolean supports(Class clazz) {
		return WebEmployeeInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {

		WebEmployeeInfo model = (WebEmployeeInfo)obj;

		ValidationUtils.rejectIfEmpty(errors, "empRole", "app.error.112", new Object[]{" Role Types"},"required field");
		
		if(model != null && model.getEmpSupervisor() != null 
				&& (!"".equals(model.getEmpSupervisor().getHomeShift()) || !"".equals(model.getEmpSupervisor().getHomeRegion()))
				&& "".equals(model.getEmpSupervisor().getId().getSupervisorId())) {
			errors.rejectValue("homeSupervisorId", "app.error.112", new Object[]{"Supervisor"},"required field");
		}
	}


}

