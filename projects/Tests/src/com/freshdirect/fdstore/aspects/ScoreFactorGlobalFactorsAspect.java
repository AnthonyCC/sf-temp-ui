package com.freshdirect.fdstore.aspects;

import java.util.List;
import java.util.Map;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class ScoreFactorGlobalFactorsAspect implements Aspect {

    public Pointcut getPointcut() {
        return new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getGlobalFactors\\(java.util.List\\)");
    }

    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(getGlobalFactors((List) ctx.getParamVals()[0]));
    }
    
    /**
     * 
     * @param names
     * @return Map<ProductId:{@link String},double[]>
     */
    public abstract Map getGlobalFactors(List names);

}
