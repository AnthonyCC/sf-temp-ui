package com.freshdirect.fdstore.util;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.fdstore.content.EnumBurstType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Product display helper class
 * 
 * @author segabor
 *
 */
public class ProductLabeling {
	FDUserI customer;
	ProductModel product;
	
	Set<EnumBurstType> hideBursts;
	
	// display flags
	boolean displayDeal = false;
	boolean displayFave = false;
	boolean displayNew = false;


	/**
	 * @deprecated
	 * 
	 * @param customer
	 * @param product
	 * 
	 * @param hideBursts Hide all burst types
	 * @param hideNew Hide New Prods burst
	 * @param hideDeals Hide Deals burst
	 * @param hideYourFave Hide Your Fave burst
	 */
	public ProductLabeling(FDUserI customer, ProductModel product, boolean hideBursts, boolean hideNew, boolean hideDeals, boolean hideYourFave) {
		this.customer = customer;
		this.product = product;

		this.hideBursts = new HashSet<EnumBurstType>();

		if (hideBursts) {
			// hide all bursts
			for (EnumBurstType b : EnumBurstType.values()) {
				this.hideBursts.add(b);
			}
		} else {
			if (hideNew)
				this.hideBursts.add(EnumBurstType.NEW);
			if (hideDeals)
				this.hideBursts.add(EnumBurstType.DEAL);
			if (hideYourFave)
				this.hideBursts.add(EnumBurstType.YOUR_FAVE);
		}
		
		setDisplayFlags();
	}


	/**
	 * @param customer
	 * @param product
	 * 
	 * @param hideBursts Set of burst types to hide. Can be null.
	 */
	public ProductLabeling(FDUserI customer, ProductModel product, Set<EnumBurstType> hideBursts) {
		this.customer = customer;
		this.product = product;

		this.hideBursts = hideBursts;

		setDisplayFlags();
	}
	
	
	
	public ProductLabeling(FDUserI customer, ProductModel product) {
		this(customer, product, null);
	}

	
	/**
	 * Set display flags according to the input
	 */
	protected void setDisplayFlags() {
		displayDeal = false;
		displayFave = false;
		displayNew = false;


		int deal = (hideBursts == null || !hideBursts.contains(EnumBurstType.DEAL) ) ? product.getHighestDealPercentage() : 0;
		boolean isNew = (hideBursts == null || !hideBursts.contains(EnumBurstType.NEW) ) && product.isNew();
		boolean isYourFave = (hideBursts == null || !hideBursts.contains(EnumBurstType.YOUR_FAVE) ) && DYFUtil.isFavorite(product, customer);

		// determine what to display
		if (deal > 0) {
			displayDeal = true;
		} else if (isYourFave) {
			displayFave = true;
		} else if (isNew) {
			displayNew = true;
		}
	}


	public boolean isDisplayDeal() {
		return displayDeal;
	}


	public boolean isDisplayFave() {
		return displayFave;
	}


	public boolean isDisplayNew() {
		return displayNew;
	}

	public boolean isDisplayAny() {
		return displayDeal || displayFave || displayNew;
	}


	public String getTrackingCode() {
		if (displayDeal) {
			return EnumBurstType.DEAL.getCode();
		} else if (displayFave) {
			return EnumBurstType.YOUR_FAVE.getCode();
		} else if (displayNew) {
			return EnumBurstType.NEW.getCode();
		}
		return null;
	}


	public String getBurstCode() {
		String burst = null;
		if (isDisplayDeal())
			burst = Integer.toString(product.getHighestDealPercentage());
		else if (isDisplayFave())
			burst = "YF";
		else if (isDisplayNew())
			burst = "NE";
		return burst;
	}

	public boolean isHideBursts() {
		return hideBursts != null && hideBursts.size() == EnumBurstType.values().length ;
	}

	public boolean isHideNew() {
		return hideBursts != null && hideBursts.contains(EnumBurstType.NEW);
	}

	public boolean isHideDeals() {
		return hideBursts != null && hideBursts.contains(EnumBurstType.DEAL);
	}

	public boolean isHideYourFave() {
		return hideBursts != null && hideBursts.contains(EnumBurstType.YOUR_FAVE);
	}
}
