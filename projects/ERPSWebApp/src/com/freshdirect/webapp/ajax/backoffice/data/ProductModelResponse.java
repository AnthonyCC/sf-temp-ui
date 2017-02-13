package com.freshdirect.webapp.ajax.backoffice.data;

import java.util.ArrayList;
import java.util.List;

public class ProductModelResponse {
	
	private boolean preconfigured;

//	private HtmlResponse productDescription;
	private String productDescription;

	private boolean incrementMaxEnforce;

	private boolean excludedRecommendation;

	private int templateType;

	
	private boolean soldBySalesUnits;

	private String skuModel;

	private boolean perishable;

	private String ymalHeader;

	private String seasonText;

	
	private int specialLayout;


	private String parentCategory;

	private boolean frozen;

	private String alsoSoldAsName;

	private String aka;

	private boolean showTopTenImage;

	private int heatRating;

	private String quantityText;

	private boolean grocery;

	private boolean fullyAvailable;

	private boolean temporaryUnavailableOrAvailable;

	private boolean displayableBasedOnCms;

	private boolean wineOtherRating;


	private String browseRecommenderType;

	private boolean disabledRecommendations;

	private int productLayout;

	private String subtitle;

	private int layout;

	private String redirectUrl;

	private String sellBySalesunit;

	private String salesUnitLabel;

	private String quantityTextSecondary;

	private String servingSuggestion;

	private String packageDescription;

	private String seafoodOrigin;

	private boolean showSalesUnitImage;

	private boolean nutritionMultiple;

	private boolean notSearchable;

	private String prodPageRatings;

	private String prodPageTextRatings;

	private String ratingProdName;


	private String perfectPair;

	private boolean retainOriginalSkuOrder;

	private boolean showDefaultSustainabilityRating;

	private boolean excludedForEBTPayment;

	private boolean enforceQuantityMax;

	
	private boolean hasSalesUnitDescription;


	private boolean hasPartiallyFrozen;


	private int expertWeight;


	private boolean hasTerms;

	private List<String> skus = new ArrayList<String>();

	private List<String> crossSellProducts = new ArrayList<String>();

	private String primaryProductModel;

	private List<String> ymals;

	private int priority;

	private String primaryHome;

	private String parentDomain;

	private String preferredSku;


	private String wineFyi;

	private String wineRegion;

	private Double containerWeightHalfPint;

	private Double containerWeightPint;

	private Double containerWeightQuart;

	
	private String pageTitle;

	
	private List<String> primarySkus = new ArrayList<String>();

	public boolean isPreconfigured() {
		return preconfigured;
	}

	public void setPreconfigured(boolean preconfigured) {
		this.preconfigured = preconfigured;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public boolean isIncrementMaxEnforce() {
		return incrementMaxEnforce;
	}

	public void setIncrementMaxEnforce(boolean incrementMaxEnforce) {
		this.incrementMaxEnforce = incrementMaxEnforce;
	}

	public boolean isExcludedRecommendation() {
		return excludedRecommendation;
	}

	public void setExcludedRecommendation(boolean excludedRecommendation) {
		this.excludedRecommendation = excludedRecommendation;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public boolean isSoldBySalesUnits() {
		return soldBySalesUnits;
	}

	public void setSoldBySalesUnits(boolean soldBySalesUnits) {
		this.soldBySalesUnits = soldBySalesUnits;
	}

	public String getSkuModel() {
		return skuModel;
	}

	public void setSkuModel(String skuModel) {
		this.skuModel = skuModel;
	}

	public boolean isPerishable() {
		return perishable;
	}

	public void setPerishable(boolean perishable) {
		this.perishable = perishable;
	}

	public String getYmalHeader() {
		return ymalHeader;
	}

	public void setYmalHeader(String ymalHeader) {
		this.ymalHeader = ymalHeader;
	}

	public String getSeasonText() {
		return seasonText;
	}

	public void setSeasonText(String seasonText) {
		this.seasonText = seasonText;
	}

	public int getSpecialLayout() {
		return specialLayout;
	}

	public void setSpecialLayout(int specialLayout) {
		this.specialLayout = specialLayout;
	}

	public String getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public String getAlsoSoldAsName() {
		return alsoSoldAsName;
	}

	public void setAlsoSoldAsName(String alsoSoldAsName) {
		this.alsoSoldAsName = alsoSoldAsName;
	}

	public String getAka() {
		return aka;
	}

	public void setAka(String aka) {
		this.aka = aka;
	}

	public boolean isShowTopTenImage() {
		return showTopTenImage;
	}

	public void setShowTopTenImage(boolean showTopTenImage) {
		this.showTopTenImage = showTopTenImage;
	}

	public int getHeatRating() {
		return heatRating;
	}

	public void setHeatRating(int heatRating) {
		this.heatRating = heatRating;
	}

	public String getQuantityText() {
		return quantityText;
	}

	public void setQuantityText(String quantityText) {
		this.quantityText = quantityText;
	}

	public boolean isGrocery() {
		return grocery;
	}

	public void setGrocery(boolean grocery) {
		this.grocery = grocery;
	}

	public boolean isFullyAvailable() {
		return fullyAvailable;
	}

	public void setFullyAvailable(boolean fullyAvailable) {
		this.fullyAvailable = fullyAvailable;
	}

	public boolean isTemporaryUnavailableOrAvailable() {
		return temporaryUnavailableOrAvailable;
	}

	public void setTemporaryUnavailableOrAvailable(
			boolean temporaryUnavailableOrAvailable) {
		this.temporaryUnavailableOrAvailable = temporaryUnavailableOrAvailable;
	}

	public boolean isDisplayableBasedOnCms() {
		return displayableBasedOnCms;
	}

	public void setDisplayableBasedOnCms(boolean displayableBasedOnCms) {
		this.displayableBasedOnCms = displayableBasedOnCms;
	}

	public boolean isWineOtherRating() {
		return wineOtherRating;
	}

	public void setWineOtherRating(boolean wineOtherRating) {
		this.wineOtherRating = wineOtherRating;
	}

	public String getBrowseRecommenderType() {
		return browseRecommenderType;
	}

	public void setBrowseRecommenderType(String browseRecommenderType) {
		this.browseRecommenderType = browseRecommenderType;
	}

	public boolean isDisabledRecommendations() {
		return disabledRecommendations;
	}

	public void setDisabledRecommendations(boolean disabledRecommendations) {
		this.disabledRecommendations = disabledRecommendations;
	}

	public int getProductLayout() {
		return productLayout;
	}

	public void setProductLayout(int productLayout) {
		this.productLayout = productLayout;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getSellBySalesunit() {
		return sellBySalesunit;
	}

	public void setSellBySalesunit(String sellBySalesunit) {
		this.sellBySalesunit = sellBySalesunit;
	}

	public String getSalesUnitLabel() {
		return salesUnitLabel;
	}

	public void setSalesUnitLabel(String salesUnitLabel) {
		this.salesUnitLabel = salesUnitLabel;
	}

	public String getQuantityTextSecondary() {
		return quantityTextSecondary;
	}

	public void setQuantityTextSecondary(String quantityTextSecondary) {
		this.quantityTextSecondary = quantityTextSecondary;
	}

	public String getServingSuggestion() {
		return servingSuggestion;
	}

	public void setServingSuggestion(String servingSuggestion) {
		this.servingSuggestion = servingSuggestion;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public String getSeafoodOrigin() {
		return seafoodOrigin;
	}

	public void setSeafoodOrigin(String seafoodOrigin) {
		this.seafoodOrigin = seafoodOrigin;
	}

	public boolean isShowSalesUnitImage() {
		return showSalesUnitImage;
	}

	public void setShowSalesUnitImage(boolean showSalesUnitImage) {
		this.showSalesUnitImage = showSalesUnitImage;
	}

	public boolean isNutritionMultiple() {
		return nutritionMultiple;
	}

	public void setNutritionMultiple(boolean nutritionMultiple) {
		this.nutritionMultiple = nutritionMultiple;
	}

	public boolean isNotSearchable() {
		return notSearchable;
	}

	public void setNotSearchable(boolean notSearchable) {
		this.notSearchable = notSearchable;
	}

	public String getProdPageRatings() {
		return prodPageRatings;
	}

	public void setProdPageRatings(String prodPageRatings) {
		this.prodPageRatings = prodPageRatings;
	}

	public String getProdPageTextRatings() {
		return prodPageTextRatings;
	}

	public void setProdPageTextRatings(String prodPageTextRatings) {
		this.prodPageTextRatings = prodPageTextRatings;
	}

	public String getRatingProdName() {
		return ratingProdName;
	}

	public void setRatingProdName(String ratingProdName) {
		this.ratingProdName = ratingProdName;
	}

	public String getPerfectPair() {
		return perfectPair;
	}

	public void setPerfectPair(String perfectPair) {
		this.perfectPair = perfectPair;
	}

	public boolean isRetainOriginalSkuOrder() {
		return retainOriginalSkuOrder;
	}

	public void setRetainOriginalSkuOrder(boolean retainOriginalSkuOrder) {
		this.retainOriginalSkuOrder = retainOriginalSkuOrder;
	}

	public boolean isShowDefaultSustainabilityRating() {
		return showDefaultSustainabilityRating;
	}

	public void setShowDefaultSustainabilityRating(
			boolean showDefaultSustainabilityRating) {
		this.showDefaultSustainabilityRating = showDefaultSustainabilityRating;
	}

	public boolean isExcludedForEBTPayment() {
		return excludedForEBTPayment;
	}

	public void setExcludedForEBTPayment(boolean excludedForEBTPayment) {
		this.excludedForEBTPayment = excludedForEBTPayment;
	}

	public boolean isEnforceQuantityMax() {
		return enforceQuantityMax;
	}

	public void setEnforceQuantityMax(boolean enforceQuantityMax) {
		this.enforceQuantityMax = enforceQuantityMax;
	}

	public boolean isHasSalesUnitDescription() {
		return hasSalesUnitDescription;
	}

	public void setHasSalesUnitDescription(boolean hasSalesUnitDescription) {
		this.hasSalesUnitDescription = hasSalesUnitDescription;
	}

	public boolean isHasPartiallyFrozen() {
		return hasPartiallyFrozen;
	}

	public void setHasPartiallyFrozen(boolean hasPartiallyFrozen) {
		this.hasPartiallyFrozen = hasPartiallyFrozen;
	}

	public int getExpertWeight() {
		return expertWeight;
	}

	public void setExpertWeight(int expertWeight) {
		this.expertWeight = expertWeight;
	}

	public boolean isHasTerms() {
		return hasTerms;
	}

	public void setHasTerms(boolean hasTerms) {
		this.hasTerms = hasTerms;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	public List<String> getCrossSellProducts() {
		return crossSellProducts;
	}

	public void setCrossSellProducts(List<String> crossSellProducts) {
		this.crossSellProducts = crossSellProducts;
	}

	public String getPrimaryProductModel() {
		return primaryProductModel;
	}

	public void setPrimaryProductModel(String primaryProductModel) {
		this.primaryProductModel = primaryProductModel;
	}

	public List<String> getYmals() {
		return ymals;
	}

	public void setYmals(List<String> ymals) {
		this.ymals = ymals;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPrimaryHome() {
		return primaryHome;
	}

	public void setPrimaryHome(String primaryHome) {
		this.primaryHome = primaryHome;
	}

	public String getParentDomain() {
		return parentDomain;
	}

	public void setParentDomain(String parentDomain) {
		this.parentDomain = parentDomain;
	}

	public String getPreferredSku() {
		return preferredSku;
	}

	public void setPreferredSku(String preferredSku) {
		this.preferredSku = preferredSku;
	}

	public String getWineFyi() {
		return wineFyi;
	}

	public void setWineFyi(String wineFyi) {
		this.wineFyi = wineFyi;
	}

	public String getWineRegion() {
		return wineRegion;
	}

	public void setWineRegion(String wineRegion) {
		this.wineRegion = wineRegion;
	}

	public Double getContainerWeightHalfPint() {
		return containerWeightHalfPint;
	}

	public void setContainerWeightHalfPint(Double containerWeightHalfPint) {
		this.containerWeightHalfPint = containerWeightHalfPint;
	}

	public Double getContainerWeightPint() {
		return containerWeightPint;
	}

	public void setContainerWeightPint(Double containerWeightPint) {
		this.containerWeightPint = containerWeightPint;
	}

	public Double getContainerWeightQuart() {
		return containerWeightQuart;
	}

	public void setContainerWeightQuart(Double containerWeightQuart) {
		this.containerWeightQuart = containerWeightQuart;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public List<String> getPrimarySkus() {
		return primarySkus;
	}

	public void setPrimarySkus(List<String> primarySkus) {
		this.primarySkus = primarySkus;
	}

	public boolean isHasActiveYmalSets() {
		return hasActiveYmalSets;
	}

	public void setHasActiveYmalSets(boolean hasActiveYmalSets) {
		this.hasActiveYmalSets = hasActiveYmalSets;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProductRatingEnum() {
		return productRatingEnum;
	}

	public void setProductRatingEnum(String productRatingEnum) {
		this.productRatingEnum = productRatingEnum;
	}

	private boolean hasActiveYmalSets;

	private String department;

	private String productRatingEnum;

}
