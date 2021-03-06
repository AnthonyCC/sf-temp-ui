package com.freshdirect.customer.ejb;


import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.customer.ErpCustomerI;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.framework.collection.CollectionException;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGiftCardModel;

/**
 * ErpCustomer entity bean implementation.
 *
 * @version
 * @author     $Author$
 *
 * @ejbHome <{ErpCustomerHome}>
 * @ejbRemote <{ErpCustomerEB}>
 * @ejbPrimaryKey <{PrimaryKey}>
 * @stereotype fd-entity*/
public class ErpCustomerEntityBean extends EntityBeanSupport implements ErpCustomerI {

	private static final long	serialVersionUID	= -8059593847257886394L;

	private static Category LOGGER = LoggerFactory.getInstance( ErpCustomerEntityBean.class );

	private String userId;
	private String passwordHash;
	private String sapId;
	private boolean active;
	private boolean socialLoginOnly;
	
	/**
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpAddressPersistentBean}>
	 */
	private AddressList shipToAddress;

	/**
	 * @link aggregationByValue
	 * @directed
	 * @associates <{ErpPaymentMethodPersistentBean}>
	 */
	private PaymentMethodList paymentMethodList;

	/**
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 */
	private ErpCustomerInfoPersistentBean customerInfo;

	private CustomerCreditList customerCredits;

	private CustomerAlertList customerAlerts;

	/**
	 * Default constructor.
	 */
	public ErpCustomerEntityBean() {
		super();
	}

	/**
	 * Copy into model.
	 *
	 * @return ErpCustomerModel object.
	 */
	public ModelI getModel() {
		ErpCustomerModel model = new ErpCustomerModel();
		super.decorateModel(model);
		model.setUserId(this.userId);
		model.setPasswordHash(this.passwordHash);
		model.setActive(this.active);
		model.setSocialLoginOnly(this.socialLoginOnly);
		model.setSapId(this.sapId);

		// deep copy properties
		model.setCustomerInfo(((ErpCustomerInfoModel)this.customerInfo.getModel()));
		for(Iterator<ErpAddressPersistentBean> i = this.shipToAddress.iterator(); i.hasNext();){
			model.addShipToAddress((ErpAddressModel)((ErpAddressPersistentBean)i.next()).getModel());
		}
		for(Iterator<ErpPaymentMethodPersistentBean> i = this.paymentMethodList.iterator(); i.hasNext();){
			model.addPaymentMethod((ErpPaymentMethodI)((ErpPaymentMethodPersistentBean)i.next()).getModel());
		}
		for(Iterator<ErpCustomerCreditPersistentBean> i = this.customerCredits.iterator(); i.hasNext();){
			model.addCustomerCredit((ErpCustomerCreditModel)((ErpCustomerCreditPersistentBean)i.next()).getModel());
		}
		for(Iterator<ErpCustomerAlertPersistentBean> i = this.customerAlerts.iterator(); i.hasNext();){
			model.addCustomerAlert((ErpCustomerAlertModel)((ErpCustomerAlertPersistentBean)i.next()).getModel());
		}
		return model;
	}

	/**
	 * Copy from model.
	 */
	public void setFromModel(ModelI model) {
		// copy properties from model
		ErpCustomerModel m = (ErpCustomerModel)model;
		this.setUserId( m.getUserId() );
		this.passwordHash = m.getPasswordHash();
		this.sapId = m.getSapId();
		this.active = m.isActive();
		this.socialLoginOnly = m.isSocialLoginOnly();
		this.customerInfo.setFromModel(m.getCustomerInfo());
		this.setShipToAddressFromModel(m.getShipToAddresses());
		this.setPaymentMethodFromModel(m.getPaymentMethods());
		this.setCustomerCreditFromModel(m.getCustomerCredits());
		this.setCustomerAlertFromModel(m.getCustomerAlerts());

		this.setModified();
	}

	public void initialize(){
		this.userId = "";
		this.passwordHash = "";
		this.sapId = "";
		this.active = false;
		this.socialLoginOnly = false;
		this.customerInfo = new ErpCustomerInfoPersistentBean();
		this.shipToAddress = new AddressList();
		this.paymentMethodList = new PaymentMethodList();
		this.customerCredits = new CustomerCreditList();
		this.customerAlerts = new CustomerAlertList();
	}

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    protected String getResourceCacheKey() {
        return "com.freshdirect.customer.ejb.ErpCustomerHome";
    }

	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID FROM CUST.CUSTOMER where ID=?");
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find ErpCustomer with PK " + pk);
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
    
	public PrimaryKey ejbCreate(ModelI model) throws CreateException, DuplicateKeyException {
        Connection conn = null;
        try {
			conn = getConnection();
			//if(isDuplicate(model, conn)){
			//	throw new DuplicateKeyException("The user id already exists");
			//}
        	initialize();
            setFromModel(model);

            PrimaryKey pk = create(conn);
            conn.close();
            return pk;
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbCreate(model)111, setting rollbackOnly, throwing CreateException", sqle);
            if (sqle.getMessage().toLowerCase().indexOf("unique") > -1) {
                throw new DuplicateKeyException("The user id already exists");
            }
            getEntityContext().setRollbackOnly();
            throw new CreateException("SQLException in ejbCreate(ModelI) "+sqle.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("unable to close connection in ejbCreate(ModelI)", sqle2);
                }
            }
        }
    }
    
	public PrimaryKey create(Connection conn) throws SQLException{

		this.setPK(new PrimaryKey(this.getNextId(conn, "CUST")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.CUSTOMER (ID, USER_ID, SAP_ID, ACTIVE, PASSWORDHASH,SOCIAL_LOGIN_ONLY) values (?,LOWER(?),?,?,?,?)");
		ps.setString(1, this.getPK().getId());
		ps.setString(2, this.getUserId());
		ps.setString(3, this.getSapId());
		ps.setString(4, (this.isActive()? "1" : "0"));
		ps.setString(5, this.passwordHash);
		ps.setString(6, (this.isSocialLoginOnly()? "1" : "0"));
		
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

		// create children
		//creating ship to address list
		this.shipToAddress.setParentPK(this.getPK());
		this.shipToAddress.create( conn );

		//creating payment list
		this.paymentMethodList.setParentPK(this.getPK());
		this.paymentMethodList.create(conn);

		//creating customer info
		this.customerInfo.setParentPK(this.getPK());
		this.customerInfo.create( conn );

		//creating customer credits
		this.customerCredits.setParentPK(this.getPK());
		this.customerCredits.create( conn );

		//creating customer alerts
		this.customerAlerts.setParentPK(this.getPK());
		this.customerAlerts.create( conn );

		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;	
		try {
			ps = conn.prepareStatement("SELECT USER_ID, PASSWORDHASH, SAP_ID, ACTIVE, SOCIAL_LOGIN_ONLY FROM CUST.CUSTOMER WHERE ID = ?");
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (!rs.next()) {
				throw new SQLException("No such ErpCustomer PK: " + this.getPK());
			}
			this.setUserId(rs.getString("USER_ID"));
			this.setPasswordHash(rs.getString("PASSWORDHASH"));
			this.setSapId(rs.getString("SAP_ID"));
			this.setActive(("1".equals(rs.getString("ACTIVE"))? true : false));
			this.setSocialLoginOnly(("1".equals(rs.getString("SOCIAL_LOGIN_ONLY"))? true : false));

//			rs.close();
//			rs = null;
//			ps.close();
//			ps = null;
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (Exception e) {
					
				}
			}
			
			if(null != ps){
				try {
					ps.close();
				} catch (Exception e) {
					
				}
			}
		}

		// load children
		//loading ship to addresses
		this.shipToAddress.setParentPK(this.getPK());
		this.shipToAddress.load(conn);

		//loading credit card list
		this.paymentMethodList.setParentPK(this.getPK());
		this.paymentMethodList.load(conn);

		//load customer info
		this.customerInfo.setParentPK(this.getPK());
		this.customerInfo.load(conn);

		//load customer credits
		this.customerCredits.setParentPK(this.getPK());
		this.customerCredits.load( conn );

		//load customer alerts
		this.customerAlerts.setParentPK(this.getPK());
		this.customerAlerts.load( conn );
		
	}

	public void store(Connection conn) throws SQLException {
		if (super.isModified()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CUSTOMER SET USER_ID = LOWER(?),  SAP_ID = ?, ACTIVE = ?, PASSWORDHASH = ?, SOCIAL_LOGIN_ONLY = ?  WHERE ID=?");
			ps.setString(1, this.getUserId());
			ps.setString(2, this.getSapId());
			ps.setString(3, (this.isActive() ? "1" : "0"));
			ps.setString(4, this.passwordHash);
			ps.setString(5, (this.isSocialLoginOnly() ? "1" : "0"));
			ps.setString(6, this.getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
			ps.close();
			ps = null;
		}

		//APPDEV 5692 updated email address is not being stored in cust.customerinfo table
		if (null!=this.userId && !this.userId.equalsIgnoreCase(customerInfo.getEmail())){
			customerInfo.setEmail(this.userId);
			this.customerInfo.store(conn);
		}
	
		// store children
		if (this.shipToAddress.isModified()) {
			this.shipToAddress.store( conn );
			
			// update names for ExpressSignup Customer
			if(shipToAddress.size() >0 && !isCustomerNameUpdatedForExpressSignupCustomer()){								
				for(Iterator<ErpAddressPersistentBean> i = this.shipToAddress.iterator(); i.hasNext();){
					ErpAddressModel address = (ErpAddressModel)((ErpAddressPersistentBean)i.next()).getModel();
					if(address != null && address.getFirstName() != null && address.getLastName() != null){
						if(!(address.getFirstName().equalsIgnoreCase(address.getLastName()))){
							this.customerInfo.updateCustomerNames( conn, address.getFirstName(), null, address.getLastName() );	
						}
					}					
				}															
			}
		}
		LOGGER.info("Before calling store ******************* ");
		if(this.paymentMethodList.isModified()){
			LOGGER.info("Inside store ******************* ");
			this.paymentMethodList.store(conn);
		}
		LOGGER.info("After calling store ******************* ");
		if (this.customerInfo.isModified()) {
			this.customerInfo.store( conn );
		}
		if (this.customerCredits.isModified()){
			this.customerCredits.store( conn );
		}
		if (this.customerAlerts.isModified()){
			this.customerAlerts.store( conn );
		}
	}

	public void remove(Connection conn) throws SQLException {
		// remove children
		this.shipToAddress.remove(conn);
		this.paymentMethodList.remove(conn);
		this.customerInfo.remove(conn);
		this.customerCredits.remove(conn);
		this.customerAlerts.remove(conn);

		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CUSTOMER WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
		ps = null;
	}


	/**
	 * Overriden isModified.
	 */
	public boolean isModified() {
		// !!! check children, eg:
		return super.isModified() || this.shipToAddress.isModified() || customerInfo.isModified() || this.paymentMethodList.isModified() || this.customerCredits.isModified()|| this.customerAlerts.isModified();
	}

	/**
	 * Get ShipToAddresss.
	 *
	 * @return collection of ShipToAddress model objects*/
	public List<ErpAddressModel> getShipToAddresses() {
		// copy into models
		return this.shipToAddress.getModelList();
	}

	public String getUserId(){
		return this.userId;
	}
	public void setUserId(String userId){
		this.userId = userId.toLowerCase();
		this.setModified();
	}

	public void updateUserId(String userId) throws ErpDuplicateUserIdException {
		Connection conn = null;
		try {
			conn = getConnection();
			if(isDuplicate(userId, conn)){
				this.getEntityContext().setRollbackOnly();
				throw new ErpDuplicateUserIdException("The user id already exists");
			}
			this.setUserId(userId);
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in updateUserId(model), while checking for duplicate UserId", sqle);
            throw new EJBException("SQLException in updateUserId(model) "+sqle.getMessage());
        } finally{
        	if (conn!=null) {
        		try {
					conn.close();
				} catch (SQLException e) {
					throw new EJBException(e);
				}
        	}
        }
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
		this.setModified();
	}

	public String getSapId(){
		return this.sapId;
	}

	public void setSapId(String sapId){
		this.sapId = sapId;
		this.setModified();
	}

	/**
	 * Add a new ShipToAddress.
	 *
	 * @param element ShipToAddress model object
	 */
	public void addShipToAddress(ErpAddressModel element) {
		ErpAddressPersistentBean bean = new ErpAddressPersistentBean(element);
		this.shipToAddress.add(bean);
	}

	/**
	 * Update an existing ShipToAddress, based on PK.
	 *
	 * @param element ShipToAddress model object, with PK
	 *
	 * @throws CollectionException if the PK was not found.
	 */
	public void updateShipToAddress(ErpAddressModel element) {
		this.shipToAddress.update(new ErpAddressPersistentBean(element));
	}

	/**
	 * Remove an existing ShipToAddress by PK.
	 *
	 * @param pk ShipToAddress PK to remove
	 *
	 * @return false if not found
	 */
	public boolean removeShipToAddress(PrimaryKey pk) {
		return null != this.shipToAddress.removeByPK(pk);
	}

	public List<ErpCustomerCreditModel> getCustomerCredits() {
		// copy into models
		return this.customerCredits.getModelList();
	}

	public void addCustomerCredit(ErpCustomerCreditModel element) {
		ErpCustomerCreditPersistentBean bean = new ErpCustomerCreditPersistentBean(element);
		this.customerCredits.add(bean);
	}

	public void updateCustomerCredit(ErpCustomerCreditModel element) {
		this.customerCredits.update(new ErpCustomerCreditPersistentBean(element));
	}
	
	public void removeCustomerCreditByComplaintId(String complaintId) throws ErpTransactionException {
		
		ErpCustomerCreditModel creditModel = null;
		List<ErpCustomerCreditModel> complaintCredits = new ArrayList<ErpCustomerCreditModel>();
		
		for(Iterator<ErpCustomerCreditPersistentBean> i = this.customerCredits.iterator(); i.hasNext(); ){
			ErpCustomerCreditPersistentBean bean = (ErpCustomerCreditPersistentBean)i.next();
			creditModel = (ErpCustomerCreditModel)bean.getModel();
			if(creditModel.getComplaintPk().getId().equals(complaintId)){
				complaintCredits.add(creditModel);
			}
		}
		if(complaintCredits.size() <= 0){
			this.getEntityContext().setRollbackOnly();
			throw new ErpTransactionException("no credit found for complaintId: "+complaintId);
		}
		//make sure that none of the credits have been utilized yet
		for(Iterator<ErpCustomerCreditModel> i = complaintCredits.iterator(); i.hasNext(); ){
			creditModel = (ErpCustomerCreditModel)i.next();	
			if(MathUtil.roundDecimal(creditModel.getRemainingAmount()) != MathUtil.roundDecimal(creditModel.getOriginalAmount())){
				this.getEntityContext().setRollbackOnly();
				throw new ErpTransactionException("Credit has already being used so cannot reverse it");
			}
		}
		//now finally remove credits for given complaint
		for(Iterator<ErpCustomerCreditModel> i = complaintCredits.iterator(); i.hasNext(); ){
			creditModel = (ErpCustomerCreditModel)i.next();
			this.customerCredits.removeByPK(creditModel.getPK());
		}
		
	}

	public boolean removeCustomerCredit(PrimaryKey pk) {
		return null != this.customerCredits.removeByPK(pk);
	}

	public ErpCustomerInfoModel getCustomerInfo() {
		return (ErpCustomerInfoModel)this.customerInfo.getModel();
	}

	public void setCustomerInfo(ErpCustomerInfoModel customerInfo) {
		this.customerInfo.setFromModel(customerInfo);
		this.setModified();
	}

	protected void setShipToAddressFromModel(Collection<ErpAddressModel> collection) {
		// create persistent bean collection
		List<ErpAddressPersistentBean> persistentBeans = new LinkedList<ErpAddressPersistentBean>();
		for ( ErpAddressModel model : collection ) {
			persistentBeans.add( new ErpAddressPersistentBean(model) );
		}
		// set it
		this.shipToAddress.set(persistentBeans);
	}

	protected void setCustomerCreditFromModel(Collection<ErpCustomerCreditModel> collection) {
		// create persistent bean collection
		List<ErpCustomerCreditPersistentBean> persistentBeans = new LinkedList<ErpCustomerCreditPersistentBean>();
		for ( ErpCustomerCreditModel model : collection ) {
			persistentBeans.add( new ErpCustomerCreditPersistentBean(model) );
		}
		// set it
		this.customerCredits.set(persistentBeans);
	}

	public PrimaryKey ejbFindByUserId(String userId) throws FinderException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			con = this.getConnection();
			ps = con.prepareStatement("SELECT ID FROM CUST.CUSTOMER WHERE USER_ID = LOWER(?)");

			ps.setString(1, userId.toLowerCase());

			rs = ps.executeQuery();

			if(!rs.next()){
				throw new ObjectNotFoundException("No account with user ID: "+userId);
			}
			return new PrimaryKey(rs.getString("ID"));
		}catch(SQLException se){
			throw new FinderException(se.getMessage());
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch(SQLException ex) {
				//eat it for the time being
			}
		}
	}

	public PrimaryKey ejbFindByUserIdAndPasswordHash(String userId, String passwordHash) throws FinderException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			con = this.getConnection();
			ps = con.prepareStatement("SELECT ID FROM CUST.CUSTOMER WHERE USER_ID = LOWER(?) and PASSWORDHASH = ? ");

			ps.setString(1, userId.toLowerCase());
			ps.setString(2, passwordHash);

			rs = ps.executeQuery();

			if(!rs.next()){
                LOGGER.debug("no login record found for " + userId + " : " + passwordHash);
				throw new ObjectNotFoundException("No account with that username/password.");
			}
			return new PrimaryKey(rs.getString("ID"));
		}catch(SQLException se){
			throw new FinderException(se.getMessage());
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch(SQLException ex) {
				//eat it for the time being
			}
		}
	}
	

	
	private boolean isDuplicate(String userId, Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CUSTOMER WHERE USER_ID = LOWER(?) AND ID <> ?");
		ps.setString(1, userId);
        ps.setString(2, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		boolean found = rs.next();
		rs.close();
		ps.close();
		return found;
	}

	
	private boolean isCustomerNameUpdatedForExpressSignupCustomer(){
		
		String fName = this.customerInfo.getFirstName();
		String lName = this.customerInfo.getLastName();
		String email = this.customerInfo.getEmail();
		
		if(fName != null && lName != null && email != null ){			
			if(fName.equalsIgnoreCase(lName) &&  fName.equalsIgnoreCase(email.substring(0, email.indexOf("@")))  ){				
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
		this.setModified();
	}

	public boolean isSocialLoginOnly() {
		return socialLoginOnly;
	}

	public void setSocialLoginOnly(boolean socialLoginOnly) {
		this.socialLoginOnly = socialLoginOnly;
		this.setModified();
	}		
	
	
	public void updatePaymentMethod(ErpPaymentMethodI element) throws RemoteException {
		this.paymentMethodList.update(new ErpPaymentMethodPersistentBean((ErpPaymentMethodModel)element));			
	}

	public boolean removePaymentMethod(PrimaryKey pk) throws RemoteException {
		return null != this.paymentMethodList.removeByPK(pk);
	}

	public List<ErpPaymentMethodI> getPaymentMethods() throws RemoteException {
		List<ErpPaymentMethodI> pmList = this.paymentMethodList.getModelList();
		for( Iterator<ErpPaymentMethodI> it = pmList.iterator(); it.hasNext(); ){
			ErpPaymentMethodI model = it.next();
			if(model instanceof ErpGiftCardModel) {
				//Remove gift cards before sending.
				it.remove();
			}
		}
		return pmList;
	}
	
	public List<ErpGiftCardModel> getGiftCards() throws RemoteException {
		List<ErpPaymentMethodModel> pmList = this.paymentMethodList.getModelList();
		//LOGGER.info("Pm List before contains "+pmList);
		for( Iterator<ErpPaymentMethodModel> it = pmList.iterator(); it.hasNext(); ) {
			ErpPaymentMethodModel model = it.next();
			if( !( model instanceof ErpGiftCardModel ) ) {
				//Remove credit cards and Echecks before sending.
				it.remove();
			}
		}
		//LOGGER.info("Pm List after contains "+pmList);
		return (List<ErpGiftCardModel>)(List)pmList;
	}

	public void addPaymentMethod(ErpPaymentMethodI element) throws RemoteException {
		ErpPaymentMethodPersistentBean bean = new ErpPaymentMethodPersistentBean((ErpPaymentMethodModel)element);
		this.paymentMethodList.add(bean);
	}

	protected void setPaymentMethodFromModel(Collection<ErpPaymentMethodI> collection) {
		// create persistent bean collection
		List<ErpPaymentMethodPersistentBean> persistentBeans = new LinkedList<ErpPaymentMethodPersistentBean>();
		for ( ErpPaymentMethodI model : collection ) {
			persistentBeans.add( new ErpPaymentMethodPersistentBean((ErpPaymentMethodModel)model) );				
		}
		// set it
		this.paymentMethodList.set(persistentBeans);
	}

	public void updateCustomerCredit(String customerCreditId, double delta) {
		boolean foundCustomerCredit = false;
		List<ErpCustomerCreditModel> customerCredits = this.getCustomerCredits();
		for ( ErpCustomerCreditModel customerCredit : customerCredits ) {
			if ( customerCredit.getPK().getId().equals(customerCreditId) ) {
				foundCustomerCredit = true;
				double remainingAmount = customerCredit.getRemainingAmount();
				double originalAmount = customerCredit.getOriginalAmount();
				double updatedRemainingAmount=MathUtil.roundDecimal(remainingAmount + delta);
				if ( ( originalAmount < updatedRemainingAmount ) || ( 0 > updatedRemainingAmount ) ) {
					StringBuilder msg=new StringBuilder();
					msg.append("Credit delta too large; exceeds maximum or minimum boundary conditions.");
					msg.append(" customerCreditId :" +customerCreditId);
					msg.append(" originalAmount :" +originalAmount);
					msg.append(" remainingAmount :" +remainingAmount);
					msg.append(" delta :" +delta);
					msg.append(" updatedRemainingAmount :" +updatedRemainingAmount);
					msg.append(" ( originalAmount < updatedRemainingAmount ):"+( originalAmount < updatedRemainingAmount ));
					msg.append(" ( 0 > updatedRemainingAmount ):"+( 0 > updatedRemainingAmount ));
				    LOGGER.error(msg.toString());
					throw new EJBException("Credit delta too large; exceeds maximum or minimum boundary conditions.");
				} else {
					customerCredit.setRemainingAmount(updatedRemainingAmount);
					this.updateCustomerCredit(customerCredit);
					break;
				}
			}
		}
		if (!foundCustomerCredit) {
			throw new EJBException("Could not find the customer credit to be updated.");
		}
		this.setModified();
	}


	public void updateCustomerAlert(ErpCustomerAlertModel element) {
		this.customerAlerts.update(new ErpCustomerAlertPersistentBean(element));			
	}

	public boolean removeCustomerAlert(PrimaryKey pk) {
		return null != this.customerAlerts.removeByPK(pk);
	}

	public List<ErpCustomerAlertModel> getCustomerAlerts() {
		return this.customerAlerts.getModelList();
	}

	public void addCustomerAlert(ErpCustomerAlertModel element) {
		ErpCustomerAlertPersistentBean bean = new ErpCustomerAlertPersistentBean(element);
		this.customerAlerts.add(bean);
	}

	public boolean isOnAlert() {
		return customerAlerts != null && customerAlerts.size() > 0;
	}

	protected void setCustomerAlertFromModel(Collection<ErpCustomerAlertModel> collection) {
		// create persistent bean collection
		List<ErpCustomerAlertPersistentBean> persistentBeans = new LinkedList<ErpCustomerAlertPersistentBean>();
		for ( ErpCustomerAlertModel model : collection ) {
			persistentBeans.add( new ErpCustomerAlertPersistentBean(model) );
		}
		// set it
		this.customerAlerts.set(persistentBeans);
	}
	
	/**
	 * Inner class for the list of dependent DlvTimeslotTemplate persistent beans.
	 */
	private static class AddressList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set( ErpAddressPersistentBean.findByParent(conn, (PrimaryKey)AddressList.this.getParentPK()) );
		}
	}

	private static class PaymentMethodList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			List list = ErpPaymentMethodPersistentBean.findByParent(conn, (PrimaryKey)PaymentMethodList.this.getParentPK());				
			this.set(list);			
		}
	}

	private static class CustomerCreditList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpCustomerCreditPersistentBean.findByParent(conn, (PrimaryKey)CustomerCreditList.this.getParentPK()));
		}
	}

	private static class CustomerAlertList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpCustomerAlertPersistentBean.findByParent(conn, (PrimaryKey)CustomerAlertList.this.getParentPK()));
		}
	}

	public void updatePaymentMethodNewTx(ErpPaymentMethodI payment) throws RemoteException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			con = this.getConnection();
			updatePaymentMethod(payment);
			this.paymentMethodList.store(con);
		}catch(SQLException se){
			throw new RemoteException(se.getMessage());
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch(SQLException ex) {
				//eat it for the time being
			}
		}	
	}
	
}
