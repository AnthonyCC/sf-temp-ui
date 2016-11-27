package com.freshdirect.webapp.util;

import java.util.Iterator;
import java.util.List;


public class JSHelper {
	/**
	 * Converts a list of strings to a javascript array
	 * 
	 * @param List<String> aList
	 * 
	 * @return "['a', 'b', 'c', ...]"
	 */
	public static String listToJSArray(List<?> aList) {
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		for (Iterator<?> it=aList.iterator(); it.hasNext();) {
			String aString = it.next().toString().replaceAll("'","\\'");
			buf.append("'"+aString+"'");
			if (it.hasNext())
				buf.append(", ");
		}
		
		buf.append("]");

		return buf.toString();
	}
}
