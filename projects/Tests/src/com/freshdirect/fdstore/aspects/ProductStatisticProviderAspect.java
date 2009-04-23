/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Map;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class ProductStatisticProviderAspect extends BaseAspect {

    public ProductStatisticProviderAspect() {
        super(new DebugMethodPatternPointCut("DyfModelSessionBean\\.getGlobalProductScores\\(\\)"));
    }

    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(getGlobalProductScores());
    }

    /**
     * 
     * @return Map<ContentKey,Float>
     */
    public abstract Map getGlobalProductScores();

}