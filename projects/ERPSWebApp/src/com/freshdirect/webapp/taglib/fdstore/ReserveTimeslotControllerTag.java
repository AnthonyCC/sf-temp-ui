package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.Util;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class ReserveTimeslotControllerTag extends AbstractControllerTag {

	private String timeslotId;
	private EnumReservationType rsvType;
	private String addressId;
	private boolean chefstable;

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {

		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		
		FDReservation reservation = user.getReservation();
		FDCartModel cart = user.getShoppingCart();
		String zoneId = null;
		if(cart!=null && cart.getZoneInfo()!=null)
			zoneId = cart.getZoneInfo().getZoneId();
		TimeslotEventModel event = new TimeslotEventModel((user.getApplication()!=null)?user.getApplication().getCode():"",
				(cart!=null)?cart.isDlvPassApplied():false, (cart!=null)?cart.getDeliverySurcharge():0.00,
						(cart!=null)?cart.isDeliveryChargeWaived():false, Util.isZoneCtActive(zoneId));
	

		try {
			String action = this.getActionName();
			if ("reserveTimeslot".equals(action)) {
				this.populate(request);
				this.validate(actionResult);
				if (actionResult.isFailure()) {
					return true;
				}
				FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(timeslotId, false);
				
				if(reservation != null && !reservation.isAnonymous()){
					this.changeReservation(user, reservation, timeslot, event);
				}else{
					this.reserveTimeslot(user, timeslot, AccountActivityUtil.getActionInfo(session), event);
				}
				session.setAttribute(SessionName.USER, user);
			}
			
			if("updateWeeklyReservation".equals(action)) {
				this.populate(request);
				this.validate(actionResult);
				if(actionResult.isFailure()){
					return true;
				}
				FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(timeslotId, false);
				FDCustomerManager.updateWeeklyReservation(user.getIdentity(), timeslot, addressId, AccountActivityUtil.getActionInfo(session));
			}

			if ("changeReservation".equals(action)) {
				this.populate(request);
				this.validate(actionResult);
				if (actionResult.isFailure()) {
					return true;
				}
				if(reservation != null){
					ErpAddressModel address=getAddress(user.getIdentity(),reservation.getAddressId());
					FDDeliveryManager.getInstance().removeReservation(reservation.getPK().getId(),address, event);
				}
				FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(timeslotId, false);
				this.reserveTimeslot(user, timeslot, AccountActivityUtil.getActionInfo(session), event);
				session.setAttribute(SessionName.USER, user);

			}

			if ("cancelReservation".equals(action)) {
				this.populate(request);
				this.validate(actionResult);
				if (actionResult.isFailure()) {
					return true;
				}
				FDCustomerManager.cancelReservation(user.getIdentity(), reservation, this.rsvType, AccountActivityUtil.getActionInfo(session), event);
				user.setReservation(null);
				session.setAttribute(SessionName.USER, user);
			}
		} catch (FDResourceException e) {
			actionResult.addError(true, "technical_difficulty", "We are experiencing technical difficulties, please try later.");
		} catch (ReservationException e) {
			actionResult.addError(true, "reservation", e.getMessage());
		}

		return true;
	}
	
	private void changeReservation(FDUserI user, FDReservation reservation, FDTimeslot timeslot, TimeslotEventModel event) throws FDResourceException, ReservationException {
		FDActionInfo aInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, user.getIdentity(), "SYSTEM", "Changed Pre-Reservation", null);
		FDReservation rsv = FDCustomerManager.changeReservation(user.getIdentity(), reservation, timeslot, this.rsvType, this.addressId, aInfo, chefstable, event);
		user.setReservation(rsv);
	}

	private void populate(HttpServletRequest request) {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
				
		this.timeslotId = NVL.apply(request.getParameter("deliveryTimeslotId"), "");
		this.addressId = NVL.apply(request.getParameter("addressId"), "");
		this.rsvType = EnumReservationType.getEnum(NVL.apply(request.getParameter("reservationType"), ""));
		try {
			this.chefstable = user.isChefsTable()||"true".equals(request.getParameter("chefstable"));
		} catch (FDResourceException e) {
			e.printStackTrace();
		}
	}

	private void validate(ActionResult actionResult) {
		if ("".equals(timeslotId)) {
			actionResult.addError(true, "deliveryTime", "You must select a timeslot to reserve. Please select one from below or contact us for help.");
		}

		if (rsvType == null) {
			actionResult.addError(true, "reservationType", "Please select type of reservation.");
		}

		if (actionResult.isSuccess() && EnumReservationType.RECURRING_RESERVATION.equals(rsvType) && "".equals(addressId)) {
			actionResult.addError(true, "addressId", "Missing Address Id");
		}
	}

	private void reserveTimeslot(FDUserI user, FDTimeslot timeslot, FDActionInfo aInfo, TimeslotEventModel event) throws FDResourceException, ReservationException {
		FDReservation rsv = FDCustomerManager.makeReservation(user.getIdentity(), timeslot, this.rsvType, this.addressId, aInfo, chefstable, event, false);
		user.setReservation(rsv);
	}
	
	
	private ErpAddressModel getAddress(FDIdentity identity,String id) throws FDResourceException {
		
		Collection<ErpAddressModel> addressList= FDCustomerManager.getShipToAddresses(identity);
		for (ErpAddressModel address : addressList) {
			if(address.getId().equals(id))
				return address;
		}
		return null;
	}
	
	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if ("extendReservation".equals(this.getActionName())) {
			throw new JspException(new StringBuilder().append("Action ").append(this.getActionName()).append(" is not supported.").toString());
			/* APPDEV-593 Remove support for extend reservation.
			 *
			HttpSession session = request.getSession();
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
			FDReservation reservation = user.getReservation();

			try {
				reservation = FDDeliveryManager.getInstance().extendReservation(reservation);
				
			} catch (FDResourceException e) {
				throw new JspException(e);
			}

			 user.setReservation(reservation);
			session.setAttribute(SessionName.USER, user);
			if (this.getSuccessPage() != null) {
				this.redirectTo(this.getSuccessPage());
			}*/
		}
		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
