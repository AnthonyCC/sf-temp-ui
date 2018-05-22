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
import java.util.List;

import com.freshdirect.framework.core.*;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.common.address.*;
import com.freshdirect.fdstore.customer.FDIdentity;

/**
 * ErpCustomerInfo model class.
 *
 * @version    $Revision: 6$
 * @author     $Author: Viktor Szathmary$
 * @stereotype fd-model
 */
public class ErpCustomerInfoModel extends ModelSupport {

	private static final long serialVersionUID = 9156189087842548162L;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;

	private String displayName;
	
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
    private String receive_emailLevel;
    private boolean noContactMail;
    private boolean noContactPhone;
    
    private boolean receiveOptinNewsletter;
    private String regRefTrackingCode;  // referral tracking code when registration
    // changes done by gopal
    private String referralProgId;
	private String referralProgInvtId;
	
	private String hasAutoRenewDP;
	private String autoRenewDPSKU;
	
	//APPDEV-2114 - SMS Capture
	private PhoneNumber mobileNumber;
	private String mobilePrefs;
	private boolean deliveryNotification;
	private boolean offersNotification;
	private boolean goGreen;
	/* APPDEV-2475 DP T&C */
	private int dpTcViewCount;
	private Date dpTcAgreeDate;
	
	private String industry;
	private int numOfEmployees;
	private String secondEmailAddress;
	
	// SMS Alerts 
	private EnumSMSAlertStatus orderNotices;
	private EnumSMSAlertStatus orderExceptions;
	private EnumSMSAlertStatus offers;
	private EnumSMSAlertStatus partnerMessages;
	private String smsPreferenceflag;
	private Date smsOptinDate;
	
	private String companyNameSignup; 
	
	private String soCartOverlayFirstTime;
	
	private String soFeatureOverlay;
	/* APPDEV-4381  */
	private String fdTcAgree;
 
	private FDIdentity identity;

	public String getSoFeatureOverlay() {
		return soFeatureOverlay;
	}

	public void setSoFeatureOverlay(String soFeatureOverlay) {
		this.soFeatureOverlay = soFeatureOverlay;
	}

	public String getSoCartOverlayFirstTime() {
		return soCartOverlayFirstTime;
	}

	public void setSoCartOverlayFirstTime(String soCartOverlayFirstTime) {
		this.soCartOverlayFirstTime = soCartOverlayFirstTime;
	}

	public String getFdTcAgree() {
		return fdTcAgree;
	}

	public void setFdTcAgree(String fdTcAgree) {
		this.fdTcAgree = fdTcAgree;
	}

	public Date getFdTcAgreeDate() {
		return fdTcAgreeDate;
	}

	public void setFdTcAgreeDate(Date fdTcAgreeDate) {
		this.fdTcAgreeDate = fdTcAgreeDate;
	}

	private Date fdTcAgreeDate;

	// Social login 
	private List<ErpCustomerSocialLoginModel> socialLoginInfo;
	

	public List<ErpCustomerSocialLoginModel> getSocialLoginInfo() {
		return socialLoginInfo;
	}

	public void setSocialLoginInfo(List<ErpCustomerSocialLoginModel> socialLoginInfo) {
		this.socialLoginInfo = socialLoginInfo;
	}	

	public Date getSmsOptinDate() {
		return smsOptinDate;
	}

	public void setSmsOptinDate(Date smsOptinDate) {
		this.smsOptinDate = smsOptinDate;
	}

	public PhoneNumber getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(PhoneNumber mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobilePreference() {
		return mobilePrefs;
	}

	public void setMobilePreference(String pref) {
		this.mobilePrefs = pref;
	}

	public boolean isDeliveryNotification() {
		return deliveryNotification;
	}

	public void setDeliveryNotification(boolean deliveryNotification) {
		this.deliveryNotification = deliveryNotification;
	}

	public boolean isOffersNotification() {
		return offersNotification;
	}

	public void setOffersNotification(boolean offersNotification) {
		this.offersNotification = offersNotification;
	}

	public boolean isGoGreen() {
		return goGreen;
	}

	public void setGoGreen(boolean goGreen) {
		this.goGreen = goGreen;
	}

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
	
	public String getEmailPreferenceLevel(){
    	return this.receive_emailLevel;
    }
	
	public void setEmailPreferenceLevel(String receive_emailLevel) {
		this.receive_emailLevel = receive_emailLevel;
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

	public boolean isNoContactMail() {
		return noContactMail;
	}

	public void setNoContactMail(boolean noContactMail) {
		this.noContactMail = noContactMail;
	}

	public boolean isNoContactPhone() {
		return noContactPhone;
	}

	public void setNoContactPhone(boolean noContactPhone) {
		this.noContactPhone = noContactPhone;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/* APPDEV-2475 DP T&C */
	public int getDpTcViewCount() { return dpTcViewCount; }
	public void setDpTcViewCount(int dpTcViewCount) { this.dpTcViewCount = dpTcViewCount; }
	public Date getDpTcAgreeDate() { return dpTcAgreeDate; }
	public void setDpTcAgreeDate(Date dpTcAgreeDate) { this.dpTcAgreeDate = dpTcAgreeDate; }

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public int getNumOfEmployees() {
		return numOfEmployees;
	}

	public void setNumOfEmployees(int numOfEmployees) {
		this.numOfEmployees = numOfEmployees;
	}

	public String getSecondEmailAddress() {
		return secondEmailAddress;
	}

	public void setSecondEmailAddress(String secondEmailAddress) {
		this.secondEmailAddress = secondEmailAddress;
	}

	public String getSmsPreferenceflag() {
		return smsPreferenceflag;
	}

	public void setSmsPreferenceflag(String smsPreferenceflag) {
		this.smsPreferenceflag = smsPreferenceflag;
	}

	public EnumSMSAlertStatus getOrderNotices() {
		return orderNotices;
	}

	public void setOrderNotices(EnumSMSAlertStatus orderNotices) {
		this.orderNotices = orderNotices;
	}

	public EnumSMSAlertStatus getOrderExceptions() {
		return orderExceptions;
	}

	public void setOrderExceptions(EnumSMSAlertStatus orderExceptions) {
		this.orderExceptions = orderExceptions;
	}

	public EnumSMSAlertStatus getOffers() {
		return offers;
	}

	public void setOffers(EnumSMSAlertStatus offers) {
		this.offers = offers;
	}

	public EnumSMSAlertStatus getPartnerMessages() {
		return partnerMessages;
	}

	public void setPartnerMessages(EnumSMSAlertStatus partnerMessages) {
		this.partnerMessages = partnerMessages;
	}

	public String getCompanyNameSignup() {
		return companyNameSignup;
	}

	public void setCompanyNameSignup(String companyNameSignup) {
		this.companyNameSignup = companyNameSignup;
	}
	
	public FDIdentity getIdentity() {
		return identity;
	}
	
	public void setIdentity(FDIdentity identity) {
		this.identity = identity;
	}

	@Override
	public void setId(String id) {
		if (id != null) {
			super.setId(id);
		}
	}
}
