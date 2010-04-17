package com.freshdirect.smartstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;

/**
 * Represents session information.
 * 
 * It's not serializable ! BY DESIGN !
 * 
 * @author istvan
 * 
 */
public class SessionInput {

	private Set<ContentKey> cartContents = null;

	private String customerId;

	private ContentNodeModel currentNode; // used by FI recommenders

	private boolean noShuffle;

	private CategoryModel category;

	private YmalSource ymalSource;

	private List<ContentNodeModel> explicitList;

	private EnumServiceType customerServiceType;

	private FDCartModel cartModel;

	private Map<String, List<ContentKey>> previousRecommendations;

	int maxRecommendations = Integer.MAX_VALUE;

	private boolean checkForEnoughSavingsMode = false;

	private String savingsVariantId;

	private boolean includeCartItems = false;

	private boolean useAlternatives = true;

	// private Set eligiblePromotions = null;
	
	private Set<ContentKey> recentItems;

	//Added for Zone Pricing.
	private PricingContext pricingCtx;
	/**
	 * Constructor.
	 * 
	 * @param customerId
	 *            the custumer to recommend for (as ERP id)
	 * @param customerServiceType
	 * 
	 *            the customer's service type (Home, Corporate, etc.)
	 */
	public SessionInput(String customerId, EnumServiceType customerServiceType, PricingContext pricingCtx) {
		this.customerId = customerId;
		this.customerServiceType = customerServiceType;
		this.pricingCtx = pricingCtx;
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
			this.savingsVariantId = user.getSavingsVariantId();
			if (user.getIdentity() != null)
				this.customerId = user.getIdentity().getErpCustomerPK();
			initCartContents(user);
			initRecentItems(user);
			this.pricingCtx = user.getPricingContext();
		}
	}

	protected void initCartContents(FDUserI user) {
		@SuppressWarnings("unchecked")
		List<FDCartLineI> orderlines = user.getShoppingCart().getOrderLines();
		Set<ContentKey> products = new HashSet<ContentKey>(orderlines.size());
		Map<String, Set<ContentKey>> tempSavingItems = new HashMap<String, Set<ContentKey>>();
		for (FDCartLineI cartLine : orderlines) {
			ProductReference productRef = cartLine.getProductRef();
			products.add(productRef.getContentKey());
			String savingsId = cartLine.getSavingsId();
			if (savingsId != null && savingsId.length() != 0) {
				if (!tempSavingItems.containsKey(savingsId)) {
					tempSavingItems.put(savingsId, new HashSet<ContentKey>());
				}
				tempSavingItems.get(savingsId).add(productRef.getContentKey());
			}
		}
		cartContents = products;
	}

	protected void initRecentItems(FDUserI user) {
		List<FDCartLineI> orderlines = user.getShoppingCart().getRecentOrderLines();
		Set<ContentKey> products = new HashSet<ContentKey>(orderlines.size());
		for (FDCartLineI cartLine : orderlines) {
			ProductReference productRef = cartLine.getProductRef();
			products.add(productRef.getContentKey());
		}
		recentItems = products;
	}
	
	/**
	 * Set the cart contents of the user.
	 * 
	 * @param cartContents
	 *            (List<@link {@link ContentKey}>)
	 */
	public void setCartContents(Set<ContentKey> cartContents) {
		this.cartContents = cartContents;
	}

	/**
	 * Get cart contents.
	 * 
	 * If not explicitly set, this method will return null.
	 * 
	 * @return The current cart contents as Collection<@link {@link ContentKey}>
	 */
	public Set<ContentKey> getCartContents() {
		return cartContents != null ? cartContents : Collections.<ContentKey>emptySet();
	}
	
	public void setRecentItems(Set<ContentKey> recentItems) {
		this.recentItems = recentItems;
	}
	
	public Set<ContentKey> getRecentItems() {
		if (recentItems != null)
			return recentItems;
		else
			return Collections.emptySet();
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
		// return false;
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

	public List<ContentNodeModel> getExplicitList() {
		return explicitList != null ? explicitList : Collections.<ContentNodeModel>emptyList();
	}

	public void setExplicitList(List<ContentNodeModel> explicitList) {
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

	public void setPreviousRecommendations(Map<String, List<ContentKey>> previousRecommendations) {
		this.previousRecommendations = previousRecommendations;
	}

	public Map<String, List<ContentKey>> getPreviousRecommendations() {
		return previousRecommendations;
	}

	public void setMaxRecommendations(int maxRecommendations) {
		this.maxRecommendations = maxRecommendations;
	}

	public int getMaxRecommendations() {
		return maxRecommendations;
	}

	public boolean isCheckForEnoughSavingsMode() {
		return checkForEnoughSavingsMode;
	}

	public void setCheckForEnoughSavingsMode(boolean checkForSavings) {
		this.checkForEnoughSavingsMode = checkForSavings;
	}

	public void setCategory(CategoryModel category) {
		this.category = category;
	}

	public String getSavingsVariantId() {
		return savingsVariantId;
	}

	public CategoryModel getCategory() {
		if ((category == null) && currentNode instanceof CategoryModel) {
			return (CategoryModel) currentNode;
		} else if (currentNode instanceof ProductModel)
			return (CategoryModel) ((ProductModel) currentNode).getParentNode();
		else
			return category;
	}

	public CategoryModel getFICategory() {
		if (currentNode instanceof CategoryModel)
			return (CategoryModel) currentNode;
		else if (currentNode instanceof ProductModel && currentNode.getParentNode() instanceof CategoryModel)
			return (CategoryModel) currentNode.getParentNode();
		else
			return null;
	}

	public boolean isIncludeCartItems() {
		return includeCartItems;
	}

	public void setIncludeCartItems(boolean includeCartItems) {
		this.includeCartItems = includeCartItems;
	}

	public Set<ContentKey> getExclusions() {
		if (includeCartItems) {
			return Collections.emptySet();
		} else {
			return getCartContents();
		}
	}

	public void setUseAlternatives(boolean useAlternatives) {
		this.useAlternatives = useAlternatives;
	}

	public boolean isUseAlternatives() {
		return useAlternatives;
        }

	public PricingContext getPricingContext() {
		return pricingCtx;
	}

	public void setPricingContext(PricingContext pricingCtx) {
		this.pricingCtx = pricingCtx;
	}
}
