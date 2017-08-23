package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDBrandProductsAdManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDBrandProductsAdManager.class);

	private static FDBrandProductsAdManagerHome managerHome = null;
	
	public static HLBrandProductAdResponse getHLBrandproducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException{
		lookupManagerHome();
		
		try {
			HLBrandProductAdResponse data = null;
			FDBrandProductsAdManagerSB sb = managerHome.create();
		/*	hLRequestData.setUserId("1234");
			hLRequestData.setSearchKeyWord("storag");*/
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
			data =FDECommerceService.getInstance().getSearchbykeyword(hLRequestData);
			
			}else {			
			data= sb.getSearchbykeyword(hLRequestData);
			}
			return data;
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
			HLBrandProductAdResponse result = null;
			FDBrandProductsAdManagerSB sb = managerHome.create();
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
				result = FDECommerceService.getInstance().getCategoryProducts(hLRequestData);
			}else{
				result= sb.getCategoryProducts(hLRequestData);
			}
			return result;
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
			Date date = null;
			FDBrandProductsAdManagerSB sb = managerHome.create();		
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
				date =  FDECommerceService.getInstance().getLastSentFeedOrderTime();
			}else{
			date= sb.getLastSentFeedOrderTime();
			}
			return date;
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
		
	}
	
	public static HLBrandProductAdResponse getHLadproductToHome(HLBrandProductAdRequest hLBrandProductAdRequest) throws FDResourceException {
		lookupManagerHome();
		
		try {
			HLBrandProductAdResponse result = null;
			FDBrandProductsAdManagerSB sb = managerHome.create();
			/*if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
				result = FDECommerceService.getInstance().getCategoryProducts(hLBrandProductAdRequest);
			}else{*/
				result= sb.getHomeAdProduct(hLBrandProductAdRequest);
//			}
			return result;
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}catch (BrandProductAdServiceException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error while connecting api");
		}
	}

	public static HLBrandProductAdResponse getHLadproductToPdp(HLBrandProductAdRequest hLBrandProductAdRequest) throws FDResourceException{
		lookupManagerHome();
		
		try {
			HLBrandProductAdResponse result = null;
			FDBrandProductsAdManagerSB sb = managerHome.create();
			/*if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
				result = FDECommerceService.getInstance().getCategoryProducts(hLBrandProductAdRequest);
			}else{*/
				try {
					result= sb.getPdpAdProduct(hLBrandProductAdRequest);
				} 
					catch (BrandProductAdServiceException e) {
						invalidateManagerHome();
						throw new FDResourceException(e, "Error while connecting api");
				}
//			}
			return result;
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e, "Error creating session bean");
		}
	}

}
