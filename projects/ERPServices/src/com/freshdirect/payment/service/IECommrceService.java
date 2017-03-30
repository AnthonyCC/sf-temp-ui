package com.freshdirect.payment.service;

import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.fdstore.FDPayPalServiceException;

public interface IECommrceService {

	public Map<String,List<String>> updateCacheWithProdFly(List<String> familyIds)throws FDPayPalServiceException;
	
	public String loadData(List<ErpProductFamilyModel> productFamilyList) throws FDPayPalServiceException;
}
