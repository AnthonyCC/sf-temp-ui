package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

public class CutOffFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();						
		return refData;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);
		
		dataBinder.registerCustomEditor(TimeOfDay.class,	new TimeOfDayPropertyEditor());
	}
	
	public Object getBackingObject(String id) {
		return getDomainManagerService().getCutOff(id);
	}
	
	public Object getDefaultBackingObject() {
		return new TrnCutOff();
	}
	
	public boolean isNew(Object command) {
		TrnCutOff modelIn = (TrnCutOff)command;
		return (modelIn.getCutOffId() == null);
	}
	
	public String getDomainObjectName() {
		return "Cut Off";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		TrnCutOff modelIn = (TrnCutOff)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getCutOffId()) ) {
			modelIn.setCutOffId(modelIn.getCutOffId());
		}
	}

}
