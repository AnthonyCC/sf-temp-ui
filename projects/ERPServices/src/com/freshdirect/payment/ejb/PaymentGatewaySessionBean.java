/*
 * 
 * PaymentGatewaySessionBean.java
 * Date: Sep 23, 2002 Time: 12:09:07 PM
 */
package com.freshdirect.payment.ejb;

/**
 * 
 * @author knadeem
 */
import javax.ejb.*;
import javax.naming.*;
import javax.jms.*;

import com.freshdirect.payment.command.PaymentCommandI;

import com.freshdirect.framework.core.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

public class PaymentGatewaySessionBean extends GatewaySessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( PaymentGatewaySessionBean.class );
	//Time To Live as capture messages are critical 24 hrs
	public final static long TTL_CRITICAL = 24*60*60*1000; 
	
	private long timeout;
	
	public void updateSaleDlvStatus(PaymentCommandI command) {
		this.enqueue(command);
	}
	
	private ObjectMessage enqueue(PaymentCommandI capture)  {
		try {
			ObjectMessage captureMsg = this.qsession.createObjectMessage();
			captureMsg.setObject(capture);
			this.qsender.send(captureMsg, DeliveryMode.PERSISTENT, 0, TTL_CRITICAL);
			
			return captureMsg;
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message",ex);
			throw new EJBException(ex);
		}
	}


	protected void initialize(Context ctx) throws NamingException, CreateException {
		super.initialize(ctx);
		Integer timeoutInt = (Integer) ctx.lookup("java:comp/env/Timeout");
		this.timeout = timeoutInt.intValue();
	}

}
