/*
 * 
 * CaptureMessageListener.java
 * Date: Sep 23, 2002 Time: 12:54:30 PM
 */
package com.freshdirect.payment.service;

/**
 * 
 * @author knadeem
 */
import javax.jms.*;
import javax.naming.*;

import com.freshdirect.payment.command.*;
import com.freshdirect.payment.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;

public class PaymentMessageListener extends MessageDrivenBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( PaymentMessageListener.class );
	
	public void onMessage(Message msg) {
				
		Context ctx = null;
		try {
			//String msgId = msg.getJMSMessageID();

			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: "+msg);
				// just silently consume it, no point in throwing it back to the queue
				return;
			}

			ObjectMessage captureMsg=(ObjectMessage)msg;

			Object ox = captureMsg.getObject();
			
			if(ox instanceof PaymentCommandI){
				PaymentCommandI command = (PaymentCommandI)ox;
				ctx = new InitialContext();
				PaymentContext pcx = new PaymentContext(ctx);
				command.setContext(pcx);
				command.execute();
				
			}


		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException occured while reading command: "+ex.getMessage());
		} catch(NamingException ne){
			LOGGER.error("NamingException occured while trying to execute command", ne);
			throw new RuntimeException("NamingException occured while trying to execute command: "+ne.getMessage());
		}catch(Exception e){
			LOGGER.error("Error occured while trying to capture", e);
		}finally{
			try{
				if(ctx != null){
					ctx.close();
					ctx = null;
				}
			}catch(NamingException ne){
				LOGGER.warn("NamingException while cleaningup", ne);
			}
		}
		
	}
}
