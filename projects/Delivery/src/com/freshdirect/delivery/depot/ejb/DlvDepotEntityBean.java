/*
 * 
 * DlvDepotEntityBean.java
 * Date: Jul 23, 2002 Time: 6:49:26 PM
 */
package com.freshdirect.delivery.depot.ejb;

/**
 * 
 * @author knadeem
 */
import java.sql.*;
import java.util.*;
import javax.ejb.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.framework.collection.*;

import com.freshdirect.delivery.depot.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class DlvDepotEntityBean extends EntityBeanSupport{
	
	private static Category LOGGER = LoggerFactory.getInstance( DlvDepotEntityBean.class );
	
	private String name;
	private String registrationCode;
	private String regionId;
	private String depotCode;
    private String custServiceEmail;
    private boolean requireEmployeeId;
    private boolean pickup;
    private boolean corporateDepot;
    private boolean deactivated;
	private LocationList locations;
	
	/**
	 * Default constructor.
	 */
	public DlvDepotEntityBean() {
		super();
	}
	
	public void initialize(){
		this.name = "";
		this.registrationCode = "";
		this.regionId = "";
		this.depotCode = "";
        this.custServiceEmail = "";
        this.requireEmployeeId = false;
        this.pickup = false;
        this.corporateDepot = false;
        this.deactivated = false;
		this.locations = new LocationList();
	}
	
	public ModelI getModel() {
		DlvDepotModel model = new DlvDepotModel();
		model.setName(this.name);
		model.setRegistrationCode(this.registrationCode);
		model.setRegionId(this.regionId);
		model.setDepotCode(this.depotCode);
        model.setCustomerServiceEmail(this.custServiceEmail);
        model.setRequireEmployeeId(this.requireEmployeeId);
        model.setPickup(this.pickup);
        model.setCorporateDepot(this.corporateDepot);
		model.setLocations(this.locations.getModelList());
		model.setDeactivated(this.deactivated);
		super.decorateModel(model);
		return model;
	}
	
	public ModelI getModel(java.util.Date effectiveDate){
		DlvDepotModel model = new DlvDepotModel();
		model.setName(this.name);
		model.setRegistrationCode(this.registrationCode);
		model.setRegionId(this.regionId);
		model.setDepotCode(this.depotCode);
        model.setCustomerServiceEmail(this.custServiceEmail);
        model.setRequireEmployeeId(this.requireEmployeeId);
        model.setPickup(this.pickup);
        model.setCorporateDepot(this.corporateDepot);
		model.setLocations(this.locations.getModelList());
		model.setDeactivated(this.deactivated);
		super.decorateModel(model);
		return model;
	}
	
	/*private DlvDepotDataPersistentBean getCurrentRegionData(){
		return getCurrentRegionData(new GregorianCalendar().getTime());
	}
	
	private DlvDepotDataPersistentBean getCurrentRegionData(java.util.Date lookupDate){
		DlvDepotDataPersistentBean foundBean = null;
		QuickDateFormat qf = new QuickDateFormat(QuickDateFormat.FORMAT_SHORT_DATE);
		String param = qf.format(lookupDate);
		for(Iterator i = this.depotDataList.iterator(); i.hasNext();){
			DlvDepotDataPersistentBean bean = (DlvDepotDataPersistentBean)i.next();
			if(qf.format(bean.getStartDate()).compareTo(param) <= 0) {
				foundBean =bean;
			}
		}		
		return foundBean;
	}*/
	
	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		// copy properties from model
		DlvDepotModel m = (DlvDepotModel)model;
		updateFromModel(m);
		this.setLocationsFromModel( m.getLocations());
		
		this.setModified();
	}
	
	public void updateFromModel(DlvDepotModel model) {
		
		this.name = model.getName();
		this.registrationCode = model.getRegistrationCode();
		this.regionId = model.getRegionId();
		this.depotCode = model.getDepotCode();
		this.custServiceEmail = model.getCustomerServiceEmail();
        this.requireEmployeeId = model.getRequireEmployeeId();
        this.pickup = model.isPickup();
        this.corporateDepot = model.isCorporateDepot();
        this.deactivated = model.isDeactivated();
		this.setModified();
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		this.setPK(new PrimaryKey(this.getNextId(conn, "DLV")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO DLV.DEPOT (ID, NAME, REGISTRATION_CODE, REGION_ID, DEPOT_CODE, CUST_SERV_EMAIL, REQUIRE_EMP_ID, PICKUP, CORPORATE_DEPOT, DEACTIVATED) values (?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, this.getPK().getId());
		ps.setString(2, this.name);
		ps.setString(3, this.registrationCode);
		ps.setString(4, this.regionId);
		ps.setString(5, this.depotCode);
        ps.setString(6, this.custServiceEmail);
        ps.setString(7, this.requireEmployeeId?"X":"");
        ps.setString(8, this.pickup? "X" : "");
        ps.setString(9, this.corporateDepot ? "X" : "");
        ps.setString(10, this.deactivated ? "X" : "");
		try {
			if (ps.executeUpdate()!=1) {
				throw new SQLException("No database rows created!");
			}
		} catch (SQLException sqle) {
			this.setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}
		
		// create children
		this.locations.setParentPK(this.getPK());
		this.locations.create(conn);
		
		return this.getPK();
	}
	
	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE DLV.DEPOT SET NAME = ?, REGISTRATION_CODE = ?, REGION_ID = ?, DEPOT_CODE = ?, CUST_SERV_EMAIL=?, REQUIRE_EMP_ID=? , PICKUP=?, CORPORATE_DEPOT=?, DEACTIVATED=? WHERE ID = ?");
		ps.setString(1, this.name);
		ps.setString(2, this.registrationCode);
		ps.setString(3, this.regionId);
		ps.setString(4, this.depotCode);
        ps.setString(5, this.custServiceEmail);
        ps.setString(6, this.requireEmployeeId ? "X" : "");
        ps.setString(7, this.pickup ? "X" : "");
        ps.setString(8, this.corporateDepot ? "X" : "");
        ps.setString(9, this.deactivated ? "X" : "");
		ps.setString(10, this.getPK().getId());
		try {
			if (ps.executeUpdate()!=1) {
				throw new SQLException("No database rows created!");
			}
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		// store children
		if(this.locations.isModified()){
			this.locations.store(conn);
		}
		this.unsetModified();
	}
	
	public void load(Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("SELECT NAME, REGISTRATION_CODE, REGION_ID, DEPOT_CODE, CUST_SERV_EMAIL, REQUIRE_EMP_ID, PICKUP, CORPORATE_DEPOT, DEACTIVATED FROM DLV.DEPOT WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("No such DlvDepot PK: " + this.getPK());
		}
		
		this.name = rs.getString("NAME");
		this.registrationCode = rs.getString("REGISTRATION_CODE");
		this.regionId = rs.getString("REGION_ID");
		this.depotCode = rs.getString("DEPOT_CODE");
        this.custServiceEmail = rs.getString("CUST_SERV_EMAIL");
        this.requireEmployeeId = "X".equals(rs.getString("REQUIRE_EMP_ID"));
        this.pickup = "X".equals(rs.getString("PICKUP"));
        this.corporateDepot = "X".equals(rs.getString("CORPORATE_DEPOT"));
        this.deactivated = "X".equals(rs.getString("DEACTIVATED"));
		
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
		// load children
		this.locations.setParentPK(this.getPK());
		this.locations.load(conn);
		
		this.unsetModified();
	}
	
	public void remove(Connection conn) throws SQLException {
		// remove children
		this.locations.remove(conn);
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM DLV.DEPOT WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		ps.executeUpdate();
		ps.close();
		ps = null;
	}
	
	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM DLV.DEPOT WHERE ID = ?");
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				return new PrimaryKey(rs.getString(1));
			} else {
				throw new FinderException("Unable to find DlvDepot with PK " + pk);
			}
		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error while trying to cleanup");
			}
		}
	}
	
	public PrimaryKey ejbFindByDepotCode(String name) throws FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM DLV.DEPOT WHERE DEPOT_CODE = ? ");
			ps.setString(1, name);
			rs = ps.executeQuery();
			if(rs.next()){
				return new PrimaryKey(rs.getString(1));
			} else {
				throw new FinderException("Unable to find DlvDepot with name: "+name);
			}
		}catch(SQLException se){
			LOGGER.warn("SQLException in ejbFindByName", se);
			throw new EJBException(se);
		} finally {
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch(SQLException se){
				LOGGER.info("SQLException while cleaning up", se);
			}
		}
	}
					
	
	public Collection ejbFindAll() {
		List pks = new ArrayList();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM DLV.DEPOT");
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				pks.add(new PrimaryKey(rs.getString("ID")));
			}
			return pks;
		}catch(SQLException se){
			throw new EJBException(se);
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(ps != null){
					ps.close();
					ps = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch(SQLException se){
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}
					
				
	public boolean isModified(){
		return super.isModified() || locations.isModified();
	}
	
	public void addLocation(DlvLocationModel location){
		this.locations.add(new DlvLocationPersistentBean(location));	
	}
	
	public void updateLocation(DlvLocationModel location){
		this.locations.update(new DlvLocationPersistentBean(location));	
	}
	
	public void deleteLocation(String locationId){
		this.locations.removeByPK(new PrimaryKey(locationId));
	}
	
	protected void setLocationsFromModel(Collection collection){
		
		java.util.List persistentBeans = new java.util.LinkedList();
		for (Iterator i=collection.iterator(); i.hasNext(); ) {
			DlvLocationModel model = (DlvLocationModel) i.next();
			persistentBeans.add( new DlvLocationPersistentBean(model) );
		}
		this.locations.set(persistentBeans);
	}
	
	private static class LocationList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set( DlvLocationPersistentBean.findByParent(conn, (PrimaryKey)LocationList.this.getParentPK()) );
		}
	}

}
