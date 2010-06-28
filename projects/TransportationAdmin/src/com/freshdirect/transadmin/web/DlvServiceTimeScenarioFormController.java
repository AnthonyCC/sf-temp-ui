package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.DlvScenarioZones;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.GeoRestrictionDays;
import com.freshdirect.transadmin.model.GeoRestrictionDaysId;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.ScenarioZonesId;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.EnumLogicalOperator;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvServiceTimeScenarioFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());				
		refData.put("zonetypes", getDomainManagerService().getZoneTypes());
		refData.put("balancebys", getDomainManagerService().getBalanceBys());
		refData.put("zones", getDomainManagerService().getActiveZones());
		refData.put("serviceTimeOperators", (List)EnumLogicalOperator.getEnumList());
		refData.put("DayOfWeeks", (List) EnumDayOfWeek.getEnumList());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getLocationManagerService().getServiceTimeScenario(id);
	}
	
	public Object getDefaultBackingObject() {
		DlvServiceTimeScenario scenario = new DlvServiceTimeScenario();
		scenario.setIsNew("true");
		return scenario;
	}
	
	public boolean isNew(Object command) {
		DlvServiceTimeScenario modelIn = (DlvServiceTimeScenario)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Service Time Scenario";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvServiceTimeScenario modelIn = (DlvServiceTimeScenario)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCode()) ) {
			modelIn.setCode(modelIn.getCode());
		}
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public String[] parseRestElelemntLink(String restLink) {
		if (restLink == null || restLink.trim().length() == 0)
			return null;

		StringTokenizer tokens = new StringTokenizer(restLink.substring(1), "$");
		String tmp[] = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreElements()) {
			tmp[i++] = (String) tokens.nextElement();
		}
		return tmp;
	}
	
	protected void onBind(HttpServletRequest request, Object command) {

		DlvServiceTimeScenario model = (DlvServiceTimeScenario) command;
		
		String scenarioDtlSizeStr = request.getParameter("restrictionListSize");
		String scenarioId = request.getParameter("code");
		String scenarioLinkStr = request.getParameter("restrictionLinkStr");
		String scenarioIndexStr[] = parseRestElelemntLink(scenarioLinkStr);

		Set scenarioZonesList = new HashSet();
		if (scenarioIndexStr != null && scenarioIndexStr.length > 0) {
			int restDtlSize = Integer.parseInt(scenarioDtlSizeStr);
			// create scenarioToZone detail list from the request
			for (int i = 0; i < scenarioIndexStr.length; i++) {
				int indexSize = Integer.parseInt(scenarioIndexStr[i]);
				String zonecode = request.getParameter("attributeList["
						+ (indexSize) + "].zoneCode");
				String sTimeType = request.getParameter("attributeList["
						+ (indexSize) + "].sTimeType");
				String sTimeOverride = request.getParameter("attributeList["
						+ (indexSize) + "].sTimeOverride");
				String sTimeOperator = request.getParameter("attributeList["
						+ (indexSize) + "].sTimeOperator");
				String sTimeAdjustment = request.getParameter("attributeList["
						+ (indexSize) + "].sTimeAdjustment");

				if (null == zonecode || "".equals(zonecode)) { // tbr
					break;
				}
				if ((null == sTimeType || "".equals(sTimeType))&&(null == sTimeOverride || "".equals(sTimeOverride))&&
							(null == sTimeOperator || "".equals(sTimeOperator))&&(null == sTimeAdjustment || "".equals(sTimeAdjustment))) { 
					break;
				}
				
				BigDecimal objsTimeOverride = new BigDecimal(sTimeOverride);
				BigDecimal objsTimeAdjustment = new BigDecimal(sTimeAdjustment);
				ScenarioZonesId Id = new ScenarioZonesId(scenarioId, zonecode);
				
				DlvScenarioZones zone = null;
				try {
					zone = new DlvScenarioZones(Id, sTimeType,objsTimeOverride,EnumLogicalOperator
							.getEnum(sTimeOperator), objsTimeAdjustment);
					scenarioZonesList.add(zone);
				} finally {

				}
			}
		}
		model.setScenarioZones(scenarioZonesList);
		System.out.println("size of the model detail:"
				+ scenarioZonesList.size());
	}
	
	public List saveDomainObject(Object domainObject) {
		List errorList = new ArrayList();
		DlvServiceTimeScenario modelIn = (DlvServiceTimeScenario)domainObject;
		
		if (modelIn.getCode() != null) {
			getLocationManagerService().removeEntity(
					getLocationManagerService().getDlvScenarioZones(modelIn.getCode()));
		}		
		
		Set tmpScenarioZones = modelIn.getScenarioZones();
		modelIn.setScenarioZones(null);
		getLocationManagerService().saveEntity(modelIn);
		
		modelIn.setScenarioZones(tmpScenarioZones);	
		if (tmpScenarioZones != null) {
			Iterator iterator = tmpScenarioZones.iterator();
			while (iterator.hasNext()) {
				DlvScenarioZones zones = (DlvScenarioZones) iterator.next();
				zones.getScenarioZonesId().setScenarioId(modelIn.getCode());
			}
		}
		
		modelIn.setScenarioZones(tmpScenarioZones);		
		
		Collection saveDataList = new ArrayList();
		if(!"X".equalsIgnoreCase(modelIn.getNeedsLoadBalance())) {
			modelIn.setBalanceBy(null);
			modelIn.setLoadBalanceFactor(null);
		}
		if("X".equals(modelIn.getIsDefault())) {
			DlvServiceTimeScenario modelDefault = getLocationManagerService().getDefaultServiceTimeScenario();
			if(modelDefault != null && !modelDefault.getCode().equals(modelIn.getCode())) {
				modelDefault.setIsDefault(null);
				saveDataList.add(modelDefault);
			} 
		}
		
		if("true".equals(modelIn.getIsNew())) {
			DlvServiceTimeScenario refDomain = getLocationManagerService().getServiceTimeScenario(modelIn.getCode());
			if(refDomain != null) {
				modelIn.setCode(null);
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			}
		}
		if(modelIn.getLateDeliveryFactor() == null) {
			modelIn.setLateDeliveryFactor(new BigDecimal(1));
		}
		if(errorList.size() == 0) {
			saveDataList.add(domainObject);
			getLocationManagerService().saveEntityList(saveDataList);
			getLocationManagerService().saveEntityList(modelIn.getScenarioZones());
			modelIn.setIsNew(null);
		}
		
		return errorList;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
}
