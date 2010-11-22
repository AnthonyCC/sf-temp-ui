/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.bapi;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiSendHandOff extends BapiFunctionI {
	
	public static interface HandOffRouteIn {
		
		public String getRouteId();
		public String getDeliveryModel();
		public Date getStartTime();
				
		public double getDistance();
		public String getTravelTimeInfo();
		public String getServiceTimeInfo();
		
		public double getTravelTime();
		public double getServiceTime();
		
		public Date getRouteDispatchTime();
		public int getDispatchSequence();
		public Date getDepartTime();
		public Date getFirstStopTime();
		public Date getLastStopCompletionTime();
		
		public Date getReturnToBuildingTime();
		public Date getCheckInTime();
	}
	
	public static interface HandOffStopIn {
		
		public String getRouteId();
		public String getErpOrderNumber();
		public int getStopNo();
		public Date getStopArrivalTime();
		
		public String getBuildingType();
		public String getServiceType();		
		public String getCrossStreet();
		public String getCrossStreet2();
		public String getServiceEntrance();
		public Date getServiceEntranceOpenTime();
		public Date getServiceEntranceCloseTime();
		public Date getBuildingOpenTime();
		public Date getBuildingCloseTime();
		public String getDeliveryInstructionA();
		public String getDeliveryInstructionB();
		public String getDifficultReason();
	}
	
	public static interface HandOffDispatchIn {
		
		public Date getDispatchTime();
		public boolean isComplete();
	}
	
	void setHandOffRoutes(List<HandOffRouteIn> routesIn);
	
	void setHandOffStops(List<HandOffStopIn> stopsIn);
	
	void setHandOffDispatchStatus(List<HandOffDispatchIn> dispatches);
	
	void setParameters(String plantCode, Date deliveryDate, String waveRunNo, boolean dropNow);
	
	String getHandOffResult();
}
