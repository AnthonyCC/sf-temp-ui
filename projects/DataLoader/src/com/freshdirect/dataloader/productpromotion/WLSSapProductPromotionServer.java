package com.freshdirect.dataloader.productpromotion;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.bapi.BapiRepository;
import com.freshdirect.dataloader.bapi.BapiServer;
import com.freshdirect.framework.util.log.LoggerFactory;

public class WLSSapProductPromotionServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSSapProductPromotionServer.class);
	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		LOGGER.info("Inside WLSSapProductPromotionServer");
		
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String progId = SAPProductPromotionContentLoader.SAP_PROGRAM_ID;

			new BapiServer(gwHost, gwServ, progId) {

				@Override
                protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDWaveRepository");
					repo.addFunction(new SAPProductPromotionContentLoader());
					return repo;
				}

			}.start();

			LOGGER.info("Started  WLSSapProductPromotionServer and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}
}
