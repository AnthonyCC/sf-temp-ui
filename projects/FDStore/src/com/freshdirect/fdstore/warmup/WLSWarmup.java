/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2004 FreshDirect
 *
 */

package com.freshdirect.fdstore.warmup;

import com.freshdirect.fdstore.FDStoreProperties;

import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.ApplicationLifecycleEvent;

/**
 * WLSWarmup
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public class WLSWarmup extends ApplicationLifecycleListener {

	public void postStart(ApplicationLifecycleEvent evt) {

		if (FDStoreProperties.performStorePreLoad()) {
			Class warmupClass = Warmup.class;
			String className = FDStoreProperties.getWarmupClass();
			if (className != null) {
				try {
					warmupClass = Class.forName(className);
				} catch (Exception e) {
					System.err.println("Could not find Warmup class "+ className +" fallback to default.");
					e.printStackTrace();
					
				}
			}
			try {
				Warmup warmup = (Warmup) warmupClass.newInstance();
				warmup.warmup();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}
