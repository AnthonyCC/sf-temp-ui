package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.catalog.model.CatalogInfo;

public class CatalogInfoResult extends Message{
	//This will have products
	private CatalogInfo catalogInfo;

	
	
	public CatalogInfo getCatalogInfo() {
		return catalogInfo;
	}



	public void setCatalogInfo(CatalogInfo catalogInfo) {
		this.catalogInfo = catalogInfo;
	}
}
