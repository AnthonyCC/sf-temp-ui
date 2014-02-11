package com.freshdirect.webapp.ajax.quickshop.data;

import java.util.ArrayList;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.product.data.ProductData;

/**
 * @author szabi
 *
 */
public class QuickShopLineItem extends ProductData {

	private static final long serialVersionUID = -5433625138270100825L;
	
	
	// ============================  Quickshop specific stuff  ============================
	
	/**
	 * The quickshop specific id (composite id) of the item
	 */
	private String itemId;
	
	/**
	 * The id of the list this item belongs to 
	 */
	private String listId;
	
	/**
	 * The original line id (e.g shoppingListLineId) of the item 
	 */
	private String originalLineId;

	
	private QuickShopLineItem replacement;
	private boolean useReplacement = false;
	private QuickShopLineItem tempConfig;
	
	

	

	
	public QuickShopLineItem() {
	}
	
	public QuickShopLineItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		populateFromAddToCartItem(source);
	}
	
	
	private void populateFromAddToCartItem(AddToCartItem source) throws FDSkuNotFoundException, FDResourceException{
		
		ProductModel pm = null;
		if(source.getSkuCode()!=null){
			pm = ContentFactory.getInstance().getProduct(source.getSkuCode());
		}else{
			throw new FDResourceException("Cannot create item");
		}
		
		itemId = source.getAtcItemId();
		productName = pm.getFullName();
		productImage = pm.getProdImage().getPathWithPublishId();
		catId=source.getCategoryId();
		configuration=source.getConfiguration();
		listId=source.getListId();
		productId=source.getProductId();
		
		//quantity
		quantity = new Quantity();
		quantity.setQuantity(Double.parseDouble(source.getQuantity()));
		
		//salesUnit
		salesUnit = new ArrayList<SalesUnit>();
		SalesUnit saleUnit = new SalesUnit();
		saleUnit.setId(source.getSalesUnit());
		salesUnit.add(saleUnit);

		skuCode=source.getSkuCode();
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public QuickShopLineItem getReplacement() {
		return replacement;
	}
	public void setReplacement(QuickShopLineItem replacement) {
		this.replacement = replacement;
	}	
	public boolean isUseReplacement() {
		return useReplacement;
	}	
	public void setUseReplacement( boolean useReplacement ) {
		this.useReplacement = useReplacement;
	}
	public String getOriginalLineId() {
		return originalLineId;
	}
	public void setOriginalLineId(String originalLineId) {
		this.originalLineId = originalLineId;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public QuickShopLineItem getTempConfig() {
		return tempConfig;
	}
	public void setTempConfig(QuickShopLineItem tempConfig) {
		this.tempConfig = tempConfig;
	}
}
