/**
 * 
 */
package com.freshdirect.fdstore.aspects;

import java.util.Set;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;

/**
 * @author zsombor
 * 
 */
public class ScoreFactorGlobalNameAspect extends BaseAspect {

    Set globalFactorNames;

    public ScoreFactorGlobalNameAspect(Set factorNames) {
        super(new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getGlobalFactorNames\\(\\)"));
        this.globalFactorNames = factorNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mockejb.interceptor.Interceptor#intercept(org.mockejb.interceptor
     * .InvocationContext)
     */
    public void intercept(InvocationContext ctx) throws Exception {
        ctx.setReturnObject(globalFactorNames);
    }

}
