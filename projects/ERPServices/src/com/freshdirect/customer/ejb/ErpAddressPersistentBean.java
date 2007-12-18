/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;

/**
 * ErpAddress persistent bean.
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpAddressPersistentBean extends DependentPersistentBeanSupport {

	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String apartment;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private PhoneNumber phone;
	private PhoneNumber altContactPhone;
	private String instructions;
	private AddressInfo addressInfo;
	private EnumServiceType serviceType;
	private String companyName;
	
	private EnumDeliverySetting altDeliverySetting;	
	private String altFirstName;
	private String altLastName;
	private String altApartment;
	private PhoneNumber altPhone;	
	
	private EnumUnattendedDeliveryFlag unattendedDeliveryFlag;
	private String unattendedDeliveryInstructions;
	
	

	/**
	 * Default constructor.
	 */
	public ErpAddressPersistentBean() {
		super();
		this.firstName = "";
		this.lastName = "";
		this.address1 = "";
		this.address2 = "";
		this.apartment = "";
		this.city = "";
		this.state = "";
		this.zipCode = "";
		this.country = "";
		this.phone = new PhoneNumber("");
		this.altContactPhone = new PhoneNumber("");
		this.instructions = "";
		this.addressInfo = new AddressInfo();
		this.serviceType = null;
		this.companyName = "";

		this.altDeliverySetting = EnumDeliverySetting.NONE;
		this.altFirstName = "";
		this.altLastName = "";
		this.altApartment = "";
		this.altPhone = new PhoneNumber("");		
		
		this.unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.NOT_SEEN;
		this.unattendedDeliveryInstructions = "";
	}

	/**
	 * Load constructor.
	 */
	public ErpAddressPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpAddressModel to copy from
	 */
	public ErpAddressPersistentBean(ErpAddressModel model) {
		super(model.getPK());
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpAddressModel object.
	 */
	public ModelI getModel() {
		ErpAddressModel model = new ErpAddressModel();
		super.decorateModel(model);
		model.setFirstName(this.firstName);
		model.setLastName(this.lastName);
		model.setAddress1(this.address1);
		model.setAddress2(this.address2);
		model.setApartment(this.apartment);
		model.setCity(this.city);
		model.setState(this.state);
		model.setZipCode(this.zipCode);
		model.setCountry(this.country);
		model.setPhone(this.phone);
		model.setAltContactPhone(this.altContactPhone);
		model.setInstructions(this.instructions);
		model.setAddressInfo(this.addressInfo);
		model.setServiceType(this.serviceType);
		model.setCompanyName(this.companyName);
		model.setAltDelivery(this.altDeliverySetting);
		model.setAltFirstName(this.altFirstName);
		model.setAltLastName(this.altLastName);
		model.setAltApartment(this.altApartment);
		model.setAltPhone(this.altPhone);		
		model.setUnattendedDeliveryFlag(this.unattendedDeliveryFlag);
		model.setUnattendedDeliveryInstructions(this.unattendedDeliveryInstructions);
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		ErpAddressModel m = (ErpAddressModel) model;
		this.firstName = m.getFirstName();
		this.lastName = m.getLastName();
		this.address1 = m.getAddress1();
		this.address2 = m.getAddress2();
		this.apartment = m.getApartment();
		this.city = m.getCity();
		this.state = m.getState();
		this.zipCode = m.getZipCode();
		this.country = m.getCountry();
		this.phone = m.getPhone();
		this.altContactPhone = m.getAltContactPhone();
		this.instructions = m.getInstructions();
		this.addressInfo = m.getAddressInfo();
		this.serviceType = m.getServiceType();
		this.companyName = m.getCompanyName();
		this.altDeliverySetting = m.getAltDelivery()==null ? EnumDeliverySetting.NONE : m.getAltDelivery();

		this.altFirstName = m.getAltFirstName();
		this.altLastName = m.getAltLastName();
		this.altApartment = m.getAltApartment();
		this.altPhone = m.getAltPhone();
		
		this.unattendedDeliveryFlag = m.getUnattendedDeliveryFlag();
		this.unattendedDeliveryInstructions = m.getUnattendedDeliveryInstructions();
		
		this.setModified();
	}


	/**
	 * Find ErpAddressPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpAddressPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException { 
	    java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.ADDRESS WHERE CUSTOMER_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpAddressPersistentBean bean = new ErpAddressPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}

	private final String convertPhone(PhoneNumber phoneNumber) {
		return phoneNumber==null ? null : phoneNumber.getPhone();
	}

	private final String convertExtension(PhoneNumber phoneNumber) {
		return phoneNumber==null ? null : phoneNumber.getExtension();
	}

	private final PhoneNumber convertPhoneNumber(String phone, String extension) {
		return "() -".equals(phone) ? null : new PhoneNumber(phone, NVL.apply(extension, ""));
	}
	
	
	private final static String STORE_ADDRESS_QUERY =
		"INSERT INTO CUST.ADDRESS (" +
		   "ID ,CUSTOMER_ID, FIRST_NAME, LAST_NAME, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP," +
		   "COUNTRY, PHONE, PHONE_EXT, DELIVERY_INSTRUCTIONS, SCRUBBED_ADDRESS, ALT_DEST, ALT_FIRST_NAME," +
		   "ALT_LAST_NAME, ALT_APARTMENT, ALT_PHONE, ALT_PHONE_EXT, LONGITUDE, LATITUDE, GEOLOC, SERVICE_TYPE," +
		   "COMPANY_NAME, ALT_CONTACT_PHONE, ALT_CONTACT_EXT, UNATTENDED_FLAG, UNATTENDED_INSTR) " +
        " values (?,?,?,?,?,?,REPLACE(REPLACE(UPPER(?),'-'),' '),?,?,?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,?,?,?,?,?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,?,?,MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL),?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?,?,?)";

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(STORE_ADDRESS_QUERY);

		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.firstName);
		ps.setString(4, this.lastName);
		ps.setString(5, this.address1);
		ps.setString(6, ("".equals(this.address2) ? " " : this.address2));
		ps.setString(7, ("".equals(this.apartment) ? " " : this.apartment));
		ps.setString(8, this.city);
		ps.setString(9, this.state);
		ps.setString(10, this.zipCode);
		ps.setString(11, this.country);
		ps.setString(12, this.convertPhone(this.phone));
		ps.setString(13, this.convertExtension(this.phone));
		ps.setString(14, this.instructions);
		ps.setString(15, this.addressInfo.getScrubbedStreet());
		ps.setString(16, this.altDeliverySetting.getDeliveryCode());
		ps.setString(17, this.altFirstName);
		ps.setString(18, this.altLastName);				
		ps.setString(19, this.altApartment);
		ps.setString(20, this.convertPhone(this.altPhone));
		ps.setString(21, this.convertExtension(this.altPhone));
		ps.setDouble(22, this.addressInfo.getLongitude());
		ps.setDouble(23, this.addressInfo.getLatitude());
		ps.setDouble(24, this.addressInfo.getLongitude());
		ps.setDouble(25, this.addressInfo.getLatitude());
		if(this.serviceType == null){
			ps.setNull(26, Types.VARCHAR);
		} else {
			ps.setString(26, this.serviceType.getName());
		}
		ps.setString(27, this.companyName);
		ps.setString(28, this.convertPhone(this.altContactPhone));
		ps.setString(29, this.convertExtension(this.altContactPhone));
		
		ps.setString(30, this.unattendedDeliveryFlag != null ? this.unattendedDeliveryFlag.toSQLValue() : null);
		ps.setString(31, this.unattendedDeliveryInstructions);
				
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {	
			ps.close();
			ps = null;
		}

		// create children here

		this.unsetModified();
		return this.getPK();
	}

	private final static String LOAD_ADDRESS_QUERY = 
		"SELECT FIRST_NAME, LAST_NAME, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP, COUNTRY," +
		"'('||substr(PHONE,1,3)||') '||substr(PHONE,4,3)||'-'||substr(PHONE,7,4) AS PHONE, PHONE_EXT," +
		"DELIVERY_INSTRUCTIONS, SCRUBBED_ADDRESS, NVL(ALT_DEST,'none') as ALT_DEST, ALT_FIRST_NAME," +
		"ALT_LAST_NAME, ALT_APARTMENT, '('||substr(ALT_PHONE,1,3)||') '||substr(ALT_PHONE,4,3)||'-'||substr(ALT_PHONE,7,4) AS ALT_PHONE," +
		"ALT_PHONE_EXT, LONGITUDE, LATITUDE, SERVICE_TYPE, COMPANY_NAME," +
		"'('||substr(ALT_CONTACT_PHONE,1,3)||') '||substr(ALT_CONTACT_PHONE,4,3)||'-'||substr(ALT_CONTACT_PHONE,7,4) AS ALT_CONTACT_PHONE," +
		"ALT_CONTACT_EXT, UNATTENDED_FLAG, UNATTENDED_INSTR FROM CUST.ADDRESS WHERE ID=?";
		
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_ADDRESS_QUERY);
		
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.firstName = rs.getString("FIRST_NAME");
			this.lastName = rs.getString("LAST_NAME");
			this.address1 = rs.getString("ADDRESS1");
			this.address2 = NVL.apply(rs.getString("ADDRESS2"), "").trim();
			this.apartment = NVL.apply(rs.getString("APARTMENT"), "").trim();
			this.city = rs.getString("CITY");
			this.state = rs.getString("STATE");
			this.zipCode = rs.getString("ZIP");
			this.country = rs.getString("COUNTRY");
			this.phone = this.convertPhoneNumber( rs.getString("PHONE"), rs.getString("PHONE_EXT") );
			this.instructions = rs.getString("DELIVERY_INSTRUCTIONS");
			this.addressInfo.setScrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));
			this.altDeliverySetting = EnumDeliverySetting.getDeliverySetting(rs.getString("ALT_DEST"));
			this.altFirstName = rs.getString("ALT_FIRST_NAME");
			this.altLastName = rs.getString("ALT_LAST_NAME");
			this.altApartment = rs.getString("ALT_APARTMENT");
			this.altPhone = this.convertPhoneNumber( rs.getString("ALT_PHONE"), rs.getString("ALT_PHONE_EXT") );
			this.addressInfo.setLongitude(rs.getDouble("LONGITUDE"));
			this.addressInfo.setLatitude(rs.getDouble("LATITUDE"));
			this.serviceType = EnumServiceType.getEnum(rs.getString("SERVICE_TYPE"));
			this.companyName = rs.getString("COMPANY_NAME");
			this.altContactPhone = this.convertPhoneNumber(rs.getString("ALT_CONTACT_PHONE"), rs.getString("ALT_CONTACT_EXT"));
			this.unattendedDeliveryFlag = EnumUnattendedDeliveryFlag.fromSQLValue(rs.getString("UNATTENDED_FLAG"));
			this.unattendedDeliveryInstructions = rs.getString("UNATTENDED_INSTR");
		} else {
			throw new SQLException("No such ErpAddress PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		// load children here

		this.unsetModified();
	}

	private final static String UPDATE_ADDRESS_QUERY =
		"UPDATE CUST.ADDRESS SET CUSTOMER_ID = ?, FIRST_NAME = ?, LAST_NAME = ?, ADDRESS1 = ?, ADDRESS2 = ?," +
			"APARTMENT = REPLACE(REPLACE(UPPER(?),'-'),' '), CITY = ?, STATE = ?, ZIP = ?, COUNTRY = ?," +
			"PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'), PHONE_EXT = ?," +
			"DELIVERY_INSTRUCTIONS = ?, SCRUBBED_ADDRESS = ?, ALT_DEST = ?, ALT_FIRST_NAME = ?, ALT_LAST_NAME = ?," +
			"ALT_APARTMENT = ?, ALT_PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.')," +
			"ALT_PHONE_EXT = ?, LONGITUDE = ?, LATITUDE = ?," +
			"GEOLOC=MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL), SERVICE_TYPE = ?," +
			"COMPANY_NAME = ?, ALT_CONTACT_PHONE = replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.')," +
			"ALT_CONTACT_EXT = ?, UNATTENDED_FLAG = ?, UNATTENDED_INSTR = ? WHERE ID = ?";
		
	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(UPDATE_ADDRESS_QUERY);
		
		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, this.firstName);
		ps.setString(3, this.lastName);
		ps.setString(4, this.address1);
		ps.setString(5, ("".equals(this.address2)? " " : this.address2));
		ps.setString(6, ("".equals(this.apartment) ? " " : this.apartment));
		ps.setString(7, this.city);
		ps.setString(8, this.state);
		ps.setString(9, this.zipCode);
		ps.setString(10, this.country);
		ps.setString(11, this.convertPhone(this.phone));
		ps.setString(12, this.convertExtension(this.phone));
		ps.setString(13, this.instructions);
		ps.setString(14, this.addressInfo.getScrubbedStreet());
		ps.setString(15, this.altDeliverySetting.getDeliveryCode());
		ps.setString(16, this.altFirstName);
		ps.setString(17, this.altLastName);
		ps.setString(18, ("".equals(this.altApartment) ? " " : this.altApartment));
		ps.setString(19, this.convertPhone(this.altPhone));		
		ps.setString(20, this.convertExtension(this.altPhone));
		ps.setDouble(21, this.addressInfo.getLongitude());
		ps.setDouble(22, this.addressInfo.getLatitude());
		ps.setDouble(23, this.addressInfo.getLongitude());
		ps.setDouble(24, this.addressInfo.getLatitude());
		if(this.serviceType == null){
			ps.setNull(25, Types.VARCHAR);
		}else{
			ps.setString(25, this.serviceType.getName());
		}
		ps.setString(26, this.companyName);
		ps.setString(27, this.convertPhone(this.altContactPhone));
		ps.setString(28, this.convertExtension(this.altContactPhone));
		
		ps.setString(29, this.unattendedDeliveryFlag.toSQLValue());
		ps.setString(30, this.unattendedDeliveryInstructions);
		
		ps.setString(31, this.getPK().getId());
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;
		this.unsetModified();

		// store children here
	}

	public void remove(Connection conn) throws SQLException {

		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.ADDRESS WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
		ps = null;

		this.setPK(null); // make it anonymous
	}

}
