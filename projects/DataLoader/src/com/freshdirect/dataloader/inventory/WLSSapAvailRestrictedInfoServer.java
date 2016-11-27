package com.freshdirect.dataloader.inventory;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class WLSSapAvailRestrictedInfoServer extends ApplicationLifecycleListener {
	
	private static Category LOGGER = LoggerFactory.getInstance(WLSSapAvailRestrictedInfoServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {
		
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
	
			final String serverName = "WLSSapAvailRestrictedInfoServer";
			final String functionName = "ZSD_RESTRICT_ATP";
			final String progId = "WEBRESTRICTEDATP";

			new FDRestrictedAvailabilityJcoServer(serverName, functionName, progId)
			{
				
			}.startServer();

			LOGGER.info("Started "+ serverName +" and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}
}