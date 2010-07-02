/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.wave;

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

			final String progId = "WEBWAVE01";

			new BapiServer(gwHost, gwServ, progId) {

				@Override
                protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDWaveRepository");
					repo.addFunction(new BapiErpsWave());
					return repo;
				}

			}.start();

			LOGGER.info("Started and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}