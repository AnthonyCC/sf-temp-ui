package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AreaFormController extends AbstractFormController {
	
	private DomainManagerI domainManagerService;
			
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("deliverymodels", getDomainManagerService().getDeliveryModels());
		refData.put("balancebys", getDomainManagerService().getBalanceBys());
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getArea(id);
	}
	
	public Object getDefaultBackingObject() {
		TrnArea area = new TrnArea();
		area.setIsNew("true");
		return area;
	}
	
	public boolean isNew(Object command) {
		TrnArea modelIn = (TrnArea)command;
		return (modelIn.getCode() == null);
	}
	
	public String getDomainObjectName() {
		return "Area";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnArea modelIn = (TrnArea)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCode()) ) {
			modelIn.setCode(modelIn.getCode());
		}
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		TrnArea modelNew = (TrnArea)domainObject;
		if(!"X".equalsIgnoreCase(modelNew.getNeedsLoadBalance())) {
			modelNew.setBalanceBy(null);
			modelNew.setLoadBalanceFactor(null);
		}
		if("true".equals(modelNew.getIsNew())) {
			TrnArea refDomain = getDomainManagerService().getArea(modelNew.getCode());
			if(refDomain != null) {
				errorList.add(this.getMessage("app.actionmessage.119", new Object[]{getDomainObjectName()}));
			} 
		}
		if(errorList.size() == 0) {
			getDomainManagerService().saveEntity(modelNew);
			modelNew.setIsNew(null);
		}
		return errorList;
	}

}
