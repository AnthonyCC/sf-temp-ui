package com.freshdirect.cms.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class XmlResourceUtil {

    private final static Logger LOGGER = LoggerFactory.getInstance(XmlResourceUtil.class);

    private XmlResourceUtil() {
    }

    public static InputStream openXmlResource(String location) throws IOException {
        InputStream stream = null;

        LOGGER.debug("Will open resource at path: " + location);

        if (location.startsWith("classpath:")) {
            final String cpLocation = extractResourceLocation(location);

            // WebLogic supports this way of loading resources from EAR
            LOGGER.debug("Opening resource '"+ cpLocation +"' with context class loader ...");
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(cpLocation);

            // fall back to basic class loader in order to support exploded classpath config
            if (stream == null) {
                LOGGER.debug("Opening resource '"+ cpLocation +"' with basic class loader ...");
                stream = ClassLoader.class.getResourceAsStream(cpLocation);
            }

        } else if (location.indexOf(":") > -1) {
            LOGGER.debug("Opening resource as URL stream ...");
            URL url = new URL(location);
            stream = url.openStream();
        } else {
            LOGGER.debug("Opening resource as File stream ...");
            stream = new FileInputStream(location);
        }

        return stream;
    }

    /**
     * Create internal resource location from classpath location
     * by removing classpath prefix and the leading slash separator
     *
     * @param location resource location prefixed with "classpath:"
     * @return
     */
    private static String extractResourceLocation(String location) {
        String classPathLocation = null;
        if (location.startsWith("classpath:/")) {
            // leading slash has to be removed
            classPathLocation = location.substring("classpath:/".length());
        } else {
            classPathLocation = location.substring("classpath:".length());
        }
        return classPathLocation;
    }
}
