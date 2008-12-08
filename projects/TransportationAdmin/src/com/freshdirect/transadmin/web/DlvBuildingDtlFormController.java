package com.freshdirect.transadmin.web;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.DlvServiceTime;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;


public class DlvBuildingDtlFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	/*protected void onBind(HttpServletRequest request, Object command) {
		FileUploadBean model = (FileUploadBean) command;
		String fileContentType=request.getParameter("fileContentType");
		if(fileContentType!=null){ 
			EnumFileContentType enmFileContentType =EnumFileContentType.getEnum(fileContentType);
			if(enmFileContentType!=null){
				model.setFileContentType(enmFileContentType);
			}
		}				
	}*/
	
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {
		
		super.initBinder(request, dataBinder);
		System.out.println("insidre initBinder ");				
		dataBinder.registerCustomEditor(TimeOfDay.class, new TimeOfDayPropertyEditor());
		 
	}
	
protected void onBind(HttpServletRequest request, Object command) {
	System.out.println("@@@@@@@entering on Bind");
	DlvBuildingDtl model = (DlvBuildingDtl) command;
	
	model.setIncludeMon(request.getParameter("includeMon"));
	model.setIncludeTue(request.getParameter("includeTue"));
	model.setIncludeWed(request.getParameter("includeWed"));
	model.setIncludeThu(request.getParameter("includeThu"));
	model.setIncludeFri(request.getParameter("includeFri"));
	model.setIncludeSat(request.getParameter("includeSat"));
	model.setIncludeSun(request.getParameter("includeSun"));
	
	model.setSvcIncludeMon(request.getParameter("svcIncludeMon"));
	model.setSvcIncludeTue(request.getParameter("svcIncludeTue"));
	model.setSvcIncludeWed(request.getParameter("svcIncludeWed"));
	model.setSvcIncludeThu(request.getParameter("svcIncludeThu"));
	model.setSvcIncludeFri(request.getParameter("svcIncludeFri"));
	model.setSvcIncludeSat(request.getParameter("svcIncludeSat"));
	model.setSvcIncludeSun(request.getParameter("svcIncludeSun"));
	
	model.setDoorman(request.getParameter("doorman"));
	model.setWalkup(request.getParameter("walkup"));
	model.setElevator(request.getParameter("elevator"));
	model.setSvcEnt(request.getParameter("svcEnt"));
	model.setHouse(request.getParameter("house"));
	
	model.setDifficultToDeliver(request.getParameter("difficultToDeliver"));
	model.setHandTruckAllowed(request.getParameter("handTruckAllowed"));
	model.setAptDlvAllowed(request.getParameter("aptDlvAllowed"));
	
	System.out.println("@@@Serialize@@@@@@@difficultToDeliver= "+ model.getDifficultToDeliver());
	
	String hours = request.getParameter("hoursOpenMon"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	
	model.setHoursOpenMon(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenTue"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenTue(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenWed"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenWed(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenThu"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenThu(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenFri"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenFri(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenSat"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenSat(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursOpenSun"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursOpenSun(new TimeOfDay(hours));

	hours = request.getParameter("hoursCloseMon"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseMon(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseTue"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseTue(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseWed"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseWed(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseThu"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseThu(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseFri"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseFri(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseSat"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseSat(new TimeOfDay(hours));
	
	hours = request.getParameter("hoursCloseSun"); hours = (hours == null || hours.length() == 0) ? "00:00 AM" : hours;
	model.setHoursCloseSun(new TimeOfDay(hours));
	
	String svcHours = request.getParameter("svcHoursOpenMon");	svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenMon(new TimeOfDay(svcHours)); 

	svcHours = request.getParameter("svcHoursOpenTue"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenTue(new TimeOfDay(svcHours)); 
	
	svcHours = request.getParameter("svcHoursOpenWed"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenWed(new TimeOfDay(svcHours)); 

	svcHours = request.getParameter("svcHoursOpenThu"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenThu(new TimeOfDay(svcHours)); 

	svcHours = request.getParameter("svcHoursOpenFri"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenFri(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursOpenSat"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenSat(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursOpenSun"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursOpenSun(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseMon"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseMon(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseTue"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseTue(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseWed"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseWed(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseThu"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseThu(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseFri"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseFri(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseSat"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseSat(new TimeOfDay(svcHours));
	
	svcHours = request.getParameter("svcHoursCloseSun"); svcHours = (svcHours == null || svcHours.length() == 0) ? "00:00 AM" : svcHours;
	model.setSvcHoursCloseSun(new TimeOfDay(svcHours));
	
	/*System.out.println("###########hoursOpenMon= "+ model.getHoursOpenMon()); 
	System.out.println("###########hoursOpenTue= "+ model.getHoursOpenTue()); 
	System.out.println("###########hoursOpenWed= "+ model.getHoursOpenWed()); 
	System.out.println("###########hoursOpenThu= "+ model.getHoursOpenThu()); 
	System.out.println("###########hoursOpenFri= "+ model.getHoursOpenFri()); 
	System.out.println("###########hoursOpenSat= "+ model.getHoursOpenSat()); 
	System.out.println("###########hoursOpenSun= "+ model.getHoursOpenSun()); 

	System.out.println("###########hoursCloseMon= "+ model.getHoursCloseMon()); 
	System.out.println("###########hoursCloseTue= "+ model.getHoursCloseTue()); 
	System.out.println("###########hoursCloseWed= "+ model.getHoursCloseWed()); 
	System.out.println("###########hoursCloseThu= "+ model.getHoursCloseThu()); 
	System.out.println("###########hoursCloseFri= "+ model.getHoursCloseFri()); 
	System.out.println("###########hoursCloseSat= "+ model.getHoursCloseSat()); 
	System.out.println("###########hoursCloseSun= "+ model.getHoursCloseSun()); 
	
	
	
	System.out.println("###########svcSvcHoursOpenMon= "+ model.getSvcHoursOpenMon()); 
	System.out.println("###########svcSvcHoursOpenTue= "+ model.getSvcHoursOpenTue()); 
	System.out.println("###########svcSvcHoursOpenWed= "+ model.getSvcHoursOpenWed()); 
	System.out.println("###########svcSvcHoursOpenThu= "+ model.getSvcHoursOpenThu()); 
	System.out.println("###########svcSvcHoursOpenFri= "+ model.getSvcHoursOpenFri()); 
	System.out.println("###########svcSvcHoursOpenSat= "+ model.getSvcHoursOpenSat()); 
	System.out.println("###########svcSvcHoursOpenSun= "+ model.getSvcHoursOpenSun()); 
	
	System.out.println("###########svcSvcHoursCloseMon= "+ model.getSvcHoursCloseMon()); 
	System.out.println("###########svcSvcHoursCloseTue= "+ model.getSvcHoursCloseTue()); 
	System.out.println("###########svcSvcHoursCloseWed= "+ model.getSvcHoursCloseWed()); 
	System.out.println("###########svcSvcHoursCloseThu= "+ model.getSvcHoursCloseThu()); 
	System.out.println("###########svcSvcHoursCloseFri= "+ model.getSvcHoursCloseFri()); 
	System.out.println("###########svcSvcHoursCloseSat= "+ model.getSvcHoursCloseSat()); 
	System.out.println("###########svcHoursCloseSun= "+ model.getSvcHoursCloseSun()); 
	System.out.println("#############Exiting onBind()");*/
	
	System.out.println("$$$$$$$$$$$$$$$$$hoursOpenMon= "+ model.getHoursOpenMon()); 

	System.out.println("$$$$$$$$$$$$Exiting onBind()");
}
	
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());	
		refData.put("confidencetypes", getLocationManagerService().getConfidenceTypes());	
		refData.put("qualitytypes", getLocationManagerService().getQualityTypes());
		try {
			refData.put("states", new GeographyServiceProxy().getStateList());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		Collection c = new ArrayList();
		c.add("Residential");
		c.add("Commercial");
		refData.put("addrTypes", c);
	
		return refData;
	}
	
	public Object getBackingObject(String id) {
		System.out.println("#########entering getBackingObject");

		//return getLocationManagerService().getDlvBuildingDtl(id);
		DlvBuildingDtl result = getLocationManagerService().getDlvBuildingDtl(id);
		if(null == result){
			DlvBuilding building = getLocationManagerService().getDlvBuilding(id);
			result = new DlvBuildingDtl();
			result.setBuilding(building);
			result.setHoursOpenMon(new TimeOfDay("00:00 AM"));
			result.setHoursCloseMon(new TimeOfDay("00:00 AM"));
			result.setHoursOpenTue(new TimeOfDay("00:00 AM"));
			result.setHoursCloseTue(new TimeOfDay("00:00 AM"));
			result.setHoursOpenWed(new TimeOfDay("00:00 AM"));
			result.setHoursCloseWed(new TimeOfDay("00:00 AM"));
			result.setHoursOpenThu(new TimeOfDay("00:00 AM"));
			result.setHoursCloseThu(new TimeOfDay("00:00 AM"));
			result.setHoursOpenFri(new TimeOfDay("00:00 AM"));
			result.setHoursCloseFri(new TimeOfDay("00:00 AM"));
			result.setHoursOpenSat(new TimeOfDay("00:00 AM"));
			result.setHoursCloseSat(new TimeOfDay("00:00 AM"));
			result.setHoursOpenSun(new TimeOfDay("00:00 AM"));
			result.setHoursCloseSun(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenTue(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseTue(new TimeOfDay("00:00 AM"));
			
			result.setSvcHoursOpenMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenTue(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseTue(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenWed(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseWed(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenThu(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseThu(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenFri(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseFri(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenSat(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseSat(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenSun(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseSun(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseMon(new TimeOfDay("00:00 AM"));
			result.setSvcHoursOpenTue(new TimeOfDay("00:00 AM"));
			result.setSvcHoursCloseTue(new TimeOfDay("00:00 AM"));
			
			result.setDifficultToDeliver("0");   //tbr
			result.setAddrType("Residential");
			result.setIsNew("true");
			return result;
		}

		result.setIsNew("false");
		
		System.out.println("!!!!!!!!!!! deserialzed!difficultToDeliver= "+ result.getDifficultToDeliver());
		
		System.out.println("#########exiting getBackingObject");
		
		return result;		
	}
	
	public Object getDefaultBackingObject() {
		DlvBuildingDtl domainObj = new DlvBuildingDtl();
		domainObj.setIsNew("true");
		return domainObj;
	}
	
	public boolean isNew(Object command) {
		DlvBuildingDtl modelIn = (DlvBuildingDtl)command;
		return (modelIn.getDlvBuildingDtlId() == null);
	}
	
	public String getDomainObjectName() {
		return "Delivery Building Detail";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		System.out.println("$$$$$$entering preProcess");	

		DlvBuildingDtl modelIn = (DlvBuildingDtl)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getBuilding().getBuildingId()) ) {
			modelIn.getBuilding().setBuildingId(modelIn.getBuilding().getBuildingId());
		}
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public List saveDomainObject(Object domainObject) {
		
		System.out.println("entering to save");	
		List errorList = new ArrayList();
		DlvBuildingDtl modelIn = (DlvBuildingDtl)domainObject;
		System.out.println("################svcValidate= "+modelIn.getSvcValidate());
		System.out.println("################difficultToDeliver= "+modelIn.getDifficultToDeliver());
		if("true".equalsIgnoreCase(modelIn.getSvcValidate())){
			modelIn.setSvcScrubbedStreet(RoutingUtil.standardizeStreetAddress(modelIn.getSvcScrubbedStreet(),
					modelIn.getSvcAddrLine2()));
			modelIn.setSvcAddrLine2("");
		}
		
		if(TransStringUtil.isEmpty(modelIn.getDifficultReason()) ) {
			modelIn.setDifficultReason(null);
		}		
		DlvBuilding building = getLocationManagerService().getDlvBuilding(modelIn.getBuilding().getBuildingId());
		modelIn.setBuilding(building);		
		try {
			getLocationManagerService().saveEntity(domainObject);
			modelIn.setIsNew("false");
			System.out.println("@@@@@\n@@@@\n@@@@@doorman="+modelIn.getDoorman());
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		return errorList;
	}
	
	
}

