package com.freshdirect.payment.ewallet.gateway.ejb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author garooru
 *
 */
public class EwalletActivityLogListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(EwalletActivityLogListener.class);
	@Override
	public void onMessage(Message message) {
		LOGGER.debug("In EwalletActivityLogListener -Before processing the received message");
		ObjectMessage objMsg = (ObjectMessage) message;
		try {
			Object obj = objMsg.getObject();
			LOGGER.debug("Received message is:"+obj);	
			if(obj instanceof EwalletActivityLogModel)	{
				logEwalletActivity((EwalletActivityLogModel)obj);
				LOGGER.debug("Completed logging the gateway activity.");
			}
		} catch (JMSException e) {
			LOGGER.error("JMSException in EwalletActivityLogListener:"+e);
		}

	}
	

	public void logEwalletActivity(EwalletActivityLogModel logModel){
		Connection conn = null;
		try {
			conn = getConnection();
			EwalletActivityLogDAO.log(logModel, conn);
		} catch (SQLException e) {
			LOGGER.error("SQLException in logGatewayActivity:", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing connection:", e);
			}
		}
	}

}
