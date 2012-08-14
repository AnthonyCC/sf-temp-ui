package com.freshdirect.athena.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.athena.config.ISystemProperty;

public class AthenaProperties implements ISystemProperty {
	
	private static final Category LOGGER =  Logger.getLogger(AthenaProperties.class);
	
	private static Properties config;	
    private final static Properties defaults = new Properties();
    private static long lastRefresh = 0;
    private final static long REFRESH_PERIOD = 5 * 60 * 1000;
    
	static {
        defaults.put(CONFIG_REFRESH_FREQUENCY, "60000");
        defaults.put(DEFAULT_CACHE_FEQUENCY, "180000");
        refresh();
    }
	
	public static int getConfigRefreshFrequency() {
        return Integer.parseInt(get(CONFIG_REFRESH_FREQUENCY));
    }
	
	public static int getDefaultCacheFrequency() {
        return Integer.parseInt(get(CONFIG_REFRESH_FREQUENCY));
    }	

    private AthenaProperties() {
    }

    private static void refresh() {
        refresh(false);
    }

    private synchronized static void refresh(boolean force) {
        long t = System.currentTimeMillis();

        if (force || ((t - lastRefresh) > REFRESH_PERIOD)) {
            config = getPropertiesFromClassLoader("athena.properties", defaults);
            lastRefresh = t;
            LOGGER.info("Loaded configuration from athena.properties: " + config);
        }
    }

    public static String get(String key) {
        refresh();

        return config.getProperty(key);
    }

    /**
     *  A method to set a specific property.
     *  Use with care - it's here for testing purposes only.
     *
     *  @param key the name of the property to set.
     *  @param value the new value of the property.
     */
    public static void set(String key, String value) {
        refresh();
        config.setProperty(key, value);
    }
    
    /**
     * Loads a .properties file from the
     * The resource path is a "/"-separated path name that identifies the resource
     * somewhere within the current classpath
     *
     * @param resourcePath a "/" delimited path to a .properties file in the classpath
     * @throws IOException thrown if there are problems reading the .properties file
     * @return a Properties object containg properties loaded from the specified .properties files
     */
    public static Properties getPropertiesFromClassLoader(String resourcePath) throws IOException {
        Properties props = new Properties();
        InputStream stream = null;
        try {
            stream = ClassLoader.getSystemResourceAsStream(resourcePath);
            if (stream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            props.load(stream);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    //Trying to close. IOException is okay here
                }
            }
        }
        return props;
    }

    /**
     * Loads a .properties file from the classpath
     * The resource path is a "/"-separated path name that identifies the resource
     * somewhere within the current classpath
     *
     * @param resourcePath a "/" delimited path to a .properties file in the classpath
     * @param defaultProperties a set of properties to use if the .properties file in the resourcePath can't be located
     * 
     * @return a Properties object containg properties loaded from the specified .properties files
     */
    public static Properties getPropertiesFromClassLoader(String resourcePath, Properties defaultProperties) {
        Properties props = new Properties(defaultProperties);
        InputStream stream = null;
        try {
            stream = ClassLoader.getSystemResourceAsStream(resourcePath);
            if (stream != null) {
                props.load(stream);
            }
            return props;
        } catch (IOException ioe) {
            return props;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    //Trying to close. IOException is okay here
                }
            }
        }
    }
}
