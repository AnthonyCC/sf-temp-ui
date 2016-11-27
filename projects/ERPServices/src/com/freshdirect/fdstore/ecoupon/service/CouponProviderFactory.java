package com.freshdirect.fdstore.ecoupon.service;

import com.freshdirect.FDCouponProperties;

public class CouponProviderFactory implements CouponProvider {
	
	private static CouponProviderFactory theOnlyFactory;
	
	public CouponService newService() {
		return getYouTechProvider();
	}
	
	private CouponService getYouTechProvider() {
		YTCouponProvider provider = new YTCouponProvider(/*FDCouponProperties.getYTProviderUrl()
															, FDCouponProperties.getYTProviderVersion()
															, FDCouponProperties.getYTSiteId()															
															, FDCouponProperties.getYTRetailerId()
															, FDCouponProperties.getYTRetailerName()
															, FDCouponProperties.getYTSignature()*/);
		return provider;
	}
	
	private CouponProviderFactory() {}

    public static CouponProvider getCouponProvider() {
    	
      if (theOnlyFactory == null) {
    	  theOnlyFactory = new CouponProviderFactory();
      }
      return theOnlyFactory;
    }
}
