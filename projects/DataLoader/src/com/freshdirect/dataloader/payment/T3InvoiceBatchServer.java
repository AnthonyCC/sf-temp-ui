package com.freshdirect.dataloader.payment;

import java.util.Hashtable;

import weblogic.common.T3ServicesDef;
import weblogic.common.T3StartupDef;

import com.freshdirect.dataloader.bapi.BapiRepository;
import com.freshdirect.dataloader.bapi.BapiServer;

public class T3InvoiceBatchServer implements T3StartupDef {
	
	public void setServices(T3ServicesDef services) {
	}
	
	public String startup(String name, Hashtable args) throws Exception {
		String listener = (String)args.get("listener");
		if (listener==null) throw new IllegalArgumentException("listener not specified");
		
		String gwHost = (String)args.get("gwHost");
		if (gwHost==null) throw new IllegalArgumentException("gwHost not specified");

		String gwServ = (String)args.get("gwServ");
		if (gwServ==null) throw new IllegalArgumentException("gwServ not specified");

		String progId = (String)args.get("progId");
		if (progId==null) throw new IllegalArgumentException("progId not specified");

		final InvoiceBatchListenerI listenerInstance = (InvoiceBatchListenerI) Class.forName(listener).newInstance();

		new BapiServer(gwHost, gwServ, progId) {
		
			@Override
            protected BapiRepository getRepository() {
				BapiRepository repo = new BapiRepository("FDInvoiceRepository");
				repo.addFunction( new BapiErpsInvoice(listenerInstance) );
				return repo;
			}
		
		}.start();

		return "Started with "+listener+" connected to "+gwHost+":"+gwServ+" as "+progId;
	}

}

