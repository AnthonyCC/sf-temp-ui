package com.freshdirect.framework.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Build a query string from parameters.
 * 
 *
 */
public class QueryStringBuilder {
	
	private Map params = new TreeMap();
	
	/**
	 * Returns whether parameter exists.
	 * @param key parameter name
	 * @return whether there is a param with key
	 */
	public boolean existsParam(String key) {
		return params.containsKey(key);
	}
	
	/**
	 * Get parameter value.
	 * 
	 * @param key parameter name
	 * @return parameter value or null if it does not exist.
	 */
	public Object getParam(String key) {
		List values = (List)params.get(key);
		if (values == null) return null;
		return values.get(0);
	}
	
	/**
	 * Get parameter values.
	 * @param key parameter name
	 * @return parameter values 
	 */
	public List getParameterValues(String key) {
		return (List)params.get(key);
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param parameter value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, Object param) {
		List values = (List)params.get(key);
		if (values == null) {
			values = new LinkedList();
			params.put(key,values);
		} 
		values.add(param);
		return this;
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param int value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, int iv) {
		return addParam(key, new Integer(iv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param boolean value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, boolean bv) {
		return addParam(key,Boolean.valueOf(bv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param char value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, char cv) {
		return addParam(key, new Character(cv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param long value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, long lv) {
		return addParam(key, new Long(lv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param float value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, float fv) {
		return addParam(key, new Float(fv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param double value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, double dv) {
		return addParam(key, new Double(dv));
	}
	
	/**
	 * Add a parameter.
	 * @param key parameter name
	 * @param param short value
	 * @return this
	 */
	public QueryStringBuilder addParam(String key, short sv) {
		return addParam(key, new Short(sv));
	}
	
	/**
	 * Remove a parameter.
	 * @param key parameter name
	 */
	public void removeParam(String key) {
		params.remove(key);
	}
	
	/**
	 * Remove all parameters.
	 *
	 */
	public void clear() {
		params.clear();
	}
	
	/**
	 * Number of parameters.
	 * @return number of parameters.
	 */
	public int size() { return params.size(); }
	
	/**
	 * Serialize parameters.
	 * 
	 * Serialize the query parameters, as defined in RFC 3986 (2396).
	 * The order of the parameters is arbitrary.
	 * 
	 * @return URI query string
	 * @see StringUtil#escapeUri(String)
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for(Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry)i.next();
			List values = (List)entry.getValue();
			for(Iterator v = values.iterator(); v.hasNext(); ) {
				Object value = v.next();
				if (value == null) continue; // nulls will not make it
				if (buffer.length() > 0) buffer.append('&');
				buffer.
					append(StringUtil.escapeUri((String)entry.getKey())).
					append('=').
					append(StringUtil.escapeUri(value.toString()));
			}
		}
		return buffer.toString();
	}
	
	

}
