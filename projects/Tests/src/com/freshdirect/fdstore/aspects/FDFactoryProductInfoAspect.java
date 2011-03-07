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

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoListing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.framework.util.DateUtil;

public class FDFactoryProductInfoAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(FDFactoryProductInfoAspect.class);
    
    Set<String> avialableSkus     = new HashSet<String>();
    Set<String> tomorrowAvailable = new HashSet<String>();
    Map<String, Double> prices    = new HashMap<String, Double>();
    Map<String, Double> promoPrices = new HashMap<String, Double> ();
    Map<String, List<ZonePriceInfoModel>> zonePrices = new HashMap<String, List<ZonePriceInfoModel>> ();

    public FDFactoryProductInfoAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProductInfo\\(java.lang.String\\)"));
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

    public void intercept(InvocationContext ctx) throws Exception {
        String sku = (String) ctx.getParamVals()[0];
        LOG.info("getProductInfo for "+sku);
        ctx.setReturnObject(getProductInfo(sku));
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
        List erpEntries = new ArrayList();
        TestFDInventoryCache inventoryCache = new TestFDInventoryCache();
        FDProductInfo productInfo;

        Double defPrice = (Double) prices.get(sku);
        double price = (defPrice != null) ? defPrice.doubleValue() : 1.0;
        
        Double defPromoPrice = (Double) promoPrices.get(sku);
        double promoPrice = (defPromoPrice != null) ? defPromoPrice.doubleValue() : 0.0;

        if (avialableSkus.contains(sku)) {
            // return this item as available
            // this SKU is not included in the ConfiguredProduct "ok"
            // a 10000 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(price, promoPrice, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
    		
    		fillZonePriceList(sku, dummyList);
    		
            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"", null, dummyList, null,"");
        } else if (tomorrowAvailable.contains(sku)) {
            // return this item as available by tomorrow, but not today
            Date tomorrow = DateUtil.addDays(now, 1);
            // a 10000 units available starting tomorrow
            erpEntries.add(new ErpInventoryEntryModel(tomorrow, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));

    		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(price, promoPrice, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
                fillZonePriceList(sku, dummyList);
    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);
            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now,inventoryCache,"",null, dummyList, null,"");
        } else {
            // fallback: return any unknown item as unavailable
            // a 0 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 0));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
       		ZonePriceInfoListing dummyList = new ZonePriceInfoListing();
    		ZonePriceInfoModel dummy = new ZonePriceInfoModel(1.0, 0.0, "ea", null, false, 0, 0, ZonePriceListing.MASTER_DEFAULT_ZONE);
    		dummyList.addZonePriceInfo(ZonePriceListing.MASTER_DEFAULT_ZONE, dummy);            
            productInfo = new FDProductInfo(sku,1, materials,EnumATPRule.MATERIAL, EnumAvailabilityStatus.DISCONTINUED, now,inventoryCache,"", null, dummyList, null,"");
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
                dummyList.addZonePriceInfo(zp.getSapZoneId(), zp);
            }
        }
    }
}
