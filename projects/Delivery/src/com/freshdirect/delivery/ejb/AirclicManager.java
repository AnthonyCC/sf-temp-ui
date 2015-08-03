package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataDecoder;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.delivery.model.AirclicCartonInfo;
import com.freshdirect.logistics.delivery.model.AirclicMessage;
import com.freshdirect.logistics.delivery.model.AirclicTextMessage;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.DeliverySignature;
import com.freshdirect.logistics.delivery.model.DeliverySummary;
import com.freshdirect.logistics.delivery.model.RouteNextel;
import com.freshdirect.sms.CrmSmsDisplayInfo;

public class AirclicManager {

	private final ServiceLocator serviceLocator;
	private static AirclicManager instance;
	private long REFRESH_PERIOD = 1000 * 60 * 10; // 10 minutes
	private long lastRefresh = 0;
	private List<AirclicMessage> messages = new ArrayList<AirclicMessage>();

	/** cache orderId -> DeliverySignature */
	private static TimedLruCache<String, DeliverySignature> signatureCache = new TimedLruCache<String, DeliverySignature>(100, 60 * 60 * 1000);
	
	/** cache orderId -> Carton History */
	private static TimedLruCache<String, List<AirclicCartonInfo>> cartonHistoryCache = new TimedLruCache<String, List<AirclicCartonInfo>>(100, 10 * 60 * 1000);
	
	/** cache orderId -> Manifest */
	private static TimedLruCache<String, DeliveryManifest> deliveryManifestCache = new TimedLruCache<String, DeliveryManifest>(100, 10 * 60 * 1000);
	
	/** cache orderId -> Summary */
	private static TimedLruCache<String, DeliverySummary> deliverySummaryCache = new TimedLruCache<String, DeliverySummary>(100, 10 * 60 * 1000);
	
	public DlvManagerHome getDlvManagerHome() {
		try {
			return (DlvManagerHome) serviceLocator.getRemoteHome(FDStoreProperties.getDeliveryManagerHome());
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}
	
	public DeliverySignature getSignatureDetails(String orderId)
			throws FDResourceException {
		DeliverySignature result = signatureCache.get(orderId);
		try {
			if(result == null){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				result = LogisticsDataDecoder.decodeDeliverySignature(airclicService.getSignatureDetails(orderId));
				signatureCache.put(orderId, result);
			}
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
		return result;
	}

	public byte[] getSignature(String orderId) throws FDResourceException {
		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			return airclicService.getSignature(orderId);
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	private AirclicManager() throws NamingException {
		this.serviceLocator = new ServiceLocator(FDStoreProperties.getInitialContext());
	}

	public static AirclicManager getInstance() {
		if (instance == null) {
			try {
				instance = new AirclicManager();
			} catch (NamingException e) {
				throw new FDRuntimeException(e);
			}
		}
		return instance;
	}

	public String sendMessage(String[] data, String[] nextels)
			throws FDResourceException {
		try {

			List<String> nextelList = null;
			if (nextels != null) {
				nextelList = new ArrayList<String>(nextels.length);
				nextelList = Arrays.asList(nextels);
			}
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			Result response = airclicService.sendMessage(data, nextelList);
			return response.getStatus();

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public synchronized List<AirclicMessage> getMessages()
			throws FDResourceException {
		if (System.currentTimeMillis() - lastRefresh > REFRESH_PERIOD) {
			try {
				messages.clear();
				messages.add(new AirclicMessage("Call Ops Center",
						"Call Ops Center"));
				messages.add(new AirclicMessage("Late Delivery",
						"Call Ops Center if this order will be late"));
				messages.add(new AirclicMessage("Cancelled Order",
						"Stop X is canceled. Do Not Deliver."));
				messages.add(new AirclicMessage("Redelivery Attempt",
						"Redeliver Stop X. Customer is now home."));
				messages.add(new AirclicMessage("Other", ""));

				lastRefresh = System.currentTimeMillis();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return messages;
	}

	public List<AirclicCartonInfo> lookupCartonScanHistory(String orderId)
			throws FDResourceException {
		List<AirclicCartonInfo> cartons = cartonHistoryCache.get(orderId);
		try {
			if(cartons == null || cartons.isEmpty()){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				
				if (!ErpServicesProperties.isAirclicBlackhole()) {
					ListOfObjects<String> cartonList = airclicService.getCartonList(orderId);
					LogisticsDataDecoder.decodeResult(cartonList);
					cartons = LogisticsDataDecoder.decodeAirclicCartonInfo(airclicService.getCartonScanHistory(cartonList.getData()));
					if (cartons != null) {
						Collections.sort(cartons, new CartonComparator());
					}
				}
				cartonHistoryCache.put(orderId, cartons);
			}
			
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
		return cartons;
	}

	public List<AirclicTextMessage> lookupAirclicMessages(String orderId)
			throws FDResourceException {
		List<AirclicTextMessage> messages = new ArrayList<AirclicTextMessage>();

		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();

			if (!ErpServicesProperties.isAirclicBlackhole()) {
				messages = LogisticsDataDecoder.decodeTextMessages(airclicService.getMessages(orderId));
				
			}
			return messages;
			
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public DeliveryManifest getDeliveryManifest(String orderId, String date)
			throws FDResourceException {
		
		DeliveryManifest result = deliveryManifestCache.get(orderId);
		try {
			if(result == null){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				result = airclicService.getDeliveryManifest(orderId, date);
				deliveryManifestCache.put(orderId, result);
			}
		} catch (FDLogisticsServiceException e) {
			e.printStackTrace();
			throw new FDResourceException(e);
		}
		return result;
	}

	public List<RouteNextel> lookupNextels(String orderId, String route,
			String date) throws FDResourceException {

		List<RouteNextel> response = new ArrayList<RouteNextel>();
		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			if (!ErpServicesProperties.isAirclicBlackhole()) {
				response = LogisticsDataDecoder.decodeRouteNextels(airclicService.getRouteNextels(orderId, route, date));
			}
			return response;
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public List<CallLogModel> getOrderCallLog(String orderId)
			throws FDResourceException {
		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			ListOfObjects<CallLogModel> result = airclicService.getOrderCallLog(orderId);
			LogisticsDataDecoder.decodeResult(result);
			return result.getData();
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public DeliverySummary lookUpDeliverySummary(String orderId,
			String routeNo, String date) throws FDResourceException {
		DeliverySummary response = deliverySummaryCache.get(orderId);
		try {
			if(response == null){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				//IOrderService orderService = LogisticsServiceLocator
					//	.getInstance().getOrderService();
				if (!ErpServicesProperties.isAirclicBlackhole()) {
					response = LogisticsDataDecoder.decodDeliverySummary(airclicService.getDeliverySummary(orderId, routeNo, date));
					//response = orderService.getDeliverySummary(response, orderId);
					deliverySummaryCache.put(orderId, response);	
				}
			}
			return response;
			
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}

	}

	protected class CartonComparator implements Comparator<AirclicCartonInfo> {

		public int compare(AirclicCartonInfo obj1, AirclicCartonInfo obj2) {
			if (obj1.getCartonNumber() != null
					&& obj2.getCartonNumber() != null) {
				return obj2.getCartonNumber().compareTo(obj1.getCartonNumber());
			}
			return 0;
		}
	}

	public Map<String, DeliveryException> getCartonScanInfo()
			throws FDResourceException {
		try{
			DlvManagerSB sb = getDlvManagerHome().create();
			Map<String, DeliveryException> response =  sb.getCartonScanInfo();
			return response;
		}catch (RemoteException re) {
			throw new FDResourceException(re);
		}catch (CreateException ce) {
			throw new FDResourceException(ce);
		}	
	}

	public List<CrmSmsDisplayInfo> getSmsMessages(String orderId)
			throws FDResourceException {
		List<CrmSmsDisplayInfo> smsInfo = new ArrayList<CrmSmsDisplayInfo>();

		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();

			if (!ErpServicesProperties.isAirclicBlackhole()) {
				smsInfo = LogisticsDataDecoder.decodeSmsInfo(airclicService.getSmsInfo(orderId));
			}
			return smsInfo;

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}
}
