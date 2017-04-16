package com.freshdirect.webapp.ajax.quickshop.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.StarterList;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.framework.util.NVL;

public class QuickShopLineItemWrapper implements ContentNodeModel {
	
	private QuickShopLineItem item;
	private ProductModelPricingAdapter product;
	
	private Date deliveryDate;
	private boolean inLastOrder;
	private String orderId;
	private String cclId;
	private String recipeId;
	private String listName;
	private boolean recipeAlive;
	private String recipeName;
	private StarterList starterList;
	private String orderStatus;
	
	private Float userScore = 0f;

	public QuickShopLineItemWrapper(QuickShopLineItem item, ProductModelPricingAdapter product) {
		this.item = item;
		this.product = product;
	}
	
	public final static Comparator<QuickShopLineItemWrapper> FULL_NAME_PRODUCT_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {
		@Override
		public int compare(QuickShopLineItemWrapper p1, QuickShopLineItemWrapper p2) {
			
			//put unavailable items to the end of the list
			if(!p1.getItem().isAvailable()){
				return 1;
			}
			if(!p2.getItem().isAvailable()){
				return -1;
			}
			
			String name1 = NVL.apply(p1.getProduct().getFullName().toLowerCase(), "");
			String name2 = NVL.apply(p2.getProduct().getFullName().toLowerCase(), "");

			int d = name1.compareTo(name2);
			if (d != 0)
				return d;
			
			return p1.getProduct().getContentKey().getId().compareTo(p2.getProduct().getContentKey().getId());
		}
	};
	
	public final static Comparator<QuickShopLineItemWrapper> FREQUENCY_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {

		@Override
		public int compare(QuickShopLineItemWrapper h1, QuickShopLineItemWrapper h2) {
			
			//put unavailable items to the end of the list
			if(!h1.getItem().isAvailable() && !h2.getItem().isAvailable()){
				return 0;
			}
			if(!h1.getItem().isAvailable()){
				return 1;
			}
			if(!h2.getItem().isAvailable()){
				return -1;
			}
			
			double retValue = h2.getItem().getFrequency() - h1.getItem().getFrequency();
			return (int)retValue;
		}
	};
	
	public final static Comparator<QuickShopLineItemWrapper> RECENT_PURCHASE_COMPARATOR_DESC = new Comparator<QuickShopLineItemWrapper>() {

		@Override
		public int compare(QuickShopLineItemWrapper h1, QuickShopLineItemWrapper h2) {
			
			//put unavailable items to the end of the list
			if(!h1.getItem().isAvailable()){
				return 1;
			}
			if(!h2.getItem().isAvailable()){
				return -1;
			}
			
			double retValue = h2.getItem().getRecency() - h1.getItem().getRecency();
			return (int)retValue;
		}
	};
	
	// Expert rating comparator uses both expert and wine ratings!
	public final static Comparator<QuickShopLineItemWrapper> EXPERT_RATING_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {

		@Override
		public int compare(QuickShopLineItemWrapper h1, QuickShopLineItemWrapper h2) {
			
			//put unavailable items to the end of the list
			if(!h1.getItem().isAvailable()){
				return 1;
			}
			if(!h2.getItem().isAvailable()){
				return -1;
			}
			
			int x1 = h1.getItem().getExpertRating();
			int x2 = h2.getItem().getExpertRating();
			int w1 = h1.getItem().getWineRating();
			int w2 = h2.getItem().getWineRating();
			Integer v1 = w1 > 0 ? w1 : x1;
			Integer v2 = w2 > 0 ? w2 : x2;
			return 	v2.compareTo(v1);
		}
	};
	
	public final static Comparator<QuickShopLineItemWrapper> FAVOURITES_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {

		@Override
		public int compare(QuickShopLineItemWrapper h1, QuickShopLineItemWrapper h2) {
			
			//put unavailable items to the end of the list
			if(!h1.getItem().isAvailable()){
				return 1;
			}
			if(!h2.getItem().isAvailable()){
				return -1;
			}
			
			Float h1f = h1.getUserScore()==null || h1.getUserScore()==0 ? 0f : 1f;
			Float h2f = h2.getUserScore()==null || h2.getUserScore()==0  ? 0f : 1f;
			
			return h2f.compareTo(h1f);
		}
	};

	public QuickShopLineItem getItem() {
		return item;
	}

	public void setItem(QuickShopLineItem item) {
		this.item = item;
	}

	public ProductModelPricingAdapter getProduct() {
		return product;
	}

	public void setProduct(ProductModelPricingAdapter product) {
		this.product = product;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public boolean isInLastOrder() {
		return inLastOrder;
	}

	public void setInLastOrder(boolean inLastOrder) {
		this.inLastOrder = inLastOrder;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCclId() {
		return cclId;
	}

	public void setCclId(String cclId) {
		this.cclId = cclId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public StarterList getStarterList() {
		return starterList;
	}
	
	public void setStarterList(StarterList starterList) {
		this.starterList = starterList;
	}
	
	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public Float getUserScore() {
		return userScore;
	}

	public void setUserScore(Float userScore) {
		this.userScore = userScore;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public boolean isRecipeAlive() {
		return recipeAlive;
	}

	public void setRecipeAlive(boolean recipeAlive) {
		this.recipeAlive = recipeAlive;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public ContentKey getContentKey() {
		return null;
	}

	@Override
	public String getContentName() {
		return null;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public Object getCmsAttributeValue(String name) {
		return null;
	}

	@Override
	public ContentNodeModel getParentNode() {
		return null;
	}

	@Override
	public boolean hasParentWithName(String[] contentNames) {
		return false;
	}

	@Override
	public String getParentId() {
		return null;
	}

	@Override
	public Object getNotInheritedAttributeValue(String name) {
		return null;
	}

	@Override
	public AttributeDefI getAttributeDef(String name) {
		return null;
	}

	@Override
	public String getAltText() {
		return null;
	}

	@Override
	public String getBlurb() {
		return null;
	}

	@Override
	public Html getEditorial() {
		return null;
	}

	@Override
	public String getEditorialTitle() {
		return null;
	}

	@Override
	public String getFullName() {
		return null;
	}

	@Override
	public String getGlanceName() {
		return null;
	}

	@Override
	public String getNavName() {
		return null;
	}

	@Override
	public String getKeywords() {
		return null;
	}

	@Override
	public boolean isSearchable() {
		return false;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public String getHideUrl() {
		return null;
	}

	@Override
	public Image getSideNavImage() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public boolean isOrphan() {
		return false;
	}

	@Override
	public Collection<ContentKey> getParentKeys() {
		return null;
	}

}
