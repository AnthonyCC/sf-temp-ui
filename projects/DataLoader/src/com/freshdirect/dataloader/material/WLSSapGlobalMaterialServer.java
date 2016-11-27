package com.freshdirect.dataloader.material;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapGlobalMaterialServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapGlobalMaterialServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		LOGGER.info("*** Inside WLSSapGlobalMaterialServer ***");
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
			
			final String serverName = "WLSMaterialGlobalServer";
			final String functionName = "ZMM_MATERIAL_EXPORT_GLOBAL";
			final String progId = "MAT_GBL_EXP";

			new FDProductBaseJcoServer(serverName, functionName, progId).startServer();
			
			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}