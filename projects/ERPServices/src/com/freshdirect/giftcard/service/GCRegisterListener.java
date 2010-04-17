/*
 * 
 * CaptureMessageListener.java
 * Date: Sep 23, 2002 Time: 12:54:30 PM
 */
package com.freshdirect.giftcard.service;

/**
 * 
 * @author knadeem
 */
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.jms.*;
import javax.naming.*;

import com.freshdirect.payment.command.*;
import com.freshdirect.payment.*;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.giftcard.Register;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;

public class GCRegisterListener extends MessageDrivenBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( GCRegisterListener.class );
	
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	public void onMessage(Message msg) {
				
		Context ctx = null;
		try {
			//String msgId = msg.getJMSMessageID();

			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: "+msg);
				// just silently consume it, no point in throwing it back to the queue
				return;
			}

			ObjectMessage registerMsg=(ObjectMessage)msg;

			Register regModel = (Register) registerMsg.getObject();
			
			//Process register transaction.
			GiftCardManagerHome home= getGiftCardManagerHome();
			GiftCardManagerSB gc=home.create();
			gc.registerGiftCard(regModel.getSaleId(), regModel.getSaleAmount());

		}catch(ErpTransactionException te){
			LOGGER.error("ErpTransactionException occured while trying to execute command", te);
		}catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading register message, throwing RuntimeException", ex);
			throw new EJBException("JMSException occured while reading register message: "+ex.getMessage());
		} catch(NamingException ne){
			LOGGER.error("NamingException occured while trying to execute command", ne);
			throw new EJBException("NamingException occured while trying to execute command: "+ne.getMessage());
		}catch(CreateException ce){
			LOGGER.error("CreateException occured while trying to execute command", ce);
			throw new EJBException("CreateException occured while trying to execute command: "+ce.getMessage());
		}catch(RemoteException re){
			LOGGER.error("CreateException occured while trying to execute command", re);
			throw new EJBException("CreateException occured while trying to execute command: "+re.getMessage());
		}catch(FDResourceException fdre){
			LOGGER.error("CreateException occured while trying to execute command", fdre);
			throw new EJBException("CreateException occured while trying to execute command: "+fdre.getMessage());
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
	
	private GiftCardManagerHome getGiftCardManagerHome() throws NamingException{
		try {
			return (GiftCardManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.GiftCardManager");
		} catch (NamingException e) {
			throw e;
		}
	}
}
