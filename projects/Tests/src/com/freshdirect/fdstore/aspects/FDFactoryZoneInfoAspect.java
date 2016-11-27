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

import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
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

public class FDFactoryZoneInfoAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(FDFactoryZoneInfoAspect.class);
    
    public FDFactoryZoneInfoAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getZoneInfo\\(java.lang.String\\)"));
    }

    public void intercept(InvocationContext ctx) throws Exception {
        String zoneId = (String) ctx.getParamVals()[0];
        LOG.info("getZoneInfo for "+zoneId);
        ctx.setReturnObject(getZoneInfo(zoneId));
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
    public ErpZoneMasterInfo getZoneInfo(String zoneId) throws RemoteException, FDSkuNotFoundException, FDResourceException {
    	ErpZoneRegionInfo regionInfo = new ErpZoneRegionInfo("00001000","Master Default Region");
    	ErpZoneMasterInfo zoneInfo = new ErpZoneMasterInfo("0000100000", regionInfo, EnumZoneServiceType.ALL, "Master Default Zone");
    	
        return zoneInfo;
    }
}