package com.freshdirect.dataloader.zoneinfo;

/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
*/

import com.freshdirect.ErpServicesProperties;

import com.freshdirect.dataloader.bapi.*;

import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationLifecycleEvent;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @version $Revision$
 * @author $Author$
 */
public class WLSSapZoneServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapZoneServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		System.out.println("inside WLSSapZoneServer");
		
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String progId = "ZONE_INFO";

			new BapiServer(gwHost, gwServ, progId) {

				protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDWaveRepository");
					repo.addFunction(new BapiErpsZoneInfoContentLoader());
					return repo;
				}

			}.start();

			LOGGER.info("Started  WLSSapZoneServer and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}