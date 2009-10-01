/*
 * ErpAuthorizationPersistentBean.java
 *
 * Created on January 9, 2002, 9:03 PM
 */

package com.freshdirect.giftcard.ejb;

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
import java.util.Date;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.customer.ejb.ErpGiftCardTranPersistentBean;
import com.freshdirect.customer.ejb.ErpPaymentPersistentBean;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpReverseAuthPersistentBean extends ErpGiftCardTranPersistentBean {
	
	private ErpReverseAuthGiftCardModel model;
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpReverseAuthPersistentBean() {
		super();
		this.model = new ErpReverseAuthGiftCardModel();
    }
    
    public ErpReverseAuthPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpReverseAuthPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpReverseAuthPersistentBean(ErpReverseAuthGiftCardModel model) {
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
		this.model = (ErpReverseAuthGiftCardModel)model;
		this.setModified();
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
		PreparedStatement ps = conn.prepareStatement("SELECT sa.ID FROM CUST.SALESACTION sa, CUST.GIFT_CARD_TRANS gct WHERE sa.SALE_ID = ? AND sa.ID= gct.SALESACTION_ID AND sa.ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.REVERSEAUTH_GIFTCARD.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpReverseAuthPersistentBean bean = new ErpReverseAuthPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_TRANS (ID, SALESACTION_ID, TRAN_TYPE, TRAN_STATUS, AUTH_CODE, PRE_AUTH_CODE, ERROR_MSG, TRANS_AMOUNT, CERTIFICATE_NUM, CREATE_DATE_TIME, REFERENCE_ID, POST_DATE_TIME) values (?,?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE)");
		String id = this.getNextId(conn, "CUST");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, salesactionId);
		ps.setString(index++, this.model.getGCTransactionType().getName());		
		ps.setString(index++, this.model.getGcTransactionStatus().getName());
		ps.setString(index++, this.model.getAuthCode());
		if(this.model.getPreAuthCode() != null) {
			ps.setString(index++, this.model.getPreAuthCode());
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}		
		ps.setString(index++, this.model.getErrorMsg());
		ps.setDouble(index++, this.model.getAmount());
		ps.setString(index++, this.model.getCertificateNum());
		//ps.setTimestamp(index++, new java.sql.Timestamp(model.getActionTime().getTime()));		
		ps.setString(index++, this.model.getReferenceId());		
		//ps.setTimestamp(index++, new java.sql.Timestamp(model.getPostedTime().getTime()));
		
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
		PreparedStatement ps = conn.prepareStatement("SELECT TRAN_TYPE, TRAN_STATUS, AUTH_CODE, PRE_AUTH_CODE, TRANS_AMOUNT, ERROR_MSG, CERTIFICATE_NUM, REFERENCE_ID, CREATE_DATE_TIME, POST_DATE_TIME FROM CUST.GIFT_CARD_TRANS WHERE SALESACTION_ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setGCTransactionType(EnumGiftCardTransactionType.getEnum(rs.getString("TRAN_TYPE")));
			this.model.setGcTransactionStatus(EnumGiftCardTransactionStatus.getEnum(rs.getString("TRAN_STATUS")));
			this.model.setAuthCode(rs.getString("AUTH_CODE"));
			this.model.setPreAuthCode(rs.getString("PRE_AUTH_CODE"));
			this.model.setCertificateNum(rs.getString("CERTIFICATE_NUM"));
			this.model.setErrorMsg(rs.getString("ERROR_MSG"));
			this.model.setAmount(rs.getDouble("TRANS_AMOUNT"));
			this.model.setReferenceId(rs.getString("REFERENCE_ID"));
			this.model.setActionTime(rs.getTimestamp("CREATE_DATE_TIME"));
			this.model.setPostedTime(rs.getTimestamp("POST_DATE_TIME"));
			
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		
		rs.close();
		ps.close();
		
		this.unsetModified();
	}
	
    public void store(Connection conn) throws SQLException {
		super.load(conn, this.model);
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.GIFT_CARD_TRANS SET TRAN_STATUS = ?, AUTH_CODE = ?, ERROR_MSG = ?, POST_DATE_TIME = ? WHERE SALESACTION_ID = ? ");
		int index = 1;
		ps.setString(index++, this.model.getGcTransactionStatus().getName());
		ps.setString(index++, this.model.getAuthCode());
		ps.setString(index++, this.model.getErrorMsg());
		ps.setTimestamp(index++, new java.sql.Timestamp(model.getPostedTime().getTime()));		
		ps.setString(index, this.getPK().getId());
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		this.unsetModified();
    }

}
