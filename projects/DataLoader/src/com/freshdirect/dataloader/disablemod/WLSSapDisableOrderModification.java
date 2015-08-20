package com.freshdirect.dataloader.disablemod;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author tbalumuri
 */
public class WLSSapDisableOrderModification extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapDisableOrderModification.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapDisableOrderModification";
			final String functionName = "FDX_ORD_PROCESSED";
			final String progId = "FDX_ORD_PRCD";

			new FDDisableOrderModificationlJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server ["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}