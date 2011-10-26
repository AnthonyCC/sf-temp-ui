package com.freshdirect.transadmin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IReservationModel;
import com.freshdirect.routing.model.IStandingOrderModel;
import com.freshdirect.routing.util.json.CrisisManagerJSONSerializer;
import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.UnmarshallException;

@SuppressWarnings("unchecked")
public class CrisisManagerUtil {
	
	private static Category	LOGGER = LoggerFactory.getInstance( CrisisManagerUtil.class );
	
	private String agent;
	private List orders;
	private Set reservations;
	
	private List lastResult = null;	
		
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public void setOrders(List orders) {
		this.orders = orders;
	}
	public void setReservations(Set reservations) {
		this.reservations = reservations;
	}
	
	public boolean doBlockCapacity(boolean block) {
		
		return true;
	}
	
	public int doCancelReservations() {
		int result = 0;
		// check parameters
		if (reservations == null || reservations.size() == 0) {
			LOGGER.error("Null or Empty Reservation list");
			return 0;
		}

		if (agent == null) {
			LOGGER.error("Missing agent parameter");
			return 0;
		}
		
		// prepare serializer
		JSONSerializer ser = new JSONSerializer();
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to register serializer modules", e);
			return 0;
		}
		
		// Serialize orders
		String reservationPayload;
		try {
			reservationPayload = ser.toJSON(reservations);
		} catch (MarshallException e) {
			LOGGER.error("Failed to serialize Reservation list ", e);
			return 0;
		}
		
		final String adminServiceURL = TransportationAdminProperties.getAdminServiceURL();
		if (adminServiceURL == null) {
			LOGGER.error("Admin Service URL is not set in transportation.properties!");
			return 0;
		}
		
		// setup http client
		HttpClient client = new HttpClient();

		PostMethod meth = new PostMethod(adminServiceURL+"?action=cancelReservation");
		meth.addRequestHeader("User-Agent", "CrisisManager/1.0");

		NameValuePair[] pairs = new NameValuePair[2];
		
		pairs[0] = new NameValuePair();
		pairs[0].setName("payload");
		pairs[0].setValue(reservationPayload);
		
		pairs[1] = new NameValuePair();
		pairs[1].setName("agent");
		pairs[1].setValue(agent);
		
		meth.setRequestBody(pairs);
		
		Date tStart = null, tEnd = null;
		//send content and wait for response
		try {
			tStart = new Date();
			int status = client.executeMethod(meth);
			tEnd = new Date();
			LOGGER.debug("Cancel Reservation operation finished with status " + status);			
			LOGGER.debug(" ############ Cancel Reservation operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
			if (status != HttpStatus.SC_OK) {
				LOGGER.error("Cancel Reservation failed with status code " + status);
				return 0;
			}			
			
			// process answer?			
			String respBody = new String(meth.getResponseBody());
			Object resp = ser.fromJSON(respBody);
			LOGGER.debug("Response: " + respBody);
			if (resp instanceof Integer) {
				result = (Integer) resp;
			}
		} catch (HttpException e) {
			LOGGER.error("Failed to Cancel Reservations", e);
			return 0;
		} catch (IOException e) {
			LOGGER.error("Failed to Cancel Reservations", e);
			return 0;
		} catch (UnmarshallException e) {
			LOGGER.error("Failed to Cancel Reservations", e);
			return 0;
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> doCancelOrders() {
		// check parameters
		if (orders == null || orders.size() == 0) {
			LOGGER.error("Null or Empty Order list");
			return null;
		}

		if (agent == null) {
			LOGGER.error("Missing agent parameter");
			return null;
		}
		
		// prepare serializer
		JSONSerializer ser = new JSONSerializer();
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to register serializer modules", e);
			return null;
		}
		Date tStart = null, tEnd = null;
		tStart = new Date();
		boolean orderCancelSuccess = false;
		lastResult = new ArrayList();
		Iterator<String> orderItr = orders.iterator();
		
		while(orderItr.hasNext()){
			String _orderId = orderItr.next();
			// Serialize orders
			String orderPayload = null;
			try {				
				orderPayload = ser.toJSON(_orderId);
			} catch (MarshallException e) {
				LOGGER.error("Failed to serialize order model", e);				
			}
			
			final String adminServiceURL = TransportationAdminProperties.getAdminServiceURL();
			if (adminServiceURL == null) {
				LOGGER.error("Admin Service URL is not set in transportation.properties!");
				return null;
			}
			
			// setup http client
			HttpClient client = new HttpClient();

			PostMethod meth = new PostMethod(adminServiceURL+"?action=cancelOrder");
			meth.addRequestHeader("User-Agent", "CrisisManager/1.0");

			NameValuePair[] pairs = new NameValuePair[2];
			
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(orderPayload);
			
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(agent);

			meth.setRequestBody(pairs);
			
			//send content and wait for response
			try {				
				int status = client.executeMethod(meth);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Cancel Order # "+_orderId +" failed with status code " + status);
					lastResult.add(_orderId);
				}				
				// process answer?			
				String respBody = new String(meth.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof Boolean) {
					orderCancelSuccess = (Boolean) resp;
				}
				if(!orderCancelSuccess){
					lastResult.add(_orderId);
				}
			} catch (HttpException e) {
				LOGGER.error("Failed to Cancel Orders", e);
				lastResult.add(_orderId);
			} catch (IOException e) {
				LOGGER.error("Failed to Cancel Orders", e);
				lastResult.add(_orderId);
			} catch (UnmarshallException e) {
				LOGGER.error("Failed to Cancel Orders", e);
				lastResult.add(_orderId);
			}			
		}
		tEnd = new Date();				
		LOGGER.debug(" ############ Cancel Order operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
	
		return lastResult;
	}
	
	public Map<String, String> doCreateReservations() {
		// check parameters
		if (reservations == null || reservations.size() == 0) {
			LOGGER.error("Null or Empty Reservations list");
			return null;
		}

		if (agent == null) {
			LOGGER.error("Missing agent parameter");
			return null;
		}
		
		// prepare serializer
		JSONSerializer ser = new JSONSerializer();
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to register serializer modules", e);
			return null;
		}
		
		Date tStart = null, tEnd = null;String result = null;
		tStart = new Date();
		Map<String, String> rsvMap = new HashMap<String, String>();
		Iterator<IReservationModel> rsvItr = reservations.iterator();		
		while(rsvItr.hasNext()){
			IReservationModel _reservation = rsvItr.next();
			// Serialize reservation
			String rsvPayload = null;
			try {				
				rsvPayload = ser.toJSON(_reservation);
			} catch (MarshallException e) {
				LOGGER.error("Failed to serialize reservation model", e);				
			}
			
			final String adminServiceURL = TransportationAdminProperties.getAdminServiceURL();
			if (adminServiceURL == null) {
				LOGGER.error("Admin Service URL is not set in transportation.properties!");
				return null;
			}
			
			// setup http client
			HttpClient client = new HttpClient();

			PostMethod meth = new PostMethod(adminServiceURL+"?action=createReservation");
			meth.addRequestHeader("User-Agent", "CrisisManager/1.0");

			NameValuePair[] pairs = new NameValuePair[2];
			
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(rsvPayload);
			
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(agent);

			meth.setRequestBody(pairs);
			
			//send content and wait for response
			try {				
				int status = client.executeMethod(meth);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Create Reservation with customer ID # "+ _reservation.getCustomerModel().getErpCustomerPK() +" failed with status code " + status);
				}				
				// process answer?			
				String respBody = new String(meth.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);	
				if (resp instanceof Integer) {
					result = (String) resp.toString();
				}
				rsvMap.put(_reservation.getOrderId(), result);
			} catch (HttpException e) {
				LOGGER.error("Failed to Create Reservation ", e);				
			} catch (IOException e) {
				LOGGER.error("Failed to Create Reservation ", e);
			} catch (UnmarshallException e) {
				LOGGER.error("Failed to Create Reservation ", e);
			}			
		}
		tEnd = new Date();				
		LOGGER.debug(" ############ Create Reservation operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
		return rsvMap;
	}
	
	@SuppressWarnings("unchecked")
	public List placeStandingOrders() {
		// check parameters
		if (orders == null || orders.size() == 0) {
			LOGGER.error("Null or Empty Standing order list");
			return null;
		}

		if (agent == null) {
			LOGGER.error("Missing agent parameter");
			return null;
		}
		
		// prepare serializer
		JSONSerializer ser = new JSONSerializer();
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to register serializer modules", e);
			return null;
		}
		
		Date tStart = null, tEnd = null;
		tStart = new Date();	
		Iterator<IStandingOrderModel> soItr = orders.iterator();
		lastResult = new ArrayList();
		while(soItr.hasNext()){
			IStandingOrderModel _standingOrder = soItr.next();
			// Serialize IStandingOrderModel
			String soPayload = null;
			try {				
				soPayload = ser.toJSON(_standingOrder);
			} catch (MarshallException e) {
				LOGGER.error("Failed to serialize reservation model", e);				
			}
			
			final String adminServiceURL = TransportationAdminProperties.getAdminServiceURL();
			if (adminServiceURL == null) {
				LOGGER.error("Admin Service URL is not set in transportation.properties!");
				return null;
			}
			
			// setup http client
			HttpClient client = new HttpClient();

			PostMethod meth = new PostMethod(adminServiceURL+"?action=placeSOOrder");
			meth.addRequestHeader("User-Agent", "CrisisManager/1.0");

			NameValuePair[] pairs = new NameValuePair[2];
			
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(soPayload);
			
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(agent);

			meth.setRequestBody(pairs);
			
			//send content and wait for response
			try {				
				int status = client.executeMethod(meth);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Place Standing Order # "+ _standingOrder.getId() +" failed with status code " + status);
				}			
				String respBody = new String(meth.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof IStandingOrderModel) {
					_standingOrder = (IStandingOrderModel) resp;
				}
				if(_standingOrder != null){
					lastResult.add(_standingOrder);
				}					
			} catch (HttpException e) {
				LOGGER.error("Failed to Place Standing Order", e);				
			} catch (IOException e) {
				LOGGER.error("Failed to Place Standing Order ", e);
			} catch (UnmarshallException e) {
				LOGGER.error("Failed to Place Standing Order ", e);
			}			
		}
		tEnd = new Date();				
		LOGGER.debug(" ############ Place Standing Orders operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
		
		return lastResult;
	}
	

}
