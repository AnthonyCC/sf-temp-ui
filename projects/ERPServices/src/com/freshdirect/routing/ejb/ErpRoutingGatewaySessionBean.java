/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.routing.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
/**
 *@deprecated Please do the changes in RoutingGatewayService and RoutingGatewayServiceI in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class ErpRoutingGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpRoutingGatewaySessionBean.class);
	
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) {
		ReservationUpdateCommand command=new ReservationUpdateCommand();
		command.setReservationId(reservationId);
		command.setSapOrderNumber(sapOrderNumber);
		command.setAddress(address);
		this.enqueue(command);
	}
	
	public void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,
			String firstName,String lastName,String deliveryInstructions,String serviceType, String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol){
		
		OrderCreateCommand command=new OrderCreateCommand();
		command.setReservationId(reservationId);
		command.setSaleId(saleId);
		command.setParentOrderId(parentOrderId);
		command.setTip(tip);
		command.setFirstName(firstName);
		command.setLastName(lastName);
		command.setDeliveryInstructions(deliveryInstructions);
		command.setServiceType(serviceType);
		command.setUnattendedInstr(unattendedInstr);
		command.setOrderMobileNumber(orderMobileNumber);
		command.setErpOrderId(erpOrderId);
		command.setContainsAlcohol(containsAlcohol);
		
		this.enqueue(command);
		
		}	
	
	public void sendCancelOrderRequest(String saleId) {
		OrderCancelCommand command=new OrderCancelCommand();
		command.setSaleId(saleId);
		this.enqueue(command);
	}
	
	public void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String firstName,String lastName,String deliveryInstructions,String serviceType, 
			String unattendedInstr,String orderMobileNumber,String erpOrderId,boolean containsAlcohol){
		OrderModifyCommand command=new OrderModifyCommand();
		command.setReservationId(reservationId);
		command.setSaleId(saleId);
		command.setParentOrderId(parentOrderId);
		command.setTip(tip);
		command.setFirstName(firstName);
		command.setLastName(lastName);
		command.setDeliveryInstructions(deliveryInstructions);
		command.setServiceType(serviceType);
		command.setUnattendedInstr(unattendedInstr);
		command.setOrderMobileNumber(orderMobileNumber);
		command.setErpOrderId(erpOrderId);
		command.setContainsAlcohol(containsAlcohol);
		this.enqueue(command);
	}
	
		
	private void enqueue(ReservationUpdateCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType","SAP_UPDATE" );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(OrderCreateCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType","FDX_CREATE" );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(OrderCancelCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType","FDX_CANCEL" );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(OrderModifyCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType","FDX_MODIFY" );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
}