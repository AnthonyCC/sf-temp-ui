package com.freshdirect.webapp.ajax.expresscheckout.data;

import java.util.List;

public class FormMetaData {

	private List<FormMetaDataItem> cardType;
	private List<FormMetaDataItem> state;
	private List<FormMetaDataItem> country;
	private List<FormMetaDataItem> expireMonth;
	private List<FormMetaDataItem> expireYear;
	private boolean enableECheck;
	private boolean enableEbtCheck;
	private String paymentPromoOAS;

	public List<FormMetaDataItem> getState() {
		return state;
	}

	public void setState(List<FormMetaDataItem> state) {
		this.state = state;
	}

	public List<FormMetaDataItem> getCountry() {
		return country;
	}

	public void setCountry(List<FormMetaDataItem> country) {
		this.country = country;
	}

	public List<FormMetaDataItem> getCardType() {
		return cardType;
	}

	public void setCardType(List<FormMetaDataItem> cardType) {
		this.cardType = cardType;
	}

	public List<FormMetaDataItem> getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(List<FormMetaDataItem> expireMonth) {
		this.expireMonth = expireMonth;
	}

	public List<FormMetaDataItem> getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(List<FormMetaDataItem> expireYear) {
		this.expireYear = expireYear;
	}

	public boolean isEnableECheck() {
		return enableECheck;
	}

	public void setEnableECheck(boolean enableECheck) {
		this.enableECheck = enableECheck;
	}

	public boolean isEnableEbtCheck() {
		return enableEbtCheck;
	}

	public void setEnableEbtCheck(boolean enableEbtCheck) {
		this.enableEbtCheck = enableEbtCheck;
	}

    public String getPaymentPromoOAS() {
		return paymentPromoOAS;
	}

	public void setPaymentPromoOAS(String paymentPromoOAS) {
		this.paymentPromoOAS = paymentPromoOAS;
	}

}
