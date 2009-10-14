package com.freshdirect.delivery.routing.ejb;

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
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDTimeslot;
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
	
	public void sendDateRangeAndZoneForTimeslots(List<FDTimeslot> timeSlots, ContactAddressModel address) {
		TimeslotCommand command=new TimeslotCommand();
		command.setAddress(address);
		command.setTimeSlots(timeSlots);
		
		this.enqueue(command);
	}

	public void sendReserveTimeslotRequest(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot) {
		ReserveTimeslotCommand command=new ReserveTimeslotCommand();
		command.setAddress(address);
		command.setReservation(reservation);
		command.setTimeslot(timeslot);
		
		this.enqueue(command);
	}
	
	public void sendCommitReservationRequest(DlvReservationModel reservation, ContactAddressModel address, String previousOrderId) {
		ConfirmTimeslotCommand command=new ConfirmTimeslotCommand();
		command.setReservation(reservation);
		command.setAddress(address);
		command.setPreviousOrderId(previousOrderId);
		this.enqueue(command);
	}
	
	public void sendReleaseReservationRequest(DlvReservationModel reservation, ContactAddressModel address) {
		CancelTimeslotCommand command=new CancelTimeslotCommand();
		command.setReservation(reservation);
		command.setAddress(address);
		this.enqueue(command);
	}
	
	public void sendUpdateReservationRequest(DlvReservationModel reservation,ContactAddressModel address) {
		ConfirmTimeslotCommand command=new ConfirmTimeslotCommand();
		command.setReservation(reservation);
		command.setAddress(address);
		command.setUpdateOnly(true);
		this.enqueue(command);
	}
	
	private void enqueue(AddressI addressCommand) {
		try {
			ObjectMessage addressMsg = this.qsession.createObjectMessage();
			addressMsg.setStringProperty("MessageType", RoutingActivityType.PROCESS_ADDRESS.value());
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
			timeslotMsg.setStringProperty("MessageType",RoutingActivityType.GET_TIMESLOT.value() );
			timeslotMsg.setObject(timeslotCommand);
			this.qsender.send(timeslotMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	private void enqueue(ReserveTimeslotCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType",RoutingActivityType.RESERVE_TIMESLOT.value() );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(ConfirmTimeslotCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType",RoutingActivityType.CONFIRM_TIMESLOT.value() );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
	
	private void enqueue(CancelTimeslotCommand reservation) {
		try {
			ObjectMessage reservationMsg = this.qsession.createObjectMessage();
			reservationMsg.setStringProperty("MessageType",RoutingActivityType.CANCEL_TIMESLOT.value() );
			reservationMsg.setObject(reservation);
			this.qsender.send(reservationMsg);
		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing command", ex);
			throw new EJBException(ex);
		}
	}
}
