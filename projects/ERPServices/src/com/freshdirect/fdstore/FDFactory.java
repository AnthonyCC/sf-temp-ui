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
import javax.naming.Context;
import javax.naming.NamingException;

import com.freshdirect.common.pricing.GrpMaterialPrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

/**
 * Singleton class for accessing the FD-layer factory session bean.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDFactory {

	private static FDFactoryHome factoryHome = null;

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
	
	public static FDProductInfo getProductInfo(ErpProductInfoModel erpProdInfo) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();  
			return sb.getProductInfo(erpProdInfo);
           
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

	
	
	public static Collection<String> getSkuCodes(String sapId) throws FDResourceException, FDSkuNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();

            return sb.getSkuCodes(sapId);
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Collection<String> getSkuCodes(List<String> sapIds) throws FDResourceException, FDSkuNotFoundException {
		List skuCodes = new ArrayList<String>();
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			for(Iterator<String>it = sapIds.iterator();it.hasNext();) {
				String sapId = it.next();
				skuCodes.addAll(sb.getSkuCodes(sapId));
			}
            return skuCodes;
           
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
	public static GroupScalePricing getGrpInfo(FDGroup group) throws FDGroupNotFoundException, FDResourceException  {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		GroupScalePricing pi;
		try {
			FDFactorySB sb = factoryHome.create();                                       		
			pi = sb.getGrpInfo(group);				
				
		}catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return pi;
	}
	
	public static ErpProductFamilyModel getFamilyInfo(String familyId) throws FDGroupNotFoundException, FDResourceException  {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		ErpProductFamilyModel pi;
		try {
			FDFactorySB sb = factoryHome.create();  
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		pi = service.findFamilyInfo(familyId);
        	}else
        		pi = sb.getFamilyInfo(familyId);				
				
		}catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return pi;
	}
	public static ErpProductFamilyModel getSkuFamilyInfo(String materialId) throws FDGroupNotFoundException, FDResourceException  {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		ErpProductFamilyModel pi;
		try {
			FDFactorySB sb = factoryHome.create(); 
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		pi = service.findSkuFamilyInfo(materialId);
        	}else
        		pi = sb.getSkuFamilyInfo(materialId);				
				
		}catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return pi;
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
	public static Collection<GroupScalePricing> getGrpInfo(FDGroup grpIds[]) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
				return FDECommerceService.getInstance().findGrpInfoMaster(grpIds);
			}else{
			FDFactorySB sb = factoryHome.create();
			return sb.getGrpInfos(grpIds);
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
//                	FDProductInfo pi = (FDProductInfo) piIter.next();
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
			null, pinfo.getZonePriceInfoList(),pinfo.getGroups(),
			pinfo.getUpc(),pinfo.getFamilyID(),pinfo.getPlantMaterialInfo(),pinfo.getAvailability(),pinfo.isAlcohol());
		
		/*(String skuCode, int version, 
	    		String[] materialNumbers, EnumATPRule atpRule, EnumAvailabilityStatus availStatus, Date availDate, 
	    		FDInventoryCacheI inventory, EnumOrderLineRating rating, String freshness,
	    		ZonePriceInfoListing zonePriceInfoList, FDGroup group, EnumSustainabilityRating sustainabilityRating,
	    		String upc,List<FDPlantMaterial> plantMaterialInfo,Map<String, FDMaterialSalesArea> materialSalesArea)*/
		
		
	}
	
	/**
	 * Utility method: create a temporarily unavailable fake FDProductInfo
	 */
	private static FDProductInfo getPreviewProductInfo(String skuCode) {
		Map<String,FDPlantMaterial> plantInfo=new HashMap<String,FDPlantMaterial>() {
			{
				put("1000",new FDPlantMaterial(EnumATPRule.JIT,false,false,DayOfWeekSet.EMPTY,1,"1000",false));
			}
		};
		
		Map<String, FDMaterialSalesArea> mAvail=new HashMap<String, FDMaterialSalesArea>(){
			{put("1000"+"1000",new FDMaterialSalesArea(new SalesAreaInfo("1000","1000"),EnumAvailabilityStatus.TEMP_UNAV.getStatusCode(),new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),"XYZ",null,"1000"));
			};
		};
		;
		
		return new FDProductInfo(skuCode,0,null,null,ZonePriceInfoListing.getDummy(),plantInfo,mAvail,false);
		
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
	
	
	public static Collection getFilteredSkus(List skuList) throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
				return FDECommerceService.getInstance().getFilteredSkus(skuList);
			}else{
			FDFactorySB sb = factoryHome.create();
            return sb.getFilteredSkus(skuList);
			}
           
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static Collection getProducts(FDSku[] skus) throws FDResourceException {
		// !!! optimize this, so that it only makes one call to the session bean
		List products = new ArrayList(skus.length);
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

	public static Map<String, Map<String,Date>> getNewSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			Map<String, Map<String,Date>> regular = sb.getNewSkus();
			Map<String, Map<String,Date>> overridden = sb.getOverriddenNewSkus();
			Map<String, Map<String,Date>> overriddenBack = sb.getOverriddenBackInStockSkus();
			Map<String, Map<String,Date>> results = new HashMap<String, Map<String,Date>>((regular.size() + overridden.size()) * 4 / 3);
			String product="";
			for (Map.Entry<String, Map<String,Date>> entry : regular.entrySet()) {
				product=entry.getKey();
				for(Map.Entry<String,Date> valueEntry:entry.getValue().entrySet() ) {
						
						if (valueEntry.getValue().after(first) && valueEntry.getValue().before(now)) {
							if(results.containsKey(product)) {
								Map<String,Date> _value=results.get(product);
								_value.put(valueEntry.getKey(), valueEntry.getValue());
								results.put(product, _value);
							} else {
								Map<String, Date> _value=new HashMap<String, Date>();
								_value.put(valueEntry.getKey(), valueEntry.getValue());
							  results.put(product,_value);
							}
						}
				 }
			}
			
			for (Map.Entry<String,  Map<String,Date>> entry : overridden.entrySet()) {
				product=entry.getKey();
				for(Map.Entry<String,Date> valueEntry:entry.getValue().entrySet() ) {
					if (valueEntry.getValue().compareTo(first) <= 0) {
						Map<String,Date> _value=results.get(product);
						if(_value!=null) {
							_value.remove(valueEntry.getKey());
							if(_value.isEmpty()) {
								results.remove(product);
							} else {
								// do nothing now.
							}
						}
					} else if (valueEntry.getValue().before(now)) {
						Map<String,Date> _value=results.get(product);
						if(_value!=null) {
							_value.put(valueEntry.getKey(), valueEntry.getValue());
							results.put(product, _value);
						}else {
							
							_value=new HashMap<String, Date>();
							_value.put(valueEntry.getKey(), valueEntry.getValue());
						  results.put(product,_value);
						}
					}
				}
			}
			
			for (Map.Entry<String, Map<String,Date>> entry : overriddenBack.entrySet()) {
				product=entry.getKey();
				for(Map.Entry<String,Date> valueEntry:entry.getValue().entrySet() ) {
					if(results.containsKey(product)) {
						Map<String,Date> _value=results.get(product);
						if(_value!=null) {
							_value.remove(valueEntry.getKey());
							if(_value.isEmpty()) {
								results.remove(product);
							} else {
								// do nothing now.
							}
						}
					}
				}
			}
			
			/*for (Map.Entry<String, Date> entry : overridden.entrySet())
				if (entry.getValue().compareTo(first) <= 0)
					results.remove(entry.getKey());
				else if (entry.getValue().before(now))
					results.put(entry.getKey(), entry.getValue());
			for (Map.Entry<String, Date> entry : overriddenBack.entrySet())
				results.remove(entry.getKey());*/
			//System.out.println();
			return results;
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Map<String,Date>> getBackInStockSkus() throws FDResourceException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			FDFactorySB sb = factoryHome.create();
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			Map<String, Map<String,Date>> regular = sb.getBackInStockSkus();
			Map<String, Map<String,Date>> overridden = sb.getOverriddenBackInStockSkus();
			Map<String, Map<String,Date>> results = new HashMap<String, Map<String,Date>>((regular.size() + overridden.size()) * 4 / 3);
			Map<String, Map<String,Date>> newSkus = getNewSkus();
			String product="";
			for (Map.Entry<String, Map<String,Date>> entry : regular.entrySet()) {
				product=entry.getKey();
				for(Map.Entry<String,Date> valueEntry:entry.getValue().entrySet() ) {
						
						if (valueEntry.getValue().after(first) && valueEntry.getValue().before(now)) {
							if(results.containsKey(product)) {
								Map<String,Date> _value=regular.get(product);
								_value.put(valueEntry.getKey(), valueEntry.getValue());
								results.put(product, _value);
							} else {
								Map<String, Date> _value=new HashMap<String, Date>();
								_value.put(valueEntry.getKey(), valueEntry.getValue());
							  results.put(product,_value);
							}
						}
				 }
			}
			
			for (Map.Entry<String, Map<String,Date>> entry : overridden.entrySet()) {
				product=entry.getKey();
				for(Map.Entry<String,Date> valueEntry:entry.getValue().entrySet() ) {
					if(valueEntry.getValue().compareTo(first) <= 0) {
						Map<String,Date> _value=results.get(product);
						if(_value!=null) {
							_value.remove(valueEntry.getKey());
							if(_value.isEmpty()) {
								results.remove(product);
							} else {
								// do nothing now.
							}
						}
					} else if (valueEntry.getValue().before(now)){
						if(newSkus.containsKey(product)) {
							Map<String,Date> _value=regular.get(product);
							if(_value!=null &&_value.containsKey(valueEntry.getKey())) {
								_value.put(valueEntry.getKey(), valueEntry.getValue());
								results.put(product, _value);
							}
						} else {
							Map<String, Date> _value=new HashMap<String, Date>();
							_value.put(valueEntry.getKey(), valueEntry.getValue());
						    results.put(product,_value);
						}
					}
				}
			}
				
			/*for (Map.Entry<String, Date> entry : overridden.entrySet())
				if (entry.getValue().compareTo(first) <= 0)
					results.remove(entry.getKey());
				else if (entry.getValue().before(now) && !newSkus.containsKey(entry.getKey()))
					results.put(entry.getKey(), entry.getValue());
					*/
			return results;
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Map<String,Date>> getOverriddenNewSkus() throws FDResourceException {
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

	public static Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws FDResourceException {
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
	public static FDGroup getLatestActiveGroup(String groupId) throws FDResourceException, FDGroupNotFoundException {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
			return FDECommerceService.getInstance().getLatestActiveGroup(groupId);
			}else{
			FDFactorySB sb = factoryHome.create();
          return sb.getLatestActiveGroup(groupId);
			}
         
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws FDResourceException{
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
				return FDECommerceService.getInstance().getGroupIdentityForMaterial(matId);
			}else{
			FDFactorySB sb = factoryHome.create();
          return sb.getGroupIdentityForMaterial(matId);
			}
         
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws FDResourceException{
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isStorefront2_0Enabled()){
			return 	FDECommerceService.getInstance().getModifiedOnlyGroups(lastModified);
			}else{
			FDFactorySB sb = factoryHome.create();
			return sb.getModifiedOnlyGroups(lastModified);
			}
         
		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
