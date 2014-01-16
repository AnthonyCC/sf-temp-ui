package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.framework.util.DateComparator;
import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class PlanValidator extends AbstractValidator {
	
	public boolean supports(Class clazz) {
		return WebPlanInfo.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
	
		WebPlanInfo model = (WebPlanInfo)obj;
	
		ValidationUtils.rejectIfEmpty(errors, "planDate", "app.error.112", new Object[]{"Plan Date"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "startTime", "app.error.112", new Object[]{"Dispatch Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "dispatchGroup", "app.error.112", new Object[]{"Group Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "endTime", "app.error.112", new Object[]{"End Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "cutOffTime", "app.error.112", new Object[]{"CutOff Time"},"required field");
		
		if (model != null && model.getDispatchGroup() == null) {
			errors.rejectValue("dispatchGroupS", "app.error.112", new Object[]{"Dispatch group time"},"required field");
		}
		
		if (model != null && model.getOriginFacility() == null) {
			errors.rejectValue("originFacility", "app.error.112",
					new Object[] { "Origin Facility" }, "required field");
		}
		if (model != null && model.getDestinationFacility() == null
				&& !"Y".equalsIgnoreCase(model.getIsBullpen())) {
			errors.rejectValue("destinationFacility", "app.error.112",
					new Object[] { "Destination Facility" }, "required field");
		}
		if (model != null
				&& TransStringUtil.isEmpty(model.getZoneCode())
				&& model.getDestinationFacility() != null
				&& model.getDestinationFacility().getTrnFacilityType() != null
				&& !DispatchPlanUtil.isBullpen(model.getIsBullpen())
				&& !EnumTransportationFacilitySrc.CROSSDOCK.getName()
						.equalsIgnoreCase(
								model.getDestinationFacility()
										.getTrnFacilityType().getName())) {
			errors.rejectValue("zoneCode", "app.error.112",
					new Object[] { "Zone" }, "required field");
		}
		if (TransStringUtil.isEmpty(model.getRegionCode())) {
			errors.rejectValue("regionCode", "app.error.112",
					new Object[] { "Region" }, "required field");
		}
		if (model != null && TransStringUtil.isEmpty(model.getSupervisorCode())) {
			errors.rejectValue("supervisorCode", "app.error.112",
					new Object[] { "Supervisor" }, "required field");
		}
		
		//checkTime("startTime", model.getStartTime(), model.getEndTime(),errors);
		checkDate("dispatchGroupS", model.getDispatchGroup(), model.getStartTime(),errors);
		
		ValidationUtils.rejectIfEmpty(errors, "sequence", "app.error.112", new Object[]{"Sequence"},"required field");
		validateIntegerMinMax("sequence",new Integer(model.getSequence()),0,99,errors);
		if( TransportationAdminProperties.isPlanValidation())
		{
			validateResources(model.getDriverReq(),model.getDriverMax(),"drivers",model.getDrivers(),errors);
			validateResources(model.getHelperReq(),model.getHelperMax(),"helpers",model.getHelpers(),errors);
			validateResources(model.getRunnerReq(),model.getRunnerMax(),"runners",model.getRunners(),errors);
		}
		checkForDuplicateResourceAllocation(model,errors);
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
				errors.rejectValue(field, "app.error.122"
						, new Object[] { resource.getName() }
						, "Resource is selected more than once.");
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
				} else {
					validateIntegerMinMax(fieldName,new Integer(getSelectedResources(resources)),req,max,errors);
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
	private void checkTime(String field, String startTime, String endTime, Errors errors) {
		
		try {
			if (startTime!=null && endTime!=null && TransStringUtil.getServerTime(endTime).before(TransStringUtil.getServerTime(startTime))) {
				errors.rejectValue(field, "app.error.151","Truck end time cannot be before truck dispatch time");
			}
		} catch (ParseException e) {
			errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Truck end time cannot be before truck dispatch time");	
		}		
	}
	private void checkDate(String field, Date startTime, String endTime, Errors errors) {
		
		try {
			if (startTime!=null && endTime!=null && TransStringUtil.getServerTime(endTime).before(startTime)) {
				errors.rejectValue(field, "app.error.152","Truck dispatch time cannot be before dispatch group time");
			}
		} catch (ParseException e) {
			errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Truck dispatch time cannot be before dispatch group time");	
		}		
	}
}
