/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.service.jms;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.framework.service.ServiceI;
import com.freshdirect.framework.util.jms.JMSQueueFactory;
import com.freshdirect.framework.util.jms.JMSUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Service class for JMS queue listener pools.
 * <P>
 * Example of weblogic.properties file:
 * <PRE>
 * weblogic.system.startupClass.blahService=com.freshdirect.framework.service.T3ServiceLauncher
 * weblogic.system.startupArgs.blahService=\
 *		serviceLauncher.class=com.freshdirect.framework.service.jms.JMSService,\
 *		serviceLauncher.delay=10,\
 *		jmsService.class=com.freshdirect.whatever.BlahMessageListener,\
 *		jmsService.poolSize=5,\
 *		jmsService.contextFactory=com.swiftmq.jndi.InitialContextFactoryImpl,\
 *		jmsService.providerURL=smqp://queueserver:4001/timeout=10000,\
 *		jmsService.connectionFactory=QueueConnectionFactory,\
 *		jmsService.queueName=sapQueue@router1,\
 *		jmsService.messageSelector=(MessageType='SAP/new')
 * </PRE>
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class JMSService implements ServiceI {

	private static Category LOGGER = LoggerFactory.getInstance( JMSService.class );

	private Map options;
	
	private int poolSize;
	private Class listenerClass;
	private String messageSelector;
	private JMSQueueFactory queueFactory;
	
	/**
	 * Configure the service.
	 *
	 * @param options Map of key-value pairs
	 *
	 * @throws ServiceException if the configuration cannot be set for some reason
	 */
	public void configure(Map options) throws ServiceException {
		this.options=options;
		String className = getParam("jmsService.class");
		try {
			this.listenerClass = Class.forName(className);
			if (! MessageDrivenI.class.isAssignableFrom( this.listenerClass ) ) {
				throw new ServiceException("Listener class "+className+" does not implement MessageListener");
			}
		} catch (ClassNotFoundException ex) {
			throw new ServiceException("Listener class "+className+" not found");
		}
		
		String contextFactoryName = getParam("jmsService.contextFactory");
		String providerURL = getParam("jmsService.providerURL");
		String connectionFactory = getParam("jmsService.connectionFactory");
		String queueName = getParam("jmsService.queueName");
		
		String poolSizeString = getParam("jmsService.poolSize");
		try {
			this.poolSize = Integer.parseInt(poolSizeString);
		} catch (NumberFormatException ex) {
			throw new ServiceException("jmsService.poolSize is not a number");
		}

		this.messageSelector = getParam("jmsService.messageSelector");

		
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryName);
		env.put(Context.PROVIDER_URL, providerURL);

		Context ctx = null;
		try {
			ctx = new InitialContext(env);
			QueueConnectionFactory qconFactory = (QueueConnectionFactory) ctx.lookup(connectionFactory);
			this.queueFactory = new JMSQueueFactory( (Queue) ctx.lookup(queueName), qconFactory, queueName );
		} catch (NamingException ex) {
			throw new ServiceException(ex, "NamingException occured");
		} finally {
			if (ctx!=null) try {
				ctx.close();
			} catch (NamingException ex) {}
		}
	}
	
	private String getParam(String name) throws ServiceException {
		String param = (String)options.get(name);
		if (param==null) {
			throw new ServiceException("Parameter "+name+" is not specified");
		}
		return param;
	}
		

	/**
	 * Start the service.
	 *
	 * @throws ServiceException if the startup fails
	 */
	public void start() throws ServiceException {
		LOGGER.info("Creating a pool of " + this.poolSize + " listeners ("+ this.listenerClass.getName() +")");
		try {
			for (int i=0; i<this.poolSize; i++) {
				MessageDrivenI listener = (MessageDrivenI) this.listenerClass.newInstance();
				configureListener(listener);

				JMSUtil.createQueueConnectionConsumer(
					this.queueFactory,
					listener,
					this.messageSelector
				);
			}
		} catch (IllegalAccessException ex) {
			throw new ServiceException(ex, "Unable to instantiate listener "+this.listenerClass.getName());
		} catch (InstantiationException ex) {
			throw new ServiceException(ex, "Unable to instantiate listener "+this.listenerClass.getName());
		}
	}
	
	/**
	 * Template method for configuring created MessageDrivenI instances. Override this in JMSService subclasses,
	 * if further configuration of the listener is necessary.
	 *
	 * @param listener MessageDrivenI instance to configure.
	 */
	public void configureListener(MessageDrivenI listener) throws ServiceException {
	}


	public JMSQueueFactory getQueueFactory() {
		return this.queueFactory;	
	}

	/**
	 * Stop the service.
	 *
	 * @throws ServiceException if the shutdown fails
	 */
	public void stop() throws ServiceException {
		throw new ServiceException("JMSListenerService cannot be stopped");
	}

}