package com.freshdirect.transadmin.service;

import java.util.Collection;

import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetType;

@SuppressWarnings("rawtypes")
public interface AssetManagerI extends BaseManagerI {	
	
	Collection getAssetTypes();

	Collection getAssetAttributeTypes();

	Collection getAssets(String assetType, String atrName, String atrValue);

	Collection getActiveAssets(String assetType);

	Asset getAsset(String assetId);

	void saveAsset(Asset asset);

	AssetTemplate getAssetTemplate(String assetTemplateId);

	void saveAssetTemplate(AssetTemplate assetTemplate);

	Collection getAssetTemplates(String assetType);

	AssetType getAssetType(String assetType);

	Collection getAssetAttributeTypes(String attributeCode, String assetType);

	Asset getAssetByAssetNumber(String assetNo);

	Collection getAsset(String assetNumber, String assetType);

}
