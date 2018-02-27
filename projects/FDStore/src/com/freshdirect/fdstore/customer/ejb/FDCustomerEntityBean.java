/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.customer.ejb;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.customer.FDCustomerI;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;

/**
 * FDCustomer entity bean implementation.
 *
 * @version    $Revision$
 * @author     $Author$
 *
 * @ejbHome <{FDCustomerHome}>
 * @ejbRemote <{FDCustomerEB}>
 * @ejbPrimaryKey <{PrimaryKey}>
 * @stereotype fd-entity
 */
public class FDCustomerEntityBean extends EntityBeanSupport implements FDCustomerI {

	private final static Category LOGGER = LoggerFactory.getInstance( FDCustomerEntityBean.class );

	private String erpCustomerPK;
	private int loginCount;
	private java.util.Date lastLogin;
	private java.util.Date passwordRequestExpiration;
	private String passwordRequestId;
	private int passwordRequestAttempts;
	private String defaultShipToAddressPK;
	private String defaultPaymentMethodPK;
	private String defaultDepotLocationPK;
	private boolean tcAcknowledgeToStore;
	private FDProfilePersistentBean profile;
	private String passwordHint;
	private String depotCode;
	private int pymtVerifyAttempts;
	private FDCustomerEStorePersistentBean customerEStore;
	private FDCustomerSmsPreferencePersistentBean customerSmsPreferences;
//	private String rafClickId;
//	private String rafPromoCode;
	private EnumPaymentMethodDefaultType defaultPaymentMethodType;
	private boolean dpFreeTrailOptin;
	/**
	 * Copy into model.
	 *
	 * @return FDCustomerModel object.
	 */
	@Override
    public ModelI getModel() {
		FDCustomerModel model = new FDCustomerModel();
		super.decorateModel(model);
		model.setLastLogin(this.lastLogin);
		model.setLoginCount(this.loginCount);
		model.setDefaultShipToAddressPK(this.defaultShipToAddressPK);
		model.setDefaultPaymentMethodPK(this.defaultPaymentMethodPK);
		model.setDefaultDepotLocationPK(this.defaultDepotLocationPK);
		model.setErpCustomerPK(this.erpCustomerPK);
		model.setProfile((ProfileModel)this.profile.getModel());
		model.setPasswordHint(this.passwordHint);
		model.setDepotCode(this.depotCode);
		model.setPasswordRequestExpiration(this.passwordRequestExpiration);
		model.setCustomerEStoreModel((FDCustomerEStoreModel)this.customerEStore.getModel());
//		model.setRafClickId(this.rafClickId);
//		model.setRafPromoCode(this.rafPromoCode);
		model.setCustomerSmsPreferenceModel((FDCustomerEStoreModel)this.customerSmsPreferences.getModel());
		model.setDefaultPaymentType(this.defaultPaymentMethodType);
		return model;
	}

	/**
	 * Copy from model.
	 */
	@Override
    public void setFromModel(ModelI model) {
		// copy properties from model
		FDCustomerModel m = (FDCustomerModel)model;
		this.erpCustomerPK = m.getErpCustomerPK();
		//this.lastLogin = m.getLastLogin();
		this.defaultShipToAddressPK = m.getDefaultShipToAddressPK();
		this.defaultPaymentMethodPK = m.getDefaultPaymentMethodPK();
		this.defaultDepotLocationPK = m.getDefaultDepotLocationPK();
		this.profile.setFromModel(m.getProfile());
		this.passwordHint = m.getPasswordHint();
		this.depotCode = m.getDepotCode();
		this.customerEStore.setFromModel(m.getCustomerEStoreModel());
//		this.rafClickId = m.getRafClickId();
//		this.rafPromoCode = m.getRafPromoCode();
		this.customerSmsPreferences.setFromModel(m.getCustomerSmsPreferenceModel());
		this.defaultPaymentMethodType = m.getDefaultPaymentType();
		this.setModified();
	}

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    @Override
    protected String getResourceCacheKey() {
        return "com.freshdirect.fdstore.customer.ejb.FDCustomerHome";
    }

	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM CUST.FDCUSTOMER where ID = ?");
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find FDCustomer with PK " + pk);
			}
			return new PrimaryKey(rs.getString(1));
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}

	public PrimaryKey ejbFindByErpCustomerId(String erpId) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM CUST.FDCUSTOMER where ERP_CUSTOMER_ID = ?");
			ps.setString(1, erpId);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find FDCustomer with erp customer id " + erpId);
			}
			return new PrimaryKey(rs.getString(1));
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}

	public PrimaryKey ejbFindByCookie(String cookie) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT fdc.ID AS ID FROM CUST.FDCUSTOMER fdc, CUST.FDUSER fdu where fdu.FDCUSTOMER_ID=fdc.ID AND fdu.COOKIE = ?");
			ps.setString(1, cookie);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find FDCustomer with cookie " + cookie);
			}
			return new PrimaryKey(rs.getString(1));
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}


	public PrimaryKey ejbFindByUserId(String email, EnumServiceType type) throws FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT F.ID FROM CUST.CUSTOMER C, CUST.FDCUSTOMER F WHERE C.USER_ID LIKE LOWER(?) AND F.ERP_CUSTOMER_ID = C.ID");
			ps.setString(1, email);
			rs = ps.executeQuery();
			int fetchSize = 0;
			if(rs.next()) {
				fetchSize = rs.getFetchSize();
			}
			if (fetchSize < 1 && (null == type || !EnumServiceType.IPHONE.equals(type))) {
				throw new ObjectNotFoundException("Unable to find FDCustomer with email " + email);
			} else if(fetchSize < 1 && null != type && EnumServiceType.IPHONE.equals(type) ) {
				LOGGER.info("Unrecognized customer from IPHONE APP capture email");
				// this is expected behavior for non-customers visiting iphone app
				// no need to throw ObjectNotFoundException, just return null to verify customer does not exist
				return null;
			}
			return new PrimaryKey(rs.getString(1));
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}


	public PrimaryKey ejbFindByUserId(String email) throws FinderException {
		return ejbFindByUserId(email, null);

	}


	public PrimaryKey ejbFindByUserIdAndPasswordRequest(String email, String passReq) throws FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT F.ID FROM CUST.CUSTOMER C, CUST.FDCUSTOMER F WHERE C.USER_ID LIKE LOWER(?) AND F.PASSREQ_ID LIKE ? AND F.ERP_CUSTOMER_ID = C.ID");
			ps.setString(1, email);
			ps.setString(2, passReq);
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find FDCustomer with email " + email);
			}
			return new PrimaryKey(rs.getString(1));
		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}

	}


	@Override
    public PrimaryKey create(Connection conn) throws SQLException {
		this.setPK(new PrimaryKey(this.getNextId(conn, "CUST")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.FDCUSTOMER (ID, ERP_CUSTOMER_ID, LOGIN_COUNT, LAST_LOGIN, DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOCATION, DEPOT_CODE, RAF_CLICK_ID, RAF_PROMO_CODE, DEFAULT_PAYMENT_METHOD_TYPE) values (?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, this.getPK().getId());
		ps.setString(2, this.erpCustomerPK);
		ps.setInt(3, this.loginCount);
		if(lastLogin != null){
			ps.setDate(4, new java.sql.Date(this.lastLogin.getTime()));
		}else{
			ps.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
		}
		ps.setString(5, this.defaultShipToAddressPK);
		ps.setString(6, this.defaultPaymentMethodPK);
	//	ps.setString(7, this.passwordHint); removed this column
		ps.setString(7, this.defaultDepotLocationPK);
		ps.setString(8, this.depotCode);
		ps.setString(9, StringUtils.EMPTY);
		ps.setString(10, StringUtils.EMPTY);
		ps.setString(11, (null != this.defaultPaymentMethodType &&  !this.defaultPaymentMethodType.getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName()))?
				this.defaultPaymentMethodType.getName(): null);

		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			this.setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}

		//create children
		this.profile.setParentPK(this.getPK());
		this.profile.create(conn);

		this.customerEStore.setParentPK(this.getPK());
		replaceCustomerEStoreModel();
		setEmailPreferenceFlag(true);//[APPDEV-4574]-Default 'emil optin' is true for the store the customer is registering from.
		this.customerEStore.create(conn);
		this.customerSmsPreferences.setParentPK(this.getPK());
		this.customerSmsPreferences.load(conn);


		return this.getPK();
	}

	/**
	 *
	 */
	private void replaceCustomerEStoreModel() {
		FDCustomerEStoreModel customerEStoreModel  =(FDCustomerEStoreModel)customerEStore.getModel();
		if(customerEStoreModel.geteStoreId()==null)
			customerEStoreModel.seteStoreId(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));

		customerEStoreModel.setDefaultShipToAddressPK(this.getDefaultShipToAddressPK());
		customerEStoreModel.setDefaultPaymentMethodPK(this.getDefaultPaymentMethodPK());
		customerEStoreModel.setDefaultDepotLocationPK(this.getDefaultDepotLocationPK());
		customerEStore.setFromModel(customerEStoreModel);
	}

	private void setEmailPreferenceFlag(boolean emailOptIn){
		FDCustomerEStoreModel customerEStoreModel  =(FDCustomerEStoreModel)customerEStore.getModel();
		if(customerEStoreModel.geteStoreId()==null)
			customerEStoreModel.seteStoreId(EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId())));
		customerEStoreModel.setEmailOptIn(emailOptIn);
		customerEStore.setFromModel(customerEStoreModel);
	}

	public void setFDCustomerEStore(FDCustomerEStoreModel fdCustomerEStoreModel){
		customerEStore.setFromModel(fdCustomerEStoreModel);
		this.setModified();
	}

	@Override
    public void load(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT ERP_CUSTOMER_ID, LOGIN_COUNT, LAST_LOGIN, DEFAULT_SHIPTO, DEFAULT_PAYMENT, PASSREQ_EXPIRATION, PASSREQ_ID, PASSREQ_ATTEMPTS, PASSWORD_HINT, DEFAULT_DEPOT_LOCATION, DEPOT_CODE, PYMT_VERIFY_ATTEMPTS, DEFAULT_PAYMENT_METHOD_TYPE FROM CUST.FDCUSTOMER WHERE ID = ? ");
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new SQLException("No such FDCustomer PK: " + this.getPK());
			}
			// load properties from result set
			this.erpCustomerPK = rs.getString("ERP_CUSTOMER_ID");
			this.loginCount = rs.getInt("LOGIN_COUNT");
			this.lastLogin = rs.getDate("LAST_LOGIN");
			this.defaultShipToAddressPK = rs.getString("DEFAULT_SHIPTO");
			this.defaultPaymentMethodPK = rs.getString("DEFAULT_PAYMENT");
			this.passwordRequestExpiration = rs.getTimestamp("PASSREQ_EXPIRATION");
			// rs.wasNull()
			this.passwordRequestId = rs.getString("PASSREQ_ID");
			this.passwordRequestAttempts = rs.getInt("PASSREQ_ATTEMPTS");
			//this.passwordHint = rs.getString("PASSWORD_HINT");
			this.defaultDepotLocationPK = rs.getString("DEFAULT_DEPOT_LOCATION");
			this.depotCode = rs.getString("DEPOT_CODE");
			this.setPymtVerifyAttempts(rs.getInt("PYMT_VERIFY_ATTEMPTS"));
			String defaultPaymentMethodType = rs.getString("DEFAULT_PAYMENT_METHOD_TYPE");
			this.setDefaultPaymentMethodType((null != defaultPaymentMethodType && !"".equals(defaultPaymentMethodType))
					? EnumPaymentMethodDefaultType.getByName(defaultPaymentMethodType)
					: EnumPaymentMethodDefaultType.UNDEFINED);
		} finally {
			DaoUtil.close(rs, ps);
		}


		// load children
		this.profile.setParentPK(this.getPK());
		this.profile.load(conn);

		this.customerEStore.setParentPK(this.getPK());
		this.customerEStore.load(conn);
		this.customerSmsPreferences.setParentPK(this.getPK());
		this.customerSmsPreferences.load(conn);

		//Assigning the Estore specific values.
		if(null !=this.customerEStore.getModel() && null !=((FDCustomerEStoreModel)this.customerEStore.getModel()).geteStoreId()){
			this.defaultShipToAddressPK = ((FDCustomerEStoreModel)this.customerEStore.getModel()).getDefaultShipToAddressPK();
			if(getDefaultPaymentMethodType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())){
			this.defaultPaymentMethodPK = ((FDCustomerEStoreModel)this.customerEStore.getModel()).getDefaultPaymentMethodPK();
			}
			this.defaultDepotLocationPK = ((FDCustomerEStoreModel)this.customerEStore.getModel()).getDefaultDepotLocationPK();
			this.tcAcknowledgeToStore = ((FDCustomerEStoreModel)this.customerEStore.getModel()).getTcAcknowledge();
		}
	}

	@Override
    public void store(Connection conn) throws SQLException {
		PreparedStatement ps =null;
		try {
			if (super.isModified()) {
				ps = conn.prepareStatement(
						"UPDATE CUST.FDCUSTOMER SET ERP_CUSTOMER_ID = ?, LOGIN_COUNT = ?, LAST_LOGIN =?, DEFAULT_SHIPTO = ?, DEFAULT_PAYMENT =?, PASSREQ_EXPIRATION =?, PASSREQ_ID =?, PASSREQ_ATTEMPTS =?, PASSWORD_HINT =?, DEFAULT_DEPOT_LOCATION = ?, DEPOT_CODE = ?, PYMT_VERIFY_ATTEMPTS=?, DEFAULT_PAYMENT_METHOD_TYPE=? WHERE ID = ?");
				ps.setString(1, this.erpCustomerPK);
				ps.setInt(2, this.loginCount);
				ps.setDate(3, new java.sql.Date(this.lastLogin.getTime()));
				ps.setString(4, this.defaultShipToAddressPK);
				ps.setString(5, this.defaultPaymentMethodPK);
				if (this.passwordRequestExpiration == null) {
					ps.setNull(6, Types.TIMESTAMP);
				} else {
					ps.setTimestamp(6, new java.sql.Timestamp(this.passwordRequestExpiration.getTime()));
				}
				ps.setString(7, this.passwordRequestId);
				ps.setInt(8, this.passwordRequestAttempts);
				ps.setString(9, this.passwordHint);
				ps.setString(10, this.defaultDepotLocationPK);
				ps.setString(11, this.depotCode);
				ps.setInt(12, this.pymtVerifyAttempts);
				ps.setString(13,
						(null != this.defaultPaymentMethodType && !this.defaultPaymentMethodType.getName()
								.equals(EnumPaymentMethodDefaultType.UNDEFINED.getName()))
										? this.defaultPaymentMethodType.getName() : null);
				ps.setString(14, this.getPK().getId());

				if (ps.executeUpdate() != 1) {
					throw new SQLException("Row not updated");
				}

			}
		} finally {
			DaoUtil.close(ps);
		}
		// store children
		if(this.profile.isModified()){
			this.profile.store(conn);
		}

			replaceCustomerEStoreModel();
			customerEStore.store(conn);
			customerSmsPreferences.store(conn);
	}

	@Override
    public void remove(Connection conn) throws SQLException {
		// remove children
		this.profile.remove(conn);

		// remove self
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("DELETE FROM CUST.FDCUSTOMER WHERE ID = ?");
			ps.setString(1, this.getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not deleted");
			}
		} finally {
			DaoUtil.close(ps);
		}
	}


	/**
	 * Overriden isModified.
	 */
	@Override
    public boolean isModified() {
		// check children too
		return super.isModified() || this.profile.isModified();
	}

	@Override
    public void initialize(){
		profile = new FDProfilePersistentBean();
		this.erpCustomerPK = null;
		this.loginCount = 0;
		this.lastLogin = null;
		this.defaultShipToAddressPK = null;
		this.defaultPaymentMethodPK = null;
		this.defaultDepotLocationPK = null;
		this.passwordRequestExpiration = null;
		this.passwordRequestId = null;
		this.passwordRequestAttempts = 0;
		this.passwordHint = null;
		this.depotCode = null;
		this.pymtVerifyAttempts=0;
		customerEStore = new FDCustomerEStorePersistentBean();
		customerSmsPreferences = new FDCustomerSmsPreferencePersistentBean();
	}

	@Override
    public String getErpCustomerPK() {
		return this.erpCustomerPK;
	}

	@Override
    public void setErpCustomerPK(String erpCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
		this.setModified();
	}

	public void setProfileAttribute(String name, String value){
		this.profile.setAttribute(name, value);
	}

	public void removeProfileAttribute(String name){
		this.profile.removeAttribute(name);
	}

	@Override
    public void incrementLoginCount() {
		this.loginCount++;
		this.lastLogin = new java.util.Date();
		this.setModified();
	}

	@Override
    public int getLoginCount() {
		return this.loginCount;
	}

	@Override
    public java.util.Date getLastLogin() {
		return this.lastLogin;
	}

	@Override
    public String getDefaultShipToAddressPK() {
		return this.defaultShipToAddressPK;
	}

	@Override
    public void setDefaultShipToAddressPK(String addressPK) {
		this.defaultShipToAddressPK = addressPK;
		this.setModified();
	}

	@Override
    public String getDefaultPaymentMethodPK() {
		return this.defaultPaymentMethodPK;
	}

	@Override
    public void setDefaultPaymentMethodPK(String pmPK) {
		this.defaultPaymentMethodPK = pmPK;
		this.setModified();
	}

	@Override
    public String getDefaultDepotLocationPK(){
		return this.defaultDepotLocationPK;
	}

	@Override
    public void setDefaultDepotLocationPK(String locationId){
		this.defaultDepotLocationPK = locationId;
		this.setModified();
	}

	public String getDepotCode(){
		return this.depotCode;
	}

	public void setDepotCode(String depotCode){
		this.depotCode = depotCode;
		this.setModified();
	}

	public void setPasswordRequestId(String s) {
		this.passwordRequestId = s;
		this.setModified();
	}

	public String getPasswordRequestId() {
		return this.passwordRequestId;
	}

	public void setPasswordRequestExpiration(java.util.Date d) {
		this.passwordRequestExpiration = d;
		this.setModified();
	}

	public java.util.Date getPasswordRequestExpiration() {
		return this.passwordRequestExpiration;
	}

	public void setPasswordRequestAttempts(int i) {
		this.passwordRequestAttempts = i;
		this.setModified();
	}

	public int getPasswordRequestAttempts() {
		return this.passwordRequestAttempts;
	}

	public int incrementPasswordRequestAttempts() {
		this.passwordRequestAttempts++;
		this.setModified();
		return this.passwordRequestAttempts;
	}

	public String getPasswordHint() {
		return this.passwordHint;
	}

	public void setPasswordHint(String s) {
		this.passwordHint = s;
		this.setModified();
	}

	public void updatePasswordHint(String s) {
		this.setPasswordHint(s);
	}

	public String generatePasswordRequest(java.util.Date expiration) {
		try {
			//
			// Generate random number
			//
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			long randomLong = random.nextLong();
			this.passwordRequestId = Long.toString(randomLong, 36);
			this.passwordRequestExpiration = expiration;
			this.passwordRequestAttempts = 0;
			this.setModified();

			LOGGER.debug("generated long (" + this.passwordRequestId + ") expiring: " + expiration.toString());
			return this.passwordRequestId;

		} catch(NoSuchAlgorithmException ex) {
			LOGGER.warn("Invalid algorithm specified for random number generator.", ex);
			throw new EJBException(ex.getMessage());
		}
	}


	public void erasePasswordRequest() {
		this.passwordRequestId = null;
		this.passwordRequestExpiration = null;
		this.passwordRequestAttempts = 0;
		this.setModified();
	}

	@Override
    public int incrementPymtVerifyAttempts() {
		this.pymtVerifyAttempts++;
		this.setModified();
		return this.pymtVerifyAttempts;
	}

	/**
	 * @param pymtVerifyAttempts the pymtVerifyAttempts to set
	 */
	@Override
    public void setPymtVerifyAttempts(int pymtVerifyAttempts) {
		this.pymtVerifyAttempts = pymtVerifyAttempts;
	}

	/**
	 * @return the pymtVerifyAttempts
	 */
	@Override
    public int getPymtVerifyAttempts() {
		return pymtVerifyAttempts;
	}

	@Override
    public void resetPymtVerifyAttempts() {

		this.pymtVerifyAttempts=0;
		this.setModified();
	}

	/**
	 * @return the rafClickId
	 */
//	public String getRafClickId() {
//		return rafClickId;
//	}
//
//	/**
//	 * @param rafClickId the rafClickId to set
//	 */
//	public void setRafClickId(String rafClickId) {
//		this.rafClickId = rafClickId;
//	}
//
//	/**
//	 * @return the rafPromoCode
//	 */
//	public String getRafPromoCode() {
//		return rafPromoCode;
//	}
//
//	/**
//	 * @param rafPromoCode the rafPromoCode to set
//	 */
//	public void setRafPromoCode(String rafPromoCode) {
//		this.rafPromoCode = rafPromoCode;
//	}

	public EnumPaymentMethodDefaultType getDefaultPaymentMethodType() {
		return defaultPaymentMethodType;
	}

	public void setDefaultPaymentMethodType(EnumPaymentMethodDefaultType defaultPaymentMethodType) {
		this.defaultPaymentMethodType = defaultPaymentMethodType;
		this.setModified();
	}

	public boolean getDpFreeTrialOptin() {
		return dpFreeTrailOptin;
	}

	public void setDpFreeTrailOptin(boolean dpFreeTrailOptin) {
		this.dpFreeTrailOptin = dpFreeTrailOptin;
	}
}
