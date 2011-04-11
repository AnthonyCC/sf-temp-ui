package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.AssetManagerDaoI;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.AssetTemplate;
import com.freshdirect.transadmin.model.AssetTemplateAttribute;
import com.freshdirect.transadmin.model.AssetType;

public class AssetManagerDaoHibernateImpl
		extends BaseManagerDaoHibernateImpl  implements AssetManagerDaoI {

	public Collection getAssetTypes() throws DataAccessException {

		return getDataList("AssetType Order By CODE");
	}

	public Collection getAssetAttributeTypes() throws DataAccessException {

		return getDataList("AssetAttributeType Order By CODE");
	}
	
	public Collection getAssets(String assetType) throws DataAccessException {

		return getDataList("Asset where ASSET_TYPE = '"+assetType+"' Order By ASSET_NO");
	}
	
	public Collection getActiveAssets(String assetType) throws DataAccessException {

		return getDataList("Asset where ASSET_TYPE = '"+assetType+"' and ASSET_STATUS = 'ACT' Order By ASSET_NO");
	}
	
	public Asset getAsset(String assetId) throws DataAccessException {

		return (Asset)getEntityById("Asset", "assetId", assetId);
	}
	
	public void saveAsset(Asset asset) throws DataAccessException {

		if (asset.getAssetId() == null || "".equals(asset.getAssetId())) {
			Set attributes = asset.getAssetAttributes();
			asset.setAssetAttributes(null);
			getHibernateTemplate().save(asset);
			if (attributes != null && attributes.size() > 0) {
				Iterator it = attributes.iterator();
				while (it.hasNext()) {
					AssetAttribute dr = (AssetAttribute) it.next();
					dr.getId().setAssetId(asset.getAssetId());
				}
			}
			asset.setAssetAttributes(attributes);
			saveEntityList(asset.getAssetAttributes());

		} else {
			saveEntity(asset);
		}
	}
	
	public AssetTemplate getAssetTemplate(String assetTemplateId){
		return (AssetTemplate)getEntityById("AssetTemplate", "assetTemplateId", assetTemplateId);
	}
	
	public void saveAssetTemplate(AssetTemplate assetTemplate) throws DataAccessException {

		if (assetTemplate.getAssetTemplateId() == null || "".equals(assetTemplate.getAssetTemplateId())) {
			Set attributes = assetTemplate.getAssetTemplateAttributes();
			assetTemplate.setAssetTemplateAttributes(null);
			getHibernateTemplate().save(assetTemplate);
			if (attributes != null && attributes.size() > 0) {
				Iterator it = attributes.iterator();
				while (it.hasNext()) {
					AssetTemplateAttribute dr = (AssetTemplateAttribute) it.next();
					dr.getId().setAssetTemplateId(assetTemplate.getAssetTemplateId());
				}
			}
			assetTemplate.setAssetTemplateAttributes(attributes);
			saveEntityList(assetTemplate.getAssetTemplateAttributes());
		} else {
			saveEntity(assetTemplate);
		}
	}
	
	public Collection getAssetTemplates(String assetType) throws DataAccessException {

		return getDataList("AssetTemplate where ASSET_TYPE = '"+assetType+"' Order By assetTemplateName");
	}
	
	public AssetType getAssetType(String assetType) throws DataAccessException{
		return (AssetType)getEntityById("AssetType", "code", assetType);
	}
	
	public Collection getAssetAttributeTypes(String attributeCode, AssetType assetType) throws DataAccessException{
		
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("AssetAttributeType a where");
		if(attributeCode != null && !"".equals(attributeCode))
			strBuf.append(" a.code='"+attributeCode+"'");
		if(attributeCode != null && assetType != null)
			strBuf.append(" and");
		if(assetType != null)
			strBuf.append(" a.assetType='"+assetType+"'");		
		
		return (Collection) getDataList(strBuf.toString());		
	}
	
}
