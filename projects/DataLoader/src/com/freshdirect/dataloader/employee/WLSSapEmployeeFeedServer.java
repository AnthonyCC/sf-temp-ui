package com.freshdirect.dataloader.employee;
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
public class WLSSapEmployeeFeedServer extends ApplicationLifecycleListener {
	
	private static Category LOGGER = LoggerFactory.getInstance(WLSSapEmployeeFeedServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {
				
		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");
	
			final String progId = "EMPLOYEE";
	
			new BapiServer(gwHost, gwServ, progId) {
	
				protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDEmployeeFeedRepo");
					repo.addFunction(new BapiErpsEmployeeFeed());
					return repo;
				}
	
			}.start();
	
			LOGGER.info("Started and connected to " + gwHost + ":" + gwServ + " as " + progId);
		
		}
		
	}

}
