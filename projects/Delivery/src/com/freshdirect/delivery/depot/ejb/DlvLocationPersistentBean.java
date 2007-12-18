/*
 * 
 * DlvLocationPersistentBean.java
 * Date: Jul 23, 2002 Time: 4:29:05 PM
 */

package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */

import java.sql.*;
import java.util.List;
import java.util.Date;

import com.freshdirect.framework.core.*;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.depot.*;

public class DlvLocationPersistentBean extends DependentPersistentBeanSupport {
	
	private Date startDate;
	private Date endDate;
	
	private String facility;
	private AddressModel address;
	private String instructions;
	private String zoneCode;
    private boolean deliveryChargeWaived;
	
	/** 
	 * Creates new DlvLocationPersistentBean 
	 */
    public DlvLocationPersistentBean() {
        super();
    }
    
    /** 
     * Creates new DlvLocationPersistentBean 
     */
    public DlvLocationPersistentBean(PrimaryKey pk, Date startDate, Date endDate, String facility, AddressModel address, String instructions, String zoneCode, boolean deliveryChargeWaived) {
        super(pk);
        this.startDate = startDate;
        this.endDate = endDate;
        this.facility = facility;
        this.address = address;
        this.instructions = instructions;
        this.zoneCode = zoneCode;
        this.deliveryChargeWaived = deliveryChargeWaived;
    }
    
    /**
     * Load constructor.
     */
    public DlvLocationPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        load(conn);
    }
    
    /**
     * Copy constructor, from model.
     *
     * @param bean DlvLoacationModel to copy from
     */
    public DlvLocationPersistentBean(DlvLocationModel model) {
        super(model.getPK());
        this.setFromModel(model);
    }
    
    /** 
     * gets a model representing the state of an object
     * 
     * @return the model representing the state of an object
     */
    public ModelI getModel(){
		DlvLocationModel model = new DlvLocationModel();
		model.setStartDate(this.startDate);
		model.setEndDate(this.endDate);
		model.setFacility(this.facility);
		model.setAddress(this.address);
		model.setInstructions(this.instructions);
		model.setZoneCode(this.zoneCode);  
        model.setDeliveryChargeWaived(this.deliveryChargeWaived);
        super.decorateModel(model);
        return model;
    }
    
    /** 
     * sets the properties of an object by copying the properties from a model
     * 
     * @param model the model to copy
     */
    public void setFromModel(ModelI model){
        DlvLocationModel m = (DlvLocationModel)model;
        this.startDate = m.getStartDate();
        this.endDate = m.getEndDate();
        this.facility = m.getFacility();
		this.address = m.getAddress();
		this.instructions = m.getInstructions();
		this.zoneCode = m.getZoneCode();
		this.deliveryChargeWaived = m.getDeliveryChargeWaived();
		this.setModified();
    }
    
    /** 
     * writes a new object to the persistent store for the first time
     * 
     * @param conn a SQLConnection to use to write this object
     * @throws SQLException any problems encountered while writing this object
     * @return the unique identity of this new object
     */
    public PrimaryKey create(Connection conn) throws SQLException {
        String id = this.getNextId(conn, "DLV");
        PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO DLV.LOCATION (ID, DEPOT_ID, START_DATE, END_DATE, FACILITY, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIPCODE, INSTRUCTIONS, ZONE_CODE, DELIVERY_CHARGE_WAIVED) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)" );
        ps.setString(1, id);
        ps.setString(2, this.getParentPK().getId());
        ps.setDate(3, new java.sql.Date(this.startDate.getTime()));
        ps.setDate(4, new java.sql.Date(this.endDate.getTime()));
		ps.setString(5, this.facility);
		ps.setString(6, this.address.getAddress1());
		ps.setString(7, this.address.getAddress2());
		ps.setString(8, this.address.getApartment());
		ps.setString(9, this.address.getCity());
		ps.setString(10, this.address.getState());
		ps.setString(11, this.address.getZipCode());
		ps.setString(12, this.instructions);
		ps.setString(13, this.zoneCode);
        ps.setString(14, this.deliveryChargeWaived?"X":"");
	    try {
            ps.executeUpdate();
            this.setPK(new PrimaryKey(id));
        } catch (SQLException se) {
            throw se;
        } finally {
            ps.close();
            ps = null;
        }
        this.unsetModified();
        return this.getPK();
    }
    
    /** 
     * reads an object from the persistent store
     * 
     * @param conn a SQLConnection to use when reading this object
     * @throws SQLException any problems entountered while reading this object
     */
    public void load(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT START_DATE, END_DATE, FACILITY, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIPCODE, INSTRUCTIONS, ZONE_CODE, DELIVERY_CHARGE_WAIVED FROM DLV.LOCATION WHERE ID=?");
        ps.setString(1, this.getPK().getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	this.startDate = rs.getDate("START_DATE");
        	this.endDate = rs.getDate("END_DATE");
           	this.facility = rs.getString("FACILITY");
           
           	AddressModel address = new AddressModel();
           	address.setAddress1(rs.getString("ADDRESS1"));
           	address.setAddress2(rs.getString("ADDRESS2"));
           	address.setApartment(rs.getString("APARTMENT"));
           	address.setCity(rs.getString("CITY"));
           	address.setState(rs.getString("STATE"));
           	address.setZipCode(rs.getString("ZIPCODE"));
           	this.address = address;
           
           	this.instructions = rs.getString("INSTRUCTIONS");
           	this.zoneCode = rs.getString("ZONE_CODE");
            this.deliveryChargeWaived = "X".equals(rs.getString("DELIVERY_CHARGE_WAIVED"));
            
        } else {
            throw new SQLException("No such DlvLocationsPersistentBean PK: " + this.getPK() );
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        this.unsetModified();
    }
    
    /** 
     * removes this object from the persistent store
     * 
     * @param conn a SQLConnection to use when removing this object
     * @throws SQLException any problems entountered while removing this object
     */
    public void remove(Connection conn) throws SQLException {
        // remove self
        PreparedStatement ps = conn.prepareStatement("DELETE FROM DLV.LOCATION WHERE ID = ?");
        ps.setString(1, this.getPK().getId() );
        ps.executeUpdate();
        ps.close();
        ps = null;
        this.setPK(null); // make it anonymous
    }
    
    /** 
     * saves this object's state to the persistent store
     * 
     * @param conn a SQLConnection to use when saving this object
     * @throws SQLException any problems entountered while saving this object
     */
    public void store(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE DLV.LOCATION SET DEPOT_ID = ?, START_DATE = ?, END_DATE = ?, FACILITY = ?, ADDRESS1 = ?, ADDRESS2 = ?, APARTMENT = ?, CITY = ?, STATE = ?, ZIPCODE = ?, INSTRUCTIONS = ?, ZONE_CODE = ?, DELIVERY_CHARGE_WAIVED = ? WHERE ID = ?");
        ps.setString(1, this.getParentPK().getId());
        ps.setDate(2, new java.sql.Date(this.startDate.getTime()));
        ps.setDate(3, new java.sql.Date(this.endDate.getTime()));
		ps.setString(4, this.facility);
		ps.setString(5, this.address.getAddress1());
		ps.setString(6, this.address.getAddress2());
		ps.setString(7, this.address.getApartment());
		ps.setString(8, this.address.getCity());
		ps.setString(9, this.address.getState());
		ps.setString(10, this.address.getZipCode());
		ps.setString(11, this.instructions);
		ps.setString(12, this.zoneCode);
        ps.setString(13, this.deliveryChargeWaived?"X":"");
		ps.setString(14, this.getPK().getId());
		
        ps.executeUpdate();
        ps.close();
        ps = null;
        this.unsetModified();
    }
    
    /**
     * Find DlvLocationPersistentBean objects for a given parent.
     *
     * @param conn the database connection to operate on
     * @param parentPK primary key of parent
     * @return a List of DlvLocationPersistentBean objects (empty if found none).
     * @throws SQLException if any problems occur talking to the database
     */
    public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
        java.util.List lst = new java.util.LinkedList();
        PreparedStatement ps = conn.prepareStatement("SELECT ID, START_DATE, END_DATE, FACILITY, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIPCODE, INSTRUCTIONS, ZONE_CODE, DELIVERY_CHARGE_WAIVED FROM DLV.LOCATION WHERE DEPOT_ID = ?");
        ps.setString(1, parentPK.getId());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String id = rs.getString("ID");
            Date startDate = rs.getDate("START_DATE");
            Date endDate = rs.getDate("END_DATE");
            String facility = rs.getString("FACILITY");
			AddressModel address = new AddressModel();
			address.setAddress1(rs.getString("ADDRESS1"));
			address.setAddress2(rs.getString("ADDRESS2"));
			address.setApartment(rs.getString("APARTMENT"));
			address.setCity(rs.getString("CITY"));
			address.setState(rs.getString("STATE"));
			address.setZipCode(rs.getString("ZIPCODE"));
			String instructions = rs.getString("INSTRUCTIONS");
			String zoneCode = rs.getString("ZONE_CODE");
            boolean deliveryChargeWaived = "X".equals(rs.getString("DELIVERY_CHARGE_WAIVED"));
			
            DlvLocationPersistentBean bean = new DlvLocationPersistentBean( new PrimaryKey(id), startDate, endDate, facility, address, instructions, zoneCode, deliveryChargeWaived);
            bean.setParentPK(parentPK);
            lst.add(bean);
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        return lst;
    }

}
