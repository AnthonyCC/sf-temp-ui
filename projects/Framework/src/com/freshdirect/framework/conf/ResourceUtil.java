/*
 * Created on Apr 6, 2005
 */
package com.freshdirect.framework.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author vszathmary
 */
public class ResourceUtil {

	private final static String PREFIX_CLASSPATH = "classpath:";
    private final static String PREFIX_CLASSPATH_SLASH = PREFIX_CLASSPATH + "/";

    private ResourceUtil() {
    }

    /**
     * Read a resource, and return its contents as a String.
     * 
     * @param location
     *            the URL of the resource.
     * @return the contents of the resource, as a string.
     * @throws IOException
     *             in case of I/O errors.
     */
    public static String readResource(String location) throws IOException {
        InputStream is = openResource(location);
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));
        StringBuffer strbuf = new StringBuffer();
        String line;

        while ((line = reader.readLine()) != null) {
            strbuf.append(line);
            strbuf.append('\n');
        }

        return strbuf.toString();
    }

    public static URL getResource(String location) {
        URL resource = null;
        if (location.startsWith(PREFIX_CLASSPATH)) {
            final String cpLocation = extractResourceLocation(location);

            // WebLogic supports this way of loading resources from EAR
            resource = Thread.currentThread().getContextClassLoader().getResource(cpLocation);

            // fall back to basic class loader in order to support exploded classpath config
            if (resource == null) {
                resource = ClassLoader.class.getResource(cpLocation);
            }

        } else {
            try {
                resource = new URL(location);
            } catch (MalformedURLException e) {
                resource = null;
            }
        }
        return resource;
    }

    public static InputStream openResource(String location) throws IOException {
        InputStream stream = null;

        if (location.startsWith(PREFIX_CLASSPATH)) {
            final String cpLocation = extractResourceLocation(location);

            // WebLogic supports this way of loading resources from EAR
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(cpLocation);

            // fall back to basic class loader in order to support exploded classpath config
            if (stream == null) {
                stream = ClassLoader.class.getResourceAsStream(cpLocation);
            }

        } else if (location.indexOf(":") > -1) {
            URL url = new URL(location);
            stream = url.openStream();
        } else {
            stream = new FileInputStream(location);
        }

        return stream;
    }

    /**
     * Create internal resource location from classpath location by removing classpath prefix and the leading slash separator
     *
     * @param location
     *            resource location prefixed with "classpath:"
     * @return
     */
    private static String extractResourceLocation(String location) {
        String classPathLocation = null;
        if (location.startsWith(PREFIX_CLASSPATH_SLASH)) {
            // leading slash has to be removed
            classPathLocation = location.substring(PREFIX_CLASSPATH_SLASH.length());
        } else {
            classPathLocation = location.substring(PREFIX_CLASSPATH.length());
        }
        return classPathLocation;
    }
}
