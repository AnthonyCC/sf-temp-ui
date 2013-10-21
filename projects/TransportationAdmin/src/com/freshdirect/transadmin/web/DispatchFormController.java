package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.constants.EnumDispatchType;
import com.freshdirect.transadmin.exception.TransAdminApplicationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumResourceSubType;
import com.freshdirect.transadmin.util.EnumResourceType;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TrnFacilityPropertyEditor;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.util.TransWebUtil;

public class DispatchFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;

	private DispatchManagerI dispatchManagerService;

	private EmployeeManagerI employeeManagerService;

	private ZoneManagerI zoneManagerService;
	
	private AssetManagerI assetManagerService;
	
	private LocationManagerI locationManagerService;
		
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}
	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		request.setAttribute("commandObj",command);
		return referenceData(request);
	}
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(TrnFacility.class, new TrnFacilityPropertyEditor());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService()
													.getActiveZoneCodes();
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
		
		Map refData = new HashMap();

		String dispDate = request.getParameter("dispDate");

		if (StringUtils.hasText(dispDate)) {
			refData.put("routes", domainManagerService
					.getAllRoutes(getServerDate(dispDate)));

		} else {
			refData.put("routes", domainManagerService
					.getAllRoutes(TransStringUtil.getCurrentServerDate()));
		}
		if (StringUtils.hasText(dispDate)) {
			refData.put("trucks", getDispatchManagerService()
					.getAvailableTrucks(getServerDate(dispDate)));
		} else {
			refData
					.put("trucks", getDispatchManagerService()
							.getAvailableTrucks(
									TransStringUtil.getCurrentServerDate()));
		}

		List drivers = null;
		List helpers = null;
		List runners = null;
		
		TrnFacility facility = null;
		Date dispatchDate = null;
		DispatchCommand model = (DispatchCommand)request.getAttribute("commandObj");
		if(null != model){
			facility = model.getDestinationFacility();
			try {
				dispatchDate = TransStringUtil.getServerDateString1( model.getDispatchDate());
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		if(facility != null && 
				EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(facility.getTrnFacilityType().getName())){

			drivers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.DRIVER.getName()
																										, EnumResourceSubType.TRAILER_DRIVER.getName()));
			helpers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.HELPER.getName()
																										, EnumResourceSubType.TRAILER_HELPER.getName()));
			runners = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.RUNNER.getName()
																										, EnumResourceSubType.TRAILER_RUNNER.getName()));
		} else {
			drivers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName(), model != null ? model.getDispatchType() : null, dispatchDate));
			helpers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName(), model != null ? model.getDispatchType() : null, dispatchDate));
			runners = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName(), model != null ? model.getDispatchType() : null, dispatchDate));
		}
		drivers.addAll(DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.MANAGER.getName(), model != null ? model.getDispatchType() : null, dispatchDate)));
		refData.put("drivers", drivers);
		refData.put("helpers", helpers);
		refData.put("runners", runners);

		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("zones", zones);
		
		if(null != model && EnumDispatchType.LIGHTDUTYDISPATCH.getName().equals(model.getDispatchType()))		
			refData.put("regions", domainManagerService.getLightDutyRegions());
		else
			refData.put("regions", domainManagerService.getRegions());
		
		refData.put("reasons", dispatchManagerService.getDispatchReasons(true));
		refData.put("trnFacilitys", locationManagerService.getTrnFacilitys());
		refData.put("statuses", domainManagerService.getDispositionTypes());
		refData.put(DispatchPlanUtil.ASSETTYPE_GPS, assetManagerService.getActiveAssets(DispatchPlanUtil.ASSETTYPE_GPS));
		refData.put(DispatchPlanUtil.ASSETTYPE_EZPASS, assetManagerService.getActiveAssets(DispatchPlanUtil.ASSETTYPE_EZPASS));
		refData.put(DispatchPlanUtil.ASSETTYPE_MOTKIT, assetManagerService.getActiveAssets(DispatchPlanUtil.ASSETTYPE_MOTKIT));
		
		refData.put("dispatchTypes", EnumDispatchType.getEnumList());
		refData.put("dispatchGroups", getDomainManagerService().getDispatchGroups());
		
		return refData;
	}

	@SuppressWarnings("rawtypes")
	private DispatchCommand getCommand(Dispatch dispatch) throws Exception{
		Zone zone = null;
		if(dispatch.getZone() != null) {
			zone = domainManagerService.getZone(dispatch.getZone().getZoneCode());
		}
		
		boolean isToday = TransStringUtil.isToday(dispatch.getDispatchDate());
		Collection punchInfo = null;
		if (isToday && TransWebUtil.isPunch(getDispatchManagerService())) {
			punchInfo = TransAdminCacheManager.getInstance().getPunchInfo(TransStringUtil.getServerDate(dispatch.getDispatchDate()), employeeManagerService);
		}
		
		DispatchCommand dispatchCommand = DispatchPlanUtil.getDispatchCommand(
				dispatch, zone, employeeManagerService, punchInfo, null, null,
				null, null, null, null, null,null);
		if (isToday && TransWebUtil.isPunch(getDispatchManagerService()))
			DispatchPlanUtil.setDispatchStatus(dispatchCommand);
		
		return dispatchCommand;
	}

	private Dispatch getDispatch(DispatchCommand command) throws Exception {
		return DispatchPlanUtil.getDispatch(command, domainManagerService);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = getIdFromRequest(request);

		if (StringUtils.hasText(id)) {
			Object  tmp = getBackingObject(id, request);
			return tmp;
		} else {
			return getDefaultBackingObject();
		}
	}

	public Object getDefaultBackingObject() {
		DispatchCommand command = new  DispatchCommand();
		command.setZoneCode("");
		command.setTruck("");
		try{
			command.setDispatchDate(TransStringUtil.getDate(new Date()));
			command.setStartTime(TransStringUtil.getServerTime(new Date()));
		} catch (ParseException exp) {
			exp.printStackTrace();
		}
		return command;
	}

	@SuppressWarnings("rawtypes")
	public Object getBackingObject(String id, HttpServletRequest request) {
		try{
			DispatchCommand command = getCommand(getDispatchManagerService().getDispatch(id));
			
			String cloneId = request.getParameter("cloneId");
			if(!TransStringUtil.isEmpty(cloneId)) {
				command.setDispatchId(null);
				command.setPlanId(null);
				
				Collection punchInfo = null;
				if (TransStringUtil.isToday(command.getDispatchDate()) && TransWebUtil.isPunch(getDispatchManagerService())) {
					punchInfo = TransAdminCacheManager.getInstance().getPunchInfo(TransStringUtil.getServerDate(command.getDispatchDate()), employeeManagerService);
				}
				
				Zone zone = null;
				if(command.getZoneCode() != null) {
					zone = domainManagerService.getZone(command.getZoneCode());
				}
				command.setResources(employeeManagerService, new HashSet(),
										DispatchPlanUtil.getResourceRequirements(zone),
										punchInfo, null, null, null, null, null, null);

				command.setRoute(null);
				command.setPhysicalTruck(null);
				command.setTruck(null);
				
				command.setComments(null);
				command.setGpsNumber(null);
				command.setEzpassNumber(null);
				command.setMotKitNumber(null);
				command.setAdditionalNextels(null);
				command.setStatus(null);
				
				command.setConfirmed(false);
				command.setIsOverride(false);
				command.setOverrideReasonCode(null);
				command.setOverrideUser(null);
			}
			
			return command;
		} catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("An Error Ocuurred when trying to construct command object "+ex.getMessage());
		}
	}
	
	@Override
	public Object getBackingObject(String id) {
		// Replaced by getBackingObject(String id, request)
		return null;
	}

	public boolean isNew(Object command) {
		DispatchCommand modelIn = (DispatchCommand)command;
		return (TransStringUtil.isEmpty(modelIn.getDispatchId()));
	}

	public String getDomainObjectName() {
		return "DispatchCommand";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List saveDispatch(DispatchCommand command) {
		List errorList = null;
		try {			
			Dispatch domainObject = getDispatch(command);
			Dispatch previousModel = getDispatchManagerService().getDispatch(domainObject.getDispatchId());
			if (previousModel != null) {
				if (previousModel.getDispatchTime() != null
						&& domainObject.getDispatchTime() != null) {
					domainObject.setDispatchTime(previousModel.getDispatchTime());
				}
				if (previousModel.getCheckedInTime() != null
						&& domainObject.getCheckedInTime() != null) {
					domainObject.setCheckedInTime(previousModel.getCheckedInTime());
				}
			}
			getDispatchManagerService().saveDispatch(domainObject, command.getReferenceContextId());
			command.setDispatchId(domainObject.getDispatchId());
			command.setReferenceContextId(null);
			
			if(TransStringUtil.isToday(command.getDispatchDate())) {
				DispatchPlanUtil.setDispatchStatus(command);
			}
		} catch (TransAdminApplicationException objExp) {
			errorList = new ArrayList();
			errorList.add(objExp.getMessage());
		} catch (Exception objExp) {
		    objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		DispatchCommand command = (DispatchCommand) domainObject;
		return saveDispatch(command);
	}


	protected void onBind(HttpServletRequest request, Object command,BindException errors) {
		DispatchCommand model = (DispatchCommand) command;

		TrnFacility deliveryFacility = locationManagerService.getTrnFacility(request.getParameter("destinationFacility"));
		if(deliveryFacility != null && 
				EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(deliveryFacility.getTrnFacilityType().getName())){
			model.setZoneCode("");
			model.setZoneName("");
		}
		model.setDestinationFacility(deliveryFacility);

		if(!TransStringUtil.isEmpty(model.getIsBullpen()) 
				&& model.getIsBullpen().equals("true")){
			model.setZoneCode("");
			model.setZoneName("");
			model.setZoneType("");
			model.setDestinationFacility(null);
		}
		if(EnumDispatchType.LIGHTDUTYDISPATCH.getName().equals(model.getDispatchType())) {
			model.setZoneCode("");
		}
		Zone zone = null;
		if(!TransStringUtil.isEmpty(model.getZoneCode())) {
			zone = domainManagerService.getZone(model.getZoneCode());
		}
		String overrideReasonCode = request.getParameter("overrideReasonCode");
		if (overrideReasonCode == null)
			model.setOverrideReasonCode(null);
		
		model = (DispatchCommand) DispatchPlanUtil.reconstructWebPlanInfo(
				model, zone, model.getDispatchGroupModified(), model.getDispatchDate(), employeeManagerService,	zoneManagerService, false);
		try {
			boolean routeChanged = false;
			Collection assignedRoutes = getDispatchManagerService().getAssignedRoutes(TransStringUtil.getServerDate(model.getDispatchDate()));
			if(!TransStringUtil.isEmpty(model.getDispatchId())){
				Dispatch currDispatch = getDispatchManagerService().getDispatch(model.getDispatchId());
				if(!model.getRoute().equals(currDispatch.getRoute()))
					routeChanged = true;
			}else{
				routeChanged = true;
			}

			if(routeChanged && assignedRoutes.contains(model.getRoute())){
				errors.rejectValue("route","app.error.135", new String[]{model.getRoute()},null);
			}
		} catch (ParseException exp) {
			// Ignore it
		}
		//set userId to command object
		model.setUserId(getUserId(request));
	}
	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		DispatchCommand _command=(DispatchCommand)command;
		if("true".equalsIgnoreCase(_command.getDispatchGroupModified())
				|| "true".equalsIgnoreCase(_command.getDestFacilityModified())
					|| "true".equalsIgnoreCase(_command.getDispatchTypeModified())) {
			return true;
		}
		else
			return isFormChangeRequest(request);
	}

	protected void onFormChange(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {

		DispatchCommand _command=(DispatchCommand)command;		
		_command.setDispatchGroupModified("false");
		_command.setDestFacilityModified("false");
		_command.setDispatchTypeModified("false");
	}


	@SuppressWarnings("unchecked")
	protected String getIdFromRequest(HttpServletRequest request)
	{
		TransWebUtil.httpRequest.set(request);
		String id = request.getParameter("id");
		if (TransStringUtil.isEmpty(id)) {
			id = request.getParameter("dispatchId");
		}
		if (TransStringUtil.isEmpty(id)) {
			id = request.getParameter("cloneId");
		}
		return id;
	}	

}