package com.freshdirect.delivery.ejb;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataDecoder;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.TimedLruCache;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.TextMessageRequest;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.delivery.model.AirclicCartonInfo;
import com.freshdirect.logistics.delivery.model.AirclicMessage;
import com.freshdirect.logistics.delivery.model.AirclicTextMessage;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.DeliverySignature;
import com.freshdirect.logistics.delivery.model.DeliverySummary;
import com.freshdirect.logistics.delivery.model.EnumApplicationException;
import com.freshdirect.logistics.delivery.model.RouteNextel;
import com.freshdirect.payment.service.FDECommerceService;
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
	
	private static final Category LOGGER = LoggerFactory.getInstance(AirclicManager.class);
	
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

	public byte[] getSignature(String orderId,String companyCode) throws FDResourceException {
		try {
			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();
			return airclicService.getSignature(orderId,companyCode);
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
			int stop = 0;
			if(data[2]!=null) stop = Integer.parseInt(data[2]);
								
			TextMessageRequest request = new TextMessageRequest(data[0], data[1], stop ,
					data[3], data[4], data[5], data[6], data[7]);
			request.setNextTelList(nextelList);
			Result response = airclicService.sendMessage(request);
		
			createCase(request);
			
			if(EnumApplicationException.FinderException.getValue() == response.getErrorCode()){
				return response.getStatus()+ ": "+" Order not in Airclic";
			}
			return response.getStatus();

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}

	private void createCase(TextMessageRequest request) {

		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date deliveryDate = df.parse(request.getDeliveryDate());
			createCase(new AirclicTextMessage(deliveryDate, request.getRoute(), request.getStop(), request.getMessage(), 
					request.getSource(), request.getSender(), request.getOrderId(), request.getCustomerId()));
		} catch (ParseException e) {
			LOGGER.info("ERROR WHILE CREATING THE CASE FOR SENT MESSAGE TO AIRCLIC");
			e.printStackTrace();
		}
	}

	private void createCase(AirclicTextMessage textMessage)
	{
		try
		{
			CrmCaseInfo caseInfo = new CrmCaseInfo();
			caseInfo.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_SYS)); // FIXME: ...
			CrmCaseSubject subject = CrmCaseSubject.getEnum(CrmCaseSubject.CODE_ORDER_ENROUTE_SPL_INSTRUCTION);
			caseInfo.setSubject(subject);
			caseInfo.setPriority(CrmCasePriority.getEnum(CrmCasePriority.CODE_LOW));
			caseInfo.setSummary("Message sent to delivery team");
			
			caseInfo.setAssignedAgentPK(new PrimaryKey(textMessage.getSender()));
			caseInfo.setCustomerPK((textMessage.getCustomerId()!=null)? new PrimaryKey(textMessage.getCustomerId()):null);
			caseInfo.setSalePK((textMessage.getOrderId()!=null)?new PrimaryKey(textMessage.getOrderId()):null);
			
			CrmCaseModel newCase = new CrmCaseModel(caseInfo);
			newCase.setState(CrmCaseState.getEnum(CrmCaseState.CODE_OPEN));
			newCase.setCrmCaseMedia("other");
			
			{
				CrmCaseAction caseAction = new CrmCaseAction();
				caseAction.setType(CrmCaseActionType.getEnum( CrmCaseActionType.CODE_NOTE ) );
				caseAction.setTimestamp(new Date());
				caseAction.setAgentPK(caseInfo.getAssignedAgentPK());
	
				caseAction.setNote(textMessage.getMessage());
				newCase.addAction(caseAction);																		
			}
			
			CrmManager.getInstance().createCase(newCase);
		}
		catch(Exception e)
		{
			LOGGER.warn("AirclicManagerSB createCase: Exception while creating case: " + e);
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
					ListOfObjects<CartonInfo> cartonList = airclicService.getCartonList(orderId);
					LogisticsDataDecoder.decodeResult(cartonList);
					
					Map<String, CartonInfo> cartonMap = new HashMap<String, CartonInfo>();
					
					for(CartonInfo cartonInfo : cartonList.getData()){
						cartonMap.put(cartonInfo.getCartonNumber(), cartonInfo);
					}
					List<String> cartonIDs = new ArrayList<String>(cartonMap.keySet());
							
					cartons = LogisticsDataDecoder.decodeAirclicCartonInfo(airclicService.getCartonScanHistory(cartonIDs));
					
					for(AirclicCartonInfo ac: cartons){
						if(cartonMap.containsKey(ac.getCartonNumber()))
							ac.setCartonType(cartonMap.get(ac.getCartonNumber()).getCartonType());
					}
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

	public DeliveryManifest getDeliveryManifest(String orderId, String date,String companyCode)
			throws FDResourceException {
		
		DeliveryManifest result = deliveryManifestCache.get(orderId);
		try {
			if(result == null){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				result = airclicService.getDeliveryManifest(orderId, date,companyCode);
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
			String routeNo, String date,String erpOrderId,String companyCode) throws FDResourceException {
		DeliverySummary response = deliverySummaryCache.get(orderId);
		try {
			if(response == null){
				IAirclicService airclicService = LogisticsServiceLocator
						.getInstance().getAirclicService();
				//IOrderService orderService = LogisticsServiceLocator
					//	.getInstance().getOrderService();
				if (!ErpServicesProperties.isAirclicBlackhole()) {
					response = LogisticsDataDecoder.decodDeliverySummary(airclicService.getDeliverySummary(orderId, routeNo, date,erpOrderId,companyCode));
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
		Map<String, DeliveryException> response;
		try{
			DlvManagerSB sb = getDlvManagerHome().create();
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("delivery.ejb.DlvManagerSB"))
				response = FDECommerceService.getInstance().getCartonScanInfo();
			else
				response =  sb.getCartonScanInfo();
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
