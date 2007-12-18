/*
 * ErpAdjustmentPersistentBean.java
 *
 * Created on May 06, 2002, 2:00 PM
 */
package com.freshdirect.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.*;
import java.util.List; 

import com.freshdirect.customer.*;
import com.freshdirect.framework.core.*;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.common.customer.EnumCardType;

public class ErpAdjustmentPersistentBean extends ErpPaymentPersistentBean {
	
	private ErpAdjustmentModel model;
    
    /** 
	 * Creates new ErpAdjustmentPersistentBean
	 */
    public ErpAdjustmentPersistentBean() {
		super();
		this.model = new ErpAdjustmentModel();
    }
    
    public ErpAdjustmentPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpAdjustmentPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpAdjustmentModel to copy from
	 */
	public ErpAdjustmentPersistentBean(ErpAdjustmentModel model) {
		this();
		this.setFromModel(model);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
	
	/** 
	 * gets a model representing the state of an object
	 * 
	 * @throws RemoteException any problems encountered while getting the model of remotely reachable object
	 * @return the model representing the state of an object
	 */
	public ModelI getModel(){
		return this.model.deepCopy();
	}
	
	/** 
	 * sets the properties of an object by copying the properties of a model
	 * 
	 * @param model the model to copy
	 * @throws RemoteException any problems during setting the properties of a remotely reachable model consumer
	 */
	public void setFromModel(ModelI model){
		this.model = (ErpAdjustmentModel)model;
	}
	
	/**
	 * Find ErpSettlementPersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpSettlementPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.ADJUSTMENT.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpChargebackPersistentBean bean = new ErpChargebackPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	/** 
	 * Reads an object from the persistent store
	 * 
	 * @param conn a SQLConnection to use when reading this object from the persistent store
	 * @throws SQLException any problems entountered while reading this object from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		super.load(conn, this.model);
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, BATCH_DATE, BATCH_NUMBER, DEPOSIT_DATE, PROCESS_DATE, TRAN_DATE, DESCRIPTION, SEQUENCE_NUMBER, INITIATOR, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE FROM CUST.SALESACTION, CUST.PAYMENT WHERE SALESACTION.ID = PAYMENT.SALESACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			this.model.setAmount(rs.getDouble("AMOUNT"));
			this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			this.model.setBatchDate(rs.getDate("BATCH_DATE"));
			this.model.setBatchNumber(rs.getString("BATCH_NUMBER"));
			this.model.setDepositDate(rs.getDate("DEPOSIT_DATE"));
			this.model.setProcessDate(rs.getDate("PROCESS_DATE"));
			this.model.setTransactionDate(rs.getDate("TRAN_DATE"));
			this.model.setAdjustmentDescription(rs.getString("DESCRIPTION"));
			this.model.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
			this.model.setTransactionInitiator(rs.getString("INITIATOR"));
			this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
			this.model.setCcNumLast4(rs.getString("CCNUM_LAST4"));
			this.model.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
			this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
			this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
		this.unsetModified();
	}
	
	/** 
	 * writes a new object to the persistent store for the first time
	 * 
	 * @param conn a SQLConnection to use to write this object to the persistent store
	 * @throws SQLException any problems encountered while writing this object to the persistent store
	 * @return the unique identity of this new object in the persistent store
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String salesactionId = super.create(conn, this.model).getId();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENT (SALESACTION_ID, BATCH_DATE, BATCH_NUMBER, DEPOSIT_DATE, PROCESS_DATE, TRAN_DATE, DESCRIPTION, SEQUENCE_NUMBER, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

		int index = 1;
		ps.setString(index++, salesactionId);
		ps.setDate(index++, new java.sql.Date(this.model.getBatchDate().getTime()));
		ps.setString(index++, this.model.getBatchNumber());
		ps.setDate(index++, new java.sql.Date(this.model.getDepositDate().getTime()));
		ps.setDate(index++, new java.sql.Date(this.model.getProcessDate().getTime()));
		ps.setDate(index++, new java.sql.Date(this.model.getTransactionDate().getTime()));
		ps.setString(index++, this.model.getAdjustmentDescription());
		ps.setString(index++, this.model.getSequenceNumber());
		ps.setString(index++, (this.model.getCardType() != null) ? this.model.getCardType().getFdName() : null);		
		ps.setString(index++, this.model.getCcNumLast4());
		ps.setString(index++, (this.model.getPaymentMethodType() != null) ? this.model.getPaymentMethodType().getName() : null);
		ps.setString(index++, this.model.getAbaRouteNumber());
		ps.setString(index++, (this.model.getBankAccountType() != null) ? this.model.getBankAccountType().getName() : null);
		
		try{
			if(ps.executeUpdate() != 1 ){
				throw new SQLException("new row cannot be created");
			}
		}finally{
			if(ps != null){
				ps.close();
				ps = null;
			}
		}
		
		this.unsetModified();
		return this.getPK();
	}

	
	protected String getTransactionType() {
		return EnumTransactionType.ADJUSTMENT.getCode();
	}

	protected ErpPaymentModel createModel() {
		return new ErpAdjustmentModel();
	}
}
