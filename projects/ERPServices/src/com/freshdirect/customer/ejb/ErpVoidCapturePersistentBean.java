package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.customer.ErpVoidCaptureModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.common.customer.EnumCardType;

public class ErpVoidCapturePersistentBean extends ErpPaymentPersistentBean {
	
	private ErpVoidCaptureModel model;
	
	public ErpVoidCapturePersistentBean() {
		super();
		this.model = new ErpVoidCaptureModel();
	}
	
	public ErpVoidCapturePersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}
	
	public ErpVoidCapturePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
	
	protected ErpPaymentModel createModel() {
		return new ErpVoidCaptureModel();
	}

	public ModelI getModel() {
		return this.model.deepCopy();
	}
	
	public void setFromModel(ModelI m) {
		this.model = (ErpVoidCaptureModel)m;
	}
	
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		
		List lst = new ArrayList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.VOID_CAPTURE.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpVoidCapturePersistentBean bean = new ErpVoidCapturePersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	protected String getTransactionType() {
		return EnumTransactionType.VOID_CAPTURE.getCode();
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		
		String salesactionId = super.create(conn, this.model).getId();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENT(SALESACTION_ID, SEQUENCE_NUMBER, AUTH_CODE, MERCHANT_ID, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE) VALUES(?,?,?,?,?,?,?,?,?)");
		int index = 1;
		ps.setString(index++, salesactionId);
		ps.setString(index++, this.model.getSequenceNumber());
		ps.setString(index++, this.model.getAuthCode());
		ps.setString(index++, this.model.getMerchantId());
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

	public void load(Connection conn) throws SQLException {
		
		super.load(conn, this.model);
		
		PreparedStatement ps = conn.prepareStatement("SELECT SEQUENCE_NUMBER, AUTH_CODE, MERCHANT_ID, CARD_TYPE, CCNUM_LAST4, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE FROM CUST.PAYMENT WHERE SALESACTION_ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {	
			this.model.setAuthCode(rs.getString("AUTH_CODE"));
			this.model.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
			this.model.setMerchantId(rs.getString("MERCHANT_ID"));
			this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
			this.model.setCcNumLast4(rs.getString("CCNUM_LAST4"));
			this.model.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
			this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
			this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
		}
		
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		this.unsetModified();
	}

}
