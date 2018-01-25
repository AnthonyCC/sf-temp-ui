package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.ecommerce.data.erp.material.ErpMaterialSalesAreaData;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialPriceData;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialSalesAreaInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpPlantMaterialInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoData;
import com.freshdirect.ecommerce.data.utill.DayOfWeekSetData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.util.DayOfWeekSet;

public class ErpMaterialSalesAreaModelConverter {

	public static ErpMaterialSalesAreaModel convertDataToModel(
			ErpMaterialSalesAreaData data) {
		ErpMaterialSalesAreaModel model = new ErpMaterialSalesAreaModel(		
				data.getSalesOrg(),
				data.getDistChannel(),
				data.getUnavailabilityStatus(),
				data.getUnavailabilityDate(),
				data.getUnavailabilityReason(),
				data.getSkuCode(),
				data.getDayPartSelling(),
				data.getPickingPlantId()
				);
		return model;
	}

	public static ErpMaterialSalesAreaData convertModelToData(
			ErpMaterialSalesAreaModel model) {
		ErpMaterialSalesAreaData data = new ErpMaterialSalesAreaData();		
				data.setSalesOrg(model.getSalesOrg());
				data.setDistChannel(model.getDistChannel());
				data.setUnavailabilityStatus(model.getUnavailabilityStatus());
				data.setUnavailabilityDate(model.getUnavailabilityDate());
				data.setUnavailabilityReason(model.getUnavailabilityReason());
				data.setSkuCode(model.getSkuCode());
				data.setDayPartSelling(model.getDayPartSelling());
				data.setPickingPlantId(model.getPickingPlantId());
				
		return data;
	}
	
	public static Collection<ErpMaterialSalesAreaModel> convertListDataToListModel(
			Collection<ErpMaterialSalesAreaData> datas) {
		Collection<ErpMaterialSalesAreaModel> erpMaterialSalesAreaModel = new ArrayList<ErpMaterialSalesAreaModel>();
		for (ErpMaterialSalesAreaData erpMaterialSalesAreaData : datas) {
			erpMaterialSalesAreaModel.add(convertDataToModel(erpMaterialSalesAreaData));
		}
		return erpMaterialSalesAreaModel;
	}

	

	
	
	
	
	
	
}
