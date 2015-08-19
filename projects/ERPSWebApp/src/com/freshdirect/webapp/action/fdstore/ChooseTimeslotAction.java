package com.freshdirect.webapp.action.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

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
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.TimeslotLogic;
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

	public static ActionResult reserveDeliveryTimeSlot(HttpSession session, String deliveryTimeSlotId, String chefsTableValue, ActionResult actionResult) throws ReservationException, FDResourceException {
		if (actionResult == null) {
			actionResult = new ActionResult();
		}
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDUserI dpTcCheckUser = (FDUserI)session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();
		boolean isForced = false;
		if (deliveryTimeSlotId == null) {
			actionResult.addError(new ActionError("deliveryTime", "You must select a delivery timeslot. Please select one from below or contact Us for help."));
		} else {
		if(deliveryTimeSlotId.startsWith("f_")) {
			deliveryTimeSlotId = deliveryTimeSlotId.replaceAll("f_", "");
			isForced = true;
		}
			boolean chefsTable = user.isChefsTable() || "true".equals(chefsTableValue);
		FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(deliveryTimeSlotId, cart.getDeliveryAddress().getBuildingId(), true);
		ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(user.getUser().getIdentity());
		if (timeSlot.isPremiumSlot() && dpTcCheckUser.isDpNewTcBlocking(false) && cm.getDpTcViewCount() <= FDStoreProperties.getDpTcViewLimit()) {
			//user bypassed dp terms block
				actionResult.addError(new ActionError("bypassedDpTcBlock", "You must agree to the new DeliveryPass Terms & Conditions before selecting a Same Day time slot."));
			} else {
		ErpAddressModel erpAddress = cart.getDeliveryAddress();
		String addressId = "";
		if (!(erpAddress instanceof ErpDepotAddressModel)) {
			FDDeliveryZoneInfo zoneInfo = AddressUtil.getZoneInfo(user, erpAddress, actionResult, timeSlot.getStartDateTime(), user.getHistoricOrderSize(), timeSlot.getRegionSvcType());
			cart.setZoneInfo(zoneInfo);
			addressId = erpAddress.getPK().getId();
		} else {
			addressId = ((ErpDepotAddressModel) erpAddress).getLocationId();
		}
		String zoneId = null;
				if (cart != null && cart.getZoneInfo() != null) {
			zoneId = cart.getZoneInfo().getZoneId();
		
		TimeslotEvent event = new TimeslotEvent((user.getApplication()!=null)?user.getApplication().getCode():"",
				cart.isDlvPassApplied(),cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(), 
				(cart.getZoneInfo()!=null)?cart.getZoneInfo().isCtActive():false, user.getPrimaryKey(),EnumCompanyCode.fd.name());
		
		try {
			FDReservation dlvRsv = cart.getDeliveryReservation();
			FDReservation advRsv = user.getReservation();
			if (advRsv != null && deliveryTimeSlotId.equals(advRsv.getTimeslotId()) && advRsv.getAddressId().equals(addressId)) {
				if (dlvRsv != null && !dlvRsv.getPK().equals(advRsv.getPK())) {
					try {
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
						if (dlvRsv == null || !deliveryTimeSlotId.equals(dlvRsv.getTimeslotId()) || (addressId != null && !addressId.equals(dlvRsv.getAddressId()))) {
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
				boolean hasSteeringDiscount = false;
				if(user.getSteeringSlotIds().contains(timeSlot.getId())){
					hasSteeringDiscount = true;
				}
				// reserve the new slot
				LOGGER.debug("Attempting to reserve timeslot, with CT = " + chefsTable);
				String custId = user.getIdentity().getErpCustomerPK();
				FDReservation timeSlotResrv =
					FDDeliveryManager.getInstance().reserveTimeslot(
						timeSlot.getId(),
						custId,
						EnumReservationType.STANDARD_RESERVATION,
						TimeslotLogic.encodeCustomer(erpAddress, user),
						chefsTable,
						null, isForced,event, hasSteeringDiscount);
				TimeslotLogic.applyOrderMinimum(user,timeSlotResrv.getTimeslot());
				if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
					setDeliveryTimeslot(session, timeSlotResrv);
				} else {
					setSODeliveryTimeslot(session, timeSlotResrv);
				}
				LOGGER.info(">>RESERVE TIMESLOT AND SET IT IN CART "+timeSlotResrv);
			}
					}
		} catch (ReservationUnavailableException re) {
					actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_CHECKOUT_TIMESLOT_NA));
		}
			}
		}	
		
		}
		return actionResult;
	}

	/**
	 * @param session
	 * @param resv
	 */
	private static void setDeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		final FDCartModel cart = user.getShoppingCart();
		cart.setDeliveryReservation(resv);
		user.setShoppingCart(cart);
		session.setAttribute(SessionName.USER, user);
	}

	private static void setSODeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		setDeliveryTimeslot(session, resv);
		FDStandingOrder so = user.getCurrentStandingOrder();
		so.setupDelivery(resv);
	}
}
