package com.freshdirect.erp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;

public class ErpProductPromotionPreviewInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5844013208675369661L;
	private Map<String,ErpProductInfoModel> erpProductInfoMap;
	private Map<ZoneInfo,List<FDProductPromotionInfo>> productPromotionInfoMap;
	public Map<String, ErpProductInfoModel> getErpProductInfoMap() {
		return erpProductInfoMap;
	}
	public void setErpProductInfoMap(
			Map<String, ErpProductInfoModel> erpProductInfoMap) {
		this.erpProductInfoMap = erpProductInfoMap;
	}
	public Map<ZoneInfo, List<FDProductPromotionInfo>> getProductPromotionInfoMap() {
		return productPromotionInfoMap;
	}
	public void setProductPromotionInfoMap(
			Map<ZoneInfo, List<FDProductPromotionInfo>> productPromotionInfoMap) {
		this.productPromotionInfoMap = productPromotionInfoMap;
	}
	
	
	
	

}
