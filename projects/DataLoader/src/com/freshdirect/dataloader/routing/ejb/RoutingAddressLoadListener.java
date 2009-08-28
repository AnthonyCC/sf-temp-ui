/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.routing.ejb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.dataloader.routing.ejb.stub.RouteNetPortType_Stub;
import com.freshdirect.dataloader.routing.ejb.stub.RouteNetWebService_Impl;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.ejb.IRoutingMessageType;
import com.freshdirect.routing.ejb.ReservationCommand;
import com.freshdirect.routing.ejb.TimeslotCommand;
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
import com.upslogisticstech.www.UPSLT.RouteNetWebService.Address;
import com.upslogisticstech.www.UPSLT.RouteNetWebService.GeocodeData;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class RoutingAddressLoadListener extends MessageDrivenBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RoutingAddressLoadListener.class);
	
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
			if(IRoutingMessageType.PROCESS_ADDRESS.equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				if ((ox == null) || (!(ox instanceof AddressI))) {
					LOGGER.error("Message is not an AddressI: " + msg);
					// discard msg, no point in throwing it back to the queue
					return;
				}
	
				address = (AddressI) ox;
				
				processAddress(address);
				
				LOGGER.debug("Message is an AddressI: " + address.getAddress1()+" - >"+address.getZipCode());
			}/* else if (IRoutingMessageType.GET_TIMESLOT.equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				process((TimeslotCommand)ox);
			}
			else if (IRoutingMessageType.RESERVE_TIMESLOT.equals(addressMsg.getStringProperty("MessageType"))) {
				Object ox = addressMsg.getObject();
				process((ReservationCommand)ox);
			}*/

		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		} catch (RoutingServiceException rx) {
			LOGGER.error("JMSException occured while executing address load command, holding RuntimeException", rx);	
			//throw new RuntimeException("JMSException occured while reading command: " + rx.getMessage());
		} /*catch (FDResourceException e) {
			//throw new RuntimeException("JMSException occured while reading command: " + e.getMessage());
		}*/
		
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
				buildingModel = proxy.getNewBuilding(new CustomGeocodeEngine(), baseModel);
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
		
		//FDDeliveryManager.getInstance().getTimeslotsForDateRangeAndZoneEx(command.getTimeSlots(), command.getAddress());
	}
	
  /*  private void process(ReservationCommand command) throws FDResourceException {
		
		FDDeliveryManager.getInstance().reserveTimeslotEx(command.getReservation(), command.getAddress());
	}*/
	
	class CustomGeocodeEngine extends BaseGeocodeEngine {
		
		public IGeocodeResult getGeocode(String street, String zipCode, String country) throws RoutingServiceException  {
			
			IGeocodeResult geocodeResult = new GeocodeResult();
			IGeographicLocation result = new GeographicLocation();
			geocodeResult.setGeographicLocation(result);
			try {

				RouteNetPortType_Stub port = (RouteNetPortType_Stub)(new RouteNetWebService_Impl().getRouteNetPortType());
				port._setProperty(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY, RoutingServicesProperties.getRoadNetProviderURL());
				Address address = new Address();
				address.setLine1(street);
				address.setPostalCode(zipCode);
				address.setCountry(country);
				
				List zipCodes = RoutingUtil.getZipCodes(zipCode);
				if(zipCodes != null) {
					Iterator iterator = zipCodes.iterator();
					String tmpZipCode = null;
					
					while(iterator.hasNext()) {
						tmpZipCode = (String)iterator.next();
						address.setPostalCode(tmpZipCode);
						GeocodeData geographicData = port.Geocode(address);
						
						if(geographicData != null) {
							result.setLatitude(""+(double)(geographicData.getCoordinate().getLatitude()/1000000.0));
							result.setLongitude(""+(double)(geographicData.getCoordinate().getLongitude()/1000000.0));
							result.setConfidence(geographicData.getConfidence().getValue());
							result.setQuality(geographicData.getQuality().getValue());
							if(RoutingUtil.isGeocodeAcceptable(geographicData.getConfidence().getValue()
								, geographicData.getQuality().getValue())) {
								geocodeResult.setAlternateZipcode(tmpZipCode);
								break;
							}
						}
					}
				}

			} catch (IOException exp) {
				throw new RoutingServiceException(exp, IIssue.PROCESS_GEOCODE_UNSUCCESSFUL);
			} 
			return geocodeResult;
		}
	}
			
}
