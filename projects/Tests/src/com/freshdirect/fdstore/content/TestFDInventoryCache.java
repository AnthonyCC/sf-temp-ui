package com.freshdirect.fdstore.content;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.FDInventoryCacheI;

public class TestFDInventoryCache implements FDInventoryCacheI {
	
	private Map cache;
	
	public TestFDInventoryCache (){
		this.cache = new HashMap();
	}

	public ErpInventoryModel getInventory(String materialId) {
		return (ErpInventoryModel) this.cache.get(materialId);
	}
	
	public void addInventory(String materialId, ErpInventoryModel inventory) {
		this.cache.put(materialId, inventory);
	}

}
