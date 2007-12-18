/**
 * 
 */
package com.freshdirect.fdstore.customer;

import java.lang.reflect.Method;

import org.mockejb.interceptor.MethodPatternPointcut;

public class DebugMethodPatternPointCut extends MethodPatternPointcut {

	public DebugMethodPatternPointCut(String arg0) {
		super(arg0);
	}

	public boolean matchesJointpoint(Method arg0) {
		if (System.getProperty("debug.methodpatternpointcut","").equalsIgnoreCase("true")) {
			System.out.println(arg0.toString());
		}
		return super.matchesJointpoint(arg0);
	}
	
	
	   
   }