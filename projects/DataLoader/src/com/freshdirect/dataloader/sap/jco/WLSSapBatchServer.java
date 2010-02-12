/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.sap.jco;

import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationLifecycleEvent;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.ErpServicesProperties;

import com.freshdirect.dataloader.bapi.*;

import com.freshdirect.dataloader.sap.SAPLoadListener;

/**
 * @version $Revision$
 * @author $Author$
 */
public class WLSSapBatchServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapBatchServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String progId = "MATERIAL_EXPORT";

			new BapiServer(gwHost, gwServ, progId) {

				protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDLoaderRepository");
					repo.addFunction(new BapiErpsBatch(new SAPLoadListener()));
					return repo;
				}

			}.start();

			LOGGER.info("Started and connected to " + gwHost + ":" + gwServ + " as " + progId);
		}
	}

}