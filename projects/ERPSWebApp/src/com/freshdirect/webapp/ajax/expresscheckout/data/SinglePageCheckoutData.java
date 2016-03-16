package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartData.Section;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;
import com.freshdirect.webapp.ajax.standingorder.StandingOrderResponseData;

public class SinglePageCheckoutData {

	private String errorMessage;
	private List<Section> cartSections;
	private UnavailabilityData atpFailure;
	private Map<String, List<DrawerData>> drawer;
	private FormLocationData address;
	private FormPaymentData payment;
	private FormMetaData formMetaData;
	private FormRestriction restriction;
	private FormTimeslotData timeslot;
	private String redirectUrl;
	private HeaderData headerData;
	private StandingOrderResponseData standingOrderResponseData;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<Section> getCartSections() {
		return cartSections;
	}

	public void setCartSections(List<Section> cartSections) {
		this.cartSections = cartSections;
	}

	public UnavailabilityData getAtpFailure() {
		return atpFailure;
	}

	public void setAtpFailure(UnavailabilityData atpFailure) {
		this.atpFailure = atpFailure;
	}

	public Map<String, List<DrawerData>> getDrawer() {
		return drawer;
	}

	public void setDrawer(Map<String, List<DrawerData>> drawer) {
		this.drawer = drawer;
	}

	public FormLocationData getAddress() {
		return address;
	}

	public void setAddress(FormLocationData address) {
		this.address = address;
	}

	public FormMetaData getFormMetaData() {
		return formMetaData;
	}

	public void setFormMetaData(FormMetaData formMetaData) {
		this.formMetaData = formMetaData;
	}

	public FormPaymentData getPayment() {
		return payment;
	}

	public void setPayment(FormPaymentData payment) {
		this.payment = payment;
	}

	public FormRestriction getRestriction() {
		return restriction;
	}

	public void setRestriction(FormRestriction restriction) {
		this.restriction = restriction;
	}

	public FormTimeslotData getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(FormTimeslotData timeslot) {
		this.timeslot = timeslot;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public HeaderData getHeaderData() {
		return headerData;
	}

	public void setHeaderData(HeaderData headerData) {
		this.headerData = headerData;
	}

	public StandingOrderResponseData getStandingOrderResponseData() {
		return standingOrderResponseData;
	}

	public void setStandingOrderResponseData(
			StandingOrderResponseData standingOrderResponseData) {
		this.standingOrderResponseData = standingOrderResponseData;
	}


	
}
