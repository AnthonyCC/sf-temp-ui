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
import javax.jms.*;

import com.freshdirect.framework.service.jms.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

public class PaylinxRequestListener implements MessageDrivenI {

	private static Category LOGGER = LoggerFactory.getInstance( PaylinxRequestListener.class );

    /** Creates new PaylinxRequestListener */
    public PaylinxRequestListener() {
    }

    public void onMessage(Message message) {
	try{
	    ObjectMessage objectMessage = (ObjectMessage)message;
	    Object s = objectMessage.getObject();
	    LOGGER.debug(s);
	}catch(JMSException jmse){
	    LOGGER.warn("error occured", jmse);
	}
    }
    
}
