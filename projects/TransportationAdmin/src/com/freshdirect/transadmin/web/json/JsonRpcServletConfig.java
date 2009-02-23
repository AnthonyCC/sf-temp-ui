
package com.freshdirect.transadmin.web.json;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import com.metaparadigm.jsonrpc.JSONRPCServlet;


public class JsonRpcServletConfig implements ServletConfig
{
	private final JsonRpcServletContext jsonRpcServletContext;
	private final String servletName;
	private final Map initParameters;

	/**
	 * 
	 */
	public JsonRpcServletConfig(JsonRpcServletContext jsonRpcServletContext,
			ServletContext servletContext)
	{
		super();
		this.jsonRpcServletContext = jsonRpcServletContext;
		this.servletName = JSONRPCServlet.class.getName();
		this.initParameters = new HashMap();
		Enumeration e = servletContext.getInitParameterNames();
		while (e.hasMoreElements())
		{
			String key = (String) e.nextElement();
			String val = servletContext.getInitParameter(key);
			this.initParameters.put(key, val);
		}
		// Disabling auto session bridge creation, since we are not using it
		this.initParameters.put("auto-session-bridge", "0");
		// Forcing keep alive
		this.initParameters.put("keepalive", "1");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletConfig#getServletName()
	 */
	public String getServletName()
	{
		return new String(servletName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletConfig#getServletContext()
	 */
	public ServletContext getServletContext()
	{
		return jsonRpcServletContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletConfig#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String parameterName)
	{
		return (String) initParameters.get(parameterName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletConfig#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames()
	{
		return (Enumeration) initParameters.keySet();
	}
}