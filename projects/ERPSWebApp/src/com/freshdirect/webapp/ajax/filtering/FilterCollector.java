package com.freshdirect.webapp.ajax.filtering;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.util.ProductInfoUtil;
import com.freshdirect.content.nutrition.EnumKosherSymbolValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.content.nutrition.NutritionValueEnum;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;

public class FilterCollector {
	private static final FilterCollector INSTANCE = new FilterCollector();

	private static final int NON_KOSHER_PRI = 999;

	private boolean showMeOnlyNewDisabled;

	private FilterCollector() {}

	public static FilterCollector defaultFilterCollector(boolean showMeOnlyNewDisabled) {
		INSTANCE.showMeOnlyNewDisabled = showMeOnlyNewDisabled;
		return INSTANCE;
	}

	public void collectBrandAndShowMeOnlyFilters(NavigationModel navigationModel, ProductModel product) throws FDResourceException, FDSkuNotFoundException {
		collectBrandFilters(navigationModel, product);
		Set<String> showMeOnlyOfSearchResults = navigationModel.getShowMeOnlyOfSearchResults();
		collectShowMeOnlyNewFilter(product, showMeOnlyOfSearchResults);
		try {
			final SkuModel defSku = PopulatorUtil.getDefSku(product);
			if (defSku == null) {
				// prevent NPE, just leave the scene
				return;
			}

			FDProduct fdProduct = defSku.getProduct();
			if (fdProduct != null) {
				String plantID=ProductInfoUtil.getPickingPlantId(defSku.getProductInfo());;
	            
				collectShowMeOnlyKosherFilter(showMeOnlyOfSearchResults, fdProduct.getKosherInfo(plantID));
				collectShowMeOnlyOrganicFilter(showMeOnlyOfSearchResults, fdProduct.getNutritionInfoList(ErpNutritionInfoType.ORGANIC));
				collectShowMeOnlyOnSaleFilter(product, showMeOnlyOfSearchResults);
			}
		} catch (FDSkuNotFoundException exc) {
			// absorb exception
		}
	}

	public void collectDepartmentAndCategoryFilters(NavigationModel navigationModel, ProductModel product) {
		Set<ContentKey> parentKeys = ContentNodeModelUtil.getAllParentKeys(product.getContentKey(), true);

		for (ContentKey contentKey : parentKeys) {
			if ("Department".equals(contentKey.getType().getName())) {
				collectDepartmentFilter(navigationModel, contentKey);
			} else if ("Category".equals(contentKey.getType().getName())) {
				collectCategoryFilters(navigationModel, contentKey);
			}
		}
	}

	public boolean isShowMeOnlyNewDisabled() {
		return showMeOnlyNewDisabled;
	}

	public void setShowMeOnlyNewDisabled(boolean showMeOnlyNewDisabled) {
		this.showMeOnlyNewDisabled = showMeOnlyNewDisabled;
	}
	
	private void collectBrandFilters(NavigationModel navigationModel, ProductModel product) {
		for (BrandModel brandModel : product.getBrands()) {
			navigationModel.getBrandsOfSearchResults().put(brandModel.getContentName(), brandModel);
		}
	}

	private void collectCategoryFilter(CategoryModel categoryModel, Map<String, CategoryModel> categoriesOfSearchResults) {
		if (categoryModel.isSearchable()) {
			categoriesOfSearchResults.put(categoryModel.getContentName(), categoryModel);
		}
	}

	private void collectCategoryFilters(NavigationModel navigationModel, ContentKey contentKey) {
		CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNode(contentKey.getId());
		Map<String, CategoryModel> categoriesOfSearchResults = navigationModel.getCategoriesOfSearchResults();
		if (categoryModel.getParentNode() instanceof DepartmentModel) { // Category
			collectCategoryFilter(categoryModel, categoriesOfSearchResults);
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
		if (departmentModel.isSearchable()) {
			navigationModel.getDepartmentsOfSearchResults().put(departmentModel.getContentName(), departmentModel);
		}
	}

	private void collectShowMeOnlyKosherFilter(Set<String> showMeOnlyOfSearchResults, FDKosherInfo kInfo) {
		int kosherPriority = kInfo != null ? kInfo.getPriority() : NON_KOSHER_PRI;
		if (EnumKosherSymbolValue.NONE.getPriority() < kosherPriority && kosherPriority < NON_KOSHER_PRI) {
			showMeOnlyOfSearchResults.add("kosher");
		}
	}
	
	private void collectShowMeOnlyNewFilter(ProductModel product, Set<String> showMeOnlyOfSearchResults) {
		if (!showMeOnlyNewDisabled && product.isNew()) {
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
		if (categoryModel.isSearchable()) {
			navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getContentName(), categoryModel);
			categoriesOfSearchResults.put(categoryModel.getParentNode().getContentName(), (CategoryModel) categoryModel.getParentNode());
		}
	}

	private void collectSubSubCategoryFilter(NavigationModel navigationModel, CategoryModel categoryModel, Map<String, CategoryModel> categoriesOfSearchResults) {
		if (categoryModel.isSearchable()) {
			navigationModel.getSubCategoriesOfSearchResults().put(categoryModel.getParentNode().getContentName(), (CategoryModel) categoryModel.getParentNode());
			categoriesOfSearchResults.put(categoryModel.getParentNode().getParentNode().getContentName(),
					(CategoryModel) categoryModel.getParentNode().getParentNode());
		}
	}

}
