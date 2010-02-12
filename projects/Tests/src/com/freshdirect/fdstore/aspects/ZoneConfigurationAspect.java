package com.freshdirect.fdstore.aspects;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class ZoneConfigurationAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(ZoneConfigurationAspect.class);

    Map<String,ErpZoneMasterInfo> zoneCache = new HashMap<String,ErpZoneMasterInfo>();
    
    public ZoneConfigurationAspect() {
        super(new DebugMethodPatternPointCut("FDFactorySessionBean\\.getZoneInfo\\(java.lang.String\\)"));
    }


    @Override
    public void intercept(InvocationContext ctx) throws Exception {
        String sku = (String) ctx.getParamVals()[0];
        LOG.info("getZoneInfo for "+sku);
        ctx.setReturnObject(getZoneInfo(sku));
    }

    public ZoneConfigurationAspect addZoneMasterInfo(String zoneId) {
        return addZoneMasterInfo(null, zoneId);
    }
    
    public ZoneConfigurationAspect addZoneMasterInfo(String parentZoneId, String zoneId) {
        ErpZoneMasterInfo z = new ErpZoneMasterInfo(zoneId, null, EnumZoneServiceType.ALL, "Description of "+zoneId);
        zoneCache.put(zoneId, z);
        if (parentZoneId != null) {
            z.setParentZone(zoneCache.get(parentZoneId));
        }
        return this;
    }
    
    

    protected ErpZoneMasterInfo getZoneInfo(String sku) {
        return zoneCache.get(sku);
    }

}
