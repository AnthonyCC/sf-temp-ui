/*
 * $Workfile: d:\FreshDirect\projects\ERPServices\src\com\freshdirect\customer\ejb\ErpCustomerInfoPersistentBean.java$
 *
 * $Date: 11/22/02 6:42:55 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer.ejb;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.util.*;
import com.freshdirect.sms.EnumSMSAlertStatus;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerSocialLoginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ErpCustomerInfo persistent bean.
 *
 * @version    $Revision: 10$
 * @author     $Author: Viktor Szathmary$
 * @stereotype fd-persistent
 */
public class ErpCustomerInfoPersistentBean extends DependentPersistentBeanSupport {

	private String title;
	private String firstName;


	private String middleName;
	private String lastName;
	
	private String displayName;

	private String email;
	private String alternateEmail;
	private boolean emailPlaintext;
	private boolean receiveNewsletter;
	private java.util.Date unsubscribeDate;

	private PhoneNumber homePhone;
	private PhoneNumber businessPhone;
	private PhoneNumber cellPhone;
	private PhoneNumber otherPhone;
	private PhoneNumber fax;
	private String workDepartment;
	private String employeeId;

	//Reminder Email related fields
	private java.util.Date lastReminderEmail;
	private int reminderDayOfWeek;
	private int reminderFrequency;
	private boolean reminderAltEmail;

	private boolean recieveOptinNewslettert;

    private String receive_emailLevel;
    private boolean noContactMail;
    private boolean noContactPhone;
	
	//Recurring Reservation fields
	private int rsvDayOfWeek;
	private java.util.Date rsvStartTime;
	private java.util.Date rsvEndTime;
	private String rsvAddressId;

    private String regRefTrackingCode;  // referral tracking code when registration
    // latest changes by gopal
	private String referralProgId;
	private String referralProgInvtId;
	
	private String hasAutoRenewDP;
	private String autoRenewDPSKU;
	
	//APPDEV-2114 - SMS Capture
	private PhoneNumber mobileNumber;
	private String  mobilePrefs;
	private boolean deliveryNotification;
	private boolean offersNotification;
	private boolean goGreen;
	// New Alerts
	private EnumSMSAlertStatus orderNotices;
	private EnumSMSAlertStatus orderExceptions;
	private EnumSMSAlertStatus offers;
	private EnumSMSAlertStatus partnerMessages;
	private String smsNoThanksflag;
	private java.util.Date smsOptinDate;
	
	/* APPDEV-2475 DP T&C */
	private java.util.Date dpTcAgreeDate;
	private int dpTcViewCount;
	
	private String industry;
	private int numOfEmployees;
	private String secondEmailAddress;
	

	private java.util.Date fdTcAgreeDate;
	private String fdTcAgree;

	// Social login 
	private List<ErpCustomerSocialLoginModel> socialLoginInfo;
	
	private String companyNameSignup;

	/**
	 * Default constructor.
	 */
	public ErpCustomerInfoPersistentBean() {
		super();
		this.title = null;
		this.firstName = null;
		this.middleName = null;
		this.lastName = null;

		this.displayName = null;
		
		this.email = null;
		this.alternateEmail = null;
		this.emailPlaintext = false;
		this.receiveNewsletter = false;
		this.unsubscribeDate = null;

		this.homePhone = null;
		this.businessPhone = null;
		this.cellPhone = null;
		this.otherPhone = null;
		this.fax = null;
		this.workDepartment = null;
		this.employeeId = null;

		this.lastReminderEmail = null;
		this.reminderAltEmail = false;

		this.recieveOptinNewslettert = false;
		this.receive_emailLevel = null;
	    this.noContactMail = false;
	    this.noContactPhone = false;
		
		rsvStartTime = null;
		rsvEndTime = null;
		rsvAddressId = null;
		
		regRefTrackingCode = null;
		this.referralProgId=null;
		this.referralProgInvtId=null;
		this.hasAutoRenewDP=null;
		this.autoRenewDPSKU=null;
		
		this.mobileNumber = null;
		this.mobilePrefs = null;
		this.deliveryNotification = false;
		this.offersNotification = false;
		this.goGreen = false;
		// New alerts
		this.orderNotices = EnumSMSAlertStatus.NONE;
		this.orderExceptions = EnumSMSAlertStatus.NONE;
		this.offers = EnumSMSAlertStatus.NONE;
		this.partnerMessages = EnumSMSAlertStatus.NONE;
		this.smsNoThanksflag = null;
		this.smsOptinDate=null;
		
		/* APPDEV-2475 DP T&C */
		this.dpTcViewCount = 0;
		this.dpTcAgreeDate = null;
		
		/* APPDEV-4381 */
		this.fdTcAgreeDate=null;
		this.fdTcAgree=null;

		
		// Social login
		this.socialLoginInfo = null;
		
		this.companyNameSignup = null;
	}

	/**
	 * Load constructor.
	 */
	public ErpCustomerInfoPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpCustomerInfoModel to copy from
	 */
	public ErpCustomerInfoPersistentBean(ErpCustomerInfoModel model) {
		super();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpCustomerInfoModel object.
	 */
	public ModelI getModel() {
		ErpCustomerInfoModel model = new ErpCustomerInfoModel();
		super.decorateModel(model);

		model.setTitle(this.title);
		model.setFirstName(this.firstName);
		model.setMiddleName(this.middleName);
		model.setLastName(this.lastName);
		
		model.setDisplayName(this.displayName);

		model.setEmail(this.email);
		model.setAlternateEmail(this.alternateEmail);
		model.setEmailPlaintext(this.emailPlaintext);
		model.setReceiveNewsletter(this.receiveNewsletter);
		model.setUnsubscribeDate(this.unsubscribeDate);

		model.setHomePhone(this.homePhone);
		model.setBusinessPhone(this.businessPhone);
		model.setCellPhone(this.cellPhone);
		model.setOtherPhone(this.otherPhone);
		model.setFax(this.fax);
		model.setWorkDepartment(this.workDepartment);
		model.setEmployeeId(this.employeeId);

		model.setLastReminderEmailSend(this.lastReminderEmail);
		model.setReminderDayOfWeek(this.reminderDayOfWeek);
		model.setReminderFrequency(this.reminderFrequency);
		model.setReminderAltEmail(this.reminderAltEmail);

		model.setReceiveOptinNewsletter(this.recieveOptinNewslettert);
		model.setEmailPreferenceLevel(this.receive_emailLevel);
		model.setNoContactMail(this.noContactMail);
		model.setNoContactPhone(this.noContactPhone);
		
		model.setRsvDayOfWeek(this.rsvDayOfWeek);
		model.setRsvStartTime(this.rsvStartTime);
		model.setRsvEndTime(this.rsvEndTime);
		model.setRsvAddressId(this.rsvAddressId);
		model.setRegRefTrackingCode(this.regRefTrackingCode);
		// changes done by gopal
		
		model.setReferralProgId(this.referralProgId);
		model.setReferralProgInvtId(this.referralProgInvtId);
		
		model.setHasAutoRenewDP(this.hasAutoRenewDP);
		model.setAutoRenewDPSKU(this.autoRenewDPSKU);
		
		//APPDEV-2114
		model.setMobileNumber(this.mobileNumber);
		model.setMobilePreference(this.mobilePrefs);
		model.setDeliveryNotification(this.deliveryNotification);
		model.setOffersNotification(this.offersNotification);
		// add the new alerts
		model.setOrderNotices(this.orderNotices);
		model.setOrderExceptions(this.orderExceptions);
		model.setOffers(this.offers);
		model.setPartnerMessages(this.partnerMessages);
		model.setSmsPreferenceflag(this.smsNoThanksflag);
		model.setSmsOptinDate(this.smsOptinDate);
		
		model.setGoGreen(this.goGreen);
		
		/* APPDEV-2475 DP T&C */
		model.setDpTcViewCount(this.dpTcViewCount);
		model.setDpTcAgreeDate(this.dpTcAgreeDate);
		
		/*   APPDEV-4381  */
		
		model.setFdTcAgree(this.fdTcAgree);
		model.setFdTcAgreeDate(this.fdTcAgreeDate);
		
		
		model.setIndustry(this.industry);
		model.setNumOfEmployees(this.numOfEmployees);
		model.setSecondEmailAddress(this.secondEmailAddress);

		model.setSocialLoginInfo(socialLoginInfo);
		
		model.setCompanyNameSignup(this.companyNameSignup);
		
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpCustomerInfoModel m = (ErpCustomerInfoModel) model;

		this.title = m.getTitle();
		this.firstName = m.getFirstName();
		this.middleName = m.getMiddleName();
		this.lastName = m.getLastName();

		this.displayName = m.getDisplayName();
		this.email = m.getEmail();
		this.alternateEmail = m.getAlternateEmail();
		this.emailPlaintext = m.isEmailPlaintext();
		this.receiveNewsletter = m.isReceiveNewsletter();
		this.unsubscribeDate = m.getUnsubscribeDate();

		this.homePhone = m.getHomePhone();
		this.businessPhone = m.getBusinessPhone();
		this.cellPhone = m.getCellPhone();
		this.otherPhone = m.getOtherPhone();
		this.fax = m.getFax();
		this.workDepartment = m.getWorkDepartment();
		this.employeeId = m.getEmployeeId();

		this.lastReminderEmail = m.getLastReminderEmailSend();
		this.reminderDayOfWeek = m.getReminderDayOfWeek();
		this.reminderFrequency = m.getReminderFrequency();
		this.reminderAltEmail = m.isReminderAltEmail();

		this.recieveOptinNewslettert = m.isReceiveOptinNewsletter();
		this.receive_emailLevel = m.getEmailPreferenceLevel();
		this.noContactMail = m.isNoContactMail();
		this.noContactPhone = m.isNoContactPhone();
		
		this.rsvDayOfWeek = m.getRsvDayOfWeek();
		this.rsvStartTime = m.getRsvStartTime();
		this.rsvEndTime = m.getRsvEndTime();
		this.rsvAddressId = m.getRsvAddressId();
		this.regRefTrackingCode = m.getRegRefTrackingCode();

		this.referralProgId=m.getReferralProgId();
		this.referralProgInvtId=m.getReferralProgInvtId();
		this.hasAutoRenewDP=m.getHasAutoRenewDP();
		this.autoRenewDPSKU=m.getAutoRenewDPSKU();
		
		//APPDEV-2114
		this.mobileNumber = m.getMobileNumber();
		this.mobilePrefs = m.getMobilePreference();
		this.deliveryNotification = m.isDeliveryNotification();
		this.offersNotification = m.isOffersNotification();
		//Add New Alerts
		this.orderNotices=m.getOrderNotices();
		this.orderExceptions=m.getOrderExceptions();
		this.offers=m.getOffers();
		this.partnerMessages=m.getPartnerMessages();
		this.smsNoThanksflag=m.getSmsPreferenceflag();
		this.smsOptinDate=m.getSmsOptinDate();
		
		this.goGreen = m.isGoGreen();

		/* APPDEV-2475 DP T&C */
		this.dpTcViewCount = m.getDpTcViewCount();
		this.dpTcAgreeDate = m.getDpTcAgreeDate();
		
		this.setIndustry(m.getIndustry());
		this.setNumOfEmployees(m.getNumOfEmployees());
		this.setSecondEmailAddress(m.getSecondEmailAddress());
		/*   APPDEV-4381  */
		this.fdTcAgree=m.getFdTcAgree();
		this.fdTcAgreeDate=m.getFdTcAgreeDate();
		
		this.companyNameSignup = m.getCompanyNameSignup();
		
		this.setModified();
	}

	/**
	 * Find ErpCustomerInfoPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpCustomerInfoPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CUSTOMERINFO WHERE CUSTOMER_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpCustomerInfoPersistentBean bean = new ErpCustomerInfoPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}

	private final String convertPhone(PhoneNumber phoneNumber) {
		return phoneNumber == null ? null : phoneNumber.getPhone();
	}

	private final String convertExtension(PhoneNumber phoneNumber) {
		return phoneNumber == null ? null : phoneNumber.getExtension();
	}

	private final PhoneNumber convertPhoneNumber(String phone, String extension) {
		return "() -".equals(phone) ? null : new PhoneNumber(phone, NVL.apply(extension, ""));
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		//String id = this.getNextId(conn);
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.CUSTOMERINFO (CUSTOMER_ID, TITLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL, ALT_EMAIL, EMAIL_PLAIN_TEXT, "
				+ " RECEIVE_NEWS, HOME_PHONE, HOME_EXT, BUSINESS_PHONE, BUSINESS_EXT, CELL_PHONE, CELL_EXT, OTHER_PHONE, OTHER_EXT, FAX, "
				+ " FAX_EXT, WORK_DEPARTMENT, EMPLOYEE_ID, REMINDER_LAST_SEND, REMINDER_FREQUENCY, REMINDER_DAY_OF_WEEK, REMINDER_ALT_EMAIL, "
				+ " RSV_DAY_OF_WEEK, RSV_START_TIME, RSV_END_TIME, RSV_ADDRESS_ID, UNSUBSCRIBE_DATE, REG_REF_TRACKING_CODE, REG_REF_PROG_ID, "
				+ " REF_PROG_INVT_ID, RECEIVE_OPTINNEWSLETTER, EMAIL_LEVEL, NO_CONTACT_MAIL, NO_CONTACT_PHONE, DISPLAY_NAME, DP_TC_VIEWS, "
				+ " DP_TC_AGREE_DATE,INDUSTRY,NUM_OF_EMPLOYEES,SECOND_EMAIL_ADDRESS,FD_TC_AGREE,FD_TC_AGREE_DATE, COMPANY_NAME_SIGNUP, GO_GREEN) "
					+ " values (?,?,?,?,?,?,?,?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(?,'('),')'),' '),'-'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'Y')");

		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, this.title);
		if((this.firstName != null) && (this.firstName.trim() != "")){
			ps.setString(3, this.firstName);
		}else{			
			ps.setString(3, email.substring(0, email.indexOf("@")));  // temporary placeholder
		}		
		ps.setString(4, this.middleName);
		if((this.lastName != null) && (this.lastName.trim() != "") ){
			ps.setString(5, this.lastName);
		}else{
			ps.setString(5, " "); // temporary placeholder
		}				
		ps.setString(6, this.email);
		ps.setString(7, this.alternateEmail);
		ps.setString(8, (this.emailPlaintext ? "X" : " "));
		ps.setString(9, (this.receiveNewsletter ? "X" : " "));

		ps.setString(10, this.convertPhone(this.homePhone));
		ps.setString(11, this.convertExtension(this.homePhone));

		ps.setString(12, this.convertPhone(this.businessPhone));
		ps.setString(13, this.convertExtension(this.businessPhone));

		ps.setString(14, this.convertPhone(this.cellPhone));
		ps.setString(15, this.convertExtension(this.cellPhone));

		ps.setString(16, this.convertPhone(this.otherPhone));
		ps.setString(17, this.convertExtension(this.otherPhone));

		ps.setString(18, this.convertPhone(this.fax));
		ps.setString(19, this.convertExtension(this.fax));
		ps.setString(20, this.workDepartment);
		ps.setString(21, this.employeeId);

		if (this.lastReminderEmail == null) {
			ps.setNull(22, Types.TIMESTAMP);
		} else {
			ps.setDate(22, new java.sql.Date(this.lastReminderEmail.getTime()));
		}
		ps.setInt(23, this.reminderFrequency);
		ps.setInt(24, this.reminderDayOfWeek);
		ps.setString(25, (this.reminderAltEmail ? "X" : " "));

		ps.setInt(26, this.rsvDayOfWeek);
		if (this.rsvStartTime == null) {
			ps.setNull(27, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(27, new Timestamp(this.rsvStartTime.getTime()));
		}
		if (this.rsvEndTime == null) {
			ps.setNull(28, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(28, new Timestamp(this.rsvEndTime.getTime()));
		}
		ps.setString(29, this.rsvAddressId);
		if (this.unsubscribeDate == null){
			ps.setNull(30, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(30, new Timestamp(this.unsubscribeDate.getTime()));
		}

		if (this.regRefTrackingCode == null){
			ps.setNull(31, Types.VARCHAR);
		} else {
			ps.setString(31, this.regRefTrackingCode);
		}

		if (this.referralProgId == null){
			ps.setNull(32, Types.VARCHAR);
		} else {
			ps.setString(32, this.referralProgId);
		}
		
		if (this.referralProgInvtId == null){
			ps.setNull(33, Types.VARCHAR);
		} else {
			ps.setString(33, this.referralProgInvtId);
		}

		ps.setString(34, this.recieveOptinNewslettert ? "X" : "");
		
		ps.setString(35, this.receive_emailLevel);
		ps.setString(36, this.noContactMail ? "X" : "");
		ps.setString(37, this.noContactPhone ? "X" : "");
		ps.setString(38, this.displayName);
		
		/* APPDEV-2475 DP T&C */
		ps.setInt(39, this.dpTcViewCount);
		if (this.dpTcAgreeDate == null) {
			ps.setNull(40, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(40, new Timestamp(this.dpTcAgreeDate.getTime()));
		}
		
		ps.setString(41, this.industry);
		ps.setInt(42, this.numOfEmployees);
		ps.setString(43, this.secondEmailAddress);
		
		/* APPDEV-4381 */
		ps.setString(44, this.fdTcAgree);
		if (this.fdTcAgreeDate == null) {
			ps.setNull(45, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(45, new Timestamp(this.fdTcAgreeDate.getTime()));
		}

		ps.setString(46, this.companyNameSignup);
		
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		this.setPK(this.getParentPK());

		ps.close();

		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		//Change the SQL...
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT TITLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL, ALT_EMAIL, EMAIL_PLAIN_TEXT, RECEIVE_NEWS, "
					+ " '('||substr(HOME_PHONE,1,3)||') '||substr(HOME_PHONE,4,3)||'-'||substr(HOME_PHONE,7,4) as HOME_PHONE, HOME_EXT, "
					+ " '('||substr(BUSINESS_PHONE,1,3)||') '||substr(BUSINESS_PHONE,4,3)||'-'||substr(BUSINESS_PHONE,7,4) as BUSINESS_PHONE, BUSINESS_EXT, "
					+ " '('||substr(CELL_PHONE,1,3)||') '||substr(CELL_PHONE,4,3)||'-'||substr(CELL_PHONE,7,4) as CELL_PHONE, CELL_EXT, "
					+ " '('||substr(OTHER_PHONE,1,3)||') '||substr(OTHER_PHONE,4,3)||'-'||substr(OTHER_PHONE,7,4) as OTHER_PHONE, OTHER_EXT, "
					+ " '('||substr(FAX,1,3)||') '||substr(FAX,4,3)||'-'||substr(FAX,7,4) as FAX, FAX_EXT, WORK_DEPARTMENT, EMPLOYEE_ID, "
					+ " REMINDER_LAST_SEND, REMINDER_FREQUENCY, REMINDER_DAY_OF_WEEK, REMINDER_ALT_EMAIL, RSV_DAY_OF_WEEK, RSV_START_TIME, "
					+ " RSV_END_TIME, RSV_ADDRESS_ID, UNSUBSCRIBE_DATE, REG_REF_TRACKING_CODE, REG_REF_PROG_ID, REF_PROG_INVT_ID, "
					+ " RECEIVE_OPTINNEWSLETTER, HAS_AUTORENEW_DP, AUTORENEW_DP_TYPE, EMAIL_LEVEL, NO_CONTACT_MAIL, NO_CONTACT_PHONE, "
					+ " mobile_number, mobile_preference_flag, delivery_notification, offers_notification, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION,SMS_PREFERENCE_FLAG, go_green, display_name, "
					+ " DP_TC_VIEWS, DP_TC_AGREE_DATE,INDUSTRY,NUM_OF_EMPLOYEES,SECOND_EMAIL_ADDRESS, SMS_OPTIN_DATE,FD_TC_AGREE,FD_TC_AGREE_DATE,COMPANY_NAME_SIGNUP"
					+ " FROM CUST.CUSTOMERINFO WHERE CUSTOMER_ID = ?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.title = NVL.apply(rs.getString("TITLE"), "");
			this.firstName = rs.getString("FIRST_NAME");
			this.middleName = NVL.apply(rs.getString("MIDDLE_NAME"), "");
			this.lastName = rs.getString("LAST_NAME");
			this.email = rs.getString("EMAIL");
			this.alternateEmail = rs.getString("ALT_EMAIL");
			this.emailPlaintext = ("X".equalsIgnoreCase(rs.getString("EMAIL_PLAIN_TEXT")) ? true : false);
			this.receiveNewsletter = ("X".equalsIgnoreCase(rs.getString("RECEIVE_NEWS")) ? true : false);
			this.homePhone = this.convertPhoneNumber(rs.getString("HOME_PHONE"), rs.getString("HOME_EXT"));
			this.businessPhone = this.convertPhoneNumber(rs.getString("BUSINESS_PHONE"), rs.getString("BUSINESS_EXT"));
			this.cellPhone = this.convertPhoneNumber(rs.getString("CELL_PHONE"), rs.getString("CELL_EXT"));
			this.otherPhone = this.convertPhoneNumber(rs.getString("OTHER_PHONE"), rs.getString("OTHER_EXT"));
			this.fax = this.convertPhoneNumber(rs.getString("FAX"), rs.getString("FAX_EXT"));
			this.workDepartment = NVL.apply(rs.getString("WORK_DEPARTMENT"), "");
			this.employeeId = NVL.apply(rs.getString("EMPLOYEE_ID"), "");

			this.lastReminderEmail = rs.getDate("REMINDER_LAST_SEND");
			this.reminderFrequency = rs.getInt("REMINDER_FREQUENCY");
			this.reminderDayOfWeek = rs.getInt("REMINDER_DAY_OF_WEEK");
			this.reminderAltEmail = ("X".equals(rs.getString("REMINDER_ALT_EMAIL")) ? true : false);
			
			this.recieveOptinNewslettert = ("X".equalsIgnoreCase(rs.getString("RECEIVE_OPTINNEWSLETTER")));
			this.receive_emailLevel = (rs.getString("EMAIL_LEVEL"));
			this.noContactMail = ("X".equalsIgnoreCase(rs.getString("NO_CONTACT_MAIL")));
			this.noContactPhone = ("X".equalsIgnoreCase(rs.getString("NO_CONTACT_PHONE")));
			
			this.rsvDayOfWeek = rs.getInt("RSV_DAY_OF_WEEK");
			this.rsvStartTime = rs.getTimestamp("RSV_START_TIME");
			this.rsvEndTime = rs.getTimestamp("RSV_END_TIME");
			this.rsvAddressId = rs.getString("RSV_ADDRESS_ID");
			this.unsubscribeDate = rs.getDate("UNSUBSCRIBE_DATE");
			this.regRefTrackingCode = rs.getString("REG_REF_TRACKING_CODE");
			// changes done by gopal
			
			this.referralProgId= rs.getString("REG_REF_PROG_ID");
			this.referralProgInvtId= rs.getString("REF_PROG_INVT_ID");
			this.hasAutoRenewDP=rs.getString("HAS_AUTORENEW_DP");
			this.autoRenewDPSKU=rs.getString("AUTORENEW_DP_TYPE");
			
			//APPDEV-2114
			if(rs.getString("mobile_number") != null)
				this.mobileNumber = this.convertPhoneNumber(rs.getString("mobile_number"), "");
			else
				this.mobileNumber = null;
			this.mobilePrefs = rs.getString("mobile_preference_flag");
			this.deliveryNotification = "Y".equals(rs.getString("delivery_notification"))?true:false;
			this.offersNotification = "Y".equals(rs.getString("offers_notification"))?true:false;
			
			//New Alerts
			//this.orderNotices="Y".equals(rs.getString("ORDER_NOTIFICATION"))?true:false;
			if(rs.getString("ORDER_NOTIFICATION")!=null){
				try {
					this.orderNotices=EnumSMSAlertStatus.getEnum(rs.getString("ORDER_NOTIFICATION"));
				} catch (IllegalArgumentException e) {
					this.orderNotices=EnumSMSAlertStatus.NONE;
				}
			} else{
				this.orderNotices=EnumSMSAlertStatus.NONE;
			}
			//this.orderExceptions="Y".equals(rs.getString("ORDEREXCEPTION_NOTIFICATION"))?true:false;
			if(rs.getString("ORDEREXCEPTION_NOTIFICATION")!=null){
				try {
					this.orderExceptions=EnumSMSAlertStatus.getEnum(rs.getString("ORDEREXCEPTION_NOTIFICATION"));
				} catch (IllegalArgumentException e) {
					this.orderExceptions=EnumSMSAlertStatus.NONE;
				}
			} else{
				this.orderExceptions=EnumSMSAlertStatus.NONE;
			}
			//this.offers="Y".equals(rs.getString("offers"))?true:false;
			if(rs.getString("SMS_OFFERS_ALERT")!=null){
				try {
					this.offers=EnumSMSAlertStatus.getEnum(rs.getString("SMS_OFFERS_ALERT"));
				} catch (IllegalArgumentException e) {
					this.offers=EnumSMSAlertStatus.NONE;
				}
			} else{
				this.offers=EnumSMSAlertStatus.NONE;
			}
			//this.partnerMessages="Y".equals(rs.getString("PARTNERMESSAGE_NOTIFICATION"))?true:false;
			if(rs.getString("PARTNERMESSAGE_NOTIFICATION")!=null){
				try {
					this.partnerMessages=EnumSMSAlertStatus.getEnum(rs.getString("PARTNERMESSAGE_NOTIFICATION"));
				} catch (IllegalArgumentException e) {
					this.partnerMessages=EnumSMSAlertStatus.NONE;
				}
			} else{
				this.partnerMessages=EnumSMSAlertStatus.NONE;
			}
			this.smsNoThanksflag=rs.getString("SMS_PREFERENCE_FLAG");
			this.smsOptinDate=rs.getTimestamp("SMS_OPTIN_DATE");
			
			this.goGreen = ("Y".equals(rs.getString("go_green"))|| "I".equals(rs.getString("go_green"))) ?true:false;
			this.displayName = rs.getString("display_name");
			
			/* APPDEV-2475 DP T&C */
			this.dpTcViewCount = rs.getInt("DP_TC_VIEWS");
			this.dpTcAgreeDate = rs.getDate("DP_TC_AGREE_DATE");
			
			/* APPDEV-4381 FD T&C */
			this.fdTcAgree = rs.getString("FD_TC_AGREE");
			this.fdTcAgreeDate = rs.getDate("FD_TC_AGREE_DATE");
			
			this.industry = NVL.apply(rs.getString("INDUSTRY"), "");
			this.numOfEmployees = rs.getInt("NUM_OF_EMPLOYEES");
			this.secondEmailAddress = rs.getString("SECOND_EMAIL_ADDRESS");
			
			this.socialLoginInfo = loadSocialUserInfo(conn, this.email, this.getParentPK());

			this.companyNameSignup = rs.getString("COMPANY_NAME_SIGNUP");
			
		} else {
			throw new SQLException("No such ErpCustomerInfo PK: " + this.getPK());
		}
		rs.close();
		ps.close();

		this.unsetModified();
	}

	
	private List<ErpCustomerSocialLoginModel> loadSocialUserInfo(Connection conn, String user_id, PrimaryKey primaryKey )  throws SQLException {
		
		List<ErpCustomerSocialLoginModel> socialLoginInfo = new ArrayList<ErpCustomerSocialLoginModel>(); // **********************************************************
		
		//PreparedStatement ps = conn.prepareStatement( "SELECT USER_TOKEN, IDENTITY_TOKEN, PROVIDER, DISPLAY_NAME, PREFERRED_USER_NAME, EMAIL, EMAIL_VERIFIED FROM CUST.CUST_SOCIAL_LINK WHERE USER_ID = ?");
		PreparedStatement ps = conn.prepareStatement( "SELECT USER_TOKEN, IDENTITY_TOKEN, PROVIDER, DISPLAY_NAME, PREFERRED_USER_NAME, EMAIL, EMAIL_VERIFIED FROM CUST.EXTERNAL_ACCOUNT_LINK WHERE CUSTOMER_ID = ?");
		ps.setString(1, primaryKey.getId());
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {		
			String user_token = rs.getString("USER_TOKEN");
			String identity_token = rs.getString("IDENTITY_TOKEN");
			String provider = rs.getString("PROVIDER");
			String display_name = rs.getString("DISPLAY_NAME");
			String preferred_user_name = rs.getString("PREFERRED_USER_NAME");
			String email = rs.getString("EMAIL");

			ErpCustomerSocialLoginModel socialLoginModel = new ErpCustomerSocialLoginModel( user_id,  
																							user_token,
																							identity_token,  
																							provider,  
																							display_name,
																							preferred_user_name,  
																							email, 
																							false);
			
			socialLoginInfo.add(socialLoginModel);
		} 
		
		rs.close();
		ps.close();

		this.unsetModified();		
		
		return socialLoginInfo;
	}
	

	private List<ErpCustomerSocialLoginModel> loadSocialUserInfo(Connection conn, String user_id)  throws SQLException {
		
		List<ErpCustomerSocialLoginModel> socialLoginInfo = new ArrayList<ErpCustomerSocialLoginModel>(); // **********************************************************
		
		//PreparedStatement ps = conn.prepareStatement( "SELECT USER_TOKEN, IDENTITY_TOKEN, PROVIDER, DISPLAY_NAME, PREFERRED_USER_NAME, EMAIL, EMAIL_VERIFIED FROM CUST.CUST_SOCIAL_LINK WHERE USER_ID = ?");
		PreparedStatement ps = conn.prepareStatement( "SELECT USER_TOKEN, IDENTITY_TOKEN, PROVIDER, DISPLAY_NAME, PREFERRED_USER_NAME, EMAIL, EMAIL_VERIFIED FROM CUST.EXTERNAL_ACCOUNT_LINK WHERE USER_ID = ?");
		ps.setString(1, user_id);
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {		
			String user_token = rs.getString("USER_TOKEN");
			String identity_token = rs.getString("IDENTITY_TOKEN");
			String provider = rs.getString("PROVIDER");
			String display_name = rs.getString("DISPLAY_NAME");
			String preferred_user_name = rs.getString("PREFERRED_USER_NAME");
			String email = rs.getString("EMAIL");

			ErpCustomerSocialLoginModel socialLoginModel = new ErpCustomerSocialLoginModel( user_id,  
																							user_token,
																							identity_token,  
																							provider,  
																							display_name,
																							preferred_user_name,  
																							email, 
																							false);
			
			socialLoginInfo.add(socialLoginModel);
		} 
		
		rs.close();
		ps.close();

		this.unsetModified();		
		
		return socialLoginInfo;
	}
		
	
	public void store(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.CUSTOMERINFO SET TITLE=?, FIRST_NAME=?, MIDDLE_NAME=?, LAST_NAME=?, EMAIL=?, ALT_EMAIL=?, "
				+ " EMAIL_PLAIN_TEXT=?, RECEIVE_NEWS=?, HOME_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), "
				+ " HOME_EXT=?, BUSINESS_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), BUSINESS_EXT=?, "
				+ " CELL_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), CELL_EXT=?, "
				+ " OTHER_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), OTHER_EXT=?, "
				+ " FAX=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), FAX_EXT=? , WORK_DEPARTMENT=?, EMPLOYEE_ID=?, "
				+ " REMINDER_LAST_SEND=?, REMINDER_FREQUENCY=?, REMINDER_DAY_OF_WEEK=?, REMINDER_ALT_EMAIL=?, RSV_DAY_OF_WEEK=?, "
				+ " RSV_START_TIME=?, RSV_END_TIME=?, RSV_ADDRESS_ID=?, UNSUBSCRIBE_DATE=?, REG_REF_TRACKING_CODE=?, REG_REF_PROG_ID=?, "
				+ " REF_PROG_INVT_ID=?, RECEIVE_OPTINNEWSLETTER=?, HAS_AUTORENEW_DP=?, AUTORENEW_DP_TYPE=?, "
				+ " EMAIL_LEVEL=?, NO_CONTACT_MAIL=?, NO_CONTACT_PHONE=?,"
				+ " mobile_number=?, delivery_notification=?, offers_notification=?, ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=?,SMS_PREFERENCE_FLAG=?, go_green=?, display_name=?,"
				+ " DP_TC_VIEWS=?, DP_TC_AGREE_DATE=?,INDUSTRY=?,NUM_OF_EMPLOYEES=?,SECOND_EMAIL_ADDRESS=?, SMS_OPTIN_DATE=? ,"
				+ " COMPANY_NAME_SIGNUP=?"
				+" WHERE CUSTOMER_ID=?");
		//ps.setString(, this.getPK().getId() );

		ps.setString(1, this.title);
		ps.setString(2, this.firstName);
		ps.setString(3, this.middleName);
		ps.setString(4, this.lastName);

		ps.setString(5, this.email);
		ps.setString(6, this.alternateEmail);
		ps.setString(7, (this.emailPlaintext ? "X" : " "));
		ps.setString(8, (this.receiveNewsletter ? "X" : " "));

		ps.setString(9, this.convertPhone(this.homePhone));
		ps.setString(10, this.convertExtension(this.homePhone));

		ps.setString(11, this.convertPhone(this.businessPhone));
		ps.setString(12, this.convertExtension(this.businessPhone));

		ps.setString(13, this.convertPhone(this.cellPhone));
		ps.setString(14, this.convertExtension(this.cellPhone));

		ps.setString(15, this.convertPhone(this.otherPhone));
		ps.setString(16, this.convertExtension(this.otherPhone));

		ps.setString(17, this.convertPhone(this.fax));
		ps.setString(18, this.convertExtension(this.fax));

		ps.setString(19, this.workDepartment);
		ps.setString(20, this.employeeId);

		if (this.lastReminderEmail == null) {
			ps.setNull(21, Types.TIMESTAMP);
		} else {
			ps.setDate(21, new java.sql.Date(this.lastReminderEmail.getTime()));
		}
		ps.setInt(22, this.reminderFrequency);
		ps.setInt(23, this.reminderDayOfWeek);
		ps.setString(24, (this.reminderAltEmail ? "X" : " "));
		
		ps.setInt(25, this.rsvDayOfWeek);
		if(this.rsvStartTime == null){
			ps.setNull(26, Types.TIMESTAMP);
		}else{
			ps.setTimestamp(26, new Timestamp(this.rsvStartTime.getTime()));
		}
		if(this.rsvEndTime == null){
			ps.setNull(27, Types.TIMESTAMP);
		}else{
			ps.setTimestamp(27, new Timestamp(this.rsvEndTime.getTime()));
		}
		ps.setString(28, this.rsvAddressId);

		if(this.unsubscribeDate == null){
			ps.setNull(29, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(29, new Timestamp(this.unsubscribeDate.getTime()));
		}
		
		if (this.regRefTrackingCode == null){
			ps.setNull(30, Types.VARCHAR);
		} else {
			ps.setString(30, this.regRefTrackingCode);
		}
		
		if (this.referralProgId == null){
			ps.setNull(31, Types.VARCHAR);
		} else {
			ps.setString(31, this.referralProgId);
		}

		if (this.referralProgInvtId == null){
			ps.setNull(32, Types.VARCHAR);
		} else {
			ps.setString(32, this.referralProgInvtId);
		}

		ps.setString(33, this.recieveOptinNewslettert ? "X" : "");

		
		
		if(this.hasAutoRenewDP==null) {
			ps.setNull(34, Types.CHAR);
		}
		else {
			ps.setString(34,this.hasAutoRenewDP);
		}

		if(this.autoRenewDPSKU==null) {
			ps.setNull(35, Types.VARCHAR);
		}
		else {
			ps.setString(35,this.autoRenewDPSKU);
		}
		ps.setString(36, this.receive_emailLevel);
		

		ps.setString(37, this.noContactMail ? "X" : "");

		ps.setString(38, this.noContactPhone ? "X" : "");
		
		ps.setString(39, this.convertPhone(this.mobileNumber));
		
		ps.setString(40, this.deliveryNotification?"Y":"N");
		
		ps.setString(41, this.offersNotification?"Y":"N");
		
		//Change this
		ps.setString(42, this.orderNotices.value());
		ps.setString(43, this.orderExceptions.value());
		ps.setString(44, this.offers.value());
		ps.setString(45, this.partnerMessages.value());
		ps.setString(46, this.smsNoThanksflag);
		
		ps.setString(47, "Y");
		
		ps.setString(48, this.displayName);
		
		/* APPDEV-2475 DP T&C */
		ps.setInt(49, this.dpTcViewCount);
		
		if (this.dpTcAgreeDate == null) {
			ps.setNull(50, Types.TIMESTAMP);
		} else {
			ps.setDate(50, new java.sql.Date(this.dpTcAgreeDate.getTime()));
		}
		
		ps.setString(51, this.industry);
		ps.setInt(52, this.numOfEmployees);
		ps.setString(53, this.secondEmailAddress);
		
		ps.setTimestamp(54, this.smsOptinDate!=null?new Timestamp(this.smsOptinDate.getTime()):null);

		ps.setString(55, this.companyNameSignup);
		
		ps.setString(56, this.getPK().getId());

		
        if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		this.unsetModified();
	}

	public void remove(Connection conn) throws SQLException {
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CUSTOMERINFO WHERE CUSTOMER_ID = ?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();

		this.setPK(null); // make it anonymous
	}

	public void updateCustomerNames(Connection conn, String fName, String mName, String lName) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CUSTOMERINFO SET FIRST_NAME=?, MIDDLE_NAME=?, LAST_NAME=? WHERE CUSTOMER_ID=?");

		ps.setString(1, fName);
		ps.setString(2, mName);
		ps.setString(3, lName);
		ps.setString(4, this.getPK().getId());
		
        if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		this.unsetModified();
	}	
	
	
	public PrimaryKey getPK() {
		return this.getParentPK();
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
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	

}
