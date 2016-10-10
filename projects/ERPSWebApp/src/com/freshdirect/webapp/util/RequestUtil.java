package com.freshdirect.webapp.util;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RequestUtil {

    private static final Category LOGGER = LoggerFactory.getInstance(RequestUtil.class);

    /**
     * Append a string to a request attribute.
     * 
     * The resulting attribute will always be a string - ie. the previous attribute will be converted into a string and the new value will be appended with the separator string in
     * between. If there was no previous attribute, the new attribute value is set.
     * 
     * @param request
     * @param key
     *            attribute key
     * @param s
     *            new attribute value
     * @param separator
     *            null or "" means no separator
     */
    public static void appendToAttribute(HttpServletRequest request, String key, String s, String separator) {
        if (separator == null)
            separator = "";
        Object attr = request.getAttribute(key);
        if (attr == null)
            request.setAttribute(key, s);
        else
            request.setAttribute(key, new StringBuffer(attr.toString()).append(separator).append(s).toString());
    }

    public static String getFilteredQueryString(HttpServletRequest request, String[] skipFields) {
        StringBuffer sb = new StringBuffer();
        Enumeration e = request.getParameterNames();
        outer: while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            for (int i = 0; i < skipFields.length; i++) {
                if (name.equals(skipFields[i])) {
                    continue outer;
                }
            }
            String[] params = request.getParameterValues(name);
            for (int i = 0; i < params.length; i++) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(name).append('=').append(URLEncoder.encode(params[i]));
            }
        }
        return sb.toString();
    }

    /**
     * retrieves value from specifed request parameter. also removes all non-printable characters.
     * 
     * @param HttpServletRequest
     *            request,
     * @param String
     *            parameterName
     * 
     * @return String
     */
    public static String getRequestParameter(HttpServletRequest request, String paramName) {
        return getRequestParameter(request, paramName, false);
    }

    public static String getRequestParameter(HttpServletRequest request, String paramName, boolean removeShiftPlusChars) {
        String rtnString = NVL.apply(request.getParameter(paramName), "").trim();
        return (removeShiftPlusChars ? removeNonPrintableChars(removeShiftedPlusChars(rtnString)) : removeNonPrintableChars(rtnString));
    }

    /**
     * remove all non printable characters from input string
     * 
     * @param String
     *            inputString
     * 
     */
    public static String removeNonPrintableChars(String inpString) {
        Pattern pattern = Pattern.compile("[^\\p{Print}*]");
        String outString = pattern.matcher(inpString).replaceAll("");
        return outString;
    }

    /**
     * remove all chars typed by using shift key,plus some other chars like ";[.."
     * 
     * @param String
     *            inputString
     * 
     */

    public static String removeShiftedPlusChars(String inpString) {
        // System.out.println(" in String = "+inspString);
        Pattern pattern = Pattern.compile("[<>?_`~!@#$%^&*()+=;\\{\\[\\]}|]");
        String outString = pattern.matcher(inpString).replaceAll("");
        return outString;
    }

    /**
     * extracts last entry of the X-Forwarded-For header if exists (as remoteAddr contains NetScaler/proxy IP), if there is no X-Forwarded-For header remoteAddr is used
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        String xffHeader = request.getHeader("True-Client-IP"); // Check if AKAMAI_IPHEADER is present
        if (xffHeader == null || xffHeader.trim().length() == 0) {
            xffHeader = request.getHeader("X-Forwarded-For"); // Check if Standard XForward Header is available
        }
        if (xffHeader != null) {
            ip = xffHeader.substring(xffHeader.lastIndexOf(",") + 1).trim();
            // LOGGER.debug("Resolved IP ("+ip+") from X-Forwarded-For header (" + xffHeader +")");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getServerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Utility method for assembling the full request url
     * 
     * @param request
     * @return the request url with the query parameters
     */
    public static String getFullRequestUrl(HttpServletRequest request) {
        StringBuffer urlBuilder = request.getRequestURL();
        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            urlBuilder.append("?").append(request.getQueryString());
        }
        return urlBuilder.toString();
    }
}
