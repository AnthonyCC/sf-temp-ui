/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.List;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.sap.bapi.BapiSendHandOff;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSendHandOff extends JcoBapiFunction implements BapiSendHandOff {

	private JCO.Table stops;
	
	private JCO.Table routes;
	
	private JCO.Table dispatches;

	private JCO.Table trailers;

	public JcoBapiSendHandOff() {
		super("ZBAPI_ROUTEINFO_UPLOAD_TO_SAP");
	}
	
	public void setHandOffTrailers(List<HandOffTrailerIn> trailerIn){
		if(ErpServicesProperties.isSendTrailerInfo())
		{
			trailers = this.function.getTableParameterList().getTable("T_TRAILER_UPLOAD");
			for(HandOffTrailerIn trailer : trailerIn){
				trailers.insertRow(1);
				trailers.setValue(trailer.getTrailerId(), "ZTRAILER_NO"); //Trailer No
				trailers.setValue(formatTime1(trailer.getTrailerDispatchTime()), "ZTRL_DISP_TM"); //Trailer Dispatch Time
				trailers.setValue(trailer.getCrossDockId(), "ZCD_ID"); //CrossDock Id
				trailers.nextRow();
			}
		}
	}

	@Override
	public void setHandOffRoutes(List<HandOffRouteIn> routesIn) {
		
		routes = this.function.getTableParameterList().getTable("T_TRUCK_UPLOAD");
		for(HandOffRouteIn route : routesIn) {
			routes.insertRow(1);
			routes.setValue(route.getRouteId(), "ZZTRKNO"); // Route No
			routes.setValue(route.getDeliveryModel(), "ZZMODEL"); // Delivery Model
			routes.setValue(formatTime(route.getStartTime()), "ZZDEPTIME"); // Route Start Time
			routes.setValue(route.getDistance(), "ZTRUCK_MILES"); 
			routes.setValue(route.getTravelTimeInfo(), "ZTRUCK_DRIVETIME");
			routes.setValue(route.getServiceTimeInfo(), "ZTRUCK_SRV_TIME");
			routes.setValue(formatTime(route.getRouteDispatchTime()), "ZTRUCK_DISP_TIME");
			routes.setValue(route.getDispatchSequence(), "ZTRUCK_DISPA_SEQ");			
			routes.setValue(formatTime(route.getDepartTime()), "ZTRUCK_DEPA_TIME");
			routes.setValue(formatTime(route.getFirstStopTime()), "ZTRUCK_FRT_STOP");
			routes.setValue(formatTime(route.getReturnToBuildingTime()), "ZTRUCK_RTN_TIME");
			routes.setValue(formatTime(route.getCheckInTime()), "ZTRUCK_CHECK_IN");
			//added the flag to reduce the dependency with SAP changes. The handoff will work even if SAP reverts back the changes.
			if(ErpServicesProperties.isSendTrailerInfo())
			{
				routes.setValue(route.getTrailerId(), "ZTRAILER_NO");
			}
			routes.setValue(route.getDepotParkingLocation(), "ZDEPOT_PARK_LOC");
			routes.nextRow();
		}
	}

	@Override
	public void setHandOffStops(List<HandOffStopIn> stopsIn) {
		stops = this.function.getTableParameterList().getTable("T_ORDER_UPLOAD");
		for(HandOffStopIn stop : stopsIn) {
			stops.insertRow(1);
			stops.setValue(stop.getErpOrderNumber(), "VBELN"); // SAP Order No
			stops.setValue(stop.getRouteId(), "ZZTRKNO"); // Route No
			stops.setValue(stop.getStopNo(), "ZZSTOPSEQ"); // Stop Sequence
			stops.setValue(formatTime(stop.getStopArrivalTime()), "ZZDROPTIME"); // Stop Arrival Time
			stops.setValue(stop.getBuildingType(), "BUILDING_TYPE"); // Building Type
			stops.setValue(stop.getServiceType(), "DEL_TYPE"); // Delivery Type
			stops.setValue(stop.getCrossStreet(), "CROSS_STREET"); // SVC Cross Street
			stops.setValue(stop.getCrossStreet2(), "CROSS_STREET2"); // Cross Street 2
			stops.setValue(stop.getServiceEntrance(), "SVC_ENTER_ADD"); // SVC Enter Address
			//added the flag to reduce the dependency with SAP changes. The handoff will work even if SAP reverts back the changes.
			if(ErpServicesProperties.isSendAddressLine2())
			{
				stops.setValue(stop.getServiceAddress2(), "SVC_ENTER_ADD_2"); // SVC Enter Address 2
			}
			stops.setValue(formatTime(stop.getServiceEntranceOpenTime()), "SVC_OPEN_TIME"); // SVC Open Time
			stops.setValue(formatTime(stop.getServiceEntranceCloseTime()), "SVC_CLOSE_TIME"); // SVC Close Time
			stops.setValue(formatTime(stop.getBuildingOpenTime()), "BLDG_OPEN_TIME"); // Building Open Time
			stops.setValue(formatTime(stop.getBuildingCloseTime()), "BLDG_CLOSE_TIME"); // Building Close Time
			stops.setValue(stop.getDeliveryInstructionA(), "FD_DELI_INSTA"); // Delivery InstructionA
			stops.setValue(stop.getDeliveryInstructionB(), "FD_DELI_INSTB"); // Delivery InstructionB Other
			stops.setValue(stop.getDifficultReason(), "REASON_FOR_DIFF"); // Reason For Diff
									
			stops.nextRow();
		}
	}
	
	@Override
	public void setHandOffDispatchStatus(List<HandOffDispatchIn> _dispatches) {
		dispatches = this.function.getTableParameterList().getTable("T_DISPATCH");
		if(_dispatches != null) {
			for(HandOffDispatchIn dispatch : _dispatches) {
				dispatches.insertRow(1);
				dispatches.setValue(formatTime(dispatch.getDispatchTime()), "ZDISPATCH_TIME"); // Dispatch Time
				dispatches.setValue(dispatch.isComplete() ? "X" : " ", "ZDISPATCH_STATUS"); // Dispatch Sequence
													
				dispatches.nextRow();
			}
		}
		
	}

	@Override
	public void setParameters(String plantCode, Date deliveryDate,
			String waveRunNo, boolean dropNow) {
		this.function.getImportParameterList().setValue(plantCode, "I_WERKS");
		this.function.getImportParameterList().setValue(deliveryDate, "I_VDATU");
		this.function.getImportParameterList().setValue(waveRunNo, "I_WAVE_RUN");
		//this.function.getImportParameterList().setValue(dropNow ? "X" : "", "I_DROP_IMMEDIATE");
	}
		
	private String formatTime(Date input) {
		if(input != null) {
			return QuickDateFormat.SHORT_TIME_FORMATTER.format(input);
		} else {
			return null;
		}
	}
	
	private String formatTime1(Date input) {
		if(input != null) {
			return QuickDateFormat.TIME_FORMATTER.format(input);
		} else {
			return null;
		}
	}

	public String getHandOffResult() {
		
		/*Map cartonInfoMap = new HashMap();
		lstCartonInfo = this.function.getTableParameterList().getTable("ZCARTON_COUNT");
		Map rowMap = null;
		String strOrderId = null;
		for (int loop = 0; loop < lstCartonInfo.getNumRows(); loop++) {
			strOrderId = lstCartonInfo.getString("VBELN");			
			rowMap = (Map)cartonInfoMap.get(strOrderId);
			if(rowMap == null) {
				rowMap = new HashMap();
				cartonInfoMap.put(strOrderId, rowMap);				
			}			
			rowMap.put(lstCartonInfo.getValue("CARTONTYPE"), lstCartonInfo.getValue("QUANTITY"));					
			lstCartonInfo.nextRow();
		}*/
		return null;
	}
}
