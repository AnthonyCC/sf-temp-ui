package com.freshdirect.dataloader.cool;
import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapCOOLChangeServer extends ApplicationLifecycleListener {
	
	private static Category LOGGER = LoggerFactory.getInstance(WLSSapCOOLChangeServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {
		
		if (ErpServicesProperties.getJcoClientListenersEnabled())
		{
			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
	
			final String serverName = "WLSSapCOOLChangeServer";
			final String functionName = "ZSDI_COUNTRY_ORIGIN";
			final String progId = "COUNTRY_ORIGIN";

			new FDCOOLJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}
}
