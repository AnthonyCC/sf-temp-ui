package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.DispatchGroup;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

@SuppressWarnings("rawtypes")
public class DispatchGroupFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {						
		return  new HashMap();
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);
		
		dataBinder.registerCustomEditor(TimeOfDay.class,	new TimeOfDayPropertyEditor());
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getDispatchGroup(id);
	}
	
	public Object getDefaultBackingObject() {
		return new DispatchGroup();
	}
	
	public boolean isNew(Object command) {
		DispatchGroup modelIn = (DispatchGroup)command;
		return (modelIn.getDispatchGroupId() == null);
	}
	
	public String getDomainObjectName() {
		return "Dispatch Group";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DispatchGroup modelIn = (DispatchGroup) domainObject;
		if (TransStringUtil.isEmpty(modelIn.getDispatchGroupId())) {
			modelIn.setDispatchGroupId(modelIn.getDispatchGroupId());
		}
	}

}
