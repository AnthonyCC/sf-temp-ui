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
import java.util.List;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpSubmitFailedModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;


/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpSubmitFailedPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpSubmitFailedModel model;
	
	/** default constructor */
	public ErpSubmitFailedPersistentBean(){
		super();
		this.model = new ErpSubmitFailedModel();
	}
	
	public ErpSubmitFailedPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}
	
	/** Load constructor. */
	public ErpSubmitFailedPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	/**
	 * Copy constructor, from model.
	 * @param bean ErpSubmitFailedModel to copy from
	 */
	public ErpSubmitFailedPersistentBean(ErpSubmitFailedModel model) {
		this();
		this.setFromModel(model);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	public ModelI getModel(){
		return this.model.deepCopy();
	}
	
	/** sets the properties of an object by copying the properties of a model
	 * @param model the model to copy
	 * @throws RemoteException any problems during setting the properties of a remotely
	 * reachable model consumer
	 */
	public void setFromModel(ModelI model){
		this.model = (ErpSubmitFailedModel)model;
	}

	/**
	 * Find ErpCreateOrderPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCreateOrderPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.SUBMIT_FAILED.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpSubmitFailedPersistentBean bean = new ErpSubmitFailedPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
	
	/** writes a new object to the persistent store for the first time
	 * @param conn a SQLConnection to use to write this object to the
	 * persistent store
	 * @throws SQLException any problems encountered while writing this object
	 * to the persistent store
	 * @return the unique identity of this new object in the
	 * persistent store
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, CUSTOMER_ID) values (?,?,?,?,?,?,?)");
		PreparedStatement ps1 = conn.prepareStatement("INSERT INTO CUST.ACTIONNOTE (SALESACTION_ID, MESSAGE) values (?,?)");
		
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, EnumTransactionType.SUBMIT_FAILED.getCode());
		ps.setDouble(5, 0);			// amount always zero, what else?
		ps.setString(6, this.model.getTransactionSource().getCode());
		ps.setString(7, this.model.getCustomerId());
		
		ps1.setString(1, id);
		ps1.setString(2, model.getMessage().substring( 0, Math.min(4000, model.getMessage().length())));
		
		try {
			if (ps.executeUpdate() != 1 || ps1.executeUpdate() != 1) {
				throw new SQLException("No rows created");
			}
			this.setPK(new PrimaryKey(id));
		} finally {
			if (ps!=null) {
				ps.close();
				ps = null;
			}
			if (ps1!=null) {
				ps1.close();
				ps1 = null;
			}
		}
		this.unsetModified();
		return this.getPK();
	}
	
	/** reads an object from the persistent store
	 * @param conn a SQLConnection to use when reading this object
	 * from the persistent store
	 * @throws SQLException any problems entountered while reading this object
	 * from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, SOURCE, MESSAGE FROM CUST.SALESACTION, CUST.ACTIONNOTE WHERE SALESACTION.ID = ACTIONNOTE.SALESACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			this.model.setMessage(rs.getString("MESSAGE"));

		} else {
			throw new SQLException("No such transaction PK: " + this.getPK());
		}
		
		rs.close();
		ps.close();
		
		this.unsetModified();
	}
}
