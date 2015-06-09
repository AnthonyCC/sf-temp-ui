package com.freshdirect.dataloader.productfamily;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapProductServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapProductServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		LOGGER.info("Post Start ---- WLSSapProductServer");
		
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapProductServer";
			final String functionName = "ZPROD_FAMILY";
			final String progId = "MAT_PROD_FAMILY ";

			new FDProductFamilyJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server["+ serverName +"] and and connected to " + gwHost + ":" + gwServ + " as " + progId);			

		}
	}

}