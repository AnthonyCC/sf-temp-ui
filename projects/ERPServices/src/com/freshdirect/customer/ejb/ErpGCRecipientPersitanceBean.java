package com.freshdirect.customer.ejb;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.ejb.EJBException;

import org.hibernate.SQLQuery;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.NVL;


/**
 * ErpOrderLine persistent bean.
 * @version	$Revision:10$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-persistent
 */
public class ErpGCRecipientPersitanceBean extends ErpReadOnlyPersistentBean {
	
	private ErpRecipentModel model;
	
	public ErpGCRecipientPersitanceBean() {
		super();
		this.model = new ErpRecipentModel();
	}

	/** Load constructor. */
	public ErpGCRecipientPersitanceBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	private ErpGCRecipientPersitanceBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpOrderLineModel to copy from
	 */
	public ErpGCRecipientPersitanceBean(ErpRecipentModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpOrderLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpRecipentModel)model;
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	/**
	 * Find ErpOrderLinePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpOrderLinePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("select ID,CUSTOMER_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID,DELIVERY_MODE,AMOUNT,PERSONAL_MSG from cust.GIFT_CARD_RECIPIENT where SALESACTION_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();		
		while (rs.next()) {
			ErpGCRecipientPersitanceBean bean = new ErpGCRecipientPersitanceBean( new PrimaryKey(rs.getString("ID")), rs );
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		
		rs.close();
		ps.close();
		return lst;
	}
	
	private final static String INSERT_QUERY =
		"INSERT INTO CUST.GIFT_CARD_RECIPIENT(ID,CUSTOMER_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID,DELIVERY_MODE,AMOUNT,PERSONAL_MSG,SALESACTION_ID,ORDERLINE_NUMBER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	public PrimaryKey create(Connection conn) throws SQLException {
				
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
				
		ps.setString(1, id);
		ps.setString(2, model.getCustomerId());
		if(model.getSenderName()!=null){
		 ps.setString(3, model.getSenderName());
		}else{
		  ps.setNull(3, Types.NULL); 	
		}
		if(model.getSenderEmail()!=null){
			 ps.setString(4, model.getSenderEmail());
		}else{
			  ps.setNull(4, Types.NULL); 	
		}
		if(model.getRecipientName()!=null){
			 ps.setString(5, model.getRecipientName());
		}else{
			  ps.setNull(5, Types.NULL); 	
		}			
		if(model.getRecipientEmail()!=null){
			 ps.setString(6, model.getRecipientEmail());
		}else{
			  ps.setNull(6, Types.NULL); 	
		}

		if(model.getTemplateId()!=null){
			 ps.setString(7, model.getTemplateId());
		}else{
			  ps.setNull(7, Types.NULL); 	
		}

		if(model.getDeliveryMode()!=null){
			 ps.setString(8, model.getDeliveryMode().getName());
		}else{
			  ps.setNull(8, Types.NULL); 	
		}			
		ps.setBigDecimal(9, new BigDecimal(String.valueOf(model.getAmount())));					
		ps.setString(10, model.getPersonalMessage());
		ps.setString(11, this.getParentPK().getId());
		ps.setString(12,model.getOrderLineId());
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
				
		PreparedStatement ps = conn.prepareStatement("select ID,CUSTOMER_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID,DELIVERY_MODE,AMOUNT,PERSONAL_MSG from cust.GIFT_CARD_RECIPIENT where ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(rs);
		} else {
			throw new SQLException("No such ErpOrderLine PK: " + this.getPK());
		}
		rs.close();
		ps.close();
	}

	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setId(rs.getString("ID"));
		this.model.setCustomerId(rs.getString("CUSTOMER_ID"));
		this.model.setSenderName(rs.getString("SENDER_NAME"));
		this.model.setSenderEmail(rs.getString("SENDER_EMAIL"));
		this.model.setRecipientName(rs.getString("RECIP_NAME"));
		this.model.setRecipientEmail(rs.getString("RECIP_EMAIL"));
		this.model.setSale_id(this.getPK().getId());
		this.model.setTemplateId(rs.getString("TEMPLATE_ID"));
		this.model.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs.getString("DELIVERY_MODE")));
		this.model.setPersonalMessage(rs.getString("PERSONAL_MSG"));
		this.model.setAmount(rs.getDouble("AMOUNT"));
		this.unsetModified();
	}
}

