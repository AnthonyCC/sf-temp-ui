package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
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
	private static final long serialVersionUID = -6185578899126874826L;

	public static class LabelAndLink implements Serializable {
		private static final long serialVersionUID = 7701246849203062432L;

		public LabelAndLink() {
		}

		public LabelAndLink(TitledMedia tm) {
			this.label = tm.getMediaTitle();
			this.link = tm.getPath();
		}

		public String label;
		public String link;
	}
	
	
	/**
	 * List of allergens  (Nutrition)
	 * @see allergens.jspf
	 */
	protected List<String> allergens;

	/**
	 * Product description
	 */
	protected String productDescription;
	
	/**
	 * Path to product description note media asset (inline media)
	 */
	protected String productDescriptionNote;

	/**
	 * Path to product about box media asset (inline media)
	 */
	protected String productAboutMedia;

	/**
	 * List of organic claims (Nutrition)
	 * @see organic_claims.jspf
	 */
	protected List<String> organicClaims;

	/**
	 * List of claims (Nutrition)
	 * @see claims.jspf
	 */
	protected List<String> claims;

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
	
	protected String kosherIconPath;
	
	protected String kosherPopupPath;
	
	/**
	 * Heating instructions (Nutrition)
	 * @see i_heating_instructions.jspf
	 */
	protected String heatingInstructions;
	
	/**
	 * Partially frozen meal - seafood
	 * Mutually exclusive with {@link ProductExtraData#isFrozenBakery}
	 */
	protected boolean isFrozenSeafood;

	/**
	 * Partially frozen meal - bakery  
	 * Mutually exclusive with {@link ProductExtraData#isFrozenSeafood}
	 */
	protected boolean isFrozenBakery;
	
	protected String partiallyFrozenMedia;
	
	/**
	 * Origin aka COOL (USA, Ireland, USA, Chile and/or Argentina, etc.)
	 */
	 protected String origin;
	
	/**
	 * Season Text
	 * May contain escaped HTML tags!
	 */
	protected String seasonText;

	public static final String KEY_CT_PINT05 = "halfPint";
	public static final String KEY_CT_PINT = "pint";
	public static final String KEY_CT_QUART = "quart";

	/**
	 * Deli Buyer Guide data.
	 * @see i_buy_guide.jspf
	 */
	protected Map<String, Double> buyerGuide;
	
	/**
	 * Show Cheese 101 popup if true
	 * @see i_cheese_101.jspf
	 */
	protected boolean isCheese101;
	
	@Deprecated
	protected String cheeseText;
	
	@Deprecated
	protected String cheesePopupPath;

	
	/**
	 * Storage Guide popup media - department and category level
	 */
	 protected String storageGuideCat;
	 protected String storageGuideDept;
	 protected String storageGuideTitle;
	
	/**
	 * Simple, flat data class to represent popup media content
	 * @author segabor
	 */
	public static class PopupContent implements Serializable {
		private static final long serialVersionUID = 3892055956103007962L;
		
		public String popupTitle;
		public String popupSize;
		public int popupWidth;
		public int popupHeight;
	}

	public static class BrandInfo implements Serializable {
		@JsonIgnore
		private static final long serialVersionUID = -102703280930094535L;

		public String id;
		public String name;
		public String alt;

		public String logoPath;
		public int logoWidth;
		public int logoHeight;
		
		public String contentPath;
	}

	/**
	 * Brands 
	 */
	protected List<BrandInfo> brands;

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
	 * List of links to Doneness Guide popup contents (inline media)
	 * @see i_doneness_guide.jspf
	 */
	// 
	protected List<LabelAndLink> donenessGuideList;
	
	/**
	 * 
	 * @see i_how_to_cook.jspf
	 */
	// ?? TBD INLINE MEDIA HERE
	protected List<WebHowToCookIt> howToCookItList;
	
	/**
	 * Fresh Tips
	 */
	protected LabelAndLink freshTips;

	
	/**
	 * Product Rating
	 * @see i_product_methods.jsp#getProdPageRatings()
	 */
	protected WebProductRating webRating;
	
	/**
	 * Usage List
	 * 
	 */
	protected List<String> usageList;
	
	/**
	 * Storage Guide (inline media)
	 */
	@Deprecated
	protected String categoryStorageGuide;

	protected String categoryStorageGuideLabel;
	
	protected String categoryStorageGuideLink;


	/**
	 * Ingredients description
	 * NOTE: this might be part of {@link ProductExtraData#nutritions} data
	 */
	protected String ingredients;


	/**
	 * Serving Suggestions
	 * @see i_serving_suggestions.jspf
	 */
	protected String servingSuggestions;

	
	public static class SourceData implements Serializable {
		private static final long serialVersionUID = -2472124530626514660L;

		public String label;
		// optional URL pointing to popup media content
		public String path;
	}
	
	/**
	 * Source map
	 */
	protected Map<String,SourceData> source;
	
	/**
	 * Shallow copy of recipe
	 * @author segabor
	 */
	public static class RecipeData implements Serializable {
		private static final long serialVersionUID = -6095242478417842844L;

		/**
		 * Recipe ID
		 */
		public String id;
		
		/**
		 * Category ID possibly of product that holds recipes
		 */
		public String catId;
		
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

	/**
	 * List of Related Recipes
	 * @see i_related_recipes.jspf
	 */
	protected List<RecipeData> relatedRecipes;
	

	/**
	 * Rating data potato
	 * @see i_wine_rating.jspf
	 */
	public static class WineRating implements Serializable {
		private static final long serialVersionUID = -656662279762691008L;

		/**
		 * Name of reviewer
		 */
		public String reviewer;

		/**
		 * String containing number between 0..100
		 */
		public String rating;

		public String ratingKey;

		/**
		 * Direct path to rating icon
		 * Formula "/media/editorial/win_usq/icons/rating/<ratingKey>.gif"
		 */
		public String iconPath;

		/**
		 * Review (inline media)
		 */
		public String media;

	}


	public static class WineData implements Serializable {
		private static final long serialVersionUID = -6606352733622906907L;

		public List<String> types;
		public List<String> typeIconPaths;
		public List<String> varietals;
		public String country;
		public String region;
		public String city;
		public String vintage;
		public String grape;
		public String classification;
		public String importer;
		public String agingNotes;
		public String alcoholGrade;
		
		public List<WineRating> ratings;
	}
	
	/**
	 * Wine details
	 */
	protected WineData wineData;


	/**
	 * Guaranteed Freshness (Perishable Product only)
	 * Number of days of guaranteed freshness (if value > 0)
	 * 
	 * Zero or negative value is considered to be disregarded.
	 */
	protected int freshness = 0;
	

	/**
	 * Messaging - various messages
	 */
	protected String msgCancellation;
	protected String msgCutoffHeader;
	protected String msgCutoffNotice;
	protected String msgDayOfWeek;
	protected String msgDayOfWeekHeader;
	protected String msgDeliveryNote;
	protected String msgLeadTime;
	protected String msgLeadTimeHeader;
	protected String msgKosherRestriction;
	
	// Earliest availability message (optional)
	// it contains a date in short format Fri, 02/14
	protected String msgEarliestAvailability;
	
	public List<String> getAllergens() {
		return allergens;
	}

	public void setAllergens(List<String> allergens) {
		this.allergens = allergens;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductDescriptionNote(String productDescriptionNote) {
		this.productDescriptionNote = productDescriptionNote;
	}
	
	public String getProductDescriptionNote() {
		return productDescriptionNote;
	}

	public String getProductAboutMedia() {
		return productAboutMedia;
	}

	public void setProductAboutMedia(String productAboutMedia) {
		this.productAboutMedia = productAboutMedia;
	}

	public List<String> getOrganicClaims() {
		return organicClaims;
	}

	public void setOrganicClaims(List<String> organicClaims) {
		this.organicClaims = organicClaims;
	}

	public List<String> getClaims() {
		return claims;
	}

	public void setClaims(List<String> claims) {
		this.claims = claims;
	}

	public String getKosherSymbol() {
		return kosherSymbol;
	}

	public void setKosherSymbol(String kosherSymbol) {
		this.kosherSymbol = kosherSymbol;
	}

	public String getKosherType() {
		return kosherType;
	}

	public void setKosherType(String kosherType) {
		this.kosherType = kosherType;
	}

	public String getKosherIconPath() {
		return kosherIconPath;
	}

	public void setKosherIconPath(String kosherIconPath) {
		this.kosherIconPath = kosherIconPath;
	}

	public String getKosherPopupPath() {
		return kosherPopupPath;
	}

	public void setKosherPopupPath(String kosherPopupPath) {
		this.kosherPopupPath = kosherPopupPath;
	}

	public String getHeatingInstructions() {
		return heatingInstructions;
	}

	public void setHeatingInstructions(String heatingInstructions) {
		this.heatingInstructions = heatingInstructions;
	}

	public boolean isFrozenSeafood() {
		return isFrozenSeafood;
	}

	public void setFrozenSeafood(boolean isFrozenSeafood) {
		this.isFrozenSeafood = isFrozenSeafood;
	}

	public boolean isFrozenBakery() {
		return isFrozenBakery;
	}

	public void setFrozenBakery(boolean isFrozenBakery) {
		this.isFrozenBakery = isFrozenBakery;
	}

	public String getPartiallyFrozenMedia() {
		return partiallyFrozenMedia;
	}

	public void setPartiallyFrozenMedia(String partiallyFrozenMedia) {
		this.partiallyFrozenMedia = partiallyFrozenMedia;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getSeasonText() {
		return seasonText;
	}

	public void setSeasonText(String seasonText) {
		this.seasonText = seasonText;
	}

	public Map<String, Double> getBuyerGuide() {
		return buyerGuide;
	}

	public void setBuyerGuide(Map<String, Double> buyerGuide) {
		this.buyerGuide = buyerGuide;
	}

	public boolean isCheese101() {
		return isCheese101;
	}

	public void setCheese101(boolean isCheese101) {
		this.isCheese101 = isCheese101;
	}

	public String getCheeseText() {
		return cheeseText;
	}

	public void setCheeseText(String cheeseText) {
		this.cheeseText = cheeseText;
	}

	public String getCheesePopupPath() {
		return cheesePopupPath;
	}

	public void setCheesePopupPath(String cheesePopupPath) {
		this.cheesePopupPath = cheesePopupPath;
	}

	public List<BrandInfo> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandInfo> brands) {
		this.brands = brands;
	}

	public NutritionPanel getNutritionPanel() {
		return nutritionPanel;
	}

	public void setNutritionPanel(NutritionPanel nutrition) {
		this.nutritionPanel = nutrition;
	}

	public String getOldNutritionPanel() {
		return oldNutritionPanel;
	}

	public void setOldNutritionPanel(String oldNutritionPanel) {
		this.oldNutritionPanel = oldNutritionPanel;
	}

	public List<LabelAndLink> getDonenessGuideList() {
		return donenessGuideList;
	}

	public void setDonenessGuideList(List<LabelAndLink> donenessGuides) {
		this.donenessGuideList = donenessGuides;
	}

	public List<WebHowToCookIt> getHowToCookItList() {
		return howToCookItList;
	}

	public void setHowToCookItList(List<WebHowToCookIt> howToCookItList) {
		this.howToCookItList = howToCookItList;
	}

	public LabelAndLink getFreshTips() {
		return freshTips;
	}

	public void setFreshTips(LabelAndLink freshTips) {
		this.freshTips = freshTips;
	}

	public WebProductRating getWebRating() {
		return webRating;
	}

	public void setWebRating(WebProductRating rating) {
		this.webRating = rating;
	}
	
	public List<String> getUsageList() {
		return usageList;
	}

	public void setUsageList(List<String> usageList) {
		this.usageList = usageList;
	}

	public String getCategoryStorageGuide() {
		return categoryStorageGuide;
	}

	public void setCategoryStorageGuide(String categoryStorageGuide) {
		this.categoryStorageGuide = categoryStorageGuide;
	}

	public String getCategoryStorageGuideLabel() {
		return categoryStorageGuideLabel;
	}

	public void setCategoryStorageGuideLabel(String categoryStorageGuideLabel) {
		this.categoryStorageGuideLabel = categoryStorageGuideLabel;
	}

	public String getCategoryStorageGuideLink() {
		return categoryStorageGuideLink;
	}

	public void setCategoryStorageGuideLink(String categoryStorageGuideLink) {
		this.categoryStorageGuideLink = categoryStorageGuideLink;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getServingSuggestions() {
		return servingSuggestions;
	}

	public void setServingSuggestions(String servingSuggestions) {
		this.servingSuggestions = servingSuggestions;
	}

	public Map<String, SourceData> getSource() {
		return source;
	}

	public void setSource(Map<String, SourceData> origins) {
		this.source = origins;
	}

	public List<RecipeData> getRelatedRecipes() {
		return relatedRecipes;
	}

	public void setRelatedRecipes(List<RecipeData> relatedRecipes) {
		this.relatedRecipes = relatedRecipes;
	}

	public WineData getWineData() {
		return wineData;
	}

	public void setWineData(WineData wineData) {
		this.wineData = wineData;
	}

	
	public String getStorageGuideCat() {
		return storageGuideCat;
	}

	
	public void setStorageGuideCat( String storageGuideCat ) {
		this.storageGuideCat = storageGuideCat;
	}

	
	public String getStorageGuideDept() {
		return storageGuideDept;
	}

	
	public void setStorageGuideDept( String storageGuideDept ) {
		this.storageGuideDept = storageGuideDept;
	}

	
	public String getStorageGuideTitle() {
		return storageGuideTitle;
	}

	
	public void setStorageGuideTitle( String storageGuideTitle ) {
		this.storageGuideTitle = storageGuideTitle;
	}

	
	public String getMsgCutoffNotice() {
		return msgCutoffNotice;
	}

	
	public void setMsgCutoffNotice( String msgCutoffNotice ) {
		this.msgCutoffNotice = msgCutoffNotice;
	}

	
	public String getMsgCancellation() {
		return msgCancellation;
	}

	
	public void setMsgCancellation( String msgCancellation ) {
		this.msgCancellation = msgCancellation;
	}

	
	public String getMsgDayOfWeek() {
		return msgDayOfWeek;
	}

	
	public void setMsgDayOfWeek( String msgDayOfWeek ) {
		this.msgDayOfWeek = msgDayOfWeek;
	}

	
	public String getMsgDeliveryNote() {
		return msgDeliveryNote;
	}

	
	public void setMsgDeliveryNote( String msgDeliveryNote ) {
		this.msgDeliveryNote = msgDeliveryNote;
	}

	
	public String getMsgCutoffHeader() {
		return msgCutoffHeader;
	}

	
	public void setMsgCutoffHeader( String msgCutoffHeader ) {
		this.msgCutoffHeader = msgCutoffHeader;
	}

	
	public String getMsgLeadTime() {
		return msgLeadTime;
	}

	
	public void setMsgLeadTime( String msgLeadTime ) {
		this.msgLeadTime = msgLeadTime;
	}

	
	public String getMsgDayOfWeekHeader() {
		return msgDayOfWeekHeader;
	}


	public void setMsgDayOfWeekHeader( String msgDayOfWeekHeader ) {
		this.msgDayOfWeekHeader = msgDayOfWeekHeader;
	}

	
	public String getMsgLeadTimeHeader() {
		return msgLeadTimeHeader;
	}

	
	public void setMsgLeadTimeHeader( String msgLeadTimeHeader ) {
		this.msgLeadTimeHeader = msgLeadTimeHeader;
	}

	public int getFreshness() {
		return freshness;
	}

	public void setFreshness(int freshness) {
		this.freshness = freshness;
	}

	public String getMsgKosherRestriction() {
		return msgKosherRestriction;
	}

	
	public void setMsgKosherRestriction( String msgKosherRestriction ) {
		this.msgKosherRestriction = msgKosherRestriction;
	}

	public String getMsgEarliestAvailability() {
		return msgEarliestAvailability;
	}
	
	public void setMsgEarliestAvailability(String msgEarliestAvailability) {
		this.msgEarliestAvailability = msgEarliestAvailability;
	}
}
