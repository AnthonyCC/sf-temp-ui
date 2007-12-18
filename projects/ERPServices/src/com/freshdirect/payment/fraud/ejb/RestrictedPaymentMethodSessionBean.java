package com.freshdirect.payment.fraud.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.fraud.EnumRestrictedPatternType;
import com.freshdirect.payment.fraud.EnumRestrictedPaymentMethodStatus;
import com.freshdirect.payment.fraud.PaymentFraudFactory;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodCriteria;
import com.freshdirect.payment.fraud.RestrictedPaymentMethodModel;

public class RestrictedPaymentMethodSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(RestrictedPaymentMethodSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.payment.fraud.ejb.RestrictedPaymentMethodHome";
	}

	public PrimaryKey createRestrictedPaymentMethod(RestrictedPaymentMethodModel model) {
		Connection conn = null;
		PrimaryKey pk = null;
		RestrictedPaymentMethodModel existingModel = null;
		
		try {
			conn = getConnection();
			// accountNumber might already exists			
			if (model.getAccountNumber() != null) {
				RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
				if (model.getBankAccountType() != null) {
					criteria.setBankAccountType(model.getBankAccountType());
				}
				if (model.getAbaRouteNumber() != null) {
					criteria.setAbaRouteNumber(model.getAbaRouteNumber());
				}
				criteria.setAccountNumber(model.getAccountNumber());
				List list = findRestrictedPaymentMethods(criteria);
				if (list != null && list.size() > 0) {
					existingModel = (RestrictedPaymentMethodModel) list.get(0);
				}
			}
			// if already exists, just update row instead of creating a new one
			if (existingModel != null) {
				model.setId(existingModel.getId());
				model.setPK(existingModel.getPK());
				model.setStatus(EnumRestrictedPaymentMethodStatus.BAD);
				model.setCreateDate(new Date());
				storeRestrictedPaymentMethod(model);
				pk = existingModel.getPK();
				if (EnumRestrictedPaymentMethodStatus.DELETED.equals(existingModel.getStatus())) {					
					logRestrictedPaymentMethodActivity(existingModel, EnumAccountActivityType.READD_RESTRICTED_PAYMENT_METHOD, model.getSource());
				} else {
					logRestrictedPaymentMethodActivity(existingModel, EnumAccountActivityType.EDIT_RESTRICTED_PAYMENT_METHOD, model.getSource());					
				}
			} else {
				pk = RestrictedPaymentMethodDAO.createRestrictedPaymentMethod(conn, model);									
				logRestrictedPaymentMethodActivity(model, EnumAccountActivityType.ADD_RESTRICTED_PAYMENT_METHOD, model.getSource());
			}
			
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}		
		return pk;
	}

	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPrimaryKey(PrimaryKey pk) {
		Connection conn = null;
		RestrictedPaymentMethodModel model = null;
		try {
			conn = getConnection();
			model = RestrictedPaymentMethodDAO.findRestrictedPaymentMethodByPrimaryKey(conn, pk);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}			
		return model;
	}

	public List findRestrictedPaymentMethodByCustomerId(String customerId, EnumRestrictedPaymentMethodStatus status) {		
		Connection conn = null;
		List list = null;
		try {
			conn = getConnection();
			list = RestrictedPaymentMethodDAO.findRestrictedPaymentMethodByCustomerId(conn, customerId, status);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}	
		return list;
	}

	public RestrictedPaymentMethodModel findRestrictedPaymentMethodByPaymentMethodId(String paymentMethodId, EnumRestrictedPaymentMethodStatus status) {
		Connection conn = null;
		RestrictedPaymentMethodModel model = null;
		try {
			conn = getConnection();
			model = RestrictedPaymentMethodDAO.findRestrictedPaymentMethodByPaymentMethodId(conn, paymentMethodId, status);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}			
		return model;
	}

	public List findRestrictedPaymentMethods(RestrictedPaymentMethodCriteria criteria) {		
		Connection conn = null;
		List list = null;
		try {
			conn = getConnection();
			list = RestrictedPaymentMethodDAO.findRestrictedPaymentMethods(conn, criteria);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}	
		return list;
	}
	
	public void storeRestrictedPaymentMethod(RestrictedPaymentMethodModel model) {
		Connection conn = null;
		PrimaryKey pk = null;
		try {
			conn = getConnection();
			RestrictedPaymentMethodDAO.storeRestrictedPaymentMethod(conn, model);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}		
	}

	public void removeRestrictedPaymentMethod(PrimaryKey pk, String lastModifyUser) {
		Connection conn = null;
		try {
			conn = getConnection();
			RestrictedPaymentMethodModel model = findRestrictedPaymentMethodByPrimaryKey(pk);
			RestrictedPaymentMethodDAO.removeRestrictedPaymentMethod(conn, pk, lastModifyUser);
			logRestrictedPaymentMethodActivity(model, EnumAccountActivityType.REMOVE_RESTRICTED_PAYMENT_METHOD, model.getSource());
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}		
	}

	public boolean checkBadAccount(ErpPaymentMethodI erpPaymentMethod, boolean useBadAccountCache) {

		if ("false".equals(ErpServicesProperties.getCheckForFraud())) {
			// no check, no problem :)
			return false;
		}
		
		LOGGER.info("Bad Account check. " +
				"Payment Method = " + erpPaymentMethod.getPaymentMethodType() +
				" Account Number = " + StringUtil.maskCreditCard(erpPaymentMethod.getAccountNumber())
				);

		if (checkBadPatterns(erpPaymentMethod)) {
			LOGGER.info("check Bad Account Pattern return true. " +
					"Payment Method = " + erpPaymentMethod.getPaymentMethodType() +
					" Account Number = " + StringUtil.maskCreditCard(erpPaymentMethod.getAccountNumber())
					);
			return true;
		}
		
		if (useBadAccountCache) {
			if (checkBadAccountInCache(erpPaymentMethod)) {
				LOGGER.info("check Bad Account In Cache returned true. " +
						"Payment Method = " + erpPaymentMethod.getPaymentMethodType() +
						" Account Number = " + StringUtil.maskCreditCard(erpPaymentMethod.getAccountNumber())
						);
				return true;				
			}
		} else {
			if (checkBadAccountInDB(erpPaymentMethod)) {
				LOGGER.info("check Bad Account In Database returned true. " +
						"Payment Method = " + erpPaymentMethod.getPaymentMethodType() +
						" Account Number = " + StringUtil.maskCreditCard(erpPaymentMethod.getAccountNumber())
						);
				return true;				
			}			
		}

		return false;
		
	}

	private boolean checkBadPatterns(ErpPaymentMethodI erpPaymentMethod) {
		
		List patternList = (List)PaymentFraudFactory.getInstance().getPatternListFromCache();
		
		if (patternList == null || patternList.size() == 0) {
			return false;
		}
		
		Iterator iter = patternList.iterator();
		while (iter.hasNext()) {
			RestrictedPaymentMethodModel m = (RestrictedPaymentMethodModel) iter.next();
		
			if (EnumRestrictedPaymentMethodStatus.BAD.equals(m.getStatus())) {
			
				// check for bad account patterns
				if (erpPaymentMethod.getAccountNumber() != null && m.getAccountPatternType() != null) {
					
						if (EnumRestrictedPatternType.STARTS_WITH.equals(m.getAccountPatternType())) {
							if (erpPaymentMethod.getAccountNumber().startsWith(m.getAccountNumber())) {
								return true;
							}
						}
						if (EnumRestrictedPatternType.ENDS_WITH.equals(m.getAccountPatternType())) {
							if (erpPaymentMethod.getAccountNumber().endsWith(m.getAccountNumber())) {
								return true;
							}
						}
						if (EnumRestrictedPatternType.CONTAINS.equals(m.getAccountPatternType())) {
							if (erpPaymentMethod.getAccountNumber().indexOf(m.getAccountNumber()) > -1) {
								return true;
							}
						}					
						if (EnumRestrictedPatternType.EQUALS.equals(m.getAccountPatternType())) {
							if (erpPaymentMethod.getAccountNumber().equalsIgnoreCase(m.getAccountNumber())) {
								return true;
							}
						}
					}

				// check for bad aba route number patterns
				if (erpPaymentMethod.getAbaRouteNumber() != null && m.getAbaRoutePatternType() != null) {
				
					if (EnumRestrictedPatternType.STARTS_WITH.equals(m.getAbaRoutePatternType())) {
						if (erpPaymentMethod.getAbaRouteNumber().startsWith(m.getAbaRouteNumber())) {
							return true;
						}
					}
					if (EnumRestrictedPatternType.ENDS_WITH.equals(m.getAbaRoutePatternType())) {
						if (erpPaymentMethod.getAbaRouteNumber().endsWith(m.getAbaRouteNumber())) {
							return true;
						}
					}
					if (EnumRestrictedPatternType.CONTAINS.equals(m.getAbaRoutePatternType())) {
						if (erpPaymentMethod.getAbaRouteNumber().indexOf(m.getAbaRouteNumber()) > -1) {
							return true;
						}
					}					
					if (EnumRestrictedPatternType.EQUALS.equals(m.getAbaRoutePatternType())) {
						if (erpPaymentMethod.getAbaRouteNumber().equalsIgnoreCase(m.getAbaRouteNumber())) {
							return true;
						}
					}
				}									
			}
		}
		return false;
	}

	private boolean checkBadAccountInCache(ErpPaymentMethodI erpPaymentMethod) {
		
		List badAccountList = PaymentFraudFactory.getInstance().getBadAccountListFromCache();

		if (badAccountList != null) {
			Iterator iter = badAccountList.iterator();
			while (iter.hasNext()) {
				boolean isPaymentMethodMatch = true;
				boolean isAccountNumberMatch = true;
				boolean isAbaRouteNumberMatch = true;
				boolean isBankAccountTypeMatch = true;
				
				RestrictedPaymentMethodModel model = (RestrictedPaymentMethodModel) iter.next();
				if (model.getPaymentMethodType() != null && erpPaymentMethod.getPaymentMethodType() != null) {
					if (!model.getPaymentMethodType().equals(erpPaymentMethod.getPaymentMethodType())) {
						isPaymentMethodMatch = false;
					}
				}
				if (model.getAccountNumber() != null && erpPaymentMethod.getAccountNumber() != null) {
					if (!model.getAccountNumber().equalsIgnoreCase(erpPaymentMethod.getAccountNumber())) {
						isAccountNumberMatch = false;
					}
				}
				if (model.getAbaRouteNumber() != null && erpPaymentMethod.getAbaRouteNumber() != null) {
					if (!model.getAbaRouteNumber().equalsIgnoreCase(erpPaymentMethod.getAbaRouteNumber())) {
						isAbaRouteNumberMatch = false;
					}
				}
				if (model.getBankAccountType() != null && erpPaymentMethod.getBankAccountType() != null) {
					if (!model.getBankAccountType().equals(erpPaymentMethod.getBankAccountType())) {
						isBankAccountTypeMatch = false;
					}
				}
				
				if (isPaymentMethodMatch && isAccountNumberMatch && isAbaRouteNumberMatch && isBankAccountTypeMatch) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean checkBadAccountInDB(ErpPaymentMethodI erpPaymentMethod) {
		
		Connection conn = null;
		try {
			
			conn = getConnection();
			
			RestrictedPaymentMethodCriteria criteria = new RestrictedPaymentMethodCriteria();
			if (erpPaymentMethod.getBankAccountType() != null) {
				criteria.setBankAccountType(erpPaymentMethod.getBankAccountType());
			}
			if (erpPaymentMethod.getAbaRouteNumber() != null) {
				criteria.setAbaRouteNumber(erpPaymentMethod.getAbaRouteNumber());
			}
			criteria.setAccountNumber(erpPaymentMethod.getAccountNumber());
			criteria.setStatus(EnumRestrictedPaymentMethodStatus.BAD);

			List list = RestrictedPaymentMethodDAO.findRestrictedPaymentMethods(conn, criteria);
			
			if (list != null && list.size() > 0) {
				return true;
			}

		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
		
		return false;
		
	}

	public List loadAllPatterns() throws EJBException {
		Connection conn = null;
		try {
			conn = getConnection();
			return RestrictedPaymentMethodDAO.loadAllPatterns(conn);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}

	public List loadAllRestrictedPaymentMethods() throws EJBException {
		Connection conn = null;
		try {
			conn = getConnection();
			return RestrictedPaymentMethodDAO.loadAll(conn);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}
	
	public List loadAllBadPaymentMethods() throws EJBException {
		Connection conn = null;
		try {
			conn = getConnection();
			return RestrictedPaymentMethodDAO.loadAllBadPaymentMethods(conn);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}
	}
	
	public ErpPaymentMethodI findPaymentMethodByAccountInfo(RestrictedPaymentMethodModel m) throws EJBException {
		Connection conn = null;
		try {
			conn = getConnection();
			return RestrictedPaymentMethodDAO.findPaymentMethodByAccountInfo(conn, m);
		} catch (SQLException ex) {
			LOGGER.error("SQLException occurred", ex);
			throw new EJBException(ex.getMessage());

		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				LOGGER.warn("Unable to close Connection", ex);
				throw new EJBException(ex.getMessage());
			}
		}		
	}
	
	private void logRestrictedPaymentMethodActivity(RestrictedPaymentMethodModel m, EnumAccountActivityType accountActivity, EnumTransactionSource source) {
		if (m.getCustomerId() != null && !"".equals(m.getCustomerId())) {
			PrimaryKey erpCustPK = 	new PrimaryKey (m.getCustomerId());
			String note = buildActivityLogNote(m, accountActivity);
			logActivity(erpCustPK, accountActivity, note, source);
		}
	}
	
	private String buildActivityLogNote(RestrictedPaymentMethodModel m, EnumAccountActivityType activityType) {
				
		StringBuffer sb = new StringBuffer();
		if (EnumAccountActivityType.EDIT_RESTRICTED_PAYMENT_METHOD.equals(activityType)) {
			sb.append("Replaced Record: ");			
		} else if (EnumAccountActivityType.READD_RESTRICTED_PAYMENT_METHOD.equals(activityType)) {
			sb.append("Replaced Record:");			
		} else if (EnumAccountActivityType.ADD_RESTRICTED_PAYMENT_METHOD.equals(activityType)) {
			sb.append("New Record:");			
		} else if  (EnumAccountActivityType.REMOVE_RESTRICTED_PAYMENT_METHOD.equals(activityType)) {
			sb.append("Deleted Record:");						
		} else {
			sb.append("Restricted Payment Method Record:");									
		}
		if (m.getBankAccountType() != null) {
			sb.append(", Account Type = ");		
			sb.append(m.getBankAccountType().getName());
		}
		sb.append(", Account # = ");
		ErpECheckModel paymentMethod = new ErpECheckModel();
		paymentMethod.setAccountNumber(m.getAccountNumber());
		sb.append(paymentMethod.getMaskedAccountNumber());
		sb.append(", Route # = ");
		sb.append(m.getAbaRouteNumber());
		sb.append(", Customer = ");
		sb.append(m.getFirstName()); 
		sb.append(" "); 
		sb.append(m.getLastName()); 
		sb.append(", Source = ");
		sb.append(m.getSource()); 
		sb.append(", User = ");
		sb.append((m.getCreateUser() != null) ? m.getCreateUser() : m.getLastModifyUser());
		if (m.getReason() != null) { 
			sb.append(", Reason = ");
			sb.append(m.getReason().getDescription());
		}
		sb.append(", Note = ");
		sb.append(m.getNote());
		return sb.toString();
		
	}
	
	private void logActivity(PrimaryKey pk, EnumAccountActivityType activityType, String note, EnumTransactionSource source) {
		ErpActivityRecord erpActivityRecord = new ErpActivityRecord();
		erpActivityRecord.setCustomerId(pk.getId());
		erpActivityRecord.setActivityType(activityType);
		erpActivityRecord.setCustomerId(pk.getId());
		erpActivityRecord.setDate(new Date());
		erpActivityRecord.setInitiator(source.getCode());
		erpActivityRecord.setSource(source);
		erpActivityRecord.setNote(note);
		logActivity(erpActivityRecord);
	}
	
	private void logActivity(ErpActivityRecord rec) {
		ActivityLogHome home = this.getActivityLogHome();
		try {
			ActivityLogSB logSB = home.create();
			logSB.logActivity(rec);
		} catch (RemoteException e) {
			throw new EJBException(e);
		} catch (CreateException e) {
			throw new EJBException(e);
		}
	}

	private ActivityLogHome getActivityLogHome() {
		try {
			return (ActivityLogHome) LOCATOR.getRemoteHome("freshdirect.customer.ActivityLog", ActivityLogHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
}
