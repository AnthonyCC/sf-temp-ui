package com.freshdirect.customer.ejb;



import java.sql.Connection;
import java.sql.Date;
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
import com.freshdirect.giftcard.ErpGiftCardTransactionModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.payment.EnumGiftCardTransactionStatus;
import com.freshdirect.payment.EnumGiftCardTransactionType;

public class ErpGiftCardDlvConfirmPersistentBean extends ErpGiftCardPersistentBean{

	
	
	protected ErpGiftCardDlvConfirmModel model;
	
	/** Creates new ErpAuthorizationPersistentBean */
    public ErpGiftCardDlvConfirmPersistentBean() {
		super();
		this.model = new ErpGiftCardDlvConfirmModel(EnumTransactionType.GIFTCARD_DLV_CONFIRM);
    }
    
    public ErpGiftCardDlvConfirmPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** Load constructor. */
	public ErpGiftCardDlvConfirmPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpInvoiceModel to copy from
	 */
	public ErpGiftCardDlvConfirmPersistentBean(ErpGiftCardDlvConfirmModel model) {
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
		this.model = (ErpGiftCardDlvConfirmModel)model;
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
		ps.setString(2, EnumTransactionType.GIFTCARD_DLV_CONFIRM.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpGiftCardDlvConfirmPersistentBean bean = new ErpGiftCardDlvConfirmPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	private String salesactionId=null;
	public void setSalesActionId(String s){
		this.salesactionId=s;
	}
	public String getSalesActionId(){
		return this.salesactionId;
	}
	
	/** 
	 * writes a new object to the persistent store for the first time
	 * 
	 * @param conn a SQLConnection to use to write this object to the persistent store
	 * @throws SQLException any problems encountered while writing this object to the persistent store
	 * @return the unique identity of this new object in the persistent store
	 */
	public PrimaryKey create(Connection conn) throws SQLException {
		String salesactionId=salesactionId = super.create(conn, this.model).getId();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_DELIVERY_INFO(ID, GIFT_CARD_ID,RECIPIENT_ID,DELIVERY_MODE,EMAIL_SENT,SALESACTION_ID) VALUES(?,?,?,?,?,?)");
		
		int index = 1;		
		List recList=this.model.getDlvInfoTranactionList();
	
		for(int i=0;i<recList.size();i++)
		{
			ErpGCDlvInformationHolder holder=(ErpGCDlvInformationHolder)recList.get(i);			
			String id=SequenceGenerator.getNextId(conn, "CUST");			
			ps.setString(1, id);
			ps.setString(2, holder.getGiftCardId());
			ps.setString(3, holder.getRecepientModel().getId());
			ps.setString(4, holder.getRecepientModel().getDeliveryMode().getName());
			if("E".equalsIgnoreCase(holder.getRecepientModel().getDeliveryMode().getName())){
				ps.setDate(5,new Date(System.currentTimeMillis()));
			}else{
				ps.setNull(5,Types.NULL);
			}
			ps.setString(6, salesactionId);
			ps.addBatch();
		} 
			
		int num[]=ps.executeBatch();
		ps.close();				
		this.unsetModified();
		return this.getPK();
	}
	
	/** 
	 * writes a new object to the persistent store for the first time
	 * by passing a given ErpGiftCardDlvConfirmModel.
	 * 
	 * @param conn a SQLConnection to use to write this object to the persistent store
	 * @throws SQLException any problems encountered while writing this object to the persistent store
	 * @return the unique identity of this new object in the persistent store
	 */
	public PrimaryKey create(Connection conn, ErpGiftCardDlvConfirmModel model) throws SQLException {
		String salesactionId=getSalesActionId();
		
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_DELIVERY_INFO(ID, GIFT_CARD_ID,RECIPIENT_ID,DELIVERY_MODE,EMAIL_SENT,SALESACTION_ID) VALUES(?,?,?,?,?,?)");
		
		int index = 1;		
		List recList=model.getDlvInfoTranactionList();
	
		for(int i=0;i<recList.size();i++)
		{
			ErpGCDlvInformationHolder holder=(ErpGCDlvInformationHolder)recList.get(i);			
			String id=SequenceGenerator.getNextId(conn, "CUST");			
			ps.setString(1, id);
			ps.setString(2, holder.getGiftCardId());
			ps.setString(3, holder.getRecepientModel().getId());
			ps.setString(4, holder.getRecepientModel().getDeliveryMode().getName());
			if("E".equalsIgnoreCase(holder.getRecepientModel().getDeliveryMode().getName())){
				ps.setDate(5,new Date(System.currentTimeMillis()));
			}else{
				ps.setNull(5,Types.NULL);
			}
			ps.setString(6, salesactionId);
			ps.addBatch();
		} 
			
		int num[]=ps.executeBatch();
		ps.close();				
		this.unsetModified();
		return this.getPK();
	}
	
	private static final String SELECT_GC_RECEPIENTS_SQL="SELECT GCR.ID as ID, GCR.CUSTOMER_ID,GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG, "+ 
														"GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT,GC.GIVEX_NUM, GCR.ORDERLINE_NUMBER " +
														"FROM CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, CUST.GIFT_CARD GC "+ 
														"WHERE GDCC.SALESACTION_ID=? AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID";
		/** 
	 * Reads an object from the persistent store
	 * 
	 * @param conn a SQLConnection to use when reading this object from the persistent store
	 * @throws SQLException any problems entountered while reading this object from the persistent store
	 */
	public void load(Connection conn) throws SQLException {
		super.load(conn, this.model);
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_RECEPIENTS_SQL);						
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		List regTranList=new ArrayList();
		while (rs.next()) {
			ErpGCDlvInformationHolder holder=new ErpGCDlvInformationHolder();
			ErpRecipentModel recModel=new ErpRecipentModel();
			//this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));			
			//this.model.setAmount(rs.getDouble("AMOUNT"));				
			//this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));			
			recModel.setId(rs.getString("ID"));
			recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
			recModel.setSenderName(rs.getString("SENDER_NAME"));
			recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
			recModel.setRecipientName(rs.getString("RECIP_NAME"));
			recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
			recModel.setSale_id(this.getPK().getId());
			recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
			recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs.getString("DELIVERY_MODE")));
			recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
			recModel.setAmount(rs.getDouble("GC_AMOUNT"));
			recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
			holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
			holder.setGivexNum(ErpGiftCardUtil.decryptGivexNum(rs.getString("GIVEX_NUM")));
			holder.setRecepientModel(recModel);
			regTranList.add(holder);			
		} 
		this.model.setDlvInfoTranactionList(regTranList);
		rs.close();
		ps.close();		
		this.unsetModified();								
	}

	
}
