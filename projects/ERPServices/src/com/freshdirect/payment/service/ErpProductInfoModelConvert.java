package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.ecommerce.data.erp.model.ErpMaterialPriceData;
import com.freshdirect.ecommerce.data.erp.model.ErpMaterialSalesAreaInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpPlantMaterialInfoData;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.fdstore.SalesAreaInfoData;
import com.freshdirect.ecommerce.data.utill.DayOfWeekSetData;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialPrice;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpMaterialSalesAreaInfo;
import com.freshdirect.erp.model.ErpProductInfoModel.ErpPlantMaterialInfo;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.util.DayOfWeekSet;

public class ErpProductInfoModelConvert {

	public static ErpProductInfoModel convertModelToData(
			ErpProductInfoModelData data) {
		ErpProductInfoModel model = new ErpProductInfoModel(		
				data.getSkuCode(),
				data.getVersion(),
				data.getMaterialNumbers(),
				data.getDescription(),
				getErpMaterialPriceModel(data.getMaterialPrices()),
				data.getUpc(),
				getErpMaterialPlantModel(data.getMaterialPlants()),
				getErpMaterialSalesAreaInfoModel(data.getMaterialSalesAreas()),
				/*data.alcoholicType()*/ EnumAlcoholicContent.NONE);
		return model;
	}

	private static ErpMaterialSalesAreaInfo[] getErpMaterialSalesAreaInfoModel(
			ErpMaterialSalesAreaInfoData[] materialSalesAreas) {
		if(null == materialSalesAreas){
			return null;
		}
		ErpMaterialSalesAreaInfo[] data = new  ErpMaterialSalesAreaInfo[materialSalesAreas.length];
		for (int i = 0; i < materialSalesAreas.length; i++) {
			ErpMaterialSalesAreaInfoData erpMaterialSalesAreaInfoData = materialSalesAreas[i];
			data[i] = new ErpMaterialSalesAreaInfo(createSalesAreaInfo(erpMaterialSalesAreaInfoData.getSalesAreaInfo()), erpMaterialSalesAreaInfoData.getUnavailabilityStatus(),erpMaterialSalesAreaInfoData.getUnavailabilityDate(), erpMaterialSalesAreaInfoData.getUnavailabilityReason(), erpMaterialSalesAreaInfoData.getDayPartType(), erpMaterialSalesAreaInfoData.getPickingPlantId());
		}
		return data;
	}

	private static SalesAreaInfo createSalesAreaInfo(
			SalesAreaInfoData salesAreaInfoData) {
		SalesAreaInfo salesAreaInfo = new SalesAreaInfo(salesAreaInfoData.getSalesOrg(), salesAreaInfoData.getDistChannel());
		return salesAreaInfo;
	}

	private static ErpPlantMaterialInfo[] getErpMaterialPlantModel(
			ErpPlantMaterialInfoData[] materialPlants) {
		if(null == materialPlants){
			return null;
		}
		ErpPlantMaterialInfo[] data = new  ErpPlantMaterialInfo[materialPlants.length];
		for (int i = 0; i < materialPlants.length; i++) {
			ErpPlantMaterialInfoData erpPlantMaterialData = materialPlants[i];
			ErpPlantMaterialInfo erpPlantMaterialInfo = new ErpPlantMaterialInfo(erpPlantMaterialData.isKosherProduction(), erpPlantMaterialData.isPlatter(), getDayOfWeekSet(erpPlantMaterialData.getBlockedDays()), getAtpRule(erpPlantMaterialData.getAtpRule()), erpPlantMaterialData.getRating(), erpPlantMaterialData.getFreshness(), erpPlantMaterialData.getSustainabilityRating(), erpPlantMaterialData.getPlantId(), erpPlantMaterialData.isLimitedQuantity());
			data[i]= erpPlantMaterialInfo;
		}
		return data;
	}

	private static EnumATPRule getAtpRule(String atpRule) {
		if(atpRule!=null)
		return EnumATPRule.getEnum(atpRule);
		return null;
	}

	private static DayOfWeekSet getDayOfWeekSet(DayOfWeekSetData blockedDays) {
		DayOfWeekSet dayOfWeekSet = DayOfWeekSet.decode(null);
		if(blockedDays!=null){
			Iterator iterator = blockedDays.getDaysOfWeek().iterator();
			while(iterator.hasNext()){
				String dayNumber = (String) iterator.next();
				dayOfWeekSet = DayOfWeekSet.decode(dayNumber);
			}
		}
		return dayOfWeekSet;
	}
	
	private static ErpMaterialPrice[] getErpMaterialPriceModel(
			ErpMaterialPriceData[] materialPrices) {
		if(null == materialPrices){
			return null;
		}
		ErpMaterialPrice[] material = new ErpMaterialPrice[materialPrices.length]; 
		for (int i = 0; i < materialPrices.length; i++) {
			ErpMaterialPriceData erpMaterialPrice = materialPrices[i];
			material[i]= new ErpMaterialPrice(erpMaterialPrice.getPrice(), erpMaterialPrice.getPricingUnit(), erpMaterialPrice.getPromoPrice(), erpMaterialPrice.getScaleUnit(), erpMaterialPrice.getScaleQuantity(), erpMaterialPrice.getSapZoneId(), erpMaterialPrice.getSalesOrg(), erpMaterialPrice.getDistChannel());
		}
		return material;
	}


	public static Collection<ErpProductInfoModel> convertListDataToListModel(
			Collection<ErpProductInfoModelData> datas) {
		Collection<ErpProductInfoModel> erpProductInfoModels = new ArrayList<ErpProductInfoModel>();
		for (ErpProductInfoModelData erpProductInfoModelData : datas) {
			erpProductInfoModels.add(convertModelToData(erpProductInfoModelData));
		}
		return erpProductInfoModels;
	}

	

	
	
	
	
	
	
}
