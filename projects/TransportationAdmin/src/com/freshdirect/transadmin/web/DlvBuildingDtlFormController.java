package com.freshdirect.transadmin.web;



import java.math.BigDecimal;
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
import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.transadmin.model.BuildingOperationHours;
import com.freshdirect.transadmin.model.BuildingOperationHoursId;
import com.freshdirect.transadmin.model.DlvBuilding;
import com.freshdirect.transadmin.model.DlvBuildingDetail;
import com.freshdirect.transadmin.model.DlvBuildingDtl;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
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
			//model.setAptDlvAllowed("");
			model.setOther("");
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
			model.setAdditional("");
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
		refData.put("serviceTimeOperators", (List)EnumArithmeticOperator.getEnumList());
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
		
		Collection diffReason=new ArrayList();
		diffReason.add("Security");
		diffReason.add("Slow Elevator");
		diffReason.add("Walkup");
		refData.put("difficultyReasons", diffReason);
		return refData;
	}
	
	private TimeOfDay getValue(TimeOfDay field) {
		 return field==null?new TimeOfDay("00:00 AM"):field;
	}
	
	private String getValue(String field) {
		return field==null?"":field;
	}
	private DlvBuildingDtl decode(DlvBuildingDetail buildingDetail) {
		
		DlvBuildingDtl result=new DlvBuildingDtl();
		result.setBuilding(buildingDetail.getBuilding());
		result.setAddrType(buildingDetail.getAddrType());
		result.setCompanyName(buildingDetail.getCompanyName());
		result.setSvcScrubbedStreet(buildingDetail.getSvcScrubbedStreet());
		result.setSvcCrossStreet(buildingDetail.getSvcCrossStreet());
		result.setSvcCity(buildingDetail.getSvcCity());
		result.setSvcState(buildingDetail.getSvcState());
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
		
		result.setServiceTimeType(buildingDetail.getBuilding().getServiceTimeType());
		result.setServiceTimeOverride(buildingDetail.getBuilding().getServiceTimeOverride());
		result.setServiceTimeOperator(buildingDetail.getBuilding().getServiceTimeOperator());
		result.setServiceTimeAdjustable(buildingDetail.getBuilding().getServiceTimeAdjustable());	
		
		result.setAdditional(buildingDetail.getAdditional());
		result.setIsNew(buildingDetail.getIsNew());
		result.setCrossStreet(buildingDetail.getCrossStreet());
		
		result=setDefaultOperations(result);
		
		if(buildingDetail.getOperationHours()!=null && !buildingDetail.getOperationHours().isEmpty()) {
			BuildingOperationHours opsHours=null;
			for(Iterator<BuildingOperationHours> i = buildingDetail.getOperationHours().iterator(); i.hasNext();){
				opsHours=i.next();
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_MON.getName())) {
					result.setCommentMon(getValue(opsHours.getBldgComments()));
					result.setHoursOpenMon(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseMon(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenMon(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseMon(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentMon(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeMon("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeMon("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_TUE.getName())) {
					result.setCommentTue(getValue(opsHours.getBldgComments()));
					result.setHoursOpenTue(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseTue(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenTue(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseTue(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentTue(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeTue("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeTue("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_WED.getName())) {
					result.setCommentWed(getValue(opsHours.getBldgComments()));
					result.setHoursOpenWed(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseWed(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenWed(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseWed(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentWed(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeWed("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeWed("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_THUR.getName())) {
					result.setCommentThu(getValue(opsHours.getBldgComments()));
					result.setHoursOpenThu(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseThu(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenThu(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseThu(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentThu(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeThu("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeThu("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_FRI.getName())) {
					result.setCommentFri(getValue(opsHours.getBldgComments()));
					result.setHoursOpenFri(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseFri(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenFri(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseFri(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentFri(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeFri("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeFri("1");
					
				} else if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SAT.getName())) {
					result.setCommentSat(getValue(opsHours.getBldgComments()));
					result.setHoursOpenSat(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseSat(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenSat(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseSat(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentSat(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeSat("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeSat("1");
					
				}
				if(opsHours.getId().getDayOfWeek().equals(EnumDayOfWeek.ENUM_DAYOFWEEK_SUN.getName())) {
					result.setCommentSun(getValue(opsHours.getBldgComments()));
					result.setHoursOpenSun(getValue(opsHours.getBldgStartHour()));
					result.setHoursCloseSun(getValue(opsHours.getBldgEndHour()));
					result.setSvcHoursOpenSun(getValue(opsHours.getServiceStartHour()));
					result.setSvcHoursCloseSun(getValue(opsHours.getServiceEndHour()));
					result.setSvcCommentSun(getValue(opsHours.getServiceComments()));
					if(opsHours.getBldgStartHour()!=null)
						result.setIncludeSun("1");
					if(opsHours.getServiceStartHour()!=null)
						result.setSvcIncludeSun("1");
					
				}
			}
			
		}
		
		return result;
	}

private DlvBuildingDetail encode(DlvBuildingDtl buildingDtl) {
		
		DlvBuildingDetail buildingDetail=new DlvBuildingDetail();
		buildingDetail.setBuilding(buildingDtl.getBuilding());
		buildingDetail.setAddrType(buildingDtl.getAddrType());
		buildingDetail.setCompanyName(buildingDtl.getCompanyName());
		buildingDetail.setSvcScrubbedStreet(buildingDtl.getSvcScrubbedStreet());
		buildingDetail.setSvcCrossStreet(buildingDtl.getSvcCrossStreet());
		buildingDetail.setSvcCity(buildingDtl.getSvcCity());
		buildingDetail.setSvcState(buildingDtl.getSvcState());
		buildingDetail.setSvcZip(buildingDtl.getSvcZip());
		buildingDetail.setDoorman(buildingDtl.getDoorman());
		buildingDetail.setWalkup(buildingDtl.getWalkup());
		buildingDetail.setElevator(buildingDtl.getElevator());
		buildingDetail.setSvcEnt(buildingDtl.getSvcEnt());
		buildingDetail.setHouse(buildingDtl.getHouse());
		buildingDetail.setFreightElevator(buildingDtl.getFreightElevator());
		buildingDetail.setHandTruckAllowed(buildingDtl.getHandTruckAllowed());
		buildingDetail.setWalkUpFloors(buildingDtl.getWalkUpFloors());
		buildingDetail.setOther(buildingDtl.getOther());
		buildingDetail.setDifficultReason(buildingDtl.getDifficultReason());
		buildingDetail.setDifficultToDeliver(buildingDtl.getDifficultToDeliver());
		buildingDetail.getBuilding().setServiceTimeType(buildingDtl.getServiceTimeType());
		buildingDetail.getBuilding().setServiceTimeOverride(buildingDtl.getServiceTimeOverride());
		buildingDetail.getBuilding().setServiceTimeOperator(buildingDtl.getServiceTimeOperator());
		buildingDetail.getBuilding().setServiceTimeAdjustable(buildingDtl.getServiceTimeAdjustable());		
		buildingDetail.setAdditional(buildingDtl.getAdditional());
		buildingDetail.setIsNew(buildingDtl.getIsNew());
		buildingDetail.setCrossStreet(buildingDtl.getCrossStreet());
		
		if("1".equals(buildingDtl.getIncludeMon()) || "1".equals(buildingDtl.getSvcIncludeMon())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_MON.getName()));
			if("1".equals(buildingDtl.getIncludeMon())) {
				opsHours.setBldgComments(buildingDtl.getCommentMon());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenMon());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseMon());
			}
			if("1".equals(buildingDtl.getSvcIncludeMon())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentMon());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenMon());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseMon());
			}
			buildingDetail.addOperationHours(opsHours);
		}
		
		if("1".equals(buildingDtl.getIncludeTue()) || "1".equals(buildingDtl.getSvcIncludeTue())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_TUE.getName()));
			if("1".equals(buildingDtl.getIncludeTue())) {
				opsHours.setBldgComments(buildingDtl.getCommentTue());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenTue());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseTue());
			}
			if("1".equals(buildingDtl.getSvcIncludeTue())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentTue());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenTue());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseTue());
			}
			buildingDetail.addOperationHours(opsHours);
		}
		if("1".equals(buildingDtl.getIncludeWed()) || "1".equals(buildingDtl.getSvcIncludeWed())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_WED.getName()));
			if("1".equals(buildingDtl.getIncludeWed())) {
				opsHours.setBldgComments(buildingDtl.getCommentWed());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenWed());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseWed());
			}
			if("1".equals(buildingDtl.getSvcIncludeWed())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentWed());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenWed());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseWed());
			}	
			buildingDetail.addOperationHours(opsHours);
		}
		
		if("1".equals(buildingDtl.getIncludeThu()) || "1".equals(buildingDtl.getSvcIncludeThu())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_THUR.getName()));
			if("1".equals(buildingDtl.getIncludeThu())) {
				opsHours.setBldgComments(buildingDtl.getCommentThu());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenThu());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseThu());
			}
			if("1".equals(buildingDtl.getSvcIncludeThu())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentThu());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenThu());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseThu());
			}
			buildingDetail.addOperationHours(opsHours);
		}

		if("1".equals(buildingDtl.getIncludeFri()) || "1".equals(buildingDtl.getSvcIncludeFri())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_FRI.getName()));
			if("1".equals(buildingDtl.getIncludeFri())) {
				opsHours.setBldgComments(buildingDtl.getCommentFri());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenFri());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseFri());
			}
			if("1".equals(buildingDtl.getSvcIncludeFri())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentFri());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenFri());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseFri());
			}
			buildingDetail.addOperationHours(opsHours);
		}
		
		if("1".equals(buildingDtl.getIncludeSat()) || "1".equals(buildingDtl.getSvcIncludeSat())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_SAT.getName()));
			if("1".equals(buildingDtl.getIncludeSat())) {
				opsHours.setBldgComments(buildingDtl.getCommentSat());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenSat());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseSat());
			}
			if("1".equals(buildingDtl.getSvcIncludeSat())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentSat());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenSat());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseSat());
			}
			buildingDetail.addOperationHours(opsHours);
		}
		if("1".equals(buildingDtl.getIncludeSun()) || "1".equals(buildingDtl.getSvcIncludeSun())) {
			BuildingOperationHours opsHours=new BuildingOperationHours();
			opsHours.setId(new BuildingOperationHoursId(buildingDtl.getDlvBuildingId(),EnumDayOfWeek.ENUM_DAYOFWEEK_SUN.getName()));
			if("1".equals(buildingDtl.getIncludeSun())) {
				opsHours.setBldgComments(buildingDtl.getCommentSun());
				opsHours.setBldgStartHour(buildingDtl.getHoursOpenSun());
				opsHours.setBldgEndHour(buildingDtl.getHoursCloseSun());
			}
			if("1".equals(buildingDtl.getSvcIncludeSun())) {
				opsHours.setServiceComments(buildingDtl.getSvcCommentSun());
				opsHours.setServiceStartHour(buildingDtl.getSvcHoursOpenSun());
				opsHours.setServiceEndHour(buildingDtl.getSvcHoursCloseSun());
			}
			buildingDetail.addOperationHours(opsHours);
		}
		
		
		
		return buildingDetail;
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

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {

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
			DlvBuildingDetail buildingDetail=encode(modelIn);
			DlvBuilding modifiedBuilding = buildingDetail.getBuilding();
			building.setServiceTimeAdjustable(modifiedBuilding.getServiceTimeAdjustable());
			building.setServiceTimeOperator(modifiedBuilding.getServiceTimeOperator());
			building.setServiceTimeOverride(modifiedBuilding.getServiceTimeOverride());
			building.setServiceTimeType(modifiedBuilding.getServiceTimeType());
			
			//getLocationManagerService().saveEntity(domainObject);
			getLocationManagerService().saveEntity(buildingDetail);
			getLocationManagerService().saveEntity(buildingDetail.getBuilding());
			
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

