package com.freshdirect.transadmin.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;


import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;

public class ZoneFormController extends AbstractDomainFormController {



	protected Map referenceData(HttpServletRequest request) throws ServletException {
//		System.out.println("referenceData");
		Map refData = new HashMap();
		//refData.put("supervisors", getDomainManagerService().getSupervisors());
		//refData.put("region", getDomainManagerService().getR);
		refData.put("zonetypes", getDomainManagerService().getZoneTypes());
		refData.put("areas", getDomainManagerService().getAreas());
		refData.put("regions", getDomainManagerService().getRegions());
		return refData;
	}

	public Object getBackingObject(String id) {
	//	System.out.println("getBackingObject");
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

	//	System.out.println("On Bind");
		Zone model = (Zone) command;
		String areaCode=request.getParameter("area");
		String trnZoneType=request.getParameter("trnZoneType");
		String regionCode=request.getParameter("region");

		String unattended=request.getParameter("unattended");

		//System.out.println(" unattended :"+unattended);

		TrnArea area= getDomainManagerService().getArea(areaCode);
		TrnZoneType zoneType= getDomainManagerService().getZoneType(trnZoneType);
		Region region= getDomainManagerService().getRegion(regionCode);

		//System.out.println("area"+area);
		//System.out.println("zoneType"+zoneType);
		//System.out.println("region"+region);

		model.setArea(area);
		model.setTrnZoneType(zoneType);
		model.setRegion(region);
		model.setUnattended(unattended);
	}

	protected void preProcessDomainObject(Object domainObject) {
		//System.out.println("preProcessDomainObject");
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
