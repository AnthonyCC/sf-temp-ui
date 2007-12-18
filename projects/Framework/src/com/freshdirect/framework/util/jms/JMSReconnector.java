/*
 * $Workfile: JMSReconnector.java$
 *
 * $Date: 8/27/2001 12:39:34 PM$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util.jms;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

import javax.jms.*;

/**
 * ExceptionListener, to recover from JMS failures.
 *
 * @version $Revision: 3$
 * @author $Author: Viktor Szathmary$
 */
class JMSReconnector implements ExceptionListener {

	private static Category LOGGER = LoggerFactory.getInstance( JMSReconnector.class );

	private JMSQueueFactory jmsFactory;
	private MessageListener listener;
	private String messageSelector;

	public JMSReconnector(JMSQueueFactory jmsFactory, MessageListener listener, String messageSelector) {
		this.jmsFactory = jmsFactory;
		this.listener = listener;
		this.messageSelector = messageSelector;
	}

	public void onException(JMSException exception) {
		LOGGER.warn("JMSException occured, reconnecting", exception);
		JMSUtil.createQueueConnectionConsumer(jmsFactory, listener, messageSelector);
	}

}
