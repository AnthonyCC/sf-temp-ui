package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.transadmin.model.TimeslotRestriction;
import com.freshdirect.transadmin.model.TimeslotRestrictionCommand;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.RestrictionManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.TransStringUtil;

public class TimeslotRestrictionFormController extends AbstractFormController {
	private RestrictionManagerI restrictionManagerService;
	private ZoneManagerI zoneManagerService;
	private DomainManagerI domainManagerService;

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
		

		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService().getActiveZoneCodes();
    	if(zones != null && activeZoneCodes != null) {
    		Iterator _iterator = zones.iterator();
    		Zone _tmpZone = null;
    		while(_iterator.hasNext()) {
    			_tmpZone = (Zone)_iterator.next();
    			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
    				_iterator.remove();
    			}
    		}
    	}
    	refData.put("conditions", (List) EnumLogicalOperator.getEnumList());
		refData.put("zones", zones);
		refData.put("DayOfWeeks", (List) EnumDayOfWeek.getEnumList());
		return refData;
	}

	public RestrictionManagerI getRestrictionManagerService() {
		return restrictionManagerService;
	}

	public void setRestrictionManagerService(
			RestrictionManagerI restrictionManagerService) {
		this.restrictionManagerService = restrictionManagerService;
	}

	public String getDomainObjectName() {
		return "Timeslot Restriction";
	}

	public Object getDefaultBackingObject() {
		TimeslotRestrictionCommand domainObj = new TimeslotRestrictionCommand();
		return domainObj;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);

	}
	
	protected void onBind(HttpServletRequest request, Object command) {
		TimeslotRestrictionCommand model = (TimeslotRestrictionCommand) command;
	}

	public Object getBackingObject(String id) {

		TimeslotRestrictionCommand result = getCommand(getRestrictionManagerService()
				.getTimeslotRestriction(id));
		if (null == result) {
			result = new TimeslotRestrictionCommand();
			return result;
		}
		return result;
	}

	private TimeslotRestrictionCommand getCommand(
			TimeslotRestriction modelIn) {
		
		try{
			TimeslotRestrictionCommand command = new TimeslotRestrictionCommand(modelIn);
			return command;
		}catch(ParseException pe){
			throw new RuntimeException("Unparseable date "+pe.getMessage());
		}
	}

	public boolean isNew(Object command) {
		TimeslotRestrictionCommand modelIn = (TimeslotRestrictionCommand) command;
		return (TransStringUtil.isEmpty(modelIn.getId())); // tbr
	}

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

		List errorList = new ArrayList();
		TimeslotRestrictionCommand modelIn = (TimeslotRestrictionCommand) domainObject;
		
		TimeslotRestriction model;
		try {
			model = new TimeslotRestriction(modelIn);
		} catch (ParseException pe) {
			throw new RuntimeException("Unparseable date "+pe.getMessage());
		}

		try {
			getRestrictionManagerService().saveTimeslotRestriction(model);
		} catch (Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119",
					new Object[] { getDomainObjectName() }));
		}
		return errorList;
	}

}
