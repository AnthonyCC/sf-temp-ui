package com.freshdirect.transadmin.util;

import com.freshdirect.framework.util.ExpiringReference;

public abstract class CustomExpiringReference extends ExpiringReference {
	
	public CustomExpiringReference(long refreshPeriod) {
		super(refreshPeriod);
	}
	
	public synchronized Object getEx() {
		return this.referent;		
	}
}
