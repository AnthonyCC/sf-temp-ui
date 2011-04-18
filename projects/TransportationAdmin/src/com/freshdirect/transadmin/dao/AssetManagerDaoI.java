package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetType;


public interface AssetManagerDaoI extends BaseManagerDaoI {
	
	Collection getAssetTypes() throws DataAccessException;
	
	Collection getAssetAttributeTypes() throws DataAccessException;
	
	Collection getAssets(String assetType) throws DataAccessException;
	
	Collection getActiveAssets(String assetType) throws DataAccessException;
	
	Asset getAsset(String assetId) throws DataAccessException;
	
	void saveAsset(Asset asset) throws DataAccessException;
	
	AssetTemplate getAssetTemplate(String assetTemplateId);
	
	void saveAssetTemplate(AssetTemplate assetTemplate) throws DataAccessException;
	
	Collection getAssetTemplates(String assetType) throws DataAccessException;
	
	AssetType getAssetType(String assetType) throws DataAccessException;	
	
	Collection getAssetAttributeTypes(String attributeCode, AssetType assetType) throws DataAccessException;
	
	Asset getAssetByAssetNumber(String assetNumber) throws DataAccessException;
	
}
