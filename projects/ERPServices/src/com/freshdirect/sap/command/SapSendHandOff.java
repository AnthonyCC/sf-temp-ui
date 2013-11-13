/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Date;
import java.util.List;

import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.bapi.BapiSendHandOff;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffDispatchIn;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffRouteBreakIn;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffRouteIn;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffStopIn;
import com.freshdirect.sap.bapi.BapiSendHandOff.HandOffTrailerIn;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapSendHandOff extends SapCommandSupport {

	private List<HandOffRouteIn> routes;
	private List<HandOffStopIn> stops;
	private String plantCode;
	private Date deliveryDate;
	private String waveRunNo;
	private boolean dropNow;
	private List<HandOffDispatchIn> dispatches;
	private List<HandOffTrailerIn> trailers;
	private List<HandOffRouteBreakIn> breaks;
	
	private BapiInfo[] bapiInfos = null;
	
	public SapSendHandOff(List<HandOffRouteIn> routes,
			List<HandOffStopIn> stops, List<HandOffTrailerIn> trailers, List<HandOffRouteBreakIn> breaks, List<HandOffDispatchIn> dispatches, String plantCode, Date deliveryDate,
			String waveRunNo, boolean dropNow) {
		super();
		this.trailers = trailers;
		this.routes = routes;
		this.stops = stops;
		this.plantCode = plantCode;
		this.deliveryDate = deliveryDate;
		this.waveRunNo = waveRunNo;
		this.dropNow = dropNow;	
		this.dispatches = dispatches;
		this.breaks = breaks;
	}


	public void execute() throws SapException {
		bapiInfos = null;
		BapiSendHandOff bapi = BapiFactory.getInstance().getHandOffSender();
		bapi.setParameters(plantCode, deliveryDate, waveRunNo, dropNow);
		bapi.setHandOffRoutes(routes);
		bapi.setHandOffStops(stops);
		bapi.setHandOffDispatchStatus(dispatches);
		bapi.setHandOffTrailers(trailers);
		bapi.setHandOffRouteBreaks(breaks);
		this.invoke(bapi);
		bapiInfos = bapi.getInfos();
	}


	public BapiInfo[] getBapiInfos() {
		return bapiInfos;
	}
	
	
}
