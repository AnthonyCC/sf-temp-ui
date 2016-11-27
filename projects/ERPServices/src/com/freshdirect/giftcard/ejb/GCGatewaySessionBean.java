/*
 * 
 * PaymentGatewaySessionBean.java
 * Date: Sep 23, 2002 Time: 12:09:07 PM
 */
package com.freshdirect.giftcard.ejb;

/**
 * 
 * @author skrishnasamy
 */
import javax.ejb.*;
import javax.naming.*;
import javax.jms.*;

import com.freshdirect.payment.command.PaymentCommandI;

import com.freshdirect.framework.core.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.Register;

import org.apache.log4j.*;

public class GCGatewaySessionBean extends GatewaySessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( GCGatewaySessionBean.class );
	
	public void sendRegisterGiftCard(String saleId, double saleAmount) {
		this.enqueue(new Register(saleId, saleAmount));
	}
	
	private void enqueue(Register registerModel)  {
		try {
			ObjectMessage registerMsg = this.qsession.createObjectMessage();
			registerMsg.setObject(registerModel);
			this.qsender.send(registerMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message",ex);
			throw new EJBException(ex);
		}
	}


}
