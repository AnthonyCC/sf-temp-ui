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
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IGenericSearchModel;
import com.freshdirect.routing.model.IReservationModel;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.routing.util.json.CrisisManagerJSONSerializer;
import com.freshdirect.transadmin.service.exception.IIssue;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.metaparadigm.jsonrpc.JSONSerializer;

@SuppressWarnings("unchecked")
public class CrisisManagerUtil {
	
	private static Category	LOGGER = LoggerFactory.getInstance( CrisisManagerUtil.class );
	
	private static List lastResult = null;	
	
	private static HttpClient client = new HttpClient();

    static {
        //Multi Threaded connection manager
        MultiThreadedHttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();
        mgr.setMaxTotalConnections(500);
        client = new HttpClient(mgr);
    }
    
	private static String getServiceURL() {
		String adminServiceURL = TransportationAdminProperties
				.getAdminServiceURL();
		LOGGER.debug("### Admin Service URL ### " + adminServiceURL);
		if (adminServiceURL == null) {
			LOGGER.error("Admin Service URL is not set in transportation.properties!");
			throw new TransAdminServiceException(
					"Please check the service Url property ", null,
					IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		return adminServiceURL;
	}
	
	public static int doBlockCapacity(List<IGenericSearchModel> models, String intiator) throws Exception {
		int result = 0;
		if (models == null) {
			LOGGER.error("SoureDate or DestDate are null");
			return 0;
		}
		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());

		Iterator<IGenericSearchModel> dateItr = models.iterator();
		while (dateItr.hasNext()) {
			IGenericSearchModel model = dateItr.next();
			String datePayload = null;
			datePayload = ser.toJSON(model);

			PostMethod method = new PostMethod(getServiceURL()
					+ "?action=blockCapacity");
			method.addRequestHeader("User-Agent", "CrisisManager/1.0");
			NameValuePair[] pairs = new NameValuePair[2];
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(datePayload);
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(intiator);
			method.setRequestBody(pairs);

			Date tStart = null, tEnd = null;
			tStart = new Date();
			try {	
				int status = client.executeMethod(method);
				tEnd = new Date();
				LOGGER.debug("Block Capacity operation finished with status "
						+ status);
				LOGGER.debug(" ############ Block Capacity operation finished in "
						+ (tEnd.getTime() - tStart.getTime()) / 1000
						+ " seconds ############");
				if (status != HttpStatus.SC_OK) {
					LOGGER
							.error("Block Capacity failed with status code "
									+ status);
					return 0;
				}
	
				String respBody = new String(method.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof Integer) {
					result = result + (Integer) resp;
				}
			} catch (IOException e) {
				LOGGER.error("Fatal transport error: " + e.getMessage());
			} finally {
				method.releaseConnection();
			}
		}
		return result;		
	}
	
	public static int doUnBlockCapacity(List<IGenericSearchModel> models, String intiator) throws Exception {
		int result = 0;
		if (models == null) {
			throw new TransAdminServiceException("Destination date is missing", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
			
		Iterator<IGenericSearchModel> dateItr = models.iterator();		
		while(dateItr.hasNext()){
			IGenericSearchModel _model = dateItr.next();
			String datePayload = null;
			datePayload = ser.toJSON(_model);
						
			PostMethod method = new PostMethod(getServiceURL()+"?action=unBlockCapacity");
			method.addRequestHeader("User-Agent", "CrisisManager/1.0");
			NameValuePair[] pairs = new NameValuePair[2];		
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(datePayload);		
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(intiator);		
			method.setRequestBody(pairs);
			
			Date tStart = null, tEnd = null;
			tStart = new Date();
			try{
				int status = client.executeMethod(method);
				tEnd = new Date();
				LOGGER.debug("UnBlock Capacity operation finished with status " + status);			
				LOGGER.debug(" ############ UnBlock Capacity operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("UnBlock Capacity failed with status code " + status);
					return 0;
				}			
						
				String respBody = new String(method.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof Integer) {
					result = (Integer) resp;
				}
			} catch (IOException e) {
				LOGGER.error("Fatal transport error: " + e.getMessage());
			} finally {
				method.releaseConnection();
			}
		}			
		return result;
	}
	
	public static int doCancelReservations(Set rsvIdList, String initiator) throws Exception {
		int result = 0;
		if (rsvIdList == null || rsvIdList.size() == 0) {
			throw new TransAdminServiceException("There are no reservations to cancel for the batch.", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		if (initiator == null) {
			throw new TransAdminServiceException("Initiator details are missing", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);	
		}
		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		
		String reservationPayload = ser.toJSON(rsvIdList);
		
		
		PostMethod method = new PostMethod(getServiceURL()+"?action=cancelReservation");
		method.addRequestHeader("User-Agent", "CrisisManager/1.0");

		NameValuePair[] pairs = new NameValuePair[2];		
		pairs[0] = new NameValuePair();
		pairs[0].setName("payload");
		pairs[0].setValue(reservationPayload);		
		pairs[1] = new NameValuePair();
		pairs[1].setName("agent");
		pairs[1].setValue(initiator);		
		method.setRequestBody(pairs);
		
		Date tStart = null, tEnd = null;
		tStart = new Date();
		try{
			int status = client.executeMethod(method);
			tEnd = new Date();
			LOGGER.debug("Cancel Reservation operation finished with status " + status);			
			LOGGER.debug(" ############ Cancel Reservation operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
			if (status != HttpStatus.SC_OK) {
				LOGGER.error("Cancel Reservation failed with status code " + status);
				return 0;
			}	
			// process answer?			
			String respBody = new String(method.getResponseBody());
			Object resp = ser.fromJSON(respBody);
			LOGGER.debug("Response: " + respBody);
			if (resp instanceof Integer) {
				result = (Integer) resp;
			}
		} catch (IOException e) {
			LOGGER.error("Fatal transport error: " + e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> doCancelOrders(List<String> orderIds, String intiator) throws Exception {
		// check parameters
		if (orderIds == null || orderIds.size() == 0) {
			throw new TransAdminServiceException("There are no orders to cancel", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		if (intiator == null) {
			throw new TransAdminServiceException("Initiator details are missing", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}		
		
		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());

		Date tStart = null, tEnd = null;
		tStart = new Date();
		lastResult = new ArrayList();
		Iterator<String> orderItr = orderIds.iterator();
		while (orderItr.hasNext()) {
			
			String _orderId = orderItr.next();
			String orderPayload = ser.toJSON(_orderId);
			boolean isOrderCanceled = false;
						
			PostMethod method = new PostMethod(getServiceURL()
						+ "?action=cancelOrder");
			method.addRequestHeader("User-Agent", "CrisisManager/1.0");
			NameValuePair[] pairs = new NameValuePair[2];
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(orderPayload);
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(intiator);
			method.setRequestBody(pairs);
			try {
				int status = client.executeMethod(method);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Cancel Order # " + _orderId + " failed with status code " + status);
					lastResult.add(_orderId);
				}
	
				String respBody = new String(method.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof Boolean) {
					isOrderCanceled = (Boolean) resp;
				}
				if (!isOrderCanceled) {
					lastResult.add(_orderId);
				}
			 } catch (IOException e) {
				 LOGGER.error("Fatal transport error: " + e.getMessage());
				 lastResult.add(_orderId);
			 } finally {
				 method.releaseConnection();
			 }  
		}
		tEnd = new Date();
		LOGGER.debug(" ############ Cancel Order operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
		
		return lastResult;
	}
	
	public static Map<String, String> doCreateReservations(Set<IReservationModel> rsvModels, String intiator) throws Exception {
		// check parameters
		if (rsvModels == null || rsvModels.size() == 0) {
			throw new TransAdminServiceException("Empty reservations list", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}
		if (intiator == null) {
			throw new TransAdminServiceException("Initiator details are missing", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}		
		Map<String, String> rsvMap = new HashMap<String, String>();

		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());

		Date tStart = null, tEnd = null;
		tStart = new Date();			
		Iterator<IReservationModel> rsvItr = rsvModels.iterator();		
		IReservationModel _reservation = null;
		while (rsvItr.hasNext()) {
			_reservation = rsvItr.next();
			String rsvPayload = ser.toJSON(_reservation);
			String result = null;
			
			PostMethod method = new PostMethod(getServiceURL()
					+ "?action=createReservation");
			method.addRequestHeader("User-Agent", "CrisisManager/1.0");

			NameValuePair[] pairs = new NameValuePair[2];
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(rsvPayload);
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(intiator);
			method.setRequestBody(pairs);
			try {	
				int status = client.executeMethod(method);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Create Reservation with customer ID # "
							+ _reservation.getCustomerModel().getErpCustomerPK()
							+ " failed with status code " + status);
				}
	
				String respBody = new String(method.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof Integer) {
					result = (String) resp.toString();
				}
				rsvMap.put(_reservation.getOrderId(), result);
			} catch (IOException e) {
				LOGGER.error("Fatal transport error: " + e.getMessage());
			} finally {
				method.releaseConnection();
			} 
		}
		tEnd = new Date();				
		LOGGER.debug(" ############ Create Reservation operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
		
		return rsvMap;
	}
		
	@SuppressWarnings("unchecked")
	public static List placeStandingOrders(List<StandingOrderModel> standingOrderLst, String intiator) throws Exception {
	
		if (standingOrderLst == null || standingOrderLst.size() == 0) {
			throw new TransAdminServiceException("Standing Order list is empty", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);	
		}
		if (intiator == null) {
			throw new TransAdminServiceException("Initiator details are missing", null, IIssue.PROCESS_CRISISMNGBATCH_ERROR);
		}

		JSONSerializer ser = new JSONSerializer();
		ser.registerDefaultSerializers();
		ser.registerSerializer(CrisisManagerJSONSerializer.getInstance());

		Date tStart = null, tEnd = null;
		tStart = new Date();
		Iterator<StandingOrderModel> soItr = standingOrderLst.iterator();
		lastResult = new ArrayList();

		StandingOrderModel _standingOrder = null;
		while (soItr.hasNext()) {
			_standingOrder = soItr.next();
			String soPayload = ser.toJSON(_standingOrder);
			String _tempResult = null;
			
			PostMethod method = new PostMethod(getServiceURL()
					+ "?action=placeSOOrder");
			method.addRequestHeader("User-Agent", "CrisisManager/1.0");

			NameValuePair[] pairs = new NameValuePair[2];
			pairs[0] = new NameValuePair();
			pairs[0].setName("payload");
			pairs[0].setValue(soPayload);
			pairs[1] = new NameValuePair();
			pairs[1].setName("agent");
			pairs[1].setValue(intiator);
			method.setRequestBody(pairs);
			try{
				int status = client.executeMethod(method);
				if (status != HttpStatus.SC_OK) {
					LOGGER.error("Place Standing Order # " + _standingOrder.getId()
							+ " failed with status code " + status);
				}
				String respBody = new String(method.getResponseBody());
				Object resp = ser.fromJSON(respBody);
				LOGGER.debug("Response: " + respBody);
				if (resp instanceof String) {
					_tempResult = (String) resp;
				}
				if(_tempResult != null){
					_standingOrder.setErrorHeader(_tempResult);
					_standingOrder.setStatus("FAILURE");
				} else {
					_standingOrder.setErrorHeader(null);
					_standingOrder.setStatus("SUCCESS");
				}
				lastResult.add(_standingOrder);
			} catch (IOException e) {
				LOGGER.error("Fatal transport error: " + e.getMessage());
			} finally {
				method.releaseConnection();
			}
		}
		tEnd = new Date();				
		LOGGER.debug(" ############ Place Standing Orders operation finished in " + (tEnd.getTime()-tStart.getTime())/1000 + " seconds ############");
		
		return lastResult;
	}
}
