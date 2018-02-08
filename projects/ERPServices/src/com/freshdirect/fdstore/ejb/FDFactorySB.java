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
public interface FDFactorySB extends EJBObject {
    
	/**
	 * Get current product information object for sku.
	 *
	 * @param sku SKU code
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	@Deprecated public FDProductInfo getProductInfo(String sku) throws RemoteException, FDSkuNotFoundException, FDResourceException;
    
    /**
	 * Get current product information object for a specific version of a sku.
	 *
	 * @param sku SKU code
     * @param version requested version
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	@Deprecated public FDProductInfo getProductInfo(String sku, int version) throws RemoteException, FDSkuNotFoundException, FDResourceException;

	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects that were found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	@Deprecated public Collection getProductInfos(String[] skus) throws RemoteException, FDResourceException;

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

	/** @return list of sku codes */
	@Deprecated   public Collection getNewSkuCodes(int days) throws RemoteException, FDResourceException;
	
	/** @return map of sku codes --&gt; oldness pairs */
	@Deprecated  public Map<String, Integer> getSkusOldness() throws RemoteException, FDResourceException;

    /** @return list of sku codes */
	@Deprecated public Collection getReintroducedSkuCodes(int days) throws RemoteException, FDResourceException;

	/** @return list of sku codes */
	@Deprecated public Collection getOutOfStockSkuCodes() throws RemoteException, FDResourceException;
	
	@Deprecated public Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes)throws FDResourceException, RemoteException;

	@Deprecated public List findPeakProduceSKUsByDepartment(List skuPrefixes) throws FDResourceException, RemoteException;
	
	
	@Deprecated public ErpZoneMasterInfo getZoneInfo(String zoneId) throws RemoteException, FDResourceException;
	
	@Deprecated public Collection getZoneInfos(String zoneIds[]) throws RemoteException, FDResourceException;
	
	
	@Deprecated public Collection getSkuCodes(String sapId) throws RemoteException, FDResourceException;
	
	@Deprecated public GroupScalePricing getGrpInfo(FDGroup group) throws RemoteException, FDGroupNotFoundException, FDResourceException;
	
	@Deprecated public ErpProductFamilyModel getFamilyInfo(String familyId) throws RemoteException, FDGroupNotFoundException, FDResourceException;
	
	@Deprecated public Collection getFilteredSkus(List skuList) throws RemoteException, FDResourceException;
	
	@Deprecated public Collection<GroupScalePricing> getGrpInfos(FDGroup grpIds[]) throws RemoteException, FDResourceException;
	
	@Deprecated public Map<String, Map<String,Date>> getNewSkus() throws RemoteException, FDResourceException;

	@Deprecated public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException, FDResourceException;

	@Deprecated public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException, FDResourceException;

	@Deprecated public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException, FDResourceException;
	
	@Deprecated public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException, FDResourceException;

	@Deprecated public void refreshNewAndBackViews() throws RemoteException, FDResourceException; 
	@Deprecated public FDGroup getLatestActiveGroup(String groupId) throws RemoteException, FDGroupNotFoundException, FDResourceException;

	@Deprecated public FDProductInfo getProductInfo(ErpProductInfoModel erpProdInfo) throws RemoteException, FDResourceException;
	@Deprecated public ErpProductFamilyModel getSkuFamilyInfo(String materialId)throws RemoteException, FDGroupNotFoundException, FDResourceException;
	
	@Deprecated public  Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws RemoteException, FDResourceException;
	/**
	 * Method to get all groups modified after lastModified time
	 * @param lastModified
	 * @return
	 * @throws FDResourceException
	 * @throws RemoteException
	 */
	@Deprecated public Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws FDResourceException, RemoteException;
}

