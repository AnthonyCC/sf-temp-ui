package com.freshdirect.dataloader.material;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapMaterialPriceServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapMaterialPriceServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
			
			final String serverName = "WLSMaterialPriceServer";
			final String functionName = "ZSDI_PRICE_EXPORT";
			final String progId = "ZONE_PRICE_EXP";

			new FDProductPriceJcoServer(serverName, functionName, progId).startServer();
			
			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}