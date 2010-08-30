package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.AssetManagerDaoI;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;

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

}
