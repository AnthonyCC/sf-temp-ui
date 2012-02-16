package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import com.freshdirect.transadmin.model.Sector;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.SectorPropertyEditor;

public class SectorFormController extends AbstractDomainFormController {

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("sectors", getDomainManagerService().getSector());
		return refData;
	}

	public Object getBackingObject(String id) {
		return getDomainManagerService().getSectorZipCode(id);
	}

	public Object getDefaultBackingObject() {
		return new SectorZipcode();
	}

	public boolean isNew(Object command) {
		SectorZipcode modelIn = (SectorZipcode)command;
		return (modelIn.getZipcode() == null);
	}

	public String getDomainObjectName() {
		return "Sector Zipcode";
	}
	protected void onBind(HttpServletRequest request, Object command) {

		SectorZipcode model = (SectorZipcode) command;
		
	}

	protected void preProcessDomainObject(Object domainObject) {

		SectorZipcode modelIn = (SectorZipcode)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getZipcode()) ) {
			modelIn.setZipcode(modelIn.getZipcode());
		}
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(Sector.class, new SectorPropertyEditor());
    }


}
