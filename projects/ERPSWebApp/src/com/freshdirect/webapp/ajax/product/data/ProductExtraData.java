package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.fdstore.content.TitledMedia;
import com.freshdirect.fdstore.content.view.WebHowToCookIt;
import com.freshdirect.fdstore.content.view.WebProductRating;


/**
 * POJO that holds product extra data
 * 
 * @author segabor
 *
 */
public class ProductExtraData implements Serializable {
	public static class BrandInfo implements Serializable {
		@JsonIgnore
		private static final long serialVersionUID = -102703280930094535L;

		public String alt;
		public String contentPath;
		public String id;

		public int logoHeight;
		public String logoPath;
		public int logoWidth;
		
		public String name;
	}

	public static class GroupScaleData implements Serializable {
		private static final long serialVersionUID = 1306378437653503330L;	
		public List<ProductData> groupProducts;
		public String grpId;			
		public String grpLongDesc;
		public String grpQty;
		public String grpShortDesc;
		public String grpTotalPrice;			
		public String version;
	}
	
	
	public static class LabelAndLink implements Serializable {
		private static final long serialVersionUID = 7701246849203062432L;

		public String label;

		public String link;

		public LabelAndLink() {
		}
		public LabelAndLink(TitledMedia tm) {
			this.label = tm.getMediaTitle();
			try {
				this.link = "/shared/template/plain_media_include.jsp?contentPath=" + URLEncoder.encode(tm.getPath(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				//Fall-back to original
				this.link = tm.getPath();
			}
		}
	}

	/**
	 * Simple, flat data class to represent popup media content
	 * @author segabor
	 */
	public static class PopupContent implements Serializable {
		private static final long serialVersionUID = 3892055956103007962L;
		
		public int popupHeight;
		public String popupSize;
		public String popupTitle;
		public int popupWidth;
	}
	
	/**
	 * Shallow copy of recipe
	 * @author segabor
	 */
	public static class RecipeData implements Serializable {
		private static final long serialVersionUID = -6095242478417842844L;

		/**
		 * Category ID possibly of product that holds recipes
		 */
		public String catId;
		
		/**
		 * Recipe ID
		 */
		public String id;
		
		/**
		 * Recipe name
		 */
		public String name;

		public RecipeData(String id, String catId, String name) {
			this.id = id;
			this.catId = catId;
			this.name = name;
		}
	}

	public static class SourceData implements Serializable {
		private static final long serialVersionUID = -2472124530626514660L;

		public String label;
		// optional URL pointing to popup media content
		public String path;
	}

    public static class WineData implements Serializable {
		private static final long serialVersionUID = -6606352733622906907L;

		public String agingNotes;
		public String alcoholGrade;
		public String city;
		public String classification;
		public String country;
		public String grape;
		public String importer;
		public List<WineRating> ratings;
		public String region;
		public List<String> typeIconPaths;
		public List<String> types;
		public List<String> varietals;
		
		public String vintage;
	}

    /**
	 * Rating data potato
	 * @see i_wine_rating.jspf
	 */
	public static class WineRating implements Serializable {
		private static final long serialVersionUID = -656662279762691008L;

		/**
		 * Direct path to rating icon
		 * Formula "/media/editorial/win_usq/icons/rating/<ratingKey>.gif"
		 */
		public String iconPath;

		/**
		 * Review (inline media)
		 */
		public String media;

		/**
		 * String containing number between 0..100
		 */
		public String rating;

		public String ratingKey;

		/**
		 * Name of reviewer
		 */
		public String reviewer;

	}

    public static final String KEY_CT_PINT = "pint";

	public static final String KEY_CT_PINT05 = "halfPint";

	public static final String KEY_CT_QUART = "quart";

	private static final long serialVersionUID = -6185578899126874826L;

	/**
	 * List of allergens  (Nutrition)
	 * @see allergens.jspf
	 */
	protected List<String> allergens;
	
	/**
	 * Brands 
	 */
	protected List<BrandInfo> brands;
	
	/**
	 * Deli Buyer Guide data.
	 * @see i_buy_guide.jspf
	 */
	protected Map<String, Double> buyerGuide;
	
	/**
	 * Storage Guide (inline media)
	 */
	@Deprecated
	protected String categoryStorageGuide;
	
	protected String categoryStorageGuideLabel;

	protected String categoryStorageGuideLink;

	@Deprecated
	protected String cheesePopupPath;
	
	@Deprecated
	protected String cheeseText;
	
	/**
	 * List of claims (Nutrition)
	 * @see claims.jspf
	 */
	protected List<String> claims;
	 private String customerServiceContact;
	
	/**
	 * List of links to Doneness Guide popup contents (inline media)
	 * @see i_doneness_guide.jspf
	 */
	// 
	protected List<LabelAndLink> donenessGuideList;

	protected List<ProductData> familyProducts;
	/**
	 * Guaranteed Freshness (Perishable Product only)
	 * Number of days of guaranteed freshness (if value > 0)
	 * 
	 * Zero or negative value is considered to be disregarded.
	 */
	protected int freshness = 0;
	/**
	 * Fresh Tips
	 */
	protected LabelAndLink freshTips;

	protected GroupScaleData groupScaleData;
	
	/**
	 * Heating instructions (Nutrition)
	 * @see i_heating_instructions.jspf
	 */
	protected String heatingInstructions;
	
	/**
	 * 
	 * @see i_how_to_cook.jspf
	 */
	// ?? TBD INLINE MEDIA HERE
	protected List<WebHowToCookIt> howToCookItList;
	
	/**
	 * Ingredients description
	 * NOTE: this might be part of {@link ProductExtraData#nutritions} data
	 */
	protected String ingredients;

	
	/**
	 * Show Cheese 101 popup if true
	 * @see i_cheese_101.jspf
	 */
	protected boolean isCheese101;
	 /**
	 * New generic attribute for anything with a frozen media due to APPBUG-1705
	 */
	protected boolean isFrozen;
	 /**
	 * Partially frozen meal - bakery  
	 * Mutually exclusive with {@link ProductExtraData#isFrozenSeafood}
	 */
	protected boolean isFrozenBakery;
	
	/**
	 * Partially frozen meal - seafood
	 * Mutually exclusive with {@link ProductExtraData#isFrozenBakery}
	 */
	protected boolean isFrozenSeafood;

	protected String kosherIconPath;

	protected String kosherPopupPath;

	/**
	 * Kosher symbol
	 * @see kosher.jspf
	 */
	protected String kosherSymbol;
	

	/**
	 * Kosher type
	 * @see kosher.jspf
	 */
	protected String kosherType;
	
	/**
	 * Collection of SKU nutrition info
	 * A.k.a new-style nutrition panel
	 */
	protected NutritionPanel nutritionPanel;
	
	/**
	 * Old-style nutrition panel (partial content)
	 */
	protected String oldNutritionPanel;
	
	/**
	 * List of organic claims (Nutrition)
	 * @see organic_claims.jspf
	 */
	protected List<String> organicClaims;

	
	protected String origin;
	
	/**
	 * Origin aka COOL (USA, Ireland, USA, Chile and/or Argentina, etc.)
	 */
	 protected String originTitle;
	
	protected String partiallyFrozenMedia;

	/**
	 * Path to product about box media asset (inline media)
	 */
	protected String productAboutMedia;
	
	private String productAboutMediaPath;


	/**
	 * Product description
	 */
	protected String productDescription;


	/**
	 * Path to product description note media asset (inline media)
	 */
	protected String productDescriptionNote;

	
	/**
     * Path to product terms media asset (inline media)
     */
    private String productTermsMedia;
	
	/**
	 * List of Related Recipes
	 * @see i_related_recipes.jspf
	 */
	protected List<RecipeData> relatedRecipes;
	
	/**
	 * Season Text
	 * May contain escaped HTML tags!
	 */
	protected String seasonText;

	/**
	 * Serving Suggestions
	 * @see i_serving_suggestions.jspf
	 */
	protected String servingSuggestions;
	

	/**
	 * Source map
	 */
	protected Map<String,SourceData> source;


	/**
	 * Storage Guide popup media - department and category level
	 */
	 protected String storageGuideCat;
	
	protected String storageGuideDept;


	protected String storageGuideTitle;
	
	
	/* placeholder for product family products */
 	
	/**
	 * Usage List
	 * 
	 */
	protected List<String> usageList;

	/**
	 * Product Rating
	 * @see i_product_methods.jsp#getProdPageRatings()
	 */
	protected WebProductRating webRating;

	/**
	 * Wine details
	 */
	protected WineData wineData;
	
	/* component group meal, optional products */
	
	protected List<ProductData> optionalProducts;
	
	public List<ProductData> getOptionalProducts() {
		return optionalProducts;
	}

	public void setOptionalProducts(List<ProductData> optionalProducts) {
		this.optionalProducts = optionalProducts;
	}

	public List<String> getAllergens() {
		return allergens;
	}

	public List<BrandInfo> getBrands() {
		return brands;
	}

	public Map<String, Double> getBuyerGuide() {
		return buyerGuide;
	}

	public String getCategoryStorageGuide() {
		return categoryStorageGuide;
	}

	public String getCategoryStorageGuideLabel() {
		return categoryStorageGuideLabel;
	}
	
	public String getCategoryStorageGuideLink() {
		return categoryStorageGuideLink;
	}

	public String getCheesePopupPath() {
		return cheesePopupPath;
	}

	public String getCheeseText() {
		return cheeseText;
	}

    public List<String> getClaims() {
		return claims;
	}

    public String getCustomerServiceContact() {
        return customerServiceContact;
    }

    public List<LabelAndLink> getDonenessGuideList() {
		return donenessGuideList;
	}

	public List<ProductData> getFamilyProducts() {
		return familyProducts;
	}

	public int getFreshness() {
		return freshness;
	}

	public LabelAndLink getFreshTips() {
		return freshTips;
	}

	public GroupScaleData getGroupScaleData() {
		return groupScaleData;
	}

	public String getHeatingInstructions() {
		return heatingInstructions;
	}

	public List<WebHowToCookIt> getHowToCookItList() {
		return howToCookItList;
	}

	public String getIngredients() {
		return ingredients;
	}

	public String getKosherIconPath() {
		return kosherIconPath;
	}

	public String getKosherPopupPath() {
		return kosherPopupPath;
	}

	public String getKosherSymbol() {
		return kosherSymbol;
	}

	public String getKosherType() {
		return kosherType;
	}

	public NutritionPanel getNutritionPanel() {
		return nutritionPanel;
	}

	public String getOldNutritionPanel() {
		return oldNutritionPanel;
	}

	public List<String> getOrganicClaims() {
		return organicClaims;
	}

	public String getOrigin() {
		return origin;
	}

	public String getOriginTitle() {
		return originTitle;
	}

	public String getPartiallyFrozenMedia() {
		return partiallyFrozenMedia;
	}

	public String getProductAboutMedia() {
		return productAboutMedia;
	}

	public String getProductAboutMediaPath() {
        return productAboutMediaPath;
    }
	
	public String getProductDescription() {
		return productDescription;
	}

	public String getProductDescriptionNote() {
		return productDescriptionNote;
	}

	public String getProductTermsMedia() {
        return productTermsMedia;
    }
	
	public List<RecipeData> getRelatedRecipes() {
		return relatedRecipes;
	}

	public String getSeasonText() {
		return seasonText;
	}

	public String getServingSuggestions() {
		return servingSuggestions;
	}

	public Map<String, SourceData> getSource() {
		return source;
	}

	public String getStorageGuideCat() {
		return storageGuideCat;
	}

	public String getStorageGuideDept() {
		return storageGuideDept;
	}

	public String getStorageGuideTitle() {
		return storageGuideTitle;
	}

	public List<String> getUsageList() {
		return usageList;
	}

	public WebProductRating getWebRating() {
		return webRating;
	}

	public WineData getWineData() {
		return wineData;
	}

	public boolean isCheese101() {
		return isCheese101;
	}

	public boolean isFrozen() {
		return isFrozen;
	}

	public boolean isFrozenBakery() {
		return isFrozenBakery;
	}

	public boolean isFrozenSeafood() {
		return isFrozenSeafood;
	}

	public void setAllergens(List<String> allergens) {
		this.allergens = allergens;
	}

	public void setBrands(List<BrandInfo> brands) {
		this.brands = brands;
	}

	public void setBuyerGuide(Map<String, Double> buyerGuide) {
		this.buyerGuide = buyerGuide;
	}

	public void setCategoryStorageGuide(String categoryStorageGuide) {
		this.categoryStorageGuide = categoryStorageGuide;
	}

	public void setCategoryStorageGuideLabel(String categoryStorageGuideLabel) {
		this.categoryStorageGuideLabel = categoryStorageGuideLabel;
	}

	public void setCategoryStorageGuideLink(String categoryStorageGuideLink) {
		this.categoryStorageGuideLink = categoryStorageGuideLink;
	}

	public void setCheese101(boolean isCheese101) {
		this.isCheese101 = isCheese101;
	}

	public void setCheesePopupPath(String cheesePopupPath) {
		this.cheesePopupPath = cheesePopupPath;
	}

	public void setCheeseText(String cheeseText) {
		this.cheeseText = cheeseText;
	}

	public void setClaims(List<String> claims) {
		this.claims = claims;
	}

	public void setCustomerServiceContact(String customerServiceContact) {
        this.customerServiceContact = customerServiceContact;
    }
	
	public void setDonenessGuideList(List<LabelAndLink> donenessGuides) {
		this.donenessGuideList = donenessGuides;
	}

	public void setFamilyProducts(List<ProductData> familyProducts) {
		this.familyProducts = familyProducts;
	}

	public void setFreshness(int freshness) {
		this.freshness = freshness;
	}

	public void setFreshTips(LabelAndLink freshTips) {
		this.freshTips = freshTips;
	}

	public void setFrozen( boolean isFrozen ) {
		this.isFrozen = isFrozen;
	}

	public void setFrozenBakery(boolean isFrozenBakery) {
		this.isFrozenBakery = isFrozenBakery;
	}

	public void setFrozenSeafood(boolean isFrozenSeafood) {
		this.isFrozenSeafood = isFrozenSeafood;
	}

	public void setGroupScaleData(GroupScaleData groupScaleData) {
		this.groupScaleData = groupScaleData;
	}

	public void setHeatingInstructions(String heatingInstructions) {
		this.heatingInstructions = heatingInstructions;
	}

	public void setHowToCookItList(List<WebHowToCookIt> howToCookItList) {
		this.howToCookItList = howToCookItList;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public void setKosherIconPath(String kosherIconPath) {
		this.kosherIconPath = kosherIconPath;
	}

	public void setKosherPopupPath(String kosherPopupPath) {
		this.kosherPopupPath = kosherPopupPath;
	}

	public void setKosherSymbol(String kosherSymbol) {
		this.kosherSymbol = kosherSymbol;
	}

	public void setKosherType(String kosherType) {
		this.kosherType = kosherType;
	}

	public void setNutritionPanel(NutritionPanel nutrition) {
		this.nutritionPanel = nutrition;
	}

	public void setOldNutritionPanel(String oldNutritionPanel) {
		this.oldNutritionPanel = oldNutritionPanel;
	}

	public void setOrganicClaims(List<String> organicClaims) {
		this.organicClaims = organicClaims;
	}

	
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	
	public void setOriginTitle(String originTitle) {
		this.originTitle = originTitle;
	}

	
	public void setPartiallyFrozenMedia(String partiallyFrozenMedia) {
		this.partiallyFrozenMedia = partiallyFrozenMedia;
	}

	
	public void setProductAboutMedia(String productAboutMedia) {
		this.productAboutMedia = productAboutMedia;
	}

	
	public void setProductAboutMediaPath(String productAboutMediaPath) {
        this.productAboutMediaPath = productAboutMediaPath;
    }

	
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductDescriptionNote(String productDescriptionNote) {
		this.productDescriptionNote = productDescriptionNote;
	}

	public void setProductTermsMedia(String productTermsMedia) {
        this.productTermsMedia = productTermsMedia;
    }


	public void setRelatedRecipes(List<RecipeData> relatedRecipes) {
		this.relatedRecipes = relatedRecipes;
	}

	
	public void setSeasonText(String seasonText) {
		this.seasonText = seasonText;
	}
	
		/* group scale data */
	
	public void setServingSuggestions(String servingSuggestions) {
		this.servingSuggestions = servingSuggestions;
	}
		
	public void setSource(Map<String, SourceData> origins) {
		this.source = origins;
	}
	
	public void setStorageGuideCat( String storageGuideCat ) {
		this.storageGuideCat = storageGuideCat;
	}
	
	public void setStorageGuideDept( String storageGuideDept ) {
		this.storageGuideDept = storageGuideDept;
	}

    public void setStorageGuideTitle( String storageGuideTitle ) {
		this.storageGuideTitle = storageGuideTitle;
	}

    public void setUsageList(List<String> usageList) {
		this.usageList = usageList;
	}

    public void setWebRating(WebProductRating rating) {
		this.webRating = rating;
	}

    public void setWineData(WineData wineData) {
		this.wineData = wineData;
	}

}
