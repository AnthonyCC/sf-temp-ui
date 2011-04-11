package com.freshdirect.transadmin.web.validation;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.model.MaintenanceIssue;

public class MaintenanceRecordValidator extends AbstractValidator {	
		
	public boolean supports(Class clazz) {
		return MaintenanceIssue.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		
		MaintenanceIssue model = (MaintenanceIssue)obj;
		
		ValidationUtils.rejectIfEmpty(errors, "serviceStatus", "app.error.112", new Object[]{"Service Status"},"required");
		ValidationUtils.rejectIfEmpty(errors, "issueType", "app.error.112", new Object[]{"Issue Type"},"required");
		ValidationUtils.rejectIfEmpty(errors, "issueSubType", "app.error.112", new Object[]{"Issue SubType"},"required");
		validateLength("comments", model.getComments(), 256, errors);
		
		if(model !=null && (EnumIssueStatus.OPEN.getName().equalsIgnoreCase(model.getIssueStatus()))){
			ValidationUtils.rejectIfEmpty(errors, "estimatedRepairDate", "app.error.112", new Object[]{"Estimated RepairDate"},"required");	
			if(model.getCreateDate()!= null && model.getEstimatedRepairDate()!= null)
				checkDate("estimatedRepairDate",model.getCreateDate(),model.getEstimatedRepairDate(),"app.error.137", errors);
		}
		
		if(model !=null && (EnumIssueStatus.VERIFIED.getName().equalsIgnoreCase(model.getIssueStatus()) 
				|| EnumIssueStatus.REVERIFIED.getName().equalsIgnoreCase(model.getIssueStatus()))){
			ValidationUtils.rejectIfEmpty(errors, "actualRepairDate", "app.error.112", new Object[]{"Actual RepairDate"},"required");
			if(model.getCreateDate()!= null && model.getActualRepairDate()!= null)
				checkDate("actualRepairDate",model.getCreateDate(),model.getActualRepairDate(),"app.error.136", errors);
			
		}
	}
	
	private void checkDate(String field, Date createDate, Date actualRepairDate, String message, Errors errors) {
		
		try {
			if(actualRepairDate.before(createDate)) {
				errors.rejectValue(field, message,"Invalid Date");
			}			
		} catch (Exception e) {
			errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Invalid Date");	
		}
		
	}
	
}
