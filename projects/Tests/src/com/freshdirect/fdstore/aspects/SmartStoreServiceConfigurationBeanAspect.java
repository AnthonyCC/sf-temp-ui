/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.fdstore.util.EnumSiteFeature;

/**
 * @author zsombor
 *
 */
public abstract class SmartStoreServiceConfigurationBeanAspect extends BaseAspect {

    final static Logger LOG = Logger.getLogger(SmartStoreServiceConfigurationBeanAspect.class);
    
    public SmartStoreServiceConfigurationBeanAspect() {
        super(new DebugMethodPatternPointCut("SmartStoreServiceConfigurationSB\\.getVariants\\(com.freshdirect.fdstore.util.EnumSiteFeature\\)"));
    }

    /* (non-Javadoc)
     * @see org.mockejb.interceptor.Interceptor#intercept(org.mockejb.interceptor.InvocationContext)
     */
    public void intercept(InvocationContext ctx) throws Exception {
        EnumSiteFeature siteFeature = (EnumSiteFeature) ctx.getParamVals()[0];
        LOG.info("getVariants for "+siteFeature);
        ctx.setReturnObject(getVariants(siteFeature));
    }
    
    public abstract Collection getVariants(EnumSiteFeature feature);
    

}
