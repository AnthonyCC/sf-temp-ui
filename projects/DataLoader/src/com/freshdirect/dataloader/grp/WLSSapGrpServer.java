package com.freshdirect.dataloader.grp;

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
public class WLSSapGrpServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapGrpServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		System.out.println("inside WLSSapZoneServer:"+ErpServicesProperties.getJcoClientListenersEnabled());
		
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String progId = "GRP_SCALE_PRICE";

			new BapiServer(gwHost, gwServ, progId) {

				protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDWaveRepository");
					repo.addFunction(new SAPGrpInfoContentLoader());
					return repo;
				}

			}.start();

			LOGGER.info("Started  WLSSapGrpServer and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}