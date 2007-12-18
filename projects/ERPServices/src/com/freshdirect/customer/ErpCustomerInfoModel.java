/*
 * $Workfile: d:\FreshDirect\projects\ERPServices\src\com\freshdirect\customer\ErpCustomerInfoModel.java$
 *
 * $Date: 12/28/02 6:58:36 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import java.util.Date;

import com.freshdirect.framework.core.*;
import com.freshdirect.common.address.*;

/**
 * ErpCustomerInfo model class.
 *
 * @version    $Revision: 6$
 * @author     $Author: Viktor Szathmary$
 * @stereotype fd-model
 */
public class ErpCustomerInfoModel extends ModelSupport {

	private String title;
	private String firstName;
	private String middleName;
	private String lastName;

	private String email;
	private String alternateEmail;
	private boolean emailPlaintext;
	private boolean receiveNewsletter;

	private PhoneNumber homePhone;
	private PhoneNumber businessPhone;
	private PhoneNumber cellPhone;
	private PhoneNumber otherPhone;
	private PhoneNumber fax;
	private String workDepartment;
    private String employeeId;
    
    private Date lastReminderEmail;
    private int reminderDayOfWeek;
    private int reminderFrequency;
    private boolean reminderAltEmail;
    
    private int rsvDayOfWeek;
    private Date rsvStartTime;
    private Date rsvEndTime;
    private String rsvAddressId;
    private java.util.Date unsubscribeDate;
    
    private boolean receiveOptinNewsletter;
    private String regRefTrackingCode;  // referral tracking code when registration
    // changes done by gopal
    private String referralProgId;
	private String referralProgInvtId;
	
	private String hasAutoRenewDP;
	private String autoRenewDPSKU;

	/**
	 * Default constructor.
	 */
	public ErpCustomerInfoModel() {
		super();
	}

	public String getTitle() { return this.title; }
	public void setTitle(String title) { this.title = title; }

	public String getFirstName() { return this.firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getMiddleName() { return this.middleName; }
	public void setMiddleName(String middleName) { this.middleName = middleName; }

	public String getLastName() { return this.lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return this.email; }
	public void setEmail(String email) { this.email = email; }

	public String getAlternateEmail() { return this.alternateEmail; }
	public void setAlternateEmail(String s) { this.alternateEmail = s; }

	public boolean isEmailPlaintext() { return this.emailPlaintext; }
	public void setEmailPlaintext(boolean emailPlaintext) { this.emailPlaintext = emailPlaintext; }

	public boolean isReceiveNewsletter() { return this.receiveNewsletter; }
	public void setReceiveNewsletter(boolean receiveNewsletter) { this.receiveNewsletter = receiveNewsletter; }

	public PhoneNumber getHomePhone() { return this.homePhone; }
	public void setHomePhone(PhoneNumber homePhone) { this.homePhone = homePhone; }

	public PhoneNumber getBusinessPhone() { return this.businessPhone; }
	public void setBusinessPhone(PhoneNumber businessPhone) { this.businessPhone = businessPhone; }

	public PhoneNumber getCellPhone() { return this.cellPhone; }
	public void setCellPhone(PhoneNumber cellPhone) { this.cellPhone = cellPhone; }

	public PhoneNumber getOtherPhone() { return otherPhone; }
	public void setOtherPhone(PhoneNumber otherPhone) { this.otherPhone = otherPhone; }

	public PhoneNumber getFax() { return this.fax; }
	public void setFax(PhoneNumber fax) { this.fax = fax; }

	public String getWorkDepartment(){ return this.workDepartment; }
	public void setWorkDepartment(String workDepartment){ this.workDepartment = workDepartment; }
    
    public String getEmployeeId() { return this.employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public Date getLastReminderEmailSend(){
    	return this.lastReminderEmail;
    }
    
    public void setLastReminderEmailSend(Date lastReminderEmailSend) {
    	this.lastReminderEmail = lastReminderEmailSend;
    }
    
    public int getReminderDayOfWeek() {
    	return this.reminderDayOfWeek;
    }
    
    public void setReminderDayOfWeek(int reminderDayOfWeek){
    	this.reminderDayOfWeek = reminderDayOfWeek;
    }
    
    public int getReminderFrequency(){
    	return this.reminderFrequency;
    }
    
    public void setReminderFrequency(int reminderFrequency){
    	this.reminderFrequency = reminderFrequency;
    }
    
    public boolean isReminderAltEmail(){
    	return this.reminderAltEmail;
    }
    
    public void setReminderAltEmail(boolean reminderAltEmail){
    	this.reminderAltEmail = reminderAltEmail;
    }
    
    public int getRsvDayOfWeek(){
    	return this.rsvDayOfWeek;
    }
    
    public void setRsvDayOfWeek(int rsvDayOfWeek){
    	this.rsvDayOfWeek = rsvDayOfWeek;
    }
    
    public Date getRsvStartTime(){
    	return this.rsvStartTime;
    }
    
    public void setRsvStartTime(Date rsvStartTime){
    	this.rsvStartTime = rsvStartTime;
    }
    
    public Date getRsvEndTime(){
    	return this.rsvEndTime;
    }
    
    public void setRsvEndTime(Date rsvEndTime) {
    	this.rsvEndTime = rsvEndTime;
    }
    
    public String getRsvAddressId(){
    	return this.rsvAddressId;
    }
    
    public void setRsvAddressId(String rsvAddressId){
    	this.rsvAddressId = rsvAddressId;
    }
    
	public Date getUnsubscribeDate() {
		return unsubscribeDate;
	}

	public void setUnsubscribeDate(java.util.Date unsubscribeDate) {
		this.unsubscribeDate = unsubscribeDate;
	}

    public String getRegRefTrackingCode(){
    	return this.regRefTrackingCode;
    }
    
    public void setRegRefTrackingCode(String regRefTrackingCode){
    	this.regRefTrackingCode = regRefTrackingCode;
    }

	public boolean isReceiveOptinNewsletter() {
		return receiveOptinNewsletter;
	}

	public void setReceiveOptinNewsletter(boolean recieveMarketReport) {
		this.receiveOptinNewsletter = recieveMarketReport;
	}


	public String getReferralProgId() {
		return referralProgId;
	}

	public void setReferralProgId(String referralProgId) {
		this.referralProgId = referralProgId;
	}

	public String getReferralProgInvtId() {
		return referralProgInvtId;
	}

	public void setReferralProgInvtId(String referralProgInvtId) {
		this.referralProgInvtId = referralProgInvtId;
	}

	public String getAutoRenewDPSKU() {
		return autoRenewDPSKU;
	}

	public void setAutoRenewDPSKU(String autoRenewDPSKU) {
		this.autoRenewDPSKU = autoRenewDPSKU;
	}

	public String getHasAutoRenewDP() {
		return hasAutoRenewDP;
	}

	public void setHasAutoRenewDP(String hasAutoRenewDP) {
		this.hasAutoRenewDP = hasAutoRenewDP;
	}
}
