/* Generated by Together */

package com.freshdirect.customer.ejb;

import java.sql.*;
import java.util.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.util.*;
import com.freshdirect.customer.*;
import com.freshdirect.common.address.*;

/**
 * ErpDeliveryInfo persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpDeliveryInfoPersistentBean extends ErpReadOnlyPersistentBean {

	private ErpDeliveryInfoModel model;
	
	/** Default constructor. */
	public ErpDeliveryInfoPersistentBean() {
		super();
		this.model = new ErpDeliveryInfoModel();
	}

	/** Load constructor. */
	public ErpDeliveryInfoPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/** Load constructor. */
	public ErpDeliveryInfoPersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}
	/**
	 * Copy constructor, from model.
	 * @param bean ErpDeliveryInfoModel to copy from
	 */
	public ErpDeliveryInfoPersistentBean(ErpDeliveryInfoModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpDeliveryInfoModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpDeliveryInfoModel)model;
	}

	/**
	 * Find ErpDeliveryInfoPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpDeliveryInfoPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.DELIVERYINFO WHERE SALESACTION_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpDeliveryInfoPersistentBean bean = new ErpDeliveryInfoPersistentBean(new PrimaryKey(rs.getString("SALESACTION_ID")), rs);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}

	private final static String STORE_DELIVERY_INFO =
		"INSERT INTO CUST.DELIVERYINFO (SALESACTION_ID, RESERVATION_ID, STARTTIME, ENDTIME, CUTOFFTIME, ZONE, DEPOTLOCATION_ID," +
		" FIRST_NAME, LAST_NAME, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP, COUNTRY, PHONE, PHONE_EXT, DELIVERY_INSTRUCTIONS," +
		"SCRUBBED_ADDRESS, ALT_DEST, ALT_FIRST_NAME, ALT_LAST_NAME, ALT_APARTMENT, ALT_PHONE, ALT_PHONE_EXT, DELIVERY_TYPE," +
		"ALT_CONTACT_PHONE, ALT_CONTACT_EXT, GEOLOC, UNATTENDED_INSTR,CHARITY_NAME,COMPANY_NAME) " +
        " values (?,?,?,?,?,?,?,?,?,?,REPLACE(REPLACE(UPPER(?),'-'),' '),?,?,?,?,?,replace(replace(replace(replace(replace(?,'('),')')," +
        "' '),'-'),'.'),?,?,?,?,?,?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.')," +
        "?,?,replace(replace(replace(replace(replace(?,'('),')'),' '),'-'),'.'),?," +
        "MDSYS.SDO_GEOMETRY(2001, 8265, MDSYS.SDO_POINT_TYPE (?, ?,NULL),NULL,NULL),?,?,?)";
		
	public PrimaryKey create(Connection conn) throws SQLException {
		//String id = this.getNextId(conn);
		PreparedStatement ps = conn.prepareStatement(STORE_DELIVERY_INFO);
		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, this.model.getDeliveryReservationId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getDeliveryStartTime().getTime()));
		ps.setTimestamp(4, new java.sql.Timestamp(this.model.getDeliveryEndTime().getTime()));
		ps.setTimestamp(5, new java.sql.Timestamp(this.model.getDeliveryCutoffTime().getTime()));
		ps.setString(6, this.model.getDeliveryZone());
		ps.setString(7, this.model.getDepotLocationId());
		ErpAddressModel address = this.model.getDeliveryAddress();
		ps.setString(8, address.getFirstName());
		ps.setString(9, address.getLastName());
		ps.setString(10, address.getAddress1());
		ps.setString(11, address.getAddress2());
		ps.setString(12, address.getApartment());
		ps.setString(13, address.getCity());
		ps.setString(14, address.getState());
		ps.setString(15, address.getZipCode());
		ps.setString(16, address.getCountry());
		ps.setString(17, address.getPhone().getPhone());
		ps.setString(18, address.getPhone().getExtension());
		ps.setString(19, address.getInstructions());
		ps.setString(20, address.getScrubbedStreet());
        
		if(address.getAltDelivery()!= null){
			ps.setString(21, address.getAltDelivery().getDeliveryCode());
		}else{
			ps.setString(21, "");
		}
		ps.setString(22, address.getAltFirstName());
		ps.setString(23, address.getAltLastName());				
		ps.setString(24, address.getAltApartment());
		
		if(address.getAltPhone()!= null){
			ps.setString(25, address.getAltPhone().getPhone());
			ps.setString(26, address.getAltPhone().getExtension());		
		}else{
			ps.setString(25, "");
			ps.setString(26, "");				
		}
        ps.setString(27, this.model.getDeliveryType().getCode());
        
        if(address.getAltContactPhone() != null) {
        	ps.setString(28, address.getAltContactPhone().getPhone());
        	ps.setString(29, address.getAltContactPhone().getExtension());
        }else{
			ps.setString(28, "");
			ps.setString(29, "");				
		}
        
		ps.setDouble(30, address.getLongitude());
		ps.setDouble(31, address.getLatitude());
		
		String unattendedDeliveryInstructions = null;
		
		if (EnumUnattendedDeliveryFlag.OPT_IN.equals(address.getUnattendedDeliveryFlag())) {
			unattendedDeliveryInstructions = address.getUnattendedDeliveryInstructions();
			if (unattendedDeliveryInstructions == null) unattendedDeliveryInstructions = "OK";
		}
		
		ps.setString(32, unattendedDeliveryInstructions);
		ps.setString(33, address.getCharityName());
		ps.setString(34, address.getCompanyName());
//		ps.setString(35, address.isOptInForDonation()?"Y":"N");

        
		
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(this.getParentPK());
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
			ps = null;
		}

		this.unsetModified();
		return this.getPK();
	}

	private static final String LOAD_DELIVERY_INFO =
		"SELECT RESERVATION_ID, STARTTIME, ENDTIME, CUTOFFTIME, ZONE, DEPOTLOCATION_ID, FIRST_NAME, LAST_NAME," +
		"ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP, COUNTRY, " +
		"'('||substr(PHONE,1,3)||') '||substr(PHONE,4,3)||'-'||substr(PHONE,7,4) as PHONE, PHONE_EXT, DELIVERY_INSTRUCTIONS," +
		"SCRUBBED_ADDRESS, ALT_DEST, ALT_FIRST_NAME, ALT_LAST_NAME, ALT_APARTMENT, " +
		"'('||substr(ALT_PHONE,1,3)||') '||substr(ALT_PHONE,4,3)||'-'||substr(ALT_PHONE,7,4) AS ALT_PHONE, ALT_PHONE_EXT, " +
		"DELIVERY_TYPE, ALT_CONTACT_PHONE, ALT_CONTACT_EXT, UNATTENDED_INSTR FROM  CUST.DELIVERYINFO WHERE SALESACTION_ID=?";
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(LOAD_DELIVERY_INFO);
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(rs);
		} else {
			throw new SQLException("No such ErpDeliveryInfo PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
	}
    
	private final PhoneNumber convertPhoneNumber(String phone, String extension) {
		return "() -".equals(phone) ? null : new PhoneNumber(phone, NVL.apply(extension, ""));
	}

	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setDeliveryReservationId(rs.getString("RESERVATION_ID"));
		this.model.setDeliveryStartTime(rs.getTimestamp("STARTTIME"));
		this.model.setDeliveryEndTime(rs.getTimestamp("ENDTIME"));
		this.model.setDeliveryCutoffTime(rs.getTimestamp("CUTOFFTIME"));
		this.model.setDeliveryZone(rs.getString("ZONE"));
		this.model.setDepotLocationId(rs.getString("DEPOTLOCATION_ID"));
		ErpAddressModel address = new ErpAddressModel();
		address.setFirstName(rs.getString("FIRST_NAME"));
		address.setLastName(rs.getString("LAST_NAME"));
		address.setAddress1(rs.getString("ADDRESS1"));
		address.setAddress2(rs.getString("ADDRESS2"));
		address.setApartment(rs.getString("APARTMENT"));
		address.setCity(rs.getString("CITY"));
		address.setState(rs.getString("STATE"));
		address.setZipCode(rs.getString("ZIP"));
		address.setCountry(rs.getString("COUNTRY"));
		address.setPhone( this.convertPhoneNumber(rs.getString("PHONE"), rs.getString("PHONE_EXT")) );
		address.setInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
		AddressInfo info = new AddressInfo();
		info.setScrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));
		address.setAddressInfo(info);

		address.setAltDelivery(EnumDeliverySetting.getDeliverySetting(rs.getString("ALT_DEST")));
		address.setAltFirstName(rs.getString("ALT_FIRST_NAME"));
		address.setAltLastName(rs.getString("ALT_LAST_NAME"));
		address.setAltApartment(rs.getString("ALT_APARTMENT")); 
		address.setAltPhone(this.convertPhoneNumber(rs.getString("ALT_PHONE"), rs.getString("ALT_PHONE_EXT")));
		this.model.setDeliveryType(EnumDeliveryType.getDeliveryType(rs.getString("DELIVERY_TYPE")));
		address.setAltContactPhone(this.convertPhoneNumber(rs.getString("ALT_CONTACT_PHONE"), rs.getString("ALT_CONTACT_EXT")));
		
		if (this.model.getDepotLocationId()!=null && !"".equals(this.model.getDepotLocationId()) ) {
			ErpDepotAddressModel depotAddress = new ErpDepotAddressModel(address);
			depotAddress.setPickup(EnumDeliveryType.PICKUP.equals(this.model.getDeliveryType()));
			depotAddress.setLocationId(this.model.getDepotLocationId());
			depotAddress.setInstructions(address.getInstructions());
			this.model.setDeliveryAddress(depotAddress);
		} else {
			this.model.setDeliveryAddress(address);
		}
		
		String unattendedDeliveryInstructions = rs.getString("UNATTENDED_INSTR");
		if (unattendedDeliveryInstructions != null) {
			address.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.OPT_IN);
			address.setUnattendedDeliveryInstructions(unattendedDeliveryInstructions);
		}
				

		// load children here
		this.unsetModified();
	}

	public PrimaryKey getPK(){
	    return this.getParentPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
}
