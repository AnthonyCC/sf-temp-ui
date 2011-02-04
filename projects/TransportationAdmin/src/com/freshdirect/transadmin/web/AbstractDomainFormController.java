package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;

public abstract class AbstractDomainFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;
	
	private LocationManagerI locationManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

//		System.out.println("trying to save the domain object"+domainObject);

		List errorList = null;
		try {
			getDomainManagerService().saveEntity(domainObject);
		} catch (DataIntegrityViolationException objExp) {
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}	

}
