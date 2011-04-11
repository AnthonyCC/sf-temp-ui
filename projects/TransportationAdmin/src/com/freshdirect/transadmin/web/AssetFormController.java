package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetType;
import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class AssetFormController extends AbstractFormController {
	
	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map<String, Collection> refData = new HashMap<String, Collection>();
		
		refData.put("assetstatuses", EnumAssetStatus.getEnumList());
		refData.put("assetAttributeTypes"
				, getAssetManagerService().getAssetAttributeTypes(null, new AssetType(request.getParameter("pAssetType")==null ? request.getParameter("assetType"): request.getParameter("pAssetType"), null)));
		refData.put("assetTemplates"
				, getAssetManagerService().getAssetTemplates(request.getParameter("pAssetType")==null ? request.getParameter("assetType"): request.getParameter("pAssetType")));
		return refData;
	}
	
	public Object getBackingObject(String id) {
		return getAssetManagerService().getAsset(id);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
														throws Exception {
		String id = getIdFromRequest(request);

		if (StringUtils.hasText(id)) {
			Object  tmp = getBackingObject(id);
			return tmp;
		} else {
			Asset asset = new Asset();
			asset.setAssetType(new AssetType(request.getParameter("pAssetType"), null));
			return asset;
		}
	}
	
	public Object getDefaultBackingObject() {
		Asset asset = new Asset();		
		return asset;
	}
	
	public boolean isNew(Object command) {
		Asset modelIn = (Asset)command;
		return (modelIn.getAssetId() == null);
	}
	
	public String getDomainObjectName() {
		return "Asset";
	}
	
	protected void preProcessDomainObject(Object domainObject) {
		Asset modelIn = (Asset)domainObject;
		if(TransStringUtil.isEmpty(modelIn.getAssetId()) ) {
			modelIn.setAssetId(modelIn.getAssetId());
		}
	}
	
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		List errorList = new ArrayList();		
		return errorList;
	}
	
	protected String getIdFromRequest(HttpServletRequest request){
		String id = request.getParameter("id");
		if(TransStringUtil.isEmpty(id)) {
			id = request.getParameter("assetId");
		}
		return id;
	}

}
