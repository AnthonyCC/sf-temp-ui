package com.freshdirect.delivery.model;

import java.io.Serializable;

public class AirclicMessageVO implements Serializable {

	private String message;
	private String description;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
