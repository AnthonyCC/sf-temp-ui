package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;

public interface FDBrandProductsAdManagerSB extends EJBObject{
	
	
	public HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	
	public void submittedOrderdDetailsToHL(Date productsOrderFeedDate) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	public void submittedOrderdDetailsToHL(List<String> orders) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	public Date getLastSentFeedOrderTime() throws FDResourceException, RemoteException;
	public HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException, RemoteException;
	
	
	
	
}
