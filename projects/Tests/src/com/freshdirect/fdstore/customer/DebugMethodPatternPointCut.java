/**
 * 
 */
package com.freshdirect.fdstore.customer;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.mockejb.interceptor.MethodPatternPointcut;

import com.freshdirect.framework.util.log.LoggerFactory;

public class DebugMethodPatternPointCut extends MethodPatternPointcut {

    final static Logger LOG = LoggerFactory.getInstance(DebugMethodPatternPointCut.class);

    String pattern;

    public DebugMethodPatternPointCut(String arg0) {
        super(arg0);
        this.pattern = arg0;
    }

    public boolean matchesJointpoint(Method arg0) {
        if (System.getProperty("debug.methodpatternpointcut", "").equalsIgnoreCase("true")) {
            System.out.println(arg0.toString());
        }
        try {
            return super.matchesJointpoint(arg0);
        } catch (RuntimeException e) {
            LOG.error("error in " + this.getClass().getName() + ", pattern: " + pattern + " with method : "+arg0 + ", problem:" + e.getMessage(), e);
            throw e;
        }
    }

}