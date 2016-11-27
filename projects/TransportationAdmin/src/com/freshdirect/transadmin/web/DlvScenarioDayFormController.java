package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.model.DlvScenarioDay;
import com.freshdirect.transadmin.model.ZoneWorkTableForm;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.EnumDayOfWeek;

public class DlvScenarioDayFormController extends BaseFormController{

	private LocationManagerI locationManagerService;
	
	private DomainManagerI domainManagerService;
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	

	
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		Collection scenarios = getLocationManagerService().getServiceTimeScenarios();
		Iterator itr=scenarios.iterator();
		DlvServiceTimeScenario dlvservicetimescenario;
		List scenarioList=new ArrayList();
		while(itr.hasNext()){
			dlvservicetimescenario=(DlvServiceTimeScenario)itr.next();
			scenarioList.add(dlvservicetimescenario);
		}
		refData.put("scenarios", scenarioList);
		refData.put("DayOfWeeks", (List) EnumDayOfWeek.getEnumList());
		return refData;
	}
	
	protected Object formBackingObject(HttpServletRequest request)throws Exception {
			
		DlvScenarioDay command = new DlvScenarioDay();
		return command;
	}
	
	public String getDomainObjectName() {
		return "Scenario to Day";
	}

	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException  {
		
		
		DlvScenarioDay scenarioDay=(DlvScenarioDay)command;
		ModelAndView mav = new ModelAndView(getSuccessView(),errors.getModel());
		try{
			getDomainManagerService().saveEntity(scenarioDay);
			saveMessage(request, getMessage("app.actionmessage.101", new Object[]{this.getDomainObjectName()}));
		} catch (DataIntegrityViolationException objExp) {
			
				//saveErrorMessage(request,getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
			
		return mav;
		
	}

}
