package com.freshdirect.dataloader.sap.jco;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapBatchServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapBatchServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
			
			final String serverName = "WLSMaterialBatchServer";
			final String functionName = "ZERPS_BATCH";
			final String progId = "MATERIAL_EXPORT";

			new FDMaterialBatchJcoServer(serverName, functionName, progId).startServer();
			
			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}