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
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
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
		LOGGER.debug("Sending Address For Routing System# " + address.getAddress1()+" : "+address.getZipCode());		
		this.enqueue(address);
	}
	
	public void sendDateRangeAndZoneForTimeslots(List timeSlots, ContactAddressModel address) {
		TimeslotCommand command=new TimeslotCommand();
		command.setAddress(address);
		command.setTimeSlots(timeSlots);
		
		this.enqueue(command);
	}

	public void sendReservationRequest(Object reservation, ContactAddressModel address) {
		ReservationCommand command=new ReservationCommand();
		command.setAddress(address);
		command.setReservation(reservation);
		
		this.enqueue(command);
	}
	private void enqueue(AddressI addressCommand) {
		try {
			ObjectMessage addressMsg = this.qsession.createObjectMessage();
			addressMsg.setStringProperty("MessageType", IRoutingMessageType.PROCESS_ADDRESS);
			addressMsg.setObject(addressCommand);

			this.qsender.send(addressMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(TimeslotCommand timeslotCommand) {
		try {
			ObjectMessage timeslotMsg = this.qsession.createObjectMessage();
			timeslotMsg.setStringProperty("MessageType",IRoutingMessageType.GET_TIMESLOT );
			timeslotMsg.setObject(timeslotCommand);
			this.qsender.send(timeslotMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	private void enqueue(ReservationCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType",IRoutingMessageType.RESERVE_TIMESLOT );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
}