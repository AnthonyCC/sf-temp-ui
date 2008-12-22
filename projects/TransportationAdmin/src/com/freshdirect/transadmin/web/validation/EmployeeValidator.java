package com.freshdirect.transadmin.web.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;



public class EmployeeValidator extends AbstractValidator {	
	
	public boolean supports(Class clazz) {
		return WebEmployeeInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		System.out.println("inside Employee Validator");
		
		WebEmployeeInfo model = (WebEmployeeInfo)obj;
		System.out.println("inside Employee Validator1"+model.getEmpRole());
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "empRole", "app.error.112", new Object[]{" Role Types"},"required field");
		
		System.out.println("errors :"+errors);
		
		/*ValidationUtils.rejectIfEmpty(errors, "firstName", "app.error.112", new Object[]{"First Name"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "lastName", "app.error.112", new Object[]{"Last Name"},"required field");
		
		if(model.getEmployeenumber() != null && !(model.getEmployeenumber().trim().length() == 6  
			//		|| model.getEmployeenumber().trim().length() == 5)) {
			errors.rejectValue("employeenumber", "app.error.114",new Object[]{"Kronos ID","5 or 6"}, "Invalid Field");
		}
				
		if(model != null && (model.getTrnSupervisor() == null || model.getTrnSupervisor().getEmployeeId() == null)) {
			errors.rejectValue("supervisor", "app.error.112", new Object[]{"Supervisor"},"required field");
		}
		
		if(model != null && (model.getTrnEmployeeJobType() == null || model.getTrnEmployeeJobType().getJobTypeId() == null)) {
			errors.rejectValue("employeeJobType", "app.error.112", new Object[]{"Job Type"},"required field");
		}
		*/
	}
	

}

