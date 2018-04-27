package com.freshdirect.fdstore.ecomm.gateway;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.ecommerce.data.promotion.management.FDPromotionNewData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.core.PrimaryKey;
public interface FDPromotionManagerServiceI {
	
	
	public FDPromotionNewData getPromotionNewDataByPK(String pK) throws FDResourceException;
/**
 * retrieves the promoID by promocode.<BR> note, this method can return a null.
 * @param promoCode
 * @return
 * @throws FDResourceException
 */
	public String findpromotionIDbyPromoCode(String promoCode) throws FDResourceException;
	public String deletePromotionByPromoID(String promoID) throws FDResourceException;
	public Boolean isPromotionCodeUsed(String promoCode) throws FDResourceException;
	public Boolean isPromotionNameUsed(String promoName) throws FDResourceException;
	public Boolean fixPromotionStatusAfterPublish(Collection<String> promoCodes) throws FDResourceException;
	public int getRedemptionCount(String promoID, Date date) throws FDResourceException;
	public String getRedemptionPromotionId(String redemptionCode) throws FDResourceException;
	public boolean isRedemptionCodeExists(String redemptionCode) throws FDResourceException;
	public boolean isRedemptionCodeExists(String redemptionCode, String promoId) throws FDResourceException;
	public boolean isRafPromoCodeExists(String redemptionCode) throws FDResourceException;
	public boolean isRafPromoCodeExists(String redemptionCode, String promoId) throws FDResourceException;
	public List<String> loadAssignedCustomerUserIds(String promoId) throws FDResourceException;
	//public boolean lookupPromotion(String promoCode) throws FDResourceException;
	public boolean lookupPromotion(String promoCode) throws FDResourceException;
	public boolean isCustomerInAssignedList(String userId, String promotionId) throws FDResourceException;
	public String setDOWLimit(int dayofweek, double limit) throws FDResourceException;
	public Boolean isTSAPromoCodeExists(String tsaCode) throws FDResourceException;
	public Boolean isTSAPromoCodeExists(String tsaCode, String promotionId) throws FDResourceException;
	public String getRedemptionCode(String tsaCode) throws FDResourceException;
	public Map<Integer, Double> getDOWLimits() throws FDResourceException;
	public PrimaryKey createPromotion(FDPromotionNewModel promotion) throws FDResourceException;
	public List<FDPromotionNewModel> getPromotions() throws FDResourceException;
	public FDPromotionNewModel getPromotionByPK(String pK) throws FDResourceException;
	public List<FDPromotionNewModel> getPromotionsByYear(int year) throws FDResourceException;
	public List<FDPromotionNewModel> getModifiedOnlyPromotions(Date modifiedDate) throws FDResourceException;
	public  	PrimaryKey storePromotion(FDPromotionNewModel promotion, boolean savelog) throws FDResourceException;
	public void createPromotions(List<FDPromotionNewModel> promotions) throws FDResourceException;
	public List<FDPromotionNewModel> getPublishablePromos() throws FDResourceException;
	public boolean cancelPromotion(FDPromotionNewModel promotion) throws FDResourceException;
	public PrimaryKey createPromotionBasic(FDPromotionNewModel promotion) throws FDResourceException;
	public PrimaryKey storePromotionBasic(FDPromotionNewModel promotion) throws FDResourceException;
	public PrimaryKey storePromotionOfferInfo(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionCartInfo(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionPaymentInfo(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionDlvZoneInfo(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionCustReqInfo(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionHoldStatus(FDPromotionNewModel promotion) throws FDResourceException;
	public void storePromotionStatus(FDPromotionNewModel promotion, EnumPromotionStatus status) throws FDResourceException;
	public void storeAssignedCustomers(FDPromotionNewModel promotion, String assignedCustomerUserIds)
			throws FDResourceException;
	public PrimaryKey createPromotionBatch(FDPromotionNewModel promotion) throws FDResourceException;
	public List<FDPromotionNewModel> getBatchPromotions(String batchId) throws FDResourceException;
	//public boolean publishPromotion(FDPromotionNewModel promotion) throws FDResourceException;
	public 	boolean publishPromotion(FDPromotionNewModel promotion) throws FDResourceException;
	public List<FDPromoChangeModel> loadPromoAuditChanges(String promotionId) throws FDResourceException;
	public void storeChangeLogEntries(String promoPk, List<FDPromoChangeModel> changes) throws FDResourceException;
	public List<PromotionI> getAllAutomaticPromotions() throws FDResourceException;
	public List<PromotionI> getModifiedOnlyPromos(Date lastModified) throws FDResourceException;
	public List<PromotionI> getReferralPromotions(String customerId, EnumEStoreId storeid) throws FDResourceException ;
	public PromotionI getPromotionForRT(String promoCode) throws FDResourceException;
}
