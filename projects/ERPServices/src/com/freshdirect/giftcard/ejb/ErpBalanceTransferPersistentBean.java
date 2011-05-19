/*
 * ErpBalanceTransferPersistentBean.java
 *
 * Created on January 9, 2002, 9:03 PM
 */

package com.freshdirect.giftcard.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ejb.ErpGiftCardTranPersistentBean;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.ErpGiftCardBalanceTransferModel;
import com.freshdirect.giftcard.ErpGiftCardTransactionModel;
import com.freshdirect.payment.EnumGiftCardTransactionType;

public class ErpBalanceTransferPersistentBean extends ErpGiftCardTranPersistentBean {
	
	private ErpGiftCardBalanceTransferModel model;
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpBalanceTransferPersistentBean() {
		super();
		this.model = new ErpGiftCardBalanceTransferModel();
    }
    
    public ErpBalanceTransferPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpBalanceTransferPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpBalanceTransferPersistentBean(ErpBalanceTransferPersistentBean model) {
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
		this.model = (ErpGiftCardBalanceTransferModel)model;
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
		PreparedStatement ps = conn.prepareStatement("SELECT sa.ID FROM CUST.SALESACTION sa, CUST.GIFT_CARD_TRANS gct WHERE sa.SALE_ID = ? AND sa.ID= gct.SALESACTION_ID AND sa.ACTION_TYPE = ? " +
														"AND gct.TRAN_STATUS <> 'F'");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.BALANCETRANSFER_GIFTCARD.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpBalanceTransferPersistentBean bean = new ErpBalanceTransferPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		ErpGiftCardTransactionModel gcModel=this.model.getBalanceTransfer();
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_TRANS (ID, SALESACTION_ID, TRAN_TYPE, TRAN_STATUS, AUTH_CODE, PRE_AUTH_CODE, ERROR_MSG, TRANS_AMOUNT, CERTIFICATE_NUM, POST_DATE_TIME) values (?,?,?,?,?,?,?,?,?,SYSDATE)");
		String id = this.getNextId(conn, "CUST");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, salesactionId);
		ps.setString(index++, gcModel.getGiftCardTransactionType().getName());		
		ps.setString(index++, gcModel.getGcTransactionStatus().getName());
		ps.setString(index++, gcModel.getAuthCode());
		ps.setNull(index++, Types.VARCHAR);		
		ps.setString(index++, gcModel.getErrorMsg());
		ps.setBigDecimal(index++, new BigDecimal(String.valueOf(this.model.getAmount())));
		ps.setString(index++, gcModel.getCertificateNumber());
		//ps.setTimestamp(index++, new java.sql.Timestamp(model.getActionTime().getTime()));					
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
		PreparedStatement ps = conn.prepareStatement("SELECT TRAN_TYPE, TRAN_STATUS, AUTH_CODE, PRE_AUTH_CODE, TRANS_AMOUNT, ERROR_MSG, CERTIFICATE_NUM FROM CUST.GIFT_CARD_TRANS WHERE SALESACTION_ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			ErpGiftCardTransactionModel gcModel=new ErpGiftCardTransactionModel();
			gcModel.setGiftCardTransactionType(EnumGiftCardTransactionType.getEnum(rs.getString("TRAN_TYPE")));		
			gcModel.setGiftCardTransactionType(EnumGiftCardTransactionType.getEnum(rs.getString("TRAN_STATUS")));
			gcModel.setAuthCode(rs.getString("AUTH_CODE"));
			gcModel.setPreAuthCode(rs.getString("PRE_AUTH_CODE"));
			gcModel.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
			gcModel.setErrorMsg(rs.getString("ERROR_MSG"));
			this.model.setAmount(rs.getDouble("TRANS_AMOUNT"));
			this.model.setBalanceTransfer(gcModel);
		} else {
			throw new SQLException("No such ErpBalanceTransfer PK: " + this.getPK());
		}
		
		rs.close();
		ps.close();
		
		this.unsetModified();
	}

    public void store(Connection conn) throws SQLException {
    	/*
    	super.load(conn, this.model);
		ErpGiftCardTransactionModel gcModel=this.model.getBalanceTransfer();
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.GIFT_CARD_TRANS SET TRAN_STATUS = ?, AUTH_CODE = ?, ERROR_MSG = ?, POST_DATE_TIME = SYSDATE WHERE SALESACTION_ID = ? ");
		int index = 1;
		ps.setString(index++, gcModel.getGcTransactionStatus().getName());
		ps.setString(index++, gcModel.getAuthCode());
		ps.setString(index++, gcModel.getErrorMsg());
		//ps.setTimestamp(index++, new java.sql.Timestamp(model.getActionTime().getTime()));		
		ps.setString(index, this.getPK().getId());
		
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		this.unsetModified();
		*/
    	//Do nothing as no need to update BT transaction. Only Insert.
    }

}
