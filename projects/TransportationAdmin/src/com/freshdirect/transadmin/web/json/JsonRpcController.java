package com.freshdirect.transadmin.web.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.metaparadigm.jsonrpc.JSONRPCServlet;


public abstract class JsonRpcController extends AbstractController implements
		DisposableBean, InitializingBean, BeanNameAware
{
	private Log logger = LogFactory.getLog(JsonRpcController.class);
	private ThreadLocal httpRequest = new ThreadLocal();
	private ThreadLocal httpResponse = new ThreadLocal();
	private String jsonRpcControllerName;
	private Servlet jsonRpcServlet;
	private String ajaxServiceName;
	private Class ajaxServiceInterface;
	private Map ajaxServices;
	private Map ajaxServicesList;
	private boolean ajaxServicesRegistered;

	
	public JsonRpcController()
	{
		super();
	}

	
	protected final ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		
		httpRequest.set(req);
		httpResponse.set(res);
		if (!ajaxServicesRegistered)
		{
			registerAjaxServices(req);
		}
		jsonRpcServlet.service(req, res);
		return null;
	}

	
	private final void registerAjaxServices(HttpServletRequest req)	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Registering: Ajax Services for ["
					+ jsonRpcControllerName + "].");
		}
		// Using global bridge
		JSONRPCBridge j = JSONRPCBridge.getGlobalBridge();
		
		/*try {
			j.registerSerializer(TransDataSerializer.getInstance());
		} catch(Exception e) {
			e.printStackTrace();
		}*/
		for (Iterator i = ajaxServicesList.keySet().iterator(); i.hasNext();)
		{
			String _serviceProviderName = (String) i.next();
			Class _serviceProviderInterface = (Class) ajaxServicesList
					.get(_serviceProviderName);
			j.registerObject(_serviceProviderName, this,
					_serviceProviderInterface);
		}
		ajaxServicesRegistered = true;
		if (logger.isDebugEnabled())
		{
			logger.debug("Registered : Ajax Services for ["
					+ jsonRpcControllerName + "].");
		}
	}

	
	public void destroy() throws Exception
	{
		try {
			jsonRpcServlet.destroy();
			jsonRpcServlet = null;
		} catch(Exception e) {
			//Unable to destroy servlet instance may be the servlet context is dead.
		}
	}

	
	public final void setBeanName(String beanName)
	{
		this.jsonRpcControllerName = beanName;
	}

	
	public final void setAjaxServiceName(String serviceProviderName)
	{
		this.ajaxServiceName = serviceProviderName;
	}

	/**
	 * 
	 * @param serviceProviderInterface
	 */
	public final void setAjaxServiceInterface(Class serviceProviderInterface)
	{
		this.ajaxServiceInterface = serviceProviderInterface;
	}

	/**
	 * 
	 * @param serviceProviders
	 */
	public final void setAjaxServices(Map serviceProviders)
	{
		this.ajaxServices = serviceProviders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public final void afterPropertiesSet() throws Exception
	{
		if (this.ajaxServiceName == null && this.ajaxServiceInterface == null
				&& this.ajaxServices == null)
		{
			IllegalArgumentException e = new IllegalArgumentException(
					"Either ajaxServiceName and ajaxServiceInterface or, ajaxServices must be set.");
			logger.fatal(e);
			throw e;
		}
		this.ajaxServicesList = new HashMap();
		if (this.ajaxServices == null)
		{
			if (this.ajaxServiceName == null
					|| this.ajaxServiceInterface == null)
			{
				IllegalArgumentException e = new IllegalArgumentException(
						"Both ajaxServiceName and ajaxServiceInterface must be set.");
				logger.fatal(e);
				throw e;
			}
			if (!this.ajaxServiceInterface.isInterface())
			{
				IllegalArgumentException e = new IllegalArgumentException("["
						+ ajaxServiceInterface.getName() + "] "
						+ "ajaxServiceInterface must be of type interface.");
				logger.fatal(e);
				throw e;
			}
			this.ajaxServicesList.put(this.ajaxServiceName,
					this.ajaxServiceInterface);
		}
		else
		{
			for (Iterator i = ajaxServices.keySet().iterator(); i.hasNext();)
			{
				String _serviceProviderName = (String) i.next();
				String _serviceProviderClassName = (String) ajaxServices
						.get(_serviceProviderName);
				Class _serviceProviderInterface = Class
						.forName(_serviceProviderClassName);
				if (!_serviceProviderInterface.isInterface())
				{
					throw new IllegalArgumentException("["
							+ _serviceProviderClassName + "] "
							+ "ajaxServiceInterface must be of type interface.");
				}
				this.ajaxServicesList.put(_serviceProviderName,
						_serviceProviderInterface);
			}
		}
		JsonRpcServletContext ctx = new JsonRpcServletContext(
				getServletContext());
		this.jsonRpcServlet = (Servlet) JSONRPCServlet.class.newInstance();
		this.jsonRpcServlet.init(ctx.getJsonRpcServletConfg());
	}

	public final HttpServletRequest getHttpServletRequest()
	{
		return (HttpServletRequest) httpRequest.get();
	}

	public final HttpServletResponse getHttpServletResponse()
	{
		return (HttpServletResponse) httpResponse.get();
	}
}
