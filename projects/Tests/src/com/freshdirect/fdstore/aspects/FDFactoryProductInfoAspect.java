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
import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.TestFDInventoryCache;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.framework.util.DateUtil;

public class FDFactoryProductInfoAspect implements Aspect {

    final static Logger LOG = Logger.getLogger(FDFactoryProductInfoAspect.class);
    
    Set avialableSkus     = new HashSet();
    Set tomorrowAvailable = new HashSet();
    Map prices            = new HashMap();

    public FDFactoryProductInfoAspect addAvailableSku(String sku) {
        avialableSkus.add(sku);
        return this;
    }

    public FDFactoryProductInfoAspect addAvailableSku(String sku, double price) {
        avialableSkus.add(sku);
        prices.put(sku, new Double(price));
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

    public Pointcut getPointcut() {
        return new DebugMethodPatternPointCut("FDFactorySessionBean\\.getProductInfo\\(java.lang.String\\)");
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

        if (avialableSkus.contains(sku)) {
            // return this item as available
            // this SKU is not included in the ConfiguredProduct "ok"
            // a 10000 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
            productInfo = new FDProductInfo(sku, 1, price, "ea", materials, EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now, "",
                    inventoryCache, "", 0.0, "", false, -1);
        } else if (tomorrowAvailable.contains(sku)) {
            // return this item as available by tomorrow, but not today
            Date tomorrow = DateUtil.addDays(now, 1);
            // a 10000 units available starting tomorrow
            erpEntries.add(new ErpInventoryEntryModel(tomorrow, 10000));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
            productInfo = new FDProductInfo(sku, 1, price, "ea", materials, EnumATPRule.MATERIAL, EnumAvailabilityStatus.AVAILABLE, now, "",
                    inventoryCache, "", 0.0, "", false, -1);
        } else {
            // fallback: return any unknown item as unavailable
            // a 0 units available starting now
            erpEntries.add(new ErpInventoryEntryModel(now, 0));
            inventoryCache.addInventory(materials[0], new ErpInventoryModel("SAP12345", now, erpEntries));
            productInfo = new FDProductInfo(sku, 1, 1.0, "ea", materials, EnumATPRule.MATERIAL, EnumAvailabilityStatus.DISCONTINUED, now, "",
                    inventoryCache, "", 0.0, "", false, -1);
        }

        return productInfo;
    }
}