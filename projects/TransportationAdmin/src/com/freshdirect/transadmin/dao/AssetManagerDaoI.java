package com.freshdirect.transadmin.dao;

import java.util.Collection;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.model.Asset;


public interface AssetManagerDaoI extends BaseManagerDaoI {
	
	Collection getAssetTypes() throws DataAccessException;
	Collection getAssetAttributeTypes() throws DataAccessException;
	Collection getAssets(String assetType) throws DataAccessException;
	Collection getActiveAssets(String assetType) throws DataAccessException;
	Asset getAsset(String assetId) throws DataAccessException;
	
	void saveAsset(Asset asset) throws DataAccessException;
}
