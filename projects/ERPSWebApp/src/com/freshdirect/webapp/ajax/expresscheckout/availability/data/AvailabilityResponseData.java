package com.freshdirect.webapp.ajax.expresscheckout.availability.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.ajax.checkout.data.UnavailabilityData;

public class AvailabilityResponseData {

	@JsonProperty("error_message")
	private String errorMessage;

	private UnavailabilityData atpFailure;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public UnavailabilityData getAtpFailure() {
		return atpFailure;
	}

	public void setAtpFailure(UnavailabilityData atpFailure) {
		this.atpFailure = atpFailure;
	}

}
