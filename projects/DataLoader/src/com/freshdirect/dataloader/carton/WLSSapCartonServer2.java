package com.freshdirect.dataloader.carton;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapCartonServer2 extends ApplicationLifecycleListener {

	private static Category LOG = LoggerFactory.getInstance(WLSSapCartonServer2.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled() && ErpServicesProperties.isNewCartonExportEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
			
			final String serverName = "WLSSapCartonServer2";
			final String functionName = "ZERPS_CARTON_DETAILS_02";
			final String progId = "WEBCARTON02";

			new FDCartonDetailJcoServer2(serverName, functionName, progId).startServer();

			LOG.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}