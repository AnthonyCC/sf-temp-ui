/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.routing.ejb;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapProperties;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RoutingGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RoutingGatewaySessionBean.class);
	
	public void sendShippingAddress(AddressI address) {
		LOGGER.debug("Sending Return Invoice for Invoice# " + address.getAddress1()+" : "+address.getZipCode());
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(address);
	}

	private void enqueue(AddressI sapCommand) {
		try {
			ObjectMessage addressMsg = this.qsession.createObjectMessage();
			addressMsg.setStringProperty("MessageType", "ROUTINGADDRESS/process");
			addressMsg.setObject(sapCommand);

			this.qsender.send(addressMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}

}