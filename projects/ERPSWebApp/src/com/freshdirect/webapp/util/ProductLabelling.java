package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Product display helper class
 * 
 * @author segabor
 *
 */
public class ProductLabelling {
	FDUserI customer;
	ProductModel product;
	
	boolean hideBursts = false;
	boolean hideNew = false;
	boolean hideDeals = false;
	boolean hideYourFave = false;

	// display flags
	boolean displayDeal = false;
	boolean displayFave = false;
	boolean displayNew = false;

	public ProductLabelling(FDUserI customer, ProductModel product, boolean hideBursts, boolean hideNew, boolean hideDeals, boolean hideYourFave) {
		this.customer = customer;
		this.product = product;
		
		this.hideBursts = hideBursts;
		this.hideNew = hideNew;
		this.hideDeals = hideDeals;
		this.hideYourFave = hideYourFave;
		
		setDisplayFlags();
	}

	
	public ProductLabelling(FDUserI customer, ProductModel product) {
		this(customer, product, false, false, false, false);
	}

	
	/**
	 * Set display flags according to the input
	 */
	protected void setDisplayFlags() {
		displayDeal = false;
		displayFave = false;
		displayNew = false;
		
		
		int deal = !hideBursts && !hideDeals ? product.getDealPercentage() : 0;
		boolean isNew = !hideBursts && !hideNew && product.isNew();
		boolean isYourFave = !hideBursts && !hideYourFave && DYFUtil.isFavorite(product, customer);

		isNew = false; isYourFave = false; // REMOVE IT AFTER TESTING !!!!

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
			return "deal";
		} else if (displayFave) {
			return "fave";
		} else if (displayNew) {
			return "new";
		}
		return null;
	}
	

	public boolean isHideBursts() {
		return hideBursts;
	}

	public void setHideBursts(boolean hideBursts) {
		this.hideBursts = hideBursts;
		setDisplayFlags();
	}

	public boolean isHideNew() {
		return hideNew;
	}

	public void setHideNew(boolean hideNew) {
		this.hideNew = hideNew;
		setDisplayFlags();
	}

	public boolean isHideDeals() {
		return hideDeals;
	}

	public void setHideDeals(boolean hideDeals) {
		this.hideDeals = hideDeals;
		setDisplayFlags();
	}

	public boolean isHideYourFave() {
		return hideYourFave;
	}

	public void setHideYourFave(boolean hideYourFave) {
		this.hideYourFave = hideYourFave;
		setDisplayFlags();
	}
}
