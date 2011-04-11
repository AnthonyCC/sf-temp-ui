package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetType;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AssetTemplateFormController extends AbstractFormController {
	
	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		
		refData.put("assetAttributeTypes"
					, getAssetManagerService().getAssetAttributeTypes(null
							, new AssetType(request.getParameter("tAssetType")==null ? request.getParameter("assetType"): request.getParameter("tAssetType"), null)));
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getAssetManagerService().getAssetTemplate(id);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		String id = getIdFromRequest(request);

		if (StringUtils.hasText(id)) {
			Object  tmp = getBackingObject(id);
			return tmp;
		} else {
			AssetTemplate assetTemplate = new AssetTemplate();
			assetTemplate.setAssetType(new AssetType(request.getParameter("tAssetType"), null));
			return assetTemplate;
		}
	}
	
	public Object getDefaultBackingObject() {
		AssetTemplate assetTemplate = new AssetTemplate();		
		return assetTemplate;
	}
	
	public boolean isNew(Object command) {
		AssetTemplate modelIn = (AssetTemplate)command;
		return (modelIn.getAssetTemplateId() == null);
	}
	
	public String getDomainObjectName() {
		return "Asset Template";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		AssetTemplate modelIn = (AssetTemplate)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getAssetTemplateId()) ) {
			modelIn.setAssetTemplateId(modelIn.getAssetTemplateId());
		}
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();		
		return errorList;
	}

}
