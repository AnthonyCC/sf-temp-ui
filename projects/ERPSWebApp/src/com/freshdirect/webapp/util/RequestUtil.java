package com.freshdirect.webapp.util;

import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.framework.util.NVL;

public class RequestUtil {
	
	/** Append a string to a request attribute.
	 * 
	 * The resulting attribute will always be a string - ie. the previous attribute will be converted into 
	 * a string and the new value will be appended with the separator string in between. If there was no 
	 * previous attribute, the new attribute value is set.  
	 * @param request 
	 * @param key attribute key
	 * @param s new attribute value
	 * @param separator null or "" means no separator
	 */
	public static void appendToAttribute(HttpServletRequest request, String key, String s, String separator) {
		if (separator == null) separator = "";
		Object attr = request.getAttribute(key);
		if (attr == null) request.setAttribute(key,s);
		else request.setAttribute(key, new StringBuffer(attr.toString()).append(separator).append(s).toString());
	}

	public static String getFilteredQueryString(HttpServletRequest request, String[] skipFields) {
		StringBuffer sb = new StringBuffer();
		Enumeration e = request.getParameterNames();
		outer : while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			for (int i = 0; i < skipFields.length; i++) {
				if (name.equals(skipFields[i])) {
					continue outer;
				}
			}
			String[] params = request.getParameterValues(name);
			for (int i = 0; i < params.length; i++) {
				if (sb.length() > 0) {
					sb.append("&amp;");
				}
				sb.append(name).append('=').append(URLEncoder.encode(params[i]));
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * retrieves value from specifed request parameter. also removes all non-printable characters.
	 * @param HttpServletRequest request, 
	 * @param String parameterName
	 * 
	 * @return String
	 */
	public static String getRequestParameter(HttpServletRequest request, String paramName) {
		return getRequestParameter(request, paramName, false);
	}
	public static String getRequestParameter(HttpServletRequest request,String paramName,boolean removeShiftPlusChars) {
		String rtnString=NVL.apply(request.getParameter(paramName),"").trim();
		return (removeShiftPlusChars 
				? removeNonPrintableChars(removeShiftedPlusChars(rtnString)) 
				: removeNonPrintableChars(rtnString) );
	}
	
	/**
	 *  remove all non printable characters from input string
	 * 
	 * @param String inputString
	 * 
	 */
	public static String removeNonPrintableChars(String inpString) {
		Pattern pattern = Pattern.compile("[^\\p{Print}*]");
		String  outString= pattern.matcher(inpString).replaceAll("");
		return outString;
	}
	/**
	 *  remove all chars typed by using shift key,plus some other chars like ";[.."
	 * 
	 * @param String inputString
	 * 
	 */

	public static String removeShiftedPlusChars(String inpString) {
System.out.println(" in String = "+inpString);
		Pattern pattern = Pattern.compile("[<>?_`~!@#$%^&*()+=;\\{\\[\\]}|]");
		String  outString= pattern.matcher(inpString).replaceAll("");
		return outString;
	}
	
}
