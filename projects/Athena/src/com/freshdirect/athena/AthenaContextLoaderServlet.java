package com.freshdirect.athena;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.athena.cache.CacheManager;
import com.freshdirect.athena.config.ConfigManager;

public class AthenaContextLoaderServlet  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5288217640329972984L;
	/**
	 * Log.
	 */
	private static final Category LOGGER = Logger.getLogger(AthenaContextLoaderServlet.class);

	
	/**
	 * Init Athena.
	 * 
	 * @throws javax.servlet.ServletException
	 */
	@Override
	public void init() throws ServletException {
		
		LOGGER.debug("AthenaContextLoaderServlet ::: init");	
		super.init();
		
		// Initialize the Cache
		CacheManager.getInstance().init();
				
		// Initialize the XML Config for datasources /api/properties
		ConfigManager.getInstance().init();				
	}

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.debug("AthenaContextLoaderServlet ::: request");
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		CacheManager.getInstance().shutdown();
		ConfigManager.getInstance().shutdown();
		super.destroy();
		LOGGER.debug("AthenaContextLoaderServlet destroy :::");
	}
	
	
}
