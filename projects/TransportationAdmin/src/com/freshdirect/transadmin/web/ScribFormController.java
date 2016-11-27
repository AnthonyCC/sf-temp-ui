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

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.constants.EnumRouteType;
import com.freshdirect.transadmin.model.DispatchGroup;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ScribUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.WaveUtil;
import com.freshdirect.transadmin.web.editor.DispatchGroupPropertyEditor;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnFacilityPropertyEditor;


public class ScribFormController extends AbstractDomainFormController {
			
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private ZoneManagerI zoneManagerService;
	private LocationManagerI locationManagerService;
	private AssetManagerI assetManagerService;
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Collection zones=getDomainManagerService().getZones();
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
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
		refData.put("zones",zones);	
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(employeeManagerService.getSupervisors()));
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
		refData.put("routeTypes", EnumRouteType.getEnumList());
		refData.put("trnFacilitys", locationManagerService.getTrnFacilitys());
		refData.put("regions", getDomainManagerService().getRegions());
		refData.put("dispatchGroups", getDomainManagerService().getDispatchGroups());
		
		return refData;
	}

	public Object getBackingObject(String id) {
		// override by getBackingObject(String id, request)
		return null;
	}
	
	public Object getBackingObject(String id, HttpServletRequest request) {
		Scrib scrib = getDispatchManagerService().getScrib(id);
		if (scrib != null && scrib.getZone() != null
				&& scrib.getZone().getArea() != null
					&& scrib.getZone().getArea().getRegion() != null) {
			scrib.setEquipmentTypes(assetManagerService.getEquipmentTypes(scrib.getZone().getArea().getRegion()));
		}
		
		String scribRefId = request.getParameter("scribRefId");
		if(!TransStringUtil.isEmpty(scribRefId)) {
			scrib.setScribId(null);
			scrib.setNoOfResources(0);
			scrib.setTruckCnt(0);
		}
		return scrib;
	}

	public Object getDefaultBackingObject() {
		return new Scrib();
	}

	public boolean isNew(Object command) {
		Scrib modelIn = (Scrib)command;
		return (modelIn.getScribId() == null);
	}

	public String getDomainObjectName() {
		return "Scrib";
	}
	
	protected void onBind(HttpServletRequest request, Object command) {

		Scrib model = (Scrib) command;
		String zoneId = request.getParameter("zoneS");
		String destFacility = request.getParameter("destinationFacility");
		Zone zone = getDomainManagerService().getZone(zoneId);
		if (zone != null) {
			model.setZone(zone);
			model.setRegion(zone.getRegion());
		}
		TrnFacility deliveryFacility = locationManagerService.getTrnFacility(destFacility);
		model.setDestinationFacility(deliveryFacility);
		if(deliveryFacility != null && 
				EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(deliveryFacility.getTrnFacilityType().getName())){
			model.setZone(null);
			model.setEquipmentTypeS("");
		}		
		model = ScribUtil.reconstructScribInfo(model, zone, model.getDispatchGroupModified(), 
							employeeManagerService,	zoneManagerService, assetManagerService);	
	}

	

	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		Scrib _command = (Scrib) command;
		if("true".equalsIgnoreCase(_command.getZoneModified()) 
				|| "true".equalsIgnoreCase(_command.getDispatchGroupModified())) {
			return true;
		} else {
			return isFormChangeRequest(request);
		}
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
																							throws Exception {
		Scrib _command=(Scrib)command;
		_command.setZoneModified("false");
		_command.setDispatchGroupModified("false");
	}

	protected void preProcessDomainObject(Object domainObject) {

		Scrib modelIn = (Scrib)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getScribId()) ) {
			modelIn.setScribId(modelIn.getScribId());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = null;
		Scrib model = null;
		Scrib previousModel = null;
		try {
			model = (Scrib) domainObject;
			previousModel = getDispatchManagerService().getScrib(model.getScribId());
			
			getDispatchManagerService().saveEntity(model);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		
		if(errorList == null && model != null && RoutingServicesProperties.getRoutingDynaSyncEnabled()) {
			try {
				WaveUtil.recalculateWave(this.getDispatchManagerService(), previousModel, model
												, EnumWaveInstancePublishSrc.SCRIB
												, com.freshdirect.transadmin.security.SecurityManager.getUserName(request));		
			} catch(Exception e) {
				e.printStackTrace();
				errorList = new ArrayList();
				errorList.add(this.getMessage("app.error.133", new Object[]{this.getDomainObjectName()}));
			}
		}
		return errorList;
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
	
	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("scribId");
		if(TransStringUtil.isEmpty(id)) {
			id = request.getParameter("scribRefId");
		}
		return id;
	}
	
	
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(Region.class, new RegionPropertyEditor());
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
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	
	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
}