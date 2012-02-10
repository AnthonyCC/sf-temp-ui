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
import com.freshdirect.transadmin.model.Neighbourhood;
import com.freshdirect.transadmin.model.NeighbourhoodZipcode;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.NeighbourhoodPropertyEditor;
import com.freshdirect.transadmin.web.editor.RegionPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnAreaPropertyEditor;
import com.freshdirect.transadmin.web.editor.TrnZoneTypePropertyEditor;

public class NeighbourhoodFormController extends AbstractDomainFormController {

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("neighbourhoods", getDomainManagerService().getNeighbourhood());
		return refData;
	}

	public Object getBackingObject(String id) {
		return getDomainManagerService().getNeighbourhoodZipCode(id);
	}

	public Object getDefaultBackingObject() {
		return new NeighbourhoodZipcode();
	}

	public boolean isNew(Object command) {
		NeighbourhoodZipcode modelIn = (NeighbourhoodZipcode)command;
		return (modelIn.getZipcode() == null);
	}

	public String getDomainObjectName() {
		return "Neighbourhood Zipcode";
	}
	protected void onBind(HttpServletRequest request, Object command) {

		NeighbourhoodZipcode model = (NeighbourhoodZipcode) command;
		
	}

	protected void preProcessDomainObject(Object domainObject) {

		NeighbourhoodZipcode modelIn = (NeighbourhoodZipcode)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getZipcode()) ) {
			modelIn.setZipcode(modelIn.getZipcode());
		}
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(Neighbourhood.class, new NeighbourhoodPropertyEditor());
    }


}
