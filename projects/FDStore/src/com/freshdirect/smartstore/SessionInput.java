package com.freshdirect.smartstore;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Represents session information.
 * 
 * @author istvan
 * 
 */
public class SessionInput {

	private Set cartContents = null;

	private String customerId;

	private ContentNodeModel currentNode; // used by FI recommenders

	private boolean noShuffle;

	private YmalSource ymalSource;

	private List explicitList;

	private EnumServiceType customerServiceType;

	private FDCartModel cartModel;

	private Map previousRecommendations;

	int maxRecommendations = Integer.MAX_VALUE;
	
	private Map promoVariantMap = null;
	
	//private Set eligiblePromotions = null;

	/**
	 * Constructor.
	 * 
	 * @param customerId
	 *            the custumer to recommend for (as ERP id)
	 * @param customerServiceType
	 *            the customer's service type (Home, Corporate, etc.)
	 */
	public SessionInput(String customerId, EnumServiceType customerServiceType) {
		this.customerId = customerId;
		this.customerServiceType = customerServiceType;
	}
/*
	public Set getEligiblePromotions() {
		return eligiblePromotions;
	}
*/
	public Map getPromoVariantMap() {
		return promoVariantMap;
	}

	/**
	 * Constructor.
	 * 
	 * @param user
	 *            the costumer to recommend for.
	 */
	public SessionInput(FDUserI user) {
		if (user != null) {
			this.customerServiceType = user.getUserServiceType();
			this.cartModel = user.getShoppingCart();
			//Reload PromoVariant Map.
			this.promoVariantMap = user.getPromoVariantMap(true);
			//this.eligiblePromotions = user.getPromotionEligibility().getEligiblePromotionCodes();
			if (user.getIdentity() != null)
				this.customerId = user.getIdentity().getErpCustomerPK();
		}
	}

	/**
	 * Set the cart contents of the user.
	 * 
	 * @param cartContents
	 *            (List<@link {@link ContentKey}>)
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
		return cartContents != null ? cartContents : Collections.EMPTY_SET;
	}

	/**
	 * Get customer id.
	 * 
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

	public YmalSource getYmalSource() {
		return ymalSource;
	}

	public void setYmalSource(YmalSource ymalSource) {
		this.ymalSource = ymalSource;
	}

	public List getExplicitList() {
		return explicitList != null ? explicitList : Collections.EMPTY_LIST;
	}

	public void setExplicitList(List explicitList) {
		this.explicitList = explicitList;
	}

	public void setCustomerServiceType(EnumServiceType customerServiceType) {
		this.customerServiceType = customerServiceType;
	}

	public EnumServiceType getCustomerServiceType() {
		return customerServiceType;
	}

	public void setCartModel(FDCartModel cartModel) {
		this.cartModel = cartModel;
	}

	public FDCartModel getCartModel() {
		return cartModel;
	}

	public void setPreviousRecommendations(Map previousRecommendations) {
		this.previousRecommendations = previousRecommendations;
	}

	public Map getPreviousRecommendations() {
		return previousRecommendations;
	}

	public void setMaxRecommendations(int maxRecommendations) {
		this.maxRecommendations = maxRecommendations;
	}

	public int getMaxRecommendations() {
		return maxRecommendations;
	}

}
