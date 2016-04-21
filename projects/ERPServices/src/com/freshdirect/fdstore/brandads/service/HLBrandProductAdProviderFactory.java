package com.freshdirect.fdstore.brandads.service;

import com.freshdirect.FDCouponProperties;

public class HLBrandProductAdProviderFactory implements BrandProductAdProviderFactory {
	
	private static HLBrandProductAdProviderFactory theOnlyFactory;
	
	public BrandProductAdService newService() {
		HLBrandProductAdServiceProvider provider = new HLBrandProductAdServiceProvider();
		return provider;
	}
		
	private HLBrandProductAdProviderFactory() {}

    public static BrandProductAdProviderFactory getInstance() {
    	
      if (theOnlyFactory == null) {
    	  theOnlyFactory = new HLBrandProductAdProviderFactory();
      }
      return theOnlyFactory;
    }
}
