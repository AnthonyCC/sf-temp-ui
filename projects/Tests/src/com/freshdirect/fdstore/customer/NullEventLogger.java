/**
 * 
 */
package com.freshdirect.fdstore.customer;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;

public class NullEventLogger implements Aspect {

	public Pointcut getPointcut() {
		return new DebugMethodPatternPointCut("EventLoggerSessionBean\\.log");
	}

	public void intercept(InvocationContext ctx) throws Exception {
		// Simply intercept the call and do nothing
	}
	   
   }