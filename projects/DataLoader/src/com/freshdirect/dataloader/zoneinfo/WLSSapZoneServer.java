package com.freshdirect.dataloader.zoneinfo;

/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
*/

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.bapi.BapiRepository;
import com.freshdirect.dataloader.bapi.BapiServer;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @version $Revision$
 * @author $Author$
 */
public class WLSSapZoneServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapZoneServer.class);

	@Override
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

				@Override
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