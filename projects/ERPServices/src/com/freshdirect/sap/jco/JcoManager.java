package com.freshdirect.sap.jco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.ConfigHelper;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;

/**
 * This class represents a manager to register / initialize the function modules
 * in the ERP system
 * 
 * @author kkanuganti
 * 
 */
public class JcoManager {

	private static final Logger LOG = Logger.getLogger(JcoManager.class.getName());

	private final static String DESTINATION_NAME = "FDSapPool";

	private static Properties config;

	private final static int DUMP_OFF = 0;
	private final static int DUMP_CRITICAL = 1;
	private final static int DUMP_ALWAYS = 2;

	private static JcoManager instance = null;

	/**
	 * Method to create an instance of JCoManager if NOT created already
	 * 
	 * @return JcoManager
	 * @throws JCoException
	 */
	public synchronized static JcoManager getInstance() throws JCoException {
		if (instance == null) {
			instance = new JcoManager();
		}
		return instance;
	}

	private JCoRepository repository;

	private int dump;

	/**
	 * @throws JCoException
	 */
	private JcoManager() throws JCoException {
		this.initializeDestination();
	}

	/**
	 * Method to initialize / register the destination to connect to ERP system
	 * 
	 * @throws JCoException
	 */
	public void initializeDestination() throws JCoException {
		final CustomDestinationDataProvider myProvider = new CustomDestinationDataProvider();

		// Register the provider with the JCo environment
		try {
			if (!Environment.isDestinationDataProviderRegistered()) {
				LOG.info("Registering SAP JCO provider...");

				com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
			}
		} catch (final IllegalStateException providerAlreadyRegisteredException) {
			throw new Error(providerAlreadyRegisteredException);
		}

		LOG.info("JCoDestination: setting properties");

		myProvider.changeProperties(DESTINATION_NAME, getDestinationProperties());

		LOG.info("JCoDestination: done setting properties");

		if (JCoDestinationManager.getDestination(DESTINATION_NAME) != null) {
			this.repository = JCoDestinationManager.getDestination(DESTINATION_NAME).getRepository();
		}
	}

	/**
	 * @return connection properties
	 */
	private Properties getDestinationProperties() {
		// parameters to configure a valid destination
		final Properties jcoClientProperties = new Properties();
		jcoClientProperties.put("sap.jco.dump", "critical");

		config = ConfigHelper.getPropertiesFromClassLoader("erpservices.properties", jcoClientProperties);

		LOG.info("Loaded configuration from erpservices.properties: " + config);

		// set the dump parameter
		final String dumpParam = config.getProperty("sap.jco.dump");
		if ("true".equalsIgnoreCase(dumpParam)) {
			this.dump = DUMP_ALWAYS;
		} else if ("critical".equalsIgnoreCase(dumpParam)) {
			this.dump = DUMP_CRITICAL;
		} else {
			this.dump = DUMP_OFF;
		}

		return config;
	}

	/**
	 * @return JCoDestination
	 * @throws JCoException
	 */
	public JCoDestination getDestination() throws JCoException {
		final JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME);

		if (LOG.isDebugEnabled()) {
			LOG.debug("*************** Destination ********************");
			LOG.debug("Pinging destination...");
			destination.ping();
			LOG.debug("Destination Name      : " + destination.getDestinationName());
			LOG.debug("Client ID             : " + destination.getClient());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug("User ID  						: " + destination.getUser());
			LOG.debug("----------------------------------------------------------------");
			LOG.debug(destination.getAttributes());
			LOG.debug("***************  END     ********************");
		}
		return destination;
	}

	/**
	 * @param functionName
	 * @return JCoFunctionTemplate
	 * @throws JCoException
	 */
	public JCoFunctionTemplate getFunctionTemplate(String functionName)
			throws JCoException {
		return this.repository.getFunctionTemplate(functionName);
	}

	/**
	 * Creates a jcoClient file that the DestinationManager can use to connect
	 * to SAP
	 * 
	 * @param poolName
	 * @param suffix
	 * @param properties
	 */
	protected static void createDestinationDataFile(final String poolName, final Properties properties) {
		final File cfg = new File(poolName + ".jcoDestination");

		if (!cfg.exists()) {
			try {
				final FileOutputStream fos = new FileOutputStream(cfg, false);

				properties.store(fos, "Jco Destination config file!");
				fos.close();
			} catch (final Exception e) {
				throw new RuntimeException("Unable to create the destination file "	+ cfg.getName(), e);
			}
		}
		LOG.info("Creating destination file " + cfg.getAbsolutePath());
	}

	/**
	 * Method to write request / response to XML file
	 * 
	 * @param function
	 * @param fileName
	 */
	public void dumpToXML(JCoFunction function, String fileName) {
		BufferedWriter writer = null;
		try {
			
			switch (dump) {

			case DUMP_OFF:
				return;

			case DUMP_CRITICAL:
				String fname = function.getName();
				if ("BAPI_MATERIAL_AVAILABILITY".equals(fname) || "BAPI_SALESORDER_SIMULATE".equals(fname)) {
					// don't log ATP checks
					return;
				}
			default:
				LOG.debug("Dumping BAPI to " + fileName);
				writer = new BufferedWriter(new FileWriter(fileName));
				writer.write(function.toXML());
			}
		} catch (final IOException ioe) {
			LOG.error("", ioe);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	static class CustomDestinationDataProvider implements DestinationDataProvider {
		private DestinationDataEventListener eL;
		private final HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();

		public Properties getDestinationProperties(final String destinationName) {
			try {
				// read the destination from DB
				final Properties p = secureDBStorage.get(destinationName);

				if (p != null) {
					if (p.isEmpty()) {
						throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
					}

					return p;
				}

				return null;
			} catch (final RuntimeException re) {
				throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
			}
		}

		/*
		 * An implementation supporting events, to retain the eventListener
		 * instance provided by the JCo runtime. This listener instance shall be
		 * used to notify the JCo runtime about all changes in destination
		 * configurations.
		 */
		public void setDestinationDataEventListener(final DestinationDataEventListener eventListener) {
			this.eL = eventListener;
		}

		public boolean supportsEvents() {
			return true;
		}

		// implementation that saves the properties in a very secure way
		void changeProperties(final String destName, final Properties properties) {
			synchronized (secureDBStorage) {

				if (!Environment.isDestinationDataProviderRegistered()) {
					Environment.registerDestinationDataProvider(this);
				}
				if (properties == null) {
					if (secureDBStorage.remove(destName) != null && eL != null) {
						eL.deleted(destName);
					}
				} else {
					secureDBStorage.put(destName, properties);
					if (eL != null) {
						eL.updated(destName);
					}
				}
			}
		}
	}

}