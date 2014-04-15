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

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public interface BapiSendHandOff extends BapiFunctionI {
	
	public static interface HandOffTrailerIn {
		public String getTrailerId();
		public Date getTrailerDispatchTime();
		public String getCrossDockId();
	}

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
		public String getTrailerId();
		public String getDepotParkingLocation();

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
		public String getServiceAddress2();
		public Date getServiceEntranceOpenTime();
		public Date getServiceEntranceCloseTime();
		public Date getBuildingOpenTime();
		public Date getBuildingCloseTime();
		public String getDeliveryInstructionA();
		public String getDeliveryInstructionB();
		public String getDifficultReason();
		public Date getETADlvStartTime();
		public Date getETADlvEndTime();
	}
	
	public static interface HandOffRouteBreakIn {
		
		public String getRouteId();
		public String getBreakId();
		public Date getStartTime();
		public Date getEndTime();

	}
	
	public static interface HandOffDispatchIn {
		
		public Date getDispatchTime();
		public boolean isComplete();
	}
	
	void setHandOffTrailers(List<HandOffTrailerIn> trailerIn);

	void setHandOffRoutes(List<HandOffRouteIn> routesIn);
	
	void setHandOffStops(List<HandOffStopIn> stopsIn);
	
	void setHandOffDispatchStatus(List<HandOffDispatchIn> dispatches);
	
	void setParameters(String plantCode, Date deliveryDate, String waveRunNo, boolean dropNow);
	
	String getHandOffResult();

	void setHandOffRouteBreaks(List<HandOffRouteBreakIn> breaks);
}
