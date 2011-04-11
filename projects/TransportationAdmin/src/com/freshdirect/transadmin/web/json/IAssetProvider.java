package com.freshdirect.transadmin.web.json;

import com.freshdirect.transadmin.model.Asset;

public interface IAssetProvider {
	
	String saveAsset(String assetId, String assetType
			, String assetNo, String description
			, String status
			, String assetTemplate
			, String[][] assetAttributes);
	
	String saveAssetTemplate(String assetTemplateId, String assetType
			, String assetTemplateName
			, String[][] assetTemplateAttributes);
	
	boolean addAssetAttribute(String attributeCode
			, String attributeDesc
			, String attribueDataType
			, String assetTypeCode);
	
	Asset getAsset(String assetId);
}
