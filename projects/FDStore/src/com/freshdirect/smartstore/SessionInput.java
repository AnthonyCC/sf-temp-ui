package com.freshdirect.smartstore;


import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.customer.FDUserI;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Represents session information.
 * 
 * @author istvan
 *
 */
public class SessionInput {
	
	private Set cartContents = null;
	
	private String customerId;
	
	private ContentNodeModel currentNode;
	
	private boolean noShuffle;
	
	/**
         * Constructor.
         * @param customerId the costumer to recommend for (as ERP id)
         */
        public SessionInput(String customerId) {
                this.customerId = customerId;
        }
        
        /**
         * Constructor.
         * @param user the costumer to recommend for.
         */
        public SessionInput(FDUserI user) {
            if (user!=null && user.getIdentity() != null) {
                this.customerId = user.getIdentity().getErpCustomerPK();
            }
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
		return cartContents != null ? cartContents : Collections.emptySet();
	}
	
	/**
	 * Get customer id.
	 * @return customer id.
	 */
	public String getCustomerId() {
		return customerId;
	}
	

	public ContentNodeModel getCurrentNode() {
            return currentNode;
        }
	
	public void setCurrentNode(ContentNodeModel currentNode) {
            this.currentNode = currentNode;
        }

	public boolean isNoShuffle() {
		return noShuffle;
	}

	public void setNoShuffle(boolean noShuffle) {
		this.noShuffle = noShuffle;
	}
}
