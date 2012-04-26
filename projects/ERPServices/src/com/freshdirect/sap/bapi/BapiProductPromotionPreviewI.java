package com.freshdirect.sap.bapi;

import java.util.List;
import java.util.Map;

import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;

public interface BapiProductPromotionPreviewI extends BapiFunctionI {

	public void addRequest(String previewId);	
	public Map<String, List<FDProductPromotionInfo>> getProductPromotionInfoMap();
	public Map<String, ErpProductInfoModel> getErpProductInfoMap();
}
