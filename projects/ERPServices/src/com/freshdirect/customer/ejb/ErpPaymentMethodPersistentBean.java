package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.giftcard.ErpGiftCardUtil;

public class ErpPaymentMethodPersistentBean extends DependentPersistentBeanSupport {

	ErpPaymentMethodModel model;

	/**
	 * Default constructor.
	 */
	public ErpPaymentMethodPersistentBean() {
		super();
	}

	/**
	 * Load constructor.
	 */
	public ErpPaymentMethodPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 *
	 * @param bean ErpPaymentMethodModel to copy from
	 */
	public ErpPaymentMethodPersistentBean(ErpPaymentMethodModel model) {
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
		this.model = (ErpPaymentMethodModel)model;		
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
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.PAYMENTMETHOD WHERE CUSTOMER_ID=?");
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
		System.out.println("Inside calling create ******************* ");
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENTMETHOD (ID, CUSTOMER_ID, NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_NAME, BANK_ACCOUNT_TYPE, ADDRESS2, APARTMENT,  ADDRESS1, CITY, STATE, ZIP_CODE, COUNTRY) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int index = 1;
		/*
		if(this.model.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD)) {
			System.out.println("Inside calling GC create ******************* ");
			ps.setString(index++, id);
			ps.setString(index++, this.getParentPK().getId());
			ps.setString(index++, "N/A");
			ps.setString(index++, model.getAccountNumber());
			if (model.getExpirationDate() != null) {
				ps.setDate(index++, new java.sql.Date(model.getExpirationDate().getTime()));
			} else {
				ps.setNull(index++, Types.DATE);			
			}
			if (model.getCardType() != null) {
				ps.setString(index++, model.getCardType().getFdName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getPaymentMethodType() != null) {
				ps.setString(index++, model.getPaymentMethodType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getAbaRouteNumber() != null) {
				ps.setString(index++, model.getAbaRouteNumber());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getBankName() != null) {
				ps.setString(index++, model.getBankName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getBankAccountType() != null) {
				ps.setString(index++, model.getBankAccountType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			ps.setString(index++, "N/A");
			if (model.getAddress2() != null) {
				ps.setString(index++, model.getAddress2());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getApartment() != null) {
				ps.setString(index++, model.getApartment());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			ps.setString(index++, "N/A");
			ps.setString(index++, "N/A");
			ps.setString(index++, "N/A");
			ps.setString(index++, "N/A");

		} else {*/
			System.out.println("Inside calling non GC create ******************* ");
			ps.setString(index++, id);
			ps.setString(index++, this.getParentPK().getId());
			ps.setString(index++, model.getName());
			ps.setString(index++, model.getAccountNumber());
			if (model.getExpirationDate() != null) {
				ps.setDate(index++, new java.sql.Date(model.getExpirationDate().getTime()));
			} else {
				ps.setNull(index++, Types.DATE);			
			}
			if (model.getCardType() != null) {
				ps.setString(index++, model.getCardType().getFdName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getPaymentMethodType() != null) {
				ps.setString(index++, model.getPaymentMethodType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getAbaRouteNumber() != null) {
				ps.setString(index++, model.getAbaRouteNumber());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getBankName() != null) {
				ps.setString(index++, model.getBankName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getBankAccountType() != null) {
				ps.setString(index++, model.getBankAccountType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getAddress2() != null) {
				ps.setString(index++, model.getAddress2());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if (model.getApartment() != null) {
				ps.setString(index++, model.getApartment());
			} else {
				ps.setNull(index++, Types.VARCHAR);			
			}
			if(this.model.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD)) {
				ps.setString(index++, "NA");
				ps.setString(index++, "NA");
				ps.setString(index++, "NA");
				ps.setString(index++, "NA");
				ps.setString(index++, "NA");
				
			} else {
				ps.setString(index++, model.getAddress1());
				ps.setString(index++, model.getCity());
				ps.setString(index++, model.getState());
				ps.setString(index++, model.getZipCode());
				ps.setString(index++, model.getCountry());
				
			}
		//}
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
		PreparedStatement ps = conn.prepareStatement("SELECT NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_NAME, BANK_ACCOUNT_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, CUSTOMER_ID FROM CUST.PAYMENTMETHOD WHERE ID = ?");
		ResultSet rs = null;
		try {
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE"));
				model = (ErpPaymentMethodModel)PaymentManager.createInstance(paymentMethodType);
				model.setName(rs.getString("NAME"));
				model.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
				model.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
				model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
				model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
				model.setBankName(rs.getString("BANK_NAME"));
				model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
				model.setAddress1(rs.getString("ADDRESS1"));
				model.setAddress2(NVL.apply(rs.getString("ADDRESS2"), ""));
				model.setApartment(NVL.apply(rs.getString("APARTMENT"), ""));
				model.setCity(rs.getString("CITY"));
				model.setState(rs.getString("STATE"));
				model.setZipCode(rs.getString("ZIP_CODE"));
				model.setCountry(rs.getString("COUNTRY"));
				model.setCustomerId(rs.getString("CUSTOMER_ID"));
				setParentPK(new PrimaryKey(model.getCustomerId()));
				model.setPK(getPK());
				if(paymentMethodType != null && paymentMethodType.equals(EnumPaymentMethodType.GIFTCARD)) {
					//Set the certification number for gift card.
					model.setCertificateNumber(ErpGiftCardUtil.getCertificateNumber(rs.getString("ACCOUNT_NUMBER")));
				}
			} else {
				throw new SQLException("No such ErpPaymentMethod PK: " + this.getPK());
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}
		
		this.unsetModified();
	}

	public void store(Connection conn) throws SQLException {
		if(this.model.getPaymentMethodType().equals(EnumPaymentMethodType.GIFTCARD)) {
			//Do Nothing. As There will not be any update to this model.
			return;
		}
		
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.PAYMENTMETHOD SET CUSTOMER_ID = ?, NAME = ?, ACCOUNT_NUMBER = ?, EXPIRATION_DATE = ?, CARD_TYPE = ?, PAYMENT_METHOD_TYPE=?, ABA_ROUTE_NUMBER=?, BANK_NAME=?, BANK_ACCOUNT_TYPE=?, ADDRESS1 = ?, ADDRESS2 = ?, APARTMENT = ?, CITY = ?, STATE = ?, ZIP_CODE = ?, COUNTRY = ? WHERE ID=?");
		
		try {
			int index = 1;
			ps.setString(index++, this.getParentPK().getId());
			ps.setString(index++, model.getName());
			ps.setString(index++, model.getAccountNumber());
			if (model.getExpirationDate() != null) {
				ps.setDate(index++, new java.sql.Date(model.getExpirationDate().getTime()));
			} else {
				ps.setNull(index++, Types.DATE);				
			}
			if (model.getCardType() != null) {
				ps.setString(index++, model.getCardType().getFdName());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			if (model.getPaymentMethodType() != null) {
				ps.setString(index++, model.getPaymentMethodType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			if (model.getAbaRouteNumber() != null) {
				ps.setString(index++, model.getAbaRouteNumber());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			if (model.getBankName() != null) {
				ps.setString(index++, model.getBankName());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			if (model.getBankAccountType() != null) {
				ps.setString(index++, model.getBankAccountType().getName());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			ps.setString(index++, model.getAddress1());
			if (model.getAddress2() != null) {
				ps.setString(index++, model.getAddress2());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			if (model.getApartment() != null) {
				ps.setString(index++, model.getApartment());
			} else {
				ps.setNull(index++, Types.VARCHAR);				
			}
			ps.setString(index++, model.getCity());
			ps.setString(index++, model.getState());
			ps.setString(index++, model.getZipCode());
			ps.setString(index++, model.getCountry());
			ps.setString(index++, this.getPK().getId());
			
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
		} finally {
			if (ps != null) ps.close();
			ps = null;
		}
		this.unsetModified();

		
	}

	public void remove(Connection conn) throws SQLException {
		
		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.PAYMENTMETHOD WHERE ID=?");
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
