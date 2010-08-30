package com.freshdirect.transadmin.web.json;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.model.Asset;
import com.freshdirect.transadmin.model.AssetAttribute;
import com.freshdirect.transadmin.model.AssetAttributeId;
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
								, String[][] assetAttributes) {
		Asset asset = new Asset();
		asset.setAssetId(assetId);
		asset.setAssetNo(assetNo);
		asset.setAssetDescription(description);
		asset.setAssetStatus(EnumAssetStatus.getEnum(status));
		asset.setAssetType(new AssetType(assetType, null));
		
		if(assetAttributes != null && assetAttributes.length > 0) {
			Set attributes = new HashSet();
			for(int intCount =0; intCount < assetAttributes.length; intCount++) {
				attributes.add(new AssetAttribute(new AssetAttributeId(assetId, assetAttributes[intCount][0])
													, assetAttributes[intCount][1]));
			}
			asset.setAssetAttributes(attributes);
		}
		this.getAssetManagerService().saveAsset(asset);
		return asset.getAssetId();
	}
}
