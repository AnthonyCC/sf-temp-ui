package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDBrandProductsAdManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDBrandProductsAdManager.class);

	private static FDBrandProductsAdManagerHome managerHome = null;
	
	public static HLBrandProductAdResponse getHLBrandproducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException{
		lookupManagerHome();
		
		try {
			FDBrandProductsAdManagerSB sb = managerHome.create();
		/*	hLRequestData.setUserId("1234");
			hLRequestData.setSearchKeyWord("storag");*/
			
			
			return sb.getSearchbykeyword(hLRequestData);
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
		
	}
	
	public static HLBrandProductAdResponse getHLCategoryProducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException{
		lookupManagerHome();
		
		try {
			FDBrandProductsAdManagerSB sb = managerHome.create();
			return sb.getCategoryProducts(hLRequestData);
			
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
		
	}
	
	private static void invalidateManagerHome() {
		managerHome = null;
	}
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDBrandProductsAdManagerHome) ctx.lookup(FDStoreProperties.getFDBrandProductsAdManagerHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	

	public Date getLastSentFeedOrderTime() throws FDResourceException {
		lookupManagerHome();
		
		try {
			FDBrandProductsAdManagerSB sb = managerHome.create();			
			return sb.getLastSentFeedOrderTime();
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
		
	}

}
