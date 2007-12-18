/**
 * 
 */
package com.freshdirect.fdstore.customer;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.InvocationContext;
import org.mockejb.interceptor.Pointcut;


public class FDCustomerManagerGetNextIdAspect implements Aspect {

	public Pointcut getPointcut() {
		return new DebugMethodPatternPointCut("FDCustomerManagerSessionBean\\.getNextId\\(java.lang.String,java.lang.String\\)");
	}

	public void intercept(InvocationContext ctx) throws Exception {
		String schema = (String) ctx.getParamVals()[0];
		String seq = (String) ctx.getParamVals()[1];
		ctx.setReturnObject(getNextId(schema, seq));
	}

	int seqVar = 0;
	public String getNextId(String schema, String sequence) {
		seqVar ++;
		return "" + seqVar;
	}
}