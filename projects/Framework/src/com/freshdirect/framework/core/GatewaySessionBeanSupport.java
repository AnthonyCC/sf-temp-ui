/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.core;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Support class for JMS gateway session beans. Creates a transacted msg queue connection.
 * Configured from deployment descriptor: jms/QueueFactory, jms/Queue.
 *
 * @version $Revision$
 * @author $Author$
 */
public class GatewaySessionBeanSupport extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance( GatewaySessionBeanSupport.class );

	// !!! how about activation/passivation of queue resources?
	private QueueConnection qcon;
	protected QueueSession qsession;
	protected QueueSender qsender;
	protected Queue queue;

	public void ejbCreate() throws CreateException {
		Context ctx = null;
		try {
			ctx = new InitialContext();

			this.initialize(ctx);

		} catch (NamingException ex) {
			LOGGER.warn("Create failed", ex);
			throw new CreateException("NamingException occured while creating bean: "+ex.getMessage());

		} finally {
			if (ctx!=null) try {
				ctx.close();
			} catch (NamingException ex) {}
		}
	}


	protected void initialize(Context ctx) throws NamingException, CreateException {
		try {
			QueueConnectionFactory qconFactory = (QueueConnectionFactory) ctx.lookup("java:comp/env/jms/QueueFactory");
			this.queue = (Queue) ctx.lookup("java:comp/env/jms/Queue");
			
			// create queue connection
			this.qcon = qconFactory.createQueueConnection();
			this.qsession = this.qcon.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
			this.qsender = this.qsession.createSender( this.queue );
			
			qcon.start();

		} catch (JMSException ex) {
			LOGGER.warn("Create failed", ex);
			throw new CreateException("JMSException occured while creating bean: "+ex.getMessage());
		}
	}
	
	
	
	public void ejbRemove() {
		// free queue resources
		try {
			//this.qcon.stop();
			this.qsender.close();
			this.qsession.close();
			this.qcon.close();
		} catch (JMSException ex) {
			LOGGER.warn("Error removing bean", ex);
			throw new EJBException("JMSException occured while removing bean: "+ex.getMessage());	
		}
	}

}
