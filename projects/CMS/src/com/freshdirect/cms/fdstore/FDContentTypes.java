package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.ContentType;

/**
 * Static definitions of {@link com.freshdirect.cms.ContentType}s
 * of the FD Storefront objects.
 */
public class FDContentTypes {

	public final static ContentType IMAGE = ContentType.get("Image");
	public final static ContentType CATEGORY = ContentType.get("Category");
	public final static ContentType PRODUCT = ContentType.get("Product");
	public final static ContentType CONFIGURED_PRODUCT = ContentType.get("ConfiguredProduct");
	public final static ContentType CONFIGURED_PRODUCT_GROUP = ContentType.get("ConfiguredProductGroup");
	public final static ContentType HTML = ContentType.get("Html");
	public final static ContentType TEMPLATE = ContentType.get("Template");
	public final static ContentType MEDIAFOLDER = ContentType.get("MediaFolder");
	public final static ContentType BRAND = ContentType.get("Brand");
	public final static ContentType SKU = ContentType.get("Sku");
	public final static ContentType STORE = ContentType.get("Store");
	public final static ContentType DEPARTMENT = ContentType.get("Department");
	public final static ContentType FDFOLDER = ContentType.get("FDFolder");
	public final static ContentType DOMAIN = ContentType.get("Domain");
	public final static ContentType DOMAINVALUE = ContentType.get("DomainValue");
	public final static ContentType COMPONENT_GROUP = ContentType.get("ComponentGroup");

	public final static ContentType ERP_MATERIAL = ContentType.get("ErpMaterial");
	public final static ContentType ERP_SALES_UNIT = ContentType.get("ErpSalesUnit");
	public final static ContentType ERP_CLASS = ContentType.get("ErpClass");
	public final static ContentType ERP_CHARACTERISTIC = ContentType.get("ErpCharacteristic");
	public final static ContentType ERP_CHARACTERISTIC_VALUE = ContentType.get("ErpCharacteristicValue");

	public static final ContentType RECIPE = ContentType.get("Recipe");
	public static final ContentType RECIPE_VARIANT = ContentType.get("RecipeVariant");
	public static final ContentType RECIPE_SECTION = ContentType.get("RecipeSection");
	public static final ContentType RECIPE_DEPARTMENT = ContentType.get("RecipeDepartment");
	public static final ContentType RECIPE_CATEGORY = ContentType.get("RecipeCategory");
	public static final ContentType RECIPE_SUBCATEGORY = ContentType.get("RecipeSubcategory");
	public static final ContentType RECIPE_SOURCE = ContentType.get("RecipeSource");
	public static final ContentType RECIPE_AUTHOR = ContentType.get("RecipeAuthor");
	public static final ContentType BOOK_RETAILER = ContentType.get("BookRetailer");
	public static final ContentType RECIPE_SEARCH_PAGE = ContentType.get("RecipeSearchPage");
	public static final ContentType RECIPE_SEARCH_CRITERIA = ContentType.get("RecipeSearchCriteria");
	
	public static final ContentType YMAL_SET = ContentType.get("YmalSet");
	
	public static final ContentType STARTER_LIST = ContentType.get("StarterList");
	
	public static final ContentType SYNONYM = ContentType.get("Synonym");

        public static final ContentType SEARCH_RELEVANCY_LIST = ContentType.get("SearchRelevancyList");
        public static final ContentType SEARCH_RELEVANCY_HINT = ContentType.get("SearchRelevancyHint");
	
   	public static final ContentType FAVORITE_LIST = ContentType.get("FavoriteList");
	
   	public static final ContentType RECOMMMENDER = ContentType.get("Recommender");
   	public static final ContentType RECOMMENDER_STRATEGY = ContentType.get("RecommenderStrategy");
   	
	private FDContentTypes() {
	}

}