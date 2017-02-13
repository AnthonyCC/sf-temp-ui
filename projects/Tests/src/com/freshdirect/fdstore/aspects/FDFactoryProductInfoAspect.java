/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDMaterialSalesArea;
import com.freshdirect.fdstore.FDPlantMaterial;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.DayOfWeekSet;

public class FDFactoryProductInfoAspect extends BaseProductInfoAspect {
    
    Set<String> avialableSkus     = new HashSet<String>();
    Set<String> tomorrowAvailable = new HashSet<String>();
    Map<String, Double> prices    = new HashMap<String, Double>();
    Map<String, Double> promoPrices = new HashMap<String, Double> ();
    Map<String, List<ZonePriceInfoModel>> zonePrices = new HashMap<String, List<ZonePriceInfoModel>> ();

    public FDFactoryProductInfoAspect() {
        super();
    }
    
    public FDFactoryProductInfoAspect addAvailableSku(String sku) {
        avialableSkus.add(sku);
        return this;
    }

    public FDFactoryProductInfoAspect addAvailableSku(String sku, double price) {
        avialableSkus.add(sku);
        prices.put(sku, new Double(price));
        return this;
    }

    public FDFactoryProductInfoAspect addAvailableSku(String sku, double price, double promoPrice) {
        avialableSkus.add(sku);
        prices.put(sku, new Double(price));
        promoPrices.put(sku, Double.valueOf(promoPrice));
        return this;
    }
    
    public FDFactoryProductInfoAspect addZonePriceInfo(String sku, ZonePriceInfoModel zonePrice) {
        List<ZonePriceInfoModel> prices = zonePrices.get(sku);
        if (prices == null) {
            prices = new ArrayList<ZonePriceInfoModel>();
            zonePrices.put(sku, prices);
        }
        prices.add(zonePrice);
        return this;
    }
    
    
    public FDFactoryProductInfoAspect addTomorrowAvailablSku(String sku) {
        tomorrowAvailable.add(sku);
        return this;
    }

    public FDFactoryProductInfoAspect addTomorrowAvailablSku(String sku, double price) {
        tomorrowAvailable.add(sku);
        prices.put(sku, new Double(price));
        return this;
    }

    /**
     * Get current product information object for sku.
     * 
     * @param sku
     *            SKU code
     * 
     * @return FDProductInfo object
     * 
     * @throws FDSkuNotFoundException
     *             if the SKU was not found in ERP services
     * @throws FDResourceException
     *             if an error occured using remote resources
     */
    public FDProductInfo getProductInfo(String sku) throws RemoteException, FDSkuNotFoundException, FDResourceException {
        Date now = new Date();
        String[] materials = { "000000000123" };
        Date[] availDates = new Date[0];
        List erpEntries = new ArrayList();
        TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
        FDProductInfo productInfo;

        Double defPrice = (Double) prices.get(sku);
        double price = (defPrice != null) ? defPrice.doubleValue() : 1.0;
        
        Double defPromoPrice = (Double) promoPrices.get(sku);
        double promoPrice = (defPromoPrice != null) ? defPromoPrice.doubleValue() : 0.0;
        
        
        Map<String,FDPlantMaterial> plantInfo=null;
        Map<String, FDMaterialSalesArea> mAvail=null;
        
        
		
		
		
		

        if (avialableSkus.contains(sku)) {
            // return this item as available
            // this SKU is not included in the ConfiguredProduct "ok"
            // a 10000 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(price, promoPrice, "ea", null, false, 0, 0, ZonePriceListing.DEFAULT_ZONE_INFO);
    		dummyList.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO, dummy);
    		
    		fillZonePriceList(sku, dummyList);

    		plantInfo=new HashMap<String,FDPlantMaterial>() {
    			{
    				put("1000",new FDPlantMaterial(EnumATPRule.MATERIAL,false,false,DayOfWeekSet.EMPTY,1,"1000",false));
    			}
    		};
    		
    		mAvail=new HashMap<String, FDMaterialSalesArea>(){
    			{put("1000"+"1000",new FDMaterialSalesArea(new SalesAreaInfo("1000","1000"),EnumAvailabilityStatus.AVAILABLE.getStatusCode(),new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),"XYZ",null, "1000"));
    			};
    		};
    		;
            productInfo = new FDProductInfo(sku,1, materials,inventoryCache, dummyList,plantInfo,mAvail,false);
        } else if (tomorrowAvailable.contains(sku)) {
            // return this item as available by tomorrow, but not today
            Date tomorrow = DateUtil.addDays(now, 1);
            // a 10000 units available starting tomorrow
            erpEntries.add(new ErpInventoryEntryModel(tomorrow, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));

    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(price, promoPrice, "ea", null, false, 0, 0, ZonePriceListing.DEFAULT_ZONE_INFO);
                fillZonePriceList(sku, dummyList);
    		dummyList.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO, dummy);
    		plantInfo=new HashMap<String,FDPlantMaterial>() {
    			{
    				put("1000",new FDPlantMaterial(EnumATPRule.MATERIAL,false,false,DayOfWeekSet.EMPTY,1,"1000",false));
    			}
    		};
    		
    		mAvail=new HashMap<String, FDMaterialSalesArea>(){
    			{put("1000"+"1000",new FDMaterialSalesArea(new SalesAreaInfo("1000","1000"),EnumAvailabilityStatus.AVAILABLE.getStatusCode(),new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),"XYZ",null, "1000"));
    			};
    		};
            //productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,null,null, dummyList, null, null, null);
            productInfo = new FDProductInfo(sku,1, materials,inventoryCache, dummyList,plantInfo,mAvail,false);
        } else {
            // fallback: return any unknown item as unavailable
            // a 0 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 0));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
       		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 0.0, "ea", null, false, 0, 0, ZonePriceListing.DEFAULT_ZONE_INFO);
    		dummyList.addZonePriceInfo(ZonePriceListing.DEFAULT_ZONE_INFO, dummy);          
    		plantInfo=new HashMap<String,FDPlantMaterial>() {
    			{
    				put("1000",new FDPlantMaterial(EnumATPRule.MATERIAL,false,false,DayOfWeekSet.EMPTY,1,"1000",false));
    			}
    		};
    		
    		mAvail=new HashMap<String, FDMaterialSalesArea>(){
    			{put("1000"+"1000",new FDMaterialSalesArea(new SalesAreaInfo("1000","1000"),EnumAvailabilityStatus.DISCONTINUED.getStatusCode(),new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime(),"XYZ",null, "1000"));
    			};
    		};
            
            productInfo = new FDProductInfo(sku,1, materials,inventoryCache, dummyList,plantInfo,mAvail,false);
        }

        return productInfo;
    }

    /**
     * @param sku
     * @param dummyList
     */
    private void fillZonePriceList(String sku, ZonePriceInfoListing dummyList) {
        List<ZonePriceInfoModel> list = zonePrices.get(sku);
        if (list != null) {
            for (ZonePriceInfoModel zp : list) {
                dummyList.addZonePriceInfo(zp.getZoneInfo(), zp);
            }
        }
    }
}
