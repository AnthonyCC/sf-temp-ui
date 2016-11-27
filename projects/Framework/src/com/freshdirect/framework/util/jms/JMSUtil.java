/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util.jms;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

import javax.jms.*;

/**
 * Useful JMS related methods.
 *
 * @version $Revision$
 * @author $Author$
 */
public class JMSUtil {

	private static Category LOGGER = LoggerFactory.getInstance( JMSUtil.class );

	public final static long RETRY_DELAY = 1000;

	/** @link dependency 
	 * @stereotype instantiate*/
	/*#JMSReconnector lnkJMSReconnector;*/

	/**
	 * Set up a MessageListener as a consumer for a queue connection. Keeps retrying until the operation is successful.
	 * Also sets up an ExceptionListener that reconnects the listener if exceptions occur.
	 *
	 * @param jmsFactory the queue factory
	 * @param listener listener
	 * @param messageSelector message selector string
	 */
	public static void createQueueConnectionConsumer(JMSQueueFactory jmsFactory, MessageListener listener, String messageSelector) {
		boolean isConnected = false;
		while (!isConnected) {
			try {
				QueueConnection qcon;
				QueueSession qsession;
				QueueReceiver qreceiver;
		
				qcon = jmsFactory.createQueueConnection();
		
				qcon.setExceptionListener( new JMSReconnector(jmsFactory, listener, messageSelector) );
		
				qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
				qreceiver = qsession.createReceiver(jmsFactory.getQueue(), messageSelector);
				qreceiver.setMessageListener( listener );
		
				qcon.start();
				isConnected = true;
			} catch (JMSException ex) {
				LOGGER.warn("createQueueConnectionConsumer got JMSException, retrying in " + RETRY_DELAY + "msecs", ex);
				try {
					Thread.sleep( RETRY_DELAY );
				} catch (InterruptedException exc) {}
			}
		}
	}

}
