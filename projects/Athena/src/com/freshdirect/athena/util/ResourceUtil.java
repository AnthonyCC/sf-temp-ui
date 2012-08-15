package com.freshdirect.athena.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceUtil {
	
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
