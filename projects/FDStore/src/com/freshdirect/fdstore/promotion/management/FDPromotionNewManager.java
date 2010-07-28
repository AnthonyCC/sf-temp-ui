package com.freshdirect.fdstore.promotion.management;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewHome;
import com.freshdirect.fdstore.promotion.management.ejb.FDPromotionManagerNewSB;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDPromotionNewManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDPromotionNewManager.class);
	
	private static FDPromotionManagerNewHome managerHome = null;
	
	public static PrimaryKey createPromotion(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			PrimaryKey pk = sb.createPromotion(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			return pk;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}			
	}
	
	public static List<FDPromotionNewModel> getPromotions() throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getPromotions();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	public static FDPromotionNewModel loadPromotion(String promotionId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getPromotion(promotionId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	/**
	 * @deprecated use {@link FDPromotionNewManager#storePromotion(FDPromotionNewModel, boolean)} instead.
	 * 
	 * @param promotion Promotion to store
	 * 
	 * @throws FDResourceException
	 * @throws FDDuplicatePromoFieldException
	 * @throws FDPromoTypeNotFoundException
	 * @throws FDPromoCustNotFoundException
	 */
	public static void storePromotion(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		FDPromotionNewManager.storePromotion(promotion, false);
	}


	public static void storePromotion(FDPromotionNewModel promotion, boolean saveLog) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotion(promotion, saveLog);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	public static void createPromotions(List<FDPromotionNewModel> promotions) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.createPromotions(promotions);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}			
	}

	public static List<FDPromotionNewModel> loadPublishablePromotions() throws FDResourceException, FinderException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getPublishablePromos();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	
	public static boolean isPromotionCodeUsed(String promoCode) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.isPromotionCodeUsed(promoCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static boolean isPromotionNameUsed(String promoName) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.isPromotionNameUsed(promoName);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static String findPromotion(String promoCode) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.findPromotion(promoCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static boolean publishPromotion(FDPromotionNewModel promo) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.publishPromotion(promo);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}


	public static boolean cancelPromotion(FDPromotionNewModel promo) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.cancelPromotion(promo);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	
	public static boolean fixPromoStatusAfterPublish(Collection<String> codes) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.fixPromoStatusAfterPublish(codes);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}			
	}

	
	public static void logPublishEvent(CrmAgentModel agent,
			Date start, Date end, String destURL,
			Map<String, Boolean> publishResults,
			Map<String, EnumPromotionStatus> preStatuses,
			Map<String, EnumPromotionStatus> postStatuses,
			Map<String, String> changeIDs) throws FDResourceException {
		
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.logPublishEvent(agent,
					start, end, destURL,
					publishResults, preStatuses, postStatuses, changeIDs);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}			
	}

	public static List<FDPromoChangeModel> loadPromoAuditChanges(String promotionId) throws FDResourceException, FinderException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getPromoAuditChanges(promotionId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDPromotionManagerNewHome) ctx.lookup(FDStoreProperties.getFDPromotionManagerNewHome());
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}
	
	public static PrimaryKey createPromotionBasic(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			PrimaryKey pk = sb.createPromotionBasic(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			return pk;
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}			
	}
	
	public static void storePromotionBasic(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionBasic(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void storePromotionOfferInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionOfferInfo(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static void storePromotionCartInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionCartInfo(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static List<PromotionI> getModifiedOnlyPromos(Date lastModified) throws FDResourceException {
		lookupManagerHome();
		
		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getModifiedOnlyPromos(lastModified);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}		
		 
	}
	
	public static void storePromotionPaymentInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionPaymentInfo(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	public static List<PromotionI> getAllAutomaticPromotions() throws FDResourceException {
		lookupManagerHome();
		
		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getAllAutomaticPromotions();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}		
		 
	}
	
	
	public static PromotionI getPromotionForRT(String  promoId) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getPromotionForRT(promoId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}				
	}
	
	
	public static void storePromotionDlvZoneInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionDlvZoneInfo(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void storePromotionCustReqInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionCustReqInfo(promotion);
			// this forces a refresh of the promotions cache
			//FDPromotionFactory.getInstance().forceRefresh();
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			//Refresh the RT cache as well for placing orders through CRM.
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static Integer getRedemptionCount(String promoId) throws FDResourceException{
		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getRedemptionCount(promoId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static String getRedemptionPromotionId(String  redemptionCode) throws FDResourceException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.getRedemptionPromotionId(redemptionCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static boolean isRedemptionCodeExists(String redemptionCode)throws FDResourceException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.isRedemptionCodeExists(redemptionCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static boolean isRedemptionCodeExists(String redemptionCode, String promotionId)throws FDResourceException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.isRedemptionCodeExists(redemptionCode, promotionId);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void storePromotionStatus(FDPromotionNewModel promotion,EnumPromotionStatus status)throws FDResourceException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionStatus(promotion,status);
			FDPromotionNewModelFactory.getInstance().forceRefresh();
			PromotionFactory.getInstance().forceRefresh(promotion.getPromotionCode());
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void storePromotionHoldStatus(FDPromotionNewModel promotion)throws FDResourceException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storePromotionHoldStatus(promotion);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public static void storeAssignedCustomers(FDPromotionNewModel promotion, String assignedCustomerUserIds)throws FDResourceException,FDPromoCustNotFoundException{
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			sb.storeAssignedCustomers(promotion,assignedCustomerUserIds);
			List<String> assignedCustomerUserIdsList = sb.loadAssignedCustomerUserIds(promotion.getId());
			if (assignedCustomerUserIdsList != null
					&& assignedCustomerUserIdsList.size() > 0) {
				promotion.setAssignedCustomerUserIds(StringUtil
						.encodeString(assignedCustomerUserIdsList));
			} else {
				promotion.setAssignedCustomerUserIds("");
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}



	public static void storeChangeLogEntries(String promoPk, List<FDPromoChangeModel> changes) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			
			sb.storeChangeLogEntries(promoPk, changes);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean lookupPromotion(String promotionCode) throws FDResourceException {
		lookupManagerHome();

		try {
			FDPromotionManagerNewSB sb = managerHome.create();
			return sb.lookupPromotion(promotionCode);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
	
	private static void invalidateManagerHome() {
		managerHome = null;
	}
}
