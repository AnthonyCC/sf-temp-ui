/*
 * ErpAuthorizationPersistentBean.java
 *
 * Created on January 9, 2002, 9:03 PM
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
import java.sql.Types;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpAuthorizationPersistentBean extends ErpPaymentPersistentBean {
	
	private ErpAuthorizationModel model;
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpAuthorizationPersistentBean() {
		super();
		this.model = new ErpAuthorizationModel();
    }
    
    public ErpAuthorizationPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpAuthorizationPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpAuthorizationPersistentBean(ErpAuthorizationModel model) {
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

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpAuthorizationModel)model;
	}

	
	/**
	 * Find ErpInvoicePersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpInvoicePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.AUTHORIZATION.getCode());
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
	
	/** 
	 * writes a new object to the persistent store for the first time
	 * 
	 * @param conn a SQLConnection to use to write this object to the persistent store
	 * @throws SQLException any problems encountered while writing this object to the persistent store
	 * @return the unique identity of this new object in the persistent store
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String salesactionId = super.create(conn, this.model).getId();
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENT (SALESACTION_ID, CARD_TYPE, SEQUENCE_NUMBER, AUTH_CODE, DESCRIPTION, AVS, CCNUM_LAST4, RESPONSE_CODE, MERCHANT_ID, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, IS_CHARGE_PAYMENT, AFFILIATE,TRANS_REF_INDEX, PROFILE_ID , GATEWAY_ORDER,EWALLET_TX_ID ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		int index = 1;
		
		ps.setString(index++, salesactionId);
		ps.setString(index++, (this.model.getCardType() != null) ? this.model.getCardType().getFdName() : null);		
		ps.setString(index++, this.model.getSequenceNumber());
		ps.setString(index++, this.model.getAuthCode());
		ps.setString(index++, this.model.getDescription());
		ps.setString(index++, this.model.getAvs());
		ps.setString(index++, this.model.getCcNumLast4());
		if (this.model.getResponseCode() != null) {
			ps.setString(index++, this.model.getResponseCode().getCode());			
		} else {
			ps.setString(index++, this.model.getProcResponseCode());						
		}
		ps.setString(index++, this.model.getMerchantId());
		ps.setString(index++, (this.model.getPaymentMethodType() != null) ? this.model.getPaymentMethodType().getName() : null);
		ps.setString(index++, this.model.getAbaRouteNumber());
		ps.setString(index++, (this.model.getBankAccountType() != null) ? this.model.getBankAccountType().getName() : null);
		ps.setString(index++, (this.model.getIsChargePayment()) ? "X" : null);
		if(this.model.getAffiliate() != null) {
			ps.setString(index++, this.model.getAffiliate().getCode());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}

		if(this.model.getTrasactionRefIndex() != null) {
			ps.setString(index++, this.model.getTrasactionRefIndex());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		if(this.model.getProfileID() != null) {
			ps.setString(index++, this.model.getProfileID());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
				
		/*if(this.model.getGatewayType() != null) {
			ps.setString(index++, String.valueOf(this.model.getGatewayType().getId()));
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}*/
		if(this.model.getGatewayOrderID() != null) {
			ps.setString(index++, this.model.getGatewayOrderID());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		
		if(this.model.getEwalletTxId() != null) {
			ps.setString(index++, this.model.getEwalletTxId());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		
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
		super.load(conn, this.model);
		//TRANS_REF_INDEX, PROFILE_ID , GATEWAY_ID, GATEWAY_ORDER
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, CARD_TYPE, SEQUENCE_NUMBER, AUTH_CODE, DESCRIPTION, AVS, RESPONSE_CODE, CCNUM_LAST4, MERCHANT_ID, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, IS_CHARGE_PAYMENT, AFFILIATE,TRANS_REF_INDEX, PROFILE_ID , GATEWAY_ID, GATEWAY_ORDER, EWALLET_TX_ID FROM CUST.SALESACTION, CUST.PAYMENT WHERE SALESACTION.ID = PAYMENT.SALESACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			this.model.setAmount(rs.getDouble("AMOUNT"));
			
			this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
			this.model.setAuthCode(rs.getString("AUTH_CODE"));
			this.model.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
			this.model.setDescription(rs.getString("DESCRIPTION"));
			this.model.setAvs(rs.getString("AVS"));
			this.model.setResponseCode(EnumPaymentResponse.getEnum(rs.getString("RESPONSE_CODE")));
			this.model.setCcNumLast4(rs.getString("CCNUM_LAST4"));
			this.model.setMerchantId(rs.getString("MERCHANT_ID"));
			this.model.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
			this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
			this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
			this.model.setIsChargePayment("X".equals(rs.getString("IS_CHARGE_PAYMENT")));
			this.model.setEwalletTxId(rs.getString("EWALLET_TX_ID"));
			
			ErpAffiliate aff = (ErpAffiliate) NVL.apply(ErpAffiliate.getEnum(rs.getString("AFFILIATE")), ErpAffiliate.getPrimaryAffiliate());
			this.model.setAffiliate(aff);
			this.model.setTrasactionRefIndex(rs.getString("TRANS_REF_INDEX"));
			this.model.setProfileID(rs.getString("PROFILE_ID"));
			//GatewayType gt = (GatewayType) NVL.apply(GatewayType.get(rs.getString("GATEWAY_ID")), GatewayType.getPrimary());
			//this.model.setGatewayType(gt);
			this.model.setGatewayOrderID(rs.getString("GATEWAY_ORDER"));
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		
		rs.close();
		ps.close();
		
		this.unsetModified();
	}
}
