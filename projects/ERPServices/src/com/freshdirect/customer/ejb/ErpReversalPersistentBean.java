/*
 * ErpManualAuthorizationPersistentBean.java
 *
 * Created on January 15, 2002, 12:36 PM
 */

package com.freshdirect.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpPaymentMethodTransactionModel;
import com.freshdirect.customer.ErpReversalModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

public class ErpReversalPersistentBean extends ErpPaymentMethodTransactionPersistentBean {
	
	private ErpReversalModel model;

    public ErpReversalPersistentBean() {
		super();
		this.model = new ErpReversalModel();
    }
    
    public ErpReversalPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	public ErpReversalPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	public ErpReversalPersistentBean(ErpReversalModel model) {
		this();
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
	
	public void setFromModel(ModelI model) {
		this.model = (ErpReversalModel)model;
	}
	
	/**
	 * Find ErpReversalPersistenttBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpInvoicePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.REVERSAL.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpAuthorizationPersistentBean bean = new ErpAuthorizationPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		return super.create(conn, this.model);
	}
	
	public void load(Connection conn) throws SQLException {
		super.load(conn, this.model);
	}
	

	protected String getTransactionType() {
		return EnumTransactionType.REVERSAL.getCode();
	}
	
	protected ErpPaymentMethodTransactionModel createModel() {
		return new ErpReversalModel();
	}

}
