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
public class ErpRoutingGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpRoutingGatewaySessionBean.class);
	
	public void sendReservationUpdateRequest(String  reservationId, ContactAddressModel address, String sapOrderNumber) {
		ReservationUpdateCommand command=new ReservationUpdateCommand();
		command.setReservationId(reservationId);
		command.setSapOrderNumber(sapOrderNumber);
		command.setAddress(address);
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
}