/*
 * $Workfile: ServiceI.java$
 *
 * $Date: 8/27/2001 9:44:34 AM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.service;

import java.util.Map;

/**
 * Generic service interface.
 *
 * @version $Revision: 2$
 * @author $Author: Viktor Szathmary$
 */
public interface ServiceI {

	/**
	 * Configure the service.
	 *
	 * @param options Map of key-value pairs
	 *
	 * @throws ServiceException if the configuration cannot be set for some reason
	 */
	public void configure(Map options) throws ServiceException;

	/**
	 * Start the service.
	 *
	 * @throws ServiceException if the startup fails
	 */
	public void start() throws ServiceException;	
	
	/**
	 * Stop the service.
	 *
	 * @throws ServiceException if the shutdown fails
	 */
	public void stop() throws ServiceException;

}