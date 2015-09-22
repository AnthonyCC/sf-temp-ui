/******************************************************************************
$Id : HtmlUtils.java 9/8/2014 1:22:41 PM
Copyright 2014-2016 IGATE GROUP OF COMPANIES. All rights reserved
(Subject to Limited Distribution and Restricted Disclosure Only.)
THIS SOURCE FILE MAY CONTAIN INFORMATION WHICH IS THE PROPRIETARY
INFORMATION OF IGATE GROUP OF COMPANIES AND IS INTENDED FOR USE
ONLY BY THE ENTITY WHO IS ENTITLED TO AND MAY CONTAIN
INFORMATION THAT IS PRIVILEGED, CONFIDENTIAL, OR EXEMPT FROM
DISCLOSURE UNDER APPLICABLE LAW.
YOUR ACCESS TO THIS SOURCE FILE IS GOVERNED BY THE TERMS AND
CONDITIONS OF AN AGREEMENT BETWEEN YOU AND IGATE GROUP OF COMPANIES.
The USE, DISCLOSURE REPRODUCTION OR TRANSFER OF THIS PROGRAM IS
RESTRICTED AS SET FORTH THEREIN.
 ******************************************************************************/

package cbf.utils;

import java.util.Map;
import java.util.Set;

/**
 * 
 * Handles all the HTML utilities E.g. Converting object to HTML, Map to
 * table,etc
 * 
 */
public class HtmlUtils {
	String tblClass;

	private static HtmlUtils htmlUtils;

	/**
	 * Returns instance of HtmlUtils class
	 * 
	 * @return object of HtmlUtils
	 */
	public static synchronized HtmlUtils getInstance() {
		if (htmlUtils == null) {
			htmlUtils = new HtmlUtils();
		}
		return htmlUtils;
	}

	private HtmlUtils() {
		tblClass = "data";
	}

	/**
	 * Converts passed Object to Html and returns its name
	 * 
	 * @param o
	 *            object to be converted
	 * @return object converted html string
	 */
	public String toHtml(Object o) {
		try {
			return any2Html(o);
		} catch (Exception e) {
			return "#Error:" + e.getMessage().toString();
		}
	}

	private String any2Html(Object object) {
		if (object == null) {
			return "";
		}
		String dataType = object.getClass().getName();
		String result = null;
		if (dataType == null) {
			result = "#Nothing#";
		} else if (dataType == "java.util.HashMap"
			|| dataType == "cbf.utils.DataRow") {
			result = map2Table((Map<Object, Object>) object, tblClass);
		} else if (dataType == "java.lang.Object") {
			result = object.toString();
		} else {
			result = StringUtils.toString(object);
		}
		return result;
	}

	/**
	 * Converts passed map to table and returns it in string format
	 * 
	 * @param object
	 *            map to be converted to table
	 * @param className
	 *            table class
	 * @return converted string
	 */
	public String map2Table(Map<Object, Object> object, String className) {
		String s = "<table class='" + className + "'><tr>";
		Set<Object> keys = object.keySet();
		for (Object key : keys) {
			s = s + "<th>" + key + "</th>";
		}
		s = s + "</tr>";

		// s = s + "/r/n" + "<tr>";
		s = s + "<tr>";
		for (Object key : keys) {
			s = s + "<td>" + toHtml(object.get(key)) + "</td>";
		}
		s = s + "</tr></table>";

		return s;
	}
}
