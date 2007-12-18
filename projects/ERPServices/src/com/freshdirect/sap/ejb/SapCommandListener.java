/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.jms.JMSQueueFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.command.SapCommandI;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCommandListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SapCommandListener.class);

	private JMSQueueFactory sapQueueFactory;

	public void ejbCreate() {
		InitialContext initCtx = null;
		try {
			initCtx = new InitialContext();

			// init jms stuff
			QueueConnectionFactory factory = (QueueConnectionFactory) initCtx.lookup("java:comp/env/jms/QueueFactory");
			Queue queue = (Queue) initCtx.lookup("java:comp/env/jms/ResponseQueue");

			// !!! hardcoded queueName
			this.sapQueueFactory = new JMSQueueFactory(queue, factory, "sapQueue");

		} catch (NamingException ex) {
			LOGGER.warn("NamingException in ejbCreate", ex);
			throw new EJBException(ex);

		} finally {
			if (initCtx != null) {
				try {
					initCtx.close();
				} catch (NamingException ne) {
				}
			}
		}

	}

	public void onMessage(Message msg) {
		String msgId = "";
		SapCommandI sapCommand = null;
		try {
			msgId = msg.getJMSMessageID();

			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: " + msg);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			ObjectMessage sapMsg = (ObjectMessage) msg;

			Object ox = sapMsg.getObject();
			if ((ox == null) || (!(ox instanceof SapCommandI))) {
				LOGGER.error("Message is not an SapCommandI: " + msg);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			sapCommand = (SapCommandI) ox;

		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		}

		// whew, so we've got the command now... :)

		SapResult result;
		try {
			sapCommand.execute();
			LOGGER.info("SapCommand executed");
			result = new SapResult(sapCommand);

		} catch (SapException ex) {
			LOGGER.warn("SapException occured while processing message", ex);
			result = new SapResult(sapCommand, ex);
		}

		// enqueue response
		try {

			// send sapResult
			sendSapResult(msgId, result);

		} catch (JMSException ex) {
			LOGGER.warn("Failed to enqueue response, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException on enqueue response: " + ex.getMessage());
		}

	}

	/**
	 * Enqueues an SapResult in the SAP queue, setting JMSCorrelationID
	 */
	protected void sendSapResult(String messageID, SapResult result) throws JMSException {
		QueueConnection qcon = sapQueueFactory.createQueueConnection();
		QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		QueueSender qsender = qsession.createSender(sapQueueFactory.getQueue());

		ObjectMessage sapMsg = qsession.createObjectMessage();
		sapMsg.setJMSCorrelationID(messageID);

		String sapCommandClass = result.getCommand().getClass().getName();
		sapMsg.setStringProperty("SAPCommandClass", sapCommandClass);
		sapMsg.setStringProperty("MessageType", "SAP/result");

		sapMsg.setObject(result);
		qsender.send(sapMsg);

		qsender.close();
		qsession.close();
		qcon.close();
	}

}
