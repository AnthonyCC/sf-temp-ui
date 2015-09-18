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
public class ErpRoutingGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpRoutingGatewaySessionBean.class);
	
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) {
		ReservationUpdateCommand command=new ReservationUpdateCommand();
		command.setReservationId(reservationId);
		command.setSapOrderNumber(sapOrderNumber);
		command.setAddress(address);
		this.enqueue(command);
	}
	
	public void sendSubmitOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String orderMobileNumber){
		
		OrderCreateCommand command=new OrderCreateCommand();
		command.setReservationId(reservationId);
		command.setSaleId(saleId);
		command.setParentOrderId(parentOrderId);
		command.setTip(tip);
		command.setOrderMobileNumber(orderMobileNumber);
		
		this.enqueue(command);
		
		}	
	
	public void sendCancelOrderRequest(String saleId) {
		OrderCancelCommand command=new OrderCancelCommand();
		command.setSaleId(saleId);
		this.enqueue(command);
	}
	
	public void sendModifyOrderRequest(String saleId, String parentOrderId, Double tip, String reservationId,String orderMobileNumber){
		OrderModifyCommand command=new OrderModifyCommand();
		command.setReservationId(reservationId);
		command.setSaleId(saleId);
		command.setParentOrderId(parentOrderId);
		command.setTip(tip);
		command.setOrderMobileNumber(orderMobileNumber);
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