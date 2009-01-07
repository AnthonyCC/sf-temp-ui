package com.freshdirect.transadmin.web.validation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class PlanValidator extends AbstractValidator {
	
	public boolean supports(Class clazz) {
		return WebPlanInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		System.out.println(errors.hasErrors());
		System.out.println(errors.getErrorCount());
		System.out.println(errors.getAllErrors());
		WebPlanInfo model = (WebPlanInfo)obj;
		//  need to decide about the validation part
		ValidationUtils.rejectIfEmpty(errors, "planDate", "app.error.112", new Object[]{"Plan Date"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "startTime", "app.error.112", new Object[]{"Plan Date"},"required field");
		//ValidationUtils.rejectIfEmpty(errors, "planDate", "app.error.112", new Object[]{"Plan Date"},"required field");
		
		if(model != null && TransStringUtil.isEmpty(model.getZoneCode())&& !DispatchPlanUtil.isBullpen(model.getIsBullpen())) {
			errors.rejectValue("zoneCode", "app.error.112", new Object[]{"Zone"},"required field");
		}
		if(DispatchPlanUtil.isBullpen(model.getIsBullpen()) && TransStringUtil.isEmpty(model.getRegionCode())) {
			errors.rejectValue("regionCode", "app.error.112", new Object[]{"Region"},"required field");
		}
		ValidationUtils.rejectIfEmpty(errors, "sequence", "app.error.112", new Object[]{"Sequence"},"required field");
		validateIntegerMinMax("sequence",new Integer(model.getSequence()),1,99,errors);
		ValidationUtils.rejectIfEmpty(errors, "supervisorCode", "app.error.112", new Object[]{"Supervisor"},"required field");
		
		validateResources(model.getDrivers().getResourceReq().getReq().intValue(),model.getDrivers().getResourceReq().getMax().intValue(),"drivers",model.getDrivers(),errors);
		validateResources(model.getHelpers().getResourceReq().getReq().intValue(),model.getHelpers().getResourceReq().getMax().intValue(),"helpers",model.getHelpers(),errors);
		validateResources(model.getRunners().getResourceReq().getReq().intValue(),model.getRunners().getResourceReq().getMax().intValue(),"runners",model.getRunners(),errors);
		checkForDuplicateResourceAllocation(model,errors);
		
		/*boolean hasTimeSlots = true;
		if(model != null && (model. .getTrnTimeslot() == null || model.getTrnTimeslot().getSlotId() == null)) {
			errors.rejectValue("timeslot", "app.error.112", new Object[]{"Start Time Slot"},"required field");
			hasTimeSlots = false;
		}
		
		if(model != null && (model.getTrnEndTimeslot() == null || model.getTrnEndTimeslot().getSlotId() == null)) {
			errors.rejectValue("endTimeslot", "app.error.112", new Object[]{"End Time Slot"},"required field");
			hasTimeSlots = false;
		}
				
		if(hasTimeSlots) {
			//checkDate("timeslot", model.getTrnTimeslot().getSlotId(), model.getTrnEndTimeslot().getSlotId(), errors);
		}*/
		/*if(model != null && (model.getTrnDriver() == null || model.getTrnDriver().getEmployeeId() == null)) {
			errors.rejectValue("driver", "app.error.112", new Object[]{"Driver"},"required field");
		}
		
		if(model != null && (model.getTrnPrimaryHelper() == null || model.getTrnPrimaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("primaryHelper", "app.error.112", new Object[]{"Helper1"},"required field");
		}
		
		if(model != null && (model.getTrnSecondaryHelper() == null || model.getTrnSecondaryHelper().getEmployeeId() == null)) {
			errors.rejectValue("secondaryHelper", "app.error.112", new Object[]{"Helper2"},"required field");
		}*/
		
	}	
	
	private void checkForDuplicateResourceAllocation(WebPlanInfo model, Errors errors) {
		
		Set validResources=  new HashSet();
		validResources=checkForDuplicateResourceAllocation(validResources,model.getDrivers(),"drivers",errors);
		validResources=checkForDuplicateResourceAllocation(validResources,model.getHelpers(),"helpers",errors);
		validResources=checkForDuplicateResourceAllocation(validResources,model.getRunners(),"runners",errors);
	}

	private Set checkForDuplicateResourceAllocation(Set validResources, List nonValidatedResources,String field, Errors errors) {
		
		if (nonValidatedResources==null ||nonValidatedResources.size()==0)
			return validResources;
		for(int i=0;i<nonValidatedResources.size();i++) {
			EmployeeInfo resource=(EmployeeInfo)nonValidatedResources.get(i);
			if(TransStringUtil.isEmpty(resource.getEmployeeId())) {
				continue;
			}
			if(validResources.contains(resource.getEmployeeId())) {
				errors.rejectValue(field, "app.error.122", new Object[]{resource.getName()},"Resource is selected more than once.");
			} else {
				validResources.add(resource.getEmployeeId());
			}
		}
		return validResources;
	}
	
	private void validateResources(int req, int max,String fieldName,List resources,  Errors errors) {
		
		if(resources!=null && resources.size()>0) {
			if(max==0) {
				errors.rejectValue(fieldName, "app.error.120",new Object[]{fieldName},"Please unselect your choices");
			} else {
				validateIntegerMinMax(fieldName,new Integer(getSelectedResources(resources)),req,max,errors);
				boolean hasSelections=false;
				for(int i=0;i<resources.size();i++) {
					EmployeeInfo resource=(EmployeeInfo)resources.get(i);
					if(TransStringUtil.isEmpty(resource.getEmployeeId())) {
						continue;
					} else if(!hasSelections) {
						hasSelections=true;
					}
				}
				if(!hasSelections&& req>0) {
					errors.rejectValue(fieldName, "app.error.112", new Object[]{fieldName},"required field");
				}
			}
		} else if(req>0) {
			//ValidationUtils.rejectIfEmpty(errors, fieldName, "app.error.112", new Object[]{fieldName},"required field");
			errors.rejectValue(fieldName, "app.error.112", new Object[]{fieldName},"required field");
		
		}
		
	}
	private int getSelectedResources(List resources) {
		int selectedResources=0;
		for(int i=0;i<resources.size();i++) {
			EmployeeInfo resource=(EmployeeInfo)resources.get(i);
			if(!TransStringUtil.isEmpty(resource.getEmployeeId())) {
				selectedResources++;
			}
		}
		return selectedResources;
	}
	private void checkDate(String field, String startTime, String endTime, Errors errors) {
		
		try {
			if(TransStringUtil.compareTime(startTime, endTime)) {
				errors.rejectValue(field, "app.error.116", new Object[]{},"Invalid Time");
			}
		} catch(NumberFormatException exp) {
			errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Invalid Time");			
		}
		
	}

}
