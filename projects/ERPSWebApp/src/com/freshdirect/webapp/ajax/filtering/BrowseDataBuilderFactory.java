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
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductItemFilterI;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortOptionModel;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.fdstore.content.browse.sorter.NutritionComparator;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.data.BasicData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;
import com.freshdirect.webapp.ajax.browse.data.DescriptiveDataI;
import com.freshdirect.webapp.ajax.browse.data.MenuBoxData;
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
	
	public static BrowseDataBuilderI createBuilder(NavDepth navDepth, boolean hasSuperDeparment, boolean isSearchPage){
		
		if (isSearchPage) {
			return getInstance().new SearchPageDataBuilder();
			
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
				data.getCarousels().setCarousel1(createCarouselData(null, superDepartmentModel.getSdFeaturedRecommenderTitle(), ProductRecommenderUtil.getAggregatedSuperDepartmentFeaturedRecommenderProducts(superDepartmentModel, null), user, EnumEventSource.SDFR.getName()));
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
				data.getCarousels().setCarousel1(createCarouselData(null, department.getFeaturedRecommenderTitle(), ProductRecommenderUtil.getFeaturedRecommenderProducts(department, user, null), user, EnumEventSource.DFR.getName()));
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
							data.getCarousels().setCarousel1(createCarouselData(null, "You May Also Like", products, user, EnumEventSource.CSR.getName()));
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
			
			section.setProductItems(ProductItemFilterUtil.createFilteringProductItems(navigationModel.getSearchResults()));
			
			sections.add(section);	
			
			data.setSectionContexts(checkEmpty(sections));
			
//			appendCatDepthFields(data, subCat, sections, user, nav, true);
			
			filterProducts(navigationModel, data);
			
			return data;
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
	private void filterProducts(List<SectionContext> sections, List<FilteringProductItem> allItems, Set<ProductItemFilterI> activeFilters){
		
		Iterator<SectionContext> it = sections.iterator();
		while(it.hasNext()){
			
			SectionContext section = it.next();
			
			if(section.getProductItems()!=null && section.getProductItems().size()>0){
				// collect all items before filtering
				allItems.addAll(section.getProductItems());
				// apply filters
				section.setProductItems(ProductItemFilterUtil.getFilteredProducts(section.getProductItems(), activeFilters, true));				
			}
			
			if(section.getSectionContexts()!=null && section.getSectionContexts().size()>0){
				filterProducts(section.getSectionContexts(), allItems, activeFilters);
			}	
		}	
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
						if(!item.isActive() && section.getCatId().equals(item.getId())){
							item.setActive(false);
						}
					}
				}
			}
		}		
	}
	
	private void filterProducts(NavigationModel navigationModel, BrowseDataContext data) {
		List<FilteringProductItem> allItems = new ArrayList<FilteringProductItem>();
		filterProducts(data.getSectionContexts(), allItems, navigationModel.getActiveFilters());
		data.setUnfilteredItems(allItems);
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
		if(!nav.isPdp()){
			data.getCarousels().setCarousel2(createCarouselData(null, cat.getCatMerchantRecommenderTitle(), ProductRecommenderUtil.getMerchantRecommenderProducts(cat), user, EnumEventSource.CMR.getName()));			
		}
	}
	
	private CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDSessionUser user, String cmEventSource){

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
	public void processSorting (BrowseDataContext data, CmsFilteringNavigator nav){
		
		if (data.getCurrentContainer() instanceof ProductContainer) {
			ProductContainer currentContainer = ((ProductContainer)data.getCurrentContainer());
			List<SortOptionModel> sorters = currentContainer.getSortOptions();
			NavigationModel navigationModel = data.getNavigationModel();
			
			if (navigationModel.isProductListing()){
				List<SortOptionData> options = new ArrayList<SortOptionData>();
				Comparator<FilteringProductItem> comparator = null;
				
				//set default sort option from first
				if (nav.getSortBy()==null && sorters.size()>0){
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
							 
							 if (!nav.isOrderAscending() && selectedLaberReverseOrder != null && !"".equals(selectedLaberReverseOrder)) {
								sortOptionData.setName(sorter.getSelectedLabelReverseOrder());
								sortOptionData.setOrderAscending(true); //if option is clicked this will be the new order
								comparator = ProductItemSorterFactory.createComparator(sorter.getSortStrategyType(), true);
							 } else {
								sortOptionData.setName(selectedLabel == null || "".equals(selectedLabel) ? sorter.getLabel() : selectedLabel);
								comparator = ProductItemSorterFactory.createComparator(sorter.getSortStrategyType(), false);
							 }
						} else {
							sortOptionData.setName(sorter.getLabel());
							sortOptionData.setOrderAscending(true);  //if option is clicked this will be the new order
						}
							
						options.add(sortOptionData);
					}
				}
				
				//check if nav.getSortBy() is a type of nutrition sorting - if applicable
				if (currentContainer.isNutritionSort()) {
					List<SelectableData> dropDownOptions = new ArrayList<SelectableData>();
					
					SelectableData defaultNutritionDropDownOption = new SelectableData();
					dropDownOptions.add(defaultNutritionDropDownOption);
					defaultNutritionDropDownOption.setName("Nutrition");
			
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
								comparator = new NutritionComparator(ErpNutritionType.getType(ErpNutritionType.SERVING_WEIGHT)); //based on LayoutManager:209
							} else {
								comparator = new NutritionComparator(erpNutritionTypeType);
							}
							comparator = ProductItemSorterFactory.wrapComparator(comparator);

							dropDownOption.setSelected(true);
							defaultNutritionDropDownOption.setName("Default");
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
					comparator=ProductItemSorterFactory.AVAILABILITY_INNER;
				}
				
				data.getSortOptions().setSortOptions(options); // create sort bar objects
				
				ProductItemComparatorUtil.sortSectionDatas(data, comparator); // sort items/section
			}
		}
	}

	/**
	 * appends carousels using shown products if necessary
	 */
	public void appendCarousels(BrowseData browseData, FDSessionUser user, Set<ContentKey> shownProductKeysForRecommender, boolean disableCategoryYmalRecommender){
		
		//Product Listing Page Scarab
		if (browseData.getCarousels().getCarousel1() == null && shownProductKeysForRecommender.size() > 0) {
			try {
				Recommendations recommendations = ProductRecommenderUtil.getBrowseProductListingPageRecommendations(user, shownProductKeysForRecommender);
				List<ProductModel> products = recommendations.getAllProducts();
				
				if (products.size()>0 && !disableCategoryYmalRecommender){
					browseData.getCarousels().setCarousel1(createCarouselData(null, "You Might Also Like", products, user, EnumEventSource.CSR.getName()));
				}
			} catch (FDResourceException e) {
				LOG.error("recommendation failed",e);
			}
		}
	}
	
	public void collectAllProductKeysForRecommender(List<SectionData> sections, Set<ContentKey> shownProductKeysForRecommender){
		
		if(sections!=null){
			for(SectionData section : sections){
				if(section.getProducts()!=null){
					for(ProductData data : section.getProducts()){
						if (shownProductKeysForRecommender.size() < ProductRecommenderUtil.MAX_LIST_CONTENT_SIZE){
							shownProductKeysForRecommender.add(new ContentKey(FDContentTypes.PRODUCT, data.getProductId()));
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
		descriptiveContent.setPageTitle("FreshDirect - " + navModel.getSelectedContentNodeModel().getFullName());
		descriptiveContent.setOasSitePage(navModel.getSelectedContentNodeModel().getPath());
	}
	
	public void populateWithFilterLabels(BrowseDataContext browseData, NavigationModel navModel){
		
		List<ParentData> filterLabels = new ArrayList<ParentData>();
		for (MenuBoxData menuBox : navModel.getLeftNav()){
			if (menuBox!=null && menuBox.getBoxType()==MenuBoxType.FILTER){
				for (MenuItemData menuItem : menuBox.getItems()){
					if (menuItem.isSelected()){
						filterLabels.add(new ParentData(menuBox.getId(), menuItem.getId(), menuItem.getName()));
					}
				}
			}
		}
		browseData.getSections().getFilterLabels().setFilterLabels(filterLabels);
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
