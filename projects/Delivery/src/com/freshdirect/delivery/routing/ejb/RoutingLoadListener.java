package com.freshdirect.delivery.routing.ejb;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.delivery.routing.ejb.TimeslotCommand;
import com.freshdirect.routing.model.GeocodeResult;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeocodeResult;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.util.BaseGeocodeEngine;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RoutingLoadListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RoutingLoadListener.class);
	
	public void onMessage(Message msg) {
		String msgId = "";
		AddressI address = null;
		try {
			msgId = msg.getJMSMessageID();
			System.out.println("Message ID :"+msgId);

			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: " + msg +"-"+msgId);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			ObjectMessage addressMsg = (ObjectMessage) msg;
			System.out.println("IRoutingMessageType.GET_TIMESLOT :"+RoutingActivityType.GET_TIMESLOT);
			if(RoutingActivityType.PROCESS_ADDRESS.equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof AddressI))) {
					LOGGER.error("Message is not an AddressI: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
	
				address = (AddressI) ox;
				
				processAddress(address);
				
				LOGGER.debug("Message is an AddressI: " + address.getAddress1()+" - >"+address.getZipCode());
			} else if (RoutingActivityType.GET_TIMESLOT.value().equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				System.out.println("IRoutingMessageType.GET_TIMESLOT :"+RoutingActivityType.GET_TIMESLOT);
				if ((ox == null) || (!(ox instanceof TimeslotCommand))) {
					LOGGER.error("Message is not an TimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
				process((TimeslotCommand)ox);
			}
			else if (RoutingActivityType.RESERVE_TIMESLOT.value().equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof ReserveTimeslotCommand))) {
					LOGGER.error("Message is not an ReserveTimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
				process((ReserveTimeslotCommand)ox);
			}
			else if (RoutingActivityType.CONFIRM_TIMESLOT.value().equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof ConfirmTimeslotCommand))) {
					LOGGER.error("Message is not an ConfirmTimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
				process((ConfirmTimeslotCommand)ox);
			}else if (RoutingActivityType.CANCEL_TIMESLOT.value().equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof CancelTimeslotCommand))) {
					LOGGER.error("Message is not an CancelTimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
				process((CancelTimeslotCommand)ox);
			}
			

		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			//throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		} catch (RoutingServiceException rx) {
			LOGGER.error("JMSException occured while executing address load command, holding RuntimeException", rx);	
			//throw new RuntimeException("JMSException occured while reading command: " + rx.getMessage());
		}/* catch (FDResourceException e) {
			//throw new RuntimeException("JMSException occured while reading command: " + e.getMessage());
		}*/
		catch(Exception e) {
			getMessageDrivenContext().setRollbackOnly();
			e.printStackTrace();
		}
		
	}
	
	private void processAddress(AddressI address) throws RoutingServiceException {
		
		List saveLocationLst = new ArrayList();
		List saveBuildingLst = new ArrayList();
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();
		ILocationModel baseModel = new LocationModel();
		baseModel.setStreetAddress1(proxy.standardizeStreetAddress(address.getAddress1(),address.getAddress2()));
		baseModel.setStreetAddress2(address.getAddress2());
		baseModel.setApartmentNumber(address.getApartment());
		baseModel.setCity(address.getCity());
		baseModel.setState(address.getState());
		baseModel.setZipCode(address.getZipCode());
		baseModel.setCountry(address.getCountry());
		
		ILocationModel locationModel = proxy.getLocation(baseModel);			
					
		if(locationModel == null) {
			IBuildingModel buildingModel = proxy.getBuildingLocation(baseModel);			
			baseModel.setLocationId(proxy.getLocationId());
			if(buildingModel != null && buildingModel.getBuildingId() != null) {				
				baseModel.setBuildingId(buildingModel.getBuildingId());									
				baseModel.setGeographicLocation(buildingModel.getGeographicLocation());		    							
				saveLocationLst.add(baseModel);
			} else {				
				buildingModel = proxy.getNewBuilding(null, baseModel);
				//buildingModel = proxy.getNewBuilding(null, baseModel);
				if(buildingModel != null) {
					baseModel.setBuildingId(buildingModel.getBuildingId());
					baseModel.setZipCode(buildingModel.getZipCode());
					baseModel.setGeographicLocation(buildingModel.getGeographicLocation());	
					saveLocationLst.add(baseModel);
					saveBuildingLst.add(buildingModel);
				}
			}						
		}
		
		if(saveBuildingLst != null && saveBuildingLst.size() > 0) {
			proxy.insertBuildings(saveBuildingLst);
		}		
		if(saveLocationLst != null && saveLocationLst.size() > 0) {
			proxy.insertLocations(saveLocationLst);
		}
	}
	
	private void process(TimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZoneEx(command.getTimeSlots(), command.getAddress());
	}
	
    private void process(ReserveTimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().reserveTimeslotEx(command.getReservation(), command.getAddress());
	}
	
    private void process(ConfirmTimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().commitReservationEx(command.getReservation(),command.getAddress());
	}
    
private void process(CancelTimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().releaseReservationEx(command.getReservation(), command.getAddress());
	}
				
}

