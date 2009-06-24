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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ResourceId;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;
import com.freshdirect.transadmin.web.model.CopyPlanCommand;

public class ScribFormController extends AbstractDomainFormController {
			
	private DispatchManagerI dispatchManagerService;
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("zones", getDomainManagerService().getZones());		
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
		String zoneId=request.getParameter("zoneS");
		Zone zone= getDomainManagerService().getZone(zoneId);
		if(zone!=null)
		{
			model.setZone(zone);
			model.setRegion(zone.getRegion());
		}
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

	public List saveDomainObject(Object domainObject) 
	{
		List errorList = null;
		try {
			Scrib model = (Scrib) domainObject;
//			Zone zone=getDomainManagerService().getZone(model.getZoneS());
//			if(zone!=null)model.setRegion(zone.getRegion());
			getDispatchManagerService().saveEntity(domainObject);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList = new ArrayList();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("scribId");
	}
	
}