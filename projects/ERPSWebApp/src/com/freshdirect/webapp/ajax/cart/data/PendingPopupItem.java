package com.freshdirect.webapp.ajax.cart.data;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;

public class PendingPopupItem extends AddToCartItem {
	
	private static final Logger LOG = LoggerFactory.getInstance( PendingPopupItem.class );

	private String productName;
	private String productImage;
		
	public PendingPopupItem() {
	}
	
	public PendingPopupItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		populateFromAddToCartItem(source);
	}
	
	public void populateFromAddToCartItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		
		ProductModel pm = null;
		if(source.getSkuCode()!=null){
			pm = ContentFactory.getInstance().getProduct(source.getSkuCode());
		}else{
			LOG.error("Cannot initialize PendingPopupItem from AddToCartItem because of missing skuCode.");
			throw new FDResourceException("Cannot create item");
		}
		
		productName = pm.getFullName();
		productImage = pm.getProdImage().getPathWithPublishId();
		setAtcItemId(source.getAtcItemId());
		setCategoryId(source.getCategoryId());
		setConfiguration(source.getConfiguration());
		setLineId(source.getLineId());
		setListId(source.getListId());
		setProductId(source.getProductId());
		setQuantity(source.getQuantity());
		setRecipeId(source.getRecipeId());
		setSalesUnit(source.getSalesUnit());
		setSkuCode(source.getSkuCode());
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}	
	
}
