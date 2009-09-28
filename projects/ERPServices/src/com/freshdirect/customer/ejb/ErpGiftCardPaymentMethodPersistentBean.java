package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;

public class ErpGiftCardPaymentMethodPersistentBean extends DependentPersistentBeanSupport {

	ErpGiftCardModel model;

	/**
	 * Default constructor.
	 */
	public ErpGiftCardPaymentMethodPersistentBean() {
		super();
	}

	/**
	 * Load constructor.
	 */
	public ErpGiftCardPaymentMethodPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpPaymentMethodModel to copy from
	 */
	public ErpGiftCardPaymentMethodPersistentBean(ErpGiftCardModel model) {
		super(model.getPK());
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpPaymentMethodModel object.
	 */
	public ModelI getModel() {
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		this.model = (ErpGiftCardModel)model;		
		this.setModified();
	}


	/**
	 * Find ErpPaymentMethodPersistentBean objects for a given parent.
	 *
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 *
	 * @return a List of ErpPaymentMethodPersistentBean objects (empty if found none).
	 *
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		List lst = new LinkedList();
		//Loads only customer gift cards that has a non-zero balance.
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.GIFT_CARD WHERE ID IN " +
													"(SELECT GIFT_CARD_ID FROM CUST.GIFT_CARD_CUST_INFO WHERE CUSTOMER_ID=?) AND BALANCE > 0");
		ResultSet rs = null;
		try {
			ps.setString(1, parentPK.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpPaymentMethodPersistentBean bean = new ErpPaymentMethodPersistentBean(new PrimaryKey(rs.getString(1)), conn);
				bean.setParentPK(parentPK);
				lst.add(bean);
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}
		return lst;
	}


	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.GIFT_CARD_CUST_INFO (ID,CUSTOMER_ID, GIFT_CARD_ID) values (?,?,?)");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, this.getParentPK().getId());
		ps.setString(index++, model.getPK().getId());
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
			ps = null;
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID,GIVEX_NUM,BALANCE,ORIG_AMOUNT,CERTIFICATE_NUM,SALE_ID,CARD_TYPE FROM CUST.GIFT_CARD WHERE ID = ?");
		ResultSet rs = null;
		try {
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				//EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE"));
				//model = (ErpPaymentMethodModel)PaymentManager.createInstance(paymentMethodType);
				model = new ErpGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				model.setPK(pk);
				model.setAccountNumber(ErpGiftCardUtil.decryptGivexNum(rs.getString("GIVEX_NUM")));
				model.setBalance(rs.getDouble("BALANCE"));
				model.setOriginalAmount(rs.getDouble("ORIG_AMOUNT"));
				model.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
				model.setPurchaseSaleId(rs.getString("SALE_ID"));
				model.setCardType(EnumCardType.getEnum(rs.getString("CARD_TYPE")));
				
			} else {
				throw new SQLException("No such ErpGCPaymentMethod PK: " + this.getPK());
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}
		
		this.unsetModified();
	}

	public final void store(Connection conn) throws SQLException {
		throw new UnsupportedOperationException("Object cannot be modified");
	}

	public void remove(Connection conn) throws SQLException {
		
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.GIFT_CARD_CUST_INFO WHERE GIFT_CARD_ID=?");
		try {
			ps.setString(1, this.getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not deleted");
			}
		} finally {
			if (ps != null) ps.close();
			ps = null;
		}
		
		this.setPK(null); // make it anonymous
	}

}
