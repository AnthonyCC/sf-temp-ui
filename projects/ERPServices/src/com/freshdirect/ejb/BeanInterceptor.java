package com.freshdirect.ejb;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.freshdirect.fdstore.FDStoreProperties;

public class BeanInterceptor {
	@AroundInvoke
	public Object BeanScopeInterceptor(InvocationContext ctx) throws Exception {
		try {
			FDStoreProperties.setInEjbScrope(true);
			Object obj = ctx.proceed();
			return obj;
		} finally {
			FDStoreProperties.setInEjbScrope(false);
		}
	}
}
