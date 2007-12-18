/*
 * $Workfile:SapResultListener.java$
 *
 * $Date:7/22/2003 12:46:57 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.ejb;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.sap.command.SapCancelSalesOrder;
import com.freshdirect.sap.command.SapChangeSalesOrder;
import com.freshdirect.sap.command.SapCommandI;
import com.freshdirect.sap.command.SapCreateCustomer;
import com.freshdirect.sap.command.SapCreateSalesOrder;
import com.freshdirect.sap.command.SapOrderCommand;
import com.freshdirect.sap.command.SapPostReturnCommand;

/**
 *
 * @version $Revision:4$
 * @author $Author:Kashif Nadeem$
 */
public class SapResultListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(SapResultListener.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public void onMessage(Message message) {

		if (!(message instanceof ObjectMessage)) {
			LOGGER.warn("Internal error: Message " + message + " not an instance of ObjectMessage");
			return;
		}

		ObjectMessage om = (ObjectMessage) message;
		Object o;
		try {
			o = om.getObject();
		} catch (JMSException ex) {
			throw new RuntimeException("JMSException occured " + ex.getMessage());
		}

		if (!(o instanceof SapResult)) {
			LOGGER.warn("Internal error: ObjectMessage " + message + " object " + o + " not an instance of SapResultI");
			return;
		}

		if (o == null) {
			LOGGER.warn(new SapException("Internal error: ObjectMessage " + message + " object is null"));
			return;
		}

		SapResult result = (SapResult) o;

		LOGGER.debug("processResult");

		this.processResult(result);
	}
	
	private void processResult(SapResult result) {

		SapCommandI command = result.getCommand();
		if (command instanceof SapCreateCustomer) {
			this.processCreateCustomer((SapCreateCustomer) command);
		
		} else if (command instanceof SapOrderCommand) {
			this.processOrderCommand((SapOrderCommand)command, result);
		
		} else if(command instanceof SapPostReturnCommand){
			this.processPostReturnCommand((SapPostReturnCommand)command, result);
		
		}else {
			LOGGER.error("Unknown command " + command);
		}

	}

	private void processCreateCustomer(SapCreateCustomer command) {
		LOGGER.debug("SapCreateCustomer");

		String custId = command.getErpCustomerNumber();
		ErpCustomerEB custEB;
		try {
			custEB = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(custId));
			custEB.setSapId(command.getSapCustomerNumber());

		} catch (RemoteException e) {
			throw new EJBException("RemoteException occured processing customer " + custId, e);

		} catch (FinderException e) {
			throw new EJBException("RemoteException occured processing customer " + custId, e);

		}
	}
	
	private void processOrderCommand(SapOrderCommand command, SapResult result){
		LOGGER.debug("SapOrderCommand");

		String saleId = command.getWebOrderNumber();
		try {

			ErpSaleEB saleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			LOGGER.debug("Found sale " + saleId);

			if (!result.isSuccessful()) {

				StringWriter sw = new StringWriter();
				result.getException().printStackTrace(new PrintWriter(sw));
				String detailMessage = sw.toString();
				
				LOGGER.debug("Calling submitFailed " + saleId);
				saleEB.submitFailed(detailMessage);

				LOGGER.debug("Creating case for submitFailed " + saleId);
				createCase(saleEB.getCustomerPk(), saleId);

				LOGGER.debug("Sending email notification for submitFailed " + saleId);
				sendNotificationEmail("[System] Not Submitted sale " + saleId, detailMessage);
				

			} else {

				if (command instanceof SapCreateSalesOrder) {
					saleEB.createOrderComplete(((SapCreateSalesOrder) command).getSapOrderNumber());

				} else if (command instanceof SapCancelSalesOrder) {
					saleEB.cancelOrderComplete();

				} else if (command instanceof SapChangeSalesOrder) {
					saleEB.modifyOrderComplete();

				} else {
					LOGGER.error("Unknown command " + command + " for sale " + saleId);

				}

			}

		} catch (ObjectNotFoundException e) {
			// we can't recover from this
			LOGGER.error("Can't find sale " + saleId, e);

		} catch (ErpTransactionException e) {
			// we can't recover from this
			LOGGER.error("ErpTransactionException occured for sale " + saleId, e);

		} catch (RemoteException e) {
			throw new EJBException("RemoteException occured processing sale " + saleId, e);

		} catch (FinderException e) {
			throw new EJBException("FinderException occured processing sale " + saleId, e);

		}
	}
	
	private void processPostReturnCommand(SapPostReturnCommand command, SapResult result){
		
		if (!result.isSuccessful()) {
			String subject = "[System] Posting return for invoice " + command.getInvoiceNumber() + " failed";

			StringWriter sw = new StringWriter();
			result.getException().printStackTrace(new PrintWriter(sw));

			sendNotificationEmail(subject, sw.toString());
		}
		
	}

	private void sendNotificationEmail(String subject, String body) {
		try {
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getSapMailFrom(), 
							ErpServicesProperties.getSapMailTo(), 
							ErpServicesProperties.getSapMailCC(), 
							subject, body);
		} catch (MessagingException e) {
			throw new EJBException(e);
		}		
	}
	
	private void createCase(PrimaryKey customerPK, String saleId) {
		CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_NOT_SUBMITTED);
		String summary = "Failed to submit sale " + saleId + " to SAP";
		CrmSystemCaseInfo info = new CrmSystemCaseInfo(customerPK, new PrimaryKey(saleId), subject, summary);
		new ErpCreateCaseCommand(LOCATOR, info).execute();
	}

	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("freshdirect.erp.Sale", ErpSaleHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
