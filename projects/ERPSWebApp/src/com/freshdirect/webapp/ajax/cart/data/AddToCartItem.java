package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AddToCartItem implements Serializable {
	
	private static final long	serialVersionUID	= 3871290125664273306L;
	
	private String atcItemId = null;
	private String listId = null;
	private String lineId = null;
	private String productId = null;
	private String categoryId = null;
	private String variantId = null;
	private String recipeId = null;
	private String skuCode = null;
	private String quantity = null;
	private String salesUnit = null;
	private boolean fromSearch = false;
	private Map<String,String> configuration = new HashMap<String, String>();
	
	private boolean deleteItem = false;
	
	public String getProductId() {
		return productId;
	}		
	public void setProductId( String productId ) {
		this.productId = productId;
	}		
	public String getCategoryId() {
		return categoryId;
	}		
	public void setCategoryId( String categoryId ) {
		this.categoryId = categoryId;
	}
	public String getRecipeId() {
		return recipeId;
	}
	public void setRecipeId( String recipeId ) {
		this.recipeId = recipeId;
	}
	public String getSkuCode() {
		return skuCode;
	}		
	public void setSkuCode( String skuCode ) {
		this.skuCode = skuCode;
	}		
	public String getQuantity() {
		return quantity;
	}		
	public void setQuantity( String quantity ) {
		this.quantity = quantity;
	}		
	public String getSalesUnit() {
		return salesUnit;
	}		
	public void setSalesUnit( String salesUnit ) {
		this.salesUnit = salesUnit;
	}		
	public boolean isFromSearch() {
		return fromSearch;
	}		
	public void setFromSearch( boolean fromSearch ) {
		this.fromSearch = fromSearch;
	}		
	public Map<String,String> getConfiguration() {
		return configuration;
	}		
	public void setConfiguration( Map<String,String> configuration ) {
		this.configuration = configuration;
	}	
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	public boolean isDeleteItem() {
		return deleteItem;
	}
	public void setDeleteItem(boolean deleteItem) {
		this.deleteItem = deleteItem;
	}	
	public String getAtcItemId() {
		return atcItemId;
	}
	public void setAtcItemId(String atcItemId) {
		this.atcItemId = atcItemId;
	}	
	public String getVariantId() {
		return variantId;
	}
	public void setVariantId(String variantId) {
		this.variantId = variantId;
	}
	
	@Override
	public String toString() {
		return "ATCi{" + skuCode + "}";
	}
}