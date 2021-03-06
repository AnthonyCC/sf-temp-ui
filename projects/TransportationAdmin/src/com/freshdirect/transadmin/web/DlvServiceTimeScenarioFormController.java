package com.freshdirect.transadmin.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DlvServiceTimeScenarioFormController extends AbstractFormController {
		
	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());	
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
		refData.put("zonetypes", getDomainManagerService().getZoneTypes());
		refData.put("balancebys", getDomainManagerService().getBalanceBys());
		refData.put("zones", getDomainManagerService().getActiveZones());
		refData.put("serviceTimeOperators", (List)EnumArithmeticOperator.getEnumList());
		refData.put("DayOfWeeks", (List) EnumDayOfWeek.getEnumList());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return null;
	}
	
	public Object getBackingObject(String id, HttpServletRequest request) {
		DlvServiceTimeScenario scenario = locationManagerService.getServiceTimeScenario(id);
		
		String cloneRefId = request.getParameter("scenarioRefId");
		if(!TransStringUtil.isEmpty(cloneRefId)) {
			scenario.setCode(null);
			scenario.setDescription(null);
		}
		return scenario;
	}
	
	public Object getDefaultBackingObject() {
		DlvServiceTimeScenario scenario = new DlvServiceTimeScenario();
		scenario.setIsNew("true");
		return scenario;
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
		
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		DlvServiceTimeScenario modelNew = (DlvServiceTimeScenario)domainObject;
		
		Collection saveDataList = new ArrayList();
		if(!"X".equalsIgnoreCase(modelNew.getNeedsLoadBalance())) {
			modelNew.setBalanceBy(null);
			modelNew.setLoadBalanceFactor(null);
		}
		/*if("X".equals(modelNew.getIsDefault())) {
			DlvServiceTimeScenario modelDefault = getLocationManagerService().getDefaultServiceTimeScenario();
			if(modelDefault != null && !modelDefault.getCode().equals(modelNew.getCode())) {
				modelDefault.setIsDefault(null);
				saveDataList.add(modelDefault);
			} 
		}*/
		
		if("true".equals(modelNew.getIsNew())) {
			DlvServiceTimeScenario refDomain = getLocationManagerService().getServiceTimeScenario(modelNew.getCode());
			if(refDomain != null) {
				modelNew.setCode(null);
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			}
		}
		if(modelNew.getLateDeliveryFactor() == null) {
			modelNew.setLateDeliveryFactor(new BigDecimal(1));
		}
		if(errorList.size() == 0) {
			saveDataList.add(domainObject);
			getLocationManagerService().saveEntityList(saveDataList);
			modelNew.setIsNew(null);
		}
		
		return errorList;
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if(TransStringUtil.isEmpty(id)) {
			id = request.getParameter("scenarioRefId");
		}
		return id;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
}
