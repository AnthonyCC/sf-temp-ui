package com.freshdirect.webapp.action.fdstore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.Util;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.util.CTDeliveryCapacityLogic;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;


public class ChooseTimeslotAction extends WebActionSupport {

	private static final long	serialVersionUID	= 7329584900713977234L;

	/** Length of reservations in msecs */
	private final static long RESERVATION_MILLISECONDS = 45 * 60 * 1000; // 45 minutes

	private static Category LOGGER = LoggerFactory.getInstance(ChooseTimeslotAction.class);

	@Override
	public String execute() throws Exception {
		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();

		boolean isForced = false;
		String deliveryTimeSlotId = request.getParameter("deliveryTimeslotId");
		if (deliveryTimeSlotId == null) {
			this.addError("deliveryTime", "You must select a delivery timeslot. Please select one from below or contact Us for help.");
			return ERROR;
		}

		if(deliveryTimeSlotId.startsWith("f_")) {
			deliveryTimeSlotId = deliveryTimeSlotId.replaceAll("f_", "");
			isForced = true;
		}
		boolean chefsTable = user.isChefsTable() || "true".equals(request.getParameter("chefstable"));

		FDTimeslot timeSlot = FDDeliveryManager.getInstance().getTimeslotsById(deliveryTimeSlotId);
        String ctDeliveryProfile=CTDeliveryCapacityLogic.isEligible(user,timeSlot);
        if(timeSlot.getBaseAvailable()>0||chefsTable)
        {
        	ctDeliveryProfile=null;
        }
		if(timeSlot.getBaseAvailable()<=0&&ctDeliveryProfile!=null)
		{
			chefsTable=true;			
		}		
		
		ErpAddressModel erpAddress = cart.getDeliveryAddress();
		String addressId = "";
		if (!(erpAddress instanceof ErpDepotAddressModel)) {
			DlvZoneInfoModel zoneInfo = AddressUtil.getZoneInfo(request, erpAddress, this.getResult(), timeSlot.getBegDateTime());
			cart.setZoneInfo(zoneInfo);
			addressId = erpAddress.getPK().getId();
		} else {
			addressId = ((ErpDepotAddressModel) erpAddress).getLocationId();
		}

		TimeslotEventModel event = new TimeslotEventModel(user.getApplication().getCode(),cart.isDlvPassApplied(),
				cart.getDeliverySurcharge(), cart.isDeliveryChargeWaived(), Util.isZoneCtActive(cart.getZoneInfo().getZoneId()));
		
		try {
			FDReservation dlvRsv = cart.getDeliveryReservation();

			FDReservation advRsv = user.getReservation();
			if (advRsv != null && deliveryTimeSlotId.equals(advRsv.getTimeslotId()) && advRsv.getAddressId().equals(addressId)) {
				if (dlvRsv != null && !dlvRsv.getPK().equals(advRsv.getPK())) {
					try {
						FDDeliveryManager.getInstance().releaseReservation(dlvRsv.getPK().getId(),erpAddress, event);
					} catch (FDResourceException fdre) {
						LOGGER.warn("Error releasing reservation", fdre);
					}
				}
				
				if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
					setDeliveryTimeslot(session, advRsv);
				} else {
					setSODeliveryTimeslot(session, advRsv);
				}
				return SUCCESS;
			}

			if (dlvRsv == null || !deliveryTimeSlotId.equals(dlvRsv.getTimeslotId())
										|| (addressId != null && !addressId.equals(dlvRsv.getAddressId()))) {
				// new reservation or different timeslot selected

				if (dlvRsv != null
						&& !(cart instanceof FDModifyCartModel)
						&& EnumReservationType.STANDARD_RESERVATION
								.equals(dlvRsv.getReservationType())) {
					// release prev reservation, unless it's a modify order
					String prevResrvId = dlvRsv.getPK().getId();

					try {
						LOGGER.debug("releasing previous reservation of id=" + prevResrvId);
						FDDeliveryManager.getInstance().releaseReservation(prevResrvId,erpAddress, event);
					} catch (FDResourceException fdre) {
						LOGGER.warn("Error releasing reservation", fdre);
					}
				}

				// reserve the new slot
				LOGGER.debug("Attempting to reserve timeslot, with CT = " + chefsTable);
				String custId = user.getIdentity().getErpCustomerPK();
				FDReservation timeSlotResrv =
					FDDeliveryManager.getInstance().reserveTimeslot(
						timeSlot,
						custId,
						RESERVATION_MILLISECONDS,
						EnumReservationType.STANDARD_RESERVATION,
						erpAddress,
						chefsTable,
						ctDeliveryProfile, isForced,event);

				if (EnumCheckoutMode.NORMAL == user.getCheckoutMode()) {
					setDeliveryTimeslot(session, timeSlotResrv);
				} else {
					setSODeliveryTimeslot(session, timeSlotResrv);
				}
			}

			return this.getResult().isSuccess() ? SUCCESS : ERROR;

		} catch (ReservationUnavailableException re) {
			this.addError("technical_difficulty", SystemMessageList.MSG_CHECKOUT_TIMESLOT_NA);
			return ERROR;
		} catch (ReservationException re) {
			throw new FDResourceException(re);
		}
	}

	/**
	 * @param session
	 * @param resv
	 */
	private void setDeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		final FDCartModel cart = user.getShoppingCart();

		cart.setDeliveryReservation(resv);
		user.setShoppingCart(cart);
		session.setAttribute(SessionName.USER, user);
	}

	private void setSODeliveryTimeslot(HttpSession session, FDReservation resv) {
		final FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		this.setDeliveryTimeslot(session, resv);
		
		FDStandingOrder so = user.getCurrentStandingOrder();
		so.setupDelivery(resv);
	}
}
