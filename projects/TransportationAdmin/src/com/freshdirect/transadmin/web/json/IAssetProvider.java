package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.web.model.AssetScanInfo;

public interface IAssetProvider {
	
	String saveAsset(String assetId, String assetType
			, String assetNo, String description
			, String status
			, String assetTemplate
			, String[][] assetAttributes
			, String barcode);
	
	String saveAssetTemplate(String assetTemplateId, String assetType
			, String assetTemplateName
			, String[][] assetTemplateAttributes);
	
	boolean addAssetAttributeType(String attributeCode
			, String attributeDesc
			, String assetTypeCode);
	
	Asset getAsset(String assetId);
		
	List getAttributeType(String assetType);
	
	int addAssetType(String name, String desc);
	
	AssetScanInfo getAssetInfo(String assetBarcode, String employeeId, String status);
	
	public boolean logScannedAssets(String[][] assets);
}
