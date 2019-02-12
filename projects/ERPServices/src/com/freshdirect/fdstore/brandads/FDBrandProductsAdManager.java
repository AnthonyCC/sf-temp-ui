package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDBrandProductsAdManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDBrandProductsAdManager.class);

	public static HLBrandProductAdResponse getHLBrandproducts(HLBrandProductAdRequest hLRequestData)
			throws FDResourceException, BrandProductAdServiceException {

		try {
			HLBrandProductAdResponse data = null;

			data = FDECommerceService.getInstance().getSearchbykeyword(hLRequestData);

			return data;
		} catch (RemoteException e) {

			throw new FDResourceException(e, "Error creating session bean");

		}

	}

	public static HLBrandProductAdResponse getHLCategoryProducts(HLBrandProductAdRequest hLRequestData)
			throws FDResourceException, BrandProductAdServiceException {

		try {
			HLBrandProductAdResponse result = null;

			result = FDECommerceService.getInstance().getCategoryProducts(hLRequestData);
			return result;
		} catch (RemoteException e) {

			throw new FDResourceException(e, "Error creating session bean");

		}

	}

	public Date getLastSentFeedOrderTime() throws FDResourceException {

		try {
			Date date = null;

			date = FDECommerceService.getInstance().getLastSentFeedOrderTime();
			return date;
		} catch (RemoteException e) {

			throw new FDResourceException(e, "Error creating session bean");

		}

	}

	public static HLBrandProductAdResponse getHLadproductToHomeByFDPriority(
			HLBrandProductAdRequest hLBrandProductAdRequest) throws FDResourceException {

		try {
			HLBrandProductAdResponse result = null;

			result = FDECommerceService.getInstance().getHLadproductToHomeByFDPriority(hLBrandProductAdRequest);
			return result;
		} catch (RemoteException e) {

			throw new FDResourceException(e, "Error creating session bean");

		}
	}

	public static HLBrandProductAdResponse getHLadproductToPdp(HLBrandProductAdRequest hLBrandProductAdRequest)
			throws FDResourceException {

		try {
			HLBrandProductAdResponse result = null;

			result = FDECommerceService.getInstance().getPdpAdProduct(hLBrandProductAdRequest);

			return result;
		} catch (RemoteException e) {

			throw new FDResourceException(e, "Error creating session bean");

		}
	}

}
