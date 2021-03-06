


package com.freshdirect.transadmin.web.json;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;
import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.transadmin.constants.EnumAssetElectricFleetMetric;
import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.DispatchReason;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.DlvScenarioZones;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.IssueSubType;
import com.freshdirect.transadmin.model.IssueType;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScenarioZonesId;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.UserPref;
import com.freshdirect.transadmin.model.WaveInstancePublish;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.DispatchStatus;
import com.freshdirect.transadmin.web.model.DispatchStatusList;
import com.freshdirect.transadmin.web.model.IssueSubTypeCommand;
import com.freshdirect.transadmin.web.model.ScenarioZoneCommand;
import com.freshdirect.transadmin.web.model.WavePublishValidationResult;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.web.model.ZoneSupervisorCommand;
import com.freshdirect.transadmin.web.util.TransWebUtil;

public class DispatchProviderController extends JsonRpcController implements IDispatchProvider {	

	private static final Category LOGGER = LoggerFactory.getInstance( DispatchProviderController.class );
	
	private DispatchManagerI dispatchManagerService;

	private DomainManagerI domainManagerService;
	
	private LocationManagerI locationManagerService;

	private LogManagerI logManager;

	private ZoneManagerI zoneManagerService;
	
	private EmployeeManagerI employeeManagerService;
	
	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
		
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}
	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(
			DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public int updateRouteMapping(String routeDate, String cutOffId,
			String sessionId, boolean isDepot) {
		int result = 0;
		
		try {
			result = getDispatchManagerService().updateRouteMapping(
					TransStringUtil.getDate(routeDate), cutOffId, sessionId,
					isDepot);
		} catch (ParseException parseExp) {
			parseExp.printStackTrace();
			// Do Nothing Return 0;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection getActiveRoute(String date, String zoneCode, boolean isTrailerRoute) {
		Collection results = null;

		try {
			if (zoneCode == null || zoneCode.trim().length() == 0) {
				Collection c = domainManagerService.getAdHocRoutes();
				if (c != null) {
					results = new ArrayList();
					Iterator iterator = c.iterator();
					while (iterator.hasNext()) {
						TrnAdHocRoute info = (TrnAdHocRoute) iterator.next();
						results.add(info.getRouteNumber());
					}
				}
			} else {
				Collection routes = domainManagerService.getRoutes(TransStringUtil.getServerDate(date));
				if (routes != null) {
					results = new ArrayList();
					Iterator iterator = routes.iterator();
					while (iterator.hasNext()) {
						ErpRouteMasterInfo _route = (ErpRouteMasterInfo) iterator.next();
						if(isTrailerRoute && zoneCode.equals(_route.getRouteNumber().substring(1, 4))){							
							results.add(_route.getRouteNumber());
						} else if (zoneCode.equals(_route.getZoneNumber())) {
							results.add(_route.getRouteNumber());
						}
					}
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return results;
	}

	public Collection getActivityLog(String date, String view) {
		try {
			Date fromDate = TransStringUtil.getDate(date);
			Date toDate = TransStringUtil.getDate(date);
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			c.add(Calendar.SECOND, -1);
			toDate = c.getTime();

			fromDate = new Timestamp(fromDate.getTime());
			toDate = new Timestamp(toDate.getTime());
			Collection list = logManager.getLogs(fromDate, toDate, view);
			
			return list;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean validateAccessCode(String accessCode)
	{
		String hashedAccessCode = MD5Hasher.hash(accessCode);
		if(hashedAccessCode != null && hashedAccessCode.equals(TransportationAdminProperties.getAccessKey())) {
			return true;
		}
		return false;
		
	}
	
	protected Map getAreaMapping(Collection areaLst) {
		Map areaMapping = new HashMap();
		if(areaLst != null) {
			Iterator iterator = areaLst.iterator();
			TrnArea tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnArea)iterator.next();
				areaMapping.put(tmpModel.getCode(), tmpModel);
			}
		}
		return areaMapping;
	}
	
	public int updateUserPref(String key,String value)
	{
		try {
			String userName=com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
			UserPref pref=new UserPref();
			pref.setUserId(userName);
			pref.setKey(key);
			pref.setValue(value);
			Collection list=new ArrayList();
			list.add(pref);
			dispatchManagerService.removeEntity(list);
			dispatchManagerService.saveEntityEx(pref);	
			TransWebUtil.updatePref(getHttpServletRequest(), pref);
			return 1;
		} catch (Exception e) 
		{
			e.printStackTrace();
			return 0;
		}
	}

	public String getUserPref(String key) 
	{
		String value=TransWebUtil.getUserPref(getHttpServletRequest(), key,dispatchManagerService);
		if(value==null) value="";
		return value;
	}
	
	public Collection getActiveZones(String date)
	{
		Collection zones=domainManagerService.getZones();
		try {
			date=TransStringUtil.getServerDate(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes(date);
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
    	List result=new ArrayList();
    	for(Iterator i=zones.iterator();i.hasNext();)
    	{
    		Zone tmpZone =(Zone)i.next();
    		Zone z=new Zone();
    		z.setZoneCode(tmpZone.getZoneCode());
    		z.setName(tmpZone.getDisplayName());  
    		result.add(z);
    	}
    	return result;
	}
	public int addReasonCode(String reason) 
	{
		try {
			DispatchReason r=new DispatchReason();
			r.setReason(reason);
			dispatchManagerService.saveEntity(r);
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	public Collection getReasonCode(boolean active) 
	{
		try {
			return dispatchManagerService.getDispatchReasons(active);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public int setReasonCode(String code,String reason, boolean enable) {
		try {
			DispatchReason r=new DispatchReason();
			r.setCode(code);
			r.setReason(reason);
			r.setActive(enable);
			dispatchManagerService.saveEntity(r);
			return 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public List<WebPlanInfo> getPlanForResource(String date, String resourceId, String planId) {
		
		List planInfos = new ArrayList();
		List<WebPlanInfo> result = null; new ArrayList<WebPlanInfo>();
		try {
			Collection plans = dispatchManagerService.getPlanForResource(TransStringUtil.getServerDate(date), resourceId);
						
			Iterator it=plans.iterator();
			while(it.hasNext()) {

				Plan plan=(Plan)it.next();
				Zone zone=null;
				if(plan.getZone()!=null) {
					zone = domainManagerService.getZone(plan.getZone().getZoneCode());
				}
				WebPlanInfo planInfo = DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService, true,null,null,null,null,null);
				planInfos.add(planInfo);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator _itr = planInfos.iterator();
		while(_itr.hasNext()) {
			WebPlanInfo _tmpPlan = (WebPlanInfo)_itr.next();
			if(!_tmpPlan.getPlanId().equalsIgnoreCase(planId) || planId == null) {
				
				if(result == null) {
					result = new ArrayList<WebPlanInfo>();					
				}
				result.add(_tmpPlan);
			}
		}
		return result;
	}
	
	public List<DispatchCommand> getDispatchForResource(String date, String resourceId, String dispatchId) {
		
		List dispatchInfos = new ArrayList();
		List<DispatchCommand> result = null; new ArrayList<DispatchCommand>();
		
		try {
			Collection dispatches = dispatchManagerService.getDispatchForResource(TransStringUtil.getServerDate(date), resourceId);
						
			Iterator it = dispatches.iterator();
			while(it.hasNext()) {

				Dispatch dispatch = (Dispatch) it.next();
				Zone zone=null;
				if(dispatch.getZone()!=null) {
					zone = domainManagerService.getZone(dispatch.getZone().getZoneCode());
				}
				DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService,null,null,null,null,null,null,null,null,null);
				dispatchInfos.add(command);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator _itr = dispatchInfos.iterator();
		while(_itr.hasNext()) {
			DispatchCommand _tmpDispatch = (DispatchCommand)_itr.next();
			if(!_tmpDispatch.getDispatchId().equalsIgnoreCase(dispatchId) || dispatchId == null) {
				if(result == null) {
					result = new ArrayList<DispatchCommand>();					
				}
				result.add(_tmpDispatch);
			}
		}
		return result;
	}
	
	private boolean getDispatchForAsset(Date deliveryDate, String dispatchId
														, Date firstDeliveryTime, Collection dispatches) throws ParseException {
		
		if(dispatches != null) {
			Iterator itr = dispatches.iterator();
			while(itr.hasNext()) {
				Dispatch dispatch = (Dispatch)itr.next();
				if((dispatchId == null || !dispatchId.equalsIgnoreCase(dispatch.getDispatchId()))
						//&& DispatchPlanUtil.getShift(deliveryDate, firstDeliveryTime).equalsIgnoreCase(DispatchPlanUtil.getShift(dispatch.getDispatchDate(), dispatch.getFirstDlvTime()))
						&& dispatch.getCheckedInTime() == null) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean hasDispatchForGPS(String dispatchDate, String dispatchId
														, String firstDeliveryTime, String assetId) {
		
		try {
			Date _deliveryDate = TransStringUtil.getDate(dispatchDate);
			Date _firstDeliveryTime = TransStringUtil.getServerTime(firstDeliveryTime);
			return getDispatchForAsset(_deliveryDate, dispatchId, _firstDeliveryTime,
											this.getDispatchManagerService().getDispatchForGPS(_deliveryDate, assetId));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	
	public boolean hasDispatchForEZPass(String dispatchDate, String dispatchId
													, String firstDeliveryTime, String assetId) {
		try {
			Date _deliveryDate = TransStringUtil.getDate(dispatchDate);
			Date _firstDeliveryTime = TransStringUtil.getServerTime(firstDeliveryTime);
			return getDispatchForAsset(_deliveryDate, dispatchId, _firstDeliveryTime,
											this.getDispatchManagerService().getDispatchForEZPass(_deliveryDate, assetId));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	
	public boolean hasDispatchForMotKit(String dispatchDate, String dispatchId
														, String firstDeliveryTime, String assetId) {
		try {
			Date _deliveryDate = TransStringUtil.getDate(dispatchDate);
			Date _firstDeliveryTime = TransStringUtil.getServerTime(firstDeliveryTime);
			return getDispatchForAsset(_deliveryDate, dispatchId, _firstDeliveryTime,
											this.getDispatchManagerService().getDispatchForMotKit(_deliveryDate, assetId));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	
	public int addScenarioDayMapping(String sCode, String sDay, String sDate, String cutoffS, String startTimeS, String endTimeS) {
		int result = 0;
		Date svcdate=null;
		BigDecimal svcDay=null;
		Date cutoff = null, startTime = null, endTime = null;
		if(!"".equalsIgnoreCase(sDay)){
			svcDay=new BigDecimal(sDay);
		}
		
		DlvServiceTimeScenario scenario=new DlvServiceTimeScenario();
		scenario.setCode(sCode);		
		try {
			if(!"".equalsIgnoreCase(sDate))
				svcdate=TransStringUtil.getDate(sDate);
			if(svcdate==null && svcDay==null){
				getDispatchManagerService().deleteDefaultScenarioDay(sDate, sDay);
			}
			if(!TransStringUtil.isEmpty(cutoffS))
				cutoff = TransStringUtil.getServerTime(cutoffS);
			if(!TransStringUtil.isEmpty(startTimeS))
				startTime = TransStringUtil.getServerTime(startTimeS);
			if(!TransStringUtil.isEmpty(endTimeS))
				endTime = TransStringUtil.getServerTime(endTimeS);
			
			DlvScenarioDay scenarioDay=new DlvScenarioDay(svcDay,svcdate,scenario, cutoff, startTime, endTime);
			result = getDispatchManagerService().addScenarioDayMapping(
					scenarioDay);
		}catch(DataIntegrityViolationException ex){
			ex.printStackTrace();
			return result=2;
		}catch (ParseException parseExp) {
			parseExp.printStackTrace();
			// Do Nothing Return 0;
		}
		return result;
	}
	
	public boolean deleteServiceTimeScenario(String sCode) {
		
		DlvServiceTimeScenario scenario=null;
		String[] scenarioCodes = StringUtil.decodeStrings(sCode);
		List processedList = new ArrayList();
		List scenarioLst = new ArrayList();
		if(scenarioCodes != null && scenarioCodes.length > 0) {
			for(String _sCode : scenarioCodes) {
				scenario = getLocationManagerService().getServiceTimeScenario(_sCode);
				if(scenario.getScenarioDays()!=null && scenario.getScenarioDays().size()==0
						&& scenario.getScenarioZones().size()==0
							&& !processedList.contains(_sCode)){
					processedList.add(_sCode);
					scenarioLst.add(scenario);
				}
			}
		}
		if(scenarioLst.size()>0){
			getLocationManagerService().removeEntity(scenarioLst);
			return true;
		}
		return false;
	}
	
	public Collection getScenarioZones(String scenarioId){
		Collection tempLst = Collections.EMPTY_LIST;
		Collection zoneLst = new ArrayList();
		if(scenarioId!=null){
			tempLst = locationManagerService.getDlvScenarioZones(scenarioId);
			for (Iterator iterator = tempLst.iterator(); iterator.hasNext();) {
				
				DlvScenarioZones dlvScenarioZone = (DlvScenarioZones) iterator.next();
				zoneLst.add(new ScenarioZoneCommand(dlvScenarioZone));							
			}
		}		
		return zoneLst;	
	}
	
	public boolean doScenarioZone(String id, String[][] zone){
			
		List selZoneLst= new ArrayList();
		
		for(int i=0;i<zone.length;i++) {
			
			ScenarioZonesId zid = new ScenarioZonesId();
			zid.setScenarioId(id);
			zid.setZoneCode(TransStringUtil.isEmpty(zone[i][0]) ?  IDeliveryService.CONSTANT_ALLZONES : zone[i][0]);
			try {
				DlvScenarioZones sZone = new DlvScenarioZones(zid);
				sZone.setServiceTimeType(TransStringUtil.isEmpty(zone[i][1]) ?  null : zone[i][1]);
				sZone.setServiceTimeOperator(TransStringUtil.isEmpty(zone[i][2]) ?  null : EnumArithmeticOperator.getEnum(zone[i][2]));
				sZone.setServiceTimeAdjustment(TransStringUtil.isEmpty(zone[i][3]) ?  null : new BigDecimal(zone[i][3]));
				selZoneLst.add(sZone);				
			} catch(NumberFormatException exp) {
				return false;
			}
		}
		if(id != null) {
			getLocationManagerService().removeEntity(
				getLocationManagerService().getDlvScenarioZones(id));
		}
		if(selZoneLst.size() > 0) {
			getLocationManagerService().saveEntityList(selZoneLst);
		}
		return true;
	}
	
	public boolean addScribLabel(String date,String label, String checked){
		try{
			Date _scribDate = null;
			List LabelLst= new ArrayList();
			if (!TransStringUtil.isEmpty(date)&& !TransStringUtil.isEmpty(label)) {
				_scribDate = TransStringUtil.getDate(date);
				
				if("true".equals(checked)){
					String[] dates = TransStringUtil.getDates(date, "All");
					if(dates!=null){
						for(int i=0;i<dates.length;i++){
							ScribLabel _tempLabel = dispatchManagerService.getScribLabelByDate(TransStringUtil.getServerDate(dates[i]));
							if(_tempLabel!=null)
								getDispatchManagerService().removeEntityEx(_tempLabel);
							
								ScribLabel _sLabel = new ScribLabel(TransStringUtil.getDate(dates[i]), label);
								LabelLst.add(_sLabel);															
						}
						if(LabelLst.size()>0) 
							dispatchManagerService.saveEntityList(LabelLst);
						return true;
					}
				}else{
					ScribLabel _tempLabel = dispatchManagerService.getScribLabelByDate(TransStringUtil.getServerDate(date));
					
					if(_tempLabel!=null)
						getDispatchManagerService().removeEntityEx(_tempLabel);
						
						ScribLabel _sLabel = new ScribLabel(_scribDate, label);						
						dispatchManagerService.saveEntity(_sLabel);
					return true;
				}
			}
		}catch(DataIntegrityViolationException ex){
			ex.printStackTrace();
			return false;
		}catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return true;
	}
	
	public String getScribLabel(String Date) {
		
		ScribLabel slabel;
		try {
			slabel = dispatchManagerService.getScribLabelByDate(TransStringUtil.getServerDate(Date));
			if(slabel != null) {
				return slabel.getScribLabel();
			}
		} catch (ParseException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	public Collection getDatesByScribLabel(String slabel) {
		Collection dates = new ArrayList();
		Collection labels = new ArrayList();
		try {
			labels = dispatchManagerService.getDatesByScribLabel(slabel);
			
			for (Iterator<ScribLabel> iterator = labels.iterator(); iterator.hasNext();) {
				ScribLabel _sl = iterator.next();
				String d = TransStringUtil.getDate(_sl.getDate());
				dates.add(d);
			}			
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return dates;
	}
	
	public String getTotalNoTrucksByDate(String date) {
		Collection scribs = new ArrayList();
		int count = 0;
		try {
			scribs = dispatchManagerService.getScribList(TransStringUtil.getServerDate(date));
			if(scribs.size() > 0) {
				for (Iterator<Scrib> itr = scribs.iterator(); itr.hasNext();) {
					Scrib _s = itr.next();
					count = count+_s.getTruckCnt();
				}				
				return Integer.toString(count);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return Integer.toString(count);
	}
	
	public Collection getDefaultZoneSupervisors(String zoneId){
		Collection tempLst = Collections.EMPTY_LIST;
		List zoneLst = new ArrayList();
		if(zoneId!=null){
			tempLst = locationManagerService.getDefaultZoneSupervisors(zoneId);
			for (Iterator iterator = tempLst.iterator(); iterator.hasNext();) {
				
				ZoneSupervisor _zoneSupervisor = (ZoneSupervisor) iterator.next();
				WebEmployeeInfo emp = employeeManagerService.getEmployeeEx(_zoneSupervisor.getSupervisorId());
				if(emp != null && emp.getEmpInfo() != null) {
				_zoneSupervisor.setSupervisorName(emp.getEmpInfo().getSupervisorName());
				_zoneSupervisor.setSupervisorId(emp.getEmpInfo().getEmployeeId());
				zoneLst.add(new ZoneSupervisorCommand(_zoneSupervisor));							
				} else {
					locationManagerService.removeEntityEx(_zoneSupervisor);
			}
		}
		}
		Collections.sort(zoneLst);
		return zoneLst;	
	}
	
	public boolean doZoneDefaultSupervisor(String id, String[][] zone){
		
		List selZoneSupervisorLst= new ArrayList();
		
		for(int i=0;i<zone.length;i++) {
					
			try {
				ZoneSupervisor _supervisor= new ZoneSupervisor();
				_supervisor.setDayPart(TransStringUtil.isEmpty(zone[i][0])? null:zone[i][0]);
				_supervisor.setSupervisorId(TransStringUtil.isEmpty(zone[i][3])? null:zone[i][3]);
				_supervisor.setEffectiveDate(TransStringUtil.isEmpty(zone[i][2])? null:TransStringUtil.getServerDateString1(zone[i][2]));
				_supervisor.setZoneCode(id);
				selZoneSupervisorLst.add(_supervisor);				
			} catch(ParseException exp) {
				return false;
			}
		}
		if(id != null) {
			getLocationManagerService().removeEntity(
				getLocationManagerService().getDefaultZoneSupervisors(id));
		}
		if(selZoneSupervisorLst.size() > 0) {
			try{
				getLocationManagerService().saveEntityList(selZoneSupervisorLst);
			}catch(Exception ex){
				return false;
			}
		}
		return true;
	}
	
	public boolean publishWave(String weekOf, String dayOfWeek) {
		Date _dlvDate = null;
		try {
			_dlvDate = getDate(weekOf, dayOfWeek);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (_dlvDate != null) {
			String actionBy = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
			this.getDispatchManagerService().publishWaveInstance(_dlvDate, actionBy, EnumWaveInstancePublishSrc.SCRIB);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean publishWave(String deliveryDate) {
		Date _dlvDate = null;
		try {
			_dlvDate = TransStringUtil.getDate(deliveryDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (_dlvDate != null) {
			String actionBy = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
			this.getDispatchManagerService().publishWaveInstance(_dlvDate, actionBy, EnumWaveInstancePublishSrc.PLAN);
			return true;
		} else {
			return false;
		}
	}
	
	public WavePublishValidationResult canPublishWave(String deliveryDate) {
		WavePublishValidationResult result = new WavePublishValidationResult();
		Date _dlvDate = null;
		try {
			_dlvDate = TransStringUtil.getDate(deliveryDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (_dlvDate != null) {
			Collection wavePublishes = this.getDispatchManagerService().getWaveInstancePublish(_dlvDate);
			if (wavePublishes != null && wavePublishes.size() > 0) {
				WaveInstancePublish _publishInfo = (WaveInstancePublish) wavePublishes.iterator().next();
				result.setHasPerviousPublish(true);
				result.setPreviousPublishPlan(_publishInfo.getSource().equals(EnumWaveInstancePublishSrc.PLAN));
				result.setPreviousPublishScrib(_publishInfo.getSource().equals(EnumWaveInstancePublishSrc.SCRIB));
			}
		}
		return result;
	}
	
	public WavePublishValidationResult canPublishWave(String weekOf, String dayOfWeek) {
		WavePublishValidationResult result = new WavePublishValidationResult();
		Date _dlvDate = null;
		try {
			_dlvDate = getDate(weekOf, dayOfWeek);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (_dlvDate != null) {
			Collection wavePublishes = this.getDispatchManagerService().getWaveInstancePublish(_dlvDate);
			if (wavePublishes != null && wavePublishes.size() > 0) {
				WaveInstancePublish _publishInfo = (WaveInstancePublish) wavePublishes.iterator().next();
				result.setHasPerviousPublish(true);
				result.setPreviousPublishPlan(_publishInfo.getSource().equals(EnumWaveInstancePublishSrc.PLAN));
				result.setPreviousPublishScrib(_publishInfo.getSource().equals(EnumWaveInstancePublishSrc.SCRIB));
			}
		}
		return result;
	}
	
	public int validatePlanGenAccessCode(String accessCode) {
		String hashedAccessCode = MD5Hasher.hash(accessCode);
		if(hashedAccessCode != null && hashedAccessCode.equals(TransportationAdminProperties.getGeneratePlanFeatureAccessKey())) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private Date getDate(String weekOf, String dayOfWeek) throws Exception {
		if (!TransStringUtil.isEmpty(weekOf) && !TransStringUtil.isEmpty(dayOfWeek)) {

			String[] sourceDates = TransStringUtil.getDates(weekOf, dayOfWeek);
			if (sourceDates != null && sourceDates.length > 0) {
				return TransStringUtil.getDate(sourceDates[0]);
			}
		}
		return null;
	}
	
	public boolean addIssueType(String date, String createdBy, String name,
			String desc, String checked) {
		try {
			IssueType _issueType = new IssueType();
			if (!TransStringUtil.isEmpty(date)
					&& !TransStringUtil.isEmpty(createdBy)
					&& !TransStringUtil.isEmpty(name)) {

				_issueType.setCreatedBy(createdBy);
				_issueType.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				_issueType.setIsActive("true".equals(checked) ? 1 : 0);
				_issueType.setIssueTypeName(name);
				_issueType.setIssueTypeDescription(desc);

				IssueType issueType = domainManagerService.getIssueType(name.trim());

				if (issueType != null)
					getDispatchManagerService().removeEntityEx(issueType);

				dispatchManagerService.saveEntityEx(_issueType);
				return true;
			}
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addIssueSubType(String date, String createdBy, String name,
			String desc, String checked, String issueTypeName) {
		try {
			IssueSubType _issueSubType = new IssueSubType();
			if (!TransStringUtil.isEmpty(date)
					&& !TransStringUtil.isEmpty(createdBy)
					&& !TransStringUtil.isEmpty(name)) {

				_issueSubType.setCreatedBy(createdBy);
				_issueSubType.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				_issueSubType.setIsActive("true".equals(checked) ? 1 : 0);
				_issueSubType.setIssueSubTypeName(name);
				_issueSubType.setIssueSubTypeDescription(desc);
				
				IssueType _issueType = domainManagerService.getIssueType(issueTypeName.trim());
				_issueSubType.setIssueType(_issueType);
				
				dispatchManagerService.saveEntityEx(_issueSubType);
				return true;
			}
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public List<IssueSubTypeCommand> getIssueSubTypes(String issueTypeName) {
		List<IssueSubType> issueSubTypes = null;
		List<IssueSubTypeCommand> result = new ArrayList<IssueSubTypeCommand>();
		try {
			if (!TransStringUtil.isEmpty(issueTypeName)) {		

				IssueType issueType = domainManagerService.getIssueType(issueTypeName.trim());

				if (issueType != null)
					issueSubTypes = new ArrayList<IssueSubType>(issueType.getIssueSubTypes());
				
				for (Iterator iterator = issueSubTypes.iterator(); iterator.hasNext();) {
					
					IssueSubType issueSubType = (IssueSubType) iterator.next();
					result.add(new IssueSubTypeCommand(issueSubType));							
				}
				
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String saveVIRRecord(String createDate, String truckNumber, String vendor
			, String driver, String createdBy, String socStart, String socEnd, String socReeferStart, String socReeferEnd
			, String[][] recordIssues) {
		
		return domainManagerService.saveVIRRecord(createDate, truckNumber, vendor, driver, createdBy, socStart, socEnd, socReeferStart, socReeferEnd, recordIssues);
	}
	
	public boolean doRejectMaintenanceIssue(String id, String userId){
		MaintenanceIssue issue = null;
		try{
			issue = domainManagerService.getMaintenanceIssue(id);
			if(issue != null){
				issue.setIssueStatus(EnumIssueStatus.REJECTED.getName());
				issue.setServiceStatus(EnumServiceStatus.ACTIVE.getDescription());
				issue.setVerifiedBy(userId);
				domainManagerService.saveEntity(issue);
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public String getTruckVendorInfo(String truckNumber){
		String vendorName = "";
		try{
			Collection asset = getAssetManagerService().getAsset(truckNumber, "TRUCK");
			if(asset == null || (asset != null && asset.size() == 0))
				asset = getAssetManagerService().getAsset(truckNumber, "TRAILER");
			if(asset != null){
				Set assetAttributes = ((Asset)asset.iterator().next()).getAssetAttributes();
				if(assetAttributes != null && assetAttributes.size() > 0){
					Iterator<AssetAttribute> itr = assetAttributes.iterator();
					while(itr.hasNext()){
						AssetAttribute attribute = itr.next();
						if("Vendor - Truck".equalsIgnoreCase(attribute.getId().getAttributeType())
								|| "Vendor - TRAILER".equalsIgnoreCase(attribute.getId().getAttributeType())){
							vendorName = attribute.getAttributeValue();
							break;
						}						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return vendorName;
	}
	
	public String[] getFacilityInfo(String orgFacilityId, String destFacilityId){
		String[] result = new String[2];
		TrnFacility orgFacility = null;
		TrnFacility destFacility = null;
		if(orgFacilityId != null && !"".equals(orgFacilityId))
				orgFacility = locationManagerService.getTrnFacility(orgFacilityId);
		if(destFacilityId != null && !"".equals(destFacilityId))
				destFacility = locationManagerService.getTrnFacility(destFacilityId);
		
		if(orgFacility != null && orgFacility.getTrnFacilityType() != null)
			result[0] = orgFacility.getTrnFacilityType().getName();

		if(destFacility != null && destFacility.getTrnFacilityType() != null)
			result[1] = destFacility.getTrnFacilityType().getName();

		return result;
	}

	public boolean updateDispatchStatus(DispatchStatusList dispatches, String userId){
		
		if(dispatches != null){
			LOGGER.info("Updating Dispatch Status -> Started ");
			Set dispatchSet = new HashSet();
			try{
				Dispatch tmpDispatch = null;
				for(DispatchStatus _dispatch : dispatches.getDispatchStatus() ) {
					tmpDispatch = dispatchManagerService.getDispatch(_dispatch.getDispatchId());
					LOGGER.info("Setting status to Dispatch object -> "+ tmpDispatch);
					if(tmpDispatch != null){
						tmpDispatch.setUserId(userId);						
						if(_dispatch.isPhoneAssigned()) {
							tmpDispatch.setPhonesAssigned(Boolean.TRUE);
						}
						if(_dispatch.isKeysReady()) {
							tmpDispatch.setKeysReady(Boolean.TRUE);
						}
						if(_dispatch.isDispatched()) {
							tmpDispatch.setDispatchTime(TransStringUtil.getServerTime(TransStringUtil.getServerTime(new Date())));
							tmpDispatch.setConfirmed(Boolean.TRUE);
						}
						if(_dispatch.isCheckedIn()){
							tmpDispatch.setCheckedInTime(TransStringUtil.getServerTime(TransStringUtil.getServerTime(new Date())));
						}
						if(_dispatch.isKeysIn()){
							tmpDispatch.setKeysIn(Boolean.TRUE);
						}
						LOGGER.info("Adding dispatch object to save list -> "+ tmpDispatch);
						dispatchSet.add(tmpDispatch);
					}
				}
				if(dispatchSet.size() > 0) {
					LOGGER.info("Dispatch list size -> "+ dispatchSet.size());
					dispatchManagerService.saveEntityList(dispatchSet);
				}
			} catch(ParseException e){
				e.printStackTrace();
				return false;
			} catch(Exception e){
				e.printStackTrace();
				return false;
			}
			LOGGER.info("Updating Dispatch Status -> Completed ");
		}
		
		return true;
	}

	@Override
	public String generateCommunityReport(String routeDate, String cutOff) {
		// TODO Auto-generated method stub
		return null;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getElectricFleetMetrics(String truckNumber) {
		Map electricFleetMapping = new HashMap();
		try {
			Collection asset = getAssetManagerService().getAsset(truckNumber, "TRUCK");
			if (asset != null && asset.size() > 0) {
				Set assetAttributes = ((Asset) asset.iterator().next()).getAssetAttributes();
				if (assetAttributes != null && assetAttributes.size() > 0) {
					Iterator<AssetAttribute> itr = assetAttributes.iterator();
					while (itr.hasNext()) {
						AssetAttribute attribute = itr.next();
						if (EnumAssetElectricFleetMetric.CHASSIS_ENGINE_TYPE.getDesc().equalsIgnoreCase(attribute.getId().getAttributeType())) {
							electricFleetMapping.put(EnumAssetElectricFleetMetric.CHASSIS_ENGINE_TYPE.getName(), attribute.getAttributeValue());
						} else if (EnumAssetElectricFleetMetric.REEFER_TYPE.getDesc().equalsIgnoreCase(attribute.getId().getAttributeType())) {
							electricFleetMapping.put(EnumAssetElectricFleetMetric.REEFER_TYPE.getName(), attribute.getAttributeValue());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return electricFleetMapping;
	}
	
	public String getRegionFacility(String regionCode){
		String regionFacilityId = null;
		Region region = null;
		
		if(regionCode != null && !"".equals(regionCode)) {
			region = domainManagerService.getRegion(regionCode);
			if(region != null && region.getOriginFacility() != null) {
				regionFacilityId = region.getOriginFacility().getFacilityId();
			}			
		}
		return regionFacilityId;
	}
	public boolean updateMuniMeterCardDetails(String flag, String value, String status, String dispatchId, String userId){
		
		return dispatchManagerService.updateMuniMeterCardDetails(flag, value, status, dispatchId, userId);
	}
	public String getDialogDisplayFlag(String dialogFlag, String dispatchId){
		
		return dispatchManagerService.getDialogDisplayFlag(dialogFlag, dispatchId);
	}
	

}