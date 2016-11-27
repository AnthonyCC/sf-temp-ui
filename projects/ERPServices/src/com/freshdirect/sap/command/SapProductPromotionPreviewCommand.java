package com.freshdirect.sap.command;

import java.util.List;
import java.util.Map;

import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiProductPromotionPreviewI;
import com.freshdirect.sap.ejb.SapException;

public class SapProductPromotionPreviewCommand extends SapCommandSupport {

	private String previewId;	
	private ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo;
	
	public void setPreviewId(String previewId) {
		this.previewId = previewId;
	}	
	public SapProductPromotionPreviewCommand(String previewId) {
		super();
		this.previewId = previewId;
	}

	

	public ErpProductPromotionPreviewInfo getErpProductPromotionPreviewInfo() {
		return erpProductPromotionPreviewInfo;
	}
	@Override
	public void execute() throws SapException {
		EnumProductPromotionType type = EnumProductPromotionType.PRESIDENTS_PICKS;
		if(null !=previewId){
			if(previewId.endsWith(""+EnumProductPromotionType.PRODUCTS_ASSORTMENTS.getCode())){
				type = EnumProductPromotionType.PRODUCTS_ASSORTMENTS;
			}
		}
		BapiProductPromotionPreviewI bapi = BapiFactory.getInstance().getBapiProductPromotionPreviewBuilder(type);
		bapi.addRequest(this.previewId);
		this.invoke(bapi);
		erpProductPromotionPreviewInfo = new ErpProductPromotionPreviewInfo();
		erpProductPromotionPreviewInfo.setErpProductInfoMap(bapi.getErpProductInfoMap());
		erpProductPromotionPreviewInfo.setProductPromotionInfoMap(bapi.getProductPromotionInfoMap());

	}

}
