package com.freshdirect.transadmin.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;



import com.freshdirect.transadmin.model.DlvServiceTimeType;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;

public class ZoneFormController extends AbstractDomainFormController {

	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("zonetypes", getDomainManagerService().getZoneTypes());
		refData.put("areas", getDomainManagerService().getAreas());
		refData.put("regions", getDomainManagerService().getRegions());
		refData.put("servicetimetypes", getLocationManagerService().getServiceTimeTypes());		
		refData.put("supervisors", DispatchPlanUtil.getSortedResources(getEmployeeManagerService().getSupervisors()));		
		return refData;
	}

	public Object getBackingObject(String id) {
		return getDomainManagerService().getZone(id);
	}

	public Object getDefaultBackingObject() {
		return new Zone();
	}

	public boolean isNew(Object command) {
		Zone modelIn = (Zone)command;
		return (modelIn.getZoneCode() == null);
	}

	public String getDomainObjectName() {
		return "Zone";
	}



	protected void onBind(HttpServletRequest request, Object command) {

		Zone model = (Zone) command;
		String areaCode=request.getParameter("area");
		String trnZoneType=request.getParameter("trnZoneType");
		String regionCode=request.getParameter("region");
		String defaultServiceTimeType=request.getParameter("serviceTimeType");
		
		String unattended=request.getParameter("unattended");

		TrnArea area= getDomainManagerService().getArea(areaCode);
		TrnZoneType zoneType= getDomainManagerService().getZoneType(trnZoneType);
		Region region= getDomainManagerService().getRegion(regionCode);
		DlvServiceTimeType serviceTimeType= getLocationManagerService().getServiceTimeType(defaultServiceTimeType);

		model.setArea(area);
		model.setTrnZoneType(zoneType);
		model.setRegion(region);
		model.setDefaultServiceTimeType(serviceTimeType);
		model.setUnattended(unattended);
	}

	protected void preProcessDomainObject(Object domainObject) {

		Zone modelIn = (Zone)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getZoneCode()) ) {
			modelIn.setZoneCode(modelIn.getZoneCode());
		}
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(TrnArea.class, new TrnAreaPropertyEditor());
		binder.registerCustomEditor(TrnZoneType.class, new TrnZoneTypePropertyEditor());
		binder.registerCustomEditor(Region.class, new RegionPropertyEditor());
    }


}
