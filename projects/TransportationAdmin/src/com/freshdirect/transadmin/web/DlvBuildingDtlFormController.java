package com.freshdirect.transadmin.web;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.BuildingOperationHours;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;
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

	private void setEntFields(HttpServletRequest request, DlvBuildingDtl model)
	{
		String openHours= "00:00 AM";
		String closeHours= "00:00 AM";
		String comment = "";

		if("1".equals(request.getParameter("includeMon"))){
			openHours= request.getParameter("hoursOpenMon");
			closeHours = request.getParameter("hoursCloseMon");
			comment=request.getParameter("commentMon");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenMon(new TimeOfDay(openHours));
		model.setHoursCloseMon(new TimeOfDay(closeHours));
		model.setCommentMon(comment);

		if("1".equals(request.getParameter("includeTue"))){
			openHours= request.getParameter("hoursOpenTue");
			closeHours = request.getParameter("hoursCloseTue");
			comment=request.getParameter("commentTue");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenTue(new TimeOfDay(openHours));
		model.setHoursCloseTue(new TimeOfDay(closeHours));
		model.setCommentTue(comment);

		if("1".equals(request.getParameter("includeWed"))){
			openHours= request.getParameter("hoursOpenWed");
			closeHours = request.getParameter("hoursCloseWed");
			comment=request.getParameter("commentWed");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenWed(new TimeOfDay(openHours));
		model.setHoursCloseWed(new TimeOfDay(closeHours));
		model.setCommentWed(comment);

		if("1".equals(request.getParameter("includeThu"))){
			openHours= request.getParameter("hoursOpenThu");
			closeHours = request.getParameter("hoursCloseThu");
			comment=request.getParameter("commentThu");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenThu(new TimeOfDay(openHours));
		model.setHoursCloseThu(new TimeOfDay(closeHours));
		model.setCommentThu(comment);

		if("1".equals(request.getParameter("includeFri"))){
			openHours= request.getParameter("hoursOpenFri");
			closeHours = request.getParameter("hoursCloseFri");
			comment=request.getParameter("commentFri");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenFri(new TimeOfDay(openHours));
		model.setHoursCloseFri(new TimeOfDay(closeHours));
		model.setCommentFri(comment);

		if("1".equals(request.getParameter("includeSat"))){
			openHours= request.getParameter("hoursOpenSat");
			closeHours = request.getParameter("hoursCloseSat");
			comment=request.getParameter("commentSat");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenSat(new TimeOfDay(openHours));
		model.setHoursCloseSat(new TimeOfDay(closeHours));
		model.setCommentSat(comment);

		if("1".equals(request.getParameter("includeSun"))){
			openHours= request.getParameter("hoursOpenSun");
			closeHours = request.getParameter("hoursCloseSun");
			comment=request.getParameter("commentSun");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";

		}
		model.setHoursOpenSun(new TimeOfDay(openHours));
		model.setHoursCloseSun(new TimeOfDay(closeHours));
		model.setCommentSun(comment);

	}


	private void setSvcEntFields(HttpServletRequest request, DlvBuildingDtl model)
	{
		model.setSvcIncludeMon(request.getParameter("svcIncludeMon"));
		model.setSvcIncludeTue(request.getParameter("svcIncludeTue"));
		model.setSvcIncludeWed(request.getParameter("svcIncludeWed"));
		model.setSvcIncludeThu(request.getParameter("svcIncludeThu"));
		model.setSvcIncludeFri(request.getParameter("svcIncludeFri"));
		model.setSvcIncludeSat(request.getParameter("svcIncludeSat"));
		model.setSvcIncludeSun(request.getParameter("svcIncludeSun"));

		String openHours= "00:00 AM";
		String closeHours= "00:00 AM";
		String comment = "";

		if("1".equals(request.getParameter("svcIncludeMon"))){
			openHours= request.getParameter("svcHoursOpenMon");
			closeHours = request.getParameter("svcHoursCloseMon");
			comment=request.getParameter("svcCommentMon");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenMon(new TimeOfDay(openHours));
		model.setSvcHoursCloseMon(new TimeOfDay(closeHours));
		model.setSvcCommentMon(comment);

		if("1".equals(request.getParameter("svcIncludeTue"))){
			openHours= request.getParameter("svcHoursOpenTue");
			closeHours = request.getParameter("svcHoursCloseTue");
			comment=request.getParameter("svcCommentTue");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenTue(new TimeOfDay(openHours));
		model.setSvcHoursCloseTue(new TimeOfDay(closeHours));
		model.setSvcCommentTue(comment);

		if("1".equals(request.getParameter("svcIncludeWed"))){
			openHours= request.getParameter("svcHoursOpenWed");
			closeHours = request.getParameter("svcHoursCloseWed");
			comment=request.getParameter("svcCommentWed");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenWed(new TimeOfDay(openHours));
		model.setSvcHoursCloseWed(new TimeOfDay(closeHours));
		model.setSvcCommentWed(comment);

		if("1".equals(request.getParameter("svcIncludeThu"))){
			openHours= request.getParameter("svcHoursOpenThu");
			closeHours = request.getParameter("svcHoursCloseThu");
			comment=request.getParameter("svcCommentThu");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenThu(new TimeOfDay(openHours));
		model.setSvcHoursCloseThu(new TimeOfDay(closeHours));
		model.setSvcCommentThu(comment);

		if("1".equals(request.getParameter("svcIncludeFri"))){
			openHours= request.getParameter("svcHoursOpenFri");
			closeHours = request.getParameter("svcHoursCloseFri");
			comment=request.getParameter("svcCommentFri");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenFri(new TimeOfDay(openHours));
		model.setSvcHoursCloseFri(new TimeOfDay(closeHours));
		model.setSvcCommentFri(comment);

		if("1".equals(request.getParameter("svcIncludeSat"))){
			openHours= request.getParameter("svcHoursOpenSat");
			closeHours = request.getParameter("svcHoursCloseSat");
			comment=request.getParameter("svcCommentSat");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenSat(new TimeOfDay(openHours));
		model.setSvcHoursCloseSat(new TimeOfDay(closeHours));
		model.setSvcCommentSat(comment);

		if("1".equals(request.getParameter("svcIncludeSun"))){
			openHours= request.getParameter("svcHoursOpenSun");
			closeHours = request.getParameter("svcHoursCloseSun");
			comment=request.getParameter("svcCommentSun");
		}
		else{
			openHours= "00:00 AM";
			closeHours= "00:00 AM";
			comment = "";
		}
		model.setSvcHoursOpenSun(new TimeOfDay(openHours));
		model.setSvcHoursCloseSun(new TimeOfDay(closeHours));
		model.setSvcCommentSun(comment);

	}


	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);
//		System.out.println("insidre initBinder ");
		dataBinder.registerCustomEditor(TimeOfDay.class, new TimeOfDayPropertyEditor());

	}

protected void onBind(HttpServletRequest request, Object command) {
	//System.out.println("@@@@@@@entering on Bind");
	DlvBuildingDtl model = (DlvBuildingDtl) command;

	model.setSvcEnt(request.getParameter("svcEnt"));
	if(!"1".equals(model.getSvcEnt())){
	    model.setSvcScrubbedStreet("");
		model.setSvcCity("");
		model.setSvcState("");
		model.setSvcZip("");
	}

	model.setDoorman(request.getParameter("doorman"));
	if(!"1".equals(model.getDoorman())){
		model.setHandTruckAllowed("");
		model.setAptDlvAllowed("");
	}


	model.setWalkup(request.getParameter("walkup"));
	if(!"1".equals(model.getWalkup())){
		model.setWalkUpFloors(new Integer(0));
	}

	model.setElevator(request.getParameter("elevator"));
	model.setSvcEnt(request.getParameter("svcEnt"));
	model.setHouse(request.getParameter("house"));

	model.setDifficultToDeliver(request.getParameter("difficultToDeliver"));
	if(!"1".equals(model.getDifficultToDeliver())){
		model.setDifficultReason("");
		model.setExtraTimeNeeded(new Integer(0));
	}

	setEntFields(request, model);
	setSvcEntFields(request, model);

	//System.out.println("$$$$$$$$$$$$Exiting onBind()");
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
		c.add("Mixed-Use");
		refData.put("addrTypes", c);

		return refData;
	}
	
	private DlvBuildingDtl decode(DlvBuildingDetail buildingDetail) {
		
		DlvBuildingDtl result=new DlvBuildingDtl();
		result.setBuilding(buildingDetail.getBuilding());
		result.setAddrType(buildingDetail.getAddrType());
		result.setCompanyName(buildingDetail.getCompanyName());
		result.setSvcScrubbedStreet(buildingDetail.getSvcScrubbedStreet());
		result.setSvcCrossStreet(buildingDetail.getSvcCrossStreet());
		result.setSvcCity(buildingDetail.getSvcCity());
		result.setSvcZip(buildingDetail.getSvcZip());
		result.setDoorman(buildingDetail.getDoorman());
		result.setWalkup(buildingDetail.getWalkup());
		result.setElevator(buildingDetail.getElevator());
		result.setSvcEnt(buildingDetail.getSvcEnt());
		result.setHouse(buildingDetail.getHouse());
		result.setFreightElevator(buildingDetail.getFreightElevator());
		result.setHandTruckAllowed(buildingDetail.getHandTruckAllowed());
		result.setWalkUpFloors(buildingDetail.getWalkUpFloors());
		result.setOther(buildingDetail.getOther());
		result.setDifficultReason(buildingDetail.getDifficultReason());
		result.setDifficultToDeliver(buildingDetail.getDifficultToDeliver());
		result.setServiceTimeOverride(buildingDetail.getServiceTimeOverride());
		result.setAdditional(buildingDetail.getAdditional());
		result.setIsNew(buildingDetail.getIsNew());
		result.setCrossStreet(buildingDetail.getCrossStreet());
		
		result=setDefaultOperations(result);
		
		if(buildingDetail.getOperationHours()!=null && !buildingDetail.getOperationHours().isEmpty()) {
			BuildingOperationHours opsHours=null;
			for(Iterator<BuildingOperationHours> i = buildingDetail.getOperationHours().iterator(); i.hasNext();){
				opsHours=i.next();
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_MON.getName())) {
					result.setCommentMon(opsHours.getBldgComments());
					result.setHoursOpenMon(opsHours.getBldgStartHour());
					result.setHoursCloseMon(opsHours.getBldgStartHour());
					result.setSvcHoursOpenMon(opsHours.getServiceStartHour());
					result.setSvcHoursCloseMon(opsHours.getServiceEndHour());
					result.setSvcCommentMon(opsHours.getServiceComments());
					result.setIncludeMon("1");
					result.setSvcIncludeMon("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_TUE.getName())) {
					result.setCommentTue(opsHours.getBldgComments());
					result.setHoursOpenTue(opsHours.getBldgStartHour());
					result.setHoursCloseTue(opsHours.getBldgStartHour());
					result.setSvcHoursOpenTue(opsHours.getServiceStartHour());
					result.setSvcHoursCloseTue(opsHours.getServiceEndHour());
					result.setSvcCommentTue(opsHours.getServiceComments());
					result.setIncludeTue("1");
					result.setSvcIncludeTue("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_WED.getName())) {
					result.setCommentWed(opsHours.getBldgComments());
					result.setHoursOpenWed(opsHours.getBldgStartHour());
					result.setHoursCloseWed(opsHours.getBldgStartHour());
					result.setSvcHoursOpenWed(opsHours.getServiceStartHour());
					result.setSvcHoursCloseWed(opsHours.getServiceEndHour());
					result.setSvcCommentWed(opsHours.getServiceComments());
					result.setIncludeWed("1");
					result.setSvcIncludeWed("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_THUR.getName())) {
					result.setCommentThu(opsHours.getBldgComments());
					result.setHoursOpenThu(opsHours.getBldgStartHour());
					result.setHoursCloseThu(opsHours.getBldgStartHour());
					result.setSvcHoursOpenThu(opsHours.getServiceStartHour());
					result.setSvcHoursCloseThu(opsHours.getServiceEndHour());
					result.setSvcCommentThu(opsHours.getServiceComments());
					result.setIncludeThu("1");
					result.setSvcIncludeThu("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_FRI.getName())) {
					result.setCommentFri(opsHours.getBldgComments());
					result.setHoursOpenFri(opsHours.getBldgStartHour());
					result.setHoursCloseFri(opsHours.getBldgStartHour());
					result.setSvcHoursOpenFri(opsHours.getServiceStartHour());
					result.setSvcHoursCloseFri(opsHours.getServiceEndHour());
					result.setSvcCommentFri(opsHours.getServiceComments());
					result.setIncludeFri("1");
					result.setSvcIncludeFri("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SAT.getName())) {
					result.setCommentSat(opsHours.getBldgComments());
					result.setHoursOpenSat(opsHours.getBldgStartHour());
					result.setHoursCloseSat(opsHours.getBldgStartHour());
					result.setSvcHoursOpenSat(opsHours.getServiceStartHour());
					result.setSvcHoursCloseSat(opsHours.getServiceEndHour());
					result.setSvcCommentSat(opsHours.getServiceComments());
					result.setIncludeSat("1");
					result.setSvcIncludeSat("1");
					
				}
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SUN.getName())) {
					result.setCommentSun(opsHours.getBldgComments());
					result.setHoursOpenSun(opsHours.getBldgStartHour());
					result.setHoursCloseSun(opsHours.getBldgStartHour());
					result.setSvcHoursOpenSun(opsHours.getServiceStartHour());
					result.setSvcHoursCloseSun(opsHours.getServiceEndHour());
					result.setSvcCommentSun(opsHours.getServiceComments());
					result.setIncludeSun("1");
					result.setSvcIncludeSun("1");
					
				}
			}
			
		}
		
		return result;
	}

private DlvBuildingDtl encode(DlvBuildingDetail buildingDetail) {
		
		DlvBuildingDtl result=new DlvBuildingDtl();
		result.setBuilding(buildingDetail.getBuilding());
		result.setAddrType(buildingDetail.getAddrType());
		result.setCompanyName(buildingDetail.getCompanyName());
		result.setSvcScrubbedStreet(buildingDetail.getSvcScrubbedStreet());
		result.setSvcCrossStreet(buildingDetail.getSvcCrossStreet());
		result.setSvcCity(buildingDetail.getSvcCity());
		result.setSvcZip(buildingDetail.getSvcZip());
		result.setDoorman(buildingDetail.getDoorman());
		result.setWalkup(buildingDetail.getWalkup());
		result.setElevator(buildingDetail.getElevator());
		result.setSvcEnt(buildingDetail.getSvcEnt());
		result.setHouse(buildingDetail.getHouse());
		result.setFreightElevator(buildingDetail.getFreightElevator());
		result.setHandTruckAllowed(buildingDetail.getHandTruckAllowed());
		result.setWalkUpFloors(buildingDetail.getWalkUpFloors());
		result.setOther(buildingDetail.getOther());
		result.setDifficultReason(buildingDetail.getDifficultReason());
		result.setDifficultToDeliver(buildingDetail.getDifficultToDeliver());
		result.setServiceTimeOverride(buildingDetail.getServiceTimeOverride());
		result.setAdditional(buildingDetail.getAdditional());
		result.setIsNew(buildingDetail.getIsNew());
		result.setCrossStreet(buildingDetail.getCrossStreet());
		
		if(buildingDetail.getOperationHours()!=null && !buildingDetail.getOperationHours().isEmpty()) {
			BuildingOperationHours opsHours=null;
			for(Iterator<BuildingOperationHours> i = buildingDetail.getOperationHours().iterator(); i.hasNext();){
				opsHours=i.next();
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_MON)) {
					result.setCommentMon(opsHours.getBldgComments());
					result.setHoursOpenMon(opsHours.getBldgStartHour());
					result.setHoursCloseMon(opsHours.getBldgStartHour());
					result.setSvcHoursOpenMon(opsHours.getServiceStartHour());
					result.setSvcHoursCloseMon(opsHours.getServiceEndHour());
					result.setSvcCommentMon(opsHours.getServiceComments());
					result.setIncludeMon("1");
					result.setSvcIncludeMon("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_TUE)) {
					result.setCommentTue(opsHours.getBldgComments());
					result.setHoursOpenTue(opsHours.getBldgStartHour());
					result.setHoursCloseTue(opsHours.getBldgStartHour());
					result.setSvcHoursOpenTue(opsHours.getServiceStartHour());
					result.setSvcHoursCloseTue(opsHours.getServiceEndHour());
					result.setSvcCommentTue(opsHours.getServiceComments());
					result.setIncludeTue("1");
					result.setSvcIncludeTue("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_WED)) {
					result.setCommentWed(opsHours.getBldgComments());
					result.setHoursOpenWed(opsHours.getBldgStartHour());
					result.setHoursCloseWed(opsHours.getBldgStartHour());
					result.setSvcHoursOpenWed(opsHours.getServiceStartHour());
					result.setSvcHoursCloseWed(opsHours.getServiceEndHour());
					result.setSvcCommentWed(opsHours.getServiceComments());
					result.setIncludeWed("1");
					result.setSvcIncludeWed("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_THUR)) {
					result.setCommentThu(opsHours.getBldgComments());
					result.setHoursOpenThu(opsHours.getBldgStartHour());
					result.setHoursCloseThu(opsHours.getBldgStartHour());
					result.setSvcHoursOpenThu(opsHours.getServiceStartHour());
					result.setSvcHoursCloseThu(opsHours.getServiceEndHour());
					result.setSvcCommentThu(opsHours.getServiceComments());
					result.setIncludeThu("1");
					result.setSvcIncludeThu("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_FRI)) {
					result.setCommentFri(opsHours.getBldgComments());
					result.setHoursOpenFri(opsHours.getBldgStartHour());
					result.setHoursCloseFri(opsHours.getBldgStartHour());
					result.setSvcHoursOpenFri(opsHours.getServiceStartHour());
					result.setSvcHoursCloseFri(opsHours.getServiceEndHour());
					result.setSvcCommentFri(opsHours.getServiceComments());
					result.setIncludeFri("1");
					result.setSvcIncludeFri("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SAT)) {
					result.setCommentSat(opsHours.getBldgComments());
					result.setHoursOpenSat(opsHours.getBldgStartHour());
					result.setHoursCloseSat(opsHours.getBldgStartHour());
					result.setSvcHoursOpenSat(opsHours.getServiceStartHour());
					result.setSvcHoursCloseSat(opsHours.getServiceEndHour());
					result.setSvcCommentSat(opsHours.getServiceComments());
					result.setIncludeSat("1");
					result.setSvcIncludeSat("1");
					
				}
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SUN)) {
					result.setCommentSun(opsHours.getBldgComments());
					result.setHoursOpenSun(opsHours.getBldgStartHour());
					result.setHoursCloseSun(opsHours.getBldgStartHour());
					result.setSvcHoursOpenSun(opsHours.getServiceStartHour());
					result.setSvcHoursCloseSun(opsHours.getServiceEndHour());
					result.setSvcCommentSun(opsHours.getServiceComments());
					result.setIncludeSun("1");
					result.setSvcIncludeSun("1");
					
				}
			}
			
		}
		
		return result;
	}
	public Object getBackingObject(String id) {
		//System.out.println("#########entering getBackingObject");

		//return getLocationManagerService().getDlvBuildingDtl(id);
		DlvBuildingDetail buildingDetail=getLocationManagerService().getDlvBuildingDtl(id);
		
		DlvBuildingDtl result = null;
		if(null == buildingDetail){
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
		} else {
			result=decode(buildingDetail);
		}

		result.setIsNew("false");

		//System.out.println("#########exiting getBackingObject");

		return result;
	}

	public Object getDefaultBackingObject() {
		DlvBuildingDtl domainObj = new DlvBuildingDtl();
		domainObj.setIsNew("true");
		return domainObj;
	}

	public boolean isNew(Object command) {
		DlvBuildingDtl modelIn = (DlvBuildingDtl)command;
		return (modelIn.getBuilding() == null);
	}

	public String getDomainObjectName() {
		return "Delivery Building Detail";
	}

	protected void preProcessDomainObject(Object domainObject) {
		//System.out.println("$$$$$$entering preProcess");

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
		if("true".equalsIgnoreCase(modelIn.getSvcValidate())){
			modelIn.setSvcScrubbedStreet(RoutingUtil.standardizeStreetAddress(modelIn.getSvcScrubbedStreet(), null));
		}

		if(TransStringUtil.isEmpty(modelIn.getDifficultReason()) ) {
			modelIn.setDifficultReason(null);
		}
		DlvBuilding building = getLocationManagerService().getDlvBuilding(modelIn.getBuilding().getBuildingId());
		modelIn.setBuilding(building);
		try {
			getLocationManagerService().saveEntity(domainObject);
			modelIn.setIsNew("false");
		} catch(Exception e) {
			e.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
		}
		return errorList;
	}
	
	private DlvBuildingDtl setDefaultOperations(DlvBuildingDtl result) {
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
		
		return result;
	}


}

