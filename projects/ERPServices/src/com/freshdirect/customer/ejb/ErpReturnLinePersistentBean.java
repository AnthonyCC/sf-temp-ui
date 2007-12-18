/*
 * 
 * ErpReturnLinePersistentBean.java
 * Date: Oct 15, 2002 Time: 6:47:56 PM
 */
package com.freshdirect.customer.ejb;

/**
 * 
 * @author knadeem
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

public class ErpReturnLinePersistentBean extends ErpReadOnlyPersistentBean {
	
	private ErpReturnLineModel model;
	
	/** Default constructor. */
	public ErpReturnLinePersistentBean() {
		super();
		this.model = new ErpReturnLineModel();
	}

	/** Load constructor. */
	public ErpReturnLinePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpReturnLineModel to copy from
	 */
	public ErpReturnLinePersistentBean(ErpReturnLineModel model) {
		super();
		this.setFromModel(model);
	}

	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
	
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpReturnLineModel)model;
	}
	
	
	

	/**
	 * Persists this object in database for the first time
	 * 
	 * @param conn database connection to operate on
	 * 
	 * @throws SQLException if there are poblems talking to database
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.RETURNLINE (ID, SALESACTION_ID, LINE_NUMBER, QUANTITY, RESTOCKING_FEE) VALUES (?,?,?,?,?)");
		
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getLineNumber());
		ps.setDouble(4, this.model.getQuantity());
		ps.setString(5, (this.model.isRestockingOnly() ? "X" : ""));
		
		try{
			if(ps.executeUpdate() != 1){
				throw new SQLException("No Row Created");
			}
			this.setPK(new PrimaryKey(id));
		}finally{
			if(ps != null){
				ps.close();
				ps = null;
			}
		}
		
		this.unsetModified();
		return this.getPK();
	}

	/**
	 * load this object from the persitent store
	 * 
	 * @param conn database connection to operate on
	 * 
	 * @throws SQLException if problems are encountered while talking to database
	 */
	public void load(Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("SELECT LINE_NUMBER, QUANTITY, RESTOCKING_FEE FROM CUST.RETURNLINE WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			this.model.setLineNumber(rs.getString("LINE_NUMBER"));
			this.model.setQuantity(rs.getDouble("QUANTITY"));
			this.model.setRestockingOnly("X".equalsIgnoreCase(rs.getString("RESTOCKING_FEE")) ? true : false);
		}else{
			throw new SQLException("No Return Line found for pk: "+this.getParentPK());
		}
		
		if(rs != null){
			rs.close();
			rs = null;
		}
		if(ps != null){
			ps.close();
			ps = null;
		}
		
		this.unsetModified();
	}
	
	/**
	 * Find ErpReturnLinePersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * 
	 * @return a List of ErpReturnLinePersistentBean objects (empty if found none).
	 * 
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.RETURNLINE WHERE SALESACTION_ID = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpReturnLinePersistentBean bean = new ErpReturnLinePersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		
		if(rs != null){
			rs.close();
			rs = null;
		}
		if(ps != null){
			ps.close();
			ps = null;
		}
		
		return lst;
	}

}
