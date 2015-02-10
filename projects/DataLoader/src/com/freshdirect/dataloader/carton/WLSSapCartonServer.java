package com.freshdirect.dataloader.carton;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapCartonServer extends ApplicationLifecycleListener {

	private static Category LOG = LoggerFactory.getInstance(WLSSapCartonServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
			
			final String serverName = "WLSSapCartonServer";
			final String functionName = "ZERPS_CARTON_DETAILS";
			final String progId = "WEBCARTON01";

			new FDCartonDetailJcoServer(serverName, functionName, progId).startServer();

			LOG.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}