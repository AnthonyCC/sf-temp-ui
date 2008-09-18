package com.freshdirect.smartstore;


import java.util.Set;

import com.freshdirect.cms.ContentKey;

/**
 * Represents session information.
 * 
 * @author istvan
 *
 */
public class SessionInput {
	
	private Set cartContents = null;
	
	private String customerId;
	
	/**
	 * Constructor.
	 * @param customerId the costumer to recommend for (as ERP id)
	 */
	public SessionInput(String customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * Set the cart contents of the user.
	 * @param cartContents (List<@link {@link ContentKey}>)
	 */
	public void setCartContents(Set cartContents) {
		this.cartContents = cartContents;
	}
	
	/**
	 * Get cart contents.
	 * 
	 * If not explicitly set, this method will return null.
	 * 
	 * @return The current cart contents as Collection<@link {@link ContentKey}> 
	 */
	public Set getCartContents() {
		return cartContents;
	}
	
	/**
	 * Get customer id.
	 * @return customer id.
	 */
	public String getCustomerId() {
		return customerId;
	}
	
	
	
	
	
}
