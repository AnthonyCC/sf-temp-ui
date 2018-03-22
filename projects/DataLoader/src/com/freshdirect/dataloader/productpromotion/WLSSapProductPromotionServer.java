package com.freshdirect.dataloader.productpromotion;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author kkanuganti
 */
public class WLSSapProductPromotionServer extends ApplicationLifecycleListener {

	private static Category LOG = LoggerFactory.getInstance(WLSSapProductPromotionServer.class);
	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		LOG.info("WLSSapProductPromotionServer");
		
		if (ErpServicesProperties.getJcoClientListenersEnabled() && FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PROP_JCO_CLIENT_LISTENER_PRODUCT_PROMOTION_ENABLED)) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSSapProductPromotionServer";
			final String functionName = "ZDDPP_PUBLISH";
			final String progId = "ZDDPP_PUB";

			new FDProductPromotionJcoServer(serverName, functionName, progId).startServer();

			LOG.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);
			
		}
	}
}
