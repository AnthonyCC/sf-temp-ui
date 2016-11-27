package com.freshdirect.transadmin.datamanager;

import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;

public interface IServiceProvider {
	
	DomainManagerI getDomainManagerService();
	
	DispatchManagerI getDispatchManagerService();
}
