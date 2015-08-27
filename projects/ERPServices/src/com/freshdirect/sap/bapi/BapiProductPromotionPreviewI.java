package com.freshdirect.sap.bapi;

import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;

public interface BapiProductPromotionPreviewI extends BapiFunctionI {

	public void addRequest(String previewId);	
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getProductPromotionInfoMap();
	public Map<String, ErpProductInfoModel> getErpProductInfoMap();
}
