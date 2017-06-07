package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryData;
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryEntryData;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;

public class ErpInventoryModelConvert {

	public static ErpInventoryModel convertDataToModel(
			ErpInventoryData erpInventoryData) {
		List<ErpInventoryEntryModel> erpInventoryEntry = createErpOInventoryEntryModel(erpInventoryData.getEntries());
		ErpInventoryModel model = new  ErpInventoryModel(erpInventoryData.getSapId(), erpInventoryData.getLastUpdated(), erpInventoryEntry);
		return model;
	}

	private static List<ErpInventoryEntryModel> createErpOInventoryEntryModel(
			List<ErpInventoryEntryData> entries) {
		List<ErpInventoryEntryModel> entryModels = new ArrayList<ErpInventoryEntryModel>();
		for (ErpInventoryEntryData erpInventoryEntryData : entries) {
			ErpInventoryEntryModel erpInventoryEntryModel = new ErpInventoryEntryModel(erpInventoryEntryData.getStartDate(),erpInventoryEntryData.getQuantity(),erpInventoryEntryData.getPlantId());
			entryModels.add(erpInventoryEntryModel);
		}
		return entryModels;
	}

	public static Map<String, ErpInventoryModel> convertDataMapToModelMap(
			Map<String, ErpInventoryData> data) {
		Map<String,ErpInventoryModel> modelMap = new HashMap<String, ErpInventoryModel>();
		for (Entry<String, ErpInventoryData> dataMap : data.entrySet()) {
			String key = dataMap.getKey();
			ErpInventoryModel model = convertDataToModel(dataMap.getValue());
			modelMap.put(key, model);
		}
		return modelMap;
	}

}
