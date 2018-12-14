package com.freshdirect.webapp.ajax.filtering;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.NutritionValueEnum;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDNutritionCache;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.EnumCatalogType;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;

public class FilterCollector {
	private static final FilterCollector INSTANCE = new FilterCollector();

	private static final int NON_KOSHER_PRI = 999;

	private FilterCollector() {}

	public static FilterCollector defaultFilterCollector() {
		return INSTANCE;
	}

	public void collectShowMeOnlyFilters(NavigationModel navigationModel, ProductModel product, FilteringFlowType pageType)
			throws FDResourceException, FDSkuNotFoundException {
		Set<String> showMeOnlyOfSearchResults = navigationModel.getShowMeOnlyOfSearchResults();
        if (!showMeOnlyOfSearchResults.contains("new")) {
            collectShowMeOnlyNewFilter(showMeOnlyOfSearchResults, product, pageType);
        }
		// if "kosher", "organic" and "sale" are in the result already,
		// we don't need to do additional check
		if (showMeOnlyOfSearchResults.contains("kosher") && showMeOnlyOfSearchResults.contains("organic")
				&& showMeOnlyOfSearchResults.contains("onsale")) {
			return;
		}
		try {
			final SkuModel defSku = PopulatorUtil.getDefSku(product);
			if (defSku == null) {
				// prevent NPE, just leave the scene
				return;
			}

			FDProduct fdProduct = defSku.getProduct();
			if (fdProduct != null) {

				if (!showMeOnlyOfSearchResults.contains("kosher")) {
					String plantID = ProductInfoUtil.getPickingPlantId(defSku.getProductInfo());
					collectShowMeOnlyKosherFilter(showMeOnlyOfSearchResults, fdProduct.getKosherInfo(plantID));
				}
				if (!showMeOnlyOfSearchResults.contains("organic")) {
					collectShowMeOnlyOrganicFilter(showMeOnlyOfSearchResults, FDNutritionCache.getInstance()
							.getNutrition(defSku.getSkuCode()).getNutritionInfoList(ErpNutritionInfoType.ORGANIC));
				}
				if (!showMeOnlyOfSearchResults.contains("onsale")) {
					collectShowMeOnlyOnSaleFilter(product, showMeOnlyOfSearchResults);
				}
			}
		} catch (FDSkuNotFoundException exc) {
			// absorb exception
		}
	}

	public void collectDepartmentAndCategoryFilters(NavigationModel navigationModel, ProductModel product) {
		Set<ContentKey> parentKeys = ContentNodeModelUtil.getAllParentKeys(product.getContentKey(), true);

		for (ContentKey contentKey : parentKeys) {
			if (ContentType.Department == contentKey.getType()) {
				collectDepartmentFilter(navigationModel, contentKey);
			} else if (ContentType.Category == contentKey.getType()) {
				collectCategoryFilters(navigationModel, contentKey);
			}
		}
	}

	public void collectBrandFilters(NavigationModel navigationModel, ProductModel product) {
		for (BrandModel brandModel : product.getBrands()) {
			navigationModel.getBrandsOfSearchResults().put(brandModel.getContentName(), brandModel);
		}
	}

	private void collectCategoryFilter(NavigationModel navigationModel, CategoryModel categoryModel, Map<String, CategoryModel> categoriesOfSearchResults) {
		if (categoryModel.isSearchable() && isCatalogSimilarToServiceType(navigationModel, categoryModel.getDepartment())) {
			categoriesOfSearchResults.put(categoryModel.getContentName(), categoryModel);
		}
	}

	private void collectCategoryFilters(NavigationModel navigationModel, ContentKey contentKey) {
		CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNode(contentKey.getId());
		Map<String, CategoryModel> categoriesOfSearchResults = navigationModel.getCategoriesOfSearchResults();
		if (categoryModel.getParentNode() instanceof DepartmentModel) { // Category
			collectCategoryFilter(navigationModel, categoryModel, categoriesOfSearchResults);
		} else {
			if (categoryModel.getParentNode() != null && categoryModel.getParentNode().getParentNode() instanceof DepartmentModel) { // Subcategory
				collectSubCategoryFilter(navigationModel, categoryModel, categoriesOfSearchResults);
			} else if (categoryModel.getParentNode() != null && categoryModel.getParentNode().getParentNode() != null) { // Subsubcategory
				collectSubSubCategoryFilter(navigationModel, categoryModel, categoriesOfSearchResults);
			}
		}
	}

	private void collectDepartmentFilter(NavigationModel navigationModel, ContentKey contentKey) {
		DepartmentModel departmentModel = (DepartmentModel) ContentFactory.getInstance().getContentNode(contentKey.getId());
		if (departmentModel.isSearchable() && isCatalogSimilarToServiceType(navigationModel, departmentModel)) {
			navigationModel.getDepartmentsOfSearchResults().put(departmentModel.getContentName(), departmentModel);
		}
	}

	private void collectShowMeOnlyKosherFilter(Set<String> showMeOnlyOfSearchResults, FDKosherInfo kInfo) {
		int kosherPriority = kInfo != null ? kInfo.getPriority() : NON_KOSHER_PRI;
		if (EnumKosherSymbolValue.NONE.getPriority() < kosherPriority && kosherPriority < NON_KOSHER_PRI) {
			showMeOnlyOfSearchResults.add("kosher");
		}
	}

    private void collectShowMeOnlyNewFilter(Set<String> showMeOnlyOfSearchResults, ProductModel product, FilteringFlowType pageType) {
        if (product.isNew() && !FilteringFlowType.NEWPRODUCTS.equals(pageType)) {
            showMeOnlyOfSearchResults.add("new");
        }
    }

	private void collectShowMeOnlyOnSaleFilter(ProductModel product, Set<String> showMeOnlyOfSearchResults) {
		final PriceCalculator pricing = product.getPriceCalculator(ContentFactory.getInstance().getCurrentUserContext().getPricingContext());
		if (pricing.getDealPercentage() > 0 || pricing.getTieredDealPercentage() > 0 || pricing.getGroupPrice() != 0.0) {
			showMeOnlyOfSearchResults.add("onsale");
		}
	}

	private void collectShowMeOnlyOrganicFilter(Set<String> showMeOnlyOfSearchResults, List<? extends NutritionValueEnum> nutritionInfo) {
		if (nutritionInfo != null) {
			for (NutritionValueEnum nutritionValueEnum : nutritionInfo) {
				if (nutritionValueEnum.getCode().equals(ErpNutritionInfoType.ORGANIC)) {
					showMeOnlyOfSearchResults.add("organic");
					break;
				}
			}
		}
	}

	private void collectSubCategoryFilter(NavigationModel navigationModel, CategoryModel categoryModel, Map<String, CategoryModel> categoriesOfSearchResults) {
		if (categoryModel.isSearchable() && isCatalogSimilarToServiceType(navigationModel, categoryModel.getDepartment())) {
			navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getContentName(), categoryModel);
			categoriesOfSearchResults.put(categoryModel.getParentNode().getContentName(), (CategoryModel) categoryModel.getParentNode());
		}
	}

	private void collectSubSubCategoryFilter(NavigationModel navigationModel, CategoryModel categoryModel, Map<String, CategoryModel> categoriesOfSearchResults) {
		if (categoryModel.isSearchable() && isCatalogSimilarToServiceType(navigationModel, categoryModel.getDepartment())) {
			navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getParentNode().getContentName(), (CategoryModel) categoryModel.getParentNode());
			categoriesOfSearchResults.put(categoryModel.getParentNode().getParentNode().getContentName(), (CategoryModel) categoryModel.getParentNode().getParentNode());
		}
	}

    private boolean isCatalogSimilarToServiceType(NavigationModel navigationModel, DepartmentModel departmentModel) {
        FDUserI user = navigationModel.getUser();
        EnumCatalogType catalogType = departmentModel.getCatalogType(EnumCatalogType.EMPTY.name());
        return user.isCorporateUser() && catalogType.isCorporate() || !user.isCorporateUser() && catalogType.isResidental();
    }

}
