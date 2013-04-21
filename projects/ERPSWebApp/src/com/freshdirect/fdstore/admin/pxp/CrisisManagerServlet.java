package com.freshdirect.fdstore.admin.pxp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.Util;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.mail.XMLEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.routing.model.IGenericSearchModel;
import com.freshdirect.routing.model.IReservationModel;
import com.freshdirect.routing.model.StandingOrderModel;
import com.freshdirect.routing.util.json.CrisisManagerJSONSerializer;
import com.freshdirect.webapp.util.StandingOrderUtil;
import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.MarshallException;
import com.metaparadigm.jsonrpc.UnmarshallException;

public class CrisisManagerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4693560915139423591L;

	private static Category LOGGER = LoggerFactory.getInstance( CrisisManagerServlet.class );
	
	private static final String ACTION_PLACE_SO_ORDER = "placeSOOrder";
	private static final String ACTION_CANCEL_ORDER = "cancelOrder";
	private static final String ACTION_BLOCK_CAPACITY = "blockCapacity";
	private static final String ACTION_UNBLOCK_CAPACITY = "unBlockCapacity";
	private static final String ACTION_CREATE_RESERVATION = "createReservation";
	private static final String ACTION_CANCEL_RESERVATION = "cancelReservation";
	
	private static JSONSerializer serializer = new JSONSerializer();
	static {
		try {
			serializer.registerDefaultSerializers();
			serializer.registerSerializer(CrisisManagerJSONSerializer.getInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to setup serializer", e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				
		String userAgent = request.getHeader("User-Agent");
		if (!"CrisisManager/1.0".equalsIgnoreCase(userAgent)) {
			LOGGER.error("Invalid user agent " + userAgent);
			// Invalid user agent
			sendError(response, ":(");
			return;
		}
		String agent = request.getParameter("agent");
		String payload = request.getParameter("payload");
		if (payload == null) {
			LOGGER.error("Empty payload");
			sendError(response, ":/");
			return;
		}
		
		LOGGER.debug("#### Payload: ####>\n" + payload);
		
		Object result = null; 
		String action = request.getParameter("action");
		String sendEmail = request.getParameter("sendEmail");
		
		if(ACTION_CANCEL_ORDER.equalsIgnoreCase(action)){
		
			result = processCancelOrder(request
											, response
											, agent
											, payload
											, Boolean.parseBoolean(sendEmail));
		} else if(ACTION_CANCEL_RESERVATION.equalsIgnoreCase(action)){
			
			result = processCancelReservation(request
												, response
												, agent
												, payload);
		} else if(ACTION_CREATE_RESERVATION.equalsIgnoreCase(action)){
			
			result = processCreateReservation(request
												, response
												, agent
												, payload);
		} else if(ACTION_PLACE_SO_ORDER.equalsIgnoreCase(action)){
			
			result = processStandingOrder(request
											, response
											, agent
											, payload);
		} else if(ACTION_BLOCK_CAPACITY.equalsIgnoreCase(action)){
			try {
				IGenericSearchModel model = extractDatePayload(payload);
				result = FDDeliveryManager.getInstance().blockTimeslotCapacity(model.getSourceDate(), model.getCutOffDate()
						, model.getArea(), model.getStartTime(), model.getEndTime());
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
		} else if(ACTION_UNBLOCK_CAPACITY.equalsIgnoreCase(action)){
			try {
				IGenericSearchModel model = extractDatePayload(payload);
				result = FDDeliveryManager.getInstance().unBlockTimeslotCapacity(model.getSourceDate(), model.getCutOffDate()
											, model.getArea(), model.getStartTime(), model.getEndTime());
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
		}
		
		PrintWriter writer = response.getWriter();
		try {
			writer.append(serializer.toJSON(result));
		} catch (MarshallException e) {
			LOGGER.error("Failed to generate response" + e);
			writer.append("OK");
		}
	}
	
	private String processStandingOrder(HttpServletRequest request,
			HttpServletResponse response, String agent, String payload){

		StandingOrderModel soModel = extractStandingOrderPayload(payload);
		if (soModel == null) {
			LOGGER.error("Cannot extract Reservation Model from payload");			
			sendError(response, ":[");
			return null;
		}		
		SOResult.Result result = placeStandingOrder(soModel.getId(), soModel.getAltDate(), soModel.getStartTime(), soModel.getEndTime(), agent,request);
		return result != null ? result.getErrorHeader() : null;
	}
	
	private SOResult.Result placeStandingOrder(String standingOrderId, Date altDate, Date startTime, Date endTime, String initiator, HttpServletRequest request) {
		
		lookupMailerHome();
		
		if (standingOrderId == null) {
			LOGGER.error("Empty standingOrderId passed.");
			sendTechnicalMail("Empty standingOrderId passed.");
			return null;
		}
		TimeslotEventModel event = new TimeslotEventModel(EnumTransactionSource.SYSTEM.getCode(), false, 0.00, false, false, null);		
		
			
		LOGGER.info( "Processing " + standingOrderId + " standing orders." );
		
		SOResult.Result result;
		FDStandingOrder so = null;
		
			try {
				so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(standingOrderId));
				FDActionInfo info = getActionInfo(initiator);
				if(info.getIdentity() == null)info.setIdentity(so.getCustomerIdentity());
				if(null != altDate){
					so.setStartTime(startTime);
					so.setEndTime(endTime);
					so.clearLastError();
					result = StandingOrderUtil.process( so, altDate, event, info, mailerHome, true, true, false );
				}else{
					LOGGER.info( "Alternate date for standing order # " + standingOrderId + " missing." );
					return null;
				}
			} catch (FDResourceException re) {
				invalidateMailerHome();
				LOGGER.error( "Processing standing order failed with FDResourceException!", re );
				result = SOResult.createTechnicalError( so, "Processing standing order failed with FDResourceException in the CrisisManagerServlet!" );
			}
			
			if ( result.isError() ) {
				if ( result.isTechnicalError() ) {
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
		}
		
		new ErpLogActivityCommand( activityRecord ).execute();		
	}
	
	private void sendErrorMail ( FDStandingOrder so, FDCustomerInfo customerInfo ) {
		try {
			XMLEmailI mail = FDEmailFactory.getInstance().createStandingOrderErrorEmail( customerInfo, so );		
			MailerGatewaySB mailer;
			mailer = mailerHome.create();
			mailer.enqueueEmail( mail );
		} catch ( RemoteException e ) {
			e.printStackTrace();
		} catch ( CreateException e ) {
			e.printStackTrace();
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

	
	private String processCreateReservation(HttpServletRequest request,
			HttpServletResponse response, String agent, String payload) {
		
		IReservationModel rsvModel = extractRsvPayload(payload);
		if (rsvModel == null) {
			LOGGER.error("Cannot extract Reservation Model from payload");			
			sendError(response, ":[");
			return null;
		}
		try{
			FDIdentity identity = new FDIdentity(rsvModel.getCustomerModel().getErpCustomerPK(), rsvModel.getCustomerModel().getFdCustomerPK());
			FDUserI user = FDCustomerManager.getFDUser(identity);
			if (identity != null) {
				List<FDReservation> rsvList = FDDeliveryManager.getInstance().getReservationsForCustomer(identity.getErpCustomerPK());
				for ( FDReservation rsv : rsvList ) {
					if (EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
						user.getShoppingCart().setDeliveryReservation(rsv);
					} else {
						user.setReservation(rsv);
					}
				}
			}
			FDCartModel cart = user.getShoppingCart();
			FDReservation userReservation = user.getReservation();
			String zoneId = null;
			if(cart!=null && cart.getZoneInfo()!=null)
				zoneId = cart.getZoneInfo().getZoneId();
			
			TimeslotEventModel event = new TimeslotEventModel(EnumTransactionSource.SYSTEM.getCode(), (cart != null) ? cart.isDlvPassApplied() : false, (cart != null) ? cart.getDeliverySurcharge() : 0.00,
					(cart != null) ? cart.isDeliveryChargeWaived() : false, Util.isZoneCtActive(zoneId), user.getPrimaryKey());
		
			FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(rsvModel.getTimeSlotId(), true);
			
			if(userReservation != null){
				ErpAddressModel address = getAddress(user.getIdentity(),userReservation.getAddressId());
				FDDeliveryManager.getInstance().removeReservation(userReservation.getPK().getId(),address, event);
			}
			FDActionInfo info = getActionInfo(agent);
			info.setIdentity(identity);
			FDReservation reservation = null;
			try{
				reservation = FDCustomerManager.makeReservation(identity, 
						timeslot, EnumReservationType.ONETIME_RESERVATION, rsvModel.getAddressId(), info, user.isChefsTable(), event, false);				
			} catch (ReservationUnavailableException re ) {
				// no more capacity in this timeslot
				LOGGER.info( "No more capacity in timeslot: " + timeslot.toString(), null );
				reservation = FDCustomerManager.makeReservation(identity, 
						timeslot, EnumReservationType.ONETIME_RESERVATION, rsvModel.getAddressId(), info, user.isChefsTable(), event, true);				
			
			} catch (ReservationException e){
				// no more capacity in this timeslot
				LOGGER.info( "No more capacity in timeslot: " + timeslot.toString(), null );
				reservation = FDCustomerManager.makeReservation(identity, 
						timeslot, EnumReservationType.ONETIME_RESERVATION, rsvModel.getAddressId(), info, user.isChefsTable(), event, true);
			}
			return reservation != null ? reservation.getId().toString() : null;
			
		} catch (FDResourceException fe) {
			fe.printStackTrace();
			LOGGER.error("System Error occurred while creating Reservation : " + rsvModel.getCustomerModel().getErpCustomerPK() + "\n" + fe.getMessage());
			return null;
		} catch (ReservationException te) {
			LOGGER.error("Transaction Error occurred while creating Reservation : " + rsvModel.getCustomerModel().getErpCustomerPK() + "\n" + te.getMessage());
			return null;
		} catch (FDAuthenticationException e) {
			LOGGER.error("Transaction Error occurred while Identifying User : " + rsvModel.getCustomerModel().getErpCustomerPK() + "\n" + e.getMessage());
			return null;
		}
	}

	private boolean processCancelOrder(HttpServletRequest request,
			HttpServletResponse response, String agent, String payload, boolean sendEmail) {
		boolean success = false;
		String orderId = payload;//extractOrderPayload(payload);
		if (orderId == null) {
			LOGGER.error("Cannot extract order model from payload");
			// no payload
			sendError(response, ":[");
			return false;
		}
		
		FDCustomerOrderInfo orderInfo = new FDCustomerOrderInfo();
		orderInfo.setSaleId(orderId);
				
		// Assume everything is fine...
		success = this.cancelOrder(orderInfo, request, agent, sendEmail);
		return success;
	}
	
	protected FDActionInfo getActionInfo(String agent){
		
		EnumTransactionSource src = EnumTransactionSource.SYSTEM;
		CrmAgentModel crmAgent = new CrmAgentModel();
		crmAgent.setLdapId(agent);
		FDActionInfo info = new FDActionInfo(src, null, agent, null, crmAgent, null);
		
		return info;
	}
	
	private ErpAddressModel getAddress(FDIdentity identity,String id) throws FDResourceException {
		
		Collection<ErpAddressModel> addressList= FDCustomerManager.getShipToAddresses(identity);
		for (ErpAddressModel address : addressList) {
			if(address.getId().equals(id))
				return address;
		}
		return null;
	}
	
	protected boolean cancelOrder(FDCustomerOrderInfo orderModel, HttpServletRequest request, String agent, boolean sendEmail) {
		boolean success = true;
		try {
			
			FDOrderI order = FDCustomerManager.getOrder(orderModel.getSaleId());
			if(!EnumSaleStatus.CANCELED.equals(order.getOrderStatus())){
				FDIdentity identity = new FDIdentity(order.getCustomerId(), null);		
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
		
	protected void sendError(HttpServletResponse response, String message) {
		try {
			response.getWriter().append(message);
			response.setStatus(500);
		} catch (IOException e) {
			LOGGER.error("Failed to append message to response", e);
		}
	}

	private int processCancelReservation(HttpServletRequest request,
			HttpServletResponse response, String userAgent, String payload) {
		
		Set<String> models = extractRsvCancelPayload(payload);
		if (models == null) {
			LOGGER.error("Cannot extract reservation models from payload");
			// no payload
			sendError(response, ":[");
			return 0;
		}
		
		int updateCount = 0;
		try {
			updateCount = CallCenterServices.cancelReservations(models, userAgent);
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error raised during reservation cancel process ", e);
			e.printStackTrace();
			sendError(response, e.toString());
		}
		
		return updateCount;
	}
	
	/**
	 * Unserialize Rsv Id's from JSON string
	 * 
	 * @param payload serialized form of reservations
	 * @return List of rsv Id's
	 */
	@SuppressWarnings("unchecked")
	protected Set<String> extractRsvCancelPayload(String rsvCancelPayload) {
		
		Set models = null;
		try {
			models = (Set) serializer.fromJSON(rsvCancelPayload);
		} catch (ClassCastException e) {
			LOGGER.error("Error raised during Reservation deserialization ", e);
			return null;
		} catch (UnmarshallException e) {
			LOGGER.error("Error raised during Reservation deserialization ", e);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error raised during Reservation deserialization ", e);
			return null;
		}		
		return models;
	}
	
	protected IReservationModel extractRsvPayload(String rsvPayload) {
		
		IReservationModel orderModel = null;
		try {
			orderModel = (IReservationModel) serializer.fromJSON(rsvPayload);
		} catch (ClassCastException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (UnmarshallException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		}
		
		return orderModel;
	}
	
	protected StandingOrderModel extractStandingOrderPayload(String soPayload) {
		
		StandingOrderModel model = null;
		try {
			model = (StandingOrderModel) serializer.fromJSON(soPayload);
		} catch (ClassCastException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (UnmarshallException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		}
		
		return model;
	}
	
	protected IGenericSearchModel extractDatePayload(String payload) {
		
		IGenericSearchModel model = null;
		try {
			model = (IGenericSearchModel) serializer.fromJSON(payload);
		} catch (ClassCastException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (UnmarshallException e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		} catch (Exception e) {
			LOGGER.error("Error raised during create Reservation deserialization", e);
			return null;
		}
		return model;
	}
}
