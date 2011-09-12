package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.dao.AssetManagerDaoI;
import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetType;
import com.freshdirect.transadmin.service.AssetManagerI;

public class AssetManagerImpl  
		extends BaseManagerImpl  implements AssetManagerI {
	
	private final static Category LOGGER = LoggerFactory.getInstance(AssetManagerImpl.class);
	
	private AssetManagerDaoI assetManagerDao = null;
	
	public AssetManagerDaoI getAssetManagerDao() {
		return assetManagerDao;
	}

	public void setAssetManagerDao(AssetManagerDaoI assetManagerDao) {
		this.assetManagerDao = assetManagerDao;
	}

	protected BaseManagerDaoI getBaseManageDao() {
		return getAssetManagerDao();
	}

	public Collection getAssetTypes() {

		return getAssetManagerDao().getAssetTypes();
	}

	public Collection getAssetAttributeTypes() {

		return getAssetManagerDao().getAssetAttributeTypes();
	}
	
	public Collection getAssets(String assetType) {

		return getAssetManagerDao().getAssets(assetType);
	}
	
	public Collection getActiveAssets(String assetType) {

		return getAssetManagerDao().getActiveAssets(assetType);
	}
	
	public Asset getAsset(String assetId) {

		return getAssetManagerDao().getAsset(assetId);
	}
	
	public void saveAsset(Asset asset)  {
		getAssetManagerDao().saveAsset(asset);
	}

	public AssetTemplate getAssetTemplate(String assetTemplateId){
		return getAssetManagerDao().getAssetTemplate(assetTemplateId);
	}

	public void saveAssetTemplate(AssetTemplate assetTemplate) {
		getAssetManagerDao().saveAssetTemplate(assetTemplate);
	}

	public Collection getAssetTemplates(String assetType){
		return getAssetManagerDao().getAssetTemplates(assetType);
	}

	public AssetType getAssetType(String assetType){
		return getAssetManagerDao().getAssetType(assetType);
	}

	public Collection getAssetAttributeTypes(String attributeCode, String assetType){
		return getAssetManagerDao().getAssetAttributeTypes(attributeCode,assetType);
	}	
	
	public Asset getAssetByAssetNumber(String assetNo){
		return getAssetManagerDao().getAssetByAssetNumber(assetNo);
	}	

}
