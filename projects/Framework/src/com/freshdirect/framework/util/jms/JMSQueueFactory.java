/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.util.jms;

import javax.jms.*;

/**
 * Utility class for aggregating every information necessary
 * for making JMS queue connections (ConnectionFactory, queue name).
 *
 * @author Viktor Szathmary
 */
public class JMSQueueFactory {

	private QueueConnectionFactory qconFactory;
	private String queueName;
	private Queue queue;

	public JMSQueueFactory(Queue queue, QueueConnectionFactory qconFactory, String queueName) {
		this.qconFactory=qconFactory;
		this.queueName=queueName;
		this.queue = queue;
	}

	
	public QueueConnectionFactory getQueueConnectionFactory() {
		return this.qconFactory;
	}
	
	public Queue getQueue() {
		return this.queue;
	}
	
	public QueueConnection createQueueConnection() throws JMSException {
		return this.qconFactory.createQueueConnection();
	}

}