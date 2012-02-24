package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;
import com.freshdirect.transadmin.model.Sector;
import com.freshdirect.transadmin.model.SectorZipcode;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.SectorPropertyEditor;

public class SectorFormController extends AbstractFormController {

	private DomainManagerI domainManagerService;
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws ServletException {

		Map refData = new HashMap();
		refData.put("sectors", domainManagerService.getSector());
		return refData;
	}

	public Object getBackingObject(String id) {
		return domainManagerService.getSectorZipCode(id);
	}

	public Object getDefaultBackingObject() {
		return new SectorZipcode();
	}

	public boolean isNew(Object command) {
		SectorZipcode modelIn = (SectorZipcode)command;
		return (modelIn.getZipcode() == null);
	}

	public String getDomainObjectName() {
		return "Zipcode";
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();
		SectorZipcode model = (SectorZipcode)domainObject;
		try {
			domainManagerService.saveEntity(model);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}
		return errorList;
	}

	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(Sector.class, new SectorPropertyEditor());
    }


}
