package com.freshdirect.delivery.routing.ejb;



import java.util.ArrayList;
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
import com.freshdirect.routing.ejb.ReservationUpdateCommand;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;
import com.freshdirect.routing.service.util.LocationLocatorResult;

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
			if (!(msg instanceof ObjectMessage)) {
				LOGGER.error("Message is not an ObjectMessage: " + msg +"-"+msgId);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			ObjectMessage addressMsg = (ObjectMessage) msg;
			
			if(RoutingActivityType.PROCESS_ADDRESS.value().equals(addressMsg.getStringProperty("MessageType"))) {
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
			} else if ("SAP_UPDATE".equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof ReservationUpdateCommand))) {
					LOGGER.error("Message is not an CancelTimeslotCommand: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
			}
				process((ReservationUpdateCommand)ox);
			}
			

		} catch (JMSException ex) {
			ex.printStackTrace();
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			//throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		} catch (RoutingServiceException rx) {
			rx.printStackTrace();
			LOGGER.error("JMSException occured while executing address load command, holding RuntimeException", rx);	
			//throw new RuntimeException("JMSException occured while reading command: " + rx.getMessage());
		}
		/* catch (FDResourceException e) {
			//throw new RuntimeException("JMSException occured while reading command: " + e.getMessage());
		}*/
		catch(Exception e) {
			//getMessageDrivenContext().setRollbackOnly();
			e.printStackTrace();
		}
		
	}
	
	private void processAddress(AddressI address) throws RoutingServiceException {
		
		GeographyServiceProxy proxy = new GeographyServiceProxy();
		
		LocationLocatorResult result = proxy.locateAddress(address.getAddress1()
																, address.getAddress2()
																, address.getApartment()
																, address.getCity()
																, address.getState()
																, address.getZipCode()
																, address.getCountry());
		

		/*List saveLocationLst = new ArrayList();
		List saveBuildingLst = new ArrayList();
		
		IBuildingModel building = new BuildingModel();
		
		building.setStreetAddress1(proxy.standardizeStreetAddress(address.getAddress1(),address.getAddress2()));
		building.setStreetAddress2(address.getAddress2());
		
		building.setCity(address.getCity());
		building.setState(address.getState());
		building.setZipCode(address.getZipCode());
		building.setCountry(address.getCountry());
		
		ILocationModel baseModel = new LocationModel(building);
		baseModel.setApartmentNumber(address.getApartment());
		
		ILocationModel locationModel = proxy.getLocation(baseModel);			
					
		if(locationModel == null) {
			IBuildingModel buildingModel = proxy.getBuildingLocation(baseModel);			
			baseModel.setLocationId(proxy.getLocationId());
			if(buildingModel != null && buildingModel.getBuildingId() != null) {																		    							
				saveLocationLst.add(baseModel);
			} else {				
				buildingModel = proxy.getNewBuilding(null, baseModel);
				//buildingModel = proxy.getNewBuilding(null, baseModel);
				if(buildingModel != null) {
						
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
		}*/
	}
	
	private void process(TimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZoneEx(command.getTimeSlots(), command.getAddress());
	}
	
    private void process(ReserveTimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().reserveTimeslotEx(command.getReservation(), command.getAddress(), command.getTimeslot());
	}
	
    private void process(ConfirmTimeslotCommand command) throws FDResourceException {
		
    	if(command != null && command.isUpdateOnly()) {
    		FDDeliveryManager.getInstance().commitReservationEx(command.getReservation(), command.getAddress());
    	} else {
    		FDDeliveryManager.getInstance().commitReservationEx(command.getReservation(), command.getAddress());
		
    	}
    
	}
    
    private void process(ReservationUpdateCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().updateReservationStatus(command.getReservationId(), command.getAddress(), command.getSapOrderNumber());
	}

    private void process(CancelTimeslotCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().releaseReservationEx(command.getReservation(), command.getAddress());
	}
				
}

