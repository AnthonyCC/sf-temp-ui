package com.freshdirect.fdstore.promotion.management.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.*;
import com.freshdirect.framework.core.PrimaryKey;

import java.rmi.RemoteException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import javax.ejb.EJBObject;

public interface FDPromotionManagerSB extends EJBObject {

	/**
	 * @return List of FDPromotionModel
	 */
	public List getPromotions() throws FDResourceException, RemoteException;	

	public PrimaryKey createPromotion(FDPromotionModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;	
	
	public FDPromotionModel getPromotion(String promoId) throws FDResourceException, RemoteException;
	
	public List getPromotionVariants(String promoId) throws FDResourceException, RemoteException;	
	
	public void storePromotion(FDPromotionModel promotion) throws FDResourceException, FDDuplicatePromoFieldException, FDPromoTypeNotFoundException, FDPromoCustNotFoundException, RemoteException;
	
	public void removePromotion(PrimaryKey pk) throws FDResourceException, RemoteException;
	
	public List getPromoCustomerInfoListFromPromotionId(PrimaryKey pk) throws FDResourceException, RemoteException;		
	
	public List getPromoCustomerInfoListFromCustomerId(PrimaryKey pk) throws FDResourceException, RemoteException;		

	public List getAvailablePromosForCustomer(PrimaryKey pk) throws FDResourceException, RemoteException;
	
	public void insertPromoCustomers(FDActionInfo actionInfo, List promoCustomers) throws FDResourceException, RemoteException;

	public void updatePromoCustomers(FDActionInfo actionInfo, List promoCustomers) throws FDResourceException, RemoteException;
	
	public void removePromoCustomers(FDActionInfo actionInfo, List promoCustomers) throws FDResourceException, RemoteException;
	
	/*
	 * New Methods added to implement the new design for how promotions 
	 * will be loaded, evaluated and applied using the new caching framework.
	 * 
	 */
	public List getAllAutomtaticPromotions() throws RemoteException;
	
	public List getModifiedOnlyPromos(Date lastModified) throws RemoteException;
	
	public PromotionI getPromotionForRT(String promoId) throws RemoteException;
	
	public String getRedemptionPromotionId(String  redemptionCode) throws RemoteException;
	
	public Map refreshAutomaticPromotionCodes() throws RemoteException;
	
	public Map getPromotionCodes() throws RemoteException;
	
	public List getAllActivePromoVariants(List smartSavingsFeatures) throws RemoteException;
}
