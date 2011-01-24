package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.WaveInstancePublish;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.ScribUtil;
import com.freshdirect.transadmin.util.TransStringUtil;

public class ScribFormController extends AbstractDomainFormController {
			
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private ZoneManagerI zoneManagerService;
	
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

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
		return refData;
	}

	public Object getBackingObject(String id) {
		return getDispatchManagerService().getScrib(id);
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
		Zone zone = getDomainManagerService().getZone(zoneId);
		if(zone!=null)
		{
			model.setZone(zone);
			model.setRegion(zone.getRegion());
		}
		model= ScribUtil.reconstructWebPlanInfo(model,zone,model.getFirstDeliveryTimeModified(),
				employeeManagerService,zoneManagerService);		
	}

	

	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {

		Scrib _command = (Scrib) command;
		if("true".equalsIgnoreCase(_command.getZoneModified())|| "true".equalsIgnoreCase(_command.getFirstDeliveryTimeModified())) {
			return true;
		}
		else
			return isFormChangeRequest(request);
	}

	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
																							throws Exception {
		Scrib _command=(Scrib)command;
		_command.setZoneModified("false");
		_command.setFirstDeliveryTimeModified("false");
	}

	protected void preProcessDomainObject(Object domainObject) {

		Scrib modelIn = (Scrib)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getScribId()) ) {
			modelIn.setScribId(modelIn.getScribId());
		}
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

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
		
		if(errorList == null && model != null) {
			Map<Date, Set<String>> deliveryMapping = new HashMap<Date, Set<String>>();
			if(previousModel != null && previousModel.getZone() != null) {
				if(!deliveryMapping.containsKey(previousModel)) {
					deliveryMapping.put(previousModel.getScribDate(), new HashSet<String>());
				}
				deliveryMapping.get(previousModel.getScribDate()).add(previousModel.getZone().getZoneCode());
			}
			if(model != null && model.getZone() != null) {
				if(!deliveryMapping.containsKey(model)) {
					deliveryMapping.put(model.getScribDate(), new HashSet<String>());
				}
				deliveryMapping.get(model.getScribDate()).add(model.getZone().getZoneCode());
			}
			String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
			recalculateWave(deliveryMapping,  userId);			
		}
		return errorList;
	}
	 
	private void recalculateWave(Map<Date, Set<String>> deliveryMapping, String userId) {
		if(deliveryMapping != null) {
			for(Map.Entry<Date, Set<String>> deliveryMapEntry : deliveryMapping.entrySet()) {
				Collection wavePublishes = getDispatchManagerService().getWaveInstancePublish(deliveryMapEntry.getKey());
				if(wavePublishes != null && wavePublishes.size() > 0) {
					WaveInstancePublish wavePublish = (WaveInstancePublish)wavePublishes.iterator().next();
					if(wavePublish.getSource() != null && wavePublish.getSource().equals(EnumWaveInstancePublishSrc.SCRIB)) {
						this.getDispatchManagerService().recalculateWaveFromScrib(deliveryMapEntry.getKey()
																					, deliveryMapEntry.getValue()
																					, userId);
					}
				}
			}
		}
	}

	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("scribId");
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
	
}