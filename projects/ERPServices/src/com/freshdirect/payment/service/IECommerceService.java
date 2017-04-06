package com.freshdirect.payment.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.fdstore.FDResourceException;

public interface IECommerceService {

	public Map<String,List<String>> updateCacheWithProdFly(List<String> familyIds)throws FDResourceException;
	
	public void loadData(List<ErpProductFamilyModel> productFamilyList) throws FDResourceException;
}
