package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.Asset;


public interface AssetManagerI extends BaseManagerI {
	
	Collection getAssetTypes();
	Collection getAssetAttributeTypes();
	Collection getAssets(String assetType);
	Asset getAsset(String assetId);
	void saveAsset(Asset asset);
}
