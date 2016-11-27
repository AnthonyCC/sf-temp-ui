package com.freshdirect.fdstore;

import com.freshdirect.erp.model.ErpInventoryModel;

public interface FDInventoryCacheI {
	public ErpInventoryModel getInventory(String materialId);
}
