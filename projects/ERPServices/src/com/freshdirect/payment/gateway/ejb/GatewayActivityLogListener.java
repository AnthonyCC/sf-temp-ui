package com.freshdirect.payment.gateway.ejb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *@deprecated Please use the GatewayActivityLogListener and FDGatewayActivityLogDAOI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */ 
public class GatewayActivityLogListener extends MessageDrivenBeanSupport{


	private static final long serialVersionUID = -5755200116155367468L;
	
	private static Category LOGGER = LoggerFactory.getInstance(GatewayActivityLogListener.class);
	@Override
	public void onMessage(Message message) {

		LOGGER.debug("In GatewayActivityLogListener -Before processing the received message");
		ObjectMessage objMsg = (ObjectMessage) message;
		try {
			Object obj = objMsg.getObject();
			LOGGER.debug("Received message is:"+obj);	
			if(obj instanceof FDGatewayActivityLogModel)	{
				logGatewayActivity((FDGatewayActivityLogModel)obj);
				LOGGER.debug("Completed logging the gateway activity.");
			}
		} catch (JMSException e) {
			LOGGER.error("JMSException in GatewayActivityLogListener:"+e);
		}	
		
	}

	
	public void logGatewayActivity(FDGatewayActivityLogModel logModel){
		Connection conn = null;
		try {
			conn = getConnection();
			FDGatewayActivityLogDAO.log(logModel, conn);
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
