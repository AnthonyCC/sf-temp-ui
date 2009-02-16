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
public class ScoreFactorGlobalNameAspect implements Aspect {

    Set globalFactorNames;

    public ScoreFactorGlobalNameAspect(Set factorNames) {
        this.globalFactorNames = factorNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mockejb.interceptor.Aspect#getPointcut()
     */
    public Pointcut getPointcut() {
        return new DebugMethodPatternPointCut("ScoreFactorSessionBean\\.getGlobalFactorNames\\(\\)");
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
