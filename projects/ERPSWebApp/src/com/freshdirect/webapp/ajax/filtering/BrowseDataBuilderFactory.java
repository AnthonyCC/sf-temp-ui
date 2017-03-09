package com.freshdirect.webapp.ajax.filtering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringComparatorUtil;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortOptionModel;
import com.freshdirect.fdstore.content.SortStrategyType;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.SearchPageType;
import com.freshdirect.webapp.ajax.browse.data.BasicData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;
import com.freshdirect.webapp.ajax.browse.data.DescriptiveDataI;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData.MenuBoxSelectionType;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData.MenuBoxType;
import com.freshdirect.webapp.ajax.browse.data.MenuItemData;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.browse.data.NavigationModel;
import com.freshdirect.webapp.ajax.browse.data.ParentData;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.browse.data.SelectableData;
import com.freshdirect.webapp.ajax.browse.data.SortDropDownData;
import com.freshdirect.webapp.ajax.browse.data.SortOptionData;
import com.freshdirect.webapp.ajax.data.CMSModelToSoyDataConverter;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.globalnav.data.DepartmentData;
import com.freshdirect.webapp.globalnav.data.SuperDepartmentData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class BrowseDataBuilderFactory {
	
	private static final Logger LOG = LoggerFactory.getInstance( BrowseDataBuilderFactory.class );
	
	private static final String CATEGORY_CAROUSEL_RATIO_DEFAULT = "TWO_TWO";
	private static final String DEPARTMENT_CAROUSEL_RATIO_DEFAULT = "THREE_ONE";
	private static final String SUPERDEPARTMENT_CAROUSEL_RATIO_DEFAULT = "THREE_ONE";
	private static final String BANNER_LOCATION_DEFAULT = "TOP";
	private static final String CAROUSEL_POSITION_DEFAULT = "BOTTOM";
	
	private static BrowseDataBuilderFactory factory;
	
	public static BrowseDataBuilderFactory getInstance(){
		if(factory==null){
			factory = new BrowseDataBuilderFactory();
		}
		
		return factory;
	}
	
	public static BrowseDataBuilderI createBuilder(NavDepth navDepth, boolean hasSuperDeparment, SearchPageType searchPageType){
		
		if (searchPageType != null) {
			if (SearchPageType.PRODUCT == searchPageType) {
				return getInstance().new SearchPageDataBuilder();
			} else if (SearchPageType.RECIPE == searchPageType) {
				return getInstance().new RecipePageDataBuilder();
			}
			
		}

		if (hasSuperDeparment) {
			return getInstance().new SuperDepartmentDataBuilder();
			
		}
		
		switch (navDepth) {
		
			case DEPARTMENT: {
				return getInstance().new DepartmentDataBuilder();
			}
			
			case CATEGORY: {
				return getInstance().new CategoryDataBuilder();
			}
			
			case SUB_CATEGORY: {
				return getInstance().new SubCategoryDataBuilder();
			}
			
			case SUB_SUB_CATEGORY: {
				return getInstance().new SubSubCategoryDataBuilder();
			}
			
			default: {
				return null;
			}		
		}
		
	}
	
	public class SuperDepartmentDataBuilder implements BrowseDataBuilderI {


		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {

			SuperDepartmentModel superDepartmentModel = (SuperDepartmentModel) navigationModel.getSelectedContentNodeModel();
			SuperDepartmentData superDepartmentData = CMSModelToSoyDataConverter.createSuperDepartmentData(superDepartmentModel, user);
		
			// create static sections
			List<SectionContext> sections = new ArrayList<SectionContext>();
			sections.add(createDepartmentSection(superDepartmentData.getName(), ((SuperDepartmentModel)navigationModel.getSelectedContentNodeModel()).getBrowseName(), superDepartmentData.getDepartments()));

			BrowseDataContext data = new BrowseDataContext();
			data.setSectionContexts(checkEmpty(sections));
			appendHtml(data.getDescriptiveContent(), superDepartmentModel.getSuperDepartmentBanner(), superDepartmentModel.getBrowseMiddleMedia(), user);
			data.getDescriptiveContent().setMediaLocation(superDepartmentModel.getBannerLocation(BANNER_LOCATION_DEFAULT).toString());
			data.getCarousels().setCarouselPosition(superDepartmentModel.getCarouselPosition(CAROUSEL_POSITION_DEFAULT).toString());
			data.getCarousels().setCarouselRatio(superDepartmentModel.getCarouselRatio(SUPERDEPARTMENT_CAROUSEL_RATIO_DEFAULT).toString());
			appendTitle(data, superDepartmentModel.getTitleBar());

			try { //session is null because saving SMART_STORE_PREV_RECOMMENDATIONS isn't necessary here
				ValueHolder<Variant> out = new ValueHolder<Variant>();
				List<ProductModel> recommendedItems = ProductRecommenderUtil.getAggregatedSuperDepartmentFeaturedRecommenderProducts(superDepartmentModel, null, out);
				data.getCarousels().setCarousel1(createCarouselData(null, superDepartmentModel.getSdFeaturedRecommenderTitle(), recommendedItems, user, EnumEventSource.SDFR.getName(), out.isSet() ? out.getValue().getId() : null ));
			} catch (FDResourceException e) {
				LOG.error("recommendation failed", e);
			}
			data.getCarousels().setCarousel2(createCarouselData(null, superDepartmentModel.getSdMerchantRecommenderTitle(), ProductRecommenderUtil.getSuperDepartmentMerchantRecommenderProducts(superDepartmentModel), user, EnumEventSource.SDFR.getName()));
			
			filterProducts(navigationModel, data);
			
			return data;
		}
				
	}
	
	public class DepartmentDataBuilder implements BrowseDataBuilderI {
		

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
	
			List<CategoryData> regularSubCategories = new ArrayList<CategoryData>();
			List<CategoryData> preferenceSubCategories = new ArrayList<CategoryData>();
			DepartmentModel department = (DepartmentModel) navigationModel.getNavigationHierarchy().get(NavDepth.DEPARTMENT);
			
			final boolean showPopularCatsGlobal=department.isShowPopularCategories();
			final boolean showCatSectionHeaders = department.isShowCatSectionHeaders();

			// create category lists on the department level
			for (CategoryModel cat : department.getSubcategories()){
				if (NavigationUtil.isCategoryHiddenInContext(user, cat)) {
					continue;
				}
				
				if (cat.isPreferenceCategory()){
					preferenceSubCategories.add(CMSModelToSoyDataConverter.createCategoryData(cat, user, showPopularCatsGlobal));
				} else {
					regularSubCategories.add(CMSModelToSoyDataConverter.createCategoryData(cat, user, showPopularCatsGlobal));
				}
			}
			
			// create static sections
			List<SectionContext> sections = new ArrayList<SectionContext>();
			if (!navigationModel.getCategorySections().isEmpty() && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.leftnav2014, user)) {
				
				if (!showCatSectionHeaders) {
					// Option 1 : one section without headers [default]

					// Step #1 - collect categories from sections
					List<CategoryModel> flatCats = new ArrayList<CategoryModel>();
					for (CategorySectionModel categorySection : navigationModel.getCategorySections()) {
						for (CategoryModel categoryModel : categorySection.getSelectedCategories()) {
							if (!NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
								flatCats.add(categoryModel);
							}
						}
					}

					// Step #2 - there is no step #2 (there was awhile ago)

					// Step #3 - transform cats to soy data
					List<CategoryData> selectedSectionCategories = new ArrayList<CategoryData>();
					for (CategoryModel cat : flatCats) {
						selectedSectionCategories.add(CMSModelToSoyDataConverter.createCategoryData(cat, user, showPopularCatsGlobal));
					}

					// Step #4 - create common section
					sections.add(createSection(null, selectedSectionCategories));

				} else {
					// Option 2 : category sections + headers
					for (CategorySectionModel categorySection : navigationModel.getCategorySections()) {
						List<CategoryData> selectedSectionCategories = new ArrayList<CategoryData>();
						for (CategoryModel categoryModel : categorySection.getSelectedCategories()) {
							if (NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
								continue;
							}
							
							selectedSectionCategories.add(CMSModelToSoyDataConverter.createCategoryData(categoryModel, user, showPopularCatsGlobal));
						}
						sections.add(createSection(categorySection.getHeadline(), selectedSectionCategories));
					}
				}
				
			} else {
				sections.add(createSection(department.getRegularCategoriesNavHeader(), regularSubCategories));
			}
			if(preferenceSubCategories.size()>0){
				sections.add(createSection(department.getPreferenceCategoriesNavHeader(), preferenceSubCategories));				
			}

			BrowseDataContext data = new BrowseDataContext();
			data.getSections().setUsePopularCategoriesLayout(showPopularCatsGlobal);
			data.setSectionContexts(checkEmpty(sections));
			appendHtml(data.getDescriptiveContent(), department.getDepartmentBanner(), department.getBrowseMiddleMedia(), user);
			data.getDescriptiveContent().setMediaLocation(department.getBannerLocation(BANNER_LOCATION_DEFAULT).toString());
			data.getCarousels().setCarouselPosition(department.getCarouselPosition(CAROUSEL_POSITION_DEFAULT).toString());
			data.getCarousels().setCarouselRatio(department.getCarouselRatio(DEPARTMENT_CAROUSEL_RATIO_DEFAULT).toString());
			appendTitle(data, department.getTitleBar());

			try {  //session is null because saving SMART_STORE_PREV_RECOMMENDATIONS isn't necessary here
				ValueHolder<Variant> out = new ValueHolder<Variant>();
				List<ProductModel> recommendedItems = ProductRecommenderUtil.getFeaturedRecommenderProducts(department, user, null, out);
				data.getCarousels().setCarousel1(createCarouselData(null, department.getFeaturedRecommenderTitle(), recommendedItems, user, EnumEventSource.DFR.getName(), out.isSet() ? out.getValue().getId() : null ));
			} catch (FDResourceException e) {
				LOG.error("recommendation failed", e);
			}
			data.getCarousels().setCarousel2(createCarouselData(null, department.getMerchantRecommenderTitle(), ProductRecommenderUtil.getMerchantRecommenderProducts(department), user, EnumEventSource.DMR.getName()));
			
			filterProducts(navigationModel, data);
			
			return data;
		}
				
	}
	
	private SectionContext createSection(String headerText, List<CategoryData> categories){
		SectionContext section = new SectionContext();
		section.setHeaderText(headerText);
		section.setCategories(categories);
		return section;
	}

	private SectionContext createDepartmentSection(String superDepartmentName, String headerText, List<DepartmentData> departments){
		SectionContext section = new SectionContext();
		section.setSuperDepartmentName(superDepartmentName);
		section.setHeaderText(headerText);
		section.setDepartments(departments);
		return section;
	}
	
	public class CategoryDataBuilder implements BrowseDataBuilderI {

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
			
			BrowseDataContext data = new BrowseDataContext();
			List<SectionContext> sections = new ArrayList<SectionContext>();
			
			
			CategoryModel cat = (CategoryModel) navigationModel.getNavigationHierarchy().get(NavDepth.CATEGORY);
			List<CategoryModel> subCats = cat.getSubcategories();
			
			boolean showPopularCatsGlobal=cat.isShowPopularCategories();
			
			if (subCats.size()==0 || nav.isAll()) { //show either the products of this category

				if(cat.isNoGroupingByCategory()){
					
					sections.add(createProductSection(cat, user, navigationModel));	
					
				} else { // or create the section tree

					SectionContext sectionTree = createSectionTree(cat, navigationModel.getNavDepth().getLevel(), user);
					sections.add(sectionTree);
					if (subCats.size()!=0 && nav.isAll()) { //hide category section header if Show All 
						sectionTree.setHeaderText(null);
						sectionTree.setHeaderImage(null);
						//sectionTree.setMedia(null); // show header media based on APPDEV-3792
					}
					
				}

			} else { //or show the actual category list
				
				data.getSections().setUsePopularCategoriesLayout(showPopularCatsGlobal);
				SectionContext catSection = createSection(cat, user);
				catSection.setCategories(createCategoryDatas(cat, user, showPopularCatsGlobal));
				
				sections.add(catSection);
				
				if(!nav.isPdp()){
					try { //add scarab recommender for category listing page
						Recommendations recommendations = ProductRecommenderUtil.getBrowseCategoryListingPageRecommendations(user, cat);
						List<ProductModel> products = recommendations.getAllProducts();
						
						if (products.size()>0 && !cat.isDisableCategoryYmalRecommender()){
							data.getCarousels().setCarousel1(createCarouselData(null, "You May Also Like", products, user, EnumEventSource.CSR.getName(), recommendations.getVariant().getId() ));
						}
					} catch (FDResourceException e) {
						LOG.error("recommendation failed",e);
					}					
				}
			}
			
			appendCatDepthFields(data, cat, sections, user, nav, (nav.isAll() || subCats.size()==0));
			
			if(!nav.isPdp()){			
				filterProducts(navigationModel, data);				
			}
			
			return data;
		}		
	}
	
	public class SubCategoryDataBuilder implements BrowseDataBuilderI {

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
			
			BrowseDataContext data = new BrowseDataContext();
			List<SectionContext> sections = new ArrayList<SectionContext>();
			
			CategoryModel subCat = (CategoryModel) navigationModel.getNavigationHierarchy().get(NavDepth.SUB_CATEGORY);
			
			// populate sections
			if(subCat.isNoGroupingByCategory()){
				
				sections.add(createProductSection(subCat, user, navigationModel));	
				
			}else{
				// or create the section tree
				sections.add(createSectionTree(subCat, navigationModel.getNavDepth().getLevel(), user));
			}

			appendCatDepthFields(data, subCat, sections, user, nav, true);
			
			if(!nav.isPdp()){			
				filterProducts(navigationModel, data);
			}
			
			return data;
		}		
	}
	
	public class SubSubCategoryDataBuilder implements BrowseDataBuilderI {

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
			
			BrowseDataContext data = new BrowseDataContext();
			List<SectionContext> sections = new ArrayList<SectionContext>();
			
			CategoryModel subCat = (CategoryModel) navigationModel.getNavigationHierarchy().get(NavDepth.SUB_CATEGORY);
			CategoryModel subSubCat = (CategoryModel) navigationModel.getNavigationHierarchy().get(NavDepth.SUB_SUB_CATEGORY);
			
			// create super section
			SectionContext superSection = createSection(subCat, user);
			
			// create and populate subSection
			List<SectionContext> subSections = new ArrayList<SectionContext>();		
			SectionContext subSection = createSection(subSubCat, user);
			appendProductItems(subSection, subSubCat);
			subSections.add(subSection);
			
			// populate superSection with subsections
			superSection.setSectionContexts(checkEmpty(subSections));
			
			// populate data
			sections.add(superSection);
			
			appendCatDepthFields(data, subSubCat, sections, user, nav, true);	
			
			if(!nav.isPdp()){				
				filterProducts(navigationModel, data);
			}
			
			return data;
		}		
	}
	
	public class SearchPageDataBuilder implements BrowseDataBuilderI {

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
			
			BrowseDataContext data = new BrowseDataContext();
			List<SectionContext> sections = new ArrayList<SectionContext>();
			SectionContext section = new SectionContext();
			
			section.setProductItems(ProductItemFilterUtil.createFilteringProductItemsFromSearchResults(navigationModel.getSearchResults()));
			
			sections.add(section);	
			
			data.setSectionContexts(checkEmpty(sections));
			
//			appendCatDepthFields(data, subCat, sections, user, nav, true);
			
			int productHits = filterProducts(navigationModel, data);
			nav.setProductHits(productHits);
			
			return data;
		}		
	}
	
	public class RecipePageDataBuilder implements BrowseDataBuilderI {

		@Override
		public BrowseDataContext buildBrowseData(NavigationModel navigationModel, FDSessionUser user, CmsFilteringNavigator nav) {
			BrowseDataContext browseDataContext = new BrowseDataContext();
			List<SectionContext> sections = new ArrayList<SectionContext>();
			SectionContext sectionContext = new SectionContext();
			sectionContext.setRecipeItems(ProductItemFilterUtil.createFilteringRecipeItems(navigationModel.getRecipeResults()));
			sections.add(sectionContext);
			browseDataContext.setSectionContexts(checkEmpty(sections));
			int recipeHits = filterRecipes(navigationModel, browseDataContext);
			nav.setRecipeHits(recipeHits);
			return browseDataContext;
		}
		
	}

	private List<CategoryData> createCategoryDatas(CategoryModel cat, FDSessionUser user, boolean showPopularCategoriesGlobal){
		List<CategoryData> categories = new ArrayList<CategoryData>();
		for(CategoryModel subCat : cat.getSubcategories()){
			if (NavigationUtil.isCategoryHiddenInContext(user, subCat)) {
				continue;
			}
			categories.add(CMSModelToSoyDataConverter.createCategoryData(subCat, user, showPopularCategoriesGlobal));
		}
		return categories;
	}
	
	/**
	 * @param cat
	 * @param level
	 * @param user
	 * @return
	 * 
	 * create section tree (product list grouped by subcategories)
	 */
	public SectionContext createSectionTree(CategoryModel cat, int level, FDSessionUser user){
		
		List<SectionContext> sections = new ArrayList<SectionContext>();
		
		SectionContext section = createSection(cat, user);
		section.setSectionContexts(sections);
		
		if(level==NavDepth.getMaxLevel() || cat.getSubcategories().size()==0){
			
			// append products in case of no sub categories			
			appendProductItems(section, cat);
			
		}else{
			
			// walk through on sub categories ...
			List<CategoryModel> subCats = cat.getSubcategories();
			// Products in content area needs to be in CMS order. Comment this line back if you need alphabetical order instead.
			//Collections.sort(subCats, ProductContainer.NAME_COMPARATOR);
			
			for (CategoryModel subCat : subCats){
				
				if (NavigationUtil.isCategoryHiddenInContext(user, subCat)) {
					continue;
				}
				
				// and create sections
				SectionContext subSection = createSectionTree(subCat, level+1, user);
				if(subSection.isSpecial()){ 
					section.setSpecial(true); // mark the whole structure as special
				}
				sections.add(subSection);
			}	
		}
		
		return section;
	}
	
	public SectionContext createProductSection(CategoryModel cat, FDSessionUser user, NavigationModel navModel){
		
		// create the ONE section contains all products
		SectionContext section = createSection(cat, user);
		
		// collect all products (make sure there are no duplicates)
		Set<ProductModel> prods = new HashSet<ProductModel>();
		collectAllProducts(cat, navModel.getNavDepth().getLevel(), user, prods);
		section.setProductItems(ProductItemFilterUtil.createFilteringProductItems(new ArrayList<ProductModel>(prods)));
		
		return section;
		
	}
	
	public void collectAllProducts(CategoryModel cat, int level, FDSessionUser user, Set<ProductModel> prods){
		
		if(cat.getRedirectUrlClean()!=null){ // no products shown in case of redirect url
			return;
		}
		
		if(level==NavDepth.getMaxLevel() || cat.getSubcategories().size()==0){
			
			// append products in case of no sub categories			
			prods.addAll(cat.getProducts());
			
		}else{
			
			// walk through on sub categories ...			
			for (CategoryModel subCat : cat.getSubcategories()){
				
				if (NavigationUtil.isCategoryHiddenInContext(user, subCat)) {
					continue;
				}
							
				collectAllProducts(subCat, level+1, user, prods);
			}	
		}
	}

	
	/**
	 * @param cat
	 * @return
	 * create a simple section
	 */
	private SectionContext createSection(CategoryModel cat, FDSessionUser user){
		SectionContext section = new SectionContext();
		section.setCatId(cat.getContentName());
		Image headerImage = cat.getNameImage();
		
		if (headerImage == null){
			section.setHeaderText(cat.getFullName());
		} else {
			section.setHeaderImage(headerImage.getPath());
		}
		
		appendHtml(section, cat.getDescription(), cat.getBrowseMiddleMedia(), user);
		
		if(cat.getRedirectUrlClean()!=null || cat.getSpecialLayout()!=null){
			section.setSpecial(true);
		}
	
		return section;
	}
	
	/**
	 * @param section
	 * @param cat
	 * @param activeFilters
	 * 
	 * add the UNFILTERED!! product list on the section 
	 */
	private void appendProductItems(SectionContext section, CategoryModel cat) {
		
		if(cat.getRedirectUrlClean()!=null){ // no products shown in case of redirect url
			return;
		}

		if(section.getProductItems()==null){
			section.setProductItems(ProductItemFilterUtil.createFilteringProductItems(cat.getProducts()));
		}else{
			section.getProductItems().addAll(ProductItemFilterUtil.createFilteringProductItems(cat.getProducts()));				
		}
	}
	
	/**
	 * @param sections
	 * @param allItems
	 * @param activeFilters
	 * 
	 * Apply active filters on sections. This method also populate an unfiltered product list into allItems.
	 */
	private int filterProducts(List<SectionContext> sections, List<FilteringProductItem> allItems, Set<ProductItemFilterI> activeFilters){
		int result = 0;
		Iterator<SectionContext> it = sections.iterator();
		while(it.hasNext()) {
			SectionContext section = it.next();
			if(section.getProductItems() != null && section.getProductItems().size() > 0){
				allItems.addAll(section.getProductItems());
				section.setProductItems(ProductItemFilterUtil.getFilteredProducts(section.getProductItems(), activeFilters, true, true));
				result += section.getProductItems().size();
			}
			if(section.getSectionContexts()!=null && section.getSectionContexts().size()>0){
				result += filterProducts(section.getSectionContexts(), allItems, activeFilters);
			}
		}
		return result;
	}
	
	private int filterRecipes(List<SectionContext> sections, List<FilteringProductItem> allItems, Set<ProductItemFilterI> activeFilters) {
		int result = 0;
		for (SectionContext sectionContext : sections) {
			if(sectionContext.getRecipeItems() != null && sectionContext.getRecipeItems().size() > 0){
				allItems.addAll(sectionContext.getRecipeItems());
				sectionContext.setRecipeItems(ProductItemFilterUtil.getFilteredProducts(sectionContext.getRecipeItems(), activeFilters, true, false)); // TODO: change ProductItemFilterUtil.getFilteredProducts showUnavProducts logic to process recipes.
				result += sectionContext.getRecipeItems().size();
			}
			if(sectionContext.getSectionContexts()!=null && sectionContext.getSectionContexts().size() > 0){
				result += filterRecipes(sectionContext.getSectionContexts(), allItems, activeFilters);
			}	
		}
		return result;
	}
	
	/**
	 * @param sections
	 * @param menu
	 * 
	 * remove empty sections from browseData. 
	 * if a sections became empty after the productPotato population (because of no default sku exception, or any other exception) then we also need to disable the related menu item
	 */
	public void removeEmptySections(List<SectionData> sections, List<MenuBoxData> menu){
		
		Iterator<SectionData> it = sections.iterator();
		
		while(it.hasNext()){
			
			SectionData section = it.next();
			
			if(section.getSections()!=null && section.getSections().size()>0){
				// go deeper
				removeEmptySections(section.getSections(), menu);
			}
			
			if((section.getSections()==null || section.getSections().size()==0) &&
					section.getCategories()==null && 
					(section.getProducts()==null || section.getProducts().size()==0)){
				// no products nor sections: remove section
				it.remove();
				
				//make sure there are no active menu items pointing to this section
				for(MenuBoxData box : menu){
					for(MenuItemData item : box.getItems()){
						if(!item.isActive() && section.getCatId() != null && section.getCatId().equals(item.getId())){
							item.setActive(false);
						}
					}
				}
			}
		}		
	}
	
	private int filterProducts(NavigationModel navigationModel, BrowseDataContext data) {
		List<FilteringProductItem> allItems = new ArrayList<FilteringProductItem>();
		int productHits = filterProducts(data.getSectionContexts(), allItems, navigationModel.getActiveFilters());
		data.setUnfilteredItems(allItems);
		return productHits;
	}
	
	private int filterRecipes(NavigationModel navigationModel, BrowseDataContext browseDataContext) {
		List<FilteringProductItem> allItems = new ArrayList<FilteringProductItem>();
		int recipeHits = filterRecipes(browseDataContext.getSectionContexts(), allItems, navigationModel.getActiveFilters());
		browseDataContext.setUnfilteredItems(allItems);
		return recipeHits;
	}
	
	private void appendHtml(DescriptiveDataI data, Html dataMedia, Html middleMedia, FDSessionUser user){
		if(dataMedia!=null){
			data.setMedia(MediaUtils.renderHtmlToString(dataMedia, user));
		}
		if(middleMedia!=null){
			data.setMiddleMedia(MediaUtils.renderHtmlToString(middleMedia, user));
		}
	}

	private void appendTitle(BrowseDataContext data, Image titleBar){
		if (titleBar != null){
			data.getDescriptiveContent().setTitleBar(titleBar.getPath());
		}
	}

	private void appendCatDepthFields(BrowseDataContext data, CategoryModel cat, List<SectionContext> sections, FDSessionUser user, CmsFilteringNavigator nav, boolean productListing){
		data.setSectionContexts(checkEmpty(sections));
		appendHtml(data.getDescriptiveContent(), cat.getCategoryBanner(), cat.getBrowseMiddleMedia(), user);
		appendTitle(data, cat.getDepartment().getTitleBar());
		
		if(productListing){
			data.getDescriptiveContent().setMediaLocation(cat.getBannerLocationPLP(BANNER_LOCATION_DEFAULT).toString());	//Product Listing Page
			data.getCarousels().setCarouselPosition(cat.getCarouselPositionPLP(CAROUSEL_POSITION_DEFAULT).toString());
			data.getCarousels().setCarouselRatio(cat.getCarouselRatioPLP(CATEGORY_CAROUSEL_RATIO_DEFAULT).toString());
		}else{
			data.getDescriptiveContent().setMediaLocation(cat.getBannerLocationCLP(BANNER_LOCATION_DEFAULT).toString()); // Category Listing Page
			data.getCarousels().setCarouselPosition(cat.getCarouselPositionCLP(CAROUSEL_POSITION_DEFAULT).toString());
			data.getCarousels().setCarouselRatio(cat.getCarouselRatioCLP(CATEGORY_CAROUSEL_RATIO_DEFAULT).toString());
		}
	}
	
	private CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDSessionUser user, String cmEventSource){
		return createCarouselData(id, name, products, user, cmEventSource, null);
	}
	
	private CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDSessionUser user, String cmEventSource, String variantId){

		if (products.size()==0){
			return null; //should not display empty carousel
		}
		
		CarouselData carousel = new CarouselData();
		carousel.setId(id);
		carousel.setName(name);
		carousel.setCmEventSource(cmEventSource);
		
		List<ProductData> productDatas = new ArrayList<ProductData>();
		for (ProductModel product : products){
			try {
				ProductData productData = ProductDetailPopulator.createProductData(user, product);
				productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
				if (variantId != null) {
					productData.setVariantId(variantId);
					productData.setProductPageUrl( FDURLUtil.getNewProductURI(product, variantId) );
				}
				productDatas.add(productData);
			} catch (FDResourceException e) {
				LOG.error("failed to create ProductData", e);
			} catch (FDSkuNotFoundException e) {
				LOG.error("failed to create ProductData", e);
			} catch (HttpErrorResponse e) {
				LOG.error("failed to create ProductData", e);
			}
		}
		carousel.setProducts(productDatas);
		return carousel;
	}
	
	private <T extends Collection<?>> T checkEmpty(T col){
		return col.size()>0 ? col : null;
	}
	
	

    /**
     * @param sorters
     * @param nav
     * @return the displayed sort options
     * 
     * Create the sort bar objects
     */
    public void processSorting (BrowseDataContext data, CmsFilteringNavigator nav, FDUserI user){
        processSorting(data, nav, user, true);
    }
	
	/**
	 * @param sorters
	 * @param nav
	 * @param enableDefaultSorting if false, the natural sorting will not happen
	 * @return the displayed sort options
	 * 
	 * Create the sort bar objects
	 */
	public void processSorting (BrowseDataContext data, CmsFilteringNavigator nav, FDUserI user, boolean enableDefaultSorting){
		
		List<SortOptionModel> sorters = getSortersForCurrentFlow(data, nav);
		
		if (sorters!=null){
			List<SortOptionData> options = new ArrayList<SortOptionData>();
			Comparator<FilteringProductItem> comparator = null;
			SortStrategyType usedSortStrategy = null;
			
			//set default sort option from first
			if (nav.getSortBy()==null && sorters.size()>0 && enableDefaultSorting){
				nav.setSortBy(sorters.get(0).getContentName());
				nav.setOrderAscending(true);
			}
			
			for(SortOptionModel sorter : sorters){
				final String sorterId = sorter.getContentName();
				if (sorterId != null) {
					SortOptionData sortOptionData = new SortOptionData();
					sortOptionData.setId(sorterId);
					
					if (sorterId.equals(nav.getSortBy())) {
						 sortOptionData.setSelected(true);
						 String selectedLabel = sorter.getSelectedLabel();
						 String selectedLaberReverseOrder = sorter.getSelectedLabelReverseOrder();
						 usedSortStrategy = sorter.getSortStrategyType();
						 
						 if (!nav.isOrderAscending() && selectedLaberReverseOrder != null && !"".equals(selectedLaberReverseOrder)) {
							sortOptionData.setName(sorter.getSelectedLabelReverseOrder());
							sortOptionData.setOrderAscending(true); //if option is clicked this will be the new order
							comparator = ProductItemSorterFactory.createComparator(usedSortStrategy, user, true);
						 } else {
							sortOptionData.setName(selectedLabel == null || "".equals(selectedLabel) ? sorter.getLabel() : selectedLabel);
							comparator = ProductItemSorterFactory.createComparator(usedSortStrategy, user,  false);
						 }
					} else {
						sortOptionData.setName(sorter.getLabel());
						sortOptionData.setOrderAscending(true);  //if option is clicked this will be the new order
					}
						
					options.add(sortOptionData);
				}
			}
			
			//check if nav.getSortBy() is a type of nutrition sorting - if applicable
			if (isNutritionSortApplicable(data, nav)) {
				List<SelectableData> dropDownOptions = new ArrayList<SelectableData>();
				
				SelectableData defaultNutritionDropDownOption = new SelectableData();
				dropDownOptions.add(defaultNutritionDropDownOption);
				defaultNutritionDropDownOption.setName("View by Nutrition");
		
				for (ErpNutritionType.Type erpNutritionTypeType : ErpNutritionType.getCommonList()){ //based on grocery_product.jsp:989
					SelectableData dropDownOption = new SelectableData();
					dropDownOptions.add(dropDownOption);
					
					String name = StringUtils.replace(erpNutritionTypeType.getDisplayName(), " quantity", "");
					if(erpNutritionTypeType.getUom().equals("%")){
			        	name = name + " % daily value";
					}
					dropDownOption.setName(name);
					dropDownOption.setId(erpNutritionTypeType.getName());

					String nutritionName = nav.getSortBy();
					if (erpNutritionTypeType.getName().equalsIgnoreCase(nutritionName)){
						
						if (nutritionName.equals(ErpNutritionType.SERVING_SIZE)) {
							comparator = ProductItemSorterFactory.createNutritionComparator(ErpNutritionType.getType(ErpNutritionType.SERVING_WEIGHT)); //based on LayoutManager:209
						} else {
							comparator = ProductItemSorterFactory.createNutritionComparator(erpNutritionTypeType);
						}

						dropDownOption.setSelected(true);
						defaultNutritionDropDownOption.setName("Default View");
						nav.setErpNutritionTypeType(erpNutritionTypeType); //will be used by ProductDetailPopulator.populateSelectedNutritionFields()
					}
				}
				
				SortDropDownData nutritionSortData = new SortDropDownData();
				nutritionSortData.setOptions(dropDownOptions);
				List<SortDropDownData> sortDropDowns = new ArrayList<SortDropDownData>();
				sortDropDowns.add(nutritionSortData);
				data.getSortOptions().setSortDropDowns(sortDropDowns);
			}
			
			if (comparator==null){
				comparator = ProductItemSorterFactory.createDefaultComparator();
			}
			
			data.getSortOptions().setSortOptions(options); // create sort bar objects
			
			ProductItemComparatorUtil.sortSectionDatas(data, comparator); // sort items/section
			ProductItemComparatorUtil.postProcess(data, nav, usedSortStrategy, user);
			//logSortResult(data, user);
		}
	}

	@SuppressWarnings("unused")
	private static void logSortResult(BrowseDataContext data, FDUserI user){
		List<FilteringSortingItem<ProductModel>> items = new ArrayList<FilteringSortingItem<ProductModel>>();
		for (FilteringProductItem p : data.getSectionContexts().get(0).getProductItems()){
			items.add(p.getSearchResult());
		}
		FilteringComparatorUtil.logSortResult(items, user);
	}
	
	private List<SortOptionModel> getSortersForCurrentFlow(BrowseDataContext data, CmsFilteringNavigator nav){
		List<SortOptionModel> sorters = null;
		
		if (data.getNavigationModel().isProductListing()){
			switch (nav.getPageType()){
				case BROWSE:
					if (data.getCurrentContainer() instanceof ProductContainer){
						sorters = ((ProductContainer)data.getCurrentContainer()).getSortOptions();
					}
					break;
			
				case ECOUPON:
					sorters = ContentFactory.getInstance().getStore().getECouponsPageSortOptions();
					break;
			
				case NEWPRODUCTS:
					sorters = ContentFactory.getInstance().getStore().getNewProductsPageSortOptions();
					break;
			
				case PRES_PICKS:
					sorters = ContentFactory.getInstance().getStore().getPresidentsPicksPageSortOptions();
					break;
					
				case STAFF_PICKS:
					sorters = ContentFactory.getInstance().getStore().getStaffPicksPageSortOptions();
					break;
				
				case SEARCH:
					sorters = ContentFactory.getInstance().getStore().getSearchPageSortOptions();
					break;
			}
		}		
		return sorters;
	}
	
	private boolean isNutritionSortApplicable(BrowseDataContext data, CmsFilteringNavigator nav){ //this can be modified id search like pages need nutrition sort
		ContentNodeModel currentContainer = data.getCurrentContainer();
		return nav.getPageType() == FilteringFlowType.BROWSE && currentContainer instanceof ProductContainer && ((ProductContainer) currentContainer).isNutritionSort();
	}
	


	/**
	 * appends carousels using shown products if necessary
	 */
	public void appendCarousels(BrowseData browseData, BrowseDataContext browseDataContext, FDSessionUser user, Set<ContentKey> shownProductKeysForRecommender, boolean disableCategoryYmalRecommender, boolean isPdp){
		//Product Listing Page Scarab
		if (browseData.getCarousels().getCarousel1() == null && shownProductKeysForRecommender.size() > 0) {
			try {
				Recommendations recommendations = ProductRecommenderUtil.getBrowseProductListingPageRecommendations(user, shownProductKeysForRecommender);
				List<ProductModel> products = recommendations.getAllProducts();
				
				if (products.size()>0 && !disableCategoryYmalRecommender){
					browseData.getCarousels().setCarousel1(createCarouselData(null, "You Might Also Like", products, user, "", recommendations.getVariant().getId() ));
				}
			} catch (FDResourceException e) {
				LOG.error("recommendation failed",e);
			}
		}
		
		if(!isPdp){
			CategoryModel categoryModel = (CategoryModel) browseDataContext.getNavigationModel().getNavigationHierarchy().get(NavDepth.SUB_SUB_CATEGORY);
			if (categoryModel == null) {
				categoryModel = (CategoryModel) browseDataContext.getNavigationModel().getNavigationHierarchy().get(NavDepth.SUB_CATEGORY);
			}
			if (categoryModel == null) {
				categoryModel = (CategoryModel) browseDataContext.getNavigationModel().getNavigationHierarchy().get(NavDepth.CATEGORY);
			}
			if (categoryModel != null) {
				browseData.getCarousels().setCarousel2(createCarouselData(null, categoryModel.getCatMerchantRecommenderTitle(), ProductRecommenderUtil.getMerchantRecommenderProducts(categoryModel), user, EnumEventSource.CMR.getName()));
			}
		}
	}
	
	public void appendSearchLikeCarousel(BrowseData browseData, FDSessionUser user, FilteringFlowType pageType, String activeTab){
		
		try {
			switch (pageType) {
			case SEARCH : 
				if ("product".equalsIgnoreCase(activeTab) &&
					browseData.getSections().getSections() != null &&
					browseData.getSections().getSections().size() > 0 && 
					browseData.getSections().getSections().get(0).getProducts() != null &&
					browseData.getSections().getSections().get(0).getProducts().size() > 0) {
	
					Recommendations recommendations = ProductRecommenderUtil.getSearchPageRecommendations(user, browseData.getSections().getSections().get(0).getProducts().get(0));
					if (recommendations != null && recommendations.getAllProducts().size() > 0) {
						browseData.getCarousels().setCarousel1(createCarouselData(null, "You Might Also Like", recommendations.getAllProducts(), user, "", recommendations.getVariant().getId()));
					}
				}
				break;
			case PRES_PICKS:

		        SessionInput si = new SessionInput(user);
	        	si.setCurrentNode(ContentFactory.getInstance().getContentNode("gro"));
				Recommendations groRecommendations = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si);
				if (groRecommendations != null && groRecommendations.getAllProducts().size() > 0) {
					browseData.getCarousels().setCarousel1(createCarouselData(null, groRecommendations.getVariant().getServiceConfig().get("prez_title_gro"), groRecommendations.getAllProducts(), user, "", groRecommendations.getVariant().getId()));
				}
	        	si.setCurrentNode(ContentFactory.getInstance().getContentNode("fro"));
				Recommendations froRecommendations = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si);
				if (froRecommendations != null && froRecommendations.getAllProducts().size() > 0) {
					browseData.getCarousels().setCarousel2(createCarouselData(null, froRecommendations.getVariant().getServiceConfig().get("prez_title_fro"), froRecommendations.getAllProducts(), user, "", froRecommendations.getVariant().getId()));
				}
	        	si.setCurrentNode(ContentFactory.getInstance().getContentNode("dai"));
				Recommendations daiRecommendations = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si);
				if (daiRecommendations != null && daiRecommendations.getAllProducts().size() > 0) {
					browseData.getCarousels().setCarousel3(createCarouselData(null, daiRecommendations.getVariant().getServiceConfig().get("prez_title_dai"), daiRecommendations.getAllProducts(), user, "", daiRecommendations.getVariant().getId()));
				}
				break;
				
			case STAFF_PICKS:
				/* do not populate for SP */
//				SessionInput si_staffPicks = new SessionInput(user);
//				si_staffPicks.setCurrentNode(ContentFactory.getInstance().getContentNode("gro"));
//				Recommendations groRecommendations_staffPicks = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si_staffPicks);
//				if (groRecommendations_staffPicks != null && groRecommendations_staffPicks.getAllProducts().size() > 0) {
//					browseData.getCarousels().setCarousel1(createCarouselData(null, groRecommendations_staffPicks.getVariant().getServiceConfig().get("prez_title_gro"), groRecommendations_staffPicks.getAllProducts(), user, "", groRecommendations_staffPicks.getVariant().getId()));
//				}
//				si_staffPicks.setCurrentNode(ContentFactory.getInstance().getContentNode("fro"));
//				Recommendations froRecommendations_staffPicks = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si_staffPicks);
//				if (froRecommendations_staffPicks != null && froRecommendations_staffPicks.getAllProducts().size() > 0) {
//					browseData.getCarousels().setCarousel2(createCarouselData(null, froRecommendations_staffPicks.getVariant().getServiceConfig().get("prez_title_fro"), froRecommendations_staffPicks.getAllProducts(), user, "", froRecommendations_staffPicks.getVariant().getId()));
//				}
//				si_staffPicks.setCurrentNode(ContentFactory.getInstance().getContentNode("dai"));
//				Recommendations daiRecommendations_staffPicks = ProductRecommenderUtil.doRecommend(user, EnumSiteFeature.BRAND_NAME_DEALS,si_staffPicks);
//				if (daiRecommendations_staffPicks != null && daiRecommendations_staffPicks.getAllProducts().size() > 0) {
//					browseData.getCarousels().setCarousel3(createCarouselData(null, daiRecommendations_staffPicks.getVariant().getServiceConfig().get("prez_title_dai"), daiRecommendations_staffPicks.getAllProducts(), user, "", daiRecommendations_staffPicks.getVariant().getId()));
//				}
				break;
			
			default:
					break;
			}
		} catch (FDResourceException e) {
			LOG.error("recommendation failed",e);
		}

	}

	public void collectAllProductKeysForRecommender(List<SectionData> sections, Set<ContentKey> shownProductKeysForRecommender){
		
		if(sections!=null){
			for(SectionData section : sections){
				if(section.getProducts()!=null){
					for(ProductData data : section.getProducts()){
						if (shownProductKeysForRecommender.size() < ProductRecommenderUtil.MAX_LIST_CONTENT_SIZE){
							shownProductKeysForRecommender.add(ContentKey.getContentKey(FDContentTypes.PRODUCT, data.getProductId()));
						} else {
							return;
						}
					}
				}
				collectAllProductKeysForRecommender(section.getSections(), shownProductKeysForRecommender);
			}			
		}
	}
	
	public void populateWithBreadCrumbAndDesciptiveContent(BrowseDataContext browseData, NavigationModel navModel){
		
		List<BasicData> breadCrumb = new ArrayList<BasicData>();
		
		if(navModel.getSuperDepartmentModel()!=null){ // add superdepartment info to breadcrumb
			breadCrumb.add(new BasicData(navModel.getSuperDepartmentModel().getContentName(),navModel.getSuperDepartmentModel().getFullName()));			
		}
		
		for(ContentNodeModel contentNodeModel : navModel.getContentNodeModelPath()){
			if (!(contentNodeModel instanceof SuperDepartmentModel) && (!(contentNodeModel instanceof CategoryModel) || !NavigationUtil.isCategoryHiddenInContext(navModel.getUser(), (CategoryModel)contentNodeModel))) {
				breadCrumb.add(new BasicData(contentNodeModel.getContentKey().getId(), contentNodeModel.getFullName()));
			}
		}
		
		browseData.getBreadCrumbs().setBreadCrumbs(breadCrumb);
		
		BrowseData.DescriptiveDataCointainer descriptiveContent = browseData.getDescriptiveContent();
		final String pageTitle = navModel.getPageTitle().isEmpty() ? "FreshDirect - " + navModel.getSelectedContentNodeModel().getFullName() : navModel.getPageTitle();
		
		descriptiveContent.setMetaDescription(navModel.getMetaDescription());
		descriptiveContent.setPageTitle(pageTitle);
		descriptiveContent.setOasSitePage(navModel.getSelectedContentNodeModel().getPath());
	}
	
	public void populateWithFilterLabels(BrowseDataContext browseData, NavigationModel navModel){
		List<ParentData> filterLabels = new ArrayList<ParentData>();
		for(ProductItemFilterI filter : navModel.getActiveFilters()){
			filterLabels.add(new ParentData(filter.getParentId(), filter.getId(), filter.getName()));
		}
		List<ParentData> orderedFilterLabels = reorderFilterLabels(browseData, filterLabels);
		browseData.getSections().getFilterLabels().setFilterLabels(orderedFilterLabels);
	}
	
	private List<ParentData> reorderFilterLabels(BrowseDataContext browseDataContext, List<ParentData> filterLabels) {
		List<ParentData> orderedFilterLabels = new ArrayList<ParentData>();
		for (MenuBoxData menuBox : browseDataContext.getMenuBoxes().getMenuBoxes()) {
			String id = menuBox.getId();
			if (MenuBoxType.FILTER.equals(menuBox.getBoxType()) && MenuBoxSelectionType.MULTI.equals(menuBox.getSelectionType())) {
				for (MenuItemData menuItem : menuBox.getItems()) {
					if (menuItem.isSelected()) {
						for (ParentData item : filterLabels) {
							if (menuItem.getId().equals(item.getId())) {
								orderedFilterLabels.add(item);
								break;
							}
						}
					}
				}
			} else {
				for (ParentData unorderedFilterLabel : filterLabels) {
					if (id.equals(unorderedFilterLabel.getParentId())) {
						orderedFilterLabels.add(unorderedFilterLabel);
						break;
					}
				}
			}
		}
		return orderedFilterLabels;
	}
	
	public void calculateMaxSectionLevel(SectionDataCointainer data, List<SectionData> sections, int level){

		if(sections!=null){
			
			for(SectionData section : sections){
				calculateMaxSectionLevel(data, section.getSections(), level+1);		
			}
		}
		
		if(data.getSectionMaxLevel()<level){
			data.setSectionMaxLevel(level);
		}
			
	}

}
