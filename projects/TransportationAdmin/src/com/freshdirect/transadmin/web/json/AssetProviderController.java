package com.freshdirect.transadmin.web.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.AssetAttributeId;
import com.freshdirect.transadmin.model.AssetAttributeType;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetTemplateAttribute;
import com.freshdirect.transadmin.model.AssetTemplateAttributeId;
import com.freshdirect.transadmin.model.AssetType;
import com.freshdirect.transadmin.service.AssetManagerI;

public class AssetProviderController extends BaseJsonRpcController  implements IAssetProvider {
		
	private AssetManagerI assetManagerService;
	
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}

	public String saveAsset(String assetId, String assetType
								, String assetNo, String description
								, String status
								, String assetTemplateId
								, String[][] assetAttributes) {
		Asset asset = new Asset();
		asset.setAssetId(assetId);
		asset.setAssetNo(assetNo);
		asset.setAssetDescription(description);
		asset.setAssetStatus(EnumAssetStatus.getEnum(status));
		asset.setAssetType(new AssetType(assetType, null));
		AssetTemplate assetTemplate = null;
		if (assetTemplateId != null)
			assetTemplate = getAssetManagerService().getAssetTemplate(
					assetTemplateId);
		asset.setAssetTemplate(assetTemplate);
		
		Set<AssetAttribute> attributes = new HashSet<AssetAttribute>();
		if (assetTemplate != null
				&& assetTemplate.getAssetTemplateAttributes() != null
				&& assetTemplate.getAssetTemplateAttributes().size() > 0) {

			Iterator<AssetTemplateAttribute> itr = assetTemplate.getAssetTemplateAttributes().iterator();
			
			while (itr.hasNext()) {
				AssetTemplateAttribute _attribute = itr.next();
				if (_attribute != null) {
					boolean isMatching = false;boolean isKeyValueMatching = false;
					String attributeType = null;
					String attributeValue = null;
					AssetTemplateAttributeId _attrId = _attribute.getId();
					if (_attrId != null) {

						if (assetAttributes != null
								&& assetAttributes.length > 0) {
							for (int intCount = 0; intCount < assetAttributes.length; intCount++) {
								if (assetAttributes[intCount][0].equals(_attrId
										.getAttributeType()) && assetAttributes[intCount][1].equals(_attribute.getAttributeValue())) {
									isKeyValueMatching = true;
								}
								if (assetAttributes[intCount][0].equals(_attrId
										.getAttributeType())) {
									isMatching = true;
									attributeType = assetAttributes[intCount][0];
									attributeValue = assetAttributes[intCount][1];
									break;
								}
							}
						}
						AssetAttribute attribute = null;
						if (isMatching){
							attribute = new AssetAttribute(
															new AssetAttributeId(assetId, attributeType)
																	, attributeValue);
							attribute.setAttributeMatch(isKeyValueMatching ? "1" : "0");
							attributes.add(attribute);
						}else{
							attribute = new AssetAttribute(
															new AssetAttributeId(assetId, _attrId
																	.getAttributeType()), _attribute.getAttributeValue());
							attribute.setAttributeMatch("1");
							attributes.add(attribute);
					
						}
					}
				}
			}

			if (assetAttributes != null && assetAttributes.length > 0) {
				for (int intCount = 0; intCount < assetAttributes.length; intCount++) {
					AssetAttribute assetAttribute = new AssetAttribute(
							new AssetAttributeId(assetId,
									assetAttributes[intCount][0]),
							assetAttributes[intCount][1]);
					if (!attributes.contains(assetAttribute)) {
						assetAttribute.setAttributeMatch("X");
						attributes.add(assetAttribute);
					}
				}
			}
			asset.setAssetAttributes(attributes);
		} else {
			if (assetAttributes != null && assetAttributes.length > 0) {

				for (int intCount = 0; intCount < assetAttributes.length; intCount++) {

					attributes.add(new AssetAttribute(new AssetAttributeId(
							assetId, assetAttributes[intCount][0]),
							assetAttributes[intCount][1]));
				}
				asset.setAssetAttributes(attributes);
			}
		}
				
		this.getAssetManagerService().saveAsset(asset);
		return asset.getAssetId();
	}
	
	public String saveAssetTemplate(String assetTemplateId, String assetType
			, String assetTemplateName, String[][] assetTemplateAttributes) {
		
		AssetTemplate assetTemplate = new AssetTemplate();
		assetTemplate.setAssetTemplateId(assetTemplateId);
		assetTemplate.setAssetTemplateName(assetTemplateName);
		assetTemplate.setAssetType(new AssetType(assetType, null));
		
		if(assetTemplateAttributes != null && assetTemplateAttributes.length > 0) {
			Set attributes = new HashSet();
			for(int intCount =0; intCount < assetTemplateAttributes.length; intCount++) {
				attributes.add(new AssetTemplateAttribute(new AssetTemplateAttributeId(assetTemplateId, assetTemplateAttributes[intCount][0])
										, assetTemplateAttributes[intCount][1]));
			}
			assetTemplate.setAssetTemplateAttributes(attributes);
		}
		this.getAssetManagerService().saveAssetTemplate(assetTemplate);
		return assetTemplate.getAssetTemplateId();
	}
	
	public boolean addAssetAttribute(String assetTypeCode, String attributeCode, String attributeDesc, String attribueDataType){
		
		try{
			AssetType assetType  = getAssetManagerService().getAssetType(assetTypeCode);
			AssetAttributeType attributeType = new AssetAttributeType();
			
			attributeType.setCode(attributeCode);
			attributeType.setDescription(attributeDesc);
			attributeType.setDataType(attribueDataType);
			attributeType.setAssetType(assetType);
			
			Collection assetAttributesTypes = getAssetManagerService().getAssetAttributeTypes(attributeCode, assetType);
			
			if(assetAttributesTypes !=null && assetAttributesTypes.size()>0)
				getAssetManagerService().removeEntity(assetAttributesTypes);
			else
				getAssetManagerService().saveEntity(attributeType);
			
			return true;			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Asset getAsset(String assetId){
		Asset asset = null;
		try{
			asset = getAssetManagerService().getAsset(assetId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return asset;
	}
}
