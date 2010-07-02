package com.freshdirect.dataloader.cool;
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
public class WLSSapCOOLChangeServer extends ApplicationLifecycleListener {
	
	private static Category LOGGER = LoggerFactory.getInstance(WLSSapCOOLChangeServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {
		
		//if (ErpServicesProperties.getJcoClientCOOLListenersEnabled()) {
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
	
			final String progId = "COUNTRY_ORIGIN";
	
			new BapiServer(gwHost, gwServ, progId) {
	
				@Override
                protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDCOOLChangeRepo");
					repo.addFunction(new BapiErpsCOOLChange());
					return repo;
				}
	
			}.start();
	
			LOGGER.info("Started and connected to " + gwHost + ":" + gwServ + " as " + progId);
		
		}
		
	}

}
