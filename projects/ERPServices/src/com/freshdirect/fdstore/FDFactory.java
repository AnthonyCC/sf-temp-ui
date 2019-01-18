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

import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.ecomm.gateway.ErpInfoService;
import com.freshdirect.ecomm.gateway.FDFactoryService;
import com.freshdirect.ecomm.gateway.FDFactoryServiceI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.ejb.FDFactoryHome;
import com.freshdirect.fdstore.ejb.FDFactorySB;
import com.freshdirect.fdstore.ejb.FDProductHelper;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

/**
 * Singleton class for accessing the FD-layer factory session bean.
 */
public class FDFactory {

	private static final Logger LOGGER = LoggerFactory.getInstance(FDFactory.class);

	private static FDFactoryHome factoryHome = null;

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

		try {

			if (!FDStoreProperties.getPreviewMode()) {
				return FDFactoryService.getInstance().getProductInfo(sku);
			} else {
				try {
					return getPreviewProductInfo(FDFactoryService.getInstance().getProductInfo(sku));
				} catch (FDSkuNotFoundException ex) {
					return getPreviewProductInfo(sku);
				}
			}

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static FDProductInfo getProductInfo(ErpProductInfoModel erpProdInfo) throws FDResourceException {
		
			
				return new FDProductHelper().getFDProductInfoNew(erpProdInfo);//::FDX::
		
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
		
		try {
			
				return FDFactoryService.getInstance().getProductInfo(sku,version);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}



	/**
	 * @param sapId
	 * @return
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException
	 */
	public static Collection<String> getSkuCodes(String sapId) throws FDResourceException, FDSkuNotFoundException {
		
		try {
			
				return FDFactoryService.getInstance().findSkusBySapId(sapId);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Collection<String> getSkuCodes(List<String> sapIds) throws FDResourceException, FDSkuNotFoundException {
		List skuCodes = new ArrayList<String>();
		
		try {
			for(Iterator<String>it = sapIds.iterator();it.hasNext();) {
				String sapId = it.next();
				
					skuCodes.addAll(FDFactoryService.getInstance().findSkusBySapId(sapId));
				
			}
            return skuCodes;

		
		} catch (RemoteException re) {
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
		try {
			return FDECommerceService.getInstance().findZoneInfoMaster(zoneId);

		} catch (RemoteException re) {
			LOGGER.error("RemoteException", re);
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
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpgrpInfoSessionBean equivalent service in  2.0
			
				pi = FDECommerceService.getInstance().findGrpInfoMaster(group);
	
		} catch (RemoteException re) {
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
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpProductFamilySessionBean equivalent service in  2.0
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductFamilySB)){
				pi = FDECommerceService.getInstance().findFamilyInfo(familyId);
        	}else{
    			FDFactorySB sb = factoryHome.create();

        		pi = sb.getFamilyInfo(familyId);
        	}
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
	 * @param materialId
	 * @return
	 * @throws FDGroupNotFoundException
	 * @throws FDResourceException
	 */
	public static ErpProductFamilyModel getSkuFamilyInfo(String materialId) throws FDGroupNotFoundException, FDResourceException  {
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		ErpProductFamilyModel pi;
		try {
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpProductFamilySessionBean equivalent service in  2.0
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpProductFamilySB)){
        		IECommerceService service = FDECommerceService.getInstance();
        		pi = service.findSkuFamilyInfo(materialId);
        	}else{
    			FDFactorySB sb = factoryHome.create();

        		pi = sb.getSkuFamilyInfo(materialId);
        	}
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
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpZoneInfoSessionBean equivalent service in  2.0
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpZoneInfoSB)){
				return FDECommerceService.getInstance().findZoneInfoMaster(zoneIds);
			}else{
				FDFactorySB sb = factoryHome.create();

				return sb.getZoneInfos(zoneIds);
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
	public static Collection<GroupScalePricing> getGrpInfo(FDGroup grpIds[]) throws FDResourceException {
		
		try {
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpGrpInfoSessionBean equivalent service in  2.0
			
				return FDECommerceService.getInstance().findGrpInfoMaster(grpIds);
			
		} catch (RemoteException re) {
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
		Collection pinfos;
		if (factoryHome==null) {
			lookupFactoryHome();
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDFactorySB_WarmUp)){
				pinfos = FDFactoryService.getInstance().getProductInfos(skus);
			}else{
				FDFactorySB sb = factoryHome.create();

				pinfos = sb.getProductInfos(skus);
			}

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

	/**
	 * Get the sku codes from erps.material table that have been modified since
	 * lastModifiedTime
	 *
	 * @param lastModified
	 *            unix timestamp in ms
	 * @return
	 * @throws FDResourceException
	 */
	public static Set<String> getModifiedSkuCodes(long lastModified) throws FDResourceException {
		if (factoryHome == null) {
			lookupFactoryHome();
		}
		Set<String> skus=null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDFactorySB_WarmUp)) {
				skus = FDFactoryService.getInstance().getModifiedSkus(lastModified);
			} else {
				FDFactorySB sb = factoryHome.create();
				skus = sb.getModifiedSkus(lastModified);
			}
			return skus;
		} catch (FDResourceException exc) {
			LOGGER.debug("Error occurred while calling getModifiedSkus("+Long.toString(lastModified)+")", exc);
			throw exc;
		} catch (CreateException ce) {
			factoryHome = null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome = null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @param days
	 * @return
	 * @throws FDResourceException
	 */
	public static Collection getNewSkuCodes(int days) throws FDResourceException {

		try {
			
			return ErpInfoService.getInstance().findNewSkuCodes(days);
			

		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return
	 * @throws FDResourceException
	 */
	public static Map<String, Integer> getSkusOldness() throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().getSkusOldness();
			
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @param days
	 * @return
	 * @throws FDResourceException
	 */
	public static Collection getReintroducedSkuCodes(int days) throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().findReintroducedSkuCodes(days);
			
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	/**
	 * @return
	 * @throws FDResourceException
	 */
	public static Collection getOutOfStockSkuCodes() throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().findOutOfStockSkuCodes();
			
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
			pinfo.getZonePriceInfoList(),pinfo.getGroups(),
			pinfo.getUpc(),pinfo.getFamilyID(),pinfo.getPlantMaterialInfo(),pinfo.getAvailability(),pinfo.getAlcoholType());

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

		return new FDProductInfo(skuCode,0,null,ZonePriceInfoListing.getDummy(),plantInfo,mAvail,EnumAlcoholicContent.NONE);

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
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDFactorySB_WarmUp)){
				FDFactoryServiceI service =  FDFactoryService.getInstance();
				return service.getProduct(sku, version);
			}else{
				FDFactorySB sb = factoryHome.create();

				return sb.getProduct(sku, version);
			}

		} catch (CreateException ce) {
			factoryHome=null;
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean; failed to retrieve FDProduct for SKU with skucode=" + sku + " and version=" + version);
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
		
		try {
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpGrpInfoSessionBean equivalent service in  2.0
			
				return FDECommerceService.getInstance().getFilteredSkus(skuList);
			
		} catch (RemoteException re) {
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
	/**
	 * @param lowerLimit
	 * @param upperLimit
	 * @param skuPrefixes
	 * @return
	 * @throws FDResourceException
	 */
	public static Collection findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().findSKUsByDeal(lowerLimit, upperLimit, skuPrefixes);
			
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}


	/**
	 * @param skuPrefixes
	 * @return
	 * @throws FDResourceException
	 */
	public static List findPeakProduceSKUsByDepartment(List skuPrefixes) throws FDResourceException {
		
		try {
			return (List) FDECommerceService.getInstance().findPeakProduceSKUsByDepartment(skuPrefixes);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	/**
	 * @return
	 * @throws FDResourceException
	 */
	public static Map<String, Map<String,Date>> getNewSkus() throws FDResourceException {
		Map<String, Map<String,Date>> overridden;
		Map<String, Map<String,Date>> overriddenBack;
		Map<String, Map<String,Date>> regular;
		
		try {
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			
				regular = ErpInfoService.getInstance().getNewSkus();
				overridden = ErpInfoService.getInstance().getOverriddenNewSkus();
				overriddenBack = ErpInfoService.getInstance().getOverriddenBackInStockSkus();
			
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
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return
	 * @throws FDResourceException
	 */
	public static Map<String, Map<String,Date>> getBackInStockSkus() throws FDResourceException {
		Map<String, Map<String,Date>> regular;
		Map<String, Map<String,Date>> overridden;
		
		try {
			Date now = new Date();
			Date first = new Date(now.getTime() - 30l * 24l * 3600000l);
			
				regular = ErpInfoService.getInstance().getBackInStockSkus();
				overridden = ErpInfoService.getInstance().getOverriddenBackInStockSkus();
			
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
							Map<String, Date> _value=results.get(product);
							if(null == _value){
								_value = new HashMap<String, Date>();
							}
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
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	/**
	 * @return
	 * @throws FDResourceException
	 */
	public static Map<String, Map<String,Date>> getOverriddenNewSkus() throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().getOverriddenNewSkus();
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws FDResourceException {
		
		try {
				return ErpInfoService.getInstance().getOverriddenBackInStockSkus();
			
		} catch (RemoteException re) {
			
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws FDResourceException {
		
		try {
			
				return ErpInfoService.getInstance().getSkuAvailabilityHistory(skuCode);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void refreshNewAndBackViews() throws FDResourceException {
		
		try {
			
				ErpInfoService.getInstance().refreshNewAndBackViews();
			
		} catch (RemoteException re) {
			factoryHome=null;
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static FDGroup getLatestActiveGroup(String groupId) throws FDResourceException, FDGroupNotFoundException {
		
		try {
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpGrpInfoSessionBean equivalent service in  2.0
			
				return FDECommerceService.getInstance().getLatestActiveGroup(groupId);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String,FDGroup> getGroupIdentityForMaterial(String matId) throws FDResourceException{
		
		try {
				return FDECommerceService.getInstance().getGroupIdentityForMaterial(matId);
			

		
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Map<String,List<String>> getModifiedOnlyGroups(Date lastModified) throws FDResourceException{
		try {
			// FDFactortySession bean is just a pass through,
	 		//so we are directly calling ErpGrpInfoSessionBean equivalent service in  2.0
			
				return 	FDECommerceService.getInstance().getModifiedOnlyGroups(lastModified);
			
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
