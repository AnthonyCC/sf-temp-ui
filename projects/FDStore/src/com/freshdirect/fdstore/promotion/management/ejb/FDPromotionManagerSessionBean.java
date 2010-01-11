package com.freshdirect.fdstore.promotion.management.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ActivityLogHome;
import com.freshdirect.customer.ejb.ActivityLogSB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionDecorator;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.ejb.FDPromotionDAO;
import com.freshdirect.fdstore.promotion.management.FDPromoCustomerInfo;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;

public class FDPromotionManagerSessionBean extends FDSessionBeanSupport {

	private final static Logger LOGGER = LoggerFactory
			.getInstance(FDPromotionManagerSessionBean.class);

	private static SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM-yyyy");

	public List getPromotions() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();

			List promoList = FDPromotionManagerDAO.getPromotions(conn);

			return promoList;

		} catch (SQLException sqle) {
			throw new EJBException(sqle.getMessage());
		} finally {
		    close(conn);
		}
	}

	public FDPromotionModel getPromotion(String promoId) throws 
			FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDPromotionModel promotion = FDPromotionManagerDAO.getPromotion(
					conn, promoId);
			return promotion;

		} catch (SQLException sqle) {
			throw new EJBException(sqle.getMessage());
		} finally {
                    close(conn);
		}
	}
	
	
	
	public List getPromotionVariants(String promoId) throws FDResourceException{
		
		Connection conn = null;
		try {
			conn = getConnection();
			List promotionList = FDPromotionManagerDAO.getPromotionVariants(
					conn, promoId);
			return promotionList;

		} catch (SQLException sqle) {
			throw new EJBException(sqle.getMessage());
		} finally {
                    close(conn);
		}
		
	}

	public PrimaryKey createPromotion(FDPromotionModel promotion)
			throws FDResourceException, FDDuplicatePromoFieldException,
			FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			PrimaryKey pk = FDPromotionManagerDAO.createPromotion(conn,
					promotion);
			conn.commit();
			return pk;
		} catch (SQLException sqle) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
				}
			}
			if (sqle.getMessage().toLowerCase().indexOf("unique") > -1) {
				throw new FDDuplicatePromoFieldException(
						"Name or promotion code or redemption code already exists.");
			} else if (sqle.getMessage().toLowerCase().indexOf("not found") > -1) {
				throw new FDPromoTypeNotFoundException(
						"Promotion type not found");
			} else if (sqle.getMessage().toLowerCase().indexOf(
					"invalid customer id(s)") > -1) {
				throw new FDPromoCustNotFoundException(sqle.getMessage());
			}
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public void storePromotion(FDPromotionModel promotion)
			throws FDResourceException, FDDuplicatePromoFieldException,
			FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			FDPromotionManagerDAO.storePromotion(conn, promotion);
			conn.commit();
		} catch (SQLException sqle) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e) {
				}
			}
			if (sqle.getMessage().toLowerCase().indexOf("unique") > -1) {
				throw new FDDuplicatePromoFieldException(
						"Name or promotion code or redemption code already exists.");
			} else if (sqle.getMessage().toLowerCase().indexOf("not found") > -1) {
				throw new FDPromoTypeNotFoundException(
						"Promotion type not found");
			} else if (sqle.getMessage().toLowerCase().indexOf(
					"invalid customer id(s)") > -1) {
				throw new FDPromoCustNotFoundException(sqle.getMessage());
			}
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}

	}

	public void removePromotion(PrimaryKey pk) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();

			FDPromotionManagerDAO.removePromotion(conn, pk);

		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public List getPromoCustomerInfoListFromPromotionId(PrimaryKey pk)
			throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDPromotionManagerDAO
					.getPromoCustomerInfoListFromPromotionId(conn, pk);
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public List getPromoCustomerInfoListFromCustomerId(PrimaryKey pk)
			throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDPromotionManagerDAO
					.getPromoCustomerInfoListFromCustomerId(conn, pk);
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public List getAvailablePromosForCustomer(PrimaryKey pk)
			throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return FDPromotionManagerDAO
					.getAvailablePromosForCustomer(conn, pk);
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public void insertPromoCustomers(FDActionInfo actionInfo,
			List promoCustomers) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDPromotionManagerDAO.insertPromoCustomers(conn, promoCustomers);
			String note = getPromoCustActivityNote(promoCustomers);
			this.logActivity(actionInfo.createActivity(
					EnumAccountActivityType.ADD_CUST_PROMO, note));
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public void updatePromoCustomers(FDActionInfo actionInfo,
			List promoCustomers) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDPromotionManagerDAO.updatePromoCustomers(conn, promoCustomers);
			String note = getPromoCustActivityNote(promoCustomers);
			this.logActivity(actionInfo.createActivity(
					EnumAccountActivityType.EDIT_CUST_PROMO, note));
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	public void removePromoCustomers(FDActionInfo actionInfo,
			List promoCustomers) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDPromotionManagerDAO.removePromoCustomers(conn, promoCustomers);
			String note = getPromoCustActivityNote(promoCustomers);
			this.logActivity(actionInfo.createActivity(
					EnumAccountActivityType.REMOVE_CUST_PROMO, note));
		} catch (SQLException sqle) {
			throw new FDResourceException(sqle);
		} finally {
                    close(conn);
		}
	}

	// Methods for loading Promotions at runtime.

	/**
	 * 
	 */
	public List getAllAutomtaticPromotions() {
		Connection conn = null;
		try {
			conn = getConnection();

			List promoList = FDPromotionDAO.loadAllAutomaticPromotions(conn);
			for (ListIterator i = promoList.listIterator(); i.hasNext();) {
				Promotion promo = (Promotion) i.next();
				//This decorate method is called for SIGNUP promos.
				PromotionDecorator.getInstance().decorate(promo);
				//Make sure Promo has a valid applicator.
				if (!promo.isValid()) {
					LOGGER.warn("Incomplete promotion configuration for " + promo);
					i.remove();
				}
			}

			return promoList;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

	/**
	 * 
	 */
	public List getModifiedOnlyPromos(Date lastModified) {
		Connection conn = null;
		try {
			conn = getConnection();

			List promoList = FDPromotionDAO.loadModifiedOnlyPromotions(conn, lastModified);
			for (ListIterator i = promoList.listIterator(); i.hasNext();) {
				Promotion promo = (Promotion) i.next();
				//This decorate method is called for SIGNUP promos.
				PromotionDecorator.getInstance().decorate(promo);
				//Make sure Promo has a valid applicator.
				if (!promo.isValid()) {
					
					LOGGER.warn("Incomplete promotion configuration for " + promo);
					i.remove();
				}
			}			
			return promoList;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
	/**
	 * 
	 * @param promoId
	 * @return
	 * @throws FinderException
	 * @throws FDResourceException
	 */
	public PromotionI getPromotionForRT(String promoId) {
		Connection conn = null;
		try {
			conn = getConnection();
			Promotion promotion = (Promotion) FDPromotionDAO.loadPromotion(conn, promoId);
			if(promotion == null){
				return null;
			}
			//This decorate method is called for SIGNUP promos.
			PromotionDecorator.getInstance().decorate(promotion);
			//Make sure Promo has a valid applicator.
			if (promotion != null && !promotion.isValid()) {
				LOGGER.warn("Incomplete promotion configuration for " + promotion);
				return null;
			}			
			return promotion;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

	public String getRedemptionPromotionId(String redemptionCode) {
		Connection conn = null;
		try {
			conn = getConnection();
			String promoId = FDPromotionDAO.getRedemptionPromotionId(conn,
					redemptionCode);
			return promoId;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

	/**
	 * This method is called to refresh the automatic promo code list
	 * for use by runtime promo engine by both Website and CRM.
	 * @return Map containing all active automatic promotion codes 
	 * along with their last modified timestamp. 
	 */
	public Map refreshAutomaticPromotionCodes() {
		Connection conn = null;
		try {
			conn = getConnection();

			Map promoCodes = FDPromotionDAO.getAllAutomaticPromotionCodes(conn);

			return promoCodes;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}
	/**
	 * This method is called by CRM for listing all promotions.
	 * @return Map containing all promotion codes along with their last modified timestamp. 
	 */
	public Map getPromotionCodes() {
		Connection conn = null;
		try {
			conn = getConnection();

			Map promoCodes = FDPromotionManagerDAO.getPromotionCodes(conn);

			return promoCodes;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
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

	private String getPromoCustActivityNote(List promoCustomers) {

		StringBuffer sb = new StringBuffer();
		if (promoCustomers != null && promoCustomers.size() > 0) {
			for (Iterator i = promoCustomers.iterator(); i.hasNext();) {
				FDPromoCustomerInfo oc = (FDPromoCustomerInfo) i.next();
				sb.append("&nbsp;&nbsp;");
				sb.append(" Promotion : ");
				sb.append(oc.getPromotionId());
				if (!"".equals(NVL.apply(oc.getPromotionDesc(), ""))) {
					sb.append(", Desc : ");
					sb.append(oc.getPromotionDesc());
				}
				if (!"".equals(NVL.apply(oc.getUsageCountStr(), ""))
						|| oc.getExpirationDate() != null) {
					if (!"".equals(NVL.apply(oc.getUsageCountStr(), ""))) {
						sb.append(", Usage Count : ");
						sb.append(oc.getUsageCount());
					}
					if (oc.getExpirationDate() != null) {
						sb.append(", Expiration Date : ");
						sb.append(SDF.format(oc.getExpirationDate()));
					}
				}
			}
		}
		return sb.toString();
	}
	// 

	/**
	 * Methods for loading All Active promo variants.
	 * Map variantId --> FDPromoVariantModel
	 */
	public List getAllActivePromoVariants(List smartSavingsFeatures) {
		Connection conn = null;
		try {
			conn = getConnection();

			List promoVariants = FDPromotionDAO.loadAllActivePromoVariants(conn, smartSavingsFeatures);

			return promoVariants;

		} catch (SQLException sqle) {
			throw new EJBException(sqle);
		} finally {
                    close(conn);
		}
	}

}
