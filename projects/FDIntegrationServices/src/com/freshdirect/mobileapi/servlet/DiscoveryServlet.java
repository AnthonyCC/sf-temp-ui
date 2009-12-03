package com.freshdirect.mobileapi.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Rob
 *
 */
public class DiscoveryServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = -7824136612297498952L;

    final static Category LOGGER = LoggerFactory.getInstance(DiscoveryServlet.class);

    private static Properties config = new Properties();

    private static Map<String, String> mapping = new HashMap<String, String>();

    private static long lastRefresh = 0;

    private final static long REFRESH_PERIOD = 5 * 60 * 1000;

    private final static String UPGRADE = "UPGRADE";

    private final static String INCOMPATIBLE = "INCOMPATIBLE";

    private final static String KEY_FORMAT = "version.action."; //will be looked up by "version.action.1" for version "1" 

    private final static String PROPERTY_FILE = "/WEB-INF/discover.properties";

    private static int classpathLoadMode = 0;

    private static final int CLASSPATH_LOAD_MODE_1 = 1;

    private static final int CLASSPATH_LOAD_MODE_2 = 2;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            refresh(true);

            //mapping load mapping here.

        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        //String servletPath = request.getServletPath();

        //Assume request is "[context-path]/discovery/<version>/" where "[context-path]/discovery/" is path to servlet
        String version = request.getPathInfo().replaceAll("/", "");
        String action = (String) config.get(KEY_FORMAT + version);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (UPGRADE.equals(action)) {
            //Send upgrade message
            out.println("{ \"debug\" : { },");
            out.println("  \"errors\" : { \"ERR_UPGRADE_APP\" : \"Please upgrade your app.\" },");
            out.println("\"status\" : \"FAILED\", \"warnings\" : { }, \"notice\" : { }");
            out.println("}");
        } else if (INCOMPATIBLE.equals(action)) {
            out.println("{ \"debug\" : { },");
            out.println("  \"errors\" : { \"ERR_API_UNSUPPORTED\" : \"Sorry, requested version of API is not supported.\" },");
            out.println("\"status\" : \"FAILED\", \"warnings\" : { }, \"notice\" : { }");
            out.println("}");
        } else if ((action == null) || (action.isEmpty())) {
            out.println("{ \"debug\" : { },");
            out.println("  \"errors\" : { \"ERR_API_UNSUPPORTED\" : \"Sorry, requested version of API is not supported.\" },");
            out.println("\"status\" : \"FAILED\", \"warnings\" : { }, \"notice\" : { }");
            out.println("}");
        } else {
            out.println("{ \"debug\" : { },");
            out.println("  \"notice\" : { \"API_BASE_URL\" : \"" + action + "\" },");
            out.println("\"status\" : \"SUCCESS\", \"warnings\" : { }, \"errors\" : { }");
            out.println("}");
        }
        out.close();
    }

    private synchronized void refresh(boolean force) throws IOException {
        long t = System.currentTimeMillis();
        if (force || (t - lastRefresh > REFRESH_PERIOD)) {
            synchronized (config) {
                //TODO: Clean up
                if (classpathLoadMode == CLASSPATH_LOAD_MODE_1) {
                    config = ConfigHelper.getPropertiesFromClassLoader(PROPERTY_FILE);
                    classpathLoadMode = CLASSPATH_LOAD_MODE_1;
                } else if (classpathLoadMode == CLASSPATH_LOAD_MODE_2) {
                    config = new Properties();
                    config.load(getServletContext().getResourceAsStream(PROPERTY_FILE));
                    classpathLoadMode = CLASSPATH_LOAD_MODE_2;
                } else {
                    try {
                        config = ConfigHelper.getPropertiesFromClassLoader(PROPERTY_FILE);
                        classpathLoadMode = CLASSPATH_LOAD_MODE_1;
                    } catch (IOException e) {
                        config = new Properties();
                        config.load(getServletContext().getResourceAsStream(PROPERTY_FILE));
                        classpathLoadMode = CLASSPATH_LOAD_MODE_2;
                    }
                }
                lastRefresh = t;
            }
            LOGGER.info("Loaded configuration from discover.properties: " + config);
        }
    }

    private String get(String key) throws IOException {
        refresh(false);
        return config.getProperty(key);
    }

}
