/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Map;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class ProductStatisticProviderAspect implements Aspect {

    public Pointcut getPointcut() {
        return new DebugMethodPatternPointCut("DyfModelSessionBean\\.getGlobalProductScores\\(\\)");
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