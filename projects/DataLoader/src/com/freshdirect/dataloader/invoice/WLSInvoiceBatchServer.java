package com.freshdirect.dataloader.invoice;

import org.apache.log4j.Category;

import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.dataloader.invoice.FDInvoiceBatchJcoServer;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 */
public class WLSInvoiceBatchServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSInvoiceBatchServer.class);

	@Override
    public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String serverName = "WLSInvoiceBatchServer";
			final String functionName = "ZSD_INVOICE_EXPORT";//"ZERPS_INVOICE";
			final String progId = "INVOICE_EXP";//"INVOICELOADER01";

			new FDInvoiceBatchJcoServer(serverName, functionName, progId).startServer();

			LOGGER.info("Started server["+ serverName +"] and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}
