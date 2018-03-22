package com.freshdirect.fdstore.brandads;

import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.service.BrandProductAdService;
import com.freshdirect.fdstore.brandads.service.HLBrandProductAdProviderFactory;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceCreationException;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDBrandProductsAdGateway {
	
	private final static Logger LOG = LoggerFactory.getInstance(FDBrandProductsAdGateway.class);
	
	
	private final static Object sync = new Object();

	private static BrandProductAdService theOnlyService;

	public static BrandProductAdService getService() throws BrandProductAdServiceCreationException{
		synchronized (sync) {
			if (theOnlyService == null) {
				theOnlyService = HLBrandProductAdProviderFactory.getInstance().newService();
			}
			return theOnlyService;
		}
	}


	public static void resetService() {
		synchronized (sync) {
			try {
				getService().close();
				theOnlyService = null;
				LOG.info("HookLogicService was reset");
			} catch (BrandProductAdServiceCreationException e) {
			}
		}
	}
	
	public static HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException{
		
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		
		try {
			hlBrandProductAdResponse =getService().getSearchbykeyword(hLRequestData);
		} catch (BrandProductAdServiceException e) {
			LOG.error("Exception while getting HLBrandProductAd metadata:"+e.getMessage());
		}
		
		return hlBrandProductAdResponse;		
	}
	
public static HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException{
		
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		
		try {
			hlBrandProductAdResponse =getService().getCategoryProducts(hLRequestData);
		} catch (BrandProductAdServiceException e) {
			LOG.error("Exception while getting HLBrandProductAd metadata:"+e.getMessage());
		}
		
		return hlBrandProductAdResponse;		
	}

	public static void submittedOrderdDetailsToHL(HLOrderFeedDataModel hLOrderFeedDataModel) throws BrandProductAdServiceException{
		
		try {
			getService().submittedOrderdDetailsToHL(hLOrderFeedDataModel);
		} catch (BrandProductAdServiceException e) {
			LOG.error("Exception while sending the order details to HL for order: "+hLOrderFeedDataModel.getOrderId(),e);
			throw e;
		}
		
	}
	
	public static HLBrandProductAdResponse getHLadproductToHomeByFDPriority(HLBrandProductAdRequest hLBrandProductAdRequest) {
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		
		try {
			hlBrandProductAdResponse =getService().getHLadproductToHomeByFDPriority(hLBrandProductAdRequest);
		} catch (BrandProductAdServiceException e) {
			LOG.error("Exception while getting getHLadproductToHomeByFDPriority metadata:"+e.getMessage());
		}
		
		return hlBrandProductAdResponse;		
	}
	
	public static HLBrandProductAdResponse getPdpAdProduct(HLBrandProductAdRequest hLBrandProductAdRequest) {
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		
		try {
			hlBrandProductAdResponse =getService().getPdpAdProduct(hLBrandProductAdRequest);
		} catch (BrandProductAdServiceException e) {
			LOG.error("Exception while getting HLBrandProductAd metadata:"+e.getMessage());
		}
		
		return hlBrandProductAdResponse;		
	}
}
