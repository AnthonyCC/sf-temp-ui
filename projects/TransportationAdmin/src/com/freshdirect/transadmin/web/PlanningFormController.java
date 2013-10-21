package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.model.DispatchGroup;
import com.freshdirect.transadmin.model.Plan;
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
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.WaveUtil;
import com.freshdirect.transadmin.web.editor.DispatchGroupPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnFacilityPropertyEditor;
import com.freshdirect.transadmin.web.model.ResourceList;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PlanningFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;

	private DispatchManagerI dispatchManagerService;

	private EmployeeManagerI employeeManagerService;
	
	private ZoneManagerI zoneManagerService;
	
	private LocationManagerI locationManagerService;
	
	private AssetManagerI assetManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Collection zones = getDomainManagerService().getZones();
		Collection activeZoneCodes = getZoneManagerService().getActiveZoneCodes();
		if (zones != null && activeZoneCodes != null) {
			Iterator _iterator = zones.iterator();
			Zone _tmpZone = null;
			while (_iterator.hasNext()) {
				_tmpZone = (Zone) _iterator.next();
				if (!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
					_iterator.remove();
				}
			}
		}
				
		Map refData = new HashMap();
		refData.put("days", domainManagerService.getDays());
		refData.put("zones", zones);
		refData.put("regions", domainManagerService.getRegions());
		
		List drivers = null;
		List helpers = null;
		List runners = null;

		WebPlanInfo model = null;
		String id = request.getParameter("id");
		if (TransStringUtil.isEmpty(id)) {
			id = request.getParameter("planRefId");
		}
		if (id != null) {
			model = (WebPlanInfo) this.getBackingObject(id, request);
		}

		String destFacility = request.getParameter("destinationFacility");
		TrnFacility deliveryFacility = locationManagerService
				.getTrnFacility(destFacility == null ? (model != null && model.getDestinationFacility() != null ? model.getDestinationFacility().getFacilityId() : destFacility) : destFacility);
		
		if(deliveryFacility != null && 
				EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(deliveryFacility.getTrnFacilityType().getName())) {
			drivers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.DRIVER.getName()
																										, EnumResourceSubType.TRAILER_DRIVER.getName()));
			helpers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.HELPER.getName()
																										, EnumResourceSubType.TRAILER_HELPER.getName()));
			runners = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRoleAndSubRole(EnumResourceType.RUNNER.getName()
																										, EnumResourceSubType.TRAILER_RUNNER.getName()));
		} else {
			drivers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.DRIVER.getName(), null, null));
			helpers = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.HELPER.getName(), null, null));
			runners = DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.RUNNER.getName(), null, null));
		}
		drivers.addAll(DispatchPlanUtil.getSortedResources(employeeManagerService.getEmployeesByRole(EnumResourceType.MANAGER.getName(), null, null)));
		refData.put("drivers", drivers);
		refData.put("helpers", helpers);
		refData.put("runners", runners);

		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
		refData.put("trnFacilitys", locationManagerService.getTrnFacilitys());
		refData.put("dispatchGroups", getDomainManagerService().getDispatchGroups());

		return refData;
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = getIdFromRequest(request);
		if (StringUtils.hasText(id)) {
			Object tmp = getBackingObject(id, request);
			return tmp;
		} else {
			return getDefaultBackingObject();
		}
	}
	
	public Object getBackingObject(String id, HttpServletRequest request) {
		WebPlanInfo planInfo = getCommand(getDispatchManagerService().getPlan(id));
		
		String planRefId = request.getParameter("planRefId");
		if(!TransStringUtil.isEmpty(planRefId)) {
			planInfo.setPlanId(null);
			planInfo.setSequence(0);
			
			planInfo.setDrivers(new ResourceList());
			planInfo.setHelpers(new ResourceList());
			planInfo.setRunners(new ResourceList());
			
			DispatchPlanUtil.setResourceInfo(planInfo, false, employeeManagerService);
		}
		return planInfo;
	}
	
	@Override
	public Object getBackingObject(String id) {		
		return null;
	}

	private WebPlanInfo getCommand(Plan plan) {

		Zone zone=null;
		if (plan.getZone() != null) {
			zone = domainManagerService.getZone(plan.getZone().getZoneCode());
			plan.setEquipmentTypes(assetManagerService.getEquipmentTypes(plan.getZone().getArea().getRegion()));
		}
		return DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService, true, null, null, null, null, null);
	}

	public Object getDefaultBackingObject() {
		return new WebPlanInfo();
	}

	public boolean isNew(Object command) {
		WebPlanInfo modelIn = (WebPlanInfo)command;
		return (TransStringUtil.isEmpty(modelIn.getPlanId()));
	}

	public String getDomainObjectName() {
		return "Plan";
	}
	
	public List saveDomainObject(HttpServletRequest request, Object command) {

		List errorList = null;
		Plan previousModel = null;
		Plan model = null;
		try {
			WebPlanInfo _command = (WebPlanInfo)command;
			model = getPlan(_command);
			previousModel = getDispatchManagerService().getPlan(model.getPlanId());
			
			getDispatchManagerService().savePlan(model, _command.getReferenceContextId());
			_command.setPlanId(model.getPlanId());
			_command.setReferenceContextId(null);
			
		} catch (Exception objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("sys.error.1001", new Object[]{this.getDomainObjectName()}));
		}
		if(errorList == null && model != null && RoutingServicesProperties.getRoutingDynaSyncEnabled()) {
			try {
				WaveUtil.recalculateWave(this.getDispatchManagerService(), previousModel, model, EnumWaveInstancePublishSrc.PLAN
								, com.freshdirect.transadmin.security.SecurityManager.getUserName(request));
			} catch(Exception e) {
				e.printStackTrace();
				errorList = new ArrayList();
				errorList.add(this.getMessage("app.error.133", new Object[]{this.getDomainObjectName()}));
			}
		}
		return errorList;
	}
	
	public void saveErrorMessage(HttpServletRequest request, Object msg) {
		List messages = (List)msg;
		if (messages != null) {
			messages.add(getMessage("app.actionmessage.109", new Object[]{}));
		}
		super.saveErrorMessage(request, msg);
	}
	
	protected void onBind(HttpServletRequest request, Object command) {

		WebPlanInfo model = (WebPlanInfo) command;
		String destFacility = request.getParameter("destinationFacility");
		Zone zone=null;

		TrnFacility deliveryFacility = locationManagerService.getTrnFacility(destFacility);
		if(deliveryFacility != null && 
				EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(deliveryFacility.getTrnFacilityType().getName())){
			model.setZoneCode("");
			model.setZoneName("");
			model.setEquipmentTypeS("");
		}
		model.setDestinationFacility(deliveryFacility);
		if(!TransStringUtil.isEmpty(model.getIsBullpen()) && "Y".equalsIgnoreCase(model.getIsBullpen())) {
			model.setZoneCode("");
			model.setZoneName("");
			model.setDestinationFacility(null);
			model.setEquipmentTypeS("");
		}

		if(!TransStringUtil.isEmpty(model.getZoneCode())) {
			zone = domainManagerService.getZone(model.getZoneCode());
		}


		if( "true".equalsIgnoreCase(model.getZoneModified()) && zone != null && zone.getArea() != null)
		{
			model.setEquipmentTypes(assetManagerService.getEquipmentTypes(zone.getArea().getRegion()));
		}
		model= DispatchPlanUtil.reconstructWebPlanInfo(model, zone, model.getDispatchGroupModified()
															,null, employeeManagerService, zoneManagerService, true);

		//set userId to command object
		model.setUserId(getUserId(request));
	}

	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		WebPlanInfo _command=(WebPlanInfo)command;
		if("true".equalsIgnoreCase(_command.getZoneModified())
				||"true".equalsIgnoreCase(_command.getDispatchGroupModified())
				   ||"true".equalsIgnoreCase(_command.getDestFacilityModified())) {
			return true;
		}
		else
			return isFormChangeRequest(request);
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

		WebPlanInfo _command = (WebPlanInfo) command;
		_command.setZoneModified("false");
		_command.setDispatchGroupModified("false");
		_command.setDestFacilityModified("false");
	}


	private Plan getPlan(WebPlanInfo command) throws Exception {
		return DispatchPlanUtil.getPlan(command);
	}

	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if (TransStringUtil.isEmpty(id)) {
			id = request.getParameter("planId");
		}
		if (TransStringUtil.isEmpty(id)) {
			id = request.getParameter("planRefId");
		}
		return id;
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(TrnFacility.class, new TrnFacilityPropertyEditor());		
	}

	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
	

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
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

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}


}
