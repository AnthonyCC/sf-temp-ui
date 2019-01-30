/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBObject;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;

/**
 * Factory session bean for FD objects.
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please use the FDFactoryController and FDFactoryServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
@Deprecated
public interface FDFactorySB extends EJBObject {
    
	
	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects that were found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	@Deprecated public Collection<FDProductInfo> getProductInfos(String[] skus) throws RemoteException, FDResourceException;

	/**
	 * Get product with specified version.
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 */
	@Deprecated  public FDProduct getProduct(String sku, int version) throws RemoteException, FDSkuNotFoundException, FDResourceException;

	@Deprecated public Set<String> getModifiedSkus(long lastModified) throws FDResourceException, RemoteException;
}

