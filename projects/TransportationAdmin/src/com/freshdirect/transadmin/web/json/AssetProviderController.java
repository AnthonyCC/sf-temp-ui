package com.freshdirect.transadmin.web.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.AssetAttributeId;
import com.freshdirect.transadmin.model.AssetAttributeType;
import com.freshdirect.transadmin.model.AssetAttributeTypeId;
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
					boolean isKeyMatching = false; boolean isKeyValueMatching = false;
					String attributeType = null;
					String attributeValue = null;
					AssetTemplateAttributeId _attrId = _attribute.getId();
					if (_attrId != null) {

						if (assetAttributes != null
								&& assetAttributes.length > 0) {
							for (int intCount = 0; intCount < assetAttributes.length; intCount++) {
								
								if (assetAttributes[intCount][0].equals(_attrId
										.getAttributeType())) {
									isKeyMatching = true;
									attributeType = assetAttributes[intCount][0];
									attributeValue = assetAttributes[intCount][1];
									break;
								}
							}
						}
						AssetAttribute attribute = null;
						if (isKeyMatching){
							attribute = new AssetAttribute(
															new AssetAttributeId(assetId, attributeType)
																	, attributeValue);
							if (attributeValue.equals(_attribute.getAttributeValue()))
								attribute.setAttributeMatch("X");
							else
								attribute.setAttributeMatch("O");
							attributes.add(attribute);
						}else{
							attribute = new AssetAttribute(
															new AssetAttributeId(assetId, _attrId
																	.getAttributeType()), _attribute.getAttributeValue());
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
						assetAttribute.setAttributeMatch("U");
						attributes.add(assetAttribute);
					}
				}
			}
			asset.setAssetAttributes(attributes);
		} else {
			if (assetAttributes != null && assetAttributes.length > 0) {

				for (int intCount = 0; intCount < assetAttributes.length; intCount++) {
					AssetAttribute assetAttribute = new AssetAttribute(new AssetAttributeId(
							assetId, assetAttributes[intCount][0]),
							assetAttributes[intCount][1]);
					assetAttribute.setAttributeMatch("U");
					attributes.add(assetAttribute);
				}
				asset.setAssetAttributes(attributes);
			} else {
				Collection attributeTypes = getAssetManagerService().getAssetAttributeTypes(null, asset.getAssetType().getCode());
				if(attributeTypes != null){
					Iterator itr = attributeTypes.iterator();
					while(itr.hasNext()){
						AssetAttributeType _atrType = (AssetAttributeType)itr.next();
						AssetAttribute assetAttribute = new AssetAttribute(new AssetAttributeId(
								assetId, _atrType.getId().getCode()),null);
						assetAttribute.setAttributeValue("UNKNOWN");
						assetAttribute.setAttributeMatch("U");
						attributes.add(assetAttribute);
					}
				}
				asset.setAssetAttributes(attributes);
			}
		}
				
		this.getAssetManagerService().saveAsset(asset);
		return asset.getAssetId();
	}
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	public boolean addAssetAttributeType(String assetType, String attributeCode, String attributeDesc, String attribueDataType){
		
		try{			
			AssetAttributeType attributeType = new AssetAttributeType();
			AssetAttributeTypeId id = new AssetAttributeTypeId();		
			id.setCode(attributeCode);
			id.setAssetType(assetType);
			attributeType.setDescription(attributeDesc);
			attributeType.setDataType(attribueDataType);
			attributeType.setId(id);
			
			Collection assetAttributeTypes = getAssetManagerService().getAssetAttributeTypes(attributeCode, assetType);
			
			if (assetAttributeTypes != null && assetAttributeTypes.size() > 0)
				getAssetManagerService().removeEntity(assetAttributeTypes);		
			else
				getAssetManagerService().saveEntity(attributeType);
			
			// add new attribute to assets with UNKNOWN value
			Collection assets = getAssetManagerService().getAssets(assetType, null, null);
			if (assets != null) {
				Iterator<Asset> itr = assets.iterator();
				while (itr.hasNext()) {
					Asset _asset = itr.next();
					boolean foundAtr = false;
					if(_asset.getAssetAttributes() != null ){
						Iterator<AssetAttribute> _atrItr = _asset.getAssetAttributes().iterator();
						while (_atrItr.hasNext()) {
							AssetAttribute _attribute = _atrItr.next();
							if (_attribute
									.getId()
									.getAttributeType()
									.equalsIgnoreCase(
											attributeType.getId().getCode())) {
								foundAtr = true;
								break;
							}
						}
					}
					if (!foundAtr) {
						AssetAttribute assetAttribute = new AssetAttribute(
								new AssetAttributeId(_asset.getAssetId(),
										attributeType.getId().getCode()), null);
						assetAttribute.setAttributeValue("UNKNOWN");
						assetAttribute.setAttributeMatch("U");
						_asset.getAssetAttributes().add(assetAttribute);
					}
				}
				this.getAssetManagerService().saveEntityList(assets);
			}
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAttributeType(String assetType){
		Collection attributeTypes = getAssetManagerService().getAssetAttributeTypes(null, assetType);
		List result = new ArrayList(attributeTypes);
		Collections.sort(result, new AtttributeTypeComparator());
		return result;
	}
	
	public class AtttributeTypeComparator implements Comparator<AssetAttributeType> {
		@Override
		public int compare(AssetAttributeType a1, AssetAttributeType a2) {			
			return a2.getId().getCode().compareTo(a1.getId().getCode());
		}
	}
	
	public int addAssetType(String name, String desc){
		try{
			getAssetManagerService().saveEntity(new AssetType(name, desc));
		} catch (DataIntegrityViolationException ex){
			ex.printStackTrace();
			return 1;
		} catch (Exception e){
			e.printStackTrace();
			return 2;
		}
		return 0;
	}
}