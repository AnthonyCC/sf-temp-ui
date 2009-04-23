package com.freshdirect.fdstore.aspects;

import java.util.List;
import java.util.Map;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public abstract class ScoreFactorGlobalFactorsAspect extends BaseAspect {

    public ScoreFactorGlobalFactorsAspect() {
         super(new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getGlobalFactors\\(java.util.List\\)"));
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
