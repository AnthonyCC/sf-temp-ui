package com.freshdirect.analytics;
import java.sql.Connection;
import java.sql.SQLException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import org.apache.log4j.Category;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author tbalumuri
 *
 */
public class TimeslotEventListener extends EventListener {

	private static Category LOGGER = LoggerFactory.getInstance(TimeslotEventListener.class);
	
	public void onMessage(Message msg) {
		String msgId = "";
		try {
			msgId = msg.getJMSMessageID();
			if (!(msg instanceof ObjectMessage)) 
			{
				LOGGER.error("Message is not an ObjectMessage: " + msg +"-"+msgId);
				return;
			}

			ObjectMessage objMsg = (ObjectMessage) msg;
			Object obj = objMsg.getObject();
			
			if(!(obj instanceof TimeslotEventWrapper))
			{
				return;
			}
			else
			{
				logTimeSlots((TimeslotEventWrapper)obj);
			}
			
		} catch (JMSException ex) {
			ex.printStackTrace();
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void logTimeSlots(TimeslotEventWrapper command) throws Exception {
		
		Connection conn = null;
		try {
			conn = getConnection();
			TimeslotEventDAO.addEntry(conn, command.getReservationId(),command.getOrderId(), command.getCustomerId(), command.getEventType(),
					 command.getEvent(), command.getResponseTime(), command.getAddress());
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("SQLException in TimeslotEventDAO.addEntry() call ", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}}
	
	
}
