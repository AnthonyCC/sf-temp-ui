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
	String includeStr=request.getParameter("includeMon");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeMon("1");
	else	
		model.setIncludeMon("0");	
	includeStr=request.getParameter("includeTue");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeTue("1");
	else	
		model.setIncludeTue("0");	
	includeStr=request.getParameter("includeWed");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeWed("1");
	else	
		model.setIncludeWed("0");	
	includeStr=request.getParameter("includeThu");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeThu("1");
	else	
		model.setIncludeThu("0");	
	includeStr=request.getParameter("includeFri");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeFri("1");
	else	
		model.setIncludeFri("0");	
	includeStr=request.getParameter("includeSat");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeSat("1");
	else	
		model.setIncludeSat("0");	
	includeStr=request.getParameter("includeSun");
	if ("true".equalsIgnoreCase(includeStr)) 
		model.setIncludeSun("1");
	else	
		model.setIncludeSun("0");	
	
	String svcIncludeStr=request.getParameter("svcIncludeMon");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeMon("1");
	else	
		model.setSvcIncludeMon("0");	
	svcIncludeStr=request.getParameter("svcIncludeTue");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeTue("1");
	else	
		model.setSvcIncludeTue("0");	
	svcIncludeStr=request.getParameter("svcIncludeWed");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeWed("1");
	else	
		model.setSvcIncludeWed("0");	
	svcIncludeStr=request.getParameter("svcIncludeThu");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeThu("1");
	else	
		model.setSvcIncludeThu("0");	
	svcIncludeStr=request.getParameter("svcIncludeFri");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeFri("1");
	else	
		model.setSvcIncludeFri("0");	
	svcIncludeStr=request.getParameter("svcIncludeSat");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeSat("1");
	else	
		model.setSvcIncludeSat("0");	
	svcIncludeStr=request.getParameter("svcIncludeSun");
	if ("true".equalsIgnoreCase(svcIncludeStr)) 
		model.setSvcIncludeSun("1");
	else	
		model.setSvcIncludeSun("0");	
	
	
	String s = request.getParameter("doorman");
	if ("true".equalsIgnoreCase(s)) 
		model.setDoorman("1");
	else	
		model.setDoorman("0");

	s = request.getParameter("walkup");
	if ("true".equalsIgnoreCase(s)) 
		model.setWalkup("1");
	else	
		model.setWalkup("0");
	
	s = request.getParameter("elevator");
	if ("true".equalsIgnoreCase(s)) 
		model.setElevator("1");
	else	
		model.setElevator("0");
	
	s = request.getParameter("svcEnt");
	if ("true".equalsIgnoreCase(s)) 
		model.setSvcEnt("1");
	else	
		model.setSvcEnt("0");
	
	s = request.getParameter("house");
	if ("true".equalsIgnoreCase(s)) 
		model.setHouse("1");
	else	
		model.setHouse("0");
	
	s = request.getParameter("difficultToDeliver");
	System.out.println("@@@@@@@@@@@@@@@difficultToDeliver= "+ s);

	if ("true".equalsIgnoreCase(s)) 
		model.setDifficultToDeliver("1");
	else	
		model.setDifficultToDeliver("0");
	
	s = request.getParameter("handTruckAllowed");
	if ("true".equalsIgnoreCase(s)) 
		model.setHandTruckAllowed("1");
	else	
		model.setHandTruckAllowed("0");
	
	s = request.getParameter("aptDlvAllowed");
	if ("true".equalsIgnoreCase(s)) 
		model.setAptDlvAllowed("1");
	else	
		model.setAptDlvAllowed("0");
	
		
	
	
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
			return result;
		}
		
		String includeStr=result.getIncludeMon();
		System.out.println("@@@@@includeStr="+includeStr);
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeMon("true");
		else	
			result.setIncludeMon("false");
		includeStr=result.getIncludeTue();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeTue("true");
		else	
			result.setIncludeTue("false");		
		includeStr=result.getIncludeWed();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeWed("true");
		else	
			result.setIncludeWed("false");		
		includeStr=result.getIncludeThu();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeThu("true");
		else	
			result.setIncludeThu("false");		
		includeStr=result.getIncludeFri();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeFri("true");
		else	
			result.setIncludeFri("false");		
		includeStr=result.getIncludeSat();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeSat("true");
		else	
			result.setIncludeSat("false");		
		includeStr=result.getIncludeSun();
		if ("1".equalsIgnoreCase(includeStr)) 
			result.setIncludeSun("true");
		else	
			result.setIncludeSun("false");
		
		String svcIncludeStr=result.getSvcIncludeMon();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeMon("true");
		else	
			result.setSvcIncludeMon("false");
		svcIncludeStr=result.getSvcIncludeTue();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeTue("true");
		else	
			result.setSvcIncludeTue("false");		
		svcIncludeStr=result.getSvcIncludeWed();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeWed("true");
		else	
			result.setSvcIncludeWed("false");		
		svcIncludeStr=result.getSvcIncludeThu();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeThu("true");
		else	
			result.setSvcIncludeThu("false");		
		svcIncludeStr=result.getSvcIncludeFri();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeFri("true");
		else	
			result.setSvcIncludeFri("false");		
		svcIncludeStr=result.getSvcIncludeSat();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeSat("true");
		else	
			result.setSvcIncludeSat("false");		
		svcIncludeStr=result.getSvcIncludeSun();
		if ("1".equalsIgnoreCase(svcIncludeStr)) 
			result.setSvcIncludeSun("true");
		else	
			result.setSvcIncludeSun("false");
		
		String s=result.getDoorman();
		if ("1".equalsIgnoreCase(s)) 
			result.setDoorman("true");
		else	
			result.setDoorman("false");
		
		s=result.getWalkup();
		if ("1".equalsIgnoreCase(s)) 
			result.setWalkup("true");
		else	
			result.setWalkup("false");
		
		s=result.getElevator();
		if ("1".equalsIgnoreCase(s)) 
			result.setElevator("true");
		else	
			result.setElevator("false");
		
		s=result.getSvcEnt();
		if ("1".equalsIgnoreCase(s)) 
			result.setSvcEnt("true");
		else	
			result.setSvcEnt("false");
		
		s=result.getHouse();
		if ("1".equalsIgnoreCase(s)) 
			result.setHouse("true");
		else	
			result.setHouse("false");
		
		s=result.getDifficultToDeliver();
		
		System.out.println("!!!!!!!!!!!difficultToDeliver= "+ s);

		if ("1".equalsIgnoreCase(s)) 
			result.setDifficultToDeliver("true");
		else	
			result.setDifficultToDeliver("false");
		
		s=result.getHandTruckAllowed();
		if ("1".equalsIgnoreCase(s)) 
			result.setHandTruckAllowed("true");
		else	
			result.setHandTruckAllowed("false");
		
		s=result.getAptDlvAllowed();
		if ("1".equalsIgnoreCase(s)) 
			result.setAptDlvAllowed("true");
		else	
			result.setAptDlvAllowed("false");
		
		
		System.out.println("#########exiting getBackingObject");
		
		return result;		
	}
	
	public Object getDefaultBackingObject() {
		return new DlvBuildingDtl();
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
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		return errorList;
	}
	
	
}

