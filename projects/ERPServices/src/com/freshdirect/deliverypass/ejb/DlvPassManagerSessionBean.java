package com.freshdirect.deliverypass.ejb;

/**
 * 
 * @author skrishnasamy
 * @version 1.0
 */
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.deliverypass.DeliveryPassException;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;


public class DlvPassManagerSessionBean extends SessionBeanSupport {

	private static final long	serialVersionUID	= -8374322338305804910L;

	private static final Category LOGGER = LoggerFactory.getInstance(DlvPassManagerSessionBean.class);
	
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	/** Creates new DlvManagerSessionBean */
	public DlvPassManagerSessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 * 
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.deliverypass.ejb.DlvPassManagerHome";
	}

	/**
	 * This method creates a new delivery pass in the system for the specified
	 * customer.
	 * 
	 * @param model
	 * @return
	 */
	public String create(DeliveryPassModel model) throws DeliveryPassException{
		Connection conn = null;
		PrimaryKey pk = null;
		try {
			/*
			 * Make sure there is no active/pending/ready to use delivery pass more than the permissable limit..
			 * 
			 */
			Map<Comparable, Serializable> statusMap = getAllStatusMap(model.getCustomerId());
			if(statusMap != null && statusMap.size() > 0){
				if(Integer.parseInt(statusMap.get("UsablePassCount").toString()) >=3){//make it read from property file.
					//HAs a pending delivery pass in the system.
					throw new DeliveryPassException("We're sorry. The order cannot be submitted since this account has reached the DeliveryPass limit.",model.getCustomerId());
				}
			}
			
			if(model.getType().isAutoRenewDP()) {
				ErpCustomerEB eb = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(model.getCustomerId()));
				ErpCustomerInfoModel info = eb.getCustomerInfo();
				info.setHasAutoRenewDP("Y");
				info.setAutoRenewDPSKU(model.getType().getAutoRenewalSKU());
				eb.setCustomerInfo(info);
			}
			conn = getConnection();
			pk = DeliveryPassDAO.create(conn, model);

		} catch (SQLException e) {
			throw new EJBException(e);
		} catch (DeliveryPassException de) {
			this.getSessionContext().setRollbackOnly();
			throw de;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return pk.getId();
	}


	/**
	 * This method applies a delivery to the specified delivery pass.
	 * @param dlvPassInfo
	 */
	public void apply(DeliveryPassModel dlvPassInfo) throws DeliveryPassException {
		Connection conn = null;
		try {
			conn = getConnection();
			if (dlvPassInfo.getType().isUnlimited()) {
				applyUnlimitedPass(conn, dlvPassInfo);
			} else {
				applyBSGSPass(conn, dlvPassInfo);
			}
		} catch (SQLException e) {
			LOGGER.warn("SQLException while applying the delivery pass.", e);
			throw new EJBException(e);
		} catch (DeliveryPassException dexp) {
			this.getSessionContext().setRollbackOnly();
			throw dexp;
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while applying the delivery pass.", exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}


	/**
	 * This method applies an unlimited pass.
	 * 
	 * @param customerPk
	 * @param conn
	 * @param dlvPassInfo
	 * @throws SQLException
	 * @throws DeliveryPassException
	 */
	private void applyUnlimitedPass(Connection conn,
			DeliveryPassModel dlvPassInfo) throws SQLException,
			DeliveryPassException {
		// Its a Unlimited pass.
		Date expDate = dlvPassInfo.getExpirationDate();
		Date today = new Date();
		if (today.after(expDate)) {
			// There are no remaining days to apply.
			throw new DeliveryPassException(
					"This customer's Unlimited DeliveryPass has expired.",
					dlvPassInfo.getCustomerId());
		}

		//Increment the usage count.
		int usageCnt = dlvPassInfo.getUsageCount();
		usageCnt = usageCnt + 1;
		dlvPassInfo.setUsageCount(usageCnt);
		DeliveryPassDAO.update(conn, dlvPassInfo,false);
	}

	/**
	 * This method applies a BSGS pass.
	 * 
	 * @param customerPk
	 * @param conn
	 * @param dlvPassInfo
	 * @throws DeliveryPassException
	 * @throws SQLException
	 */
	private void applyBSGSPass(Connection conn,
			DeliveryPassModel dlvPassInfo) throws DeliveryPassException,
			SQLException {
		// Its a BSGS pass.
		int remDeliveries = dlvPassInfo.getRemainingDlvs();
		if (remDeliveries <= 0) {
			// There are no remaining deliveries to apply.
			throw new DeliveryPassException(
					"This customer's BSGS pass does not have enough deliveries.",
					dlvPassInfo.getCustomerId());
		}
		// Else apply the pass. Update the new information to the database.
		remDeliveries = remDeliveries - 1;
		dlvPassInfo.setRemainingDlvs(remDeliveries);
		//Update the usage Count.
		int usageCnt = dlvPassInfo.getUsageCount();
		usageCnt = usageCnt + 1;
		dlvPassInfo.setUsageCount(usageCnt);
		
		if (remDeliveries == 0) {
			// There are no more deliveries left. So set the status to expired pending.
			//The status then changed to expired once the order is delivery confirmed.
			dlvPassInfo.setStatus(EnumDlvPassStatus.EXPIRED_PENDING);
		}
		DeliveryPassDAO.update(conn, dlvPassInfo,false);
	}

	/**
	 * This method applies a new delivery pass model to the order in which it was 
	 * purchased.
	 * 
	 * @param DeliveryPassModel
	 */
	public void applyNew(DeliveryPassModel model) {
		Connection conn = null;
		boolean setOrigExpDate=false;
		try {
			conn = getConnection();
			//Update the usage count to 1.
			model.setUsageCount(1);
			if (!(model.getType().isUnlimited())) {
				//BSGS Pass. Decrement the remaining deliveries by one.
				int remDeliveries = model.getRemainingDlvs();
				remDeliveries = remDeliveries - 1;
				model.setRemainingDlvs(remDeliveries);
			}
			else {
				Date expiryDate=getExpirationDate(model.getType());
				model.setOrgExpirationDate(expiryDate);
				model.setExpirationDate(expiryDate);
				setOrigExpDate=true;
			}
			DeliveryPassDAO.update(conn, model,setOrigExpDate);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while applying the new delivery pass.", e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while applying the new delivery pass.", exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}
	
	public void revoke(DeliveryPassModel appliedPass, DeliveryPassModel activePass) {
		
		if(activePass==null) {
			revoke(appliedPass);
			return;
		}
		boolean isUnlimitedPassApplied=appliedPass.getType().isUnlimited();
		if( !isUnlimitedPassApplied && 
			!(appliedPass.getPK().getId().equals(activePass.getPK().getId())) ) {
			
			activePass.setStatus(EnumDlvPassStatus.READY_TO_USE);
			update(activePass);
			
		}
		revoke(appliedPass);
	}
	
	/**
	 * This method revokes the used delivery pass.
	 * @param customerPk
	 * @throws DeliveryPassException
	 */
	public void revoke(DeliveryPassModel appliedPass) {
		Connection conn = null;
		try {
			conn = getConnection();
			/*
			 * Lookup the active delivery pass linked with the customer's
			 * account. Returns a list containing one item.
			 */
			if (!(appliedPass.getType().isUnlimited())) {
				//BSGS Pass. Increment the remaining deliveries count if
				//the pass is active. TODO If the pass is not Active.
				int remDlvs = appliedPass.getRemainingDlvs();
				remDlvs = remDlvs + 1;
				appliedPass.setRemainingDlvs(remDlvs);
/*				This is the case when a BSGS pass is already expired and
 * 				when the last delivery is revoked, you put it back to
 * 				Active state.
 */
				if (remDlvs == 1 && EnumDlvPassStatus.EXPIRED_PENDING.equals(appliedPass.getStatus())) {
					// The pass has one delivery left. So set the status to Active.
					appliedPass.setStatus(EnumDlvPassStatus.ACTIVE);
				}
			}
			
			//Decrement usage count.
			int usageCnt = appliedPass.getUsageCount();
			usageCnt = usageCnt - 1;
			appliedPass.setUsageCount(usageCnt);

			DeliveryPassDAO.update(conn, appliedPass,false);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while revoking the delivery pass.", e);
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}
	
	/**
	 * This method updates the existing pending delivery pass linked with a
	 * customer's account to a new delivery pass that the customer purchased
	 * during Modify order.
	 * 
	 * @param purchaseOrderId -
	 *            This parameter will be required when the system allows the
	 *            customer to buy more than one delivery pass at any given time.
	 * @param newPass
	 * @throws DeliveryPassException
	 */
	public String modify(String purchaseOrderId,
			DeliveryPassModel newPass) throws DeliveryPassException {
		Connection conn = null;
		PrimaryKey pk = null;
		try {
			conn = getConnection();
			/*
			 * Lookup the pending delivery pass linked with the customer's
			 * account. Note: Only pending Delivery pass can be modified.
			 * Returns a list containing one item.
			 */
			List<DeliveryPassModel> deliveryPasses = DeliveryPassDAO.getDeliveryPassesByOrderId(conn, purchaseOrderId);
					
			if (deliveryPasses == null || deliveryPasses.size() == 0) {
				throw new DeliveryPassException(
						"There is no DeliveryPass found for this purchase order id.",
						purchaseOrderId);
			}
			DeliveryPassModel dlvPassInfo = deliveryPasses
					.get(0);
			// Remove the existing delivery pass from the system.
			DeliveryPassDAO.remove(conn, dlvPassInfo.getPK());
			if(dlvPassInfo.getType().isAutoRenewDP()) {
				ErpCustomerEB eb = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(dlvPassInfo.getCustomerId()));
				ErpCustomerInfoModel info = eb.getCustomerInfo();
				info.setHasAutoRenewDP(null);
				info.setAutoRenewDPSKU(null);
				eb.setCustomerInfo(info);
			}			
			
			// Create the new delivery pass in the system.
			pk = DeliveryPassDAO.create(conn, newPass);
			if(newPass.getType().isAutoRenewDP()) {
				ErpCustomerEB eb = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(dlvPassInfo.getCustomerId()));
				ErpCustomerInfoModel info = eb.getCustomerInfo();
				info.setHasAutoRenewDP("Y");
				info.setAutoRenewDPSKU(newPass.getType().getAutoRenewalSKU());
				eb.setCustomerInfo(info);
			}			

		} catch (SQLException e) {
			LOGGER.warn("SQLException while modifying the delivery pass.", e);
			throw new EJBException(e);
		} catch (DeliveryPassException de) {
			this.getSessionContext().setRollbackOnly();
			throw de;
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return pk.getId();
	}


	/**
	 * This method removes an existing delivery pass from the system
	 * when user modifies an order by removing a delivery pass.
	 * 
	 * @param DeliveryPassModel
	 * @throws DeliveryPassException
	 */
	public void remove(DeliveryPassModel model) {
		Connection conn = null;
		try {
			conn = getConnection();
			// Remove the existing delivery pass from the system.
			DeliveryPassDAO.remove(conn, model.getPK());
			ErpCustomerEB eb = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(model.getCustomerId()));
			ErpCustomerInfoModel info = eb.getCustomerInfo();
			if(model.getType().isAutoRenewDP()) {
				info.setHasAutoRenewDP(null);
				info.setAutoRenewDPSKU(null);
				eb.setCustomerInfo(info);
			}			
		} catch (SQLException e) {
			LOGGER.warn("SQLException while removing the delivery pass.", e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while removing the delivery pass.", exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}
	
	/**
	 * This method cancels an existing active delivery pass from the system on
	 * CSR request.
	 * 
	 * @param deliveryPassId
	 */
	public void cancel(DeliveryPassModel dlvPassModel) {
		Connection conn = null;
		try {
			conn = getConnection();
			// Update the delivery pass status to Cancelled/Order Cancelled.
			DeliveryPassDAO.update(conn, dlvPassModel,false);
			if(dlvPassModel.getType().isAutoRenewDP()) {
				
				ErpCustomerEB eb = this.getErpCustomerHome().findByPrimaryKey(new PrimaryKey(dlvPassModel.getCustomerId()));
				ErpCustomerInfoModel info = eb.getCustomerInfo();
				info.setHasAutoRenewDP(null);
				info.setAutoRenewDPSKU(null);
				eb.setCustomerInfo(info);
			}
			
		} catch (SQLException e) {
			LOGGER.warn("SQLException while cancelling the delivery pass.", e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while cancelling the delivery pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	/**
	 * This method reactivates a cancelled delivery pass from the system on CSR
	 * request.
	 * 
	 * @param deliveryPassId
	 */
	public void reactivate(DeliveryPassModel dlvPassModel) {
		Connection conn = null;
		try {
			conn = getConnection();
			dlvPassModel.setStatus(EnumDlvPassStatus.ACTIVE);
			// Update the delivery pass status to Active.
			DeliveryPassDAO.update(conn, dlvPassModel,false);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while reactivating the delivery pass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while reactivating the delivery pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	/**
	 * This method reactivates a cancelled delivery pass from the system on CSR
	 * request.
	 * 
	 * @param deliveryPassId
	 */
	private void update(DeliveryPassModel dlvPassModel) {
		Connection conn = null;
		try {
			conn = getConnection();
			// Update the delivery pass status to Active.
			DeliveryPassDAO.update(conn, dlvPassModel,false);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while reactivating the delivery pass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while reactivating the DeliveryPass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	/**
	 * This method increments the delivery count of a active BSGS pass on CSR
	 * request.
	 * 
	 * @param deliveryPassId
	 * @param delta -
	 *            the incremental value
	 */
	public void creditDelivery(DeliveryPassModel dlvPassModel, int delta) {
		Connection conn = null;
		try {
			conn = getConnection();
			int remNoOfDlvs = dlvPassModel.getRemainingDlvs();
			int numOfCredits = dlvPassModel.getNoOfCredits();
			remNoOfDlvs = remNoOfDlvs + delta;
			numOfCredits = numOfCredits + delta;
			dlvPassModel.setRemainingDlvs(remNoOfDlvs);
			dlvPassModel.setNoOfCredits(numOfCredits);
			
			/*	This is the case when a BSGS pass is already expired and
			 *	when the last delivery is credited back due to a return, you put it back to
			 *	Active state.
			 */
			if (remNoOfDlvs == 1 && EnumDlvPassStatus.EXPIRED_PENDING.equals(dlvPassModel.getStatus())) {
				// The pass has one delivery left. So set the status to Active.
				dlvPassModel.setStatus(EnumDlvPassStatus.ACTIVE);
			}			
			// Update the delivery pass remNoOfDlvs and num of credits to the db.
			DeliveryPassDAO.update(conn, dlvPassModel,false);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while incrementing the delivery count for BSGS pass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while incrementing the delivery count for BSGS pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}

	}

	/**
	 * This method increments the expiration period of the unlimited pass by x
	 * no.of days on CSR request.
	 * 
	 * @param deliveryPassId
	 * @param noOfdays
	 */
	public void extendExpirationPeriod(DeliveryPassModel dlvPassModel, int noOfdays) {
		
		if(!dlvPassModel.getType().isUnlimited())
			return;

		Connection conn = null;
		boolean setOrigExpDate=false;
		try {
			
			conn = getConnection();
			Date expDate = dlvPassModel.getExpirationDate();
			Calendar cal = Calendar.getInstance();
			if(expDate!=null) {
				cal.setTime(expDate);
			}
			else {
			    cal.setTime(getExpirationDate(dlvPassModel.getType()));
			}
			cal.set(Calendar.HOUR, 11);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.AM_PM, Calendar.PM);
			if(dlvPassModel.getOrgExpirationDate()==null) {
				dlvPassModel.setOrgExpirationDate(cal.getTime());
				setOrigExpDate=true;
			}
			cal.add(Calendar.DATE, noOfdays);
			dlvPassModel.setExpirationDate(cal.getTime());
			// Update the new expiration period to the db.
			DeliveryPassDAO.update(conn, dlvPassModel,setOrigExpDate);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while incrementing the expiration period for unlimited pass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while incrementing the expiration period for unlimited pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	/**
	 * This method returns a list of one or more delivery passes linked to this
	 * customer account.
	 * 
	 * @param customerPk
	 * @return List - returns null if no delivery passes available for this
	 *         customer.
	 */
	public List<DeliveryPassModel> getDeliveryPasses(String customerPk) {
		Connection conn = null;
		List<DeliveryPassModel> deliveryPasses = null;
		try {
			conn = getConnection();
			deliveryPasses = DeliveryPassDAO.getDeliveryPasses(conn, customerPk);
		} catch (SQLException e) {
			LOGGER.warn("SQLException while retreiving the delivery passes.", e);
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn("Unknown error while retreiving the delivery passes.", exp);
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return deliveryPasses;
	}

	/**
	 * This method returns a list of one or more delivery passes linked to this
	 * customer account based on given status.
	 * 
	 * @param customerPk
	 * @return List - returns null if no delivery passes available for this
	 *         customer.
	 */
	public List<DeliveryPassModel> getDlvPassesByStatus(String customerPk, EnumDlvPassStatus status) {
		Connection conn = null;
		List<DeliveryPassModel> deliveryPasses = null;
		try {
			conn = getConnection();
			deliveryPasses = DeliveryPassDAO.getDlvPassesByStatus(conn,	customerPk, status);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while retreiving the delivery passes based on status.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown errorwhile retreiving the delivery passes based on status.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return deliveryPasses;
	}
	/**
	 * This method returns details on a specific delivery pass linked to this
	 * customer account.
	 * 
	 * @param dlvpassId
	 * @return List - returns null if no delivery passes available for this
	 *         customer.
	 */
	public DeliveryPassModel getDeliveryPassInfo(String deliveryPassId) {
		Connection conn = null;
		DeliveryPassModel deliveryPass = null;
		try {
			conn = getConnection();
			PrimaryKey pk = new PrimaryKey(deliveryPassId);
			deliveryPass = DeliveryPassDAO
					.getDeliveryPassInfo(conn, pk);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while retreiving the delivery pass information.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while retreiving the delivery pass information.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return deliveryPass;
	}
	
	/**
	 * This method returns a list of one or more delivery passes linked to this
	 * order Id.
	 * 
	 * @param orderId
	 * @return List - returns null if no delivery passes available for this
	 *         customer.
	 */
	public List<DeliveryPassModel> getDlvPassesByOrderId(String orderId) {
		Connection conn = null;
		List<DeliveryPassModel> deliveryPasses = null;
		try {
			conn = getConnection();
			deliveryPasses = DeliveryPassDAO.getDeliveryPassesByOrderId(conn, orderId);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while retreiving the delivery passes based on status.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while retreiving the delivery passes based on status.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return deliveryPasses;
	}
	
	public Map<Comparable, Serializable> getAllStatusMap(String customerPk){
		Connection conn = null;
		Map<Comparable, Serializable> allStatusMap = new HashMap<Comparable, Serializable>();
		int usablePassCount=0;
		int autoRenewUsablePassCount=0;
		allStatusMap.put(DlvPassConstants.USABLE_PASS_COUNT, "0");
		allStatusMap.put(DlvPassConstants.IS_FREE_TRIAL_RESTRICTED, new Boolean(false));
		allStatusMap.put(DlvPassConstants.AUTORENEW_USABLE_PASS_COUNT, "0");
		allStatusMap.put(DlvPassConstants.AUTORENEW_DP_TYPE,null);
		allStatusMap.put(DlvPassConstants.AUTORENEW_DP_PRICE,new Double(0));
		try {
			conn = getConnection();
			List<DeliveryPassModel> dlvPasses = DeliveryPassDAO.getDeliveryPasses(conn, customerPk);
			if(dlvPasses != null && dlvPasses.size() > 0){
				EnumDlvPassStatus dlvPassStatus = null;
				String dlvPassId = "";
				Date expDate = null;
				Date today = null;
				for ( DeliveryPassModel model : dlvPasses ) {
					dlvPassStatus = model.getStatus();
					dlvPassId = model.getPK().getId();
					if(model.getType().isUnlimited()){
						expDate = model.getExpirationDate();
						if(expDate!=null) {
							//Make sure the pass has not expired.
							today = new Date();
							if(today.after(expDate) && EnumDlvPassStatus.ACTIVE.equals(dlvPassStatus)){
								dlvPassStatus = EnumDlvPassStatus.EXPIRED;
							}
							if(model.getType().isFreeTrialRestricted()&& !EnumDlvPassStatus.ORDER_CANCELLED.equals(model.getStatus())) {
								allStatusMap.put(DlvPassConstants.IS_FREE_TRIAL_RESTRICTED, new Boolean(true));
							}
						}
					}
					if((dlvPassStatus==EnumDlvPassStatus.ACTIVE)||
					   (dlvPassStatus==EnumDlvPassStatus.PENDING)||
					   (dlvPassStatus==EnumDlvPassStatus.READY_TO_USE)
					   ) {
						
						usablePassCount=Integer.parseInt(allStatusMap.get(DlvPassConstants.USABLE_PASS_COUNT).toString());
						usablePassCount++;
						allStatusMap.put(DlvPassConstants.USABLE_PASS_COUNT, String.valueOf(usablePassCount));
						if(model.getType().isAutoRenewDP()) {
							autoRenewUsablePassCount=Integer.parseInt(allStatusMap.get(DlvPassConstants.AUTORENEW_USABLE_PASS_COUNT).toString());
							autoRenewUsablePassCount++;
							allStatusMap.put(DlvPassConstants.AUTORENEW_USABLE_PASS_COUNT, String.valueOf(autoRenewUsablePassCount));
							allStatusMap.put(DlvPassConstants.AUTORENEW_DP_PRICE,new Double(model.getAmount()));
							allStatusMap.put(DlvPassConstants.AUTORENEW_DP_TYPE,model.getType());
						}
						
					}
					Object ObjKey = allStatusMap.get(dlvPassStatus);
					if(ObjKey != null){
						if((dlvPassStatus==EnumDlvPassStatus.READY_TO_USE)||(dlvPassStatus==EnumDlvPassStatus.PENDING)) {
							allStatusMap.put(dlvPassStatus, dlvPassId);
						}
						continue;
					}
					allStatusMap.put(dlvPassStatus, dlvPassId);
				}
			}
			//LOGGER.debug("Status Map Info "+allStatusMap);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while retreiving the delivery pass status counts.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown errorwhile retreiving the delivery pass status counts.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		return allStatusMap;
		
	}
	/**
	 * This method updates the existing delivery pass with the new price.
	 * @param dlvPassModel
	 * @param newPrice
	 */
	public void updatePrice(DeliveryPassModel dlvPassModel, double newPrice){
		Connection conn = null;
		try {
			conn = getConnection();
			// Update the delivery pass price.
			DeliveryPassDAO.updatePrice(conn, dlvPassModel, newPrice);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while updating the new price for DeliveryPass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while updating the new price for Delivery pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		
	}
	public void activateReadyToUsePass(DeliveryPassModel dlvPass) {
		
		if(!isReadyToUse(dlvPass.getStatus()))
			return;
		
		Connection conn = null;
		try {
			conn = getConnection();
			dlvPass.setStatus(EnumDlvPassStatus.ACTIVE);
			if(dlvPass.getType().isUnlimited()&& dlvPass.getOrgExpirationDate()==null) {
				Date expiryDate=getExpirationDate(dlvPass.getType());
				dlvPass.setOrgExpirationDate(expiryDate);
				dlvPass.setExpirationDate(expiryDate);
				
			}
			DeliveryPassDAO.update(conn, dlvPass,true);
		} catch (SQLException e) {
			LOGGER.warn( "SQLException while activating the Ready To Use delivery pass.", e );
			throw new EJBException(e);
		} catch (Exception exp) {
			LOGGER.warn( "Unknown error while activating the Ready To Use delivery pass.", exp );
			throw new EJBException(exp);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
		
	}
	
	public  boolean isReadyToUse(EnumDlvPassStatus dpStatus) {
		if(EnumDlvPassStatus.READY_TO_USE.equals(dpStatus)) {
			return true;
		}
		else {
			return false;
		}
	}
	   public Date getExpirationDate(DeliveryPassType dlvPassType) {
		   
		   if(dlvPassType.isUnlimited()) {
				Calendar cal = Calendar.getInstance(Locale.US);
				//Add duration to today's date to calculate expiration date.
				cal.set(Calendar.HOUR, 11);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.AM_PM, Calendar.PM);
				return DateUtil.addDays(cal.getTime(), dlvPassType.getDuration());
		   }
		   else {
			   return null;
		   }
	   }
	   public boolean hasPurchasedPass(String customerPK) {

			Connection conn = null;
			boolean hasPurchasedPass=false;
			try {
				conn = getConnection();
				hasPurchasedPass = DeliveryPassDAO.hasPurchasedPass(conn, customerPK);
			} catch (Exception e) {
				LOGGER.warn("Exception during hasPurchasedPass() for customer :"+customerPK,e);
				throw new EJBException(e);
			} 
			finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while closing conn in cleanup", e);
				}
			}
			return hasPurchasedPass;

	   }
	   public List<DeliveryPassModel> getUsableAutoRenewPasses(String customerPK ) {
		   
		   List<DeliveryPassModel> autoRenewPasses=null;
			Connection conn = null;
			try {
				conn = getConnection();
				autoRenewPasses = DeliveryPassDAO.getUsableAutoRenewPasses(conn, customerPK);
			} catch (Exception e) {
				LOGGER.warn("Exception during getUsableAutoRenewPasses() for customer :"+customerPK,e);
				throw new EJBException(e);
			} 
			finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while closing conn in cleanup", e);
				}
			}
		   return autoRenewPasses;
	   }
	   public  Object[] getAutoRenewalInfo() {
			Connection conn = null;
			try {
				conn = getConnection();
				return DeliveryPassDAO.getAutoRenewalInfo(conn);
			} catch (Exception e) {
				LOGGER.warn("Exception during getAutoRenewalInfo()",e);
				throw new EJBException(e);
			} 
			finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while closing conn in cleanup", e);
				}
			}

		   
	   }
		
		private ErpCustomerHome getErpCustomerHome() {
			try {
				return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer");
			} catch (NamingException e) {
				throw new EJBException(e);
			}
		}
   

	    public int getDaysSinceDPExpiry(String customerID) {
	    	
			Connection conn = null;
			try {
				conn = getConnection();
				return DeliveryPassDAO.getDaysSinceDPExpiry(conn,customerID);
			} catch (Exception e) {
				LOGGER.warn("Exception during getAutoRenewalInfo()",e);
				throw new EJBException(e);
			} 
			finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while closing conn in cleanup", e);
				}
			}
	    }
	    
	    public int getDaysToDPExpiry(String customerID, String activeDPID) {
	    	
			Connection conn = null;
			try {
				conn = getConnection();
				return DeliveryPassDAO.getDaysToDPExpiry(conn,customerID,activeDPID);
			} catch (Exception e) {
				LOGGER.warn("Exception during getAutoRenewalInfo()",e);
				throw new EJBException(e);
			} 
			finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while closing conn in cleanup", e);
				}
			}
	    }

	   
	   
}
