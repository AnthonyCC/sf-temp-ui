package com.freshdirect.framework.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Entry point for the Hivemind registry, initialized with a list of configurations.
 * 
 * <p>By default, configurations are loaded from <code>classpath:/freshdirect.registry</code>.
 * 
 * This location can be overriden with the JVM property <code>-Dcom.freshdirect.registry=...</code>
 * </p>
 * 
 * The file provides a list of hivemodule descriptors
 * that the registry needs to initialize with. Empty lines and lines starting with # are ignored.
 * 
 * Example:
 * <pre>
 * # This is a comment
 * classpath:/com/freshdirect/rules/hivemodule.xml
 * classpath:/com/freshdirect/fdstore/hivemodule.xml
 * classpath:/com/freshdirect/fdstore/dlv/hivemodule.xml
 * classpath:/com/freshdirect/fdstore/promotions/hivemodule.xml
 * #classpath:/com/freshdirect/cms/hivemodule.xml
 * </pre>
 * 
 * @author vszathmary
 */
public class FDRegistry {

	private static Registry instance;
	private static List<String> configLocations = new ArrayList<String>();

	private final static String SYSPROP_REGISTRY = "com.freshdirect.registry";
	private final static String DEFAULT_REGISTRY = "classpath:/freshdirect.registry";
	
	private static Category LOGGER = LoggerFactory.getInstance(FDRegistry.class);

	public static Registry getInstance() {
		if (instance == null) {
			RegistryBuilder builder = new RegistryBuilder();
			builder.addDefaultModuleDescriptorProvider();

			ClassResolver resolver = new DefaultClassResolver();
			List<String> confs = getAllConfigurations();
			LOGGER.info( "Loading configurations " + confs );
			for ( String location : confs ) {
				Resource res = ResourceUtil.getResource(location);
				if (res.getResourceURL() == null) {
					LOGGER.error("Resource not found: " + location);
				}
				builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(resolver, res));
			}

			instance = builder.constructRegistry(Locale.getDefault());
		}
		return instance;
	}
	
	/**
	 *  Set the default freshdirect registry path.
	 *  
	 *  @param location the location to the default freshdirect registry,
	 *         something like classpath:/freshdirect.registry
	 */
	public static void setDefaultRegistry(String location) {
		System.setProperty(SYSPROP_REGISTRY, location);
	}

	private static List<String> getAllConfigurations() {
		List<String> l = new ArrayList<String>();

		l.addAll(configLocations);

		String registry = System.getProperty(SYSPROP_REGISTRY);
		if (registry == null) {
			registry = DEFAULT_REGISTRY;
		}
		try {
			l.addAll(loadRegistry(registry));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return l;
	}

	private static List<String> loadRegistry(String registry) throws IOException {
		Resource res = ResourceUtil.getResource(registry);
		URL url = res.getResourceURL();
		if (url == null) {
			throw new IOException("Resource not found: " + registry);
		}
		InputStream is = url.openStream();
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		String line;
		List<String> l = new ArrayList<String>();
		while ((line = r.readLine()) != null) {
			line = line.trim();
			if ("".equals(line) || line.startsWith("#")) {
				continue;
			}
			l.add(line);
		}
		return l;
	}

	public static void addConfiguration(String location) {
		configLocations.add(location);
		instance = null;
	}

}
