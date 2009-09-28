package com.freshdirect.customer.ejb;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAuthorizationModel;
import com.freshdirect.giftcard.ErpEmailGiftCardModel;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardTransModel;
import com.freshdirect.giftcard.ErpGiftCardTransactionModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;

public class ErpEmailGiftCardPersistentBean extends ErpGiftCardDlvConfirmPersistentBean{

	
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpEmailGiftCardPersistentBean() {
		super();
		super.setFromModel(new ErpGiftCardDlvConfirmModel(EnumTransactionType.EMAIL_GIFTCARD));
    }
    
    public ErpEmailGiftCardPersistentBean(PrimaryKey pk){
    	this();
    	super.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpEmailGiftCardPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		super.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpEmailGiftCardPersistentBean(ErpEmailGiftCardModel model) {
		this();
		this.setFromModel(model);
	}
	
	
	public PrimaryKey getPK() {
		return super.getPK();
		
	}
	
	public void setPK(PrimaryKey pk) {
		super.setPK(pk);
//		ErpGiftCardDlvConfirmModel model=(ErpGiftCardDlvConfirmModel)super.getModel();
//		model.setPK(pk);
	}
	
	/**
	 * Copy into model.
	 * @return ErpPaymentModel object.
	 */
	public ModelI getModel() {
		return super.getModel().deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		super.setFromModel(model);
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
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? order by action_date desc");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.EMAIL_GIFTCARD.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpEmailGiftCardPersistentBean bean = new ErpEmailGiftCardPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		String salesactionId=super.create(conn,(ErpGiftCardTransModel)this.getModel()).getId();		
		setSalesActionId(salesactionId);
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_RECIPIENT(ID,CUSTOMER_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID,DELIVERY_MODE,AMOUNT,PERSONAL_MSG,SALESACTION_ID,ORDERLINE_NUMBER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
		
		int index = 1;
		ErpGiftCardDlvConfirmModel model = (ErpGiftCardDlvConfirmModel)super.getModel();
		List recList = model.getDlvInfoTranactionList();								
	
		for(int i=0;i<recList.size();i++)
		{
			ErpGCDlvInformationHolder holder=(ErpGCDlvInformationHolder)recList.get(i);										
			ErpRecipentModel recModel =holder.getRecepientModel();					
			String id=SequenceGenerator.getNextId(conn, "CUST");			
			ps.setString(1, id);
			ps.setString(2, recModel.getCustomerId());
			if(recModel.getSenderName()!=null){
			 ps.setString(3, recModel.getSenderName());
			}else{
			  ps.setNull(3, Types.NULL); 	
			}
			if(recModel.getSenderEmail()!=null){
				 ps.setString(4, recModel.getSenderEmail());
			}else{
				  ps.setNull(4, Types.NULL); 	
			}
			if(recModel.getRecipientName()!=null){
				 ps.setString(5, recModel.getRecipientName());
			}else{
				  ps.setNull(5, Types.NULL); 	
			}			
			if(recModel.getRecipientEmail()!=null){
				 ps.setString(6, recModel.getRecipientEmail());
			}else{
				  ps.setNull(6, Types.NULL); 	
			}

			if(recModel.getTemplateId()!=null){
				 ps.setString(7, recModel.getTemplateId());
			}else{
				  ps.setNull(7, Types.NULL); 	
			}

			if(recModel.getDeliveryMode()!=null){
				 ps.setString(8, recModel.getDeliveryMode().getName());
			}else{
				  ps.setNull(8, Types.NULL); 	
			}			
			ps.setDouble(9, recModel.getAmount());			
			ps.setString(10, recModel.getPersonalMessage());
			ps.setString(11, salesactionId);
			ps.setString(12,recModel.getOrderLineId());
			recModel.setId(id);
			ps.addBatch();
		} 
			
		int num[]=ps.executeBatch();
		//super.setFromModel(model);
		super.create(conn, model);
		
		ps.close();		
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
		super.load(conn);
		
		this.unsetModified();								
	}

	
}
