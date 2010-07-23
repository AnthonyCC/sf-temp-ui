package com.freshdirect.fdstore.promotion.management.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.core.PrimaryKey;

public interface FDPromotionManagerNewSB extends EJBObject {

	public List<FDPromotionNewModel> getPromotions() throws FDResourceException, RemoteException;	
	
	public PrimaryKey createPromotion(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public FDPromotionNewModel getPromotion(String promoId) throws FDResourceException, RemoteException;
	
	public void storePromotion(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public void createPromotions(List<FDPromotionNewModel> promotions)throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public List<FDPromotionNewModel> getPublishablePromos() throws FDResourceException, RemoteException;
	
	public List<FDPromoChangeModel> getPromoAuditChanges(String promotionId) throws FDResourceException, RemoteException;

	
	public PrimaryKey createPromotionBasic(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public void storePromotionBasic(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;


	public void deletePromotion(String promotionId) throws FDResourceException, RemoteException;
	
	public boolean isPromotionCodeUsed(String promoCode) throws FDResourceException, RemoteException;

	public boolean isPromotionNameUsed(String promoName) throws FDResourceException, RemoteException;

	public String findPromotion(String promoCode) throws FDResourceException, RemoteException;

	public boolean publishPromotion(FDPromotionNewModel promo) throws FDResourceException, RemoteException;

	public boolean cancelPromotion(FDPromotionNewModel promo) throws FDResourceException, RemoteException;

	public boolean fixPromoStatusAfterPublish(Collection<String> codes) throws FDResourceException, RemoteException;

	public void logPublishEvent(CrmAgentModel agent,
			java.util.Date start, java.util.Date end, String destURL,
			Map<String, Boolean> publishResults,
			Map<String, EnumPromotionStatus> preStatuses,
			Map<String, EnumPromotionStatus> postStatuses,
			Map<String, String> changeIDs) throws FDResourceException, RemoteException;
	
	public void storePromotionOfferInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public void storePromotionCartInfo(FDPromotionNewModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException,RemoteException;
	
	public void storePromotionPaymentInfo(FDPromotionNewModel promotion)throws FDResourceException, FDDuplicatePromoFieldException,	FDPromoTypeNotFoundException, FDPromoCustNotFoundException,RemoteException ;
	
	public List<PromotionI> getModifiedOnlyPromos(Date lastModified) throws FDResourceException, RemoteException;
	
	public  List<PromotionI>  getAllAutomaticPromotions() throws FDResourceException, RemoteException;
	
	public PromotionI getPromotionForRT(String  promoId) throws FDResourceException, RemoteException;
	
	public void storePromotionDlvZoneInfo(FDPromotionNewModel promotion)throws FDResourceException, FDDuplicatePromoFieldException,	FDPromoTypeNotFoundException, FDPromoCustNotFoundException,RemoteException ;
	
	public void storePromotionCustReqInfo(FDPromotionNewModel promotion)throws FDResourceException, FDDuplicatePromoFieldException,	FDPromoTypeNotFoundException, FDPromoCustNotFoundException,RemoteException ;
	
	public Integer getRedemptionCount(String promoId) throws FDResourceException, RemoteException;
	
	public String getRedemptionPromotionId(String  redemptionCode) throws RemoteException;
	
	public boolean isRedemptionCodeExists(String redemptionCode)throws FDResourceException,RemoteException;
	
	public boolean isRedemptionCodeExists(String redemptionCode, String promotionId)throws FDResourceException,RemoteException;
	
	public void storePromotionStatus(FDPromotionNewModel promotion,EnumPromotionStatus status)throws FDResourceException,RemoteException;
	
	public void storePromotionHoldStatus(FDPromotionNewModel promotion)throws FDResourceException,RemoteException;
	
	public void storeAssignedCustomers(String promotionId, String assignedCustomerUserIds) throws FDResourceException, FDPromoCustNotFoundException, RemoteException;
	
	public List<String> loadAssignedCustomerUserIds(String promotionId) throws RemoteException, FDResourceException;
}

