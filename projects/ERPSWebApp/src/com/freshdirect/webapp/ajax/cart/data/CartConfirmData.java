package com.freshdirect.webapp.ajax.cart.data;

import java.util.Map;


public class CartConfirmData {
	
	/**
	 * Product data for this cartline
	 * 
	 * TODO: currently a plain map, as it is merged from a 
	 * ProductConfigResponseData without the skus list
	 * and a ProductConfigResponseData.Sku for the selected sku
	 */
	private Map<String,?> cartLine;
	
	/**
	 * Link back to the category page a.k.a. "Continue Shopping"
	 */
	private String backUrl;

	/**
	 * Link to 'edit item in cart' page
	 */
	private String editUrl;
	
	/**
	 * Cart subtotal - formatted string
	 */
	private String subTotal;

	/**
	 * Cartline total price
	 */
	private String lineTotal;
	
	
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl( String backUrl ) {
		this.backUrl = backUrl;
	}
	public String getEditUrl() {
		return editUrl;
	}
	public void setEditUrl( String editUrl ) {
		this.editUrl = editUrl;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal( String subTotal ) {
		this.subTotal = subTotal;
	}
	public Map<String, ?> getCartLine() {
		return cartLine;
	}
	public void setCartLine( Map<String, ?> cartLine ) {
		this.cartLine = cartLine;
	}
	public String getLineTotal() {
		return lineTotal;
	}
	public void setLineTotal( String lineTotal ) {
		this.lineTotal = lineTotal;
	}
}
