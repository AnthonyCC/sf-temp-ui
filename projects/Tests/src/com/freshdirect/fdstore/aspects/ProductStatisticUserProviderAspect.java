/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Map;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class ProductStatisticUserProviderAspect implements Aspect {

    public Pointcut getPointcut() {
        return new DebugMethodPatternPointCut("DyfModelSessionBean\\.getProductFrequencies\\(java.lang.String\\)");
    }

    public void intercept(InvocationContext ctx) throws Exception {
        String param = (String) ctx.getParamVals()[0];
        ctx.setReturnObject(getUserProductScores(param));
    }

    /**
     * 
     * @return Map<ContentKey,Float>
     */
    public abstract Map getUserProductScores(String userId);
    
}