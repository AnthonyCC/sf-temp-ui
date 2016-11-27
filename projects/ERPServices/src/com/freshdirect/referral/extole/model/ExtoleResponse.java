package com.freshdirect.referral.extole.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtoleResponse implements Serializable{

	private static final long serialVersionUID = 9221375836895918919L;
	
	//Contains the original ID from RAF_TRANS table
	private String rafTransId;
	
	//private EnumRafTransactionStatus status;
	private String status;
	private String message;
	private String eventId;
	private String code;
	/**
	 * @return the rafTransId
	 */
	public String getRafTransId() {
		return rafTransId;
	}
	/**
	 * @param rafTransId the rafTransId to set
	 */
	public void setRafTransId(String rafTransId) {
		this.rafTransId = rafTransId;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the eventId
	 */
	@JsonProperty("event_id")
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param event_id the event_id to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
