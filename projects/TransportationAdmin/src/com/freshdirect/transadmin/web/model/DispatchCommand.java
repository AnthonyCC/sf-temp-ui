package com.freshdirect.transadmin.web.model;

//Generated Dec 5, 2008 2:34:33 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.model.DispatchResource;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.ResourceInfoI;
import com.freshdirect.transadmin.model.TrnBaseEntityI;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransStringUtil;

/**
 * Dispatch generated by hbm2java
 */
public class DispatchCommand extends WebPlanInfo implements TrnBaseEntityI  {

	private String dispatchId;
	private String status;
	private String dispatchDate;
	private String route;
	private String truck;
	private boolean confirmed;
	private String comments;
	private String statusName;
	private String noOfStops;
		
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDispatchDate() {
		return dispatchDate;
	}
	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getTruck() {
		return truck;
	}
	public void setTruck(String truck) {
		this.truck = truck;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public boolean  isObsoleteEntity(){
		return false;
	}


	public Set getResources() {
		Set dispatchResources=new HashSet();
		dispatchResources.addAll(getResources(this.getDrivers(),EnumResourceType.DRIVER));
		dispatchResources.addAll(getResources(this.getHelpers(),EnumResourceType.HELPER));
		dispatchResources.addAll(getResources(this.getRunners(),EnumResourceType.RUNNER));
		return dispatchResources;
	}
	
	public ResourceInfoI constructResourceInfo(){
		return new DispatchResourceInfo();
	}

	public ResourceInfoI getResourceInfo(WebEmployeeInfo webEmpInfo, ResourceI resource){
		ResourceInfoI resourceInfo=new DispatchResourceInfo();
		if(webEmpInfo!=null && webEmpInfo.getEmpInfo()!=null ) {
        	EmployeeInfo empInfo= webEmpInfo.getEmpInfo();
        	resourceInfo.setEmployeeId(empInfo.getEmployeeId());
        	resourceInfo.setFirstName(empInfo.getFirstName());
        	resourceInfo.setLastName(empInfo.getLastName());
        	if(resource != null)
        		resourceInfo.setNextelNo(resource.getNextTelNo());
        }
		return resourceInfo;
		
	}
	
	protected ResourceI getResource(ResourceInfoI resourceInfo, EnumResourceType role) {
		
		if(TransStringUtil.isEmpty(resourceInfo.getEmployeeId())||TransStringUtil.isEmpty(role.getName()))
			return null;
		else {
			ResourceI resource= new DispatchResource();
			EmployeeRoleType empRole=new EmployeeRoleType();
			empRole.setCode(role.getName());
			resource.setId(new ResourceId(this.dispatchId,resourceInfo.getEmployeeId()));
			resource.setEmployeeRoleType(empRole);
			resource.setNextTelNo(resourceInfo.getNextelNo());
			return resource;
		}
	}
	public String getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(String noOfStops) {
		this.noOfStops = noOfStops;
	}
}