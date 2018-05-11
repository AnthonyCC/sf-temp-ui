package com.freshdirect.fdlogistics.controller;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.DlvPaymentManager;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.IOrderService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.controller.data.DeliveryConfirmOrder;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.CartonExportRequest;
import com.freshdirect.logistics.controller.data.request.CreateSOIRequest;
import com.freshdirect.logistics.controller.data.request.OrderSearchCriteria;
import com.freshdirect.logistics.controller.data.request.RouteStopRequest;
import com.freshdirect.logistics.controller.data.response.DeliveryExceptions;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslot;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslots;
import com.freshdirect.logistics.controller.data.response.ListOfObjects;
import com.freshdirect.logistics.controller.data.response.OrderIds;
import com.freshdirect.logistics.controller.data.response.OrdersETAWList;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.DeliveryConfirmOrders;
import com.freshdirect.logistics.delivery.dto.OrderDTO;
import com.freshdirect.logistics.delivery.dto.OrdersDTO;
import com.freshdirect.logistics.delivery.dto.OrdersSummaryDTO;
import com.freshdirect.logistics.delivery.model.ActionError;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.logistics.delivery.model.SystemMessageList;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.webapp.util.StandingOrderUtil;


@Controller
@RequestMapping("/v/{version}/{companycode}/orders")
public class OMSController extends BaseController  {
	
	private final static Category LOGGER = LoggerFactory
			.getInstance(OMSController.class);


	@Autowired
	private IOrderService orderService;
	
	@RequestMapping(value = "/id/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	OrdersDTO getOrderById(@PathVariable("orderId") String orderId) {

		OrdersDTO orders = new OrdersDTO();
			try {
				OrderDTO order =orderService.getOrderById(orderId);
				orders.getOrders().add(order);
			} catch (FDLogisticsServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		orders.setSuccessMessage("orders retrieved successfully"); 
	
		return orders;
	}



	@RequestMapping(value = "/cutoff",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	OrdersDTO getOrdersByCutoff(@RequestBody OrderSearchCriteria criteria) {

		OrdersDTO orders = new OrdersDTO();
		
		try{
		
		orders = orderService.getOrderByCutoff(criteria.getDeliveryDate(), criteria.getCutOffDateTime());
		orders.setSuccessMessage("SUCCESS");
			
		}catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		orders.setSuccessMessage("orders retrieved successfully");
		return orders;
	}
	
	
	@RequestMapping(value = "/area",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	OrdersSummaryDTO getActiveOrderByArea(@RequestBody OrderSearchCriteria criteria) {

		OrdersSummaryDTO orders = new OrdersSummaryDTO();
		
		try{
		
		orders = orderService.getActiveOrderByArea(criteria.getDeliveryDate(), criteria.isStandingOrder());
		orders.setSuccessMessage("orders retrieved successfully");
		}catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		} 	
		return orders;
	}

	@RequestMapping(value = "/get",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody OrdersDTO getOrders(@RequestBody OrderSearchCriteria criteria) {
		OrdersDTO orders = new OrdersDTO();
		try{
			orders = orderService.getOrders(criteria);
			orders.setSuccessMessage("orders retrieved successfully");
		}catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return orders;
	
	}


	@RequestMapping(value = "/count",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Map<String, Integer> getOrderStatsByCutoff(@RequestBody OrderSearchCriteria criteria) throws FDLogisticsServiceException{
		Map<String, Integer> orders = new HashMap<String, Integer>();
		try {
			orders = orderService.getOrderStatsByCutoff(criteria.getDeliveryDate(), criteria.getCutOffDateTime());
		} catch (FDLogisticsServiceException e) {
			
		} 
		return orders;
	}
	
	@RequestMapping(value = "/regular",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody OrdersDTO getOrderByCriteria(@RequestBody OrderSearchCriteria criteria) {
		OrdersDTO orders = new OrdersDTO();
		try{
			orders = orderService.getOrderByCriteria(criteria);
			orders.setSuccessMessage("orders retrieved successfully");
		}catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return orders;
	
	}

	@RequestMapping(value = "/date",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody OrdersDTO getOrderStatsByDate(@RequestBody OrderSearchCriteria criteria){
		OrdersDTO orders = new OrdersDTO();
		try {
			orders = orderService.getOrderStatsByDate(criteria.getDeliveryDate());
			orders.setSuccessMessage("orders retrieved successfully");
			return orders;
		} catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return orders;
	}

	@RequestMapping(value = "/so", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody OrdersDTO getStandingOrderByCriteria(@RequestBody OrderSearchCriteria criteria){
		OrdersDTO orders = new OrdersDTO();
		try{	
			orders = orderService.getStandingOrderByCriteria(criteria);
			orders.setSuccessMessage("orders retrieved successfully");
		}catch (FDLogisticsServiceException e) {
		orders.setStatus(Result.STATUS_FAILED);
		orders.addErrorMessages(new ActionError("technical_difficulty",
			SystemMessageList.MSG_TECHNICAL_ERROR));
	} 
		return orders;
	}

	
	@RequestMapping(value = "/cancel/{orderId}/{sendEmail}", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Result doCancelOrderAction(@PathVariable("orderId") String orderId, @PathVariable("sendEmail") boolean sendEmail) {

		boolean success = false;
		if (orderId == null) {
			LOGGER.error("Cannot extract order model from payload");
			return Result.createFailureMessage("order cancelled successfully");
		}
		
		try {
			FDCustomerOrderInfo orderInfo = new FDCustomerOrderInfo();
			orderInfo.setSaleId(orderId);		
			// Assume everything is fine...
			success = this.cancelOrder(orderInfo, "SYSTEM", sendEmail);
			if(!success){
				 return Result.createFailureMessage("failed to cancel order.");
			}else{
				return Result.createSuccessMessage("order cancelled successfully");
			}
		} catch (Exception e) {
			return Result.createFailureMessage("failed to cancel order.");
		}
	}
	
	private OrderDTO convertToOrderDTOModel(FDOrderI f)
	{
		OrderDTO order=new OrderDTO();
		Customer custModel = new Customer();
		custModel.setFirstName((f.getDeliveryAddress()!=null)?f.getDeliveryAddress().getFirstName():"");
		custModel.setLastName((f.getDeliveryAddress()!=null)?f.getDeliveryAddress().getLastName():"");
		custModel.setCustomerId(f.getCustomerId());
		custModel.setHomePhone((f.getDeliveryInfo().getDeliveryAddress().getPhone()!=null)?f.getDeliveryInfo().getDeliveryAddress().getPhone().toString():"");
		order.setOrderNumber(f.getErpSalesId());
		order.setErpOrderNumber(f.getSapOrderId());
		if(f.getPaymentMethod()!=null && f.getPaymentMethod().getCardType()!=null)
		order.setCardType(f.getPaymentMethod().getCardType().getName());
		
		if(f.getDeliveryInfo()!=null)
		{
		order.setArea(f.getDeliveryInfo().getDeliveryZone());
		order.setStartTime(f.getDeliveryInfo().getDeliveryStartTime());
		order.setEndTime(f.getDeliveryInfo().getDeliveryEndTime());
		order.setCutOffTime(f.getDeliveryInfo().getDeliveryCutoffTime());
		order.setReservationId(f.getDeliveryInfo().getDeliveryReservationId());
		}
		Address address = new Address();						
		order.setAddress(address);
		order.setCustomer(custModel);
		if(f.getDeliveryInfo().getDeliveryAddress()!=null)
		{
		address.setAddress1(f.getDeliveryInfo().getDeliveryAddress().getAddress1());
		address.setAddress2(f.getDeliveryInfo().getDeliveryAddress().getAddress2());
		address.setApartment(f.getDeliveryInfo().getDeliveryAddress().getApartment());
		address.setCity(f.getDeliveryInfo().getDeliveryAddress().getCity());
		address.setState(f.getDeliveryInfo().getDeliveryAddress().getState());
		address.setZipCode(f.getDeliveryInfo().getDeliveryAddress().getZipCode());
		address.setDeliveryInstructions(f.getDeliveryInstructions());
		address.setUnattendedInstructions(f.getDeliveryInfo().getDeliveryAddress().getUnattendedDeliveryInstructions());
		String altDest=(f.getDeliveryInfo().getDeliveryAddress().getAltDelivery()!=null)?f.getDeliveryInfo().getDeliveryAddress().getAltDelivery().getName():null;
		if(altDest!=null)
		{
		if(altDest.equalsIgnoreCase(EnumDeliverySetting.NEIGHBOR.getName()))
		{
		String altDeliveryInst=f.getDeliveryInfo().getDeliveryAddress().getAltFirstName()
				+" "+f.getDeliveryInfo().getDeliveryAddress().getAltFirstName()+","+
		((f.getDeliveryInfo().getDeliveryAddress().getAltApartment()!=null)?(" "+f.getDeliveryInfo().getDeliveryAddress().getAltApartment()):"")
		+",Contact at:"+f.getDeliveryInfo().getDeliveryAddress().getAltPhone();
		address.setAltDeliveryInstructions(altDeliveryInst);
		}
		else
			address.setAltDeliveryInstructions(altDest);
		}
		}
		DlvSaleInfo saleInfo = null;
		try {
			saleInfo = DlvPaymentManager.getInstance().getSaleInfo(f.getErpSalesId());
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErpSaleNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		order.setOrderStatus(saleInfo.getStatus().getName());
		order.setAlcohol(f.containsAlcohol());
		order.setSource(f.getOrderSource().toString());
		return order;
	}

	@RequestMapping(value = "/cancel", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Result doCancelOrders(@RequestBody OrderIds orderIds) {
		return Result.createSuccessMessage("order cancelled successfully");
	}

	
	@RequestMapping(value = "/create/so", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	Result doCreateSOAction(@RequestBody CreateSOIRequest request) {
		
		SOResult.Result result = placeStandingOrder(request.getSoId(), request.getDeliveryDate(), 
				request.getStartTime(), request.getEndTime(), request.getInitiator());
		
		if(result != null){
			return Result.createFailureMessage(result.getErrorHeader());
		}
		return Result.createSuccessMessage("Standing order instance created successfully");
	}
	
	@RequestMapping(value = "/delivery/confirm/", method = RequestMethod.POST)
	public @ResponseBody OrdersDTO confirmOrder(@PathVariable("companycode") String companycode, @RequestBody DeliveryConfirmOrders request) {
		OrdersDTO result = new OrdersDTO();
		int partialCount = 0;
		for(DeliveryConfirmOrder dcorder: request.getOrders()){
			OrderDTO order = new OrderDTO();
			order.setOrderNumber(dcorder.getOrderId());
			try {
			DlvSaleInfo saleInfo = DlvPaymentManager.getInstance().getSaleInfo(dcorder.getOrderId());

			if(saleInfo.isEBT()){
				order.setException("");
			}else{
				if (EnumSaleStatus.ENROUTE.equals(saleInfo.getStatus()) 
						|| EnumSaleStatus.REDELIVERY.equals(saleInfo.getStatus()) 
						|| EnumSaleStatus.PENDING.equals(saleInfo.getStatus())) { 
					DlvPaymentManager.getInstance().deliveryConfirm(dcorder.getOrderId());
					order.setException("");
				}else
					if (EnumSaleStatus.PAYMENT_PENDING.equals(saleInfo.getStatus()) 
						|| EnumSaleStatus.SETTLED.equals(saleInfo.getStatus()) 
						|| EnumSaleStatus.CAPTURE_PENDING.equals(saleInfo.getStatus())) { 
					order.setException("");
					}
					else{
					order.setException("Order is not in correct status to confirm");
					partialCount++;
				}
					
			}
			
			} catch (FDResourceException e) {
				order.setException("technical failure");
			} catch (ErpSaleNotFoundException e) {
				order.setException("Order not found");
			}
			result.getOrders().add(order);
		}
		if(partialCount == 0) {
			result.setSuccessMessage("Orders confirmed successfully");
		} else if(partialCount > 0) {
			result.setSuccessMessage("Orders are not in correct status to confirm");
		}
		return result;
	}
		
	@RequestMapping(value = "/delivery/unconfirm/", method = RequestMethod.POST)
	public @ResponseBody
	OrdersDTO unconfirmOrder(@PathVariable("companycode") String companycode, @RequestBody DeliveryConfirmOrders request) {
		OrdersDTO result = new OrdersDTO();
		for(DeliveryConfirmOrder dcorder: request.getOrders()){
			String orderId = dcorder.getOrderId();
			OrderDTO order = new OrderDTO();
			order.setOrderNumber(orderId);
		
			try {
				DlvSaleInfo saleInfo = DlvPaymentManager.getInstance().getSaleInfo(orderId);
				
				if(EnumSaleStatus.CAPTURE_PENDING.equals(saleInfo.getStatus())){
					DlvPaymentManager.getInstance().unconfirmOrder(orderId);
					order.setException("");
				}else{
					order.setException("Order is not in correct status to unconfirm");
				}
			} catch (FDResourceException e) {
				order.setException("technical failure");
			} catch (ErpSaleNotFoundException e) {
				order.setException("Order not found");
			}
			result.getOrders().add(order);
		}
		result.setSuccessMessage("orders unconfirmed successfully");
		return result;
	}

	@RequestMapping(value = "/delivery/refuse/", method = RequestMethod.POST)
	public @ResponseBody
	OrdersDTO refuseOrder(@PathVariable("companycode") String companycode,
			@RequestBody DeliveryConfirmOrders request) {
		OrdersDTO result = new OrdersDTO();
		int partialCount = 0;
		for (DeliveryConfirmOrder dcorder : request.getOrders()) {
			String orderId = dcorder.getOrderId();
			boolean fullReturn = dcorder.isFullReturn();
			OrderDTO order = new OrderDTO();
			order.setOrderNumber(orderId);
			try {
				DlvSaleInfo saleInfo = DlvPaymentManager.getInstance()
						.getSaleInfo(dcorder.getOrderId());
				if (EnumSaleStatus.ENROUTE.equals(saleInfo.getStatus())
						|| EnumSaleStatus.REDELIVERY.equals(saleInfo
								.getStatus())
						|| EnumSaleStatus.PENDING.equals(saleInfo.getStatus())) {

					LOGGER.debug("Going to create a Return");
					DlvPaymentManager.getInstance().addReturn(orderId,
							fullReturn, !fullReturn);
					order.setException("");
				} 
				else
					if ( EnumSaleStatus.REFUSED_ORDER.equals(saleInfo.getStatus())
							|| EnumSaleStatus.SETTLED.equals(saleInfo.getStatus())) { 
					order.setException("");
					}
					else {
						order.setException("Order is not in correct status to return");
						partialCount++;
					}
			} catch (FDResourceException e) {
				order.setException("technical failure");
			} catch (ErpSaleNotFoundException e) {
				order.setException("Order not found");
			}
			result.getOrders().add(order);
		}

		if (partialCount == 0) {
			result.setSuccessMessage("Orders returned successfully");
		} else if (partialCount > 0) {
			result.setSuccessMessage("Orders are not in correct status to returned");
		}
		return result;
	}
	
	@RequestMapping(value = "/etaw", method = RequestMethod.POST)
	public @ResponseBody
	Result sendOrdersETAW(@RequestBody OrdersETAWList orders) {
		 return Result.createSuccessMessage("orders with eta window sent successfully");
	}

	private boolean cancelOrder(FDCustomerOrderInfo orderModel, String agent, boolean sendEmail) {
		boolean success = true;
		try {
			
			FDOrderI order = FDCustomerManager.getOrder(orderModel.getSaleId());
			if(!EnumSaleStatus.CANCELED.equals(order.getOrderStatus())){
				FDIdentity identity = new FDIdentity(order.getCustomerId(), FDCustomerFactory.getFDCustomerIdFromErpId(order.getCustomerId()));		
				orderModel.setIdentity(identity);
				FDActionInfo actionInfo = getActionInfo(agent);
				// Set it to actionInfo object to write to the activity log.
				actionInfo.setIdentity(identity);
				FDCustomerManager.cancelOrder(actionInfo, orderModel.getSaleId(), sendEmail, 0, false);
				
				ErpActivityRecord rec = actionInfo.createActivity(EnumAccountActivityType.CANCEL_ORDER);
				rec.setNote("Order Cancelled");
				rec.setChangeOrderId( orderModel.getSaleId());
				rec.setStandingOrderId(order.getStandingOrderId());
				FDCustomerManager.logMassCancelActivity(rec);
			}
		} catch (FDResourceException fe) {
			LOGGER.error("System Error occurred while processing Sale ID : " + orderModel.getSaleId() + "\n" + fe.getMessage());			
			fe.printStackTrace();
			success = false;
		} catch (ErpTransactionException te) {
			LOGGER.error("Transaction Error occurred while processing Sale ID : " + orderModel.getSaleId() + "\n" + te.getMessage());
			te.printStackTrace();
			success = false;
		} catch (DeliveryPassException de) {
			LOGGER.error("Delivery Pass Error occurred while processing Sale ID : " + orderModel.getSaleId() + "\n" + de.getMessage());			
		}
		return success;
	}
	
	

	@RequestMapping(value = "/promotion/ws",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody DiscountTimeslots getWindowSteeringDiscounts(@RequestBody Date deliveryDate) {
		DiscountTimeslots result = new DiscountTimeslots();
		try {
			Map<String, List<DiscountTimeslot>> timeslots = orderService.getWindowSteeringDiscounts(deliveryDate);
			result.setDiscountSlots(timeslots);
			result.setSuccessMessage("orders retrieved successfully");

		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
	
	private SOResult.Result placeStandingOrder(String standingOrderId, Date altDate, Date startTime, Date endTime, String initiator) {
		
		lookupMailerHome();
		
		if (standingOrderId == null) {
			LOGGER.error("Empty standingOrderId passed.");
			sendTechnicalMail("Empty standingOrderId passed.");
			return null;
		}
		TimeslotEvent event = new TimeslotEvent(EnumTransactionSource.SYSTEM.getCode(), false, 0.00, false, false, null, EnumCompanyCode.fd.name());		
		
			
		LOGGER.info( "Processing " + standingOrderId + " standing orders." );
		
		SOResult.Result result;
		FDStandingOrder so = null;
		
			try {
				so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(standingOrderId));
				FDActionInfo info = getActionInfo(initiator);
				if(info.getIdentity() == null)info.setIdentity(so.getCustomerIdentity());
				if(null != altDate){
					FDStandingOrderAltDeliveryDate altDateInfo = new FDStandingOrderAltDeliveryDate();
					altDateInfo.setOrigDate(so.getNextDeliveryDate());
					altDateInfo.setAltDate(altDate);
					so.setStartTime(startTime);
					so.setEndTime(endTime);
					so.clearLastError();
					result = StandingOrderUtil.process( so, altDateInfo, event, info, mailerHome, true, true, false );
				}else{
					LOGGER.info( "Alternate date for standing order # " + standingOrderId + " missing." );
					return null;
				}
			} catch (FDResourceException re) {
				invalidateMailerHome();
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = SOResult.createTechnicalError( so, "Processing standing order failed with FDResourceException!" );
			}
			
			if ( result.isError() ) {
				if ( result.isTechnicalError() && result.isSendErrorEmail() ) {
					// technical error
					sendTechnicalMail( result.getErrorDetail() );
				}
			}
			logActivity( so, result, initiator );

		return result;
	}
	
private static MailerGatewayHome mailerHome	= null;
	
	private static void invalidateMailerHome() {
		mailerHome = null;
	}
	
	private static void lookupMailerHome() {
		if (mailerHome != null) {
			return;
		}		
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			mailerHome = (MailerGatewayHome) ctx.lookup( "freshdirect.mail.MailerGateway" );
		} catch ( NamingException ne ) {
			ne.printStackTrace();
		} finally {
			try {
				if ( ctx != null ) {
					ctx.close();
				}
			} catch ( NamingException ne ) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
	private void logActivity ( FDStandingOrder so, SOResult.Result result, String initiator ) {
		
		ErpActivityRecord activityRecord = new ErpActivityRecord();
		activityRecord.setStandingOrderId( so.getId() );
		activityRecord.setCustomerId( so.getCustomerId() );
		activityRecord.setDate( new Date() );
		activityRecord.setInitiator( initiator );
		activityRecord.setSource( EnumTransactionSource.SYSTEM );
		if (result.getErrorHeader() != null || result.getErrorDetail() != null) {
			String note;
			if (result.getErrorHeader() != null) {
				if (result.getErrorDetail() != null)
					note = result.getErrorHeader() + ";" + result.getErrorDetail();
				else
					note = result.getErrorHeader();
			} else {
				if (result.getErrorDetail() != null)
					note = result.getErrorDetail();
				else
					note = null;
			}
			activityRecord.setNote( note );
		}
		activityRecord.setChangeOrderId( result.getSaleId() );
		
		SOResult.Status status = result.getStatus();
		
		if ( status == SOResult.Status.SUCCESS ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_PLACED );
		} else if ( status == SOResult.Status.FAILURE ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FAILED );
		} else if ( status == SOResult.Status.SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_SKIPPED );					
		} /*else if ( status == SOResult.Status.FORCED_SKIPPED ) {
			activityRecord.setActivityType( EnumAccountActivityType.STANDINGORDER_FORCED_SKIPPED );					
		}*/
		if(FDStoreProperties.isSF2_0_AndServiceEnabled("customer.ejb.ActivityLogSB")){
			try {
				FDECommerceService.getInstance().logActivity(activityRecord);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		new ErpLogActivityCommand( activityRecord ).execute();	
		}
	}
	
	private void sendTechnicalMail ( String msg ) {
		try {
						
			ErpMailSender mailer = new ErpMailSender();
			
			String from = ErpServicesProperties.getStandingOrdersTechnicalErrorFromAddress();
			String recipient = ErpServicesProperties.getStandingOrdersTechnicalErrorRecipientAddress();
			String subject = "TECHNICAL ERROR occurred in the Standing Orders background service!";
			
			StringBuilder message = new StringBuilder(); 
			message.append( "Standing Orders background process failed!\n" );
			
			message.append( "message: " );
			message.append( msg );
			message.append( '\n' );
			
			message.append( " Please check the server log for more details...\n" );
			
			message.append( "timestamp: " );
			message.append( new Date() );
			message.append( '\n' );
			
			try {
				message.append( "ip: " );
				message.append( java.net.InetAddress.getLocalHost().getHostAddress() );
				message.append( '\n' );
				message.append( "host: " );
				message.append( java.net.InetAddress.getLocalHost().getCanonicalHostName() );
				message.append( '\n' );
			} catch ( UnknownHostException e ) {
				message.append( "host is unknown" );				
			}
			
			mailer.sendMail(from, recipient, "", subject, message.toString() );
			
		} catch ( MessagingException e ) {
			LOGGER.error( "Failed to send out technical error report email!" );
			e.printStackTrace();
		}
	}


	private FDActionInfo getActionInfo(String agent){
		
		EnumTransactionSource src = EnumTransactionSource.SYSTEM;
		CrmAgentModel crmAgent = new CrmAgentModel();
		crmAgent.setLdapId(agent);
		FDActionInfo info = new FDActionInfo(src, null, agent, null, crmAgent, null);
		
		return info;
	}
	
	@RequestMapping(value = "/ivrlog/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ListOfObjects<CallLogModel> getOrderCallLog( @PathVariable("orderId") String orderId) {
		ListOfObjects<CallLogModel> result = new ListOfObjects<CallLogModel>();
		try {
			List<CallLogModel> data = orderService.getOrderCallLog(orderId);
			result.setData(data);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
	
	@RequestMapping(value = "/calllog",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody DeliveryExceptions getIVRCallLog(@RequestBody DeliveryExceptions exceptions){
		DeliveryExceptions result = new DeliveryExceptions();
		try {
			Map<String ,DeliveryException> data = orderService.getIVRCallLog(exceptions.getData(), 
					exceptions.getFromTime(), exceptions.getToTime());
			result.setData(data);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}

	@RequestMapping(value = "/cartons/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ListOfObjects<String> getCartonList( @PathVariable("orderId") String orderId) {
		ListOfObjects<String> result = new ListOfObjects<String>();
		try {
			List<String> cartonList =  orderService.getCartonList(orderId);
			result.setData(cartonList);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
	
	@RequestMapping(value = "/cartonsV2/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ListOfObjects<CartonInfo> getCartonsV2( @PathVariable("orderId") String orderId) {
		ListOfObjects<CartonInfo> result = new ListOfObjects<CartonInfo>();
		try {
			List<CartonInfo> cartonList =  orderService.getCartonInfo(orderId);
			result.setData(cartonList);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
	
	
	@RequestMapping(value = "/manifest/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody DeliveryManifest getDeliveryManifest( @PathVariable("orderId") String orderId) {
		DeliveryManifest result = new DeliveryManifest();
		try {
			result = orderService.getDeliveryManifest(orderId);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
		
	
	@RequestMapping(value = "/delivery/req/{orderId}",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody ListOfObjects<CallLogModel> getDeliveryReq( @PathVariable("orderId") String orderId) {
		ListOfObjects<CallLogModel> result = new ListOfObjects<CallLogModel>();
		try {
			
			List<CallLogModel> data = orderService.getDeliveryReq(orderId);
			result.setData(data);
			result.setSuccessMessage("records retrieved successfully");
		} catch (FDLogisticsServiceException e) {
			result.setStatus(Result.STATUS_FAILED);
			result.addErrorMessages(new ActionError("technical_difficulty",
				SystemMessageList.MSG_TECHNICAL_ERROR));
		} 
		return result;
	}
	
	@RequestMapping(value = "/carton/export",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result saveCartonInfo(@RequestBody CartonExportRequest request) {
		try {
			orderService.saveCartonInfo(request.getData());
			return Result.createSuccessMessage("saved carton info");
		} catch (FDLogisticsServiceException e) {
			return Result.createFailureMessage("failed to save carton info");
		}
	}
	
	@RequestMapping(value = "/deliveryDate", method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody OrdersDTO getOrdersByDeliveryDate(@RequestBody OrderSearchCriteria criteria) {
		
		OrdersDTO orders = new OrdersDTO();
		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			orders = orderService.getOrdersByDeliveryDate(criteria.getDeliveryDate());
			orders.setSuccessMessage("Records retrieved successfully");
			return orders;
		} catch (FDLogisticsServiceException e) {
			orders.setStatus(Result.STATUS_FAILED);
			orders.addErrorMessages(new ActionError("technical_difficulty",
					SystemMessageList.MSG_TECHNICAL_ERROR));
			return orders;
		}
	}
	
	
	@RequestMapping(value = "/routeStop/export",  method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody Result saveRouteStopInfo(@RequestBody RouteStopRequest request) {
		try {
			orderService.saveRouteStopInfo(request.getData());
			return Result.createSuccessMessage("saved carton info");
		} catch (FDLogisticsServiceException e) {
			return Result.createFailureMessage("failed to save carton info");
		}
	}
	
	
}
