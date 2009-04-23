package com.freshdirect.fdstore.aspects;

import java.util.Set;

import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

public class ScoreFactorPersonalNameAspect extends BaseAspect {

    Set personalFactorNames;

    public ScoreFactorPersonalNameAspect(Set factorNames) {
        super(new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getPersonalizedFactorNames\\(\\)"));
        this.personalFactorNames = factorNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mockejb.interceptor.Interceptor#intercept(org.mockejb.interceptor
     * .InvocationContext)
     */
    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(personalFactorNames);
    }

}
