package com.freshdirect.dataloader.zonepricing;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.zonepricing.FDPricingZoneJcoServer;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapZoneServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapZoneServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapZoneServer";
			final String functionName = "Z_BAPI_ZONE_INFO";
			final String progId = "ZONE_INFO";

			new FDPricingZoneJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);			
		}
	}

}