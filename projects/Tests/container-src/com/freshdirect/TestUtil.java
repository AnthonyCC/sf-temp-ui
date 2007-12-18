/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect;

import javax.naming.*;
import java.util.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class TestUtil {

	private final static Properties CONFIG;
	static {
		Properties defaults = new Properties();
		defaults.put("tests.contextFactory", "weblogic.jndi.WLInitialContextFactory");
		//stage: app1.stage.nyc2:7221
		//int: app1.dev.nyc1:7221
		defaults.put("tests.providerURL", "t3://localhost:80");
		CONFIG = com.freshdirect.framework.util.ConfigHelper.getPropertiesFromClassLoader("tests.properties", defaults);
	}

	public static Context getInitialContext() throws NamingException {
		Hashtable h = new Hashtable();
		h.put( Context.INITIAL_CONTEXT_FACTORY, CONFIG.getProperty("tests.contextFactory") );
		h.put( Context.PROVIDER_URL, CONFIG.getProperty("tests.providerURL") );
		//System.out.println(h.get(Context.INITIAL_CONTEXT_FACTORY));
		return new InitialContext(h);
	}

}