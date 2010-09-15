/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;

/**
 * Singleton class for accessing the FD-layer factory session bean.
 *
 * @version $Revision$
 * @author $Author$
 */
class FDFactory {

	private static FDFactoryHome factoryHome = null;

	
        final static FDProductInfo SKU_NOT_FOUND = new FDProductInfo(null, 0, null,null,null,null,null,null,null,null,null);
	
	/**@link dependency
	 * @label creates*/
	/*#FDProductInfo lnkFDProductInfo;*/

	/**@link dependency
	 * @label creates*/
	/*#FDProduct lnkFDProduct;*/

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
	public static FDProductInfo getProductInfo(String sku) throws FDResourceException, FDSkuNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();

            if (!FDStoreProperties.getPreviewMode()) {
				return sb.getProductInfo(sku);
            } else {
            	try {
            		return getPreviewProductInfo( sb.getProductInfo(sku) );
            	} catch (FDSkuNotFoundException ex) {
            		return getPreviewProductInfo(sku);	
            	}
            }
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
    
    /**
	 * Get product information object for a specific version of a sku.
	 *
	 * @param sku SKU code
     * @param version requested version
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProductInfo getProductInfo(String sku, int version) throws FDResourceException, FDSkuNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();

            return sb.getProductInfo(sku, version);
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	
	  /**
	 * Get product information object for a specific version of a sku.
	 *
	 * @param sku SKU code
     * @param version requested version
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static ErpZoneMasterInfo getZoneInfo(String zoneId) throws FDResourceException  {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();

            return sb.getZoneInfo(zoneId);
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	  /**
	 * Get product information object for a specific version of a sku.
	 *
	 * @param sku SKU code
     * @param version requested version
	 *
	 * @return FDProductInfo object
	 *
 	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getZoneInfo(String zoneIds[]) throws FDResourceException, FDSkuNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();

            return sb.getZoneInfos(zoneIds);
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	/**
	 * Get current product information object for multiple SKUs.
	 *
	 * @param skus array of SKU codes
	 *
	 * @return a list of FDProductInfo objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection getProductInfos(String[] skus) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			Collection pinfos = sb.getProductInfos(skus);

            if (FDStoreProperties.getPreviewMode()) {
            	
            	Set foundSkus = new HashSet();
                LinkedList newPinfos = new LinkedList();
                for (Iterator piIter = pinfos.iterator(); piIter.hasNext(); ) {
                	FDProductInfo pi = getPreviewProductInfo( (FDProductInfo) piIter.next() );
                    newPinfos.add( pi );
                    foundSkus.add( pi.getSkuCode() );
                }
                if (foundSkus.size()!=skus.length) {
                	// some skus were not found, fake'em
                	for (int i=skus.length; --i>=0; ) {
                		if (!foundSkus.contains(skus[i])) {
							newPinfos.add( getPreviewProductInfo( skus[i] ) );
						}
	            	}
	            }
                pinfos = newPinfos;
            }
            
            return pinfos;

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Collection getNewSkuCodes(int days) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getNewSkuCodes(days);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Integer> getSkusOldness() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getSkusOldness();

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Collection getReintroducedSkuCodes(int days) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getReintroducedSkuCodes(days);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
    

	public static Collection getOutOfStockSkuCodes() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getOutOfStockSkuCodes();

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}

	/**
	 * Utility method: nothing is ever discontinued, out of season, or indefinitely unavailable in preview mode
	 */
	private static FDProductInfo getPreviewProductInfo(FDProductInfo pinfo) {
		return new FDProductInfo(
			pinfo.getSkuCode(),
			pinfo.getVersion(),
			null,
			EnumATPRule.JIT,
			EnumAvailabilityStatus.AVAILABLE,
			new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),
			null,pinfo.getRating(),pinfo.getFreshness(), pinfo.getDefaultPriceUnit(), pinfo.getZonePriceInfoList());
	}
	
	/**
	 * Utility method: create a temporarily unavailable fake FDProductInfo
	 */
	private static FDProductInfo getPreviewProductInfo(String skuCode) {
		return new FDProductInfo(
			skuCode,
			0,
			null,
			EnumATPRule.JIT,
			EnumAvailabilityStatus.TEMP_UNAV,
			new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),
			null,"",null,"LB",ZonePriceInfoListing.getDummy());
	}


	/**
	 * Get product with specified version. 
	 *
	 * @param sku SKU code
	 * @param version requested version
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProduct getProduct(String sku, int version) throws FDResourceException, FDSkuNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getProduct(sku, version);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param sku FDSku instance
	 *
	 * @return FDProduct object
	 *
	 * @throws FDSkuNotFoundException if the SKU was not found in ERP services
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static FDProduct getProduct(FDSku sku) throws FDResourceException, FDSkuNotFoundException {
		return getProduct(sku.getSkuCode(), sku.getVersion());
	}

	/**
	 * Convenience method to get the product for the specified FDProductInfo.
	 *
	 * @param skus array of FDSku instances
	 *
	 * @return collection of FDProduct objects found
	 *
	 * @throws FDResourceException if an error occured using remote resources
	 */
	public static Collection<FDProduct> getProducts(FDSku[] skus) throws FDResourceException {
		// !!! optimize this, so that it only makes one call to the session bean
		List<FDProduct> products = new ArrayList<FDProduct>(skus.length);
		for (int i=0; i<skus.length; i++) {
			try {
				products.add( getProduct(skus[i]) );
			} catch (FDSkuNotFoundException ex) {
				// not found
			}
		}
		return products;
	}

	protected static void lookupFactoryHome() throws FDResourceException {
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			factoryHome = (FDFactoryHome) ctx.lookup( FDStoreProperties.getFDFactoryHome() );
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException e) {
			}
		}
	}
	public static Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}
	
	
	public static List findPeakProduceSKUsByDepartment(List skuPrefixes) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.findPeakProduceSKUsByDepartment(skuPrefixes);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		
	}

	public static Map<String, Date> getNewSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			Date now = new Date();
			Date first = new Date(now.getTime() - 120l * 24l * 3600000l);
			Map<String, Date> regular = sb.getNewSkus();
			Map<String, Date> overridden = sb.getOverriddenNewSkus();
			Map<String, Date> overriddenBack = sb.getOverriddenBackInStockSkus();
			Map<String, Date> results = new HashMap<String, Date>((regular.size() + overridden.size()) * 4 / 3);
			for (Map.Entry<String, Date> entry : regular.entrySet())
				if (entry.getValue().after(first) && entry.getValue().before(now))
					results.put(entry.getKey(), entry.getValue());
			for (Map.Entry<String, Date> entry : overridden.entrySet())
				if (entry.getValue().compareTo(first) <= 0)
					results.remove(entry.getKey());
				else if (entry.getValue().before(now))
					results.put(entry.getKey(), entry.getValue());
			for (Map.Entry<String, Date> entry : overriddenBack.entrySet())
				results.remove(entry.getKey());
			return results;
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Date> getBackInStockSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			Map<String, Date> regular = sb.getBackInStockSkus();
			Map<String, Date> overridden = sb.getOverriddenBackInStockSkus();
			Map<String, Date> results = new HashMap<String, Date>((regular.size() + overridden.size()) * 4 / 3);
			Map<String, Date> newSkus = getNewSkus();
			for (Map.Entry<String, Date> entry : regular.entrySet())
				if (entry.getValue().after(first) && entry.getValue().before(now) && !newSkus.containsKey(entry.getKey()))
					results.put(entry.getKey(), entry.getValue());
			for (Map.Entry<String, Date> entry : overridden.entrySet())
				if (entry.getValue().compareTo(first) <= 0)
					results.remove(entry.getKey());
				else if (entry.getValue().before(now) && !newSkus.containsKey(entry.getKey()))
					results.put(entry.getKey(), entry.getValue());
			return results;
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Date> getOverriddenNewSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getOverriddenNewSkus();
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Date> getOverriddenBackInStockSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getOverriddenBackInStockSkus();
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			return sb.getSkuAvailabilityHistory(skuCode);

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void refreshNewAndBackViews() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			sb.refreshNewAndBackViews();

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
        public static List<FDSku> getChangedSkus(int lastVersion) throws FDResourceException {
            try {
                return ERPServiceLocator.getInstance().getErpInfoSessionBean().getChangedSkus(lastVersion);
            } catch (EJBException re) {
                throw new FDResourceException(re, "Error talking to session bean");
            } catch (RemoteException re) {
                throw new FDResourceException(re, "Error talking to session bean");
            }
        }

        public static List<FDSku> getLatestOrChangedSkus(Integer lastVersion) throws FDResourceException {
            try {
                if (lastVersion == null) {
                    return ERPServiceLocator.getInstance().getErpInfoSessionBean().getLatestSkus();
                } else {
                    return ERPServiceLocator.getInstance().getErpInfoSessionBean().getChangedSkus(lastVersion);
                }
            } catch (EJBException re) {
                throw new FDResourceException(re, "Error talking to session bean");
            } catch (RemoteException re) {
                throw new FDResourceException(re, "Error talking to session bean");
            }
            
        }
}
