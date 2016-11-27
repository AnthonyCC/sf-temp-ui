package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;
import com.freshdirect.webapp.ajax.expresscheckout.location.data.FormLocationData;
import com.freshdirect.webapp.ajax.expresscheckout.payment.data.FormPaymentData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.SuccessPageData;
import com.freshdirect.webapp.ajax.expresscheckout.sempixels.data.SemPixelData;
import com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.data.TextMessageAlertData;
import com.freshdirect.webapp.ajax.expresscheckout.timeslot.data.FormTimeslotData;

public class SinglePageCheckoutSuccessData {

	private UnavailabilityData atpFailure;
	private SuccessPageData successPageData;
	private TextMessageAlertData textMessageAlertData;
	private Map<String, List<DrawerData>> drawer;
	private FormLocationData address;
	private FormPaymentData payment;
	private FormTimeslotData timeslot;
    private SemPixelData semPixelData;

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

	public SuccessPageData getSuccessPageData() {
		return successPageData;
	}

	public void setSuccessPageData(SuccessPageData successPageData) {
		this.successPageData = successPageData;
	}

	public TextMessageAlertData getTextMessageAlertData() {
		return textMessageAlertData;
	}

	public void setTextMessageAlertData(TextMessageAlertData textMessageAlertData) {
		this.textMessageAlertData = textMessageAlertData;
	}

	public FormPaymentData getPayment() {
		return payment;
	}

	public void setPayment(FormPaymentData payment) {
		this.payment = payment;
	}

	public FormTimeslotData getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(FormTimeslotData timeslot) {
		this.timeslot = timeslot;
	}

    public SemPixelData getSemPixelData() {
        return semPixelData;
    }

    public void setSemPixelData(SemPixelData semPixelData) {
        this.semPixelData = semPixelData;
    }

}
