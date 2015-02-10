package com.freshdirect.dataloader.wave;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapWaveServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapWaveServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapWaveServer";
			final String functionName = "ZERPS_WAVE_DETAILS";
			final String progId = "WEBWAVE01";

			new FDWaveDetailJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server ["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}