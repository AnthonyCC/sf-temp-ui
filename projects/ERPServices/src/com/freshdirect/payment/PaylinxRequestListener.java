/*
 * PaylinxRequestListener.java
 *
 * Created on October 16, 2001, 11:17 AM
 */

package com.freshdirect.payment;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class PaylinxRequestListener implements MessageListener {

	private static Category LOGGER = LoggerFactory.getInstance( PaylinxRequestListener.class );

    /** Creates new PaylinxRequestListener */
    public PaylinxRequestListener() {
    }

    @Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Object s = objectMessage.getObject();
			LOGGER.debug(s);
		} catch (JMSException jmse) {
			LOGGER.warn("error occured", jmse);
		}
	}
}
