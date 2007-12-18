/*
 * Created on Apr 6, 2005
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.customer.ErpAbstractSettlementModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * @author jng
 *
 */
public class ErpAbstractSettlementPersistentBean extends ErpPaymentPersistentBean {

	protected ErpAbstractSettlementModel model;
	
    public ErpAbstractSettlementPersistentBean() {
		super();
    }
    
    public ErpAbstractSettlementPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpAbstractSettlementPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	public ErpAbstractSettlementPersistentBean(ErpAbstractSettlementModel model) {
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
	 * Copy into model.
	 * @return ErpPaymentModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** 
	 * Copy from model.
	 * 
	 * @param ModelI that is a instance of ErpSettlementModel  
	 */
	public void setFromModel(ModelI model) {
		this.model = (ErpAbstractSettlementModel)model;
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
		return findByParent(conn, parentPK, null);
	}
	
	public static List findByParent(Connection conn, PrimaryKey parentPK, EnumTransactionType transactionType) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		if (transactionType != null) {
			ps.setString(2, transactionType.getCode());			
		} else {
			ps.setString(2, EnumTransactionType.SETTLEMENT.getCode());
		}
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpSettlementPersistentBean bean = new ErpSettlementPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
	 * writes a new object to the persistent store for the first time
	 * 
	 * @param conn a SQLConnection to use to write this object to the persistent store
	 * @throws SQLException any problems encountered while writing this object to the persistent store
	 * @return the unique identity of this new object in the persistent store
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String salesactionId = super.create(conn, this.model).getId();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENT (SALESACTION_ID, SEQUENCE_NUMBER, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, RESPONSE_CODE, AFFILIATE, AUTH_CODE) values (?,?,?,?,?,?,?,?,?,?)");
		
		int index =1;
		ps.setString(index++, salesactionId);
		ps.setString(index++, this.model.getSequenceNumber());
		ps.setString(index++, (this.model.getCardType() != null) ? this.model.getCardType().getFdName() : null);		
		ps.setString(index++, this.model.getCcNumLast4());
		ps.setString(index++, (this.model.getPaymentMethodType() != null) ? this.model.getPaymentMethodType().getName() : null);
		ps.setString(index++, this.model.getAbaRouteNumber());
		ps.setString(index++, (this.model.getBankAccountType() != null) ? this.model.getBankAccountType().getName() : null);
		ps.setString(index++, (this.model.getResponseCode() != null) ? this.model.getResponseCode().getName() : null);
		if(this.model.getAffiliate() != null) {
			ps.setString(index++, this.model.getAffiliate().getCode());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		ps.setString(index++, this.model.getAuthCode());
		
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
	
	/** 
	 * Reads an object from the persistent store
	 * 
	 * @param conn a SQLConnection to use when reading this object from the persistent store
	 * @throws SQLException any problems entountered while reading this object from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		super.load(conn, model);
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, SEQUENCE_NUMBER, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, RESPONSE_CODE, AFFILIATE, AUTH_CODE FROM CUST.SALESACTION, CUST.PAYMENT WHERE SALESACTION.ID = PAYMENT.SALESACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			this.model.setAmount(rs.getDouble("AMOUNT"));
			this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			this.model.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
			this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
			this.model.setCcNumLast4(rs.getString("CCNUM_LAST4"));
			this.model.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
			this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
			this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
			this.model.setResponseCode(EnumPaymentResponse.getEnum(rs.getString("RESPONSE_CODE")));
			ErpAffiliate aff = (ErpAffiliate) NVL.apply(ErpAffiliate.getEnum(rs.getString("AFFILIATE")), ErpAffiliate.getPrimaryAffiliate());
			this.model.setAffiliate(aff);
			this.model.setAuthCode(rs.getString("AUTH_CODE"));
			
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
		this.unsetModified();
	}
	

	protected String getTransactionType() {
		return null;
	}
	
	protected ErpPaymentModel createModel() {return null;};

}
