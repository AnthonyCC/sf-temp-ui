package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
/**
 *@deprecated Please use the FDBrandProductsAdController and FDBrandProductsAdServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public interface FDBrandProductsAdManagerSB extends EJBObject{
	@Deprecated
	public HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	@Deprecated public void submittedOrderdDetailsToHL(Date productsOrderFeedDate) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	@Deprecated public void submittedOrderdDetailsToHL(List<String> orders) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	@Deprecated public Date getLastSentFeedOrderTime() throws FDResourceException, RemoteException;
	@Deprecated public HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	@Deprecated public HLBrandProductAdResponse getHomeAdProduct(HLBrandProductAdRequest hLBrandProductAdRequest) throws BrandProductAdServiceException, RemoteException;
	@Deprecated public HLBrandProductAdResponse getPdpAdProduct(HLBrandProductAdRequest hLBrandProductAdRequest) throws BrandProductAdServiceException, RemoteException;
}
