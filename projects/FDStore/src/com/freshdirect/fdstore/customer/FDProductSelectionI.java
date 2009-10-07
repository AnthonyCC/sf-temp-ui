/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */
 
package com.freshdirect.fdstore.customer;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;


/**
 * FDProductI
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public interface FDProductSelectionI extends FDConfigurableI {
		
	public FDSku getSku();
	public void setSku(FDSku sku);
	
	public ProductRef getProductRef();
		
	public String getSkuCode();

	public int getVersion();
	
	public String getCategoryName();
	
	public String getProductName();

	public FDConfigurableI getConfiguration();
	
	public void setConfiguration(FDConfigurableI configuration);	

	public void setQuantity(double quantity);
	
	public void setFixedPrice(double price);
	
	public double getFixedPrice();

	public void setSalesUnit(String salesUnit);
	
	public void setOptions(Map options);
	
	public boolean isAlcohol();

	public boolean isPerishable();
	
	public boolean isKosher();
	
	public boolean isPlatter();
	
	public void setDepartmentDesc(String deptDesc);
	public String getDepartmentDesc();
	
	public void setDescription(String desc);
	public String getDescription();
	
	public void setConfigurationDesc(String configDesc);
	public String getConfigurationDesc();
	
	public String getSalesUnitDescription();
	public String getUnitPrice();
	public String getLabel();
	public boolean isPricedByLb();
	public boolean isSoldByLb();
	public boolean isSoldBySalesUnits();
	
	public ProductModel lookupProduct();
	public FDProductInfo lookupFDProductInfo();
	public FDProduct lookupFDProduct();
	
	public String getDisplayQuantity();
	
	public ErpAffiliate getAffiliate();
	
	public SaleStatisticsI getStatistics();
	public void setStatistics(SaleStatisticsI statistics);
	
	public String getCustomerListLineId();
	public void setCustomerListLineId(String id);
	
	//  produce rating changes	
	public EnumOrderLineRating getProduceRating();
	
	public String getRecipeSourceId();
	public void setRecipeSourceId(String recipeSourceId);
	
	/**
	 *  Set the category ID that was displayed when this product was added to
	 *  the cart as a YMAL.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @param ymalCategoryId the ID of the category which was shown when this
	 *         product was added to the cart as a YMAL.
	 */
	public void setYmalCategoryId(String ymalCategoryId);
	
	/**
	 *  Get the category ID that was displayed when this product was added to
	 *  the cart as a YMAL.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @return the ID of the category which was shown when this
	 *         product was added to the cart as a YMAL.
	 */
	public String getYmalCategoryId();
	
	/**
	 *  Set the YMAL set ID this product was added to the cart from.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @param ymalSetId the ID of the YMAL set in which this product was
	 *         shown when added to the cart.
	 */
	public void setYmalSetId(String ymalSetId);
	
	/**
	 *  Get the YMAL set ID this product was added to the cart from.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @return the ID of the YMAL set in which this product was
	 *         shown when added to the cart.
	 */
	public String getYmalSetId();
	
	/**
	 *  Set the ID of the originating product for which this product was shown as a YMAL.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @param originatingProductId the ID of the product for which this product was
	 *         shown when added to the cart.
	 */
	public void setOriginatingProductId(String originatingProductId);
	
	/**
	 *  Get the ID of the originating product for which this product was shown as a YMAL.
	 *  Only applicable if the product was added to the cart from a YMAL set.
	 *  
	 *  @return the ID of the product for which this product was
	 *         shown when added to the cart.
	 */
	public String getOriginatingProductId();
	
	public boolean isRequestNotification();
	public void setRequestNotification(boolean requestNotification);
	
	public boolean isInvalidConfig();
	
	public void refreshConfiguration() throws FDResourceException, FDInvalidConfigurationException;
	
	public final static Comparator FREQUENCY_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			FDProductSelectionI  h1 = (FDProductSelectionI) o1;
			FDProductSelectionI h2 = (FDProductSelectionI) o2;
			
			int retValue = ((FDProductSelectionI)o1).getDepartmentDesc().compareTo(((FDProductSelectionI)o2).getDepartmentDesc());
			
			if(retValue == 0){
				retValue = (h2.getStatistics() == null ? 0 : h2.getStatistics().getFrequency()) - (h1.getStatistics() == null ? 0 : h1.getStatistics().getFrequency());
			}
			
			return retValue;
		}

	};
	
	public final static Comparator RECENT_PURCHASE_COMPARATOR_DESC = new Comparator() {

		public int compare(Object o1, Object o2) {
			FDProductSelectionI h1 = (FDProductSelectionI) o1;
			FDProductSelectionI h2 = (FDProductSelectionI) o2;
			
			int retValue = ((FDProductSelectionI)o1).getDepartmentDesc().compareTo(((FDProductSelectionI)o2).getDepartmentDesc());
			if(retValue == 0){
				retValue = (h2.getStatistics() == null ? new Date(0) : h2.getStatistics().getLastPurchase()).compareTo(h1 == null ? new Date(0) : h1.getStatistics().getLastPurchase());
			}
			
			return retValue;
			
		}

	};
	
	public final static Comparator DESCRIPTION_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {
			FDProductSelectionI h1 = (FDProductSelectionI) o1;
			FDProductSelectionI h2 = (FDProductSelectionI) o2;
			
			int retValue = ((FDProductSelectionI)o1).getDepartmentDesc().compareTo(((FDProductSelectionI)o2).getDepartmentDesc());
			
			if(retValue == 0){
				return h1.getDescription().compareTo(h2.getDescription());
			}
			
			return retValue;
		}

	};
}
