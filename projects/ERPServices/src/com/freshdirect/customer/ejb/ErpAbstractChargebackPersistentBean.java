/*
 * Created on Apr 11, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.common.customer.EnumCardType;

/**
 * @author knadeem
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class ErpAbstractChargebackPersistentBean extends ErpPaymentPersistentBean {
	
	private ErpChargebackModel model;

	public ErpAbstractChargebackPersistentBean() {
		super();
		this.model = new ErpChargebackModel();
	}
	
	public ErpAbstractChargebackPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
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
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** 
	 * sets the properties of an object by copying the properties of a model
	 * 
	 * @param model the model to copy
	 * @throws RemoteException any problems during setting the properties of a remotely reachable model consumer
	 */
	public void setFromModel(ModelI model) {
		this.model = (ErpChargebackModel)model;
	}

	/** 
	 * Reads an object from the persistent store
	 * 
	 * @param conn a SQLConnection to use when reading this object from the persistent store
	 * @throws SQLException any problems entountered while reading this object from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		super.load(conn, this.model);
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, BATCH_DATE, BATCH_NUMBER, CONTROL_NUMBER, CBK_DISCOUNT, REASON_CODE, CBK_REF_NUMBER, CBK_RESPOND_DATE, CBK_WORK_DATE, CBK_ORIGINAL_TX_AMOUNT, CBK_ORIGINAL_TX_DATE, DESCRIPTION, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, AFFILIATE FROM CUST.SALESACTION, CUST.PAYMENT WHERE SALESACTION.ID = PAYMENT.SALESACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			this.model.setAmount(rs.getDouble("AMOUNT"));
			this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			this.model.setBatchDate(rs.getDate("BATCH_DATE"));
			this.model.setBatchNumber(rs.getString("BATCH_NUMBER"));
			this.model.setCbkControlNumber(rs.getString("CONTROL_NUMBER"));
			this.model.setCbkDiscount(rs.getDouble("CBK_DISCOUNT"));
			this.model.setCbkReasonCode(rs.getString("REASON_CODE"));
			this.model.setCbkReferenceNumber(rs.getString("CBK_REF_NUMBER"));
			this.model.setCbkRespondDate(rs.getDate("CBK_RESPOND_DATE"));
			this.model.setCbkWorkDate(rs.getDate("CBK_WORK_DATE"));
			this.model.setOriginalTxAmount(rs.getDouble("CBK_ORIGINAL_TX_AMOUNT"));
			this.model.setOriginalTxDate(rs.getDate("CBK_ORIGINAL_TX_DATE"));
			this.model.setDescription(rs.getString("DESCRIPTION"));
			this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
			this.model.setCcNumLast4(rs.getString("CCNUM_LAST4"));
			this.model.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
			this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
			this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
			ErpAffiliate aff = (ErpAffiliate) NVL.apply(ErpAffiliate.getEnum(rs.getString("AFFILIATE")), ErpAffiliate.getPrimaryAffiliate());
			this.model.setAffiliate(aff);
			
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENT (SALESACTION_ID, BATCH_DATE, BATCH_NUMBER, CONTROL_NUMBER, CBK_DISCOUNT, REASON_CODE, CBK_REF_NUMBER, CBK_RESPOND_DATE, CBK_WORK_DATE, CBK_ORIGINAL_TX_AMOUNT, CBK_ORIGINAL_TX_DATE, DESCRIPTION, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, AFFILIATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		int index = 1; 
		ps.setString(index++, salesactionId);
		ps.setDate(index++, new java.sql.Date(this.model.getBatchDate().getTime()));
		ps.setString(index++, this.model.getBatchNumber());
		ps.setString(index++, this.model.getCbkControlNumber());
		ps.setDouble(index++, this.model.getCbkDiscount());
		ps.setString(index++, this.model.getCbkReasonCode());
		ps.setString(index++, this.model.getCbkReferenceNumber());
		ps.setDate(index++, new java.sql.Date(this.model.getCbkRespondDate().getTime()));
		ps.setDate(index++, new java.sql.Date(this.model.getCbkWorkDate().getTime()));
		ps.setDouble(index++, this.model.getOriginalTxAmount());
		ps.setDate(index++, new java.sql.Date(this.model.getOriginalTxDate().getTime()));
		ps.setString(index++, this.model.getDescription());
		ps.setString(index++, (this.model.getCardType() != null) ? this.model.getCardType().getFdName() : null);		
		ps.setString(index++, this.model.getCcNumLast4());
		ps.setString(index++, (this.model.getPaymentMethodType() != null) ? this.model.getPaymentMethodType().getName() : null);
		ps.setString(index++, this.model.getAbaRouteNumber());
		ps.setString(index++, (this.model.getBankAccountType() != null) ? this.model.getBankAccountType().getName() : null);
		if(this.model.getAffiliate() != null) {
			ps.setString(index++, this.model.getAffiliate().getCode());
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
	
	protected abstract ErpPaymentModel createModel();
	
	protected abstract String getTransactionType(); 

}
