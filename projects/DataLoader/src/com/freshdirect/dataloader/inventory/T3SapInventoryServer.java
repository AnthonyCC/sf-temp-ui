/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.inventory;

import java.util.Hashtable;

import com.freshdirect.dataloader.bapi.*;

import weblogic.common.T3StartupDef;
import weblogic.common.T3ServicesDef;

/**
 * Sample configuration:
 * <code>
 * gwHost=12.39.155.113
 * gwServ=3300
 * progId=JCOSERVER01
 * </code>
 *
 * @version $Revision$
 * @author $Author$
 */
public class T3SapInventoryServer implements T3StartupDef {

	public void setServices(T3ServicesDef services) {
	}
	
	public String startup(String name, Hashtable args) throws Exception {
		
		final String gwHost = (String)args.get("gwHost");
		if (gwHost==null) throw new IllegalArgumentException("gwHost not specified");

		final String gwServ = (String)args.get("gwServ");
		if (gwServ==null) throw new IllegalArgumentException("gwServ not specified");

		final String progId = (String)args.get("progId");
		if (progId==null) throw new IllegalArgumentException("progId not specified");

		/*
		String listener = (String)args.get("listener");
		if (listener==null) throw new IllegalArgumentException("listener not specified");
		final SapBatchListenerI listenerInstance = (SapBatchListenerI) Class.forName(listener).newInstance();
		*/

		new BapiServer(gwHost, gwServ, progId) {
		
			protected BapiRepository getRepository() {
				BapiRepository repo = new BapiRepository("FDInventoryRepository");
				repo.addFunction( new BapiErpsInventory() );
				return repo;
			}
		
		}.start();


		return "Started and connected to "+gwHost+":"+gwServ+" as "+progId;
	}


}