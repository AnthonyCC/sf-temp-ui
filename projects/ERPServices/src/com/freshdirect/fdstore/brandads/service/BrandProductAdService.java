package com.freshdirect.fdstore.brandads.service;

import com.freshdirect.fdstore.brandads.HLOrderFeedDataModel;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;


public interface BrandProductAdService {
	
	void close();
	HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException;
	void submittedOrderdDetailsToHL(HLOrderFeedDataModel hLOrderFeedDataModel) throws BrandProductAdServiceException;
	HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException;
	
}
