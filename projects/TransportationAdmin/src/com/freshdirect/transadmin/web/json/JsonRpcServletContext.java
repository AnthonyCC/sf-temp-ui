
package com.freshdirect.transadmin.web.json;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class JsonRpcServletContext implements ServletContext
{
	private final ServletContext servletContext;
	private final JsonRpcServletConfig jsonRpcServletConfig;

	/**
	 * 
	 */
	public JsonRpcServletContext(ServletContext servletContext)
	{
		super();
		this.servletContext = servletContext;
		this.jsonRpcServletConfig = new JsonRpcServletConfig(this,
				servletContext);
	}

	public JsonRpcServletConfig getJsonRpcServletConfg()
	{
		return this.jsonRpcServletConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 */
	public ServletContext getContext(String context)
	{
		return this.servletContext.getContext(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMajorVersion()
	 */
	public int getMajorVersion()
	{
		return this.servletContext.getMajorVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public int getMinorVersion()
	{
		return this.servletContext.getMinorVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	public String getMimeType(String arg0)
	{
		return this.servletContext.getMimeType(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	public Set getResourcePaths(String arg0)
	{
		return this.servletContext.getResourcePaths(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResource(java.lang.String)
	 */
	public URL getResource(String arg0) throws MalformedURLException
	{
		return this.servletContext.getResource(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream(String arg0)
	{
		return this.servletContext.getResourceAsStream(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		return this.servletContext.getRequestDispatcher(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	public RequestDispatcher getNamedDispatcher(String arg0)
	{
		return this.servletContext.getNamedDispatcher(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	/**
	 * @deprecated
	 */
	public Servlet getServlet(String arg0) throws ServletException
	{
		return this.servletContext.getServlet(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlets()
	 */
	/**
	 * @deprecated
	 */
	public Enumeration getServlets()
	{
		return this.servletContext.getServlets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	/**
	 * @deprecated
	 */
	public Enumeration getServletNames()
	{
		return this.servletContext.getServletNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	public void log(String arg0)
	{
		this.servletContext.log(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.Exception,
	 *      java.lang.String)
	 */
	/**
	 * @deprecated
	 */
	public void log(Exception arg0, String arg1)
	{
		this.servletContext.log(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String,
	 *      java.lang.Throwable)
	 */
	public void log(String arg0, Throwable arg1)
	{
		this.servletContext.log(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0)
	{
		return this.servletContext.getRealPath(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	public String getServerInfo()
	{
		return this.servletContext.getServerInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String arg0)
	{
		return this.jsonRpcServletConfig.getInitParameter(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	public Enumeration getInitParameterNames()
	{
		return this.jsonRpcServletConfig.getInitParameterNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0)
	{
		return this.servletContext.getAttribute(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	public Enumeration getAttributeNames()
	{
		return this.servletContext.getAttributeNames();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1)
	{
		this.servletContext.setAttribute(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0)
	{
		this.servletContext.removeAttribute(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getServletContextName()
	{
		return this.servletContext.getServletContextName();
	}
}
