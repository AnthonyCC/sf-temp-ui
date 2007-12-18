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

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.util.*;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerInfoModel;
import java.util.List;

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
	

	/**
	 * Default constructor.
	 */
	public ErpCustomerInfoPersistentBean() {
		super();
		this.title = null;
		this.firstName = null;
		this.middleName = null;
		this.lastName = null;

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
		
		rsvStartTime = null;
		rsvEndTime = null;
		rsvAddressId = null;
		
		regRefTrackingCode = null;
		this.referralProgId=null;
		this.referralProgInvtId=null;
		this.hasAutoRenewDP=null;
		this.autoRenewDPSKU=null;
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
		
		this.rsvDayOfWeek = m.getRsvDayOfWeek();
		this.rsvStartTime = m.getRsvStartTime();
		this.rsvEndTime = m.getRsvEndTime();
		this.rsvAddressId = m.getRsvAddressId();
		this.regRefTrackingCode = m.getRegRefTrackingCode();

		this.referralProgId=m.getReferralProgId();
		this.referralProgInvtId=m.getReferralProgInvtId();
		this.hasAutoRenewDP=m.getHasAutoRenewDP();
		this.autoRenewDPSKU=m.getAutoRenewDPSKU();

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
				"INSERT INTO CUST.CUSTOMERINFO (CUSTOMER_ID, TITLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL, ALT_EMAIL, EMAIL_PLAIN_TEXT, RECEIVE_NEWS, HOME_PHONE, HOME_EXT, BUSINESS_PHONE, BUSINESS_EXT, CELL_PHONE, CELL_EXT, OTHER_PHONE, OTHER_EXT, FAX, FAX_EXT, WORK_DEPARTMENT, EMPLOYEE_ID, REMINDER_LAST_SEND, REMINDER_FREQUENCY, REMINDER_DAY_OF_WEEK, REMINDER_ALT_EMAIL, RSV_DAY_OF_WEEK, RSV_START_TIME, RSV_END_TIME, RSV_ADDRESS_ID, UNSUBSCRIBE_DATE, REG_REF_TRACKING_CODE, REG_REF_PROG_ID, REF_PROG_INVT_ID, RECEIVE_OPTINNEWSLETTER) "
					+ " values (?,?,?,?,?,?,?,?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,replace(replace(replace(replace(?,'('),')'),' '),'-'),?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, this.title);
		ps.setString(3, this.firstName);
		ps.setString(4, this.middleName);
		ps.setString(5, this.lastName);
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
		
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not created");
		}
		this.setPK(this.getParentPK());

		ps.close();

		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT TITLE, FIRST_NAME, MIDDLE_NAME, LAST_NAME, EMAIL, ALT_EMAIL, EMAIL_PLAIN_TEXT, RECEIVE_NEWS, "
					+ " '('||substr(HOME_PHONE,1,3)||') '||substr(HOME_PHONE,4,3)||'-'||substr(HOME_PHONE,7,4) as HOME_PHONE, HOME_EXT, "
					+ " '('||substr(BUSINESS_PHONE,1,3)||') '||substr(BUSINESS_PHONE,4,3)||'-'||substr(BUSINESS_PHONE,7,4) as BUSINESS_PHONE, BUSINESS_EXT, "
					+ " '('||substr(CELL_PHONE,1,3)||') '||substr(CELL_PHONE,4,3)||'-'||substr(CELL_PHONE,7,4) as CELL_PHONE, CELL_EXT, "
					+ " '('||substr(OTHER_PHONE,1,3)||') '||substr(OTHER_PHONE,4,3)||'-'||substr(OTHER_PHONE,7,4) as OTHER_PHONE, OTHER_EXT, "
					+ " '('||substr(FAX,1,3)||') '||substr(FAX,4,3)||'-'||substr(FAX,7,4) as FAX, FAX_EXT, WORK_DEPARTMENT, EMPLOYEE_ID, REMINDER_LAST_SEND, REMINDER_FREQUENCY, REMINDER_DAY_OF_WEEK, REMINDER_ALT_EMAIL, RSV_DAY_OF_WEEK, RSV_START_TIME, RSV_END_TIME, RSV_ADDRESS_ID, UNSUBSCRIBE_DATE, REG_REF_TRACKING_CODE, REG_REF_PROG_ID, REF_PROG_INVT_ID, RECEIVE_OPTINNEWSLETTER, HAS_AUTORENEW_DP, AUTORENEW_DP_TYPE"
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
			
		} else {
			throw new SQLException("No such ErpCustomerInfo PK: " + this.getPK());
		}
		rs.close();
		ps.close();

		this.unsetModified();
	}

	public void store(Connection conn) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"UPDATE CUST.CUSTOMERINFO SET TITLE=?, FIRST_NAME=?, MIDDLE_NAME=?, LAST_NAME=?, EMAIL=?, ALT_EMAIL=?, EMAIL_PLAIN_TEXT=?, RECEIVE_NEWS=?, HOME_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), HOME_EXT=?, BUSINESS_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), BUSINESS_EXT=?, CELL_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), CELL_EXT=?, OTHER_PHONE=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), OTHER_EXT=?, FAX=replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), FAX_EXT=? , WORK_DEPARTMENT=?, EMPLOYEE_ID=? , REMINDER_LAST_SEND=?, REMINDER_FREQUENCY=?, REMINDER_DAY_OF_WEEK=?, REMINDER_ALT_EMAIL=?, RSV_DAY_OF_WEEK=?, RSV_START_TIME=?, RSV_END_TIME=?, RSV_ADDRESS_ID=?, UNSUBSCRIBE_DATE=?, REG_REF_TRACKING_CODE=?, REG_REF_PROG_ID=?, REF_PROG_INVT_ID=?, RECEIVE_OPTINNEWSLETTER=?,HAS_AUTORENEW_DP=?,AUTORENEW_DP_TYPE=?  WHERE CUSTOMER_ID=?");
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
		ps.setString(36, this.getPK().getId());

		
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

	public PrimaryKey getPK() {
		return this.getParentPK();
	}

}
