package com.freshdirect.delivery.routing.ejb;



import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.ejb.ReservationUpdateCommand;
/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RoutingLoadListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RoutingLoadListener.class);
	
	public void onMessage(Message msg) {
		String msgId = "";
		AddressI address = null;
		try {
			msgId = msg.getJMSMessageID();
			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: " + msg +"-"+msgId);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			ObjectMessage addressMsg = (ObjectMessage) msg;
			
			if ("SAP_UPDATE".equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof ReservationUpdateCommand))) {
					LOGGER.error("Message is not an CancelTimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
			}
				process((ReservationUpdateCommand)ox);
			}
			

		} catch (JMSException ex) {
			ex.printStackTrace();
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			//throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		} 
		catch(Exception e) {
			//getMessageDrivenContext().setRollbackOnly();
			e.printStackTrace();
		}
		
	}
	
    private void process(ReservationUpdateCommand command) throws FDResourceException {
    	LOGGER.debug("receiving updateReservationStatus from queue..."+ command.getReservationId());
		FDDeliveryManager.getInstance().updateReservationStatus(command.getReservationId(), command.getAddress(), command.getSapOrderNumber());
	}		
}

