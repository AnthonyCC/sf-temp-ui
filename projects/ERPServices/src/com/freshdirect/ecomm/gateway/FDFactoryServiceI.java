package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Set;

import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;

public interface FDFactoryServiceI {

	public FDProductInfo getProductInfo(String sku) throws FDSkuNotFoundException, RemoteException;

	public FDProductInfo getProductInfo(String skuCode, int version) throws RemoteException;

	public Collection getProductInfos(String[] skus) throws FDResourceException, RemoteException;

	public FDProduct getProduct(String sku, int version) throws RemoteException;

	public Collection<String> findSkusBySapId(String sapId) throws RemoteException;

	public Set<String> getModifiedSkus(long lastModifiedTime) throws RemoteException;

	


}
