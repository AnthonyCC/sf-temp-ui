package com.freshdirect.transadmin.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.model.EquipmentType;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetActivity;
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
	
	Collection getAssetAttributeTypes(String assetType);

	Asset getAssetByAssetNumber(String assetNo);

	Collection getAsset(String assetNumber, String assetType);
	
	List<EquipmentType> getEquipmentTypes(String region);

	Map<String, List<EquipmentType>> loadEquipmentTypes();
	
	Asset getAssetByBarcode(String barcode);
	
	 Map<String, List<AssetActivity>> getScannedAssets(Date assetScanDate);
	
}
