package com.freshdirect.webapp.ajax.backoffice.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryModelResponse implements Serializable {
	
	private String catName;

	private int sideNavPriority = 1;// by default

	private int columnSpan;

//	private HtmlResponse description;
	private String description;

	private boolean topLevelCategory;

	private String departmentId;

	private List<String> privateProducts = new ArrayList<String>();

	private List<String> featuredProducts = new ArrayList<String>();


	private List<String> subcategories = new ArrayList<String>();

	private String ratingHome;

	private boolean active;


	private int showChildren=3;

	private boolean showSelf;

	private boolean useAlternateImages;

	private boolean sideNavBold;

	private boolean sideNavLink;

	private boolean fakeAllFolder;

	private boolean secondaryCategory;

	private boolean featured;

	private boolean hideWineRating;

	private boolean hideFeaturedItems;

	private boolean catMerchantRecommenderRandomizeProducts;

	private boolean hideIfFilteringIsSupported;

	private boolean containsBeer;

	private boolean ssLevelAggregation;

	private boolean disableCategoryYmalRecommender;

	private boolean showSideNav;

	private boolean preferenceCategory;



	private List<String> candidateList;



	private boolean treatAsProduct = true;

	private String materialCharacteristic;

	private String productPromotionType;

	private String catMerchantRecommenderTitle;

	private List<String> wineFilterCriteria;


	private int manualSelectionSlots;

	private String contentTemplatePath;


	private String aliasCategory;

	private List<String> featuredNewProdBrands;

	private boolean dYFAggregated;


	private List<String> popularCategories = new ArrayList<String>();

	private int priority;

	private String aliasAttributeValue;

	private String pageTitle;

	private String seoMetaDescription;


	private boolean showAllByDefault;

	private String redirectUrl;

	private String redirectUrlClean;

	private List<String> rating;

	private int layoutType;

	private boolean favoriteShowPrice;

	private String departmentManagerBio;

	private Integer templateType;

	private boolean ratingBreakOnSubfolders;

	private boolean showRatingRelatedImage;

	private String ratingGroupNames;

	private String ratingGroupLabel;

	private String seasonText;

	private boolean nutritionSort;

	private String defaultGrocerySort;

	private int sideNavShowChildren;

	private int columnNum;

	private boolean hideIphone;

	private String globalMenuTitleLabel;

	private String globalMenuLinkLabel;

	private boolean excludedForEBTPayment;

	private boolean noGroupingByCategory;

	private boolean expandSecondLowestNavigationBox;

	private boolean showPopularCategories;

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public int getSideNavPriority() {
		return sideNavPriority;
	}

	public void setSideNavPriority(int sideNavPriority) {
		this.sideNavPriority = sideNavPriority;
	}

	public int getColumnSpan() {
		return columnSpan;
	}

	public void setColumnSpan(int columnSpan) {
		this.columnSpan = columnSpan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTopLevelCategory() {
		return topLevelCategory;
	}

	public void setTopLevelCategory(boolean topLevelCategory) {
		this.topLevelCategory = topLevelCategory;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public List<String> getPrivateProducts() {
		return privateProducts;
	}

	public void setPrivateProducts(List<String> privateProducts) {
		this.privateProducts = privateProducts;
	}

	public List<String> getFeaturedProducts() {
		return featuredProducts;
	}

	public void setFeaturedProducts(List<String> featuredProducts) {
		this.featuredProducts = featuredProducts;
	}

	public List<String> getSubcategories() {
		return subcategories;
	}

	public void setSubcategories(List<String> subcategories) {
		this.subcategories = subcategories;
	}

	public String getRatingHome() {
		return ratingHome;
	}

	public void setRatingHome(String ratingHome) {
		this.ratingHome = ratingHome;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getShowChildren() {
		return showChildren;
	}

	public void setShowChildren(int showChildren) {
		this.showChildren = showChildren;
	}

	public boolean isShowSelf() {
		return showSelf;
	}

	public void setShowSelf(boolean showSelf) {
		this.showSelf = showSelf;
	}

	public boolean isUseAlternateImages() {
		return useAlternateImages;
	}

	public void setUseAlternateImages(boolean useAlternateImages) {
		this.useAlternateImages = useAlternateImages;
	}

	public boolean isSideNavBold() {
		return sideNavBold;
	}

	public void setSideNavBold(boolean sideNavBold) {
		this.sideNavBold = sideNavBold;
	}

	public boolean isSideNavLink() {
		return sideNavLink;
	}

	public void setSideNavLink(boolean sideNavLink) {
		this.sideNavLink = sideNavLink;
	}

	public boolean isFakeAllFolder() {
		return fakeAllFolder;
	}

	public void setFakeAllFolder(boolean fakeAllFolder) {
		this.fakeAllFolder = fakeAllFolder;
	}

	public boolean isSecondaryCategory() {
		return secondaryCategory;
	}

	public void setSecondaryCategory(boolean secondaryCategory) {
		this.secondaryCategory = secondaryCategory;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public boolean isHideWineRating() {
		return hideWineRating;
	}

	public void setHideWineRating(boolean hideWineRating) {
		this.hideWineRating = hideWineRating;
	}

	public boolean isHideFeaturedItems() {
		return hideFeaturedItems;
	}

	public void setHideFeaturedItems(boolean hideFeaturedItems) {
		this.hideFeaturedItems = hideFeaturedItems;
	}

	public boolean isCatMerchantRecommenderRandomizeProducts() {
		return catMerchantRecommenderRandomizeProducts;
	}

	public void setCatMerchantRecommenderRandomizeProducts(
			boolean catMerchantRecommenderRandomizeProducts) {
		this.catMerchantRecommenderRandomizeProducts = catMerchantRecommenderRandomizeProducts;
	}

	public boolean isHideIfFilteringIsSupported() {
		return hideIfFilteringIsSupported;
	}

	public void setHideIfFilteringIsSupported(boolean hideIfFilteringIsSupported) {
		this.hideIfFilteringIsSupported = hideIfFilteringIsSupported;
	}

	public boolean isContainsBeer() {
		return containsBeer;
	}

	public void setContainsBeer(boolean containsBeer) {
		this.containsBeer = containsBeer;
	}

	public boolean isSsLevelAggregation() {
		return ssLevelAggregation;
	}

	public void setSsLevelAggregation(boolean ssLevelAggregation) {
		this.ssLevelAggregation = ssLevelAggregation;
	}

	public boolean isDisableCategoryYmalRecommender() {
		return disableCategoryYmalRecommender;
	}

	public void setDisableCategoryYmalRecommender(
			boolean disableCategoryYmalRecommender) {
		this.disableCategoryYmalRecommender = disableCategoryYmalRecommender;
	}

	public boolean isShowSideNav() {
		return showSideNav;
	}

	public void setShowSideNav(boolean showSideNav) {
		this.showSideNav = showSideNav;
	}

	public boolean isPreferenceCategory() {
		return preferenceCategory;
	}

	public void setPreferenceCategory(boolean preferenceCategory) {
		this.preferenceCategory = preferenceCategory;
	}

	public List<String> getCandidateList() {
		return candidateList;
	}

	public void setCandidateList(List<String> candidateList) {
		this.candidateList = candidateList;
	}

	public boolean isTreatAsProduct() {
		return treatAsProduct;
	}

	public void setTreatAsProduct(boolean treatAsProduct) {
		this.treatAsProduct = treatAsProduct;
	}

	public String getMaterialCharacteristic() {
		return materialCharacteristic;
	}

	public void setMaterialCharacteristic(String materialCharacteristic) {
		this.materialCharacteristic = materialCharacteristic;
	}

	public String getProductPromotionType() {
		return productPromotionType;
	}

	public void setProductPromotionType(String productPromotionType) {
		this.productPromotionType = productPromotionType;
	}

	public String getCatMerchantRecommenderTitle() {
		return catMerchantRecommenderTitle;
	}

	public void setCatMerchantRecommenderTitle(String catMerchantRecommenderTitle) {
		this.catMerchantRecommenderTitle = catMerchantRecommenderTitle;
	}

	public List<String> getWineFilterCriteria() {
		return wineFilterCriteria;
	}

	public void setWineFilterCriteria(List<String> wineFilterCriteria) {
		this.wineFilterCriteria = wineFilterCriteria;
	}

	public int getManualSelectionSlots() {
		return manualSelectionSlots;
	}

	public void setManualSelectionSlots(int manualSelectionSlots) {
		this.manualSelectionSlots = manualSelectionSlots;
	}

	public String getContentTemplatePath() {
		return contentTemplatePath;
	}

	public void setContentTemplatePath(String contentTemplatePath) {
		this.contentTemplatePath = contentTemplatePath;
	}

	public String getAliasCategory() {
		return aliasCategory;
	}

	public void setAliasCategory(String aliasCategory) {
		this.aliasCategory = aliasCategory;
	}

	public List<String> getFeaturedNewProdBrands() {
		return featuredNewProdBrands;
	}

	public void setFeaturedNewProdBrands(List<String> featuredNewProdBrands) {
		this.featuredNewProdBrands = featuredNewProdBrands;
	}

	public boolean isdYFAggregated() {
		return dYFAggregated;
	}

	public void setdYFAggregated(boolean dYFAggregated) {
		this.dYFAggregated = dYFAggregated;
	}

	public List<String> getPopularCategories() {
		return popularCategories;
	}

	public void setPopularCategories(List<String> popularCategories) {
		this.popularCategories = popularCategories;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getAliasAttributeValue() {
		return aliasAttributeValue;
	}

	public void setAliasAttributeValue(String aliasAttributeValue) {
		this.aliasAttributeValue = aliasAttributeValue;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getSeoMetaDescription() {
		return seoMetaDescription;
	}

	public void setSeoMetaDescription(String seoMetaDescription) {
		this.seoMetaDescription = seoMetaDescription;
	}

	public boolean isShowAllByDefault() {
		return showAllByDefault;
	}

	public void setShowAllByDefault(boolean showAllByDefault) {
		this.showAllByDefault = showAllByDefault;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getRedirectUrlClean() {
		return redirectUrlClean;
	}

	public void setRedirectUrlClean(String redirectUrlClean) {
		this.redirectUrlClean = redirectUrlClean;
	}

	public List<String> getRating() {
		return rating;
	}

	public void setRating(List<String> rating) {
		this.rating = rating;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

	public boolean isFavoriteShowPrice() {
		return favoriteShowPrice;
	}

	public void setFavoriteShowPrice(boolean favoriteShowPrice) {
		this.favoriteShowPrice = favoriteShowPrice;
	}

	public String getDepartmentManagerBio() {
		return departmentManagerBio;
	}

	public void setDepartmentManagerBio(String departmentManagerBio) {
		this.departmentManagerBio = departmentManagerBio;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public boolean isRatingBreakOnSubfolders() {
		return ratingBreakOnSubfolders;
	}

	public void setRatingBreakOnSubfolders(boolean ratingBreakOnSubfolders) {
		this.ratingBreakOnSubfolders = ratingBreakOnSubfolders;
	}

	public boolean isShowRatingRelatedImage() {
		return showRatingRelatedImage;
	}

	public void setShowRatingRelatedImage(boolean showRatingRelatedImage) {
		this.showRatingRelatedImage = showRatingRelatedImage;
	}

	public String getRatingGroupNames() {
		return ratingGroupNames;
	}

	public void setRatingGroupNames(String ratingGroupNames) {
		this.ratingGroupNames = ratingGroupNames;
	}

	public String getRatingGroupLabel() {
		return ratingGroupLabel;
	}

	public void setRatingGroupLabel(String ratingGroupLabel) {
		this.ratingGroupLabel = ratingGroupLabel;
	}

	public String getSeasonText() {
		return seasonText;
	}

	public void setSeasonText(String seasonText) {
		this.seasonText = seasonText;
	}

	public boolean isNutritionSort() {
		return nutritionSort;
	}

	public void setNutritionSort(boolean nutritionSort) {
		this.nutritionSort = nutritionSort;
	}

	public String getDefaultGrocerySort() {
		return defaultGrocerySort;
	}

	public void setDefaultGrocerySort(String defaultGrocerySort) {
		this.defaultGrocerySort = defaultGrocerySort;
	}

	public int getSideNavShowChildren() {
		return sideNavShowChildren;
	}

	public void setSideNavShowChildren(int sideNavShowChildren) {
		this.sideNavShowChildren = sideNavShowChildren;
	}

	public int getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}

	public boolean isHideIphone() {
		return hideIphone;
	}

	public void setHideIphone(boolean hideIphone) {
		this.hideIphone = hideIphone;
	}

	public String getGlobalMenuTitleLabel() {
		return globalMenuTitleLabel;
	}

	public void setGlobalMenuTitleLabel(String globalMenuTitleLabel) {
		this.globalMenuTitleLabel = globalMenuTitleLabel;
	}

	public String getGlobalMenuLinkLabel() {
		return globalMenuLinkLabel;
	}

	public void setGlobalMenuLinkLabel(String globalMenuLinkLabel) {
		this.globalMenuLinkLabel = globalMenuLinkLabel;
	}

	public boolean isExcludedForEBTPayment() {
		return excludedForEBTPayment;
	}

	public void setExcludedForEBTPayment(boolean excludedForEBTPayment) {
		this.excludedForEBTPayment = excludedForEBTPayment;
	}

	public boolean isNoGroupingByCategory() {
		return noGroupingByCategory;
	}

	public void setNoGroupingByCategory(boolean noGroupingByCategory) {
		this.noGroupingByCategory = noGroupingByCategory;
	}

	public boolean isExpandSecondLowestNavigationBox() {
		return expandSecondLowestNavigationBox;
	}

	public void setExpandSecondLowestNavigationBox(
			boolean expandSecondLowestNavigationBox) {
		this.expandSecondLowestNavigationBox = expandSecondLowestNavigationBox;
	}

	public boolean isShowPopularCategories() {
		return showPopularCategories;
	}

	public void setShowPopularCategories(boolean showPopularCategories) {
		this.showPopularCategories = showPopularCategories;
	}

	public List<String> getFeaturedProductIds() {
		return featuredProductIds;
	}

	public void setFeaturedProductIds(List<String> featuredProductIds) {
		this.featuredProductIds = featuredProductIds;
	}

	public String getFirstAvailableFeaturedProduct() {
		return firstAvailableFeaturedProduct;
	}

	public void setFirstAvailableFeaturedProduct(
			String firstAvailableFeaturedProduct) {
		this.firstAvailableFeaturedProduct = firstAvailableFeaturedProduct;
	}

	public boolean isHavingBeer() {
		return havingBeer;
	}

	public void setHavingBeer(boolean havingBeer) {
		this.havingBeer = havingBeer;
	}

	public boolean isHideWineRatingPricing() {
		return hideWineRatingPricing;
	}

	public void setHideWineRatingPricing(boolean hideWineRatingPricing) {
		this.hideWineRatingPricing = hideWineRatingPricing;
	}

	public boolean isHasParentWithName() {
		return hasParentWithName;
	}

	public void setHasParentWithName(boolean hasParentWithName) {
		this.hasParentWithName = hasParentWithName;
	}

	public String getBrandFilterLocation() {
		return brandFilterLocation;
	}

	public void setBrandFilterLocation(String brandFilterLocation) {
		this.brandFilterLocation = brandFilterLocation;
	}

	public Set<String> getAllBrands() {
		return allBrands;
	}

	public void setAllBrands(Set<String> allBrands) {
		this.allBrands = allBrands;
	}

	public boolean isDisplayable() {
		return displayable;
	}

	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

	private List<String> featuredProductIds = new ArrayList<String>();

	private String firstAvailableFeaturedProduct;

	private boolean havingBeer;

	private boolean hideWineRatingPricing;

	private boolean hasParentWithName;


	private String brandFilterLocation;

	private Set<String> allBrands = new HashSet<String>();

	private boolean displayable;

}
