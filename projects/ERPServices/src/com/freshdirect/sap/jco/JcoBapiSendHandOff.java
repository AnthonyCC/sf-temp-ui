package com.freshdirect.sap.jco;

import java.util.Date;
import java.util.List;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.sap.bapi.BapiSendHandOff;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSendHandOff extends JcoBapiFunction implements BapiSendHandOff {

	private JCoTable stops;
	
	private JCoTable routes;
	
	private JCoTable dispatches;

	private JCoTable trailers;
	
	private JCoTable breaks;

	public JcoBapiSendHandOff() throws JCoException
	{
		super("ZBAPI_ROUTEINFO_UPLOAD_TO_SAP");
	}
	
	public void setHandOffTrailers(List<HandOffTrailerIn> trailerIn)
	{
		if(ErpServicesProperties.isSendTrailerInfo())
		{
			trailers = this.function.getTableParameterList().getTable("T_TRAILER_UPLOAD");
			for(HandOffTrailerIn trailer : trailerIn)
			{
				trailers.insertRow(1);
				trailers.setValue("ZTRAILER_NO", trailer.getTrailerId()); //Trailer No
				trailers.setValue("ZTRL_DISP_TM", formatTime1(trailer.getTrailerDispatchTime())); //Trailer Dispatch Time
				trailers.setValue("ZCD_ID", trailer.getCrossDockId()); //CrossDock Id
				trailers.nextRow();
			}
		}
	}

	@Override
	public void setHandOffRoutes(List<HandOffRouteIn> routesIn)
	{
		routes = this.function.getTableParameterList().getTable("T_TRUCK_UPLOAD");
		for(HandOffRouteIn route : routesIn) {
			routes.insertRow(1);
			routes.setValue("ZZTRKNO", route.getRouteId()); // Route No
			routes.setValue("ZZMODEL", route.getDeliveryModel()); // Delivery Model
			routes.setValue("ZZDEPTIME", formatTime(route.getStartTime())); // Route Start Time
			routes.setValue("ZTRUCK_MILES", route.getDistance()); 
			routes.setValue("ZTRUCK_DRIVETIME", route.getTravelTimeInfo());
			routes.setValue("ZTRUCK_SRV_TIME", route.getServiceTimeInfo());
			routes.setValue("ZTRUCK_DISP_TIME", formatTime(route.getRouteDispatchTime()));
			routes.setValue("ZTRUCK_DISPA_SEQ", route.getDispatchSequence());			
			routes.setValue("ZTRUCK_DEPA_TIME", formatTime(route.getDepartTime()));
			routes.setValue("ZTRUCK_FRT_STOP", formatTime(route.getFirstStopTime()));
			routes.setValue("ZTRUCK_RTN_TIME", formatTime(route.getReturnToBuildingTime()));
			routes.setValue("ZTRUCK_CHECK_IN", formatTime(route.getCheckInTime()));
			//added the flag to reduce the dependency with SAP changes. The handoff will work even if SAP reverts back the changes.
			if(ErpServicesProperties.isSendTrailerInfo())
			{
				routes.setValue("ZTRAILER_NO", route.getTrailerId());
			}
			routes.setValue("ZDEPOT_PARK_LOC", route.getDepotParkingLocation());
			routes.nextRow();
		}
	}

	@Override
	public void setHandOffStops(List<HandOffStopIn> stopsIn)
	{
		stops = this.function.getTableParameterList().getTable("T_ORDER_UPLOAD");
		for(HandOffStopIn stop : stopsIn) {
			stops.insertRow(1);
			stops.setValue("VBELN", stop.getErpOrderNumber()); // SAP Order No
			stops.setValue("ZZTRKNO", stop.getRouteId()); // Route No
			stops.setValue("ZZSTOPSEQ", stop.getStopNo()); // Stop Sequence
			stops.setValue("ZZDROPTIME", formatTime(stop.getStopArrivalTime())); // Stop Arrival Time
			stops.setValue("BUILDING_TYPE", stop.getBuildingType()); // Building Type
			stops.setValue("DEL_TYPE", stop.getServiceType()); // Delivery Type
			stops.setValue("CROSS_STREET", stop.getCrossStreet()); // SVC Cross Street
			stops.setValue("CROSS_STREET2", stop.getCrossStreet2()); // Cross Street 2
			stops.setValue("SVC_ENTER_ADD", stop.getServiceEntrance()); // SVC Enter Address
			
			//added the flag to reduce the dependency with SAP changes. The handoff will work even if SAP reverts back the changes.
			if(ErpServicesProperties.isSendAddressLine2())
			{
				stops.setValue("SVC_ENTER_ADD_2", stop.getServiceAddress2()); // SVC Enter Address 2
			}
			
			stops.setValue("SVC_OPEN_TIME", formatTime(stop.getServiceEntranceOpenTime())); // SVC Open Time
			stops.setValue("SVC_CLOSE_TIME", formatTime(stop.getServiceEntranceCloseTime())); // SVC Close Time
			stops.setValue("BLDG_OPEN_TIME", formatTime(stop.getBuildingOpenTime())); // Building Open Time
			stops.setValue("BLDG_CLOSE_TIME", formatTime(stop.getBuildingCloseTime())); // Building Close Time
			stops.setValue("FD_DELI_INSTA", stop.getDeliveryInstructionA()); // Delivery InstructionA
			stops.setValue("FD_DELI_INSTB", stop.getDeliveryInstructionB()); // Delivery InstructionB Other
			stops.setValue("REASON_FOR_DIFF", stop.getDifficultReason()); // Reason For Diff
			stops.setValue("ETA_START", formatTime(stop.getETADlvStartTime())); // ETA start Time
			stops.setValue("ETA_END", formatTime(stop.getETADlvEndTime())); // ETA end Time
									
			stops.nextRow();
		}
	}
	
	@Override
	public void setHandOffDispatchStatus(List<HandOffDispatchIn> _dispatches)
	{
		dispatches = this.function.getTableParameterList().getTable("T_DISPATCH");
		if(_dispatches != null)
		{
			for(HandOffDispatchIn dispatch : _dispatches)
			{
				dispatches.insertRow(1);
			
				dispatches.setValue("ZDISPATCH_TIME", formatTime(dispatch.getDispatchTime())); // Dispatch Time
				dispatches.setValue("ZDISPATCH_STATUS", dispatch.isComplete() ? "X" : " "); // Dispatch Sequence
													
				dispatches.nextRow();
			}
		}
		
	}

	@Override
	public void setParameters(String plantCode, Date deliveryDate,
			String waveRunNo, boolean dropNow)
	{
		this.function.getImportParameterList().setValue("I_WERKS", plantCode);
		this.function.getImportParameterList().setValue("I_VDATU", deliveryDate);
		this.function.getImportParameterList().setValue("I_WAVE_RUN", waveRunNo);
		//this.function.getImportParameterList().setValue("I_DROP_IMMEDIATE", dropNow ? "X" : "");
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

	@Override
	public void setHandOffRouteBreaks(List<HandOffRouteBreakIn> breaksIn)
	{
		breaks = this.function.getTableParameterList().getTable("T_DRBRK_UPLOAD");
		for(HandOffRouteBreakIn breakIn : breaksIn)
		{
			breaks.insertRow(1);
			breaks.setValue("ZZTRKNO", breakIn.getRouteId()); //Route No
			breaks.setValue("SEQNO", breakIn.getBreakId()); // Break Sequence
			breaks.setValue("STRTM", formatTime(breakIn.getStartTime())); // Start Time
			breaks.setValue("ENDTM", formatTime(breakIn.getEndTime())); // End Time
			breaks.nextRow();
		}
	}
}
