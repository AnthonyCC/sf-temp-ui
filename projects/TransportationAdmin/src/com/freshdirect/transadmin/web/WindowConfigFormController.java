package com.freshdirect.transadmin.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.EnumLogicalOperator;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.DlvWindow;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.editor.TimeOfDayPropertyEditor;

public class WindowConfigFormController extends AbstractDomainFormController {
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();						
		return refData;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder dataBinder) throws Exception {

		super.initBinder(request, dataBinder);
		
	}
	
	
	protected Object formBackingObject(HttpServletRequest request)
														throws Exception {
		String id = getIdFromRequest(request);

		if (StringUtils.hasText(id)) {
			Object  tmp = getBackingObject(id);
			return tmp;
		} else {
			return new DlvWindow();
		}
	}
	
	public Object getBackingObject(String id) {
		if(id!=null && id.length()>0)
		{
			String[] ids = id.split("\\^");
			return getLocationManagerService().getDlvWindow(ids[0], ids[1]);
		}
		return null;
	}
	
	public Object getDefaultBackingObject() {
		return new DlvWindow();
	}
	
	public boolean isNew(Object command) {
		DlvWindow modelIn = (DlvWindow)command;
		return (modelIn.getIdx() == null);
	}
	
	public String getDomainObjectName() {
		return "Window";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		DlvWindow modelIn = (DlvWindow)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getId().getWindowType()) && TransStringUtil.isEmpty(modelIn.getId().getZone())) {
			modelIn.setId(modelIn.getId());
		}
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("idx");
	}

}
