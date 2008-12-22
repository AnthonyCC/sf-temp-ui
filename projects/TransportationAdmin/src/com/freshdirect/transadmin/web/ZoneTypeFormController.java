package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.model.EmployeeRoleType;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.ZonetypeResource;
import com.freshdirect.transadmin.model.ZonetypeResourceId;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.ZoneTypeCommand;

public class ZoneTypeFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;
	
	private EmployeeManagerI employeeManagerService;

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		
		this.employeeManagerService=employeeManagerService;
	}
	
	public boolean isNew(Object command) {
		ZoneTypeCommand modelIn = (ZoneTypeCommand)command;
		return (modelIn.getZoneTypeId() == null);
	}
	
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		String id=request.getParameter("id");
		Map refData = new HashMap();
		//refData.put("employeeRoleTypes", getDomainManagerService().getEmployeeRoleTypes());
		return refData;
	}

	public Object getBackingObject(String id) {
		
		ZoneTypeCommand command=getCommand(getDomainManagerService().getZoneType(id));		
		return command;
	}
	
	public Object getDefaultBackingObject() {
		ZoneTypeCommand zoneType = new ZoneTypeCommand();	
		Collection employeeRoleTypes=getEmployeeManagerService().getEmployeeRoleTypes();
		zoneType.setDriverCode(getEmployeeTypeCode(employeeRoleTypes,"Driver"));
		zoneType.setHelperCode(getEmployeeTypeCode(employeeRoleTypes,"Helper"));
		zoneType.setRunnerCode(getEmployeeTypeCode(employeeRoleTypes,"Runner"));

		return zoneType;
	}

	public List saveDomainObject(Object domainObject) {			

		ZoneTypeCommand model = (ZoneTypeCommand)domainObject;
				
		return saveZoneType(model);								
	}
	
	private List saveZoneType(ZoneTypeCommand model) {
		
		List errorList = null;
		try {
			
			
			TrnZoneType domainObject=getTrnZoneType(model);
			if(!isNew(domainObject)) {
				getDomainManagerService().saveEntity(domainObject);
			} else {
				getDomainManagerService().saveZoneType(domainObject);
			}
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
		
	}
	
	private boolean isNew(TrnZoneType domainObject) {
		if(TransStringUtil.isEmpty(domainObject.getZoneTypeId()))
			return true;
		else 
			return false;
	}
	
    public String getDomainObjectName() {
		
		return "Zone Type";
	}

	private TrnZoneType getTrnZoneType(ZoneTypeCommand command) {
    	
    	TrnZoneType zoneType=new TrnZoneType();
    	if(!"".equals(command.getZoneTypeId())) {
    		zoneType.setZoneTypeId(command.getZoneTypeId());
    	}
    	zoneType.setName(command.getName());
    	zoneType.setDescription(command.getDescription());
    	zoneType.setZonetypeResources(getZoneTypeResources(command,zoneType));
    	return zoneType;
    	
    }
	
	private Set getZoneTypeResources(ZoneTypeCommand command,TrnZoneType zoneType) {
		

		Set resources=new HashSet();
    	if(command.getDriverMax()!=null) {
    		resources.add(getZonetypeResourceInfo(zoneType,command.getZoneTypeId(),command.getDriverCode(), command.getDriverMax(),command.getDriverReq()));
    	} 
    	if(command.getHelperMax()!=null) {
    		resources.add(getZonetypeResourceInfo(zoneType,command.getZoneTypeId(),command.getHelperCode(), command.getHelperMax(),command.getHelperReq()));
    	}
    	if(command.getRunnerMax()!=null) {
    		resources.add(getZonetypeResourceInfo(zoneType,command.getZoneTypeId(),command.getRunnerCode(), command.getRunnerMax(),command.getRunnerReq()));
    	}
    	return resources;
	}
    
    private ZonetypeResource getZonetypeResourceInfo(TrnZoneType zoneType,String zoneTypeId, String code, Integer max, Integer req) {
    	
    	ZonetypeResource ztr=new ZonetypeResource();
    	ztr.setId(new ZonetypeResourceId(zoneTypeId,code));
    	ztr.setMaximumNo(max);
    	ztr.setRequiredNo(req);
    	ztr.setZoneType(zoneType);
    	return ztr;
    }
	
	private ZoneTypeCommand getCommand(TrnZoneType zoneType) {
		
		Collection employeeRoleTypes=getEmployeeManagerService().getEmployeeRoleTypes();
		ZoneTypeCommand command = new ZoneTypeCommand();
		command.setZoneTypeId(zoneType.getZoneTypeId());
		command.setDescription(zoneType.getDescription());
		command.setName(zoneType.getName());
		command.setDriverCode(getEmployeeTypeCode(employeeRoleTypes,"Driver"));
		command.setHelperCode(getEmployeeTypeCode(employeeRoleTypes,"Helper"));
		command.setRunnerCode(getEmployeeTypeCode(employeeRoleTypes,"Runner"));

		Set resources=zoneType.getZonetypeResources();
		Iterator it=resources.iterator();
		while(it.hasNext()) {
			ZonetypeResource _resource=(ZonetypeResource)it.next();
			
			
			if(getEmployeeTypeCode(employeeRoleTypes,"Driver").equalsIgnoreCase(_resource.getId().getRole())) {
				command.setDriverMax(_resource.getMaximumNo());
				command.setDriverReq(_resource.getRequiredNo());
			} 
			else if(getEmployeeTypeCode(employeeRoleTypes,"Helper").equalsIgnoreCase(_resource.getId().getRole())) {
				command.setHelperMax(_resource.getMaximumNo());
				command.setHelperReq(_resource.getRequiredNo());
			} 
			else if(getEmployeeTypeCode(employeeRoleTypes,"Runner").equalsIgnoreCase(_resource.getId().getRole())) {
				command.setRunnerMax(_resource.getMaximumNo());
				command.setRunnerReq(_resource.getRequiredNo());
			}
		}
		return command;
	}
	
	private String getEmployeeTypeCode(Collection employeeRoleType, String name) {
		
		Iterator it=employeeRoleType.iterator();
		String code="";
		while(it.hasNext()) {
			EmployeeRoleType role=(EmployeeRoleType)it.next();
			if(name.equals(role.getName())) {
				code=role.getCode();
				break;
			}
		}
		return code;
	}
}
