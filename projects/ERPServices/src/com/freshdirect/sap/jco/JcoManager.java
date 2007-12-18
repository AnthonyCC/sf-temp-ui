/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Function;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class JcoManager {

	private static Category LOGGER = LoggerFactory.getInstance(JcoManager.class);

	private final static String POOL_NAME = "FDPool";
	private final static Properties config;
	static {
		Properties defaults = new Properties();
		defaults.put("sap.jco.poolsize", "20");
		defaults.put("sap.jco.dump", "critical");

		/*
		jco.client.client
		jco.client.user
		jco.client.passwd
		jco.client.lang
		jco.client.sysnr
		jco.client.ashost
		jco.client.mshost
		jco.client.gwhost
		jco.client.gwserv
		jco.client.group
		jco.client.trace
		*/

		config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", defaults);
		LOGGER.info("Loaded configuration from erpservices.properties: " + config);
	}

	private final static int DUMP_OFF = 0;
	private final static int DUMP_CRITICAL = 1;
	private final static int DUMP_ALWAYS = 2;

	private static JcoManager instance = null;

	public synchronized static JcoManager getInstance() {
		if (instance == null) {
			instance = new JcoManager();
		}
		return instance;
	}

	private final IRepository repository;
	private final int dump;

	private JcoManager() {
		JCO.addClientPool(POOL_NAME, Integer.parseInt(config.getProperty("sap.jco.poolsize")), config);

		String dumpParam = config.getProperty("sap.jco.dump");
		if ("true".equalsIgnoreCase(dumpParam)) {
			this.dump = DUMP_ALWAYS;
		} else if ("critical".equalsIgnoreCase(dumpParam)) {
			this.dump = DUMP_CRITICAL;
		} else {
			this.dump = DUMP_OFF;
		}

		this.repository = JCO.createRepository("FDRepository", POOL_NAME);
	}

	public JCO.Client getClient() {
		return JCO.getClient(POOL_NAME);
	}

	public IFunctionTemplate getFunctionTemplate(String functionName) {
		return this.repository.getFunctionTemplate(functionName);
	}

	public void dump(Function function, String fileName) {
		switch (dump) {

			case DUMP_OFF :
				return;

			case DUMP_CRITICAL :
				String fname = function.getName();
				if ("BAPI_MATERIAL_AVAILABILITY".equals(fname) || "BAPI_SALESORDER_SIMULATE".equals(fname)) {
					// don't log ATP checks
					return;
				}
				// fall thru

			default :
				LOGGER.debug("Dumping BAPI to " + fileName);
				function.writeHTML(fileName);
		}
	}

}