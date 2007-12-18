package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnTruck;

public class TruckFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("trucktypes", getDomainManagerService().getTruckTypes());		
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getTruck(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnTruck();
	}
	
	public boolean isNew(Object command) {
		TrnTruck modelIn = (TrnTruck)command;
		return (modelIn.getTruckId() == null);
	}
	
	public String getDomainObjectName() {
		return "Truck";
	}

}
