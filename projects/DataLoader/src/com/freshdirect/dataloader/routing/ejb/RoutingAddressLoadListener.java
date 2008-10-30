/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.routing.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressI;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.GeographyServiceProxy;

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

			Object ox = addressMsg.getObject();
			if ((ox == null) || (!(ox instanceof AddressI))) {
				LOGGER.error("Message is not an AddressI: " + msg);
				// discard msg, no point in throwing it back to the queue
				return;
			}

			address = (AddressI) ox;
			
			processAddress(address);
			
			LOGGER.debug("Message is an AddressI: " + address.getAddress1()+" - >"+address.getZipCode());

		} catch (JMSException ex) {
			LOGGER.error("JMSException occured while reading command, throwing RuntimeException", ex);
			throw new RuntimeException("JMSException occured while reading command: " + ex.getMessage());
		} catch (RoutingServiceException rx) {
			LOGGER.error("JMSException occured while executing address load command, holding RuntimeException", rx);	
			throw new RuntimeException("JMSException occured while reading command: " + rx.getMessage());
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
				
				buildingModel = proxy.getNewBuilding(baseModel);
				
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
	
}
