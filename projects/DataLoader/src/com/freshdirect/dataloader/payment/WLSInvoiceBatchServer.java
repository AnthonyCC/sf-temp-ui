package com.freshdirect.dataloader.payment;

import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationLifecycleEvent;

import com.freshdirect.dataloader.bapi.BapiRepository;
import com.freshdirect.dataloader.bapi.BapiServer;

import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.ErpServicesProperties;

import com.freshdirect.dataloader.payment.InvoiceLoadListener;

public class WLSInvoiceBatchServer extends ApplicationLifecycleListener {

	private static Category LOGGER = LoggerFactory.getInstance(WLSInvoiceBatchServer.class);

	public void postStart(ApplicationLifecycleEvent evt) {

		if (ErpServicesProperties.getJcoClientListenersEnabled()) {

			final String gwHost = ErpServicesProperties.getJcoClientListenHost();
			if (gwHost == null)
				throw new IllegalArgumentException("gwHost not specified");

			final String gwServ = ErpServicesProperties.getJcoClientListenServer();
			if (gwServ == null)
				throw new IllegalArgumentException("gwServ not specified");

			final String progId = "INVOICELOADER01";

			new BapiServer(gwHost, gwServ, progId) {

				protected BapiRepository getRepository() {
					BapiRepository repo = new BapiRepository("FDInvoiceRepository");
					repo.addFunction(new BapiErpsInvoice(new InvoiceLoadListener()));
					return repo;
				}

			}.start();

			LOGGER.info("Started and connected to " + gwHost + ":" + gwServ + " as " + progId);

		}
	}

}
