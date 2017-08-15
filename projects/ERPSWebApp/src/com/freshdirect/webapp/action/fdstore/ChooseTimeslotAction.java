package com.freshdirect.webapp.action.fdstore;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.TimeslotLogic;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.delivery.model.EnumCompanyCode;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class ChooseTimeslotAction extends WebActionSupport {

	private static final long	serialVersionUID	= 7329584900713977234L;

	/** Length of reservations in msecs */
	private final static long RESERVATION_MILLISECONDS = 45 * 60 * 1000; // 45
																			// minutes

	private static Category LOGGER = LoggerFactory.getInstance(ChooseTimeslotAction.class);

	@Override
	public String execute() throws Exception {
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();
		String deliveryTimeSlotId = request.getParameter("deliveryTimeslotId");
		String chefsTableValue = request.getParameter("chefstable");
		reserveDeliveryTimeSlot(session, deliveryTimeSlotId, chefsTableValue, getResult());
		return this.getResult().isSuccess() ? SUCCESS : ERROR;
	}

	public static ActionResult reserveDeliveryTimeSlot(HttpSession session, String deliveryTimeSlotId, String chefsTableValue, ActionResult actionResult, String soNextDeliveryDate) throws ReservationException, FDResourceException {
		if (actionResult == null) {
			actionResult = new ActionResult();
		}
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDUserI dpTcCheckUser = (FDUserI)session.getAttribute(SessionName.USER);
		FDDeliveryZoneInfo previousZone = null;
		FDCartModel cart = StandingOrderHelper.isSO3StandingOrder(user)? user.getSoTemplateCart()
				:user.getShoppingCart();
		ErpAddressModel erpAddress=StandingOrderHelper.isSO3StandingOrder(user)?user.getCurrentStandingOrder().getDeliveryAddress():cart.getDeliveryAddress();
		
		if(StandingOrderHelper.isEligibleForSo3_0(user) && null !=user.getCurrentStandingOrder() && null != soNextDeliveryDate && !"".equals(soNextDeliveryDate.trim())){        		
			try {
				user.getCurrentStandingOrder().setNextDeliveryDate(DateUtil.parseMDY(soNextDeliveryDate));
			} catch (ParseException e) {
				LOGGER.warn("ParseException while parsing soNextDeliveryDate:"+soNextDeliveryDate,e);				
			}
		}
		   boolean isForced=false;
			final MasqueradeContext masqueradeContext = user.getMasqueradeContext();
			if(masqueradeContext!=null && masqueradeContext.getParentOrderId()!=null) {
				 isForced = true;
			}
			
			if (deliveryTimeSlotId == null) {
				actionResult.addError(new ActionError("deliveryTime", "You must select a delivery timeslot. Please select one from below or contact Us for help."));
			} else if(erpAddress == null) {
				actionResult.addError(new ActionError("deliveryAddress", "You must select a address."));
			} else {
				final boolean hasForcePrefix = deliveryTimeSlotId.startsWith("f_");
				if (masqueradeContext != null) {
					// update masq context
					masqueradeContext.setForceOrderEnabled(hasForcePrefix);
				}
	
				if(hasForcePrefix) {
					deliveryTimeSlotId = deliveryTimeSlotId.replaceAll("f_", "");
					isForced = true;
				}
				boolean chefsTable = user.isChefsTable() || "true".equals(chefsTableValue);
				FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(deliveryTimeSlotId, erpAddress.getBuildingId(), true);
				if(FDStoreProperties.isDlvFeeTierEnabled()){
					TimeslotLogic.calcTieredDeliveryFee(user, timeSlot);
				}
				
				ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(user.getUser().getIdentity());
				if (timeSlot.isPremiumSlot() && dpTcCheckUser.isDpNewTcBlocking(false) && cm.getDpTcViewCount() <= FDStoreProperties.getDpTcViewLimit()) {
					//user bypassed dp terms block
						actionResult.addError(new ActionError("bypassedDpTcBlock", "You must agree to the new DeliveryPass Terms & Conditions before selecting a Same Day time slot."));
					} else {
				String addressId = "";
				if (!(erpAddress instanceof ErpDepotAddressModel)) {
					FDDeliveryZoneInfo zoneInfo = timeSlot.getZoneInfo();
					
					if(zoneInfo == null 
							|| StringUtils.isEmpty(zoneInfo.getZoneId()) 
							|| FDStoreProperties.isRefreshZoneInfoEnabled()){
						zoneInfo = AddressUtil.getZoneInfo(user, erpAddress, actionResult, timeSlot.getStartDateTime(), user.getHistoricOrderSize(), timeSlot.getRegionSvcType());
					}
					previousZone = cart.getZoneInfo();
					cart.setZoneInfo(zoneInfo);
					addressId = erpAddress.getPK().getId();
				} else {
					addressId = ((ErpDepotAddressModel) erpAddress).getLocationId();
				}
				String zoneId = null;
						if (cart != null && cart.getZoneInfo() != null) {
					zoneId = cart.getZoneInfo().getZoneId();
				
				String applicationId = (user.getApplication()!=null)?user.getApplication().getCode():"";
				if(masqueradeContext!=null){
						applicationId = "CSR";
				}
				TimeslotEvent event = new TimeslotEvent(applicationId, 
						cart.isDlvPassApplied(),cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(), 
						(cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(),EnumCompanyCode.fd.name());
				
				try {
					FDReservation dlvRsv = cart.getDeliveryReservation();
					FDReservation advRsv = user.getReservation();
					boolean hasSteeringDiscount = false;
					if (advRsv != null && deliveryTimeSlotId.equals(advRsv.getTimeslotId()) && advRsv.getAddressId().equals(addressId)) {
						if (dlvRsv != null && !dlvRsv.getPK().equals(advRsv.getPK())) {
							try {
								LOGGER.info("releaseReservation by ID: " + dlvRsv.getPK().getId());
								FDDeliveryManager.getInstance().releaseReservation(dlvRsv.getPK().getId(),erpAddress, event, true);
							} catch (FDResourceException fdre) {
								LOGGER.warn("Error releasing reservation", fdre);
							}
						}
						if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
							setDeliveryTimeslot(session, advRsv);
						} else {
							setSODeliveryTimeslot(session, advRsv);
						}
						
						LOGGER.info(">>CANCEL STANDARD RESERVATION IN CART AND KEEP THE ONE TIME RESERVATION "+advRsv);
							} else {
								if (dlvRsv == null || !deliveryTimeSlotId.equals(dlvRsv.getTimeslotId()) || (TimeslotLogic.isAddressChange(dlvRsv.getAddress(), erpAddress, addressId, dlvRsv.getAddressId()))) {
						// new reservation or different timeslot selected
									if (dlvRsv != null && !(cart instanceof FDModifyCartModel) && EnumReservationType.STANDARD_RESERVATION.equals(dlvRsv.getReservationType())) {
										// release prev reservation, unless it's a
										// modify order
							String prevResrvId = dlvRsv.getPK().getId();
							try {
								LOGGER.debug("releasing previous reservation of id=" + prevResrvId);
								FDDeliveryManager.getInstance().releaseReservation(prevResrvId,erpAddress, event, true);
							} catch (FDResourceException fdre) {
								LOGGER.warn("Error releasing reservation", fdre);
							}
						}
						
						if(user.getSteeringSlotIds().contains(timeSlot.getId())){
							hasSteeringDiscount = true;
						}
						// reserve the new slot
						LOGGER.debug("Attempting to reserve timeslot, with CT = " + chefsTable);
						
						reserveTimeslot(user, timeSlot, erpAddress, chefsTable, isForced, hasSteeringDiscount, event, session);
						
					}
					}
					
					call_zoneswitch_logic( previousZone, cart, user, erpAddress);
					
				}catch (ReservationUnavailableException re) {
							actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_CHECKOUT_TIMESLOT_NA));
				}catch (ReservationException re) {
					actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_CHECKOUT_TIMESLOT_NA));
				}
					}
				}	
			
			} 
		return actionResult;
	}
	
	
	/**
	 * APPDEV 6131 - FDC Transition
	 * 
	 * @param previousZone - previous zone from the cart
	 * @param cart
	 * @param user
	 * @param erpAddress
	 * @throws FDResourceException
	 */
	private static void call_zoneswitch_logic(FDDeliveryZoneInfo previousZone,
			FDCartModel cart, FDSessionUser user, ErpAddressModel erpAddress)
			throws FDResourceException {
		boolean isZoneInfoNotMatched = ((previousZone != null
				&& cart.getZoneInfo() != null && !previousZone.getZoneCode()
				.equals(cart.getZoneInfo().getZoneCode())) || (previousZone == null && cart.getZoneInfo() != null));
		
		String oldPlant = null, newPlant = null;
		oldPlant = (cart != null && cart.getDeliveryPlantInfo() != null && cart.getDeliveryPlantInfo().getPlantId() != null)? cart.getDeliveryPlantInfo().getPlantId() : null;
		newPlant = (cart.getZoneInfo() != null && cart.getZoneInfo().getFulfillmentInfo() != null && cart.getZoneInfo().getFulfillmentInfo().getPlantCode() != null)? cart.getZoneInfo().getFulfillmentInfo().getPlantCode() : null;
		
		
		
		isZoneInfoNotMatched = isZoneInfoNotMatched || (  oldPlant ==null || newPlant == null || (oldPlant!=null && newPlant!=null && !oldPlant.equalsIgnoreCase(newPlant)) );
		
		if ((isZoneInfoNotMatched)|| (null == cart.getDeliveryAddress() && erpAddress != null)) {
			cart.setDeliveryAddress(erpAddress);
			user.setAddress(erpAddress);
			user.setZPServiceType(erpAddress.getServiceType());
			user.resetUserContext();
			cart.setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user));
			if (!cart.isEmpty()) {
				for (FDCartLineI cartLine : cart.getOrderLines()) {
					cartLine.setUserContext(user.getUserContext());
					cartLine.setFDGroup(null);//clear the group
				}
			}
			try {
				cart.refreshAll(true);
			} catch (FDInvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	private static void reserveTimeslot(FDSessionUser user,
			FDTimeslot timeSlot, ErpAddressModel erpAddress,
			boolean chefsTable, boolean isForced, boolean hasSteeringDiscount,
			TimeslotEvent event, HttpSession session) throws FDResourceException, ReservationException {
		String custId = user.getIdentity().getErpCustomerPK();
		FDReservation timeSlotResrv =
				FDDeliveryManager.getInstance().reserveTimeslot(
					timeSlot.getId(),
					custId,
					EnumReservationType.STANDARD_RESERVATION,
					TimeslotLogic.encodeCustomer(erpAddress, user),
					chefsTable,
					null, isForced,event, hasSteeringDiscount, (timeSlot.getDlvfeeTier()!=null)?timeSlot.getDlvfeeTier().name():null);
			TimeslotLogic.applyOrderMinimum(user,timeSlotResrv.getTimeslot());
			if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
				setDeliveryTimeslot(session, timeSlotResrv);
			} else {
				setSODeliveryTimeslot(session, timeSlotResrv);
			}
			LOGGER.info(">>RESERVE TIMESLOT AND SET IT IN CART "+timeSlotResrv);
	}
	
	public static ActionResult reserveDeliveryTimeSlot(HttpSession session, String deliveryTimeSlotId, String chefsTableValue, ActionResult actionResult) throws ReservationException, FDResourceException{
		return reserveDeliveryTimeSlot(session,deliveryTimeSlotId, chefsTableValue, actionResult, null);
	}

	/**
	 * @param session
	 * @param resv
	 */
	private static void setDeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		final FDCartModel cart = StandingOrderHelper.isSO3StandingOrder(user)? user.getSoTemplateCart():user.getShoppingCart();
		cart.setDeliveryReservation(resv);
		if(StandingOrderHelper.isSO3StandingOrder(user)) {
			user.setSoTemplateCart(cart);
		}else{
			user.setShoppingCart(cart);
		}
		session.setAttribute(SessionName.USER, user);
	}

	private static void setSODeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		setDeliveryTimeslot(session, resv);
		FDStandingOrder so = user.getCurrentStandingOrder();
		if(user.isNewSO3Enabled()){
			so.setupDelivery(resv,false);
		}else{
			so.setupDelivery(resv);
		}
	}
}
