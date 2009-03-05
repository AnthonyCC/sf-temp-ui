package com.freshdirect.routing.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.routing.model.DrivingDirection;
import com.freshdirect.routing.model.DrivingDirectionArc;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IDrivingDirection;
import com.freshdirect.routing.model.IDrivingDirectionArc;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IPathDirection;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.PathDirection;
import com.freshdirect.routing.model.RouteModel;
import com.freshdirect.routing.model.RoutingStopModel;
import com.freshdirect.routing.proxy.stub.roadnet.DirectionArc;
import com.freshdirect.routing.proxy.stub.roadnet.DirectionData;
import com.freshdirect.routing.proxy.stub.roadnet.PathDirections;
import com.freshdirect.routing.proxy.stub.transportation.RoutingRoute;
import com.freshdirect.routing.proxy.stub.transportation.RoutingStop;

public class RoutingDataDecoder {
	
	public static List decodeRouteList(RoutingRoute[] routes) {
		
		List result = null;
		if(routes != null) {
			result = new ArrayList();
			
			for(int intCount=0; intCount < routes.length; intCount++) {
				result.add(decodeRoute(routes[intCount]));
			}
		}
		return result;
	}
	
	public static IDrivingDirection decodeDrivingDirection(DirectionData direction) {
		
		IDrivingDirection result = null;
		if(direction != null) {
			result = new DrivingDirection();
			result.setTime(direction.getTime());
			result.setDistance(direction.getDistance());

			if(direction.getPathDirections() != null) {
				result.setPathDirections(new ArrayList());
				for(int intCount=0; intCount < direction.getPathDirections().length; intCount++) {
					result.getPathDirections().add(decodePathDirection(direction.getPathDirections()[intCount]));
				}
			}
		}
		return result;
	}
	
	public static IPathDirection decodePathDirection(PathDirections path) {
		IPathDirection result = null;
		if(path != null) {
			result = new PathDirection();
			result.setTime(path.getTime());
			result.setDistance(path.getDistance());

			if(path.getDirectionArcs() != null) {
				result.setDirectionsArc(new ArrayList());
				for(int intCount=0; intCount < path.getDirectionArcs().length; intCount++) {
					result.getDirectionsArc().add(decodeDirectionArc(path.getDirectionArcs()[intCount]));
				}
			}
		}
		return result;
	}
	
	public static IDrivingDirectionArc decodeDirectionArc(DirectionArc arc) {
		IDrivingDirectionArc result = null;
		if(arc != null) {
			result = new DrivingDirectionArc();
			result.setTime(arc.getTime());
			result.setDistance(arc.getDistance());
			result.setInstruction(arc.getInstruction());
		}
		return result;
	}
	
	public static IRouteModel decodeRoute(RoutingRoute route) {
		
		IRouteModel result = null;
		
		if(route != null) {
			
			result = new RouteModel();
			result.setRouteId(route.getRouteID());
			result.setRouteStartTime(route.getStartTime().getTime());
			result.setStops(new TreeSet());
			
			IRoutingStopModel _stop = null;
			ILocationModel _locModel = null;
			IGeographicLocation _geoLocModel = null;
			
			RoutingStop _refStop = null;
			
			if(route.getStops() != null) {
				
				for(int intCount=0;intCount<route.getStops().length ;intCount++) {
					_refStop = route.getStops()[intCount];
					
					_stop = new RoutingStopModel(_refStop.getSequenceNumber());
					_locModel = new LocationModel();
					
					_locModel.setStreetAddress1(_refStop.getAddress().getLine1());
					_locModel.setCity(_refStop.getAddress().getRegion1()); 
					_locModel.setState(_refStop.getAddress().getRegion2());
					_locModel.setZipCode(_refStop.getAddress().getPostalCode());
					
					_stop.setLocation(_locModel);
					
					_geoLocModel = new GeographicLocation();
					_geoLocModel.setLatitude(""+_refStop.getLatitude());
					_geoLocModel.setLongitude(""+_refStop.getLongitude());
					
					_locModel.setGeographicLocation(_geoLocModel);
					
					_stop.setStopArrivalTime(Calendar.getInstance().getTime());
					result.getStops().add(_stop);
				}
			}
			
		}
		return result;
	}

}
