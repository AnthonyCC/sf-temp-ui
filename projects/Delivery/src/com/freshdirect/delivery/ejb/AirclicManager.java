package com.freshdirect.delivery.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataDecoder;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
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

	private static AirclicManager instance;
	private long REFRESH_PERIOD = 1000 * 60 * 10; // 10 minutes
	private long lastRefresh = 0;
	private List<AirclicMessage> messages;

	public DeliverySignature getSignatureDetails(String orderId)
			throws FDResourceException {
		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			return LogisticsDataDecoder.decodeDeliverySignature(airclicService.getSignatureDetails(orderId));

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
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

	public static AirclicManager getInstance() {
		if (instance == null) {
			instance = new AirclicManager();
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
		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			List<AirclicCartonInfo> cartons = new ArrayList<AirclicCartonInfo>();
			
			if (!ErpServicesProperties.isAirclicBlackhole()) {
				ListOfObjects<String> cartonList = airclicService.getCartonList(orderId);
				LogisticsDataDecoder.decodeResult(cartonList);
				cartons = LogisticsDataDecoder.decodeAirclicCartonInfo(airclicService.getCartonScanHistory(cartonList.getData()));
				if (cartons != null) {
					Collections.sort(cartons, new CartonComparator());
				}
			}

			return cartons;
		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
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
		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			return airclicService.getDeliveryManifest(orderId, date);

		} catch (FDLogisticsServiceException e) {
			e.printStackTrace();
			throw new FDResourceException(e);
		}
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
		DeliverySummary response = new DeliverySummary();
		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			//IOrderService orderService = LogisticsServiceLocator
				//	.getInstance().getOrderService();
			if (!ErpServicesProperties.isAirclicBlackhole()) {
				response = LogisticsDataDecoder.decodDeliverySummary(airclicService.getDeliverySummary(orderId, routeNo, date));
				//response = orderService.getDeliverySummary(response, orderId);
				
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

		Map<String, DeliveryException> response = new HashMap<String, DeliveryException>();
		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();

			if (!ErpServicesProperties.isAirclicBlackhole()) {
				response = airclicService.getCartonScanInfo();
			}
			return response;

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	public List<com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo> getSmsMessages(String orderId)
			throws FDResourceException {
		List<com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo> smsInfo = new ArrayList<com.freshdirect.logistics.delivery.sms.model.CrmSmsDisplayInfo>();

		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();

			if (!ErpServicesProperties.isAirclicBlackhole()) {
				smsInfo = airclicService.getSmsInfo();
			}
			return smsInfo;

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}
}
