package com.freshdirect.mobileapi.servlet;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.util.MobileApiProperties;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        //String servletPath = request.getServletPath();

        //Assume request is "[context-path]/discovery/<version>/" where "[context-path]/discovery/" is path to servlet
        String version = request.getPathInfo().replaceAll("/", "");
        String action = MobileApiProperties.getApiVersionAction(version);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (MobileApiProperties.UPGRADE.equals(action)) {
            //Send upgrade message
            out.println("{ \"debug\" : { },");
            out.println("  \"errors\" : { \"ERR_UPGRADE_APP\" : \"Please upgrade your app.\" },");
            out.println("\"status\" : \"FAILED\", \"warnings\" : { }, \"notice\" : { }");
            out.println("}");
        } else if (MobileApiProperties.INCOMPATIBLE.equals(action)) {
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

}
