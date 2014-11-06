package com.freshdirect.transadmin.web.validation;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.freshdirect.framework.util.DateComparator;
import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.constants.EnumDispatchType;
import com.freshdirect.transadmin.dao.DomainManagerDaoI;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.DispatchResourceInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class DispatchValidator extends AbstractValidator {
	
	

	public boolean supports(Class clazz) {
		return DispatchCommand.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {

		DispatchCommand model = (DispatchCommand)obj;

		ValidationUtils.rejectIfEmpty(errors, "startTime", "app.error.112", new Object[]{"Start Time"},"required field");
		ValidationUtils.rejectIfEmpty(errors, "dispatchType", "app.error.112", new Object[]{"Dispatch Type"},"required field");	
		
		if(model != null && TransStringUtil.isEmpty(model.getRegionCode())) {
			errors.rejectValue("regionCode", "app.error.112", new Object[]{"Region"},"required field");
		}
		
		if(model != null && !(EnumDispatchType.LIGHTDUTYDISPATCH.getName().equals(model.getDispatchType())) ) {

			if(model != null && TransStringUtil.isEmpty(model.getZoneCode()) && model.getDestinationFacility() != null && model.getDestinationFacility().getTrnFacilityType() != null
					&& !DispatchPlanUtil.isBullpen(model.getIsBullpen()) && (EnumTransportationFacilitySrc.DELIVERYZONE.getName().equalsIgnoreCase(model.getDestinationFacility().getTrnFacilityType().getName())
					|| EnumTransportationFacilitySrc.DEPOTDELIVERY.getName().equalsIgnoreCase(model.getDestinationFacility().getTrnFacilityType().getName()))) {
				errors.rejectValue("zoneCode", "app.error.112", new Object[]{"Zone"},"required field");
			}
			
	
			if(model != null && model.getOriginFacility() == null) {
				errors.rejectValue("originFacility", "app.error.112", new Object[]{"Origin Facility"},"required field");
			}
	
			if(model != null && model.getDestinationFacility() == null && !"true".equalsIgnoreCase(model.getIsBullpen())) {
				errors.rejectValue("destinationFacility", "app.error.112", new Object[]{"Destination Facility"},"required field");
			}
			
			ValidationUtils.rejectIfEmpty(errors, "route", "app.error.112", new Object[]{"Route Number"},"required field");
			ValidationUtils.rejectIfEmpty(errors, "supervisorCode", "app.error.112", new Object[]{"Supervisor"},"required field");
			//MuniMeter Specific validations
			// need to add some new messages to messages.properties.
			
			if(model != null && !TransStringUtil.isEmpty(model.getMuniMeterEnabled()) && "X".equals(model.getMuniMeterEnabled())){
					
					if(model.getMuniMeterValueAssigned()!=null){
						if(model.getMuniMeterCardNotAssigned()!=null && "X".equals(model.getMuniMeterCardNotAssigned())){
							//reject value app.error.154
							errors.rejectValue("muniMeterValueAssigned", "app.error.154", null);
						}
						if(model.getMuniMeterValueAssigned()>TransportationAdminProperties.getMuniMeterMaxValue()){
							//reject value app.error.156
							errors.rejectValue("muniMeterValueAssigned", "app.error.156", new Object[]{"Card Value Assigned","$"+TransportationAdminProperties.getMuniMeterMaxValue()},"Field value cannot exceed Max.");
						}
						if(model.getMuniMeterValueAssigned()<0){
							//reject Value app.error.158
							errors.rejectValue("muniMeterValueAssigned", "app.error.158", null);
						}
						
					}
					if(model.getMuniMeterValueReturned()!=null){
						if(model.getMuniMeterCardNotReturned()!=null && "X".equals(model.getMuniMeterCardNotReturned())){
							//reject value app.error.155
							errors.rejectValue("muniMeterValueReturned", "app.error.155", null);
						}
						if(model.getMuniMeterValueReturned()>TransportationAdminProperties.getMuniMeterMaxValue()){
							//reject value app.error.156
							errors.rejectValue("muniMeterValueReturned", "app.error.156", new Object[]{"Card Value Returned","$"+TransportationAdminProperties.getMuniMeterMaxValue()},"Field value cannot exceed Max.");
						}
						if(model.getMuniMeterValueAssigned()==null){
							//reject value
							errors.rejectValue("muniMeterValueReturned", "app.error.157", null);
						}
						if(model.getMuniMeterValueAssigned()!=null && model.getMuniMeterValueReturned()>model.getMuniMeterValueAssigned()){
							//reject value app.error.119
							errors.rejectValue("muniMeterValueReturned", "app.error.119", new Object[]{"Card Value Returned","$0","$"+model.getMuniMeterValueAssigned()},"Field value cannot exceed Card Value Assigned.");
						}
						if(model.getMuniMeterValueReturned()<0){
							//reject Value app.error.158
							errors.rejectValue("muniMeterValueReturned", "app.error.158", null);
						}
					}
					if(model.getMuniMeterCardNotReturned()!=null && "X".equals(model.getMuniMeterCardNotReturned())){
						if(model.getMuniMeterValueAssigned()==null){
							//reject value
							errors.rejectValue("muniMeterCardNotReturned", "app.error.157", null);
						}
					}
			}
			
			
			if(!model.getIsOverride())
			{
				validateResources(model.getDriverReq(),model.getDriverMax(),"drivers",model.getDrivers(),errors);
				validateResources(model.getHelperReq(),model.getHelperMax(),"helpers",model.getHelpers(),errors);
				validateResources(model.getRunnerReq(),model.getRunnerMax(),"runners",model.getRunners(),errors);
			}
					
			validateLength("additionalNextels", model.getAdditionalNextels(), 40, errors);
		}
		checkForDuplicateResourceAllocation(model,errors);
		if(errors.getErrorCount() > 0 && model.isConfirmed()){
			model.setConfirmed(false);
		}
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
		DispatchResourceInfo resource=(DispatchResourceInfo)nonValidatedResources.get(i);
		if(TransStringUtil.isEmpty(resource.getEmployeeId())) {
			continue;
		}
		if(validResources.contains(resource.getEmployeeId())) {
			errors.rejectValue(field, "app.error.122", new Object[]{resource.getName()},"Resource is selected more than once.");
			break;
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
				DispatchResourceInfo resource=(DispatchResourceInfo)resources.get(i);
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
		DispatchResourceInfo resource=(DispatchResourceInfo)resources.get(i);
		if(!TransStringUtil.isEmpty(resource.getEmployeeId())) {
			selectedResources++;
		}
	}
	return selectedResources;
}

private void checkDate(String field, String startTime, String endTime, Errors errors) {
	
	try {
		if(startTime !=null && endTime!=null){
			if(DateComparator.compare(DateComparator.PRECISION_MINUTE, TransStringUtil.getServerTime(startTime), TransStringUtil.getServerTime(endTime))>=0) {
				errors.rejectValue(field, "app.error.123","Invalid Time");
			}
		}
	} catch (ParseException e) {
		errors.rejectValue(field, "typeMismatch.time", new Object[]{},"Invalid Time");	
	}
	
}

}
