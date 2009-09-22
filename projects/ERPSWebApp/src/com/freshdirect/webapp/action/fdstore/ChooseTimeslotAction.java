package com.freshdirect.webapp.action.fdstore;
/*
 * Created on Apr 30, 2003
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.util.CTDeliveryCapacityLogic;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AddressUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

/**
 * @author knadeem
 */

public class ChooseTimeslotAction extends WebActionSupport {

	/** Length of reservations in msecs */
	private final static long RESERVATION_MILLISECONDS = 45 * 60 * 1000; // 45 minutes

	private static Category LOGGER = LoggerFactory.getInstance(ChooseTimeslotAction.class);

	public String execute() throws Exception {
		return this.doExecute();
	}

	protected String doExecute() throws FDResourceException {

		HttpSession session = this.getWebActionContext().getSession();
		HttpServletRequest request = this.getWebActionContext().getRequest();

		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		FDCartModel cart = user.getShoppingCart();

		String deliveryTimeSlotId = request.getParameter("deliveryTimeslotId");
		if (deliveryTimeSlotId == null) {
			this.addError("deliveryTime", "Please select a delivery time.");
			return ERROR;
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
			/*ErpDepotAddressModel depotAddress = (ErpDepotAddressModel)erpAddress;
			if(depotAddress.isPickup()){
				String pickupAgreement = request.getParameter("pickup_agreement");
				if(pickupAgreement == null){
					this.addError("pickup_didnot_agree", SystemMessageList.MSG_CHECKOUT_PICKUP_DIDNOT_AGREE);
					return ERROR;
				}
				session.setAttribute(SessionName.PICKUP_AGREEMENT, pickupAgreement);
				String authorizedPeople = request.getParameter("authorized_people");
				if(authorizedPeople != null && authorizedPeople.length() > 1){
					erpAddress.setInstructions(authorizedPeople.substring(0, Math.min(authorizedPeople.length(), 88)));
					session.setAttribute(SessionName.AUTHORIZED_PEOPLE, authorizedPeople);
				}
			}*/
		}

		try {
			FDReservation dlvRsv = cart.getDeliveryReservation();

			FDReservation advRsv = user.getReservation();
			if (advRsv != null && deliveryTimeSlotId.equals(advRsv.getTimeslotId()) && advRsv.getAddressId().equals(addressId)) {
				if (dlvRsv != null && !dlvRsv.getPK().equals(advRsv.getPK())) {
					try {
						FDDeliveryManager.getInstance().releaseReservation(dlvRsv.getPK().getId(),erpAddress);
					} catch (FDResourceException fdre) {
						LOGGER.warn("Error releasing reservation", fdre);
					}
				}
				cart.setDeliveryReservation(advRsv);
				user.setShoppingCart(cart);
				session.setAttribute(SessionName.USER, user);
				return SUCCESS;
			}

			if (dlvRsv == null || !deliveryTimeSlotId.equals(dlvRsv.getTimeslotId())) {
				// new reservation or different timeslot selected

				if (dlvRsv != null
						&& !(cart instanceof FDModifyCartModel)
						&& EnumReservationType.STANDARD_RESERVATION
								.equals(dlvRsv.getReservationType())) {
					// release prev reservation, unless it's a modify order
					String prevResrvId = dlvRsv.getPK().getId();

					try {
						LOGGER.debug("releasing previous reservation of id=" + prevResrvId);
						FDDeliveryManager.getInstance().releaseReservation(prevResrvId,erpAddress);
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
						ctDeliveryProfile);

				// save reservation id in cart
				cart.setDeliveryReservation(timeSlotResrv);

				user.setShoppingCart(cart);
				session.setAttribute(SessionName.USER, user);
			}

			return this.getResult().isSuccess() ? SUCCESS : ERROR;

		} catch (ReservationUnavailableException re) {
			this.addError("technical_difficulty", SystemMessageList.MSG_CHECKOUT_TIMESLOT_NA);
			return ERROR;
		} catch (ReservationException re) {
			throw new FDResourceException(re);
		}
	}
}
