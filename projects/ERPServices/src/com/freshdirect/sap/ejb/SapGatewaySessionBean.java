/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.command.SapCancelSalesOrder;
import com.freshdirect.sap.command.SapChangeSalesOrder;
import com.freshdirect.sap.command.SapCheckAvailability;
import com.freshdirect.sap.command.SapCommandI;
import com.freshdirect.sap.command.SapCreateCustomer;
import com.freshdirect.sap.command.SapCreateSalesOrder;
import com.freshdirect.sap.command.SapPostReturnCommand;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SapGatewaySessionBean.class);

	public SapOrderI checkAvailability(SapOrderI order, long timeout) {
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return order;
		}

		SapCheckAvailability command = new SapCheckAvailability(order, timeout);

		try {
			command.execute();
		} catch (Exception e) {
			LOGGER.warn("Error checking availability", e);
		}

		return command.getOrder();
	}

	public void sendCreateSalesOrder(SapOrderI order) {
		LOGGER.debug("Sending sales order create request " + order);
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapCreateSalesOrder(order));
	}

	public void sendCreateCustomer(String erpCustomerNumber, SapCustomerI customer) {
		LOGGER.debug("Sending customer order create request " + customer);
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapCreateCustomer(erpCustomerNumber, customer));
	}

	public void sendCancelSalesOrder(String webOrderNumber, String sapOrderNumber) {
		LOGGER.debug("Sending cancel sales order request " + webOrderNumber + " (" + sapOrderNumber + ")");
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapCancelSalesOrder(webOrderNumber, sapOrderNumber));
	}

	public void sendChangeSalesOrder(String webOrderNumber, String sapOrderNumber, SapOrderI order) {
		LOGGER.debug("Sending change sales order request " + webOrderNumber + " (" + sapOrderNumber + ") " + order);
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapChangeSalesOrder(webOrderNumber, sapOrderNumber, order));
	}

	public void sendReturnInvoice(SapPostReturnCommand command) {
		LOGGER.debug("Sending Return Invoice for Invoice# " + command.getInvoiceNumber());
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(command);
	}


	private void enqueue(SapCommandI sapCommand) {
		try {
			ObjectMessage sapMsg = this.qsession.createObjectMessage();
			sapMsg.setStringProperty("MessageType", "SAP/new");
			sapMsg.setObject(sapCommand);

			this.qsender.send(sapMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}

}
