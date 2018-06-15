/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import java.sql.Connection;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.inventory.LocalInventoryDAO;
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
			//This method will check the local inventory stored in ERPS.INVENTORY to do real time ATP check when SAP is down
			//(APPDEV-3034) Enhancement Storefront to do local inventory check when SAP blackhole is enabled 
			if(FDStoreProperties.isCheckLocalInventoryEnabled())
				return checkLocalAvailability(order);
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

	private SapOrderI checkLocalAvailability(SapOrderI order) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			LocalInventoryDAO dao = new LocalInventoryDAO();
			return dao.checkLocalAvailability(conn, order);
		} catch (Exception e) {
			LOGGER.warn("Error checking availability", e);
		} finally {
			close(conn);
		}
		return order;
	}
	
	private void releaseLocalInventory(String saleId){
		Connection conn = null;
		try {
			conn = this.getConnection();
			LocalInventoryDAO dao = new LocalInventoryDAO();
			dao.releaseLocalInventory(conn, saleId);
		} catch (Exception e) {
			LOGGER.warn("Error releasing inventory", e);
		} finally {
			close(conn);
		}
	}
	
	private void commitLocalInventory(SapOrderI order){
		Connection conn = null;
		try {
			conn = this.getConnection();
			LocalInventoryDAO dao = new LocalInventoryDAO();
			dao.commitLocalInventory(conn, order);
		} catch (Exception e) {
			LOGGER.warn("Error commiting inventory", e);
		} finally {
			close(conn);
		}
	}
	public void sendCreateSalesOrder(SapOrderI order,EnumSaleType saleType) {
		LOGGER.info("Sending sales order crssseate request " + (order != null ? order.getWebOrderNumber() : "NULL"));
		System.out.println("asdf");
		if (SapProperties.isBlackhole()) {
			//This method will check the local inventory stored in ERPS.INVENTORY to do real time ATP check when SAP is down
			//(APPDEV-3034) Enhancement Storefront to do local inventory check when SAP blackhole is enabled 
			if(FDStoreProperties.isCheckLocalInventoryEnabled())
				commitLocalInventory(order);
			LOGGER.debug("Message blackholed.");
			//return;
		}
		this.enqueue(new SapCreateSalesOrder(order,saleType));
	}

	public void sendCreateCustomer(String erpCustomerNumber, SapCustomerI customer) {
		LOGGER.info("Sending customer order create request " + erpCustomerNumber);
		if (SapProperties.isBlackhole()) {
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapCreateCustomer(erpCustomerNumber, customer));
	}

	public void sendCancelSalesOrder(String webOrderNumber, String sapOrderNumber) {
		LOGGER.info("Sending cancel sales order request " + webOrderNumber + " (" + sapOrderNumber + ")");
		if (SapProperties.isBlackhole()) {
			//This method will check the local inventory stored in ERPS.INVENTORY to do real time ATP check when SAP is down
			//(APPDEV-3034) Enhancement Storefront to do local inventory check when SAP blackhole is enabled 
			if(FDStoreProperties.isCheckLocalInventoryEnabled())
				releaseLocalInventory(webOrderNumber);
			LOGGER.debug("Message blackholed.");
			return;
		}
		this.enqueue(new SapCancelSalesOrder(webOrderNumber, sapOrderNumber));
	}

	public void sendChangeSalesOrder(String webOrderNumber, String sapOrderNumber, SapOrderI order, boolean isPlantChanged) {
		LOGGER.info("Sending change sales order request " + webOrderNumber + " (" + sapOrderNumber + ") ");
		if (SapProperties.isBlackhole()) {
			//This method will check the local inventory stored in ERPS.INVENTORY to do real time ATP check when SAP is down
			//(APPDEV-3034) Enhancement Storefront to do local inventory check when SAP blackhole is enabled
			System.out.println("asdfsdf");
			if(FDStoreProperties.isCheckLocalInventoryEnabled())
				commitLocalInventory(order);
			LOGGER.debug("Message blackholed.");
			return;
		}
		if(isPlantChanged) { //check for plant change 
			this.enqueue(new SapCreateSalesOrder(order, null)); //currently only supports plant switch for regular orders
		} else {
			this.enqueue(new SapChangeSalesOrder(webOrderNumber, sapOrderNumber, order));
		}
	}

	public void sendReturnInvoice(SapPostReturnCommand command) {
		LOGGER.info("Sending Return Invoice for Invoice# " + command.getInvoiceNumber());
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