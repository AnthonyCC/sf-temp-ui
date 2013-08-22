package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.PaymentManager;


/**
 * ErpPaymentInfo persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpPaymentInfoPersistentBean extends ErpReadOnlyPersistentBean {
	
	private static final long	serialVersionUID	= 1064369139597619759L;
	
	private ErpPaymentMethodModel model;
	
	/** Default constructor. */
	public ErpPaymentInfoPersistentBean() {
		super();
		// credit model as default
		this.model = (ErpPaymentMethodModel)PaymentManager.createInstance(); // default to ErpCreditCardModel for now 
	}

	/** Load constructor. */
	public ErpPaymentInfoPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/** Load constructor. */
	public ErpPaymentInfoPersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 */
	public ErpPaymentInfoPersistentBean(ErpPaymentMethodModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpPaymentInfoModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpPaymentMethodModel)model;
	}

	/**
	 * Find ErpPaymentInfoPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpPaymentInfoPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List<ErpPaymentInfoPersistentBean> findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		List<ErpPaymentInfoPersistentBean> lst = new LinkedList<ErpPaymentInfoPersistentBean>();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM CUST.PAYMENTINFO WHERE SALESACTION_ID=? and CARD_TYPE <> 'GCP'");
		ResultSet rs = null;
		try {
			ps.setString(1, parentPK.getId());
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpPaymentInfoPersistentBean bean = new ErpPaymentInfoPersistentBean(new PrimaryKey(rs.getString("SALESACTION_ID")), rs);
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
		//String id = this.getNextId(conn);
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PAYMENTINFO (SALESACTION_ID, NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, BILLING_REF, ON_FD_ACCOUNT, REFERENCED_ORDER, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, BANK_NAME, PROFILE_ID,ACCOUNT_NUM_MASKED) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int index = 1; 
		ps.setString(index++, this.getParentPK().getId());
		ps.setString(index++, this.model.getName());
		ps.setString(index++, this.model.getAccountNumber());
		if (model.getExpirationDate() != null) {
			ps.setDate(index++, new java.sql.Date(this.model.getExpirationDate().getTime()));
		} else {
			ps.setNull(index++, Types.DATE);
		}
		if (model.getCardType() != null) {
			ps.setString(index++, this.model.getCardType().getFdName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		ps.setString(index++, this.model.getAddress1());
		ps.setString(index++, this.model.getAddress2());
		ps.setString(index++, this.model.getApartment());
		ps.setString(index++, this.model.getCity());
		ps.setString(index++, this.model.getState());
		ps.setString(index++, this.model.getZipCode());
		ps.setString(index++, this.model.getCountry());
		ps.setString(index++, this.model.getBillingRef());
		ps.setString(index++, this.model.getPaymentType().getName());
		ps.setString(index++, this.model.getReferencedOrder());
		if (model.getPaymentMethodType() != null) {
			ps.setString(index++, this.model.getPaymentMethodType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (model.getAbaRouteNumber() != null) {
			ps.setString(index++, this.model.getAbaRouteNumber());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (model.getBankAccountType() != null) {
			ps.setString(index++, this.model.getBankAccountType().getName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}
		if (model.getBankName() != null) {
			ps.setString(index++, this.model.getBankName());
		} else {
			ps.setNull(index++, Types.VARCHAR);			
		}	
		if(model.getProfileID()!=null) {
			ps.setString(index++, model.getProfileID() );	
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		if(model.getMaskedAccountNumber()!=null) {
			ps.setString(index++, model.getMaskedAccountNumber() );	
		}else{
			ps.setNull(index++, Types.VARCHAR);
		}
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(this.getParentPK());
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
		PreparedStatement ps = conn.prepareStatement("SELECT NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, BILLING_REF, ON_FD_ACCOUNT, REFERENCED_ORDER, PAYMENT_METHOD_TYPE, ABA_ROUTE_NUMBER, BANK_ACCOUNT_TYPE, BANK_NAME, PROFILE_ID,ACCOUNT_NUM_MASKED FROM CUST.PAYMENTINFO WHERE SALESACTION_ID=?");
		ResultSet rs = null;
		try {
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				this.loadFromResultSet(rs);
			} else {
				throw new SQLException("No such ErpPaymentInfo PK: " + this.getPK());
			}
		} finally {
			if (rs != null) rs.close();
			rs = null;
			if (ps != null) ps.close();
			ps = null;
		}
		
	}
	
	private void loadFromResultSet(ResultSet rs) throws SQLException {
		EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE"));
		this.model = (ErpPaymentMethodModel)PaymentManager.createInstance(paymentMethodType);
		this.model.setPK(getParentPK());
		this.model.setName(rs.getString("NAME"));
		this.model.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
		this.model.setExpirationDate(rs.getDate("EXPIRATION_DATE"));
		this.model.setCardType(EnumCardType.getCardType(rs.getString("CARD_TYPE")));
		this.model.setAddress1(rs.getString("ADDRESS1"));
		this.model.setAddress2(rs.getString("ADDRESS2"));
		this.model.setApartment(rs.getString("APARTMENT"));
		this.model.setCity(rs.getString("CITY"));
		this.model.setState(rs.getString("STATE"));
		this.model.setZipCode(rs.getString("ZIP_CODE"));
		this.model.setCountry(rs.getString("COUNTRY"));
		this.model.setBillingRef(rs.getString("BILLING_REF"));
		EnumPaymentType pt = EnumPaymentType.getEnum(rs.getString("ON_FD_ACCOUNT")); 
		this.model.setPaymentType(pt != null ? pt : EnumPaymentType.REGULAR);
		this.model.setReferencedOrder(rs.getString("REFERENCED_ORDER"));
		this.model.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
		this.model.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
		this.model.setBankName(rs.getString("BANK_NAME"));
		this.model.setProfileID(rs.getString("PROFILE_ID"));
		this.model.setAccountNumLast4(rs.getString("ACCOUNT_NUM_MASKED"));
		//if(paymentMethodType.equals(EnumPaymentMethodType.GIFTCARD)) {
			//Set the certification number for gift card.
			//model.setCertificateNumber(ErpGiftCardUtil.getCertificateNumber(rs.getString("ACCOUNT_NUMBER")));
		//}
		this.unsetModified();
	}
	
	public PrimaryKey getPK(){
		return this.getParentPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
}
