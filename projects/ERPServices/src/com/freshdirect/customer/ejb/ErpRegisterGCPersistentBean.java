package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.giftcard.ErpGiftCardTransactionModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;

public class ErpRegisterGCPersistentBean extends ErpGiftCardPersistentBean{

	
	
	private ErpRegisterGiftCardModel model;
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpRegisterGCPersistentBean() {
		super();
		this.model = new ErpRegisterGiftCardModel();
    }
    
    public ErpRegisterGCPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpRegisterGCPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpRegisterGCPersistentBean(ErpRegisterGiftCardModel model) {
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
		this.model = (ErpRegisterGiftCardModel)model;
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
		ps.setString(2, EnumTransactionType.REGISTER_GIFTCARD.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpRegisterGCPersistentBean bean = new ErpRegisterGCPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		
		if(this.model.getRegisterTranactionList()!=null){
		
			
			
			PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_TRANS (ID, SALESACTION_ID, TRAN_TYPE, TRAN_STATUS, AUTH_CODE, ERROR_MSG, TRANS_AMOUNT, CERTIFICATE_NUM, POST_DATE_TIME, REFERENCE_ID) values (?,?,?,?,?,?,?,?,sysdate,?)");
			List regList=this.model.getRegisterTranactionList();
			for(int i=0;i<regList.size();i++){
			
				ErpGiftCardTransactionModel regModel=(ErpGiftCardTransactionModel)regList.get(i);
				int index = 1;
				String id = this.getNextId(conn, "CUST");
				
				ps.setString(index++, id);
				ps.setString(index++, salesactionId);
				ps.setString(index++, (this.model.getTransactionType() != null) ? this.model.getTransactionType().getCode() : null);		
				ps.setString(index++, (regModel.getGcTransactionStatus() != null) ? regModel.getGcTransactionStatus().getName() : null);
				if(regModel.getAuthCode()!=null)
				   ps.setString(index++, regModel.getAuthCode());
				else
				   ps.setNull(index++, Types.NULL);
				if(regModel.getErrorMsg()!=null)
				   ps.setString(index++, regModel.getErrorMsg());
				else
					ps.setNull(index++, Types.NULL);		
				ps.setDouble(index++, regModel.getTransactionAmount());
				if(regModel.getGivexNum()!=null)
				  ps.setString(index++, regModel.getCertificateNumber());		
				else
				  ps.setNull(index++, Types.NULL);		
				ps.setString(index++, regModel.getReferenceId());
				ps.addBatch();
				
			}	
			try{
					int row[]=ps.executeBatch();
			}catch(SQLException e)		
			{		
				}finally{
					if(ps != null){
						ps.close();
						ps = null;
					}
				}
			}				
			this.unsetModified();
			return this.getPK();
	}
	
	private static final String SELECT_GC_TRANS_SQL="SELECT TRAN_TYPE, TRAN_STATUS,AUTH_CODE, ERROR_MSG, TRANS_AMOUNT, "+
												"CERTIFICATE_NUM, POST_DATE_TIME FROM CUST.GIFT_CARD_TRANS WHERE SALESACTION_ID = ?";
	
	/** 
	 * Reads an object from the persistent store
	 * 
	 * @param conn a SQLConnection to use when reading this object from the persistent store
	 * @throws SQLException any problems encountered while reading this object from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		this.model.setId(this.getPK().getId());
		super.load(conn, this.model);
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_TRANS_SQL);
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		List regTranList=new ArrayList();
		while (rs.next()) {
			ErpGiftCardTransactionModel regModel=new ErpGiftCardTransactionModel();
			//this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));			
			//this.model.setAmount(rs.getDouble("AMOUNT"));				
			//this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			
			regModel.setGiftCardTransactionType(EnumGiftCardTransactionType.getEnum(rs.getString("TRAN_TYPE")));
			regModel.setGcTransactionStatus(EnumGiftCardTransactionStatus.getEnum(rs.getString("TRAN_STATUS")));			
			regModel.setAuthCode(rs.getString("AUTH_CODE"));
			regModel.setTransactionAmount(rs.getDouble("TRANS_AMOUNT"));			
			regModel.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
			regModel.setErrorMsg(rs.getString("ERROR_MSG"));
			regTranList.add(regModel);
		} 
		this.model.setRegisterTranactionList(regTranList);
		rs.close();
		ps.close();
		
		this.unsetModified();
	}

	
}
