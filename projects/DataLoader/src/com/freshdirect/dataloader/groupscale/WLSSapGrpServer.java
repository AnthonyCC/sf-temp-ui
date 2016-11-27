package com.freshdirect.dataloader.groupscale;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapGrpServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapGrpServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapGrpScaleServer";
			final String functionName = "Z_BAPI_SGRP_PRICE_WEB";
			final String progId = "GRP_SCALE_PRICE";

			new FDGroupScalePriceJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server["+ serverName +"] and and connected to " + gwHost + ":" + gwServ + " as " + progId);			

		}
	}

}