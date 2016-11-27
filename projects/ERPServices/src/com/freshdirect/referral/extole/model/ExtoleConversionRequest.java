package com.freshdirect.referral.extole.model;

import java.io.Serializable;
import java.util.Map;


public class ExtoleConversionRequest implements Serializable {

	private static final long serialVersionUID = 5768380794454315022L;
	
	/* mandatory request variable */
	private String eventType;
	private String email;
	private String clickId;
	
	/* optional request variable */
	private String firstName;
	private String lastName;
	private String couponCode;
	private String eventDate;
	private String partnerConversionId;
	private String partnerUserId;
	private Map<String,String> tags;
	
	//Contains the original ID from RAF_TRANS table
	private String rafTransId;
	
	/*These are for Approve EndPoint conversion*/
	
	/* mandatory request variables */
	private String eventStatus;
	
	/* optional request variables */
	private String eventid;
	private String note;
	
	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}
	/**
	 * @param enumRafTransactionType the eventType to set
	 */
	public void setEventType(String enumRafTransactionType) {
		this.eventType = enumRafTransactionType;
	}
	/**
	 * @return the eventStatus
	 */
	public String getEventStatus() {
		return eventStatus;
	}
	/**
	 * @param eventStatus the eventStatus to set
	 */
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	/**
	 * @return the eventid
	 */
	public String getEventid() {
		return eventid;
	}
	/**
	 * @param eventid the eventid to set
	 */
	public void setEventid(String eventid) {
		this.eventid = eventid;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the clickId
	 */
	public String getClickId() {
		return clickId;
	}
	/**
	 * @param clickId the clickId to set
	 */
	public void setClickId(String clickId) {
		this.clickId = clickId;
	}
	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	/**
	 * @return the eventDate
	 */
	public String getEventDate() {
		return eventDate;
	}
	/**
	 * @param eventDate the eventDate to set
	 */
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	/**
	 * @return the partnerConversionId
	 */
	public String getPartnerConversionId() {
		return partnerConversionId;
	}
	/**
	 * @param partnerConversionId the partnerConversionId to set
	 */
	public void setPartnerConversionId(String partnerConversionId) {
		this.partnerConversionId = partnerConversionId;
	}
	/**
	 * @return the partneruserId
	 */
	public String getPartnerUserId() {
		return partnerUserId;
	}
	/**
	 * @param partneruserId the partneruserId to set
	 */
	public void setPartnerUserId(String partnerUserId) {
		this.partnerUserId = partnerUserId;
	}
	/**
	 * @return the tags
	 */
	public Map<String, String> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
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

}
